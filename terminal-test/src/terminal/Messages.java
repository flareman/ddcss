package terminal;

import java.util.Vector;

class Message {
    String theType;
    
    public Message() {
        theType = "UNDEFINED";
    }

    public String toString() { return ""; };
    
    public String type() { return theType; };
    
    public static Message newMessageFromString(String input) throws Exception {
        String[] words = null;
        words = Helper.splitString(input, ' ', 0);
        if (words[0].equals("PROFILES")) {
            if (words.length < 1) throw new Exception("Invalid PROFILE message syntax.");
            return new ProfilesMessage(input);
        }
        if (words[0].equals("OK")) {
            if (words.length > 1) throw new Exception("Invalid OK message syntax.");
            return new OKMessage();
        }
        if (words[0].equals("OVERLOADED")) {
            if (words.length > 1) throw new Exception("Invalid OVERLOADED message syntax.");
            return new OverloadedMessage();
        }
        if (words[0].equals("NO_COVERAGE")) {
            if (words.length > 1) throw new Exception("Invalid NO_COVERAGE message syntax.");
            return new NoCoverageMessage();
        }
        if (words[0].equals("DISCONNECT")) {
            if (words.length > 1) throw new Exception("Invalid DISCONNECT message syntax.");
            return new DisconnectMessage();
        }
        throw new Exception("Unrecognized message syntax.");
    };
}

class DiscoverMessage extends Message {
    String IMEI;
    Float x, y;
    
    public DiscoverMessage(String newIMEI, Float x, Float y) {
        theType = "DISCOVER";
        this.IMEI = newIMEI;
        this.x = x;
        this.y = y;
    }
    
    public String toString() {
        return "DISCOVER "+IMEI+" "+x+" "+y;
    }
    
    public String getIMEI() { return this.IMEI; };

    public Float getX() { return this.x; };

    public Float getY() { return this.y; };
}

class ConnectMessage extends Message {
    String IMEI, IMSI;
    Float x, y;
    
    public ConnectMessage(String newIMEI, String newIMSI, Float x, Float y) {
        theType = "CONNECT";
        this.IMEI = newIMEI;
        this.IMSI = newIMSI;
        this.x = x;
        this.y = y;
    }
    
    public String toString() {
        return "CONNECT "+IMEI+" "+IMSI+" "+x+" "+y;
    }
    
    public String getIMEI() { return this.IMEI; };

    public String getIMSI() { return this.IMSI; };

    public Float getX() { return this.x; };

    public Float getY() { return this.y; };
}

class NoCoverageMessage extends Message {
    public NoCoverageMessage() {
        theType = "NO_COVERAGE";
    }
    
    public String toString() {
        return "NO_COVERAGE";
    }
}

class OverloadedMessage extends Message {
    public OverloadedMessage() {
        theType = "OVERLOADED";
    }
    
    public String toString() {
        return "OVERLOADED";
    }
}

class ErrorMessage extends Message {
    public ErrorMessage() {
        theType = "ERROR";
    }
    
    public String toString() {
        return "ERROR";
    }
}

class OKMessage extends Message {
    public OKMessage() {
        theType = "OK";
    }
    
    public String toString() {
        return "OK";
    }
}

class DisconnectMessage extends Message {
    public DisconnectMessage() {
        theType = "DISCONNECT";
    }
    
    public String toString() {
        return "DISCONNECT";
    }
}

class DisconnectReqMessage extends Message {
    String IMEI;

    public DisconnectReqMessage(String newIMEI) {
        theType = "DISCONNECT_REQ";
        this.IMEI = newIMEI;
    }
    
    public String toString() {
        return "DISCONNECT "+this.IMEI;
    }

    public String getIMEI() { return this.IMEI; };
}

class ProfilesMessage extends Message {
    Vector BSList = new Vector();
    
    public ProfilesMessage(String msg) {
        theType = "PROFILES";
        String[] baseStations = null;
        baseStations = Helper.splitString(msg, ' ', 0);
        for (int i = 1; i < baseStations.length; i++) {
            String[] properties = null;
            properties = Helper.splitString(baseStations[i], '#', 0);
            DummyBS newBS = new DummyBS("localhost", properties[0], properties[1], properties[2], new Integer(Integer.parseInt(properties[3])), new Float(Float.parseFloat(properties[4])),
                    new Integer(Integer.parseInt(properties[5])), new Integer(Integer.parseInt(properties[6])), new Integer(Integer.parseInt(properties[7])),
                    new Float(Float.parseFloat(properties[8])), new Float(Float.parseFloat(properties[9])), new Float(Float.parseFloat(properties[10])),
                    properties[11], properties[12], new Integer(Integer.parseInt(properties[13])), new Integer(Integer.parseInt(properties[14])));
            BSList.addElement(newBS);
        }
    }
    
    public Vector getBaseStations() { return this.BSList; }
}
