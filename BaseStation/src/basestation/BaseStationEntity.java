package basestation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Properties;

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
        System.setErr(null);
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
        println("Deactivating base station...");
        if (!isActive) 
            throw new Exception("Base station not active.");
        stopBroadcast();
        stopListening();
        disconnectAllClients();
        isActive = false;
        println("Base station deactivated.");
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
        Properties pFile = new Properties();
        pFile.load(new FileReader(fileName));
        this.networkID = pFile.getProperty("NetworkID");
        this.SSID = pFile.getProperty("BaseStationID");
        this.percentilePower = Integer.parseInt(pFile.getProperty("PercentilePower"));
        this.type = NetworkType.valueOf(pFile.getProperty("NetworkType"));
        this.frequency = Float.parseFloat(pFile.getProperty("Frequency"));
        this.maxBitrate = Integer.parseInt(pFile.getProperty("MaxBitrate"));
        this.guaranteedBitrate = Integer.parseInt(pFile.getProperty("MaxGuaranteedBitrate"));
        this.provider = pFile.getProperty("Provider");
        this.charges = ChargeModel.valueOf(pFile.getProperty("ChargeModel"));
        this.range = Float.parseFloat(pFile.getProperty("Range"));
        this.longtitude = Float.parseFloat(pFile.getProperty("Longtitude"));
        this.latitude = Float.parseFloat(pFile.getProperty("Latitude"));
        this.port = Integer.parseInt(pFile.getProperty("Port"));
        this.keepAlivePeriod = Integer.parseInt(pFile.getProperty("KeepAlivePeriod"));
        this.theForm.populatePropertyTable();
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
        while (true) {
            try { mxRemove.lock(); } catch (InterruptedException e) { continue; }
            if (mxRemove.check()) break;
        }
        while (true) {
            try { mxPerform.lock(); } catch (InterruptedException e) { continue; }
            if (mxPerform.check()) break;
        }
        mxRemove.raise();
        connectedTerminals.clear();
        Iterator it = socketThreads.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            Processor p = (Processor)entry.getValue();
            p.disconnectTerminal();
            try {
                it.remove();
            } catch (Exception e) { }
        }
        mxPerform.raise();
    }

    public Integer getCurrentLoad() {
        Integer result = 0;
        while (true) {
            try { mxPerform.lock(); } catch (InterruptedException e) { continue; }
            if (mxPerform.check()) break;
        }
        result =  (connectedTerminals.size() * 5);
        mxPerform.raise();
        return result;
    }
    
    public void addSubscriber(String IMEI, String IMSI, Float longt, Float lat, Processor newThread) {
        while (true) {
            try { mxPerform.lock(); } catch (InterruptedException e) { continue; }
            if (mxPerform.check()) break;
        }
        while (mxRemove.check()) {
            mxPerform.raise();
            while (true) {
                try { mxPerform.lock(); } catch (InterruptedException e) { continue; }
                if (mxPerform.check()) break;
            }
        }
        Subscriber newSubscriber = new Subscriber(IMEI, IMSI, longt, lat);
        this.connectedTerminals.put(IMEI, newSubscriber);
        this.theForm.clearTable();
        this.theForm.populateTable(this.connectedTerminals);
        this.socketThreads.put(IMEI, newThread);
        mxPerform.raise();
        this.theForm.updateProgressBar(this.getCurrentLoad());
    }

    public synchronized void processDisconnection(String theIMEI) {
        while (true) {
            try { mxRemove.lock(); } catch (InterruptedException e) { continue; }
            if (mxRemove.check()) break;
        }
        while (true) {
            try { mxPerform.lock(); } catch (InterruptedException e) { continue; }
            if (mxPerform.check()) break;
        }
        mxRemove.raise();
        socketThreads.remove(theIMEI);
        connectedTerminals.remove(theIMEI);
        this.theForm.clearTable();
        this.theForm.populateTable(this.connectedTerminals);
        mxPerform.raise();
        this.theForm.updateProgressBar(this.getCurrentLoad());
    }
}