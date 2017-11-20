import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Solver {
	private static HashMap<Integer,String> finalOnes =null;
	private static PrintWriter currentFile;
	private static int toSolve=0;
	private static int solutionsFound=0;
	static boolean over=false;
	static boolean allDone=false;
	
	public static void main(String[] args){
		//First argument
		//File name
		// Sub-folder name
		toSolve=0;
		solutionsFound=0;
		
		File output=new File("C:/Users/Emmanuel/workspace/Levels Generator/New/Hard/"+args[0]+"SOLUTIONS"
				+".txt");
		try {
			currentFile=new PrintWriter(output,"UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finalOnes =new HashMap<Integer,String>();
		FileInputStream fstream=null;
		allDone=false;
		try {
			//The name of the file (including the .txt extension should be the first argument sent to the main method.
			//fstream = new FileInputStream(args[0]);
			fstream = new FileInputStream("C:/Users/Emmanuel/workspace/Levels Generator/New/Hard/"+args[0]);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		LinkedList<String> input=new LinkedList<String>();
		try {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	if(!line.equals("")){
		    		input.add(line);
			    	toSolve++;
		    	}
		    }
		    
		    LinkedList<SolverAgent> agents=new LinkedList<>();
		    for(int i=0;i<10;i++){
		    	agents.add(new SolverAgent(i+1));
		    }
		    for (SolverAgent a:agents){
		    	a.start();
		    }
		    over=false;
		    StatusIndicator myStatus=new StatusIndicator();
		    myStatus.start();
		    for(String s :input){
		    	boolean launched=false;
		    	while(!launched){
		    		for(SolverAgent a:agents){
		    			if(a.isAvailable()){
		    				if((input.indexOf(s)+1)<toSolve){
		    					//System.out.println("Requested processing of "+((input.indexOf(s))+1));
		    					a.processLevel(s,input.indexOf(s)+1,false);
		    				}else{
		    				//	System.out.println("Requested processing of the last level.");
		    					a.processLevel(s,input.indexOf(s)+1,true);
		    				}
		    				launched=true;
		    				break;
		    			}
		    		}
		    		if(!launched){
		    			Thread.sleep(1000);
		    		}
		    	}
		    }
		    over=true;
		  //  while(!allDone){
		    while(solutionsFound<toSolve){
		    	Thread.sleep(2000);
		    }
		    MapUtil.sortByValue(finalOnes);
		    System.out.println("All the solutions were found: "+solutionsFound+"/"+toSolve);
		   // System.out.println("The list:"+finalOnes.toString());
		    for(int i=1;i<=toSolve;i++){
		    	currentFile.println(finalOnes.remove(i));
		    }
		}catch(Exception e){
			System.out.println("Problem in Solver main.");
		}
		currentFile.close();
		System.out.println("Just closed the file.");
	}
	
	
	static class SolverAgent extends Thread{
		
		String levelToProcess;
		boolean available=true;
		boolean active=true;
		int myNumber=0;
		int levelNumber;
		boolean last=false;
		SolverAgent(int number){
			last=false;
			myNumber=number;
			levelNumber=0;
		}
		
		public void run(){
			/*while(active && !allDone){*/
			while(active){
				if(!available){
					String [] stage1=levelToProcess.split(":");
				       String [] stage2=stage1[1].split("_");
				       String [] stage3=stage1[0].split("_");
				       LinkedList<Integer> level= new LinkedList<Integer>();
				       for(int i=0;i<stage2.length;i++){
				    	   level.add(Integer.parseInt(stage2[i]));
				       }
				       int j=0;
				       String solution=Checker.getSolution(level, Integer.parseInt(stage3[0]), Integer.parseInt(stage3[1]));
				       if(solution==null ){
				    	   System.out.println("We have a problem.");
				       }else{
				    	   if(solution.equals("Void")){
				    		  // System.out.println("Found a void");
				    		   saveGoodOne(solution,levelNumber,last);
				    	   }else{
				    		   solution=solution.substring(1, solution.length()-1);
						       saveGoodOne(solution,levelNumber,last);
				    	   }
				       }
				   //    System.out.println("Thread "+myNumber+": "+levelNumber+" processed.");
				       available=true;
				}else{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		public void processLevel(String toProcess,int levelNumbr,boolean isLastOne){
			levelNumber=levelNumbr;
			levelToProcess=toProcess;
			available=false;
			last=isLastOne;
		}
		
		public boolean isAvailable(){
			return available;
		}
	}
	
	static synchronized void saveGoodOne(String toSave, int levelNumber,boolean lasst){
		solutionsFound++;
		finalOnes.put(new Integer(levelNumber),toSave);
		if(lasst){
			allDone=true;
		}
		//TODO: make sure to add a final method that will process the HashMap and Write the solutions to 
		//the currentFile 
		//.txt 
		//currentFile.println(toSave);
	}
	
	static class StatusIndicator extends Thread{
		
		StatusIndicator(){
			
		}
		
		public void run(){
			while(solutionsFound<toSolve){
				SimpleDateFormat sdf= new SimpleDateFormat(" HH:mm:ss");
				Date now=new Date();
				String strDate=sdf.format(now);
				System.out.println("Found: "+solutionsFound+"/"+toSolve+strDate);
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
	 static class MapUtil
	{
	    public static <K, V extends Comparable<? super V>> Map<K, V> 
	        sortByValue( Map<K, V> map )
	    {
	        List<Map.Entry<K, V>> list =
	            new LinkedList<Map.Entry<K, V>>( map.entrySet() );
	        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
	        {
	            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
	            {
	                return (o1.getValue()).compareTo( o2.getValue() );
	            }
	        } );

	        Map<K, V> result = new LinkedHashMap<K, V>();
	        for (Map.Entry<K, V> entry : list)
	        {
	            result.put( entry.getKey(), entry.getValue() );
	        }
	        return result;
	    }
	}


}
