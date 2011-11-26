package basestation;

public class Broadcaster implements Runnable {
    private BaseStationEntity baseStation;
    private Integer keepAliveMsecs;
    // Diffusion channel web service server needed
    
    public Broadcaster(BaseStationEntity parent) {
        this.baseStation = parent;
        this.keepAliveMsecs = parent.getKeepAlive();
    }
    
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(keepAliveMsecs);
                this.baseStation.println(new BSProfileMessage(baseStation).toString());
                String response = "OK";
                BSMessage rmsg = BSMessage.newMessageFromString(response);
                if (rmsg.type() != BSMessage.MessageType.OK) throw new Exception("Error: DDC replied \""+rmsg.toString()+"\"");
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                this.baseStation.println(e.getLocalizedMessage());
            }
        }
    }
    
}
