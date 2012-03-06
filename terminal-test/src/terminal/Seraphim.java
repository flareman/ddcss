package terminal;

import java.util.Enumeration;
import java.util.Vector;

public class Seraphim implements Runnable {
    private boolean activeConnections = false;
    private boolean keepRunning = false;
    private int sleepInterval;
    private ChannelConnection channel = null;
    private TerminalMIDlet parent = null;
    private Thread theThread = null;
    
    public Seraphim(TerminalMIDlet parent, ChannelConnection channel, int sleepInterval) {
        this.parent = parent;
        this.sleepInterval = sleepInterval;
        this.channel = channel;
    }
    
    public void setActiveState(boolean b) { this.activeConnections = b; }
    
    public void setThread(Thread t) { this.theThread = t; }
    
    public void terminate() {
        this.keepRunning = false;
        this.theThread.interrupt();
    }
    
    public void run() {
        this.keepRunning = true;
        while (keepRunning) {
            Vector list = null;
            try { Thread.sleep(this.sleepInterval); } catch (InterruptedException e) {}
            if (this.parent.isConnectedToStation()) {
                list = this.channel.getBaseStations();
                boolean outOfRange = true;
                for (Enumeration e = list.elements(); e.hasMoreElements();)
                    if (((DummyBS)e.nextElement()).getSSID().equals(this.parent.getConnectedBaseStationSSID()))
                        outOfRange = false;
                if (outOfRange)
                    this.parent.disconnectFromStation(false);
            }
            if (!(this.parent.isConnectedToStation()) && (this.activeConnections)) {
                if (list == null) list = this.channel.getBaseStations();
                if (list.size() == 0) {
                    this.parent.disconnectFromStation(false);
                    this.parent.getTicker().setString("Disconnected from network");
                }
                DummyBS baseStation = null;
                for (Enumeration e = list.elements(); e.hasMoreElements();) {
                    DummyBS temp = (DummyBS)e.nextElement();
                    if (temp.getCharges().equals(this.parent.getPreferredChargeModel()))
                        if ((baseStation == null) ||
                        (temp.getPower().intValue() > baseStation.getPower().intValue()))
                            baseStation = temp;
                }
                if (baseStation != null)
                    this.parent.connectToStation(baseStation.getSSID());
            }
        }
    }
    
}
