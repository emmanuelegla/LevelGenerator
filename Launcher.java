
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

/*Generates unique levels.
 * Sample launch config: 42 10 5 * */
public class Launcher implements TableModelListener{
	
	private JFrame frame;	
	private JTable table;
	private static LinkedList<String> mySelection;	
	public static int nCellsToRemove;
	public static int numbRows;
	public static int numbCols;
	public static Node.Direction dir;
	private static Iterator<String> staticIterator;	
	public static Launcher currentFrame;	
	public static String currentPath;
	private static LinkedList<String> remaining;
	public static String fileName="";
	
	private static PrintWriter currentFile;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		
		
		if(args.length!=0){
			/* args[0]= number of cells that the levels must have. 
			 * args[1]= number of rows.
			 * args[2]= number of columns.
			 * args[3]= Difficulty. One of E,M and H. 
			 */
			nCellsToRemove=Integer.parseInt(args[0]);
			numbRows=Integer.parseInt(args[1]);
			numbCols=Integer.parseInt(args[2])   ;
					// IT HAPPENS HERE !   !  !  !  !
					Checker.setNumberOfCols(numbCols);
					Checker.setNumberOfRows(numbRows);
			try {
				fileName=Integer.toString((nCellsToRemove))+"_on_"+Integer.toString(numbRows)+"_"+Integer.toString(numbCols)
				+"_V5"+args[3]+".txt";
				currentFile=new PrintWriter(fileName,"UTF-8");
			//	currentFile=new PrintWriter(Integer.toString((nCellsToRemove))+"_on_"+Integer.toString(numbRows)+"_"+Integer.toString(numbCols)
			//	+"_SYM"+".txt","UTF-8");
			//	currentFile=new PrintWriter(Integer.toString((nCellsToRemove))+"_on_"+Integer.toString(numbRows)+"_"+Integer.toString(numbCols)
			//	+"_RANDOM"+".txt","UTF-8");
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Launcher window = new Launcher(numbRows,numbCols);
					window.frame.setVisible(true);				
					LinkedList<String> temporaryResults;
					
					mySelection=new LinkedList<String>();
				if(args.length!=0){
					System.out.println("Using the arguments provided in the run configuration menu.");
					Alpha.generateConfigs(numbCols,numbRows,nCellsToRemove,args[3]);
				//	Alpha.generateSymetricConfigs(numbCols,numbRows,nCellsToRemove);
				//	Alpha.generateRandomConfigs(numbCols,numbRows,nCellsToRemove);
					while(!Alpha.getCompleted()){
						try {
						//	System.out.println(" Processing not completed.");
							Thread.sleep(3000);
							Approver.printStatus();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					temporaryResults=Alpha.getProduct();
					Iterator<String> it =temporaryResults.iterator();
					setStaticIterator(it);
					System.out.println("Number of solutions: "+temporaryResults.size());
					saveConfigs(temporaryResults);
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
		//System.out.println(Launcher.currentFrame);
					//Launcher.currentFrame.presentNextOption();	
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
			Launcher.currentFile.println(initial.getFirst());
			Launcher.currentFile.println(initial.getLast());
			Launcher.currentFile.close();
		}else if(initial.size()>10 &&initial.size()<=100){
			
			int max =initial.size();
			int numberChosen= (int) ( max*0.1);
			int interval=(int)Math.floor(initial.size()/numberChosen);
			System.out.println("Picking randomly "+numberChosen+" levels.");
			for (int i=0;i<numberChosen;i++){
				mySelection.add(initial.get(i*interval));
				Launcher.currentFile.println(initial.get(i*interval));
			}
		}else if(initial.size()>100 &&initial.size()<=1000){
			int max =initial.size();
			int numberChosen= (int) ( max*0.1);
			int interval=(int)Math.floor(initial.size()/numberChosen);
			System.out.println("Picking randomly "+numberChosen+" levels.");
			for (int i=0;i<numberChosen;i++){
				mySelection.add(initial.get(i*interval));
				Launcher.currentFile.println(initial.get(i*interval));
			}
		}else if(initial.size()>1000 &&initial.size()<=10000){
			int max =initial.size();
			int numberChosen= (int) ( max*0.1);
			int interval=(int)Math.floor(initial.size()/numberChosen);
			System.out.println("Picking randomly "+numberChosen+" levels.");
			for (int i=0;i<numberChosen;i++){
				mySelection.add(initial.get(i*interval));
				Launcher.currentFile.println(initial.get(i*interval));
			}
		}else{
			System.out.println("Too many levels to process - More than 10000.");
		}
		Launcher.currentFile.close();
	}
	
	
	public Launcher(int rows,int cols) {
		mySelection=new LinkedList<String>();
		initialize(rows,cols);
		
	}
	
	private static void setStaticIterator(Iterator<String> l){
		staticIterator=l;
	}
	

	
	public static void setCurrentFrame(Launcher frame){
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
				Launcher.currentFile.println(currentPath);
				//Launcher.currentFile.close();
				mySelection.add(currentPath);
				//Launcher.currentFile.close();
			
			}
		});
		closebtn.setBounds(200, 400, 100, 50);
		frame.getContentPane().add(closebtn);
	}
	
	public void tableChanged(TableModelEvent e) {
        table.repaint();        
    }
	
	/**Saves to a file created the generated levels. */
	private static void saveConfigs(LinkedList<String> generatedConfigs){
		for(int i=0;i<generatedConfigs.size();i++){
			Launcher.currentFile.println(generatedConfigs.get(i));
		}
		Launcher.currentFile.close();
		System.out.println("Saved "+generatedConfigs.size()+" levels to file "+fileName);
		
	}
	


}
