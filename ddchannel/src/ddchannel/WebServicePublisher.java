package ddchannel;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public class WebServicePublisher {
    private DDChannel ddchannel;
    
    public WebServicePublisher(DDChannel ddc) {
        this.ddchannel = ddc;
    }
    
    @WebMethod
    @Oneway
    public void updateProfile(@WebParam String input) {
        UpdateMessage msg = null;
        ddchannel.printMessage("Received update from a basestation");
        try {
            msg = (UpdateMessage)Message.newMessageFromString(input);
            ddchannel.processUpdate(msg);
            ddchannel.printMessage("Update from basestation "+msg.getNetworkID()+" complete.");
        } catch (Exception e) { e.printStackTrace(); this.ddchannel.printMessage(e.getLocalizedMessage()); }
    }
}