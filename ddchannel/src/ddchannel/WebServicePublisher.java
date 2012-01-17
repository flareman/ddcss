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
        try {
            msg = (UpdateMessage)Message.newMessageFromString(input);
            ddchannel.processUpdate(msg);
            ddchannel.printMessage("Update received from basestation "+msg.getNetworkID()+".");
        } catch (Exception e) { e.printStackTrace(); this.ddchannel.printMessage(e.getLocalizedMessage()); }
    }
}