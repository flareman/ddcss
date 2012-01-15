package ddchannel;

public class DummyBS {
    private String networkID, SSID, provider;
    private Integer percentilePower;
    private Float frequency;
    private Integer maxBitrate, guaranteedBitrate, port;
    private Float range, longtitude, latitude;
    private String type;
    private String charges;
    private Integer load, keepAlivePeriod;
    
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
        this.longtitude = x;
        this.latitude = y;
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
        this.longtitude = msg.getX();
        this.latitude = msg.getY();
        this.type = msg.getType();
        this.charges = msg.getCharges();
        this.load = msg.getLoad();
        this.keepAlivePeriod = msg.getKeepAlive();
    }

    @Override
    public String toString() {
        return (networkID+"#"+SSID+"#"+provider+"#"+percentilePower+"#"+frequency+"#"+maxBitrate+"#"+guaranteedBitrate+"#"+port+"#"+range+"#"+longtitude+"#"+latitude+"#"+type+"#"+charges+"#"+load);
    }
}
