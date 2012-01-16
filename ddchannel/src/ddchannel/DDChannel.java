package ddchannel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import javax.xml.ws.Endpoint;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

public class DDChannel extends SingleFrameApplication {
    private WebServicePublisher publisher;
    private Endpoint serverEndpoint;
    private Thread listener;
    private Reaper reaper; // Alexis Dellis reaps lost souls
    private ArrayList<Processor> processors = new ArrayList<Processor>();
    private int nextProcessor = 0;
    private MainWindow window;
    private Connection db = null;
    private PreparedStatement deletePst = null;
    private PreparedStatement insertPst = null;
    private HashMap<String, DummyBS> cache = new HashMap<String, DummyBS>();
    private BaseStationTableModel tm;
    private Mutex mxSQL = new Mutex();
    private long nextReap;

    @Override protected void startup() {
        this.tm = new BaseStationTableModel(cache);
        window = new MainWindow(this);
        show(window);
    }

    @Override protected void configureWindow(java.awt.Window root) {
    }

    public static DDChannel getApplication() {
        return Application.getInstance(DDChannel.class);
    }

    public static void main(String[] args) {
        launch(DDChannel.class, args);
    }
    
    public void startServer(Integer listenerPort, String server, Integer WSDLPort,
            Integer threads, String dbServer, Integer dbPort,
            String dbID, String dbPass, String databaseSchema, String script) throws Exception {
        if (this.isActive()) return;
        this.window.clearLog();
        this.window.printMessage("Starting channel...");
        this.cache.clear();
        this.nextReap = -1;
        this.connectToDatabase(dbServer, dbPort, dbID, dbPass, databaseSchema);
        this.initializeSchema(script);
        this.setupProcessorThreads(threads);
        this.listener = new Thread(new Listener(listenerPort, this));
        this.listener.start();
        this.reaper = new Reaper(this);
        this.reaper.start();
        this.window.printMessage("Listener thread is listening at port "+listenerPort+".");
        this.publisher = new WebServicePublisher(this);
        this.serverEndpoint = Endpoint.publish("http://"+server+":"+WSDLPort+"/ddc", publisher);
        this.window.printMessage("Endpoint published at http://"+server+":"+WSDLPort+"/ddc?WSDL.");
        this.window.printMessage("Channel is listening.");
    }
    
    private void setupProcessorThreads(int newThreads) {
        for (int i = 0; i < newThreads; i++)
            processors.add(new Processor(this));
        for (Processor p: processors) p.start();
        this.window.printMessage("Processor threads started.");
    }
    
    public boolean isActive() { return listener != null?true:false; }
    
    public void stopServer() throws Exception {
        if (!this.isActive()) return;
        this.window.printMessage("Stopping channel...");
        this.serverEndpoint.stop();
        this.window.printMessage("Endpoint publishing stopped.");
        listener.interrupt();
        listener.join();
        listener = null;
        reaper.requestShutdown();
        reaper.interrupt();
        reaper.join();
        reaper = null;
        this.window.printMessage("Listener thread killed.");
        for (Processor p: processors) {
            p.requestShutdown();
            p.interrupt();
            p.join();
        }
        processors.clear();
        this.window.printMessage("Processor threads killed.");
        this.closeDatabase();
        this.window.printMessage("Channel stopped.");
    }
    
    public void printMessage(String msg) {
        window.printMessage(msg);
    }
    
    public void processRequest(Request req) {
        processors.get(nextProcessor).addRequest(req);
        if (nextProcessor >= processors.size()) nextProcessor = 0; else nextProcessor++;
    }
    
    public BaseStationTableModel getDatabaseTM() { return this.tm; }
    
    public ArrayList<DummyBS> listOfBSForCoords(Float x, Float y) {
        ArrayList<DummyBS> reply = new ArrayList<DummyBS>();
        while (true) {
                try { this.mxSQL.lock(); } catch (InterruptedException e) { continue; }
                break;
        }
        for (DummyBS bs: cache.values())
            if ((Math.abs(x - bs.getX()) < bs.getRange()) && (Math.abs(y - bs.getY()) < bs.getRange())) // bounding box
                reply.add(bs);
        this.mxSQL.raise();
        return reply;
    }
    
    private void connectToDatabase(String server, Integer port, String username, String password, String schema) throws Exception {
        if ((db != null) && (!db.isClosed())) return;
        Class.forName("com.mysql.jdbc.Driver");
        this.window.printMessage("Connecting to MySQL server...");
        db = DriverManager.getConnection("jdbc:mysql://"+server+":"+port+"/"+schema, username, password);
        this.window.printMessage("Connected to "+server+":"+port+" as "+username+".");
    }
    
    private void closeDatabase() throws Exception {
        this.window.printMessage("Disconnecting from MySQL server...");
        deletePst.close();
        insertPst.close();
        if (this.db != null) this.db.close();
        db = null;
        this.window.printMessage("Disconnected from MySQL server.");
    }
    
    private void initializeSchema(String SQLScript) throws Exception {
        if ((db == null) || (db.isClosed()))
            throw new Exception("Database connection is down.");
        
        this.window.printMessage("Initializing schema...");
        Statement st = null;
        try {
            this.db.setAutoCommit(false);
            st = this.db.createStatement();
            BufferedReader sqlReader = new BufferedReader(new FileReader(SQLScript));
            String nextStatement;
            while ((nextStatement = sqlReader.readLine()) != null)
                st.addBatch(nextStatement);
            st.executeBatch();
            db.commit();
            db.setAutoCommit(true);
            this.window.printMessage("Schema initialized.");
            this.deletePst = db.prepareStatement("DELETE FROM BS WHERE networkID = ?");
            this.insertPst = db.prepareStatement("INSERT INTO BS VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        } catch (SQLException ex) {
            if (this.db != null) try { this.db.rollback(); this.window.printMessage("Rolling back SQL initialization: "+ex.getLocalizedMessage()); } catch (SQLException ex1) { throw ex1; }
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException ex) { throw ex; }
        }
    }
    
    public void processUpdate(UpdateMessage msg) throws Exception {
        while (true) {
                try { this.mxSQL.lock(); } catch (InterruptedException e) { continue; }
                break;
        }
        DummyBS update = new DummyBS(msg);
        update.updateTimestamp();
        if ((this.nextReap == -1) || (this.nextReap > update.getTimestamp())) { this.nextReap = update.getTimestamp(); this.reaper.interrupt(); }
        this.cache.put(msg.getNetworkID(), update);
        try {
            this.db.setAutoCommit(false);
            this.deletePst.setString(1, msg.getNetworkID());
            this.insertPst.setString(1, msg.getNetworkID());
            this.insertPst.setString(2, msg.getSSID());
            this.insertPst.setInt(3, msg.getPower());
            this.insertPst.setFloat(4, msg.getFrequency());
            this.insertPst.setString(5, msg.getType());
            this.insertPst.setInt(6, msg.getMaxBr());
            this.insertPst.setInt(7, msg.getGuaranteedBr());
            this.insertPst.setInt(8, msg.getLoad());
            this.insertPst.setString(9, msg.getProvider());
            this.insertPst.setFloat(10, msg.getRange());
            this.insertPst.setFloat(11, msg.getX());
            this.insertPst.setFloat(12, msg.getY());
            this.insertPst.setInt(13, msg.getPort());
            this.insertPst.setString(14, msg.getCharges());
            this.deletePst.executeUpdate();
            this.insertPst.executeUpdate();
            this.db.commit();
            this.db.setAutoCommit(true);
        } catch (SQLException ex) {
            if (this.db != null) try { this.db.rollback(); this.window.printMessage("Rolling back SQL initialization: "+ex.getLocalizedMessage()); } catch (SQLException ex1) { throw ex1; }
        }
        this.tm.fireTableDataChanged();
        this.mxSQL.raise();
    }
    
    public long nextReapInterval() {
        if (this.nextReap == -1) return -1;
        long result = this.nextReap - System.currentTimeMillis(); if (result <= 0) return 0; else return result;
    }
    
    public void reapDatabase() {
        while (true) {
                try { this.mxSQL.lock(); } catch (InterruptedException e) { continue; }
                break;
        }
        this.window.printMessage("Beginning reap with "+cache.size()+" stations...");
        this.nextReap = -1;
        Iterator it = cache.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String networkID = (String)entry.getKey();
            DummyBS baseStation = (DummyBS)entry.getValue();
            if (baseStation.getTimestamp() < System.currentTimeMillis()) {
                it.remove();
                try {
                    this.deletePst.setString(1, baseStation.getNetworkID());
                    this.deletePst.executeUpdate();
                } catch (SQLException ex) { this.window.printMessage(ex.getLocalizedMessage()); }
            } else {
                if (this.nextReap == -1) this.nextReap = baseStation.getTimestamp();
                else if (this.nextReap > baseStation.getTimestamp()) this.nextReap = baseStation.getTimestamp();
            }
        }
        this.tm.fireTableDataChanged();
        this.window.printMessage("Reaped obsolete stations, "+cache.size()+" remain.");
        this.mxSQL.raise();
    }
}
