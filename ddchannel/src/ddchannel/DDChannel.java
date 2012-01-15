package ddchannel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import javax.xml.ws.Endpoint;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

public class DDChannel extends SingleFrameApplication {
    private WebServicePublisher publisher;
    private Endpoint serverEndpoint;
    private Thread listener;
    private ArrayList<Processor> processors = new ArrayList<Processor>();
    private int nextProcessor = 0;
    private MainWindow window;
    private Connection db = null;

    @Override protected void startup() {
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
        this.window.printMessage("Starting channel...");
        this.connectToDatabase(dbServer, dbPort, dbID, dbPass, databaseSchema);
        this.initializeSchema(script);
        this.setupProcessorThreads(threads);
        listener = new Thread(new Listener(listenerPort, this));
        listener.start();
        this.window.printMessage("Listener thread is listening at port "+listenerPort+".");
        this.publisher = new WebServicePublisher();
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
    
    public Object listOfBSForCoords(Float x, Float y) {
        return null; // send a valid list of BS's back to the requesting Processor
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
        db.close();
        db = null;
        this.window.printMessage("Disconnected from MySQL server.");
    }
    
    private void initializeSchema(String SQLScript) throws Exception {
        if ((db == null) || (db.isClosed()))
            throw new Exception("Database connection is down.");
        
        this.window.printMessage("Initializing schema...");
        Statement st = null;
        try {
            db.setAutoCommit(false);
            st = db.createStatement();
            BufferedReader sqlReader = new BufferedReader(new FileReader(SQLScript));
            String nextStatement;
            while ((nextStatement = sqlReader.readLine()) != null)
                st.addBatch(nextStatement);
            st.executeBatch();
            db.commit();
            db.setAutoCommit(true);
            this.window.printMessage("Schema initialized.");
        } catch (SQLException ex) {
            if (db != null) try { db.rollback(); window.printMessage("Rolling back SQL initialization: "+ex.getLocalizedMessage()); } catch (SQLException ex1) { throw ex1; }
        } finally {
            try {
                if (st != null) st.close();
                if (db != null) db.close();
            } catch (SQLException ex) { throw ex; }
        }
    }
}
