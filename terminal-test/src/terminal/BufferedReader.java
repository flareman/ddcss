package terminal;

import java.io.IOException;
import java.io.InputStreamReader;

public class BufferedReader {
    private InputStreamReader stream = null;
    private boolean interrupted = false;
    private boolean canBeInterrupted = true;

    public BufferedReader(InputStreamReader iss) throws IOException {
        if (iss == null) throw new IOException("Null input stream not acceptable.");
        this.stream = iss;
    }
    
    public void setInterruptible(boolean value) { this.canBeInterrupted = value; }
    public boolean isInterruptible() { return this.canBeInterrupted; }
    
    public String readLine() throws InterruptedException, IOException {
        if (this.canBeInterrupted) {
            if (this.interrupted) throw new InterruptedException();
            while (!this.stream.ready())
                Thread.sleep(500);
        }
        int ch = 0;
        StringBuffer temp = new StringBuffer();
        while ((ch = stream.read()) != -1) {
            if (ch == '\n') break;
            temp.append((char)ch);
        }
        return temp.toString();
    }
    
    public void close() throws IOException {
        this.stream.close();
    }
    
    public void interrupt() { this.interrupted = true; }
}
