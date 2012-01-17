package ddchannel;

import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollBar;

public class MainWindow extends FrameView {
    private DDChannel ddchannel;
    private Mutex mxMessageLog = new Mutex();

    public MainWindow(SingleFrameApplication app) {
        super(app);
        ddchannel = (DDChannel) app;

        initComponents();
        jBaseStations.setModel(ddchannel.getDatabaseTM());
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = DDChannel.getApplication().getMainFrame();
            aboutBox = new AboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        DDChannel.getApplication().show(aboutBox);
    }
    
    public synchronized void printMessage(String msg) {
        while (true) {
                try { this.mxMessageLog.lock(); } catch (InterruptedException e) { continue; }
                break;
        }
        JScrollBar vbar = jScrollPane1.getVerticalScrollBar();
        boolean autoScroll = ((vbar.getValue()+vbar.getVisibleAmount()) == vbar.getMaximum());
        this.jMessageLog.append(msg+"\n");
        if( autoScroll ) this.jMessageLog.setCaretPosition(this.jMessageLog.getDocument().getLength());

        this.jMessageLog.setCaretPosition(this.jMessageLog.getDocument().getLength());
        this.mxMessageLog.raise();
    }
    
    public synchronized void clearLog() { this.jMessageLog.setText(""); }

    private void setControls(boolean value) {
        jDBID.setEnabled(value);
        jDBPassword.setEnabled(value);
        jDBPort.setEnabled(value);
        jDBSchema.setEnabled(value);
        jDBServer.setEnabled(value);
        jListenerPort.setEnabled(value);
        jSQLInitScript.setEnabled(value);
        jThreads.setEnabled(value);
        jWSDLIP.setEnabled(value);
        jWSDLPort.setEnabled(value);
    }
    
    @Action
    public void toggleServer() {
        try {
            if (ddchannel.isActive()) {
                ddchannel.stopServer();
                jToggle.setText("Start");
                this.setControls(true);
            } else {
                ddchannel.startServer(Integer.parseInt(jListenerPort.getText()), jWSDLIP.getText(), Integer.parseInt(jWSDLPort.getText()),
                        Integer.parseInt(jThreads.getText()), jDBServer.getText(), Integer.parseInt(jDBPort.getText()), jDBID.getText(),
                        String.valueOf(jDBPassword.getPassword()), jDBSchema.getText(), jSQLInitScript.getText());
                jToggle.setText("Stop");
                this.setControls(false);
            }
        } catch (Exception e) { this.jMessageLog.append(e.getLocalizedMessage()); }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jToggle = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jWSDLIP = new javax.swing.JTextField();
        jWSDLPort = new javax.swing.JTextField();
        jListenerPort = new javax.swing.JTextField();
        jThreads = new javax.swing.JTextField();
        jDBServer = new javax.swing.JTextField();
        jDBPort = new javax.swing.JTextField();
        jDBID = new javax.swing.JTextField();
        jDBSchema = new javax.swing.JTextField();
        jSQLInitScript = new javax.swing.JTextField();
        jDBPassword = new javax.swing.JPasswordField();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jMessageLog = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jBaseStations = new javax.swing.JTable();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();

        mainPanel.setName("mainPanel"); // NOI18N

        jTabbedPane3.setName("jTabbedPane3"); // NOI18N

        jSplitPane1.setDividerLocation(350);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(327, 450));

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(ddchannel.DDChannel.class).getContext().getActionMap(MainWindow.class, this);
        jToggle.setAction(actionMap.get("toggleServer")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ddchannel.DDChannel.class).getContext().getResourceMap(MainWindow.class);
        jToggle.setText(resourceMap.getString("jToggle.text")); // NOI18N
        jToggle.setName("jToggle"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        jWSDLIP.setText(resourceMap.getString("jWSDLIP.text")); // NOI18N
        jWSDLIP.setName("jWSDLIP"); // NOI18N

        jWSDLPort.setText(resourceMap.getString("jWSDLPort.text")); // NOI18N
        jWSDLPort.setName("jWSDLPort"); // NOI18N

        jListenerPort.setText(resourceMap.getString("jListenerPort.text")); // NOI18N
        jListenerPort.setName("jListenerPort"); // NOI18N

        jThreads.setText(resourceMap.getString("jThreads.text")); // NOI18N
        jThreads.setName("jThreads"); // NOI18N

        jDBServer.setText(resourceMap.getString("jDBServer.text")); // NOI18N
        jDBServer.setName("jDBServer"); // NOI18N

        jDBPort.setText(resourceMap.getString("jDBPort.text")); // NOI18N
        jDBPort.setName("jDBPort"); // NOI18N

        jDBID.setText(resourceMap.getString("jDBID.text")); // NOI18N
        jDBID.setName("jDBID"); // NOI18N

        jDBSchema.setText(resourceMap.getString("jDBSchema.text")); // NOI18N
        jDBSchema.setName("jDBSchema"); // NOI18N

        jSQLInitScript.setText(resourceMap.getString("jSQLInitScript.text")); // NOI18N
        jSQLInitScript.setName("jSQLInitScript"); // NOI18N

        jDBPassword.setText(resourceMap.getString("jDBPassword.text")); // NOI18N
        jDBPassword.setName("jDBPassword"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(jLabel2)
                                .add(69, 69, 69)
                                .add(jWSDLIP, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                                .add(jLabel3)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 231, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(123, 123, 123)
                                .add(jWSDLPort, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                                .add(jLabel4)
                                .add(35, 35, 35)
                                .add(jListenerPort, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel5)
                                    .add(jLabel6)
                                    .add(jLabel7)
                                    .add(jLabel8)
                                    .add(jLabel9)
                                    .add(jLabel10)
                                    .add(jLabel11))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jDBPassword, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jSQLInitScript, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jDBSchema, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                                    .add(jDBID, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jDBPort, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jDBServer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                                    .add(jThreads, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))))
                        .add(8, 8, 8))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                        .add(jToggle)
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jWSDLIP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(jWSDLPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jListenerPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jThreads, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5))
                .add(8, 8, 8)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jDBServer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel6))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jDBPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel7))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jDBID, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel8))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel9)
                    .add(jDBPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jDBSchema, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel10))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jSQLInitScript, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel11))
                .add(18, 18, 18)
                .add(jToggle)
                .add(131, 131, 131))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        jSplitPane1.setLeftComponent(jTabbedPane1);

        jTabbedPane2.setName("jTabbedPane2"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jMessageLog.setColumns(20);
        jMessageLog.setLineWrap(true);
        jMessageLog.setRows(5);
        jMessageLog.setName("jMessageLog"); // NOI18N
        jScrollPane1.setViewportView(jMessageLog);

        jTabbedPane2.addTab(resourceMap.getString("jScrollPane1.TabConstraints.tabTitle"), jScrollPane1); // NOI18N

        jSplitPane1.setRightComponent(jTabbedPane2);

        jTabbedPane3.addTab(resourceMap.getString("jSplitPane1.TabConstraints.tabTitle"), jSplitPane1); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jBaseStations.setName("jBaseStations"); // NOI18N
        jBaseStations.setRowSelectionAllowed(false);
        jBaseStations.setShowGrid(true);
        jScrollPane2.setViewportView(jBaseStations);

        jTabbedPane3.addTab(resourceMap.getString("jScrollPane2.TabConstraints.tabTitle"), jScrollPane2); // NOI18N

        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jTabbedPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1105, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jTabbedPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        fileMenu.add(aboutMenuItem);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        setComponent(mainPanel);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        try { ddchannel.stopServer(); } catch (Exception e) { this.printMessage(e.getLocalizedMessage()); return; }
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable jBaseStations;
    private javax.swing.JTextField jDBID;
    private javax.swing.JPasswordField jDBPassword;
    private javax.swing.JTextField jDBPort;
    private javax.swing.JTextField jDBSchema;
    private javax.swing.JTextField jDBServer;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jListenerPort;
    private javax.swing.JTextArea jMessageLog;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jSQLInitScript;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTextField jThreads;
    private javax.swing.JButton jToggle;
    private javax.swing.JTextField jWSDLIP;
    private javax.swing.JTextField jWSDLPort;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables

    private JDialog aboutBox;
}
