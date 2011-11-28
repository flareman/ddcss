package basestation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.lang.Thread;

public class BaseStationEntity {    
    enum NetworkType {
        GSM,
        UTMS,
        WLAN,
        WIMAX
    };

    enum ChargeModel {
        FIXED,
        METERED,
        PACKET,
        EXPECTED,
        EDGE,
        PARIS,
        AUCTION
    };
    
    BaseStationMainForm theForm;
    
    private String networkID, SSID, provider;
    private Integer percentilePower;
    private Float frequency;
    private Integer maxBitrate, guaranteedBitrate, port;
    private Float range, longtitude, latitude;
    private NetworkType type;
    private ChargeModel charges;
    private HashMap<String, Subscriber> connectedTerminals = new HashMap<String, Subscriber>();
    private Integer keepAlivePeriod;
    private Boolean isActive = false;
    private HashMap<String, Processor> socketThreads = new HashMap<String, Processor>();
    private Thread listenerThread = null;
    private Thread broadcasterThread = null;
    BSMutex mxRemove = new BSMutex();
    BSMutex mxPerform = new BSMutex();

    public Float getLongtitude() { return this.longtitude; }
    public Float getLatitude() { return this.latitude; }
    public Float getRange() { return this.range; }
    public Integer getPort() { return this.port; }
    public String getNetworkID() { return this.networkID; }
    public String getProvider() { return this.provider; }
    public String getSSID() { return this.SSID; }    
    public Integer getPower() { return this.percentilePower; }
    public Integer getGuaranteedBitrate() { return this.guaranteedBitrate; }
    public Integer getMaxBitrate() { return this.maxBitrate; }
    public Float getFrequency() { return this.frequency; }
    public String getNetworkType() { return this.type.toString(); }
    public String getChargeModel() { return this.charges.toString(); }
    public Integer getKeepAlive() { return this.keepAlivePeriod; }
        
    public BaseStationEntity(BaseStationMainForm mainForm) {
        theForm = mainForm;
        mainForm.setBSEntity(this);
    }
    
    public void activate(String propertyFile) throws Exception {
        print("Activating base station... ");
        if (isActive) 
            throw new Exception("Base station already active.");
        readPropertyFile(propertyFile);
        beginBroadcast();
        startListening();
        isActive = true;
        println("active.");
        this.theForm.populateTable(this.connectedTerminals);
    }
    
    public void deactivate() throws Exception {
        print("Deactivating base station... ");
        if (!isActive) 
            throw new Exception("Base station not active.");
        stopBroadcast();
        stopListening();
        disconnectAllClients();
        isActive = false;
        println("done.");
        this.theForm.clearTable();
        this.theForm.clearPropertyTable();
        this.theForm.updateProgressBar(new Integer(0));
    }

    public Boolean isActive() { return isActive; }
    
    public void print(String input) {
        theForm.appendToStatus(input);
    }
    
    public void println(String input) {
        theForm.appendToStatus(input+"\n");
    }
    
    public void clear() {
        theForm.clearStatus();
    }

    private void readPropertyFile(String fileName) throws Exception {
        // Reads the property file provided as argument line by line and stores each value to the corresponding class field.
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String line;
        int i = 0;
        while((line = in.readLine()) != null) {
            if (i == 0) this.networkID = line;
            if (i == 1) this.SSID = line;
            if (i == 2) this.percentilePower = Integer.parseInt(line);
            if (i == 3) this.type = NetworkType.valueOf(line);
            if (i == 4) this.frequency = Float.parseFloat(line);
            if (i == 5) this.maxBitrate = Integer.parseInt(line);
            if (i == 6) this.guaranteedBitrate = Integer.parseInt(line);
            if (i == 7) this.provider = line;
            if (i == 8) this.charges = ChargeModel.valueOf(line);
            if (i == 9) this.range = Float.parseFloat(line);
            if (i == 10) this.longtitude = Float.parseFloat(line);
            if (i == 11) this.latitude = Float.parseFloat(line);
            if (i == 12) this.port = Integer.parseInt(line);
            if (i == 13) this.keepAlivePeriod = Integer.parseInt(line);
            i++;
        }
        this.theForm.populatePropertyTable(this);
    }
    
    private void beginBroadcast() throws Exception {
        // Starts the helper thread that will perform the updates
        if ((broadcasterThread != null) && (broadcasterThread.isAlive()))
            throw new Exception("Base station already broadcasting to DDC.");
        
        Broadcaster bt = new Broadcaster(this);
        broadcasterThread = new Thread(bt);
        broadcasterThread.start();
    }

    private void stopBroadcast() {
        // Notifies the helper thread to stop the updates
        broadcasterThread.interrupt();
        try {
            broadcasterThread.join();
        } catch (Exception e) {}
        broadcasterThread = null;
    }
    
    private void startListening() throws Exception {
        // Opens socket for listening to incoming terminal requests
        if ((listenerThread != null) && (listenerThread.isAlive()))
            throw new Exception("Base station already listening.");
        
        Listener lt = new Listener(this);
        listenerThread = new Thread(lt);
        listenerThread.start();
    }

    private void stopListening() {
        // Shuts down the socket
        listenerThread.interrupt();
        try {
            listenerThread.join();
        } catch (Exception e) {}
        listenerThread = null;
    }
    
    private void disconnectAllClients() {
        connectedTerminals.clear();
        Iterator it = socketThreads.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Processor p = (Processor)pairs.getValue();
            p.disconnectTerminal();
            it.remove();
        }
    }

    public Integer getCurrentLoad() {
        Integer result = 0;
        while (!mxPerform.check()) try { mxPerform.lock(); } catch (InterruptedException e) {}
        result =  (connectedTerminals.size() * 5);
        mxPerform.raise();
        return result;
    }
    
    public void addSubscriber(String IMEI, String IMSI, Float longt, Float lat, Processor newThread) {
        while (!mxPerform.check()) try { mxPerform.lock(); } catch (InterruptedException e) {}
        while (mxRemove.check()) {
            mxPerform.raise();
            while (!mxPerform.check()) try { mxPerform.lock(); } catch (InterruptedException e) {}
        }
        Subscriber newSubscriber = new Subscriber(IMEI, IMSI, longt, lat);
        this.connectedTerminals.put(IMEI, newSubscriber);
        Boolean invalidIMEI = true;
        for (String s: connectedTerminals.keySet())
            if (s.equals(IMEI)) { invalidIMEI = true; break; }
        this.theForm.clearTable();
        this.theForm.populateTable(this.connectedTerminals);
        this.socketThreads.put(IMEI, newThread);
        mxPerform.raise();
        this.theForm.updateProgressBar(this.getCurrentLoad());
    }

    public void processDisconnection(String theIMEI) {
        while (!mxRemove.check()) try { mxRemove.lock(); } catch (InterruptedException e) {}
        while (!mxPerform.check()) try { mxPerform.lock(); } catch (InterruptedException e) {}
        mxRemove.raise();
        socketThreads.remove(theIMEI);
        connectedTerminals.remove(theIMEI);
        this.theForm.clearTable();
        this.theForm.populateTable(this.connectedTerminals);
        mxPerform.raise();
        this.theForm.updateProgressBar(this.getCurrentLoad());
    }
}
