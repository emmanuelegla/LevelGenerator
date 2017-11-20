import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class Checker {
	
	private static int numbCols=1;
	private static int numbRows=1;
	//private static LinkedList<Integer> currentCells;
	private LinkedList<Integer> cells;
	private enum Decision {SolvedSimple,SolvedComplex,NotSolved};
	
	public static LinkedList<String> removeSimple(LinkedList<String> input,int rangeStart,int rangeEnd,long beginning,int index){
	//	System.out.println("Start of super-check.");
		//long start=System.currentTimeMillis();
		LinkedList<String> output =new LinkedList<String>();
		int count=0;
		for(int i=0;i<input.size();i++){
			
			String[] temp1=input.get(i).split(":");
			String [] temp2 =temp1[1].split("_");
			LinkedList<Integer> listForCallingThread=new LinkedList<Integer>();
			for(int j=0;j<temp2.length;j++){
				listForCallingThread.add(Integer.parseInt(temp2[j]));
			}
			boolean simple=false;
			for(int l=0;l<listForCallingThread.size()&&!simple;l++){
			//	System.out.println("Checking from position "+l+" and size of container is "+currentCells.size());
				simple=checkFrom(listForCallingThread.get(l),listForCallingThread);
			}
			if(!simple){
				count++;
				output.add(input.get(i));
			}
		}
		//long finish=System.currentTimeMillis();
	//	if(index%100==0 || input.size()>0){
			System.out.println("Thread "+index+": "+count+" complex out of "+input.size()+" in input.");
	//	}
		return output;
	}
	
	public static LinkedList<String> keepSimpleSymetric(LinkedList<String> input,int rangeStart,int rangeEnd,long beginning){
			LinkedList<String> output =new LinkedList<String>();
			for(int i=0;i<input.size();i++){
				String[] temp1=input.get(i).split(":");
				String [] temp2 =temp1[1].split("_");
				String [] temp3 =temp1[0].split("_");
				LinkedList<Integer> listForCallingThread=new LinkedList<Integer>();
				for(int j=0;j<temp2.length;j++){
					listForCallingThread.add(Integer.parseInt(temp2[j]));
				}
				boolean simple=false;
				for(int l=0;l<listForCallingThread.size()&&!simple;l++){
					simple=checkFrom(listForCallingThread.get(l),listForCallingThread);
				}
				if(simple&&(isVerticalSymetric(listForCallingThread,Integer.parseInt(temp3[0]),Integer.parseInt(temp3[1])))|| isHorizontalSymetric(listForCallingThread,Integer.parseInt(temp3[0]),Integer.parseInt(temp3[1]))){
					output.add(input.get(i));
				}
			}
			//long finish=System.currentTimeMillis();
			return output;
		}
	
	public static LinkedList<String> keepRandomSimple(LinkedList<String> input,int rangeStart,int rangeEnd,long beginning,int index){
			LinkedList<String> output =new LinkedList<String>();
			int count=0;
			int simpleCount=0;
			for(int i=0;i<input.size();i++){
				String[] temp1=input.get(i).split(":");
				String [] temp2 =temp1[1].split("_");
				//String [] temp3 =temp1[0].split("_");
				LinkedList<Integer> listForCallingThread=new LinkedList<Integer>();
				for(int j=0;j<temp2.length;j++){
					listForCallingThread.add(Integer.parseInt(temp2[j]));
				}
				boolean simple=false;
				for(int l=0;l<listForCallingThread.size()&&!simple;l++){
					simple=checkFrom(listForCallingThread.get(l),listForCallingThread);
				}
				
				if(simple){
					simpleCount++;
					int random=ThreadLocalRandom.current().nextInt(1, 1001);
					//System.out.println("Random number is "+random);
					if(random<100){
					//	System.out.println("Level was simple and added.");
						output.add(input.get(i));
						count++;
					}else{
					//	System.out.println("Level was simple but not added because random is too high.");
					}
				}
			}
			long finish=System.currentTimeMillis();
			if(index%100==0 || input.size()>0){
				System.out.println("Thread "+index+": "+count+" random out of "+simpleCount+" simple levels in "+(finish-beginning)+" ms. Input: "+input.size());
			}
			//System.out.println(rangeStart+" through "+rangeEnd+" - Total: "+input.size()+" Valid: "+output.size()+" Duration: "+(finish-beginning)+" ms.");
			return output;
		}
	
	
	
	public static boolean isVerticalSymetric(LinkedList<Integer> in, int rows, int columns){
		boolean isSymetric=true;
		if(rows%2==0){
			int half=rows/2;
			//Even number of rows.
			for(int i=0;i<in.size() && isSymetric;i++){
				if(Math.ceil((in.get(i)+columns-1)/columns) <=half){
					isSymetric=in.contains((int)(in.get(i)+(2*(half-Math.ceil((in.get(i)+columns-1)/columns)) +1)*columns));
				//	System.out.println(of.get(i)+" V becomes "+result.getLast());
				}else{
					isSymetric=in.contains((int)(in.get(i) - (2*(Math.ceil((in.get(i)+columns-1)/columns)-half-1) +1)*columns));
				//	System.out.println(of.get(i)+" W becomes "+result.getLast());
				}
			}
		}else{
			int middleModulo=(rows+1)/2;
			for(int i=0;i<in.size()&& isSymetric;i++){
				if(Math.ceil((in.get(i)+columns-1)/columns) <middleModulo){
					isSymetric=in.contains((int)(in.get(i)+ (2*(middleModulo-Math.ceil((in.get(i)+columns-1)/columns)))*columns));
				//	System.out.println(of.get(i)+" X becomes "+result.getLast()+" and middle modulo is "+middleModulo+" with ceil "+(int)Math.ceil((of.get(i)+columns-1)/columns) );
				}else if(Math.ceil((in.get(i)+columns-1)/columns) == middleModulo){
					isSymetric=in.contains(in.get(i));
				//	System.out.println(of.get(i)+" Y becomes "+result.getLast());
				}else{
					isSymetric=in.contains((int)(in.get(i)- (2*(Math.ceil((in.get(i)+columns-1)/columns)-middleModulo))*columns));
				//	System.out.println(of.get(i)+" Z becomes "+result.getLast());
				}
			}
		}
		return isSymetric;
		
	}
	
	public static boolean isHorizontalSymetric(LinkedList<Integer> in, int rows, int columns){
		boolean isSymetric=true;
		
		if(columns%2==0){
			//Even number of columns.
			int half=columns/2;			
			for(int i=0;i<in.size()&& isSymetric;i++){
				if(in.get(i)%columns<=half && in.get(i)%columns!=0){
					isSymetric=in.contains((int)(in.get(i)+1+2*(half-in.get(i)%columns)));
				//	System.out.println(in.get(i)+" A becomes "+result.getLast());
				}else if(in.get(i)%columns>half ){
					isSymetric=in.contains((int)(in.get(i) -1- 2*(in.get(i)%columns -half-1) ));
				//	System.out.println(in.get(i)+" B becomes "+result.getLast());
				}else{
					isSymetric=in.contains((int)(in.get(i) -columns+1));
				//	System.out.println(in.get(i)+" F becomes "+result.getLast());
				}
			}
			
		}else{
			//Odd number in columns.
			int middleModulo=((columns+1)/2);
			for(int i=0;i<in.size()&& isSymetric;i++){
				if(in.get(i)%columns <middleModulo && in.get(i)%columns!=0){
					isSymetric=in.contains(in.get(i)+ 2*(middleModulo-(in.get(i)%columns)));
				//	System.out.println(in.get(i)+" C becomes "+result.getLast());
				}else if(in.get(i)%columns == middleModulo){
					isSymetric=in.contains(in.get(i));
				//	System.out.println(in.get(i)+" D becomes "+result.getLast());
				}else{
					isSymetric=in.contains(in.get(i)- 2*(in.get(i)%columns)-middleModulo);
				//	System.out.println(of.get(i)+" E becomes "+result.getLast());
				}
			}
		}
		
		return isSymetric;
	}
	
	public static LinkedList<String> removeSimpleOriginal(LinkedList<String> input){
			System.out.println("Start of super-check.");
			long start=System.currentTimeMillis();
			LinkedList<String> output =new LinkedList<String>();
			
			for(int i=0;i<input.size();i++){
				
				String[] temp1=input.get(i).split(":");
				String [] temp2 =temp1[1].split("_");
			//	currentCells=new LinkedList<Integer>();
				LinkedList<Integer> listForCallingThread=new LinkedList<Integer>();
				for(int j=0;j<temp2.length;j++){
					listForCallingThread.add(Integer.parseInt(temp2[j]));
				}
				boolean simple=false;
				for(int l=0;l<listForCallingThread.size()&&!simple;l++){
					simple=checkFrom(listForCallingThread.get(l),listForCallingThread);
				}
				if(!simple){
					output.add(input.get(i));
				}
			}
			long finish=System.currentTimeMillis();
			System.out.println("End of super-check: From "+ input.size()+ " to " +output.size()+" in "+(finish-start)+" ms.");
			return output;
		}
	
	private Checker(){
		
	}
	
	private Checker(LinkedList<Integer> input){
		cells=input;
	}
	
	/**Returns true when the level is found to be simple. */
	private static boolean checkFrom(int init,LinkedList<Integer> receivedFromInitiator){
		LinkedList<Integer> prep=new LinkedList<Integer>();
		prep.add(init);
		Checker root =new Checker(prep);	
		//return one||two||three||four;
		return checkElement(root,receivedFromInitiator);
	}
	
	/**Returns true when the level is found to be simple. */
	private static boolean checkElement(Checker e, LinkedList<Integer> toUse){
		/*Checks to see if all the cells in the level are included and if they are, return true,
		 * which means that the level is simple.
		 * If all the cells are not included, try adding the neighboring cells. 
		 * Recursively verify if one of the new chains has all the cells. If from this checker no 
		 * chain containing all the cells can be built, return false.
		 * Return false means that the level can not be simply solved from the current chain (Checker). */
		boolean result=false;
		boolean allCellsIn=true;
		for(int i=0;i<toUse.size()&&allCellsIn;i++){
			int a=toUse.get(i);
			boolean cellIn=false;
			for(int j=0;j<e.cells.size()&&!cellIn;j++){
				if(a==e.cells.get(j)){
					cellIn=true;
				}
			}
			if(!cellIn){
				allCellsIn=false;
			}
		}
		
		if(!allCellsIn){
			boolean foundSimple=false;
			int lastAdded=e.cells.getLast();
			//Try and add the neighboring cells.
			//UP
				if(lastAdded>numbCols && toUse.contains(lastAdded-numbCols)&& !e.cells.contains(lastAdded-numbCols)){
					//Try adding cell at the top
					@SuppressWarnings("unchecked")
					LinkedList<Integer> dum =((LinkedList<Integer>)e.cells.clone());
					dum.add(lastAdded-numbCols);
						Checker c=new Checker(dum);
						foundSimple=checkElement(c,toUse);
					}
				
				//DOWN
				if(lastAdded<((numbCols*(numbRows-1))+1) && toUse.contains(lastAdded+numbCols)&& !e.cells.contains(lastAdded+numbCols) && !foundSimple){
					//Try adding cell at the bottom.
					@SuppressWarnings("unchecked")
					LinkedList<Integer> dum =((LinkedList<Integer>)e.cells.clone());
					dum.add(lastAdded+numbCols);
						Checker c=new Checker(dum);
						foundSimple=checkElement(c,toUse);
				}
				//RIGHT
				if(lastAdded%numbCols!=0 && toUse.contains(lastAdded+1)&& !e.cells.contains(lastAdded+1) && !foundSimple){
					//Try adding cell at the right
					@SuppressWarnings("unchecked")
					LinkedList<Integer> dum =((LinkedList<Integer>)e.cells.clone());
					dum.add(lastAdded+1);
						Checker c=new Checker(dum);
						foundSimple=checkElement(c,toUse);
				}
				//LEFT
				if(lastAdded%numbCols!=1 && toUse.contains(lastAdded-1)&& !e.cells.contains(lastAdded-1) && !foundSimple){
					//Try adding cell at the left
					@SuppressWarnings("unchecked")
					LinkedList<Integer> dum =((LinkedList<Integer>)e.cells.clone());
					dum.add(lastAdded-1);
						Checker c=new Checker(dum);
						foundSimple=checkElement(c,toUse);
				}		
				result=foundSimple;
		}else{
			result=true;
		}
		return result;
	}
	
	public static void setNumberOfCols(int a){
		numbCols=a;
	}
	
	public static void setNumberOfRows(int b){
		numbRows=b;
	}
	
	public static boolean canBeSolvedFrom(int initial, LinkedList<Integer> level,int rows, int cols){
		//First input should be like this for example: _1_ with the cell added specified. 
		boolean result=false;
		String base="_"+initial+"_";
		if(!result && initial>cols && level.contains(initial-cols)&& !Alpha.hasEnoughFound()){
			result=trySolvingByAdding(initial-cols,initial,level,base,rows,cols);
		}
		if(!result && initial%cols!=0 && level.contains(initial+1)&& !Alpha.hasEnoughFound()){
			result=trySolvingByAdding(initial+1,initial,level,base,rows,cols);
		}
		if(!result && initial%cols!=1 && level.contains(initial-1)&& !Alpha.hasEnoughFound()){
			result=trySolvingByAdding(initial-1,initial,level,base,rows,cols);
		}
		if(!result && initial<=((rows*cols)-cols) && level.contains(initial+cols)&& !Alpha.hasEnoughFound()){
			result=trySolvingByAdding(initial+cols,initial,level,base,rows,cols);
		}
		
		return result;
	}
	
	
	
	public static boolean trySolvingByAdding(int toAdd, int lastAdded, LinkedList<Integer> level, String input, int rows, int cols){
		if(input.contains("_"+lastAdded+"_"+toAdd+"_")||input.contains("_"+toAdd+"_"+lastAdded+"_")){
			return false;
		}else{
			//Add the cell and see if the level is solved.
			//If not, try adding each of the four neighboring cells.
			String continuation=input+toAdd+"_";
			boolean allIn=true;
			for(int i=0;i<level.size()&&allIn;i++){
				if(!continuation.contains("_"+level.get(i)+"_")){
					allIn=false;
				}
			}
			if(allIn){
			//	System.out.println(continuation+" contains ");
			//	System.out.println(level);
				return true;
			}else{
				boolean result=false;
				if(!result && toAdd>cols && level.contains(toAdd-cols)&& !Alpha.hasEnoughFound()){
					result=trySolvingByAdding(toAdd-cols,toAdd,level,continuation,rows,cols);
				}
				if(!result && toAdd%cols!=0 && level.contains(toAdd+1)&& !Alpha.hasEnoughFound()){
					result=trySolvingByAdding(toAdd+1,toAdd,level,continuation,rows,cols);
				}
				if(!result && toAdd%cols!=1 && level.contains(toAdd-1)&& !Alpha.hasEnoughFound()){
					result=trySolvingByAdding(toAdd-1,toAdd,level,continuation,rows,cols);
				}
				if(!result && toAdd<=((rows*cols)-cols) && level.contains(toAdd+cols)&& !Alpha.hasEnoughFound()){
					result=trySolvingByAdding(toAdd+cols,toAdd,level,continuation,rows,cols);
				}
				return result;
				
			}
		}
	}
	
	public static boolean canBeComplexSolvedFrom(int initial, LinkedList<Integer> level,int rows, int cols){
		//First input should be like this for example: _1_ with the cell added specified. 
		Decision result=Decision.NotSolved;
		String base="_"+initial+"_";
		long start=System.currentTimeMillis();
		if(result==Decision.NotSolved && initial>cols && level.contains(initial-cols) && !Alpha.hasEnoughFound()){
			result=tryComplexSolvingByAdding(initial-cols,initial,level,base,rows,cols,start);
		}
		start=System.currentTimeMillis();
		if(result==Decision.NotSolved && initial%cols!=0 && level.contains(initial+1)&& !Alpha.hasEnoughFound()){
				result=tryComplexSolvingByAdding(initial+1,initial,level,base,rows,cols,start);
		}
		start=System.currentTimeMillis();
		if(result==Decision.NotSolved && initial%cols!=1 && level.contains(initial-1)&& !Alpha.hasEnoughFound()){
			result=tryComplexSolvingByAdding(initial-1,initial,level,base,rows,cols,start);
		}
		start=System.currentTimeMillis();
		if(result==Decision.NotSolved && initial<=((rows*cols)-cols) && level.contains(initial+cols)&& !Alpha.hasEnoughFound()){
			result=tryComplexSolvingByAdding(initial+cols,initial,level,base,rows,cols,start);
		}
		
		
		if(result==Decision.SolvedSimple){
			return false;
		}else if(result==Decision.NotSolved){
			return false;
		}else{
			return true;
		}
	}
	
	public static Decision tryComplexSolvingByAdding(int toAdd, int lastAdded, LinkedList<Integer> level, String input, int rows, int cols,long startTime){
		if(input.contains("_"+lastAdded+"_"+toAdd+"_")||input.contains("_"+toAdd+"_"+lastAdded+"_")){
			return Decision.NotSolved;
		}else{
			//Add the cell and see if the level is solved.
			//If not, try adding each of the four neighboring cells.
			String continuation=input+toAdd+"_";
			boolean allIn=true;
			for(int i=0;i<level.size()&&allIn;i++){
				if(!continuation.contains("_"+level.get(i)+"_")){
					allIn=false;
				}
			}
			if(allIn){
				String [] solution =continuation.split("_");
				if(level.size()==solution.length-1){
					return Decision.SolvedSimple;
					//One of the cells was crossed twice in this solution.  good solution.
				}else{
					return Decision.SolvedComplex;
				}
			//	System.out.println(continuation+" contains ");
			//	System.out.println(level);
			//	return true;
			}else{
				Decision result=Decision.NotSolved;
				
				
				if(System.currentTimeMillis()-startTime<60000){
					if(result==Decision.NotSolved && toAdd>cols && level.contains(toAdd-cols)&& !Alpha.hasEnoughFound()){
						result=tryComplexSolvingByAdding(toAdd-cols,toAdd,level,continuation,rows,cols,startTime);
					}
				}
				if(System.currentTimeMillis()-startTime<60000){
					if(result ==Decision.NotSolved && toAdd%cols!=0 && level.contains(toAdd+1)&& !Alpha.hasEnoughFound()){
						result=tryComplexSolvingByAdding(toAdd+1,toAdd,level,continuation,rows,cols,startTime);
					}
				}
				if(System.currentTimeMillis()-startTime<60000){
					if(result ==Decision.NotSolved && toAdd%cols!=1 && level.contains(toAdd-1)&& !Alpha.hasEnoughFound()){
						result=tryComplexSolvingByAdding(toAdd-1,toAdd,level,continuation,rows,cols,startTime);
					}
				}
				if(System.currentTimeMillis()-startTime<60000){
					if(result ==Decision.NotSolved && toAdd<=((rows*cols)-cols) && level.contains(toAdd+cols)&& !Alpha.hasEnoughFound()){
						result=tryComplexSolvingByAdding(toAdd+cols,toAdd,level,continuation,rows,cols,startTime);
					}
				}
				return result;
			}
		}
	}
	
	public static boolean canBeComplexSolvedFrom2(int initial, LinkedList<Integer> level,int rows, int cols){
		//First input should be like this for example: _1_ with the cell added specified. 
		Decision result=Decision.NotSolved;
		String base="_"+initial+"_";
		long start=System.currentTimeMillis();
		if(result==Decision.NotSolved && initial>cols && level.contains(initial-cols) && !Alpha.hasEnoughFound()){
			result=tryComplexSolvingByAdding2(initial-cols,initial,level,base,rows,cols,start);
		}
		start=System.currentTimeMillis();
		if(result==Decision.NotSolved && initial%cols!=0 && level.contains(initial+1)&& !Alpha.hasEnoughFound()){
				result=tryComplexSolvingByAdding2(initial+1,initial,level,base,rows,cols,start);
		}
		start=System.currentTimeMillis();
		if(result==Decision.NotSolved && initial%cols!=1 && level.contains(initial-1)&& !Alpha.hasEnoughFound()){
			result=tryComplexSolvingByAdding2(initial-1,initial,level,base,rows,cols,start);
		}
		start=System.currentTimeMillis();
		if(result==Decision.NotSolved && initial<=((rows*cols)-cols) && level.contains(initial+cols)&& !Alpha.hasEnoughFound()){
			result=tryComplexSolvingByAdding2(initial+cols,initial,level,base,rows,cols,start);
		}
		
		
		if(result==Decision.SolvedSimple){
			return false;
		}else if(result==Decision.NotSolved){
			return false;
		}else{
			return true;
		}
	}
	
	
	public static Decision tryComplexSolvingByAdding2(int toAdd, int lastAdded, LinkedList<Integer> level, String input, int rows, int cols,long startTime){
			//Add the cell and see if the level is solved.
			//If not, try adding each of the four neighboring cells.
			String continuation=input+toAdd+"_";
			boolean allIn=true;
			for(int i=0;i<level.size()&&allIn;i++){
				if(!continuation.contains("_"+level.get(i)+"_")){
					allIn=false;
				}
			}
			if(allIn){
				String [] solution =continuation.split("_");
				if(level.size()==solution.length-1){
					return Decision.SolvedSimple;
					//One of the cells was crossed twice in this solution.  good solution.
				}else{
					return Decision.SolvedComplex;
				}
			//	System.out.println(continuation+" contains ");
			//	System.out.println(level);
			//	return true;
			}else{
				Decision result=Decision.NotSolved;
				boolean one=input.contains("_"+(toAdd-cols)+"_"+toAdd+"_")||input.contains("_"+toAdd+"_"+(toAdd-cols)+"_");
				boolean two=input.contains("_"+(toAdd+1)+"_"+toAdd+"_")||input.contains("_"+toAdd+"_"+(toAdd+1)+"_");
				boolean three=input.contains("_"+(toAdd-1)+"_"+toAdd+"_")||input.contains("_"+toAdd+"_"+(toAdd-1)+"_");
				boolean four=input.contains("_"+(toAdd+cols)+"_"+toAdd+"_")||input.contains("_"+toAdd+"_"+(toAdd+cols)+"_");
				
				if(System.currentTimeMillis()-startTime<60000){
					if(result==Decision.NotSolved && toAdd>cols && level.contains(toAdd-cols)&& !Alpha.hasEnoughFound()&& !one){
						result=tryComplexSolvingByAdding(toAdd-cols,toAdd,level,continuation,rows,cols,startTime);
					}
				}
				if(System.currentTimeMillis()-startTime<60000){
					if(result ==Decision.NotSolved && toAdd%cols!=0 && level.contains(toAdd+1)&& !Alpha.hasEnoughFound() && !two){
						result=tryComplexSolvingByAdding(toAdd+1,toAdd,level,continuation,rows,cols,startTime);
					}
				}
				if(System.currentTimeMillis()-startTime<60000){
					if(result ==Decision.NotSolved && toAdd%cols!=1 && level.contains(toAdd-1)&& !Alpha.hasEnoughFound() && !three){
						result=tryComplexSolvingByAdding(toAdd-1,toAdd,level,continuation,rows,cols,startTime);
					}
				}
				if(System.currentTimeMillis()-startTime<60000){
					if(result ==Decision.NotSolved && toAdd<=((rows*cols)-cols) && level.contains(toAdd+cols)&& !Alpha.hasEnoughFound()&& !four){
						result=tryComplexSolvingByAdding(toAdd+cols,toAdd,level,continuation,rows,cols,startTime);
					}
				}
				return result;
			}
	}
	
	public static boolean canBeSimplySolvedFrom(int initial, LinkedList<Integer> level,int rows, int cols){
		//First input should be like this for example: _1_ with the cell added specified. 
		Decision result=Decision.NotSolved;
		String base="_"+initial+"_";
		long start=System.currentTimeMillis();
		if(result!=Decision.SolvedSimple && initial>cols && level.contains(initial-cols)&& !Alpha.hasEnoughFound()){
			result=trySimplySolvingByAdding(initial-cols,initial,level,base,rows,cols,start);
		}
		start=System.currentTimeMillis();
		if(result!=Decision.SolvedSimple && initial%cols!=0 && level.contains(initial+1)&& !Alpha.hasEnoughFound()){
				result=trySimplySolvingByAdding(initial+1,initial,level,base,rows,cols,start);
		}
		start=System.currentTimeMillis();
		if(result!=Decision.SolvedSimple && initial%cols!=1 && level.contains(initial-1)&& !Alpha.hasEnoughFound()){
				result=trySimplySolvingByAdding(initial-1,initial,level,base,rows,cols,start);
		}
		start=System.currentTimeMillis();
		if(result!=Decision.SolvedSimple && initial<=((rows*cols)-cols) && level.contains(initial+cols)&& !Alpha.hasEnoughFound()){
				result=trySimplySolvingByAdding(initial+cols,initial,level,base,rows,cols,start);
		}
		
		
		if(result==Decision.SolvedComplex){
			return false;
		}else if(result==Decision.NotSolved){
			return false;
		}else{
			return true;
		}
	}
	
	public static Decision trySimplySolvingByAdding(int toAdd, int lastAdded, LinkedList<Integer> level, String input, int rows, int cols, long startTime){
		if(input.contains("_"+lastAdded+"_"+toAdd+"_")||input.contains("_"+toAdd+"_"+lastAdded+"_")){
			return Decision.NotSolved;
		}else{
			//Add the cell and see if the level is solved.
			//If not, try adding each of the four neighboring cells.
			String continuation=input+toAdd+"_";
			boolean allIn=true;
			for(int i=0;i<level.size()&&allIn;i++){
				if(!continuation.contains("_"+level.get(i)+"_")){
					allIn=false;
				}
			}
			if(allIn){
				String [] solution =continuation.split("_");
				if(level.size()==solution.length-1){
					return Decision.SolvedSimple;
					//One of the cells was crossed twice in this solution.  good solution.
				}else{
					return Decision.SolvedComplex;
				}
			//	System.out.println(continuation+" contains ");
			//	System.out.println(level);
			//	return true;
			}else{
				Decision result=Decision.NotSolved;
				
				if(System.currentTimeMillis()-startTime<60000){
					if(result!=Decision.SolvedSimple && toAdd>cols && level.contains(toAdd-cols)&& !Alpha.hasEnoughFound()){
						result=trySimplySolvingByAdding(toAdd-cols,toAdd,level,continuation,rows,cols,startTime);
					}
					if(result!=Decision.SolvedSimple && toAdd%cols!=0 && level.contains(toAdd+1)&& !Alpha.hasEnoughFound()){
						result=trySimplySolvingByAdding(toAdd+1,toAdd,level,continuation,rows,cols,startTime);
					}
					if(result!=Decision.SolvedSimple && toAdd%cols!=1 && level.contains(toAdd-1)&& !Alpha.hasEnoughFound()){
						result=trySimplySolvingByAdding(toAdd-1,toAdd,level,continuation,rows,cols,startTime);
					}
					if(result!=Decision.SolvedSimple && toAdd<=((rows*cols)-cols) && level.contains(toAdd+cols)&& !Alpha.hasEnoughFound()){
						result=trySimplySolvingByAdding(toAdd+cols,toAdd,level,continuation,rows,cols,startTime);
					}
				}
				
				return result;
				
			}
		}
	}
	
	public static boolean isSimpleFrom(int initial, LinkedList<Integer> level,int rows, int cols){
		//First input should be like this for example: _1_ with the cell added specified. 
				boolean result=false;
			//	System.out.println("Rows: "+rows+" Columns: "+cols);
				String base="_"+initial+"_";
				if(!result && initial%cols!=0 && level.contains(initial+1)){
					result=isSimpleFrom_Recursive(initial+1,initial,level,base,rows,cols,1);
				}
			//	System.out.println("1/4 completed"+(result? " - Simple": " - Not simple"+ " Base: "+base));
				if(!result && initial>cols && level.contains(initial-cols)){
					result=isSimpleFrom_Recursive(initial-cols,initial,level,base,rows,cols,1);
				}
				//System.out.println("2/4 completed"+(result? " - Simple": " - Not simple"));
				if(!result && initial%cols!=1 && level.contains(initial-1)){
						result=isSimpleFrom_Recursive(initial-1,initial,level,base,rows,cols,1);
				}
			//	System.out.println("3/4 completed"+(result? " - Simple": " - Not simple"));
				if(!result && initial<=((rows*cols)-cols) && level.contains(initial+cols)){
						result=isSimpleFrom_Recursive(initial+cols,initial,level,base,rows,cols,1);
				}
			//	System.out.println((result? "Was simple": "Was not simple, attempting other starting point."));
				return result;
	}
	
	public static boolean isSimpleFrom_Recursive(int toAdd, int lastAdded, LinkedList<Integer> level, String input, int rows, int cols, int addedSoFar){
		//boolean allIn=true;
		if(addedSoFar==level.size()){
			boolean allIn=true;
			for(int i=0;i<level.size()&&allIn;i++){
				if(!input.contains("_"+level.get(i)+"_")){
					allIn=false;
				}
			}
			if(allIn){
			//	System.out.println("Found a simple solution!"); 
				return true;
			}else{
				System.out.println("Doesn't work.");
				return false;
		}
		}else if(addedSoFar>level.size()){
			System.out.println("Too many dots added, stop.");
			return false;
			
		}else if(addedSoFar<level.size()){
			if(input.contains("_"+lastAdded+"_"+toAdd+"_")||input.contains("_"+toAdd+"_"+lastAdded+"_")|| input.contains("_"+toAdd+"_")){
				//System.out.println("Cant't add "+toAdd+" to "+input);
				return false;
			}else{
				String continuation=input+toAdd+"_";
				boolean result=false;
				if(!result && toAdd>cols && level.contains(toAdd-cols) &&toAdd-cols!=lastAdded){
					result=isSimpleFrom_Recursive(toAdd-cols,toAdd,level,continuation,rows,cols,addedSoFar+1);
				}
				//System.out.println("B 1/4 completed"+(result? " - Simple": " - Not simple"));
				if(!result && toAdd%cols!=0 && level.contains(toAdd+1)&& toAdd+1!=lastAdded){
					result=isSimpleFrom_Recursive(toAdd+1,toAdd,level,continuation,rows,cols,addedSoFar+1);
				}
			//	System.out.println("B 2/4 completed"+(result? " - Simple": " - Not simple"));
				if(!result && toAdd%cols!=1 && level.contains(toAdd-1)&&toAdd-1!=lastAdded){
					result=isSimpleFrom_Recursive(toAdd-1,toAdd,level,continuation,rows,cols,addedSoFar+1);
				}
			//	System.out.println("B 3/4 completed"+(result? " - Simple": " - Not simple"));
				if(!result && toAdd<=((rows*cols)-cols) && level.contains(toAdd+cols)&&toAdd+cols!=lastAdded){
					result=isSimpleFrom_Recursive(toAdd+cols,toAdd,level,continuation,rows,cols,addedSoFar+1);
				}
			//	System.out.println("B "+(result? "Was simple": " Not simple overall"));
			return result;
			}
		}
		return false;
	}
	
	
	
	public static boolean isSimpleFrom_Recursive2(int toAdd, int lastAdded, LinkedList<Integer> level, String input, int rows, int cols, int addedSoFar){
		//boolean allIn=true;
		if(addedSoFar==level.size()){
			boolean allIn=true;
			for(int i=0;i<level.size()&&allIn;i++){
				if(!input.contains("_"+level.get(i)+"_")){
					allIn=false;
				}
			}
			if(allIn){
			//	System.out.println("Found a simple solution!"); 
				return true;
			}else{
				System.out.println("Doesn't work.");
				return false;
		}
		}else if(addedSoFar>level.size()){
			System.out.println("Too many dots added, stop.");
			return false;
			
		}else if(addedSoFar<level.size()){
			if(input.contains("_"+lastAdded+"_"+toAdd+"_")||input.contains("_"+toAdd+"_"+lastAdded+"_")|| input.contains("_"+toAdd+"_")){
				//System.out.println("Cant't add "+toAdd+" to "+input);
				return false;
			}else{
				String continuation=input+toAdd+"_";
				boolean result=false;
				if(!result && toAdd>cols && level.contains(toAdd-cols) &&toAdd-cols!=lastAdded){
					result=isSimpleFrom_Recursive(toAdd-cols,toAdd,level,continuation,rows,cols,addedSoFar+1);
				}
				//System.out.println("B 1/4 completed"+(result? " - Simple": " - Not simple"));
				if(!result && toAdd%cols!=0 && level.contains(toAdd+1)&& toAdd+1!=lastAdded){
					result=isSimpleFrom_Recursive(toAdd+1,toAdd,level,continuation,rows,cols,addedSoFar+1);
				}
			//	System.out.println("B 2/4 completed"+(result? " - Simple": " - Not simple"));
				if(!result && toAdd%cols!=1 && level.contains(toAdd-1)&&toAdd-1!=lastAdded){
					result=isSimpleFrom_Recursive(toAdd-1,toAdd,level,continuation,rows,cols,addedSoFar+1);
				}
			//	System.out.println("B 3/4 completed"+(result? " - Simple": " - Not simple"));
				if(!result && toAdd<=((rows*cols)-cols) && level.contains(toAdd+cols)&&toAdd+cols!=lastAdded){
					result=isSimpleFrom_Recursive(toAdd+cols,toAdd,level,continuation,rows,cols,addedSoFar+1);
				}
			//	System.out.println("B "+(result? "Was simple": " Not simple overall"));
			return result;
			}
		}
		return false;
	}
	
	public static String getSolution(LinkedList<Integer> level,int rows, int cols){
		String result=null;
		//FIX HERE ! ! ! !
		long init =System.currentTimeMillis();
		//FIX HERE ! ! ! !
		//PUT Outside of for loop.
		for(int i=0;i<level.size()&&result==null;i++){
			int startingCell=level.get(i);
			String base="_"+startingCell+"_";
			//System.out.println("Start time: "+init);
			
			if(result==null && startingCell>cols && level.contains(startingCell-cols)){
				result=getSolutionFast(startingCell-cols,startingCell,level,base,rows,cols,init);
			}
			if(result==null && startingCell%cols!=0 && level.contains(startingCell+1)){
				result=getSolutionFast(startingCell+1,startingCell,level,base,rows,cols,init);
			}
			if(result==null && startingCell%cols!=1 && level.contains(startingCell-1)){
				result=getSolutionFast(startingCell-1,startingCell,level,base,rows,cols,init);
			}
			if(result==null && startingCell<=((rows*cols)-cols) && level.contains(startingCell+cols)){
				result=getSolutionFast(startingCell+cols,startingCell,level,base,rows,cols,init);
			}
			
			/*if(result==null && startingCell>cols && level.contains(startingCell-cols)){
				result=getSolutionRecursive(startingCell-cols,startingCell,level,base,rows,cols);
			}
			if(result==null && startingCell%cols!=0 && level.contains(startingCell+1)){
				result=getSolutionRecursive(startingCell+1,startingCell,level,base,rows,cols);
			}
			if(result==null && startingCell%cols!=1 && level.contains(startingCell-1)){
				result=getSolutionRecursive(startingCell-1,startingCell,level,base,rows,cols);
			}
			if(result==null && startingCell<=((rows*cols)-cols) && level.contains(startingCell+cols)){
				result=getSolutionRecursive(startingCell+cols,startingCell,level,base,rows,cols);
			}*/
		}
		return result;
		//return null;
	}
	
	public static String getSolutionRecursive(int toAdd, int lastAdded, LinkedList<Integer> level, String input, int rows, int cols){
		String result=null;
		if(input.contains("_"+lastAdded+"_"+toAdd+"_")||input.contains("_"+toAdd+"_"+lastAdded+"_")){
			return null;
		}else{
			//Add the cell and see if the level is solved.
			//If not, try adding each of the four neighboring cells.
			String continuation=input+toAdd+"_";
			boolean allIn=true;
			for(int i=0;i<level.size()&&allIn;i++){
				if(!continuation.contains("_"+level.get(i)+"_")){
					allIn=false;
				}
			}
			if(allIn){
// The solution returned is in the form: _int_int_int_int_.
				//TODO: Make sure to remove first and last characters to have usable solution. 
				return continuation;
			}else{
				if(result==null && toAdd>cols && level.contains(toAdd-cols)){
					result=getSolutionRecursive(toAdd-cols,toAdd,level,continuation,rows,cols);
				}
				if(result==null && toAdd%cols!=0 && level.contains(toAdd+1)){
					result=getSolutionRecursive(toAdd+1,toAdd,level,continuation,rows,cols);
				}
				if(result==null && toAdd%cols!=1 && level.contains(toAdd-1)){
					result=getSolutionRecursive(toAdd-1,toAdd,level,continuation,rows,cols);
				}
				if(result==null && toAdd<=((rows*cols)-cols) && level.contains(toAdd+cols)){
					result=getSolutionRecursive(toAdd+cols,toAdd,level,continuation,rows,cols);
				}
				return result;
			}
		}
	}
	
	public static String getSolutionFast(int toAdd, int lastAdded, LinkedList<Integer> level, String input, int rows, int cols,long initTime){
		String result=null;
		long delay=System.currentTimeMillis()-initTime;
		//System.out.println("Using fast algorithm)");
		if(delay>3000000){
			System.out.println("Reached the limit. Returning void.");
			return "Void";
		}else{
			//System.out.println("Time elapsed in seconds: "+(curr-initTime)/1000+" in ms: "+(curr-initTime));
			if(input.contains("_"+lastAdded+"_"+toAdd+"_")||input.contains("_"+toAdd+"_"+lastAdded+"_")){
				return null;
			}else{
				//Add the cell and see if the level is solved.
				//If not, try adding each of the four neighboring cells.
				String continuation=input+toAdd+"_";
				boolean allIn=true;
				for(int i=0;i<level.size()&&allIn;i++){
					if(!continuation.contains("_"+level.get(i)+"_")){
						allIn=false;
					}
				}
				if(allIn){
	// The solution returned is in the form: _int_int_int_int_.
					//TODO: Make sure to remove first and last characters to have usable solution. 
					return continuation;
				}else{
					//If no solution found under 5 minutes, abort.
						if(result==null && toAdd>cols && level.contains(toAdd-cols)){
							result=getSolutionFast(toAdd-cols,toAdd,level,continuation,rows,cols,initTime);
						}
						if(result==null && toAdd%cols!=0 && level.contains(toAdd+1)){
							result=getSolutionFast(toAdd+1,toAdd,level,continuation,rows,cols,initTime);
						}
						if(result==null && toAdd%cols!=1 && level.contains(toAdd-1)){
							result=getSolutionFast(toAdd-1,toAdd,level,continuation,rows,cols,initTime);
						}
						if(result==null && toAdd<=((rows*cols)-cols) && level.contains(toAdd+cols)){
							result=getSolutionFast(toAdd+cols,toAdd,level,continuation,rows,cols,initTime);
						}
					return result;
				}
			}
		}
	}
	
	/**Returns true when the level can be solved with a solution that has less than 3 double crossings. */
	public static boolean isSimpleFrom3(int initial, LinkedList<Integer> level,int rows, int cols){
		//First input should be like this for example: _1_ with the cell added specified. 
				boolean result=false;
			//	System.out.println("Rows: "+rows+" Columns: "+cols);
				String base="_"+initial+"_";
				if(!result && initial%cols!=0 && level.contains(initial+1)){
					result=isSimpleFrom_Recursive3(initial+1,initial,level,base,rows,cols,1);
				}
			//	System.out.println("1/4 completed"+(result? " - Simple": " - Not simple"+ " Base: "+base));
				if(!result && initial>cols && level.contains(initial-cols)){
					result=isSimpleFrom_Recursive3(initial-cols,initial,level,base,rows,cols,1);
				}
				//System.out.println("2/4 completed"+(result? " - Simple": " - Not simple"));
				if(!result && initial%cols!=1 && level.contains(initial-1)){
						result=isSimpleFrom_Recursive3(initial-1,initial,level,base,rows,cols,1);
				}
			//	System.out.println("3/4 completed"+(result? " - Simple": " - Not simple"));
				if(!result && initial<=((rows*cols)-cols) && level.contains(initial+cols)){
						result=isSimpleFrom_Recursive3(initial+cols,initial,level,base,rows,cols,1);
				}
			//	System.out.println((result? "Was simple": "Was not simple, attempting other starting point."));
				return result;
	}
	
	public static boolean isSimpleFrom_Recursive3(int toAdd, int lastAdded, LinkedList<Integer> level, String input, int rows, int cols, int addedSoFar){
		//boolean allIn=true;
		if(addedSoFar>=level.size()){
			boolean allIn=true;
			for(int i=0;i<level.size()&&allIn;i++){
				if(!input.contains("_"+level.get(i)+"_")){
					allIn=false;
				}
			}
			if(allIn){
				//We have a solution, see if it has less than 3 double cells and return true if that's the case. 
				int delta=addedSoFar-level.size();
				if(delta<3)	{
					//System.out.println("Removed solution with just "+delta+" doubles.");
					return true;
				}else{
				//	System.out.println("Found a simple solution!");
				return false;
			}
		}
			
		}
			if(input.contains("_"+lastAdded+"_"+toAdd+"_")||input.contains("_"+toAdd+"_"+lastAdded+"_")){
				//System.out.println("Cant't add "+toAdd+" to "+input);
				return false;
			}else{
				String continuation=input+toAdd+"_";
				boolean result=false;
				if(!result && toAdd>cols && level.contains(toAdd-cols) &&toAdd-cols!=lastAdded){
					result=isSimpleFrom_Recursive3(toAdd-cols,toAdd,level,continuation,rows,cols,addedSoFar+1);
				}
				//System.out.println("B 1/4 completed"+(result? " - Simple": " - Not simple"));
				if(!result && toAdd%cols!=0 && level.contains(toAdd+1)&& toAdd+1!=lastAdded){
					result=isSimpleFrom_Recursive3(toAdd+1,toAdd,level,continuation,rows,cols,addedSoFar+1);
				}
			//	System.out.println("B 2/4 completed"+(result? " - Simple": " - Not simple"));
				if(!result && toAdd%cols!=1 && level.contains(toAdd-1)&&toAdd-1!=lastAdded){
					result=isSimpleFrom_Recursive3(toAdd-1,toAdd,level,continuation,rows,cols,addedSoFar+1);
				}
			//	System.out.println("B 3/4 completed"+(result? " - Simple": " - Not simple"));
				if(!result && toAdd<=((rows*cols)-cols) && level.contains(toAdd+cols)&&toAdd+cols!=lastAdded){
					result=isSimpleFrom_Recursive3(toAdd+cols,toAdd,level,continuation,rows,cols,addedSoFar+1);
				}
			//	System.out.println("B "+(result? "Was simple": " Not simple overall"));
			return result;
			}
	}
}
