package terminal;

import java.io.InputStreamReader;
import java.io.PrintStream;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;
import javax.microedition.lcdui.*;

public class StationConnection implements Runnable {
    private Display display;
    private String address;
    private int port;
    private int refreshInteval;
    private boolean continuePolling = false;
    private TerminalMIDlet parent;
    private SocketConnection socket = null;
    private BufferedReader istream = null;
    private PrintStream ostream = null;
    private boolean connected = false;
    
    public StationConnection(TerminalMIDlet parent, String address, int port, int refreshInteval) {
        this.parent = parent;
        this.display = parent.getDisplay();
        this.address = address;
        this.port = port;
        this.refreshInteval = refreshInteval;
    }
    
    private boolean connect() throws Exception {
        if (this.socket != null) return true;
        this.connected = false;
        this.socket = (SocketConnection)Connector.open("socket://"+this.address+":"+this.port);
        this.istream = new BufferedReader(new InputStreamReader(this.socket.openInputStream()));
        this.ostream = new PrintStream(this.socket.openOutputStream());
        this.ostream.println(new ConnectMessage(this.parent.getIMEI(), this.parent.getIMSI(), this.parent.getX(), this.parent.getY()));
        Message baseStationResponse = Message.newMessageFromString(this.istream.readLine());
        if (baseStationResponse.type().equals("OK")) {
            this.connected = true;
            return true;
        }
        else return false;
    }
    
    private void disconnect() throws Exception {
        if (this.socket == null) return;
        this.ostream.println(new DisconnectReqMessage(this.parent.getIMEI()));
        this.ostream.close();
        this.istream.close();
        this.socket.close();
        this.ostream = null;
        this.istream = null;
        this.socket = null;
        this.connected = false;
    }

    public void run() {
        try {
            if (!this.connect()) return;
            this.continuePolling = true;
            while (continuePolling) {
                try {
                    Thread.sleep(this.refreshInteval);
                } catch (InterruptedException e) {
                    this.continuePolling = false;
                } catch (Exception e) {
                    Alert alert = new Alert("Error while updating", e.getMessage(), null, AlertType.ERROR);
                    alert.setTimeout(Alert.FOREVER);
                    this.display.setCurrent(alert);
                }
            }
            this.disconnect();
            Alert alert = new Alert("Disconnected", "Disconnected from base station "+this.address+"!", null, AlertType.INFO);
            alert.setTimeout(Alert.FOREVER);
            this.display.setCurrent(alert);
        } catch (Exception e) {
            Alert alert = new Alert("Error while (dis)connecting", e.getMessage(), null, AlertType.ERROR);
            alert.setTimeout(Alert.FOREVER);
            this.display.setCurrent(alert);
        }
    }
    
    public boolean isConnected() { return this.connected; }
}
