package ddchannel;

public class Reaper extends Thread {
    private DDChannel ddchannel;
    private boolean shutdownRequested;
    
    public void requestShutdown() { this.shutdownRequested = true; }
    
    public Reaper(DDChannel ddc) {
        this.ddchannel = ddc;
    }
    
    @Override
    public void run() {
        this.shutdownRequested = false;
        
        while (!shutdownRequested) {
            try {
                long sleepInterval = this.ddchannel.nextReapInterval();
                if (sleepInterval == -1) synchronized (this) { wait(); }
                else sleep(sleepInterval);
            } catch (InterruptedException e) {}
            if (this.ddchannel.nextReapInterval() > 0) continue;
            if (shutdownRequested) break;
            this.ddchannel.reapDatabase();
        }
    }
}
