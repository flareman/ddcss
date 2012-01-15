package ddchannel;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public class WebServicePublisher {
    private DDChannel ddchannel;
    
    public WebServicePublisher(DDChannel ddc) {
        this.ddchannel = ddc;
    }
    
    @WebMethod
    @WebResult public String updateProfile(@WebParam String input) {
        UpdateMessage msg = null;
        try {
            msg = (UpdateMessage)Message.newMessageFromString(input);
            ddchannel.processUpdate(msg);
        } catch (Exception e) { this.ddchannel.printMessage(e.getLocalizedMessage()); return new ErrorMessage().toString();}
        return new OKMessage().toString();
    }
}