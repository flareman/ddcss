/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package terminal;

import javax.microedition.io.Connector;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.RecordStore;

public class TerminalMIDlet extends MIDlet implements CommandListener {
    private StationConnection stationConnection;
    private Thread stationThread;
    private boolean midletPaused = false;
    private RecordStore detailsRS = null;
    private RecordStore prefsRS = null;
    private String IMEI, IMSI, clockFrequency, memory, operatingSystem, networkCapabilities;
    private String name, surname, userAddress, chargeModel, services;
    private boolean automaticConnections;
//<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private Command exitCommand;
    private Command startCommand;
    private Command cancelCommand;
    private Command okCommand;
    private Form prefsForm;
    private TextField textField;
    private TextField textField1;
    private TextField textField2;
    private ChoiceGroup choiceGroup;
    private ChoiceGroup choiceGroup1;
    private ChoiceGroup choiceGroup2;
    private List mainCard;
    private Ticker ticker;
//</editor-fold>//GEN-END:|fields|0|

    public TerminalMIDlet() {
    }

//<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
//</editor-fold>//GEN-END:|methods|0|
//<editor-fold defaultstate="collapsed" desc=" Generated Method: initialize ">//GEN-BEGIN:|0-initialize|0|0-preInitialize
    /**
     * Initializes the application.
     * It is called only once when the MIDlet is started. The method is called before the <code>startMIDlet</code> method.
     */
    private void initialize() {//GEN-END:|0-initialize|0|0-preInitialize
        // write pre-initialize user code here
//GEN-LINE:|0-initialize|1|0-postInitialize
        // write post-initialize user code here
    }//GEN-BEGIN:|0-initialize|2|
//</editor-fold>//GEN-END:|0-initialize|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Method: startMIDlet ">//GEN-BEGIN:|3-startMIDlet|0|3-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     */
    public void startMIDlet() {//GEN-END:|3-startMIDlet|0|3-preAction
        // write pre-action user code here
        switchDisplayable(null, getMainCard());//GEN-LINE:|3-startMIDlet|1|3-postAction
        // write post-action user code here
    }//GEN-BEGIN:|3-startMIDlet|2|
//</editor-fold>//GEN-END:|3-startMIDlet|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Method: resumeMIDlet ">//GEN-BEGIN:|4-resumeMIDlet|0|4-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
     */
    public void resumeMIDlet() {//GEN-END:|4-resumeMIDlet|0|4-preAction
        // write pre-action user code here
//GEN-LINE:|4-resumeMIDlet|1|4-postAction
        // write post-action user code here
    }//GEN-BEGIN:|4-resumeMIDlet|2|
//</editor-fold>//GEN-END:|4-resumeMIDlet|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Method: switchDisplayable ">//GEN-BEGIN:|5-switchDisplayable|0|5-preSwitch
    /**
     * Switches a current displayable in a display. The <code>display</code> instance is taken from <code>getDisplay</code> method. This method is used by all actions in the design for switching displayable.
     * @param alert the Alert which is temporarily set to the display; if <code>null</code>, then <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     */
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {//GEN-END:|5-switchDisplayable|0|5-preSwitch
        // write pre-switch user code here
        Display display = getDisplay();//GEN-BEGIN:|5-switchDisplayable|1|5-postSwitch
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }//GEN-END:|5-switchDisplayable|1|5-postSwitch
        // write post-switch user code here
    }//GEN-BEGIN:|5-switchDisplayable|2|
//</editor-fold>//GEN-END:|5-switchDisplayable|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Method: commandAction for Displayables ">//GEN-BEGIN:|7-commandAction|0|7-preCommandAction
    /**
     * Called by a system to indicated that a command has been invoked on a particular displayable.
     * @param command the Command that was invoked
     * @param displayable the Displayable where the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:|7-commandAction|0|7-preCommandAction
        // write pre-action user code here
        if (displayable == mainCard) {//GEN-BEGIN:|7-commandAction|1|66-preAction
            if (command == List.SELECT_COMMAND) {//GEN-END:|7-commandAction|1|66-preAction
                // write pre-action user code here
                mainCardAction();//GEN-LINE:|7-commandAction|2|66-postAction
                // write post-action user code here
            } else if (command == exitCommand) {//GEN-LINE:|7-commandAction|3|77-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|4|77-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|5|47-preAction
        } else if (displayable == prefsForm) {
            if (command == cancelCommand) {//GEN-END:|7-commandAction|5|47-preAction
                // write pre-action user code here
                switchDisplayable(null, getMainCard());//GEN-LINE:|7-commandAction|6|47-postAction
                // write post-action user code here
            } else if (command == okCommand) {//GEN-LINE:|7-commandAction|7|83-preAction
                String[] prefs = new String[6];
                prefs[0] = this.textField.getString();
                prefs[1] = this.textField1.getString();
                prefs[2] = this.textField2.getString();
                for (int i = 0; i < this.choiceGroup.size(); i++)
                    if (this.choiceGroup.isSelected(i))
                        prefs[3] = this.choiceGroup.getString(i);
                prefs[4] = "";
                for (int i = 0; i < this.choiceGroup1.size(); i++) {
                    if (!prefs[4].equals("")) prefs[4] += " ";
                    if (this.choiceGroup1.isSelected(i))
                        prefs[4] += this.choiceGroup1.getString(i);
                }
                for (int i = 0; i < this.choiceGroup2.size(); i++)
                    if (this.choiceGroup2.isSelected(i))
                        prefs[5] = (this.choiceGroup.getString(i).equals("Yes"))?"AUTOMATIC":"MANUAL";
                this.updatePreferencesRecordStore(prefs, false);
                switchDisplayable(null, getMainCard());
//GEN-LINE:|7-commandAction|8|83-postAction
                // write post-action user code here
            } else if (command == startCommand) {//GEN-LINE:|7-commandAction|9|45-preAction
                String[] prefs = new String[6];
                prefs[0] = this.textField.getString();
                prefs[1] = this.textField1.getString();
                prefs[2] = this.textField2.getString();
                for (int i = 0; i < this.choiceGroup.size(); i++)
                    if (this.choiceGroup.isSelected(i))
                        prefs[3] = this.choiceGroup.getString(i);
                prefs[4] = "";
                for (int i = 0; i < this.choiceGroup1.size(); i++) {
                    if (!prefs[4].equals("")) prefs[4] += " ";
                    if (this.choiceGroup1.isSelected(i))
                        prefs[4] += this.choiceGroup1.getString(i);
                }
                for (int i = 0; i < this.choiceGroup2.size(); i++)
                    if (this.choiceGroup2.isSelected(i))
                        prefs[5] = (this.choiceGroup.getString(i).equals("Yes"))?"AUTOMATIC":"MANUAL";
                this.updatePreferencesRecordStore(prefs, true);
                switchDisplayable(null, getMainCard());
//GEN-LINE:|7-commandAction|10|45-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|11|7-postCommandAction
        }//GEN-END:|7-commandAction|11|7-postCommandAction
        // write post-action user code here
    }//GEN-BEGIN:|7-commandAction|12|
//</editor-fold>//GEN-END:|7-commandAction|12|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: exitCommand ">//GEN-BEGIN:|18-getter|0|18-preInit
    /**
     * Returns an initiliazed instance of exitCommand component.
     * @return the initialized component instance
     */
    public Command getExitCommand() {
        if (exitCommand == null) {//GEN-END:|18-getter|0|18-preInit
            // write pre-init user code here
            exitCommand = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|18-getter|1|18-postInit
            // write post-init user code here
        }//GEN-BEGIN:|18-getter|2|
        return exitCommand;
    }
//</editor-fold>//GEN-END:|18-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: prefsForm ">//GEN-BEGIN:|43-getter|0|43-preInit
    /**
     * Returns an initiliazed instance of prefsForm component.
     * @return the initialized component instance
     */
    public Form getPrefsForm() {
        if (prefsForm == null) {//GEN-END:|43-getter|0|43-preInit
            // write pre-init user code here
            prefsForm = new Form("User Preferences", new Item[]{getTextField(), getTextField1(), getTextField2(), getChoiceGroup(), getChoiceGroup1(), getChoiceGroup2()});//GEN-BEGIN:|43-getter|1|43-postInit
            prefsForm.addCommand(getStartCommand());
            prefsForm.addCommand(getCancelCommand());
            prefsForm.addCommand(getOkCommand());
            prefsForm.setCommandListener(this);//GEN-END:|43-getter|1|43-postInit
            prefsForm.removeCommand(getCancelCommand());
            prefsForm.removeCommand(getStartCommand());
            prefsForm.removeCommand(getOkCommand());
        }//GEN-BEGIN:|43-getter|2|
        return prefsForm;
    }
//</editor-fold>//GEN-END:|43-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: startCommand ">//GEN-BEGIN:|44-getter|0|44-preInit
    /**
     * Returns an initiliazed instance of startCommand component.
     * @return the initialized component instance
     */
    public Command getStartCommand() {
        if (startCommand == null) {//GEN-END:|44-getter|0|44-preInit
            // write pre-init user code here
            startCommand = new Command("Start", Command.OK, 0);//GEN-LINE:|44-getter|1|44-postInit
            // write post-init user code here
        }//GEN-BEGIN:|44-getter|2|
        return startCommand;
    }
//</editor-fold>//GEN-END:|44-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: cancelCommand ">//GEN-BEGIN:|46-getter|0|46-preInit
    /**
     * Returns an initiliazed instance of cancelCommand component.
     * @return the initialized component instance
     */
    public Command getCancelCommand() {
        if (cancelCommand == null) {//GEN-END:|46-getter|0|46-preInit
            // write pre-init user code here
            cancelCommand = new Command("Cancel", Command.CANCEL, 0);//GEN-LINE:|46-getter|1|46-postInit
            // write post-init user code here
        }//GEN-BEGIN:|46-getter|2|
        return cancelCommand;
    }
//</editor-fold>//GEN-END:|46-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField ">//GEN-BEGIN:|48-getter|0|48-preInit
    /**
     * Returns an initiliazed instance of textField component.
     * @return the initialized component instance
     */
    public TextField getTextField() {
        if (textField == null) {//GEN-END:|48-getter|0|48-preInit
            // write pre-init user code here
            textField = new TextField("Name", null, 32, TextField.ANY);//GEN-LINE:|48-getter|1|48-postInit
            // write post-init user code here
        }//GEN-BEGIN:|48-getter|2|
        return textField;
    }
//</editor-fold>//GEN-END:|48-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField1 ">//GEN-BEGIN:|49-getter|0|49-preInit
    /**
     * Returns an initiliazed instance of textField1 component.
     * @return the initialized component instance
     */
    public TextField getTextField1() {
        if (textField1 == null) {//GEN-END:|49-getter|0|49-preInit
            // write pre-init user code here
            textField1 = new TextField("Surname", null, 32, TextField.ANY);//GEN-LINE:|49-getter|1|49-postInit
            // write post-init user code here
        }//GEN-BEGIN:|49-getter|2|
        return textField1;
    }
//</editor-fold>//GEN-END:|49-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField2 ">//GEN-BEGIN:|50-getter|0|50-preInit
    /**
     * Returns an initiliazed instance of textField2 component.
     * @return the initialized component instance
     */
    public TextField getTextField2() {
        if (textField2 == null) {//GEN-END:|50-getter|0|50-preInit
            // write pre-init user code here
            textField2 = new TextField("Address", null, 32, TextField.ANY);//GEN-LINE:|50-getter|1|50-postInit
            // write post-init user code here
        }//GEN-BEGIN:|50-getter|2|
        return textField2;
    }
//</editor-fold>//GEN-END:|50-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: choiceGroup ">//GEN-BEGIN:|51-getter|0|51-preInit
    /**
     * Returns an initiliazed instance of choiceGroup component.
     * @return the initialized component instance
     */
    public ChoiceGroup getChoiceGroup() {
        if (choiceGroup == null) {//GEN-END:|51-getter|0|51-preInit
            // write pre-init user code here
            choiceGroup = new ChoiceGroup("Charge Model", Choice.POPUP);//GEN-BEGIN:|51-getter|1|51-postInit
            choiceGroup.append("FIXED", null);
            choiceGroup.append("METERED", null);
            choiceGroup.append("PACKET", null);
            choiceGroup.append("EXPECTED", null);
            choiceGroup.append("EDGE", null);
            choiceGroup.append("PARIS", null);
            choiceGroup.append("AUCTION", null);
            choiceGroup.setFitPolicy(Choice.TEXT_WRAP_DEFAULT);
            choiceGroup.setSelectedFlags(new boolean[]{true, false, false, false, false, false, false});//GEN-END:|51-getter|1|51-postInit
            // write post-init user code here
        }//GEN-BEGIN:|51-getter|2|
        return choiceGroup;
    }
//</editor-fold>//GEN-END:|51-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: choiceGroup1 ">//GEN-BEGIN:|59-getter|0|59-preInit
    /**
     * Returns an initiliazed instance of choiceGroup1 component.
     * @return the initialized component instance
     */
    public ChoiceGroup getChoiceGroup1() {
        if (choiceGroup1 == null) {//GEN-END:|59-getter|0|59-preInit
            // write pre-init user code here
            choiceGroup1 = new ChoiceGroup("Preferred Services", Choice.MULTIPLE);//GEN-BEGIN:|59-getter|1|59-postInit
            choiceGroup1.append("VOICE", null);
            choiceGroup1.append("DATA", null);
            choiceGroup1.setSelectedFlags(new boolean[]{false, false});//GEN-END:|59-getter|1|59-postInit
            // write post-init user code here
        }//GEN-BEGIN:|59-getter|2|
        return choiceGroup1;
    }
//</editor-fold>//GEN-END:|59-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: choiceGroup2 ">//GEN-BEGIN:|62-getter|0|62-preInit
    /**
     * Returns an initiliazed instance of choiceGroup2 component.
     * @return the initialized component instance
     */
    public ChoiceGroup getChoiceGroup2() {
        if (choiceGroup2 == null) {//GEN-END:|62-getter|0|62-preInit
            // write pre-init user code here
            choiceGroup2 = new ChoiceGroup("Enable autoconnect:", Choice.EXCLUSIVE);//GEN-BEGIN:|62-getter|1|62-postInit
            choiceGroup2.append("Yes", null);
            choiceGroup2.append("No", null);
            choiceGroup2.setSelectedFlags(new boolean[]{true, false});//GEN-END:|62-getter|1|62-postInit
            // write post-init user code here
        }//GEN-BEGIN:|62-getter|2|
        return choiceGroup2;
    }
//</editor-fold>//GEN-END:|62-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: mainCard ">//GEN-BEGIN:|65-getter|0|65-preInit
    /**
     * Returns an initiliazed instance of mainCard component.
     * @return the initialized component instance
     */
    public List getMainCard() {
        if (mainCard == null) {//GEN-END:|65-getter|0|65-preInit
            // write pre-init user code here
            mainCard = new List("Terminal Application", Choice.IMPLICIT);//GEN-BEGIN:|65-getter|1|65-postInit
            mainCard.append("Connect to Network", null);
            mainCard.append("Device Capabilities", null);
            mainCard.append("Edit Preferences", null);
            mainCard.setTicker(getTicker());
            mainCard.addCommand(getExitCommand());
            mainCard.setCommandListener(this);
            mainCard.setSelectedFlags(new boolean[]{false, false, false});//GEN-END:|65-getter|1|65-postInit
            // write post-init user code here
        }//GEN-BEGIN:|65-getter|2|
        return mainCard;
    }
//</editor-fold>//GEN-END:|65-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Method: mainCardAction ">//GEN-BEGIN:|65-action|0|65-preAction
    /**
     * Performs an action assigned to the selected list element in the mainCard component.
     */
    public void mainCardAction() {//GEN-END:|65-action|0|65-preAction
        // enter pre-action user code here
        String __selectedString = getMainCard().getString(getMainCard().getSelectedIndex());//GEN-BEGIN:|65-action|1|70-preAction
        if (__selectedString != null) {
            if (__selectedString.equals("Connect to Network")) {//GEN-END:|65-action|1|70-preAction
                // write pre-action user code here
//GEN-LINE:|65-action|2|70-postAction
                // write post-action user code here
            } else if (__selectedString.equals("Device Capabilities")) {//GEN-LINE:|65-action|3|69-preAction
                // write pre-action user code here
//GEN-LINE:|65-action|4|69-postAction
                // write post-action user code here
            } else if (__selectedString.equals("Edit Preferences")) {//GEN-LINE:|65-action|5|68-preAction
                Form theForm = this.getPrefsForm();
                theForm.removeCommand(this.getStartCommand());
                theForm.addCommand(this.getCancelCommand());
                theForm.addCommand(this.getOkCommand());
                switchDisplayable(null, getPrefsForm());
//GEN-LINE:|65-action|6|68-postAction
                // write post-action user code here
            }//GEN-BEGIN:|65-action|7|65-postAction
        }//GEN-END:|65-action|7|65-postAction
        // enter post-action user code here
    }//GEN-BEGIN:|65-action|8|
//</editor-fold>//GEN-END:|65-action|8|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: ticker ">//GEN-BEGIN:|71-getter|0|71-preInit
    /**
     * Returns an initiliazed instance of ticker component.
     * @return the initialized component instance
     */
    public Ticker getTicker() {
        if (ticker == null) {//GEN-END:|71-getter|0|71-preInit
            // write pre-init user code here
            ticker = new Ticker("Disconnected from Network");//GEN-LINE:|71-getter|1|71-postInit
            // write post-init user code here
        }//GEN-BEGIN:|71-getter|2|
        return ticker;
    }
//</editor-fold>//GEN-END:|71-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: okCommand ">//GEN-BEGIN:|82-getter|0|82-preInit
    /**
     * Returns an initiliazed instance of okCommand component.
     * @return the initialized component instance
     */
    public Command getOkCommand() {
        if (okCommand == null) {//GEN-END:|82-getter|0|82-preInit
            // write pre-init user code here
            okCommand = new Command("Ok", Command.OK, 0);//GEN-LINE:|82-getter|1|82-postInit
            // write post-init user code here
        }//GEN-BEGIN:|82-getter|2|
        return okCommand;
    }
//</editor-fold>//GEN-END:|82-getter|2|

    public Display getDisplay() {
        return Display.getDisplay(this);
    }

    public void exitMIDlet() {
        switchDisplayable(null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    public void startApp() {
        if (midletPaused) {
            resumeMIDlet();
        } else {
            initialize();
            startMIDlet();
            this.initializeDetailsRecordStore();
            this.preparePreferencesRecordStore();
            this.stationConnection = null;
            this.stationThread = null;
        }
        midletPaused = false;
    }

    public void pauseApp() {
        midletPaused = true;
    }

    public void destroyApp(boolean unconditional) {
    }
        
    public String getIMEI() { return this.IMEI; }
    public String getIMSI() { return this.IMSI; }
    public String getNetworkCapabilities() { return this.networkCapabilities; }
    public Float getX() { return new Float(5.0f); }
    public Float getY() { return new Float(5.0f); }
    
    private void initializeDetailsRecordStore() {
        try {
            this.detailsRS = RecordStore.openRecordStore("detailsRecordStore", true);
            Properties deviceProperties = Properties.loadProperties("device.properties");
            this.IMEI = deviceProperties.getProperty("IMEI");
            this.IMSI = deviceProperties.getProperty("IMSI");
            this.networkCapabilities = deviceProperties.getProperty("NetworkCapabilities");
            this.operatingSystem = deviceProperties.getProperty("OperatingSystem");
            this.clockFrequency = deviceProperties.getProperty("CPU");
            this.memory = deviceProperties.getProperty("RAM");
            if (this.detailsRS.getNextRecordID() == 1) {
                this.detailsRS.addRecord(this.IMEI.getBytes(), 0, this.IMEI.getBytes().length);
                this.detailsRS.addRecord(this.IMSI.getBytes(), 0, this.IMSI.getBytes().length);
                this.detailsRS.addRecord(this.networkCapabilities.getBytes(), 0, this.networkCapabilities.getBytes().length);
                this.detailsRS.addRecord(this.operatingSystem.getBytes(), 0, this.operatingSystem.getBytes().length);
                this.detailsRS.addRecord(this.clockFrequency.getBytes(), 0, this.clockFrequency.getBytes().length);
                this.detailsRS.addRecord(this.memory.getBytes(), 0, this.memory.getBytes().length);
            } else {
                this.detailsRS.setRecord(1, this.IMEI.getBytes(), 0, this.IMEI.getBytes().length);
                this.detailsRS.setRecord(2, this.IMSI.getBytes(), 0, this.IMSI.getBytes().length);
                this.detailsRS.setRecord(3, this.networkCapabilities.getBytes(), 0, this.networkCapabilities.getBytes().length);
                this.detailsRS.setRecord(4, this.operatingSystem.getBytes(), 0, this.operatingSystem.getBytes().length);
                this.detailsRS.setRecord(5, this.clockFrequency.getBytes(), 0, this.clockFrequency.getBytes().length);
                this.detailsRS.setRecord(6, this.memory.getBytes(), 0, this.memory.getBytes().length);
            }
            this.detailsRS.closeRecordStore();
        } catch (Exception e) {}
    }

    private void preparePreferencesRecordStore() {
        try {
            this.prefsRS = RecordStore.openRecordStore("preferencesRecordStore", true);
            if (this.prefsRS.getNextRecordID() == 1) this.getUserPreferences(true);
            else {
                this.name = new String(this.prefsRS.getRecord(1));
                this.textField.setString(this.name);
                this.surname = new String(this.prefsRS.getRecord(2));
                this.textField1.setString(this.surname);
                this.userAddress = new String(this.prefsRS.getRecord(3));
                this.textField2.setString(this.userAddress);
                this.chargeModel = new String(this.prefsRS.getRecord(4));
                if (this.chargeModel.indexOf("FIXED") != -1) this.choiceGroup.setSelectedIndex(0, true);
                else this.choiceGroup.setSelectedIndex(0, false);
                if (this.chargeModel.indexOf("METERED") != -1) this.choiceGroup.setSelectedIndex(1, true);
                else this.choiceGroup.setSelectedIndex(1, false);
                if (this.chargeModel.indexOf("PACKET") != -1) this.choiceGroup.setSelectedIndex(2, true);
                else this.choiceGroup.setSelectedIndex(2, false);
                if (this.chargeModel.indexOf("EXPECTED") != -1) this.choiceGroup.setSelectedIndex(3, true);
                else this.choiceGroup.setSelectedIndex(3, false);
                if (this.chargeModel.indexOf("EDGE") != -1) this.choiceGroup.setSelectedIndex(4, true);
                else this.choiceGroup.setSelectedIndex(4, false);
                if (this.chargeModel.indexOf("PARIS") != -1) this.choiceGroup.setSelectedIndex(5, true);
                else this.choiceGroup.setSelectedIndex(5, false);
                if (this.chargeModel.indexOf("AUCTION") != -1) this.choiceGroup.setSelectedIndex(6, true);
                else this.choiceGroup.setSelectedIndex(6, false);
                this.services = new String(this.prefsRS.getRecord(5));
                if (this.services.indexOf("VOICE") != -1) this.choiceGroup1.setSelectedIndex(0, true);
                else this.choiceGroup1.setSelectedIndex(0, false);
                if (this.services.indexOf("DATA") != -1) this.choiceGroup1.setSelectedIndex(1, true);
                else this.choiceGroup1.setSelectedIndex(1, false);
                this.automaticConnections = (new String(this.prefsRS.getRecord(6)).equals("AUTOMATIC"))?true:false;
                if (this.automaticConnections) { this.choiceGroup2.setSelectedIndex(0, true); this.choiceGroup2.setSelectedIndex(1, false); }
                else { this.choiceGroup2.setSelectedIndex(0, false); this.choiceGroup2.setSelectedIndex(1, true); }
            }
            this.prefsRS.closeRecordStore();
        } catch (Exception e) {}
    }
    
    private void updatePreferencesRecordStore(String[] prefs, boolean initialize) {
        try {
            this.prefsRS = RecordStore.openRecordStore("preferencesRecordStore", true);
            if (initialize) {
                this.prefsRS.addRecord(prefs[0].getBytes(), 0, prefs[0].getBytes().length);
                this.prefsRS.addRecord(prefs[1].getBytes(), 0, prefs[1].getBytes().length);
                this.prefsRS.addRecord(prefs[2].getBytes(), 0, prefs[2].getBytes().length);
                this.prefsRS.addRecord(prefs[3].getBytes(), 0, prefs[3].getBytes().length);
                this.prefsRS.addRecord(prefs[4].getBytes(), 0, prefs[4].getBytes().length);
                this.prefsRS.addRecord(prefs[5].getBytes(), 0, prefs[5].getBytes().length);
            } else {
                this.prefsRS.setRecord(1, prefs[0].getBytes(), 0, prefs[0].getBytes().length);
                this.prefsRS.setRecord(2, prefs[1].getBytes(), 0, prefs[1].getBytes().length);
                this.prefsRS.setRecord(3, prefs[2].getBytes(), 0, prefs[2].getBytes().length);
                this.prefsRS.setRecord(4, prefs[3].getBytes(), 0, prefs[3].getBytes().length);
                this.prefsRS.setRecord(5, prefs[4].getBytes(), 0, prefs[4].getBytes().length);
                this.prefsRS.setRecord(6, prefs[5].getBytes(), 0, prefs[5].getBytes().length);
            }
            this.name = prefs[0];
            this.surname = prefs[1];
            this.userAddress = prefs[2];
            this.chargeModel = prefs[3];
            this.services = prefs[4];
            this.automaticConnections = (prefs[5].equals("AUTOMATIC"))?true:false;
            this.prefsRS.closeRecordStore();
        } catch (Exception e) {}
    }
    
    private void getUserPreferences(boolean mandatory) {
        Form theForm = this.getPrefsForm();
        if (mandatory)
            theForm.addCommand(this.getStartCommand());
        else {
            theForm.addCommand(this.getCancelCommand());
            theForm.addCommand(this.getOkCommand());
        }
        switchDisplayable(null, getPrefsForm());
    }
}
