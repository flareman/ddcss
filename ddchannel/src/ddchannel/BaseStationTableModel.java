package ddchannel;

import java.util.HashMap;
import javax.swing.table.AbstractTableModel;

final public class BaseStationTableModel extends AbstractTableModel {
    private final String columnNames[] = 
            new String[] {
                "NetworkID", "SSID", "Power", "Frequency",
                "Type", "Max Bitrate", "Guaranteed Bitrate", "Current Load",
                "Provider", "Range", "Longtitude", "Latitude", "Port", "Charges"
            };

    private HashMap<String, DummyBS> baseStations;

    public int getColumnCount() {
        return columnNames.length;
    }

    public BaseStationTableModel(HashMap<String, DummyBS> input) {
        super();
        this.baseStations = input;
    }

    public int getRowCount() {
        return baseStations.size();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        DummyBS bs = (DummyBS)((baseStations.values().toArray())[row]);
        switch (col) {
            case 0: return bs.getNetworkID();
            case 1: return bs.getSSID();
            case 2: return bs.getPower();
            case 3: return bs.getFrequency();
            case 4: return bs.getType();
            case 5: return bs.getMaxBr();
            case 6: return bs.getGuaranteedBr();
            case 7: return bs.getLoad().toString()+"%";
            case 8: return bs.getProvider();
            case 9: return bs.getRange();
            case 10: return bs.getX();
            case 11: return bs.getY();
            case 12: return bs.getPort();
            case 13: return bs.getCharges();
            default: return "";
        }
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
    }
}
