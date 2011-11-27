package basestation;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;

public class Processor implements Runnable {
    private Socket client;
    private BaseStationEntity baseStation;
    private String IMEI;
    
    public Processor(String newIMEI, Socket client, BaseStationEntity parent) {
        this.client = client;
        this.IMEI = newIMEI;
        this.baseStation = parent;
    }
    
    public void run() {
        try {
            PrintWriter sockOut = new PrintWriter(client.getOutputStream(),true);
            baseStation.println("IMEI "+IMEI+" connected.");
            sockOut.println((new BSOkMessage()).toString());
            BufferedReader sockIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
            while (!Thread.currentThread().isInterrupted()) {
                String input = sockIn.readLine();
                BSMessage msg = BSMessage.newMessageFromString(input);
                try {
                    if (msg.type() == BSMessage.MessageType.DISCONNECT_REQ) {
                        BSDisconnectReqMessage dmsg = (BSDisconnectReqMessage)msg;
                        if (!dmsg.getIMEI().equals(this.IMEI)) {
                            sockOut.println((new BSErrorMessage()).toString());
                            throw new Exception("IMEI mismatch detected ("+IMEI+" stored, "+dmsg.getIMEI()+" received.");
                        }
                        break;
                    }
                    sockOut.println((new BSErrorMessage()).toString());
                    throw new Exception("Unrecognized message received from terminal with IMEI "+IMEI+".");
                } catch (Exception e) { baseStation.println(e.getLocalizedMessage()); }
            }
            if (Thread.currentThread().isInterrupted()) sockOut.println((new BSDisconnectComMessage()).toString());
            baseStation.println("IMEI "+IMEI+" disconnected.");
            sockIn.close();
            sockOut.close();
            client.close();
        } catch (Exception e) {
            baseStation.println(e.getLocalizedMessage());
        }
        baseStation.processDisconnection(IMEI);
    }
    
}
