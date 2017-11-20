


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;

/** 
 * TableDemo is just like SimpleTableDemo, except that it
 * uses a custom TableModel.
 */
public class TableTest extends JPanel {
    private boolean DEBUG = false;

    public TableTest() {
        //super(new GridLayout(0,1));
    	super(new BorderLayout());

        JTable table = new JTable(new MyTableModel());
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        table.setDefaultRenderer(Boolean.class, new ColorRenderer(true));

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        

        //Add the scroll pane to this panel.
        add(scrollPane,BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new GridLayout(1,0));
        
        
        JButton btnNewButton = new JButton("Yes");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MyTableModel model=(MyTableModel) table.getModel();
				model.showNextLevel();
			}
		});
		buttonPanel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("No");
		btnNewButton_1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg1){
				MyTableModel model=(MyTableModel) table.getModel();
				model.showNextLevel();
			}
		});
		
		buttonPanel.add(btnNewButton_1);
		add(buttonPanel,BorderLayout.SOUTH);
		
		
    }

    class MyTableModel extends AbstractTableModel {
    	
    	private LinkedList<String> fixed=null;
    	
    	private LinkedList<Integer> currentLevel;
    	
    	private String[] cellsToActivate;
    	
    	private Iterator<String> arrow;
    	
    	private int rows=13;
    	
    	private int columns=8;
    	
    	public MyTableModel(){
    		super();
    		LinkedList<String> temporaryResults;
 
    		//String initialPath=new String("820");
    		//Node root=new Node();
    		//root.setPath(initialPath);
    			//Node.clearLists();
    			//root.generateLevels(initialPath, numberOfCells-1, Node.Direction.RIGHT, 820);
    			//temporaryResults=Node.getLevelsGenerated();
    			//System.out.println("Initial number of solutions: "+temporaryResults.size());
    			//fixed=Test.convertToGameGrid(temporaryResults);
    			// arrow=fixed.iterator();
    			// currentLevel=new LinkedList<Integer>();
    			//String initialPath=new String("52");
    			//Node root=new Node();
    			//root.setPath(initialPath);
     			//Node.clearLists();
     			//root.generateLevels2(initialPath, numberOfCells-1, Node.Direction.RIGHT, 52);
    			Node.generateLevels(18, Node.Direction.DOWN, 2, 5, 5);
     			temporaryResults=Node.getLevelsGenerated2();
     			System.out.println("Initial number of solutions: "+temporaryResults.size());
     			fixed=temporaryResults;
     			fixed=Node.adaptToGrid(fixed);
     			 arrow=fixed.iterator();
     			 currentLevel=new LinkedList<Integer>();

    	}
        private String[] columnNames = {"First Name",
                                        "Last Name",
                                        "Sport",
                                        "# of Years",
                                        "Vegetarian"};
        private Object[][] data = {
	    {"Kathy", "Smith",
	     "Snowboarding", new Integer(5), new Boolean(false)},
	    {"John", "Doe",
	     "Rowing", new Integer(3), new Boolean(true)},
	    {"Sue", "Black",
	     "Knitting", new Integer(2), new Boolean(false)},
	    {"Jane", "White",
	     "Speed reading", new Integer(20), new Boolean(true)},
	    {"Joe", "Brown",
	     "Pool", new Integer(10), new Boolean(false)}
        };

        public int getColumnCount() {
            return columns;
        }

        public int getRowCount() {
            return rows;
        }

        public String getColumnName(int col) {
            return "";
        }

        public Object getValueAt(int row, int col) {
        	int cell=1+(row*8)+col;
           // return data[row][col];
        	return new Boolean(currentLevel.indexOf(cell)!=-1) ;       }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            return false;
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if (DEBUG) {
                System.out.println("Setting value at " + row + "," + col
                                   + " to " + value
                                   + " (an instance of "
                                   + value.getClass() + ")");
            }

            data[row][col] = value;
            fireTableCellUpdated(row, col);

            if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }
        }
        
        public boolean showNextLevel(){
        	boolean result;
        	currentLevel=new LinkedList<Integer>();
        	if(arrow.hasNext()){
        		result=true;
        	
        		cellsToActivate=arrow.next().split("_");
        		for(int i=0;i<cellsToActivate.length;i++){
        			currentLevel.add(Integer.parseInt(cellsToActivate[i]));
        		}
        		fireTableDataChanged();
        		if (DEBUG) {
                    System.out.println("New value of data:");
                    printDebugData();
                }
        		
        	}else{
        		result=false;
        	}
        	return result;
        }

        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();

            for (int i=0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j=0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI(String arg) {
        //Create and set up the window.
        JFrame frame = new JFrame("TableTest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        TableTest newContentPane = new TableTest();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args[0]);
            }
        });
    }
}
