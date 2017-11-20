import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class Displayer implements TableModelListener{
	
	private JFrame frame;	
	private JTable table;
	private static int numbRows;
	private static int numbCols;
	private static LinkedList<String> mySelection;
	private static Iterator<String> staticIterator;	
	public static String currentPath;
	public static Displayer currentFrame;
	
	
public static void main(String[] args) {
	mySelection =new LinkedList<String>();
	numbRows=Integer.parseInt(args[1]);
	numbCols=Integer.parseInt(args[2]);
		
	FileInputStream fstream=null;
		try {
			//The name of the file (including the .txt extension should be the first argument sent to the main method.
			fstream = new FileInputStream(args[0]);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		try {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	mySelection.add(line);
		    	//System.out.println("Added a level.");
		       }
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("File container contains "+mySelection.size()+" files");
	
		

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Displayer window = new Displayer(numbRows,numbCols);
					window.frame.setVisible(true);		
					Iterator<String> it =mySelection.iterator();
					setStaticIterator(it);
					System.out.println("Number of levels in file: "+mySelection.size());
					window.presentNextOption();
				setCurrentFrame(window);	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		//System.out.println(Launcher.currentFrame);
					//Launcher.currentFrame.presentNextOption();	
		}

public Displayer(int rows,int cols) {
	initialize(rows,cols);
}
private void initialize(int rows, int cols) {
	
	frame = new JFrame();
	frame.setBounds(0, 0, 500, 700);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().setLayout(null);
	table = new JTable(new MyTableModel(rows,cols));
	table.setBounds(50, 10, 300, 300);
	table.getModel().addTableModelListener(this);
	frame.getContentPane().add(table);
	JButton btnNewButton = new JButton("Yes");
	btnNewButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			//Launcher.currentFile.println(currentPath);
			//mySelection.add(currentPath);
			presentNextOption();
		}
	});
	btnNewButton.setBounds(0, 400, 100, 50);
	frame.getContentPane().add(btnNewButton);
	
	JButton btnNewButton_1 = new JButton("No");
	btnNewButton_1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent arg1){
			presentNextOption();
		}
	});
	btnNewButton_1.setBounds(400, 400, 100, 50);
	frame.getContentPane().add(btnNewButton_1);
	
	JButton closebtn = new JButton("Close");
	closebtn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			//Displayer.currentFile.println(currentPath);
			//Launcher.currentFile.close();
			mySelection.add(currentPath);
			//Launcher.currentFile.close();
		
		}
	});
	closebtn.setBounds(200, 400, 100, 50);
	frame.getContentPane().add(closebtn);
}

public void presentNextOption(){
	if(staticIterator.hasNext()){
		Boolean [][] cellsToActivate=new Boolean[numbRows][numbCols];
		String path=staticIterator.next();
		setCurrentPath(path);
		System.out.println("Current path: " +path);
		String [] split1=path.split(":");
		String[] theCells=split1[1].split("_");
		for(int i=0;i<numbRows;i++){
			for(int j=0;j<numbCols;j++){
				cellsToActivate[i][j]=new Boolean(false);
			}
		}
		for(int k=0;k<theCells.length;k++){
			int cell=Integer.parseInt(theCells[k]);
			//int a=(cell-1)/numbRows;
			int a=(int)Math.floor((cell-1)/numbCols);
			int b=(cell-1)%numbCols;
			cellsToActivate[a][b]=new Boolean(true);
			//System.out.println("Activating row " +a+ " and column "+b);
		}
		MyTableModel theModel=(MyTableModel)table.getModel();
		theModel.updateTable(cellsToActivate);
		
	}else{
		//currentFile.close();
	}
}

public static void setCurrentPath(String current){
	currentPath=current;
}
public static void setCurrentFrame(Displayer frame){
	currentFrame=frame;
}

private static void setStaticIterator(Iterator<String> l){
	staticIterator=l;
}

public void tableChanged(TableModelEvent e) {
    table.repaint();        
}

}
