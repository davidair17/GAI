package GAI.hibernate;

import javax.swing.table.DefaultTableModel;

public class EditableTableModel extends DefaultTableModel {
    public EditableTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}