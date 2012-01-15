package ddchannel;

public class Mutex extends Object  {
    private boolean locked = false;

    public void lock() throws InterruptedException {
        if (Thread.interrupted()) throw new InterruptedException();
        synchronized(this) {
            try {
                while (locked) wait();
                locked = true;
            } catch (InterruptedException e) { notifyAll(); throw e; }
        }
    }
    
    public synchronized void raise() { locked = false; notifyAll(); }
    
    public boolean check() {
        synchronized(this) { return locked; }
    }
}