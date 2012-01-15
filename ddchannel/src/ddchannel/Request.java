package ddchannel;

import java.io.BufferedReader;
import java.net.Socket;

public class Request {
    private Float x, y;
    private Socket socket;
    private String IMEI;
    BufferedReader sockIn;
    
    public Request(String newIMEI, Float longtitude, Float latitude, Socket newSocket, BufferedReader sin) {
        this.x = longtitude;
        this.y = latitude;
        this.socket = newSocket;
        this.IMEI = newIMEI;
        this.sockIn = sin;
    }
    
    public void closeSockIn() throws Exception { sockIn.close(); }
    
    public Float getX() { return x; }
    public Float getY() { return y; }
    public Socket getSocket() { return socket; }
    public String getIMEI() { return IMEI; }
}
