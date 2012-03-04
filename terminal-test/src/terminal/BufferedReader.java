package terminal;

import java.io.InputStreamReader;

public class BufferedReader {
    private char[] buffer = null;
    private String bigBertha;
    private InputStreamReader stream = null;
    private boolean EOF = false;
    private boolean interrupted = false;

    public BufferedReader(InputStreamReader iss) throws Exception {
        if (iss == null) throw new Exception("Null input stream not acceptable.");
        this.stream = iss;
        this.buffer = new char[2048];
        this.bigBertha = "";
        this.EOF = false;
    }
    
    public String readLine() throws Exception {
        while ((!this.EOF) && (this.bigBertha.indexOf('\n') == -1)) {
            Helper.fillCharBuffer(buffer, '\0');
            if (this.interrupted) throw new InterruptedException();
            while (!this.stream.ready()) {
                Thread.sleep(500);
            }
            int readBytes = this.stream.read(buffer);
            if (readBytes == -1) this.EOF = true;
            this.bigBertha += new String(this.buffer);
        }
        if (this.bigBertha.equals("")) return null;
        String[] lines = Helper.splitString(bigBertha, "\n");
        this.bigBertha = "";
        for (int i = 1; i < lines.length; i++) this.bigBertha += lines[i];
        return lines[0];
    }
    
    public void close() throws Exception {
        this.stream.close();
    }
    
    public void interrupt() { this.interrupted = true; }
}
