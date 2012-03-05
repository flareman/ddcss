package terminal;

public class DummyBS {
    private String address;
    private String networkID, SSID, provider;
    private Integer percentilePower;
    private Float frequency;
    private Integer maxBitrate, guaranteedBitrate, port;
    private Float range, x, y;
    private String type;
    private String charges;
    
    public DummyBS(String newAddress, String newID, String newSSID, String newProvider, Integer newPower, Float newFrequency, Integer newMaxBitrate, Integer newGuaranteedBitrate, Integer newPort, Float newRange, Float x, Float y, String newType, String newCharges) {
        this.address = newAddress;
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
    }

    public Float getRange() { return this.range; }
    public Float getX() { return this.x; }
    public Float getY() { return this.y; }
    public String getAddress() { return this.address; }
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
}
