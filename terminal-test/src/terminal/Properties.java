package terminal;

import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.util.Enumeration;  
import java.util.Hashtable;  
  
public class Properties {  
    private Hashtable propTable = new Hashtable();
  
    public static Properties loadProperties(String filePath) throws IOException {  
        Properties result = new Properties();

        InputStream stream = result.getClass().getResourceAsStream(filePath);
        InputStreamReader reader = new InputStreamReader(stream);
  
        StringBuffer sBuf = new StringBuffer();
        char[] buff = new char[1024];
  
        int pos = reader.read(buff, 0, 1024);
        while (pos != -1) {
            sBuf.append(buff, 0, pos);
            pos = reader.read(buff, 0, 1024);
        }

        String[] lines = Helper.splitString(sBuf.toString(), '\n', 0);
        for (int i = 0; i < lines.length; i++) {
            String[] kv = Helper.splitString(Helper.chomp(lines[i]), '=', 2);
            if (kv.length == 1) { result.setProperty(kv[0], ""); }
            if (kv.length == 2) { result.setProperty(kv[0], kv[1]); }
        }  
          
        return result;  
    }  
  
  
    public void setProperty(String key, String val) { this.propTable.put(key, val); }
  
    public String getProperty(String key) { return (String) this.propTable.get(key); }
  
    public int getPropertyCount() { return this.propTable.size(); }
  
    public Enumeration getEnumeratedNames() { return this.propTable.keys(); }
  
    public String[] getPropertyNames() {
        String[] result = new String[this.propTable.size()];
        int c = 0;
        for (Enumeration e = this.propTable.keys(); e.hasMoreElements();) {
            result[c] = (String) e.nextElement();
            c++;
        }
        return result;
    }  
}  
