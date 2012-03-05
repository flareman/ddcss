package terminal;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;
import javax.microedition.lcdui.*;

public class StationConnection implements Runnable {
    private DummyBS baseStation;
    private TerminalMIDlet parent;
    private Display display;
    private SocketConnection socket = null;
    private BufferedReader istream = null;
    private PrintStream ostream = null;
    private boolean connected = false;
    private boolean continuePolling = false;
    
    public StationConnection(TerminalMIDlet parent, DummyBS baseStation) {
        this.parent = parent;
        this.display = parent.getDisplay();
        this.baseStation = baseStation;
    }
    
    private boolean connect() throws Exception {
        if (this.socket != null) return true;
        this.connected = false;
        this.socket = (SocketConnection)Connector.open("socket://"+this.baseStation.getAddress()+":"+this.baseStation.getPort().toString());
        this.istream = new BufferedReader(new InputStreamReader(this.socket.openInputStream()));
        this.ostream = new PrintStream(this.socket.openOutputStream());
        this.ostream.println(new ConnectMessage(this.parent.getIMEI(), this.parent.getIMSI(), this.parent.getX(), this.parent.getY()));
        String msg = this.istream.readLine();
        Message baseStationResponse = Message.newMessageFromString(msg);
        if (baseStationResponse.type().equals("OK")) {
            this.connected = true;
            return true;
        }
        else return false;
    }
    
    private void disconnect() throws Exception {
        if (this.socket == null) return;
        this.ostream.close();
        this.istream.close();
        this.socket.close();
        this.ostream = null;
        this.istream = null;
        this.socket = null;
        this.connected = false;
        this.parent.getTicker().setString("Disconnected from network");
    }
    
    public void terminate() {
        if (!this.connected) return;
        this.ostream.println(new DisconnectReqMessage(this.parent.getIMEI()));
        Thread.currentThread().interrupt();
        this.istream.interrupt();
        this.continuePolling = false;
    }

    public void run() {
        try {
            if (!this.connect())
                return;
            this.parent.getTicker().setString("Connected to "+this.baseStation.getSSID());
            this.continuePolling = true;
            while (continuePolling) {
                try {
                    String str = "";
                    while ((str = this.istream.readLine()) != null) {
                        if (!continuePolling) break;
                        Message msg = Message.newMessageFromString(str);
                        if (msg.type().equals("DISCONNECT")) { continuePolling = false; break; }
                    }
                } catch (InterruptedException e) {
                    this.continuePolling = false;
                } catch (IOException e) {
                    Alert alert = new Alert("I/O error while communicating with station", e.getMessage(), null, AlertType.ERROR);
                    alert.setTimeout(Alert.FOREVER);
                    this.display.setCurrent(alert);
                    this.continuePolling = false;
                    break;
                } catch (Exception e) {
                    Alert alert = new Alert("Error while updating", e.getMessage(), null, AlertType.ERROR);
                    alert.setTimeout(Alert.FOREVER);
                    this.display.setCurrent(alert);
                }
            }
            this.disconnect();
        } catch (Exception e) {
            Alert alert = new Alert("Error while (dis)connecting", e.getMessage(), null, AlertType.ERROR);
            alert.setTimeout(Alert.FOREVER);
            this.display.setCurrent(alert);
        }
    }
    
    public boolean isConnected() { return this.connected; }
    
    public DummyBS getBaseStation() { return this.baseStation; }
}
