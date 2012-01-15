package ddchannel;

class Message {
    public enum MessageType {
        UNDEFINED,
        DISCOVER,
        UPDATE,
        PROFILES,
        ERROR,
    };
    
    MessageType theType;
    
    public Message() {
        theType = MessageType.UNDEFINED;
    }

    @Override
    public String toString() { return ""; };
    
    public MessageType type() { return theType; };
    
    public static Message newMessageFromString(String input) throws Exception {
        String[] words = null;
        words = input.split(" ");
        if (words[0].equals("DISCOVER")) {
            if (words.length < 4) throw new Exception("Invalid DISCOVER message syntax.");
            return new DiscoverMessage(words[1], Float.parseFloat(words[2]), Float.parseFloat(words[3]));
        }
        if (words[0].equals("PROFILE")) {
            if (words.length < 16) throw new Exception("Invalid PROFILE message syntax.");
            return new UpdateMessage(words[1],words[2],words[3],Integer.parseInt(words[4]),Float.parseFloat(words[5]),Integer.parseInt(words[6]),Integer.parseInt(words[7]),Integer.parseInt(words[8]),Float.parseFloat(words[9]),Float.parseFloat(words[10]),Float.parseFloat(words[11]),words[12],words[13], Integer.parseInt(words[14]), Integer.parseInt(words[15]));
        }
        throw new Exception("Unrecognized message syntax.");
    };
}

class DiscoverMessage extends Message {
    String IMEI;
    Float x, y;
    
    public DiscoverMessage(String newIMEI, Float longtitude, Float latitude) {
        theType = MessageType.DISCOVER;
        this.IMEI = newIMEI;
        this.x = x;
        this.y = y;
    }
    
    @Override
    public String toString() {
        return "DISCOVER "+IMEI+" "+x+" "+y;
    }
    
    public String getIMEI() { return this.IMEI; };

    public Float getX() { return this.x; };

    public Float getY() { return this.y; };
}

class ErrorMessage extends Message {
    public ErrorMessage() {
        theType = MessageType.ERROR;
    }
    
    @Override
    public String toString() {
        return "ERROR";
    }
}

class UpdateMessage extends Message {
    private String networkID, SSID, provider;
    private Integer percentilePower;
    private Float frequency;
    private Integer maxBitrate, guaranteedBitrate, port;
    private Float range, longtitude, latitude;
    private String type;
    private String charges;
    private Integer load, keepAlivePeriod;
    
    public UpdateMessage(String newID, String newSSID, String newProvider, Integer newPower, Float newFrequency, Integer newMaxBitrate, Integer newGuaranteedBitrate, Integer newPort, Float newRange, Float x, Float y, String newType, String newCharges, Integer newLoad, Integer newKeepAlive) {
        theType = MessageType.UPDATE;
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
    
    public String getNetworkID(){
        return this.networkID;
    }
    
    public String getSSID(){
        return this.SSID;
    }
    
    public String getProvider(){
        return this.provider;
    }
    
    public String getType(){
        return this.type;
    }
    
    public String getCharges(){
        return this.charges;
    }
    
    public Integer getPower(){
        return this.percentilePower;
    }
    
    public Integer getMaxBr(){
        return this.maxBitrate;
    }
    
    public Integer getGuaranteedBr(){
        return this.guaranteedBitrate;
    }
    
    public Integer getPort(){
        return this.port;
    }
    
    public Integer getLoad(){
        return this.load;
    }
    
    public Integer getKeepAlive(){
        return this.keepAlivePeriod;
    }
    
    public Float getFrequency(){
        return this.frequency;
    }
    
    public Float getLongtitude(){
        return this.longtitude;
    }
    
    public Float getLatitude(){
        return this.latitude;
    }
    
    @Override
    public String toString() {
        return "PROFILE "+networkID+" "+SSID+" "+provider+" "+percentilePower+" "+frequency+" "+maxBitrate+" "+guaranteedBitrate+" "+port+" "+range+" "+longtitude+" "+latitude+" "+type+" "+charges+" "+load+" "+keepAlivePeriod;
    }
}

class ProfilesMessage extends Message {
    Object theList;
    
    public ProfilesMessage(Object someList) { // Accepts the list of valid base stations
        theType = MessageType.PROFILES;
        this.theList = someList;
    }
    
    @Override
    public String toString() {
        return "PROFILES ";
    }
}