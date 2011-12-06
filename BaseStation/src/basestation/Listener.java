package basestation;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;

public class Listener implements Runnable {
    private Integer port;
    private BaseStationEntity theParent;
    private Boolean interrupted;
    
    public Listener(BaseStationEntity bs) {
        this.theParent = bs;
        this.port = bs.getPort();
    }
    
    public void run() {
        try{
            ServerSocket listenSocket = new ServerSocket(this.port.intValue());
            listenSocket.setSoTimeout(1000);
            while(!Thread.currentThread().isInterrupted()){
                Socket newClient = null;
                try {
                    newClient = listenSocket.accept();
                } catch (SocketTimeoutException e) { continue; }
                BufferedReader sockIn = new BufferedReader(new InputStreamReader(newClient.getInputStream()));
                String input = sockIn.readLine();
                BSMessage message = null;
                try {
                    message = BSMessage.newMessageFromString(input);
                } catch (Exception e) {
                    this.theParent.println(e.getLocalizedMessage());
                    continue;
                }
                if (message.type() != BSMessage.MessageType.CONNECT) {
                    this.theParent.println("Expected CONNECT message; closing socket.");
                    PrintWriter sockOut = new PrintWriter(newClient.getOutputStream(),true);
                    sockOut.println((new BSErrorMessage()).toString());
                    sockOut.close();
                    newClient.close();
                    continue;
                }
                if (this.theParent.getCurrentLoad() >= 90){ //overloaded check
                    PrintWriter sockOut = new PrintWriter(newClient.getOutputStream(),true);
                    sockOut.println((new BSOverloadedMessage()).toString());
                    sockOut.close();
                    newClient.close();
                    continue;
                }
                BSConnectMessage cmsg = (BSConnectMessage)message;
                Double distance = Math.abs(Math.sqrt((Math.pow(cmsg.getLongtitude() - this.theParent.getLongtitude(),2) + (Math.pow(cmsg.getLatitude() - this.theParent.getLatitude(),2)))));
                Float dist = distance.floatValue(); //Distance calculations
                if (theParent.getRange()< dist) { //out of range check
                    PrintWriter sockOut = new PrintWriter(newClient.getOutputStream(),true);
                    sockOut.println((new BSNoCoverageMessage()).toString());
                    sockOut.close();
                    sockIn.close();
                    newClient.close();
                    continue;
                }
                Processor proc = new Processor(cmsg.getIMEI(), newClient, theParent);
                this.theParent.addSubscriber(cmsg.getIMEI(), cmsg.getIMSI(), cmsg.getLongtitude(), cmsg.getLatitude(), proc);
                proc.start(); // processor thread start
            }
            listenSocket.close();
        }
        catch (UnknownHostException e) {
            this.theParent.println(("Cannot find host provided : "+e.getLocalizedMessage()));
        }
        catch (IOException e2) {
            this.theParent.println(("Cannot perform IO operation : "+e2.getLocalizedMessage()));
        }
    }
}
