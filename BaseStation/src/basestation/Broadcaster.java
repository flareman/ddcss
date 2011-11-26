package basestation;

public class Broadcaster implements Runnable {
    private BaseStationEntity baseStation;
    private Integer keepAliveMsecs;

    private static String ddcUpdateProfile(java.lang.String message) {
        DDCclient.DDCProfile_Service service = new DDCclient.DDCProfile_Service();
        DDCclient.DDCProfile port = service.getDDCProfilePort();
        return port.ddcUpdateProfile(message);
    }

    public Broadcaster(BaseStationEntity parent) {
        this.baseStation = parent;
        this.keepAliveMsecs = parent.getKeepAlive();
    }
    
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(keepAliveMsecs);
                String response = Broadcaster.ddcUpdateProfile(new BSProfileMessage(baseStation).toString());
                BSMessage rmsg = BSMessage.newMessageFromString(response);
                if (rmsg.type() != BSMessage.MessageType.OK) throw new Exception("Error: DDC replied \""+rmsg.toString()+"\"");
                this.baseStation.println("Profile updated; DDC replied \""+rmsg.toString()+"\"");
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                this.baseStation.println(e.getLocalizedMessage());
            }
        }
    }    
}
