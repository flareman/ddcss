package ddchannel;

public class DummyBS {
    private String networkID, SSID, provider;
    private Integer percentilePower;
    private Float frequency;
    private Integer maxBitrate, guaranteedBitrate, port;
    private Float range, x, y;
    private String type;
    private String charges;
    private Integer load, keepAlivePeriod;
    private long timestamp;
    
    public DummyBS(String newID, String newSSID, String newProvider, Integer newPower, Float newFrequency, Integer newMaxBitrate, Integer newGuaranteedBitrate, Integer newPort, Float newRange, Float x, Float y, String newType, String newCharges, Integer newLoad, Integer newKeepAlive) {
        this.networkID = newID;
        this.SSID = newSSID;
        this.provider = newProvider;
        this.percentilePower = newPower;
        this.frequency = newFrequency;
        this.maxBitrate = newMaxBitrate;
        this.guaranteedBitrate = newGuaranteedBitrate;
        this.port = newPort;
        this.range = newRange;
        this.x = x;
        this.y = y;
        this.type = newType;
        this.charges = newCharges;
        this.load = newLoad;
        this.keepAlivePeriod = newKeepAlive;
    }

    public DummyBS(UpdateMessage msg) {
        this.networkID = msg.getNetworkID();
        this.SSID = msg.getSSID();
        this.provider = msg.getProvider();
        this.percentilePower = msg.getPower();
        this.frequency = msg.getFrequency();
        this.maxBitrate = msg.getMaxBr();
        this.guaranteedBitrate = msg.getGuaranteedBr();
        this.port = msg.getPort();
        this.range = msg.getRange();
        this.x = msg.getX();
        this.y = msg.getY();
        this.type = msg.getType();
        this.charges = msg.getCharges();
        this.load = msg.getLoad();
        this.keepAlivePeriod = msg.getKeepAlive();
    }
    
    public long getTimestamp() { return this.timestamp; }
    public void updateTimestamp() { this.timestamp = 500+System.currentTimeMillis()+3*this.keepAlivePeriod; }
    public Float getRange() { return this.range; }
    public Float getX() { return this.x; }
    public Float getY() { return this.y; }
    public String getNetworkID() { return this.networkID; }
    public String getSSID() { return this.SSID; }
    public String getProvider() { return this.provider; }
    public String getType() { return this.type; }
    public String getCharges() { return this.charges; }
    public Float getFrequency() { return this.frequency; }
    public Integer getPower() { return this.percentilePower; }
    public Integer getMaxBr() { return this.maxBitrate; }
    public Integer getGuaranteedBr() { return this.guaranteedBitrate; }
    public Integer getPort() { return this.port; }
    public Integer getLoad() { return this.load; }
    public Integer getKeepAlive() { return this.keepAlivePeriod; }
    
    @Override
    public String toString() {
        return (networkID+"#"+SSID+"#"+provider+"#"+percentilePower+"#"+frequency+"#"+maxBitrate+"#"+guaranteedBitrate+"#"+port+"#"+range+"#"+x+"#"+y+"#"+type+"#"+charges+"#"+load);
    }
}
