package terminal;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;
import javax.microedition.lcdui.*;

public class ChannelConnection implements Runnable {
    private TerminalMIDlet parent = null;
    private Display display;
    private int refreshInteval;
    private SocketConnection socket = null;
    private BufferedReader istream = null;
    private PrintStream ostream = null;
    private boolean continuePolling = false;
    private Vector availableBaseStations = new Vector();
    private String address;
    private int port;
    private final Object mutex = new Object();
    
    public ChannelConnection(TerminalMIDlet parent, int refreshInteval, String address, int port) {
        this.parent = parent;
        this.display = parent.getDisplay();
        this.refreshInteval = refreshInteval;
        this.address = address;
        this.port = port;
    }
    
    public void terminate() {
        this.continuePolling = false;
        Thread.currentThread().interrupt();
    }
    
    public Vector getBaseStations() {
        synchronized(mutex) {
            return this.availableBaseStations;
        }
    }
    
    public void run() {
        this.continuePolling = true;
        while (this.continuePolling) {
            try {
                try { Thread.sleep(refreshInteval); } catch (InterruptedException e) { }
                if (!this.continuePolling) break;
                try { this.socket = (SocketConnection)Connector.open("socket://"+this.address+":"+this.port); }
                catch (IOException e) { e.printStackTrace(); continue; }
                this.ostream = new PrintStream(this.socket.openOutputStream());
                this.istream = new BufferedReader(new InputStreamReader(this.socket.openInputStream()));
                this.istream.setInterruptible(false);
                this.ostream.println(new DiscoverMessage(this.parent.getIMEI(), this.parent.getX(), this.parent.getY()));
                String response = this.istream.readLine();
                Message msg = Message.newMessageFromString(response);
                if (!msg.type().equals("PROFILES")) throw new Exception("Diffusion channel did not send base station profiles over.");
                synchronized(this.mutex) {
                    this.availableBaseStations.removeAllElements();
                    for (int i = 0; i < ((ProfilesMessage)msg).getBaseStations().size(); i++) {
                        DummyBS bs = (DummyBS)((ProfilesMessage)msg).getBaseStations().elementAt(i);
                        if (this.parent.getNetworkCapabilities().indexOf(bs.getType()) == -1) continue;
                        this.availableBaseStations.addElement(bs);
                    }
                }
                this.istream.close();
                this.ostream.close();
                this.socket.close();
                this.istream = null;
                this.ostream = null;
                this.socket = null;
            } catch (InterruptedException ie) {
                continue;
            } catch (IOException IOe) {
                try {
                    Alert alert = new Alert("I/O Error", IOe.getMessage(), null, AlertType.ERROR);
                    alert.setTimeout(Alert.FOREVER);
                    this.display.setCurrent(alert);
                    this.istream.close();
                    this.ostream.close();
                    this.socket.close();
                    this.istream = null;
                    this.ostream = null;
                    this.socket = null;
                } catch (Exception inE) {}
            } catch (Exception e) {
                try {
                    Alert alert = new Alert("Error", e.getMessage(), null, AlertType.ERROR);
                    alert.setTimeout(Alert.FOREVER);
                    this.display.setCurrent(alert);
                    e.printStackTrace();
                    this.istream.close();
                    this.ostream.close();
                    this.socket.close();
                    this.istream = null;
                    this.ostream = null;
                    this.socket = null;
                } catch (Exception inE) {}
            }
        }
    }
}
