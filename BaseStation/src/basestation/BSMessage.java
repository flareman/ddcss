package basestation;

class BSMessage {
    public enum MessageType {
        UNDEFINED,
        CONNECT,
        DISCONNECT_REQ,
        DISCONNECT_COM,
        OK,
        ERROR,
        NO_COVERAGE,
        OVERLOADED,
        PROFILE
    };
    
    MessageType theType;
    
    public BSMessage() {
        theType = MessageType.UNDEFINED;
    }

    @Override
    public String toString() { return ""; };
    
    public MessageType type() { return theType; };
    
    public static BSMessage newMessageFromString(String input) throws Exception {
        String[] words = null;
        words = input.split(" ");
        if (words[0].equals("CONNECT")) {
            if (words.length < 5) throw new Exception("Invalid CONNECT message syntax.");
            return new BSConnectMessage(words[1], words[2], Float.parseFloat(words[3]), Float.parseFloat(words[4]));
        }
        if (words[0].equals("OK"))
            return new BSOkMessage();
        if (words[0].equals("NO_COVERAGE"))
            return new BSNoCoverageMessage();
        if (words[0].equals("OVERLOADED"))
            return new BSOverloadedMessage();
        if (words[0].equals("DISCONNECT")) {
            if (words.length == 1)
                return new BSDisconnectComMessage();
            if (words.length == 2)
                return new BSDisconnectReqMessage(words[1]);
            throw new Exception("Invalid DISCONNECT message syntax.");
        }
        if (words[0].equals("PROFILE")) {
            if (words.length < 16) throw new Exception("Invalid PROFILE message syntax.");
            return new BSProfileMessage(words[1],words[2],words[3],Integer.parseInt(words[4]),Float.parseFloat(words[5]),Integer.parseInt(words[6]),Integer.parseInt(words[7]),Integer.parseInt(words[8]),Float.parseFloat(words[9]),Float.parseFloat(words[10]),Float.parseFloat(words[11]),words[12],words[13], Integer.parseInt(words[14]), Integer.parseInt(words[15]));
        }
        throw new Exception("Unrecognized message syntax.");
    };
}

class BSConnectMessage extends BSMessage {
    String IMEI, IMSI;
    Float longtitude, latitude;
    
    public BSConnectMessage(String newIMEI, String newIMSI, Float x, Float y) {
        theType = MessageType.CONNECT;
        this.IMEI = newIMEI;
        this.IMSI = newIMSI;
        this.longtitude = x;
        this.latitude = y;
    }
    
    @Override
    public String toString() {
        return "CONNECT "+IMEI+" "+IMSI+" "+longtitude+" "+latitude;
    }
    
    public String getIMEI() { return this.IMEI; };

    public String getIMSI() { return this.IMSI; };

    public Float getLongtitude() { return this.longtitude; };

    public Float getLatitude() { return this.latitude; };
}

class BSDisconnectReqMessage extends BSMessage {
    String IMEI;
    
    public BSDisconnectReqMessage(String newIMEI) {
        theType = MessageType.DISCONNECT_REQ;
        this.IMEI = newIMEI;
    }
    
    @Override
    public String toString() {
        return "DISCONNECT "+IMEI;
    }
    
    public String getIMEI() { return this.IMEI; };
}

class BSDisconnectComMessage extends BSMessage {
    public BSDisconnectComMessage() {
        theType = MessageType.DISCONNECT_COM;
    }
    
    @Override
    public String toString() {
        return "DISCONNECT";
    }
}

class BSOverloadedMessage extends BSMessage {
    public BSOverloadedMessage() {
        theType = MessageType.OVERLOADED;
    }
    
    @Override
    public String toString() {
        return "OVERLOADED";
    }
}

class BSNoCoverageMessage extends BSMessage {
    public BSNoCoverageMessage() {
        theType = MessageType.NO_COVERAGE;
    }
    
    @Override
    public String toString() {
        return "NO_COVERAGE";
    }
}

class BSOkMessage extends BSMessage {
    public BSOkMessage() {
        theType = MessageType.OK;
    }
    
    @Override
    public String toString() {
        return "OK";
    }
}

class BSErrorMessage extends BSMessage {
    public BSErrorMessage() {
        theType = MessageType.ERROR;
    }
    
    @Override
    public String toString() {
        return "ERROR";
    }
}

class BSProfileMessage extends BSMessage {
    private String networkID, SSID, provider;
    private Integer percentilePower;
    private Float frequency;
    private Integer maxBitrate, guaranteedBitrate, port;
    private Float range, longtitude, latitude;
    private String type;
    private String charges;
    private Integer load, keepAlivePeriod;
    
    public BSProfileMessage(String newID, String newSSID, String newProvider, Integer newPower, Float newFrequency, Integer newMaxBitrate, Integer newGuaranteedBitrate, Integer newPort, Float newRange, Float x, Float y, String newType, String newCharges, Integer newLoad, Integer newKeepAlive) {
        theType = MessageType.PROFILE;
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
    
    public BSProfileMessage(BaseStationEntity baseStation) throws Exception {
        theType = MessageType.PROFILE;
        this.networkID = baseStation.getNetworkID();
        this.SSID = baseStation.getSSID();
        this.provider = baseStation.getProvider();
        this.percentilePower = baseStation.getPower();
        this.frequency = baseStation.getFrequency();
        this.maxBitrate = baseStation.getMaxBitrate();
        this.guaranteedBitrate = baseStation.getGuaranteedBitrate();
        this.port = baseStation.getPort();
        this.range = baseStation.getRange();
        this.longtitude = baseStation.getLongtitude();
        this.latitude = baseStation.getLatitude();
        this.type = baseStation.getNetworkType();
        this.charges = baseStation.getChargeModel();
        this.load = baseStation.getCurrentLoad();
        this.keepAlivePeriod = baseStation.getKeepAlive();
    }
    
    @Override
    public String toString() {
        return "PROFILE "+networkID+" "+SSID+" "+provider+" "+percentilePower+" "+frequency+" "+maxBitrate+" "+guaranteedBitrate+" "+port+" "+range+" "+longtitude+" "+latitude+" "+type+" "+charges+" "+load+" "+keepAlivePeriod;
    }
}