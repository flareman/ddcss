package ddchannel;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public class WebServicePublisher {
    @WebMethod
    @Oneway
    public void updateProfile(@WebParam String input) {
        System.out.println(input); // Process the incoming updates here
    }
}