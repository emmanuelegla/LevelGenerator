import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

public class Remover {

	
	private static LinkedList<LinkedList<Integer>> selectedOnes =null;
	private static LinkedList<LinkedList<Integer>> finalOnes =null;
	private static PrintWriter currentFile;
	private static int rows=0;
	private static int columns=0;
	private static int processedSoFar=0;
	private static int levelsKept=0;
	static boolean over=false;
	
	public static void main(String[] args){
		//First argument
		//File name
		// Sub-folder name
		processedSoFar=0;
		levelsKept=0;
		
		File output=new File("C:/Users/Emmanuel/workspace/Levels Generator/New/"+args[1]+"/"+args[0]+"TOUGH"
				+".txt");
		try {
			currentFile=new PrintWriter(output,"UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		selectedOnes =new LinkedList<LinkedList<Integer>>();
		finalOnes =new LinkedList<LinkedList<Integer>>();
		//rows=Integer.parseInt(args[1]);
		//columns=Integer.parseInt(args[2]);
		//System.out.println("Rows= "+rows+ " Columns= "+columns);
		//System.out.println(columns/2);
		FileInputStream fstream=null;
		try {
			//The name of the file (including the .txt extension should be the first argument sent to the main method.
			fstream = new FileInputStream(args[0]);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		LinkedList<String> input=new LinkedList<String>();
		try {
		    String line;
		    int processed=0;
		    while ((line = br.readLine()) != null) {
		    	input.add(line);
		    }
		    
		    LinkedList<RemoveAgent> agents=new LinkedList<>();
		    for(int i=0;i<20;i++){
		    	agents.add(new RemoveAgent(i+1));
		    }
		    
		    for (RemoveAgent a:agents){
		    	a.start();
		    }
		    
		    over=false;
		    for(String s :input){
		    	boolean launched=false;
		    	while(!launched){
		    		for(RemoveAgent a:agents){
		    			if(a.isAvailable()){
		    			//	System.out.println("Successfully started a thread.");
		    				a.processLevel(s);
		    				launched=true;
		    				break;
		    			}
		    		}
		    		if(!launched){
		    			Thread.sleep(5000);
		    		}
		    	}
		    }
		    over=true;
		    System.out.println("Went from "+processedSoFar+" levels to "+levelsKept+" levels.");
		}catch(Exception e){
			
		}
		currentFile.close();
	}
	
	static class RemoveAgent extends Thread{
		
		String levelToProcess;
		boolean available=true;
		boolean active=true;
		int myNumber=0;
		RemoveAgent(int number){
			myNumber=number;
		}
		
		
		
		public void run(){
			while(active && !over){
				if(!available){
				//	processedSoFar++;
					String [] stage1=levelToProcess.split(":");
				       String [] stage2=stage1[1].split("_");
				       String [] stage3=stage1[0].split("_");
				       LinkedList<Integer> level= new LinkedList<Integer>();
				       for(int i=0;i<stage2.length;i++){
				    	   level.add(Integer.parseInt(stage2[i]));
				       }
				       boolean isSimple=false;
				       
				       
				       
				       int starter=Approver.getStartingCell(level, Integer.parseInt(stage3[0]), Integer.parseInt(stage3[1]));
				       isSimple=Checker.isSimpleFrom3(starter, level, Integer.parseInt(stage3[0]), Integer.parseInt(stage3[1]));
				       
				       /* int j=0;
				        while(!isSimple &&j<level.size() ){
				    	   isSimple=Checker.isSimpleFrom3(level.get(j), level, Integer.parseInt(stage3[0]), Integer.parseInt(stage3[1]));
				    	   //Use line below instead to remove the levels that can be solved with no crossings at all.
				    	  // isSimple=Checker.isSimpleFrom(level.get(j), level, Integer.parseInt(stage3[0]), Integer.parseInt(stage3[1]));
				    	   j++;
				       }*/
				       if(!isSimple){
				    	 //  selectedOnes.add(level);
				    	   saveGoodOne(levelToProcess);
				       }
				       System.out.println("Thread "+myNumber+": #"+getLevelNumber()+" processed.");
				       
				       available=true;
				}else{
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		public void processLevel(String toProcess){
			levelToProcess=toProcess;
			available=false;
		}
		
		public boolean isAvailable(){
			return available;
		}
	}
	
	static synchronized void saveGoodOne(String toSave){
		levelsKept++;
		currentFile.println(toSave);
	}
	
	static synchronized int getLevelNumber(){
		processedSoFar++;
		return processedSoFar;
	}

}
