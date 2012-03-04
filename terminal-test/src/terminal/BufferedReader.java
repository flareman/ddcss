package terminal;

import java.io.IOException;
import java.io.InputStreamReader;

public class BufferedReader {
    private char[] buffer = null;
    private String bigBertha;
    private InputStreamReader stream = null;
    private boolean EOF = false;
    private boolean interrupted = false;
    private boolean canBeInterrupted = true;

    public BufferedReader(InputStreamReader iss) throws IOException {
        if (iss == null) throw new IOException("Null input stream not acceptable.");
        this.stream = iss;
        this.buffer = new char[2048];
        this.bigBertha = "";
        this.EOF = false;
    }
    
    public void setInterruptible(boolean value) { this.canBeInterrupted = value; }
    public boolean isInterruptible() { return this.canBeInterrupted; }
    
    public String readLine() throws InterruptedException, IOException {
        while ((!this.EOF) && (this.bigBertha.indexOf('\n') == -1)) {
            Helper.fillCharBuffer(buffer, '\0');
            if (this.canBeInterrupted) {
                if (this.interrupted) throw new InterruptedException();
                while (!this.stream.ready())
                    Thread.sleep(500);
            }
            int readBytes = this.stream.read(buffer);
            if (readBytes == -1) this.EOF = true;
            this.bigBertha += new String(this.buffer);
        }
        if (this.bigBertha.equals("")) return null;
        String[] lines = Helper.splitString(bigBertha, '\n', 0);
        this.bigBertha = "";
        for (int i = 1; i < lines.length; i++) this.bigBertha += lines[i];
        return lines[0];
    }
    
    public void close() throws IOException {
        this.stream.close();
    }
    
    public void interrupt() { this.interrupted = true; }
}
