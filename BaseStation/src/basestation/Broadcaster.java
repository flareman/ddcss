package basestation;

public class Broadcaster implements Runnable {
    private BaseStationEntity baseStation;
    private Integer keepAliveMsecs;

    public Broadcaster(BaseStationEntity parent) {
        this.baseStation = parent;
        this.keepAliveMsecs = parent.getKeepAlive();
    }
    
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(keepAliveMsecs);
                Broadcaster.updateProfile(new BSProfileMessage(baseStation).toString());
                this.baseStation.println("Profile updated.");
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                this.baseStation.println(e.getLocalizedMessage());
            }
        }
    }

    private static void updateProfile(java.lang.String arg0) {
        ddchannel.WebServicePublisherService service = new ddchannel.WebServicePublisherService();
        ddchannel.WebServicePublisher port = service.getWebServicePublisherPort();
        port.updateProfile(arg0);
    }

}
