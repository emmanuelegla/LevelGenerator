import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;
import java.awt.event.ActionEvent;

public class FmyFrame implements TableModelListener{

	private JFrame frame;	
	private JTable table;
	private static LinkedList<String> mySelection;	
	public static int numberOfOptions;	
	public static int currentOption;	
	public static int initialCell;
	public static int totalNumbOfCells;
	public static int numbRows;
	public static int numbCols;
	public static Node.Direction dir;
	private static Iterator<String> staticIterator;	
	public static FmyFrame currentFrame;	
	public static String currentPath;
	private static LinkedList<String> remaining;
	
	private static PrintWriter currentFile;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		
		
		if(args.length!=0){
			/* args[0]= total number of cells 
			 * args[1]= direction
			 * args[2]= initial cell
			 * args[3]= number of rows
			 * args[4]= number of columns  */
			switch (args[1]) {
			case "1" :
				dir=Node.Direction.UP;
				break;
			case "2" : 
				dir=Node.Direction.RIGHT;
				break;		
			case "3" : 
				dir=Node.Direction.DOWN;
				break;			
			case "4" :
				dir=Node.Direction.LEFT;
			break;
			}
			
			// IT HAPPENS HERE !   !  !  !  !
			initialCell= Integer.parseInt(args[2]);
			totalNumbOfCells=Integer.parseInt(args[0]);
					numbRows=Integer.parseInt(args[3]);
					numbCols=Integer.parseInt(args[4])   ;
					// IT HAPPENS HERE !   !  !  !  !
					Checker.setNumberOfCols(numbCols);
					Checker.setNumberOfRows(numbRows);
			try {
				currentFile=new PrintWriter(Integer.toString(totalNumbOfCells)+"_cells on "+Integer.toString(numbRows)+"_"+Integer.toString(numbCols)
				+"_initial_"+Integer.toString(initialCell)+".txt","UTF-8");
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FmyFrame window = new FmyFrame(numbRows,numbCols);
					window.frame.setVisible(true);				
					LinkedList<String> temporaryResults;
					
					mySelection=new LinkedList<String>();
			//	String initialPath=new String("20");
			//	Node root=new Node();
				//root.setPath(initialPath);
				if(args.length!=0){
					System.out.println("Using the arguments provided in the run configuration menu.");
					Node.generateLevels(totalNumbOfCells, dir, initialCell,  numbRows, numbCols);
					//temporaryResults=Node.getLevelsGenerated2();
					temporaryResults=Node.getLevelsGenerated3();
					LinkedList<String> s=Checker.removeSimpleOriginal(temporaryResults);
					/*ALGORITHM 1:
					 * - Take all the levels generated.
					 * - Sort them according to the number of cells that are crossing and place in different lists.
					 * - Removed identical levels in each list/ in list with no crossing levels.
					 * - Process the list containing the levels with crossings.
					 * - If a level in that list matches a level with no crossings, do not add to final list.*/
					
					//	choose(temporaryResults);
					//remaining=new LinkedList<String>();
					//selectComplex(temporaryResults);
					Iterator<String> it =s.iterator();
					//Iterator<String> it=temporaryResults.iterator();
					//Iterator<String> it=mySelection.iterator();
					//Iterator<String> it=remaining.iterator();
					setStaticIterator(it);
					System.out.println("Number of solutions: "+s.size());
					window.presentNextOption();
				}else{
				//	Node.clearList();
					//root.generateLevels(initialPath, Integer.parseInt(args[0])-1, Node.Direction.RIGHT, 20);
					System.out.println("Using the in-code settings to generate the levels, no args provided.");
					Node.generateLevels(10, Node.Direction.DOWN, 6, 3, 4);	
					temporaryResults=Node.getLevelsGenerated2();
					Iterator<String> it=temporaryResults.iterator();
					setStaticIterator(it);
					System.out.println("Number of solutions: "+temporaryResults.size());
					window.presentNextOption();
				}
				setCurrentFrame(window);	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		//System.out.println(FmyFrame.currentFrame);
					//FmyFrame.currentFrame.presentNextOption();	
		}
	
	private static void selectComplex(LinkedList<String> input){
		long initial=System.currentTimeMillis();
		for(int i=0;i<input.size();i++){
			int highestC=0;
			int numbOfDoubles=0;
			String curr=input.get(i);
			String[] a=curr.split(":");
			String [] b=a[1].split("_");
			//for(int k=0;k<b.length&&highestC<2;k++){
			for(int k=0;k<b.length;k++){
				String now=b[k];
				//Pattern p = Pattern.compile(b[k]+"_");
				//Matcher m = p.matcher(a[1]);
				int count = 0;
				for (int s=0;s<b.length;s++){
					if(b[s].equals(now)){
						//System.out.println(b[s]+" equals "+now);
						 count +=1;
					}
				}
				if(count==2){
					numbOfDoubles +=1;
					highestC=count;
				}
			}
			if(numbOfDoubles>4){
				remaining.add(curr);
			}
			
		}
		long b=System.currentTimeMillis();
		System.out.println("Selection of 2+ crossing levels took "+(b-initial)+" milliseconds.");
		System.out.println("Went from "+input.size()+" to "+remaining.size()+" levels.");
		
	}
	

	/**
	 * Create the application.
	 */
	
	private static void choose(LinkedList<String> initial){
		if(initial.size()<=10){
			FmyFrame.currentFile.println(initial.getFirst());
			FmyFrame.currentFile.println(initial.getLast());
			FmyFrame.currentFile.close();
		}else if(initial.size()>10 &&initial.size()<=100){
			
			int max =initial.size();
			int numberChosen= (int) ( max*0.1);
			int interval=(int)Math.floor(initial.size()/numberChosen);
			System.out.println("Picking randomly "+numberChosen+" levels.");
			for (int i=0;i<numberChosen;i++){
				mySelection.add(initial.get(i*interval));
				FmyFrame.currentFile.println(initial.get(i*interval));
			}
		}else if(initial.size()>100 &&initial.size()<=1000){
			int max =initial.size();
			int numberChosen= (int) ( max*0.1);
			int interval=(int)Math.floor(initial.size()/numberChosen);
			System.out.println("Picking randomly "+numberChosen+" levels.");
			for (int i=0;i<numberChosen;i++){
				mySelection.add(initial.get(i*interval));
				FmyFrame.currentFile.println(initial.get(i*interval));
			}
		}else if(initial.size()>1000 &&initial.size()<=10000){
			int max =initial.size();
			int numberChosen= (int) ( max*0.1);
			int interval=(int)Math.floor(initial.size()/numberChosen);
			System.out.println("Picking randomly "+numberChosen+" levels.");
			for (int i=0;i<numberChosen;i++){
				mySelection.add(initial.get(i*interval));
				FmyFrame.currentFile.println(initial.get(i*interval));
			}
		}else{
			System.out.println("Too many levels to process - More than 10000.");
		}
		FmyFrame.currentFile.close();
	}
	
	
	public FmyFrame(int rows,int cols) {
		mySelection=new LinkedList<String>();
		initialize(rows,cols);
		
	}
	
	private static void setStaticIterator(Iterator<String> l){
		staticIterator=l;
	}
	
	public static void setNumberOfOptions(int a){
		numberOfOptions=a;
	}
	
	public static void setCurrentOption(int b){
		currentOption=b;
	}
	
	public static void setCurrentFrame(FmyFrame frame){
		currentFrame=frame;
	}
	
	public static void setCurrentPath(String current){
		currentPath=current;
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
			currentFile.close();
		}
	}
	
	public void showSelectedRandomly(){
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
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
				FmyFrame.currentFile.println(currentPath);
				//FmyFrame.currentFile.close();
				mySelection.add(currentPath);
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
				FmyFrame.currentFile.println(currentPath);
				//FmyFrame.currentFile.close();
				mySelection.add(currentPath);
				FmyFrame.currentFile.close();
				//presentNextOption();
			}
		});
		closebtn.setBounds(200, 400, 100, 50);
		frame.getContentPane().add(closebtn);
	}
	
	public void tableChanged(TableModelEvent e) {
        table.repaint();        
    }
}
