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
    private Thread theThread = null;
    
    public StationConnection(TerminalMIDlet parent, DummyBS baseStation) {
        this.parent = parent;
        this.display = parent.getDisplay();
        this.baseStation = baseStation;
    }
    
    public void setThread(Thread t) { this.theThread = t; }
    
    private boolean connect() throws Exception {
        if (this.socket != null) return true;
        this.connected = true;
        this.socket = (SocketConnection)Connector.open("socket://"+this.baseStation.getAddress()+":"+this.baseStation.getPort().toString());
        this.istream = new BufferedReader(new InputStreamReader(this.socket.openInputStream()));
        this.ostream = new PrintStream(this.socket.openOutputStream());
        this.ostream.println(new ConnectMessage(this.parent.getIMEI(), this.parent.getIMSI(), this.parent.getX(), this.parent.getY()));
        String msg = this.istream.readLine();
        Message baseStationResponse = Message.newMessageFromString(msg, "");
        if (baseStationResponse.type().equals("OK"))
            return true;
        else {
            this.connected = false;
            return false;
        }
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
        if (this.ostream != null)
            this.ostream.println(new DisconnectReqMessage(this.parent.getIMEI()));
        if (this.istream != null)
            this.istream.interrupt();
        this.theThread.interrupt();
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
                        Message msg = Message.newMessageFromString(str, "");
                        if (msg.type().equals("DISCONNECT")) { continuePolling = false; break; }
                    }
                } catch (InterruptedException e) {
                    this.continuePolling = false;
                } catch (IOException e) {
                    this.continuePolling = false;
                    break;
                } catch (Exception e) {
                }
            }
            this.disconnect();
        } catch (Exception e) {}
    }
    
    public boolean isConnected() { return this.connected; }
    
    public DummyBS getBaseStation() { return this.baseStation; }
}
