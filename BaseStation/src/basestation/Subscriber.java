/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package basestation;

/**
 *
 * @author flareman
 */
public class Subscriber {
    private String IMSI, IMEI;
    private Float longtitude, latitude;
    
    public Subscriber(String newIMEI, String newIMSI, Float x, Float y) {
        this.IMEI = newIMEI;
        this.IMSI = newIMSI;
        this.longtitude = x;
        this.latitude = y;
    }
    
    public String getIMSI() { return IMSI; }
    public String getIMEI() { return IMEI; }
    public Float getLongtitude() { return longtitude; }
    public Float getLatitude() { return latitude; }
}
