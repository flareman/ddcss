package ddchannel;

import java.io.PrintWriter;
import java.util.ArrayList;

public class Processor extends Thread {
    private ArrayList<Request> requests = new ArrayList<Request>();
    private boolean shutdownRequested = false;
    private DDChannel ddchannel;
    private Mutex mxModify = new Mutex();
    
    public Processor(DDChannel ddc) {
        this.ddchannel = ddc;
    }
    
    public boolean addRequest(Request aRequest) {
        if (shutdownRequested) return false;
        while (true) {
                try { this.mxModify.lock(); } catch (InterruptedException e) { continue; }
                break;
        }
        requests.add(aRequest);
        this.interrupt();
        this.mxModify.raise();
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
                    ArrayList<DummyBS> requestResponse = this.ddchannel.listOfBSForCoords(head.getX(), head.getY());
                    this.ddchannel.printMessage("Request from "+head.getIMEI()+" for coords "+head.getX()+", "+head.getY());
                    this.ddchannel.printMessage("Returning "+requestResponse.size()+" stations to IMEI "+head.getIMEI());
                    PrintWriter sockOut = new PrintWriter(head.getSocket().getOutputStream(),true);
                    sockOut.println((new ProfilesMessage(requestResponse)).toString());
                    sockOut.close();
                    head.closeSockIn();
                    head.getSocket().close();
                    requests.remove(0);
                } catch (Exception e) { this.ddchannel.printMessage(e.getLocalizedMessage()); }
            }
            if (shutdownRequested) break;
            if (!Thread.interrupted())
                synchronized(Thread.currentThread()) {
                    try { wait(); } catch (InterruptedException e) {}
                }
        }
    }
}
