package ddchannel;

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
    private DDChannel ddchannel;
    private Boolean shutdownRequested;
    
    public Listener(Integer listenPort, DDChannel ddc) {
        this.port = listenPort;
        this.ddchannel = ddc;
    }
    
    @Override
    public void run() {
        shutdownRequested = false;
        try{
            ServerSocket listenSocket = new ServerSocket(this.port.intValue());
            listenSocket.setSoTimeout(1000);
            while (!shutdownRequested) {
                if (Thread.interrupted()) { shutdownRequested = true; continue; }
                Socket newClient = null;
                try { newClient = listenSocket.accept(); } catch (SocketTimeoutException e) { continue; }
                BufferedReader sockIn = new BufferedReader(new InputStreamReader(newClient.getInputStream()));
                String input = sockIn.readLine();
                Message message = null;
                try { message = Message.newMessageFromString(input); } catch (Exception e) {
                    this.ddchannel.printMessage(e.getLocalizedMessage());
                    PrintWriter sockOut = new PrintWriter(newClient.getOutputStream(),true);
                    sockOut.println((new ErrorMessage()).toString());
                    sockOut.close();
                    newClient.close();
                    continue;
                }
                if (message.type() != Message.MessageType.DISCOVER) {
                    this.ddchannel.printMessage("Expected DISCOVER message; closing socket.");
                    PrintWriter sockOut = new PrintWriter(newClient.getOutputStream(),true);
                    sockOut.println((new ErrorMessage()).toString());
                    sockOut.close();
                    newClient.close();
                    continue;
                }
                DiscoverMessage dmsg = (DiscoverMessage)message;
                this.ddchannel.processRequest(new Request(dmsg.getIMEI(), dmsg.getX(), dmsg.getY(), newClient, sockIn));
            }
            listenSocket.close();
        }
        catch (UnknownHostException e) {
            this.ddchannel.printMessage(("Cannot find host provided: "+e.getLocalizedMessage()));
        }
        catch (IOException e2) {
            this.ddchannel.printMessage(("Cannot perform IO operation: "+e2.getLocalizedMessage()));
        }
    }
}
