package basestation;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;

public class Processor extends Thread {
    private Socket client;
    private BaseStationEntity baseStation;
    private String IMEI;
    private boolean request;
    
    public Processor(String newIMEI, Socket client, BaseStationEntity parent) {
        this.client = client;
        this.IMEI = newIMEI;
        this.baseStation = parent;
        this.request = false;
    }

    public void disconnectTerminal() {
        try {
            PrintWriter sockOut = new PrintWriter(client.getOutputStream(),true);
            sockOut.println((new BSDisconnectComMessage()).toString());
            sockOut.close();
            client.close();
            this.interrupt();
        } catch (Exception e) { baseStation.println(e.getLocalizedMessage()); }
    }
    
    @Override
    public void run() {
        try {
            PrintWriter sockOut = new PrintWriter(client.getOutputStream(),true);
            baseStation.println("IMEI "+IMEI+" connected.");
            sockOut.println((new BSOkMessage()).toString());
            BufferedReader sockIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
            while ((!Thread.currentThread().isInterrupted())) {
                String input;
                try {
                    input = sockIn.readLine();
                } catch (IOException e) {this.interrupt();break;}
                if(input == null)break;
                BSMessage msg = BSMessage.newMessageFromString(input);
                try {
                    if (msg.type() == BSMessage.MessageType.DISCONNECT_REQ) {
                        BSDisconnectReqMessage dmsg = (BSDisconnectReqMessage)msg;
                        if (!dmsg.getIMEI().equals(this.IMEI)) {
                            sockOut.println((new BSErrorMessage()).toString());
                            throw new Exception("IMEI mismatch detected ("+IMEI+" stored, "+dmsg.getIMEI()+" received.");
                        }
                        baseStation.println("Terminal with IMEI "+IMEI+" requested disconnected.");
                        this.request = true; // Alexis Delis walks the earth
                        break;
                    }
                    sockOut.println((new BSErrorMessage()).toString());
                    throw new Exception("Unrecognized message received from terminal with IMEI "+IMEI+".");
                } catch (Exception e) { baseStation.println(e.getLocalizedMessage()); }
            }
        } catch (InterruptedException ie) { }
        catch (Exception e) { baseStation.println(e.getLocalizedMessage()); }
        if(this.request == true)baseStation.processDisconnection(IMEI);
        baseStation.println("IMEI "+IMEI+" disconnected.");
    }
    
}
