package ddchannel;

import java.io.PrintWriter;
import java.util.ArrayList;

public class Processor extends Thread {
    private ArrayList<Request> requests = new ArrayList<Request>();
    private boolean shutdownRequested = false;
    private DDChannel ddchannel;
    
    public Processor(DDChannel ddc) {
        this.ddchannel = ddc;
    }
    
    public boolean addRequest(Request aRequest) {
        if (shutdownRequested) return false;
        requests.add(aRequest);
        return true;
    }
    
    public void requestShutdown() { this.shutdownRequested = true; }
    
    @Override
    public void run() {
        this.shutdownRequested = false;
        while (true) {
            while (requests.size() > 0) {
                try {
                    Request head = this.requests.get(0);
                    Object lista = this.ddchannel.listOfBSForCoords(head.getX(), head.getY()); // request valid list of base stations
                    PrintWriter sockOut = new PrintWriter(head.getSocket().getOutputStream(),true);
                    sockOut.println((new ProfilesMessage(lista)).toString());
                    sockOut.close();
                    head.closeSockIn();
                    head.getSocket().close();
                    requests.remove(0);
                }
                catch (Exception e) { this.ddchannel.printMessage(e.getLocalizedMessage()); }
            }
            if (shutdownRequested) break;
            if (!Thread.interrupted())
                synchronized(Thread.currentThread()) {
                    try { wait(); } catch (InterruptedException e) {}
                }
        }
    }
}
