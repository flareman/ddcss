package terminal;

import java.io.InputStreamReader;

public class BufferedReader {
    private char[] buffer = null;
    private String bigBertha;
    private InputStreamReader stream = null;

    public BufferedReader(InputStreamReader iss) throws Exception {
        if (iss == null) throw new Exception("Null input stream not acceptable.");
        this.stream = iss;
        this.buffer = new char[2048];
        this.bigBertha = "";
    }
    
    public String readLine() throws Exception {
        while (this.bigBertha.indexOf('\n') == -1) {
            Helper.fillCharBuffer(buffer, '\0');
            while (!this.stream.ready());
            int readBytes = this.stream.read(buffer);
            this.bigBertha += new String(this.buffer);
        }
        String[] lines = Helper.splitString(bigBertha, "\n");
        this.bigBertha = "";
        for (int i = 1; i < lines.length; i++) this.bigBertha += lines[i];
        return lines[0];
    }
    
    public void close() throws Exception {
        this.stream.close();
    }
}
