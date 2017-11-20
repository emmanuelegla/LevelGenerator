import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] columnNames =new String[40];
    private boolean[][] data = new boolean[40][40];

    public int getColumnCount() {
        return data[0].length;
    }
    
    public MyTableModel(int rows, int cols){
    	data=new boolean[rows][cols];
    }
    
    public MyTableModel(){
    	data=new boolean[40][40];
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public Class getColumnClass(int c) {
       return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
            return true;
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(boolean value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
    
    public void updateTable(Boolean [][] newValues){
    	for(int i=0;i<newValues.length;i++){
    		for(int j=0;j<newValues[0].length;j++){
    			data[i][j]=newValues[i][j];
    		}
    	}
    	fireTableDataChanged();
    }
   
}


