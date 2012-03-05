package terminal;

import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.RecordStore;

public class TerminalMIDlet extends MIDlet implements CommandListener {
    private StationConnection stationConnection;
    private ChannelConnection channelConnection;
    private Seraphim seraphim;
    private Thread stationThread, channelThread, seraphimThread;
    private boolean midletPaused = false;
    private RecordStore detailsRS = null;
    private RecordStore prefsRS = null;
    private String IMEI, IMSI, clockFrequency, memory, operatingSystem, networkCapabilities;
    private String name, surname, userAddress, chargeModel, services;
    private boolean automaticConnections = false;
//<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private Command exitCommand;
    private Command cancelCommand;
    private Command startCommand;
    private Command okCommand;
    private Command backCommand;
    private Command backToMainCommand;
    private Command yesCommand;
    private Command noCommand;
    private Form prefsForm;
    private ChoiceGroup choiceGroup;
    private TextField textField2;
    private TextField textField1;
    private TextField textField;
    private ChoiceGroup choiceGroup1;
    private ChoiceGroup choiceGroup2;
    private List mainCard;
    private Form deviceForm;
    private StringItem stringItem;
    private StringItem stringItem5;
    private StringItem stringItem3;
    private StringItem stringItem4;
    private StringItem stringItem1;
    private StringItem stringItem2;
    private List connectForm;
    private Alert chargeAlert;
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
        if (displayable == chargeAlert) {//GEN-BEGIN:|7-commandAction|1|117-preAction
            if (command == noCommand) {//GEN-END:|7-commandAction|1|117-preAction
                // write pre-action user code here
                switchDisplayable(null, getPrefsForm());//GEN-LINE:|7-commandAction|2|117-postAction
                // write post-action user code here
            } else if (command == yesCommand) {//GEN-LINE:|7-commandAction|3|115-preAction
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
                if (prefs[4].equals("")) {
                    Alert alert = new Alert("Missing info", "You must choose at least one preferred service.", null, AlertType.ERROR);
                    alert.setTimeout(Alert.FOREVER);
                    this.switchDisplayable(null, alert);
                    return;
                }
                for (int i = 0; i < this.choiceGroup2.size(); i++)
                    if (this.choiceGroup2.isSelected(i))
                        prefs[5] = (this.choiceGroup2.getString(i).equals("Yes"))?"AUTOMATIC":"MANUAL";
                this.updatePreferencesRecordStore(prefs, false);
                this.disconnectFromStation(true);
                switchDisplayable(null, getMainCard());//GEN-LINE:|7-commandAction|4|115-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|5|102-preAction
        } else if (displayable == connectForm) {
            if (command == List.SELECT_COMMAND) {//GEN-END:|7-commandAction|5|102-preAction
                // write pre-action user code here
                connectFormAction();//GEN-LINE:|7-commandAction|6|102-postAction
                // write post-action user code here
            } else if (command == backToMainCommand) {//GEN-LINE:|7-commandAction|7|107-preAction
                // write pre-action user code here
                switchDisplayable(null, getMainCard());//GEN-LINE:|7-commandAction|8|107-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|9|92-preAction
        } else if (displayable == deviceForm) {
            if (command == backCommand) {//GEN-END:|7-commandAction|9|92-preAction
                // write pre-action user code here
                switchDisplayable(null, getMainCard());//GEN-LINE:|7-commandAction|10|92-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|11|66-preAction
        } else if (displayable == mainCard) {
            if (command == List.SELECT_COMMAND) {//GEN-END:|7-commandAction|11|66-preAction
                // write pre-action user code here
                mainCardAction();//GEN-LINE:|7-commandAction|12|66-postAction
                // write post-action user code here
            } else if (command == exitCommand) {//GEN-LINE:|7-commandAction|13|77-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|14|77-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|15|47-preAction
        } else if (displayable == prefsForm) {
            if (command == cancelCommand) {//GEN-END:|7-commandAction|15|47-preAction
                // write pre-action user code here
                switchDisplayable(null, getMainCard());//GEN-LINE:|7-commandAction|16|47-postAction
                // write post-action user code here
            } else if (command == okCommand) {//GEN-LINE:|7-commandAction|17|83-preAction
                if (this.textField.getString().equals("")) {
                    Alert alert = new Alert("Missing info", "Please fill in your name.", null, AlertType.ERROR);
                    alert.setTimeout(Alert.FOREVER);
                    this.switchDisplayable(null, alert);
                    return;
                }
                if (this.textField1.getString().equals("")) {
                    Alert alert = new Alert("Missing info", "Please fill in your surname.", null, AlertType.ERROR);
                    alert.setTimeout(Alert.FOREVER);
                    this.switchDisplayable(null, alert);
                    return;
                }
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
                if (prefs[4].equals("")) {
                    Alert alert = new Alert("Missing info", "You must choose at least one preferred service.", null, AlertType.ERROR);
                    alert.setTimeout(Alert.FOREVER);
                    this.switchDisplayable(null, alert);
                    return;
                }
                for (int i = 0; i < this.choiceGroup2.size(); i++)
                    if (this.choiceGroup2.isSelected(i))
                        prefs[5] = (this.choiceGroup2.getString(i).equals("Yes"))?"AUTOMATIC":"MANUAL";
                if (this.chargeModelIsDifferent(prefs[3])) {
                    switchDisplayable(null, this.getChargeAlert());
                    return;
                }
                this.updatePreferencesRecordStore(prefs, false);
                switchDisplayable(null, getMainCard());
//GEN-LINE:|7-commandAction|18|83-postAction
                // write post-action user code here
            } else if (command == startCommand) {//GEN-LINE:|7-commandAction|19|45-preAction
                if (this.textField.getString().equals("")) {
                    Alert alert = new Alert("Missing info", "Please fill in your name.", null, AlertType.ERROR);
                    alert.setTimeout(Alert.FOREVER);
                    this.switchDisplayable(null, alert);
                    return;
                }
                if (this.textField1.getString().equals("")) {
                    Alert alert = new Alert("Missing info", "Please fill in your surname.", null, AlertType.ERROR);
                    alert.setTimeout(Alert.FOREVER);
                    this.switchDisplayable(null, alert);
                    return;
                }
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
                if (prefs[4].equals("")) {
                    Alert alert = new Alert("Missing info", "You must choose at least one preferred service.", null, AlertType.ERROR);
                    alert.setTimeout(Alert.FOREVER);
                    this.switchDisplayable(null, alert);
                    return;
                }
                for (int i = 0; i < this.choiceGroup2.size(); i++)
                    if (this.choiceGroup2.isSelected(i))
                        prefs[5] = (this.choiceGroup2.getString(i).equals("Yes"))?"AUTOMATIC":"MANUAL";
                this.updatePreferencesRecordStore(prefs, true);
                switchDisplayable(null, getMainCard());
//GEN-LINE:|7-commandAction|20|45-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|21|7-postCommandAction
        }//GEN-END:|7-commandAction|21|7-postCommandAction
        // write post-action user code here
    }//GEN-BEGIN:|7-commandAction|22|
//</editor-fold>//GEN-END:|7-commandAction|22|

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
            choiceGroup1.setSelectedFlags(new boolean[]{true, false});//GEN-END:|59-getter|1|59-postInit
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
            choiceGroup2.setSelectedFlags(new boolean[]{false, true});//GEN-END:|62-getter|1|62-postInit
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
            mainCard.append("Device Capabilities", null);
            mainCard.append("Edit Preferences", null);
            mainCard.append("Connect to Network", null);
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
        String __selectedString = getMainCard().getString(getMainCard().getSelectedIndex());//GEN-BEGIN:|65-action|1|69-preAction
        if (__selectedString != null) {
            if (__selectedString.equals("Device Capabilities")) {//GEN-END:|65-action|1|69-preAction
                // write pre-action user code here
                switchDisplayable(null, getDeviceForm());//GEN-LINE:|65-action|2|69-postAction
                // write post-action user code here
            } else if (__selectedString.equals("Edit Preferences")) {//GEN-LINE:|65-action|3|68-preAction
                this.getUserPreferences(false);
//GEN-LINE:|65-action|4|68-postAction
                // write post-action user code here
            } else if (__selectedString.equals("Connect to Network")) {//GEN-LINE:|65-action|5|70-preAction
                this.channelThread.interrupt();
                try { Thread.currentThread().sleep(500); } catch (InterruptedException e) {}
                this.getConnectForm().deleteAll();
                Vector available = this.channelConnection.getBaseStations();
                for (Enumeration e = available.elements(); e.hasMoreElements();) {
                    DummyBS bs = (DummyBS)e.nextElement();
                    if (bs.getCharges().equals(this.chargeModel))
                        this.getConnectForm().append(bs.getSSID(), null);
                }
                switchDisplayable(null, getConnectForm());//GEN-LINE:|65-action|6|70-postAction
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

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: deviceForm ">//GEN-BEGIN:|84-getter|0|84-preInit
    /**
     * Returns an initiliazed instance of deviceForm component.
     * @return the initialized component instance
     */
    public Form getDeviceForm() {
        if (deviceForm == null) {//GEN-END:|84-getter|0|84-preInit
            // write pre-init user code here
            deviceForm = new Form("Device Capabilities", new Item[]{getStringItem(), getStringItem1(), getStringItem2(), getStringItem3(), getStringItem4(), getStringItem5()});//GEN-BEGIN:|84-getter|1|84-postInit
            deviceForm.addCommand(getBackCommand());
            deviceForm.setCommandListener(this);//GEN-END:|84-getter|1|84-postInit
            // write post-init user code here
        }//GEN-BEGIN:|84-getter|2|
        return deviceForm;
    }
//</editor-fold>//GEN-END:|84-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand ">//GEN-BEGIN:|91-getter|0|91-preInit
    /**
     * Returns an initiliazed instance of backCommand component.
     * @return the initialized component instance
     */
    public Command getBackCommand() {
        if (backCommand == null) {//GEN-END:|91-getter|0|91-preInit
            // write pre-init user code here
            backCommand = new Command("Back", Command.BACK, 0);//GEN-LINE:|91-getter|1|91-postInit
            // write post-init user code here
        }//GEN-BEGIN:|91-getter|2|
        return backCommand;
    }
//</editor-fold>//GEN-END:|91-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem ">//GEN-BEGIN:|95-getter|0|95-preInit
    /**
     * Returns an initiliazed instance of stringItem component.
     * @return the initialized component instance
     */
    public StringItem getStringItem() {
        if (stringItem == null) {//GEN-END:|95-getter|0|95-preInit
            // write pre-init user code here
            stringItem = new StringItem("IMEI:", null);//GEN-LINE:|95-getter|1|95-postInit
            // write post-init user code here
        }//GEN-BEGIN:|95-getter|2|
        return stringItem;
    }
//</editor-fold>//GEN-END:|95-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem1 ">//GEN-BEGIN:|96-getter|0|96-preInit
    /**
     * Returns an initiliazed instance of stringItem1 component.
     * @return the initialized component instance
     */
    public StringItem getStringItem1() {
        if (stringItem1 == null) {//GEN-END:|96-getter|0|96-preInit
            // write pre-init user code here
            stringItem1 = new StringItem("IMSI:", null);//GEN-LINE:|96-getter|1|96-postInit
            // write post-init user code here
        }//GEN-BEGIN:|96-getter|2|
        return stringItem1;
    }
//</editor-fold>//GEN-END:|96-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem2 ">//GEN-BEGIN:|97-getter|0|97-preInit
    /**
     * Returns an initiliazed instance of stringItem2 component.
     * @return the initialized component instance
     */
    public StringItem getStringItem2() {
        if (stringItem2 == null) {//GEN-END:|97-getter|0|97-preInit
            // write pre-init user code here
            stringItem2 = new StringItem("Network Capabilities:", null);//GEN-LINE:|97-getter|1|97-postInit
            // write post-init user code here
        }//GEN-BEGIN:|97-getter|2|
        return stringItem2;
    }
//</editor-fold>//GEN-END:|97-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem3 ">//GEN-BEGIN:|98-getter|0|98-preInit
    /**
     * Returns an initiliazed instance of stringItem3 component.
     * @return the initialized component instance
     */
    public StringItem getStringItem3() {
        if (stringItem3 == null) {//GEN-END:|98-getter|0|98-preInit
            // write pre-init user code here
            stringItem3 = new StringItem("Operating System:", null);//GEN-LINE:|98-getter|1|98-postInit
            // write post-init user code here
        }//GEN-BEGIN:|98-getter|2|
        return stringItem3;
    }
//</editor-fold>//GEN-END:|98-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem4 ">//GEN-BEGIN:|99-getter|0|99-preInit
    /**
     * Returns an initiliazed instance of stringItem4 component.
     * @return the initialized component instance
     */
    public StringItem getStringItem4() {
        if (stringItem4 == null) {//GEN-END:|99-getter|0|99-preInit
            // write pre-init user code here
            stringItem4 = new StringItem("CPU Clock:", null);//GEN-LINE:|99-getter|1|99-postInit
            // write post-init user code here
        }//GEN-BEGIN:|99-getter|2|
        return stringItem4;
    }
//</editor-fold>//GEN-END:|99-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem5 ">//GEN-BEGIN:|100-getter|0|100-preInit
    /**
     * Returns an initiliazed instance of stringItem5 component.
     * @return the initialized component instance
     */
    public StringItem getStringItem5() {
        if (stringItem5 == null) {//GEN-END:|100-getter|0|100-preInit
            // write pre-init user code here
            stringItem5 = new StringItem("RAM:", null);//GEN-LINE:|100-getter|1|100-postInit
            // write post-init user code here
        }//GEN-BEGIN:|100-getter|2|
        return stringItem5;
    }
//</editor-fold>//GEN-END:|100-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: backToMainCommand ">//GEN-BEGIN:|106-getter|0|106-preInit
    /**
     * Returns an initiliazed instance of backToMainCommand component.
     * @return the initialized component instance
     */
    public Command getBackToMainCommand() {
        if (backToMainCommand == null) {//GEN-END:|106-getter|0|106-preInit
            // write pre-init user code here
            backToMainCommand = new Command("Back", Command.BACK, 0);//GEN-LINE:|106-getter|1|106-postInit
            // write post-init user code here
        }//GEN-BEGIN:|106-getter|2|
        return backToMainCommand;
    }
//</editor-fold>//GEN-END:|106-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: connectForm ">//GEN-BEGIN:|101-getter|0|101-preInit
    /**
     * Returns an initiliazed instance of connectForm component.
     * @return the initialized component instance
     */
    public List getConnectForm() {
        if (connectForm == null) {//GEN-END:|101-getter|0|101-preInit
            // write pre-init user code here
            connectForm = new List("Connect to Station", Choice.IMPLICIT);//GEN-BEGIN:|101-getter|1|101-postInit
            connectForm.addCommand(getBackToMainCommand());
            connectForm.setCommandListener(this);
            connectForm.setFitPolicy(Choice.TEXT_WRAP_DEFAULT);//GEN-END:|101-getter|1|101-postInit
            // write post-init user code here
        }//GEN-BEGIN:|101-getter|2|
        return connectForm;
    }
//</editor-fold>//GEN-END:|101-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Method: connectFormAction ">//GEN-BEGIN:|101-action|0|101-preAction
    /**
     * Performs an action assigned to the selected list element in the connectForm component.
     */
    public void connectFormAction() {//GEN-END:|101-action|0|101-preAction
        // enter pre-action user code here
        String __selectedString = getConnectForm().getString(getConnectForm().getSelectedIndex());//GEN-LINE:|101-action|1|101-postAction
        if (__selectedString != null) {
            if (this.connectToStation(__selectedString))
                switchDisplayable(null, getMainCard());
            else {
                    Alert alert = new Alert("Station unavailable", "The station you selected has been taken out of range.\nPlease try with another station.", null, AlertType.WARNING);
                    alert.setTimeout(Alert.FOREVER);
                    this.getDisplay().setCurrent(alert);
            }
        }                                    
    }//GEN-BEGIN:|101-action|2|
//</editor-fold>//GEN-END:|101-action|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: chargeAlert ">//GEN-BEGIN:|111-getter|0|111-preInit
    /**
     * Returns an initiliazed instance of chargeAlert component.
     * @return the initialized component instance
     */
    public Alert getChargeAlert() {
        if (chargeAlert == null) {//GEN-END:|111-getter|0|111-preInit
            // write pre-init user code here
            chargeAlert = new Alert("Charge Model Change", "The new charge model you chose is different from that used by your current station.\\nIf you continue, you will be disconnected from your station and will have to reconnect.", null, AlertType.CONFIRMATION);//GEN-BEGIN:|111-getter|1|111-postInit
            chargeAlert.addCommand(getYesCommand());
            chargeAlert.addCommand(getNoCommand());
            chargeAlert.setCommandListener(this);
            chargeAlert.setTimeout(Alert.FOREVER);//GEN-END:|111-getter|1|111-postInit
            // write post-init user code here
        }//GEN-BEGIN:|111-getter|2|
        return chargeAlert;
    }
//</editor-fold>//GEN-END:|111-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: yesCommand ">//GEN-BEGIN:|114-getter|0|114-preInit
    /**
     * Returns an initiliazed instance of yesCommand component.
     * @return the initialized component instance
     */
    public Command getYesCommand() {
        if (yesCommand == null) {//GEN-END:|114-getter|0|114-preInit
            // write pre-init user code here
            yesCommand = new Command("Ok", Command.OK, 0);//GEN-LINE:|114-getter|1|114-postInit
            // write post-init user code here
        }//GEN-BEGIN:|114-getter|2|
        return yesCommand;
    }
//</editor-fold>//GEN-END:|114-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: noCommand ">//GEN-BEGIN:|116-getter|0|116-preInit
    /**
     * Returns an initiliazed instance of noCommand component.
     * @return the initialized component instance
     */
    public Command getNoCommand() {
        if (noCommand == null) {//GEN-END:|116-getter|0|116-preInit
            // write pre-init user code here
            noCommand = new Command("Cancel", Command.CANCEL, 0);//GEN-LINE:|116-getter|1|116-postInit
            // write post-init user code here
        }//GEN-BEGIN:|116-getter|2|
        return noCommand;
    }
//</editor-fold>//GEN-END:|116-getter|2|

    public Display getDisplay() {
        return Display.getDisplay(this);
    }

    public void exitMIDlet() {
        this.laySeraphimToRest();
        this.disconnectFromStation(true);
        this.disconnectFromDDChannel();
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
            this.channelConnection = null;
            this.channelThread = null;
            this.stationConnection = null;
            this.stationThread = null;
            this.seraphim = null;
            this.seraphimThread = null;
            this.connectToDDChannel();
            this.startSeraphim();
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
            this.getStringItem().setText("\n"+this.IMEI);
            this.getStringItem1().setText("\n"+this.IMSI);
            this.getStringItem2().setText("\n"+this.networkCapabilities);
            this.getStringItem3().setText("\n"+this.operatingSystem);
            this.getStringItem4().setText("\n"+this.clockFrequency);
            this.getStringItem5().setText("\n"+this.memory);
        } catch (Exception e) { }
    }

    private void preparePreferencesRecordStore() {
        try {
            this.prefsRS = RecordStore.openRecordStore("preferencesRecordStore", true);
            if (this.prefsRS.getNextRecordID() == 1) this.getUserPreferences(true);
            else {
                this.name = new String(this.prefsRS.getRecord(1));
                this.surname = new String(this.prefsRS.getRecord(2));
                this.userAddress = new String(this.prefsRS.getRecord(3));
                this.chargeModel = new String(this.prefsRS.getRecord(4));
                this.services = new String(this.prefsRS.getRecord(5));
                this.automaticConnections = (new String(this.prefsRS.getRecord(6)).equals("AUTOMATIC"))?true:false;
                this.updatePrefsForm();
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
            for (int i = 0; i < this.getMainCard().size(); i++)
                if (this.getMainCard().getString(i).equals("Connect to Network")) {
                    this.getMainCard().delete(i);
                }
            if (!this.automaticConnections) {
                this.seraphim.setActiveState(false);
                this.getMainCard().append("Connect to Network", null);
            } else this.seraphim.setActiveState(true);
            this.prefsRS.closeRecordStore();
        } catch (Exception e) {}
    }
    
    private void getUserPreferences(boolean mandatory) {
        Form theForm = this.getPrefsForm();
        if (mandatory) {
            theForm.removeCommand(this.getOkCommand());
            theForm.removeCommand(this.getCancelCommand());
            theForm.addCommand(this.getStartCommand());
        } else {
            this.updatePrefsForm();
            theForm.removeCommand(this.getStartCommand());
            theForm.addCommand(this.getCancelCommand());
            theForm.addCommand(this.getOkCommand());
        }
        switchDisplayable(null, getPrefsForm());
    }
    
    private void updatePrefsForm() {
        this.getTextField().setString(this.name);
        this.getTextField1().setString(this.surname);
        this.getTextField2().setString(this.userAddress);
        if (this.chargeModel.indexOf("FIXED") != -1) this.getChoiceGroup().setSelectedIndex(0, true);
        else this.getChoiceGroup().setSelectedIndex(0, false);
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
        if (this.services.indexOf("VOICE") != -1) this.getChoiceGroup1().setSelectedIndex(0, true);
        else this.getChoiceGroup1().setSelectedIndex(0, false);
        if (this.services.indexOf("DATA") != -1) this.choiceGroup1.setSelectedIndex(1, true);
        else this.choiceGroup1.setSelectedIndex(1, false);
        if (this.automaticConnections) { this.getChoiceGroup2().setSelectedIndex(0, true); this.choiceGroup2.setSelectedIndex(1, false); }
        else { this.getChoiceGroup2().setSelectedIndex(0, false); this.choiceGroup2.setSelectedIndex(1, true); }
    }
    
    private void connectToDDChannel() {
        if (this.channelConnection != null) return;
        this.channelConnection = new ChannelConnection(this, 5000, "localhost", 32000);
        this.channelThread = new Thread(this.channelConnection);
        this.channelConnection.setThread(this.channelThread);
        this.channelThread.start();
    }
    
    private void startSeraphim() {
        if (this.seraphim != null) return;
        this.seraphim = new Seraphim(this, this.channelConnection, 500);
        this.seraphimThread = new Thread(this.seraphim);
        this.seraphim.setThread(this.seraphimThread);
        this.seraphimThread.start();
    }

    private void disconnectFromDDChannel() {
        if (this.channelConnection == null) return;
        this.channelConnection.terminate();
        try { this.channelThread.join(); } catch (Exception e) {}
        this.channelConnection = null;
        this.channelThread = null;
    }
    
    private void laySeraphimToRest() {
        if (this.seraphim == null) return;
        this.seraphim.terminate();
        try { this.seraphimThread.join(); } catch (Exception e) {}
        this.seraphim = null;
        this.seraphimThread = null;
    }

    public boolean connectToStation(String SSID) {
        this.disconnectFromStation(true);
        Vector stations = this.channelConnection.getBaseStations();
        DummyBS bs = null;
        for (Enumeration e = stations.elements(); e.hasMoreElements();) {
            bs = (DummyBS)e.nextElement();
            if (bs.getSSID().equals(SSID)) break;
        }
        if (bs == null) return false;
        this.getTicker().setString("Connecting to "+SSID+"...");
        this.stationConnection = new StationConnection(this, bs);
        this.stationThread = new Thread(this.stationConnection);
        this.stationConnection.setThread(this.stationThread);
        this.stationThread.start();
        return true;
    }
    
    public void disconnectFromStation(boolean implicit) {
        if (this.stationConnection == null) return;
        if (implicit)
            this.stationConnection.terminate();
        try { this.stationThread.join(); } catch (Exception e) {}
        this.stationThread = null;
        this.stationConnection = null;
    }
    
    private boolean chargeModelIsDifferent(String chargeModel) {
        if (this.stationConnection == null) return false;
        if (!this.stationConnection.isConnected()) return false;
        return (!this.stationConnection.getBaseStation().getCharges().equals(chargeModel));
    }
    
    public String getConnectedBaseStationSSID() {
        if (this.stationConnection == null) return "";
        if (!this.stationConnection.isConnected()) return "";
        return this.stationConnection.getBaseStation().getSSID();
    }
    
    public boolean isConnectedToStation() {
        if ((this.stationConnection != null) && (this.stationConnection.isConnected())) return true;
        else return false;
    }
    
    public String getPreferredChargeModel() { return this.chargeModel; }
}
