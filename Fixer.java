import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
/*Removes levels identical by symmetry to only keep one.
 * Checks both vertical and horizontal symmetry. */
/*Launch sample: 22_cells on 10_3.txt 10 3 */

public class Fixer {
	
	private static LinkedList<LinkedList<Integer>> selectedOnes =null;
	private static LinkedList<LinkedList<LinkedList<Integer>>> gridSets =new LinkedList<LinkedList<LinkedList<Integer>>>();
	private static LinkedList<LinkedList<Integer>> finalOnes =null;
	private static LinkedList<Integer> usedRows;
	private static LinkedList<Integer> usedColumns;
	private static PrintWriter currentFile;
	private static int rows=0;
	private static int columns=0;
	
	public static void main(String[] args){
		try {
			String [] parts =args[0].split(".txt");
			currentFile=new PrintWriter(parts[0]+"F_"+args[1]
			+".txt","UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int finalNumber=0;
		int processed =0;
		//Send "V" to remove vertical symmetrical and "H" to remove horizontal symmetrical.
		boolean checkingVertical=args[1].equals("V");
		selectedOnes =null;
		finalOnes =new LinkedList<LinkedList<Integer>>();
		usedRows=new LinkedList<Integer>();
		usedColumns=new LinkedList<Integer>();
		rows=0;
		columns=0;
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
		try {
		    String line;
		    int counter=0;
		    while ((line = br.readLine()) != null) {
		       String [] stage1=line.split(":");
		       //Getting the cells to activate for the current level. 
		       String [] stage2=stage1[1].split("_");
		       //Getting the number of rows and of columns for the current level.
		       String [] stage3=stage1[0].split("_");
		       if(Integer.parseInt(stage3[0])!=rows || Integer.parseInt(stage3[1])!=columns ){
		    	   
		    	  //A new levels container needs to be created for the levels on this grid. 
		    	   rows=Integer.parseInt(stage3[0]);
			       columns=Integer.parseInt(stage3[1]);
			       System.out.println("Rows= "+rows+ " Columns= "+columns);
		    	   usedRows.add(rows);
		    	   usedColumns.add(columns);
			       if(selectedOnes!=null){
			    	   //Saving the levels processed previously. 
			    	   gridSets.add(selectedOnes);
			       }
			       selectedOnes=new LinkedList<LinkedList<Integer>>();
		       }
		       counter++;
		       LinkedList<Integer> level= new LinkedList<Integer>();
		       for(int i=0;i<stage2.length;i++){
		    	   level.add(Integer.parseInt(stage2[i]));
		       }
		       processed++;
		       boolean originalAlreadyPresent=false;
		       for(int j=0;j<selectedOnes.size() && !originalAlreadyPresent;j++){
		    	   /*This loop makes sure that truly identical levels don't get processed twice.   */
		    	   LinkedList<Integer> current=selectedOnes.get(j);
		    	   boolean allCellsPresent=true;
		    	   for(int l=0;l<level.size() && allCellsPresent;l++){
		    		   if(!current.contains(level.get(l))){
		    			   allCellsPresent=false;
		    		   }
		    	   }
		    	   if(!allCellsPresent){
		    		   
		    	   }else{
		    		   originalAlreadyPresent=true;
		    	   }
		       }
		       LinkedList<Integer> symetric=null;
		       if(checkingVertical){
		    	    symetric=getVerticalSymetric(level);
		       }else{
		    	    symetric=getHorizontalSymetric(level);
		       }
		       
		       boolean symetricAlreadyPresent=false;
		       for(int j=0;j<selectedOnes.size() && !symetricAlreadyPresent;j++){
		    	   
		    	   /*This loop makes sure that the horizontal symmetric of the current level is not already present in the final set.  */
		    	   LinkedList<Integer> current=selectedOnes.get(j);
		    	   boolean allCellsPresent=true;
		    	   for(int l=0;l<symetric.size() && allCellsPresent;l++){
		    		   if(!current.contains(symetric.get(l))){
		    			   allCellsPresent=false;
		    		   }
		    	   }
		    	   if(!allCellsPresent){
		    		   
		    	   }else{
		    		   symetricAlreadyPresent=true;
		    	   }
		       }
		       if(!(originalAlreadyPresent || symetricAlreadyPresent)){
		    	   selectedOnes.add(level);
		       }
 
		    }
		   // System.out.println("Went from "+counter+" levels to "+selectedOnes.size()+" levels.");
		}catch(Exception e){
			
		}
		gridSets.add(selectedOnes);
		for (int s=0;s<gridSets.size();s++){
			LinkedList<LinkedList<Integer>> ab=gridSets.get(s);
			for(int t=0;t<ab.size();t++){
				LinkedList<Integer> level= ab.get(t);
				String forFile=usedRows.get(s)+"_"+usedColumns.get(s)+":";
				for(int i=0;i<level.size();i++){
		    		   if(i!=level.size()-1){
		    			   forFile=forFile+level.get(i)+"_";
		    		   }else{
		    			   forFile=forFile+level.get(i);
		    		   }
		    	   }
				finalNumber++;
		    	currentFile.println(forFile);
			}
		}
		currentFile.close();
		System.out.println("Went from "+processed+" to "+finalNumber);
		
		/*	for(int z=0;z<selectedOnes.size();z++){
		LinkedList<Integer> level= selectedOnes.get(z);
		 boolean originalAlreadyPresent=false;
	       for(int j=0;j<finalOnes.size() && !originalAlreadyPresent;j++){
	    	   LinkedList<Integer> current=finalOnes.get(j);
	    	   boolean allCellsPresent=true;
	    	   for(int l=0;l<level.size() && allCellsPresent;l++){
	    		   if(!current.contains(level.get(l))){
	    			   allCellsPresent=false;
	    		   }
	    	   }
	    	   if(!allCellsPresent){
	    	   }else{
	    		   originalAlreadyPresent=true;
	    	   }
	       }
	       
	       
	       
	       LinkedList<Integer> vSymetric=getVerticalSymetric(level);
	       boolean symetricAlreadyPresent=false;
	       for(int j=0;j<finalOnes.size() && !symetricAlreadyPresent;j++){
	    	   LinkedList<Integer> current=finalOnes.get(j);
	    	   boolean allCellsPresent=true;
	    	   for(int l=0;l<vSymetric.size() && allCellsPresent;l++){
	    		   if(!current.contains(vSymetric.get(l))){
	    			   allCellsPresent=false;
	    		   }
	    	   }
	    	   if(!allCellsPresent){		    		   
	    	   }else{
	    		   symetricAlreadyPresent=true;
	    	   }
	       }
	       if(!(originalAlreadyPresent || symetricAlreadyPresent)){
	    	   finalOnes.add(level);
	    	   String forFile=rows+"_"+columns+":";
	    	   for(int i=0;i<level.size();i++){
	    		   if(i!=level.size()-1){
	    			   forFile=forFile+level.get(i)+"_";
	    		   }else{
	    			   forFile=forFile+level.get(i);
	    		   }
	    	   }
	    	   currentFile.println(forFile);
	    	 //  System.out.println("Added level "+forFile);
	       }
	} */
	}
	
	public static LinkedList<Integer> getHorizontalSymetric(LinkedList<Integer> of){
		
		String original ="";
		String symetric ="";
		for(int m=0;m<of.size();m++){
			if(m!=of.size()-1){
				original=original+of.get(m)+"_";
			}else{
				original=original+of.get(m);
			}
		}
		LinkedList<Integer> result= new LinkedList<Integer>();
		if(columns%2==0){
			//Even number of columns.
			int half=columns/2;			
			for(int i=0;i<of.size();i++){
				if(of.get(i)%columns<=half && of.get(i)%columns!=0){
					result.add((int)(of.get(i)+1+2*(half-of.get(i)%columns)));
				//	System.out.println(of.get(i)+" A becomes "+result.getLast());
				}else if(of.get(i)%columns>half ){
					result.add((int)(of.get(i) -1- 2*(of.get(i)%columns -half-1) ));
				//	System.out.println(of.get(i)+" B becomes "+result.getLast());
				}else{
					result.add((int)(of.get(i) -columns+1));
				//	System.out.println(of.get(i)+" F becomes "+result.getLast());
				}
			}
			
		}else{
			//Odd number of columns.
			int middleModulo=((columns+1)/2);
			for(int i=0;i<of.size();i++){
				if(of.get(i)%columns <middleModulo && of.get(i)%columns!=0){
					result.add(of.get(i)+ 2*(middleModulo-(of.get(i)%columns)));
				//	System.out.println(of.get(i)+" C becomes "+result.getLast());
				}else if(of.get(i)%columns == middleModulo){
					result.add(of.get(i));
				//	System.out.println(of.get(i)+" D becomes "+result.getLast());
				}else{
					result.add(of.get(i)- 2*(of.get(i)%columns)-middleModulo);
				//	System.out.println(of.get(i)+" E becomes "+result.getLast());
				}
			}
		}
		
		for(int n=0;n<result.size();n++){
			if(n!=result.size()-1){
				symetric=symetric+result.get(n)+"_";
			}else{
				symetric=symetric+result.get(n);
			}
		}
	//	System.out.println("Went from "+original);
	//	System.out.println("To "+symetric);
		return result;
	}
	
	public static LinkedList<Integer> getVerticalSymetric(LinkedList<Integer> of){
		LinkedList<Integer> result= new LinkedList<Integer>();
		if(rows%2==0){
			int half=rows/2;
			//Even number of rows.
			for(int i=0;i<of.size();i++){
				if(Math.ceil((of.get(i)+columns-1)/columns) <=half){
					result.add((int)(of.get(i)+(2*(half-Math.ceil((of.get(i)+columns-1)/columns)) +1)*columns));
				//	System.out.println(of.get(i)+" V becomes "+result.getLast());
				}else{
					result.add((int)(of.get(i) - (2*(Math.ceil((of.get(i)+columns-1)/columns)-half-1) +1)*columns));
				//	System.out.println(of.get(i)+" W becomes "+result.getLast());
				}
			}
		}else{
			int middleModulo=(rows+1)/2;
			for(int i=0;i<of.size();i++){
				if(Math.ceil((of.get(i)+columns-1)/columns) <middleModulo){
					result.add((int)(of.get(i)+ (2*(middleModulo-Math.ceil((of.get(i)+columns-1)/columns)))*columns));
				//	System.out.println(of.get(i)+" X becomes "+result.getLast()+" and middle modulo is "+middleModulo+" with ceil "+(int)Math.ceil((of.get(i)+columns-1)/columns) );
				}else if(Math.ceil((of.get(i)+columns-1)/columns) == middleModulo){
					result.add(of.get(i));
				//	System.out.println(of.get(i)+" Y becomes "+result.getLast());
				}else{
					result.add((int)(of.get(i)- (2*(Math.ceil((of.get(i)+columns-1)/columns)-middleModulo))*columns));
				//	System.out.println(of.get(i)+" Z becomes "+result.getLast());
				}
			}
			
		}
		return result;
	}
	
	/**Saves the unique levels to a file. */
	private static void writeToFile(){
		
	}

}
