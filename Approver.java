import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class Approver extends Thread{

	/*Only keeps the levels that have one or two cells with a single connection
	 * (Those cells are forced start or end points). */
	/**Finds levels that have one or two cells with a single connection that can be solved. 
	 * See method beginProcessing() for IMPORTANT information. 
	 * Check notifycompleted2 for control of number of solutions needed. */
	
	private static int activeThreads=0;
	private static LinkedList<Approver> waitingList;
	private LinkedList<LinkedList<Integer>> allLevels;
	private LinkedList<Integer> validIndexes;
	private LinkedList<String> canBeSolved;
	private static int found;
	private static int lastDisplayed;
	private static int totalThreads;
	private static int step;
	private static LinkedList<Integer> suggested; 
	//true when this approver is the last one. 
	boolean finalRun;
	int init;
	int last;
	int nCols;
	int nRows;
	int myListIndex;
	String myDiff;
	
	/**Checks the validity of levels at indexes initialIndex through lastIndex. */
	Approver(LinkedList<LinkedList<Integer>> levels, int initialIndex, int lastIndex,int cols,int rows, boolean lastRun, int myLI,String diff){
		allLevels=levels;
		init=initialIndex;
		last=lastIndex;
		validIndexes=new LinkedList<Integer>();
		canBeSolved=new LinkedList<String>();
		suggested=new LinkedList<Integer>();
		nCols=cols;
		nRows=rows;
		finalRun=lastRun;
		myListIndex=myLI;
		myDiff=diff;
	}
	
	public static void reset(){
		waitingList=new LinkedList<Approver>();
		activeThreads=0;
		lastDisplayed=0;
		found=0;
		step=2;
	}
	
	
	public void run(){
			if(myDiff.equals("E")){
				algorithm1();
			}else if(myDiff.equals("M")){
				algorithm2();
			}else if( myDiff.equals("H")){
				//algorithm3();
				//Performing test of a new algorithm for the hard levels.
				algorithm4();
			}else{
				System.out.println("Error - Invalid difficulty.");
			}
	}
	
	//BACKUP OF RUN METHOD BELOW - NEEDED FOR FAST PROCESSING.
/*public void run(){
		
		if(found<100){
			//System.out.println("Found so far: "+found+" - Launching");
			if(myDiff.equals("E")){
				algorithm1();
			}else if(myDiff.equals("M")){
				algorithm2();
			}else if( myDiff.equals("H")){
				algorithm3();
			}else{
				System.out.println("Error - Invalid difficulty.");
			}
		}else{
			//Alpha.increaseLaunch();
			//System.out.println("Bypassing because "+found+" found already."+activeThreads+ " threads active.");
			allLevels.clear();
			if(activeThreads==0){
				Alpha.setCompleted();
			}else{
				//notifyVoidCompleted();
				notifyVoidCompleted2();
			}
		}
		
	}*/
	
	/**EASY mode. (They have cells with a single connection).
	 * Solvale without crossings. */
	private void algorithm1(){
		Alpha.increaseLaunch();
		long start= System.currentTimeMillis();
	//	for(int i=init;i<=last;i++){
		boolean foundOne=false;
		for(int i=0;i<allLevels.size()&& !Alpha.hasEnoughFound();i++){
			//Use the line below instead of the one above if we want just one level in a set of 10.
		//for(int i=0;i<allLevels.size()&& !Alpha.hasEnoughFound()&& !foundOne;i++){
			LinkedList<Integer> current=null;
			try{
			current=allLevels.get(i);
			}catch(Exception e){
				System.out.println("Caught an exception.");
			}
			int singleConnections=0;
			boolean valid=true;
			int singleOne=0;
			int singleTwo=0;
			for(int j=0;current!=null && j<current.size() && valid && singleConnections<3;j++ ){
				int connections=0;
				int analyzed=current.get(j);
				if(analyzed>nCols && current.contains(analyzed-nCols)){
					connections++;
				}
				if(analyzed%nCols!=0 && current.contains(analyzed+1)){
					connections++;
				}
				if(analyzed%nCols!=1 && current.contains(analyzed-1)){
					connections++;
				}
				if(analyzed<((nCols*(nRows-1))+1) && current.contains(analyzed+nCols)){
					connections++;
				}
				if(connections==1){
					singleConnections++;
					if(singleOne==0){
						singleOne=analyzed;
					}else{
						singleTwo=analyzed;
					}
				}
				if(connections==0){
					valid=false;
				}
			}
			if(valid && 0<singleConnections ){
				boolean goodToAdd=true;
				if(singleOne!=0){
					goodToAdd=Checker.canBeComplexSolvedFrom(singleOne, current, nRows, nCols);
				}
				if(!goodToAdd && singleTwo!=0){
					goodToAdd=Checker.canBeComplexSolvedFrom(singleTwo, current, nRows, nCols);
				}
				if(goodToAdd){
					foundOne=true;
					validIndexes.add(i);
				}else{
					//System.out.println("Discarded an unsolvable level.");
				}
			}
		}
		//removeDisconnected();
		for(int k=0;k<validIndexes.size();k++){
			LinkedList<Integer> currL=allLevels.get(validIndexes.get(k));
			String level=nRows+"_"+nCols+":"+currL.get(0);
			for(int l=1;l<currL.size();l++){
				level=level+"_"+currL.get(l);
			}
			canBeSolved.add(level);
		}
		//Alpha.addToFinalResult(primary);
		Alpha.addToFinalResult(canBeSolved);
		found+=canBeSolved.size();
		System.out.println("Thread "+myListIndex+": "+canBeSolved.size()+" levels containing single connection(s) added.");
		//Alpha.addToFinalResult(Checker.keepSimpleSymetric(primary,init,last,start));
		//Alpha.addToFinalResult(Checker.removeSimple(canBeSolved,init,last,start,myListIndex));
		notifyCompleted2();
		if(finalRun){
			allLevels.clear();
			Alpha.setCompleted();
		}else{
			allLevels.clear();
		}
	}
	
	/**MEDIUM mode. 
	 * Must have isolated cells. Should require at least one crossing. 5x5 to 7x7  */
	private void algorithm2(){
		Alpha.increaseLaunch();
		long start= System.currentTimeMillis();
	//	for(int i=init;i<=last;i++){
		boolean foundOne=false;
		for(int i=0;i<allLevels.size() && !Alpha.hasEnoughFound() ;i++){
		//Use the line below instead of the one above if we want just one level in a set of 10.
	//	for(int i=0;i<allLevels.size() && !Alpha.hasEnoughFound() && !foundOne;i++){
			LinkedList<Integer> current=null;
			try{
			current=allLevels.get(i);
			}catch(Exception e){
				System.out.println("Caught an exception.");
			}
			int singleConnections=0;
			boolean valid=true;
			int singleOne=0;
			int singleTwo=0;
			for(int j=0;current!=null && j<current.size() && valid && singleConnections==0;j++ ){
				int connections=0;
				int analyzed=current.get(j);
				if(analyzed>nCols && current.contains(analyzed-nCols)){
					connections++;
				}
				if(analyzed%nCols!=0 && current.contains(analyzed+1)){
					connections++;
				}
				if(analyzed%nCols!=1 && current.contains(analyzed-1)){
					connections++;
				}
				if(analyzed<((nCols*(nRows-1))+1) && current.contains(analyzed+nCols)){
					connections++;
				}
				if(connections==1){
					singleConnections++;
					if(singleOne==0){
						singleOne=analyzed;
					}else{
						singleTwo=analyzed;
					}
				}
				if(connections==0){
					valid=false;
				}
			}
			if(valid && 0<singleConnections ){
				boolean goodToAdd=true;
				if(singleOne!=0){
					goodToAdd=Checker.canBeComplexSolvedFrom(singleOne, current, nRows, nCols);
				}
				if(!goodToAdd && singleTwo!=0){
					goodToAdd=Checker.canBeComplexSolvedFrom(singleTwo, current, nRows, nCols);
				}
				if(goodToAdd){
					foundOne=true;
					validIndexes.add(i);
				}else{
					//System.out.println("Discarded an unsolvable level.");
				}
			}
		}
		//removeDisconnected();
		for(int k=0;k<validIndexes.size();k++){
			LinkedList<Integer> currL=allLevels.get(validIndexes.get(k));
			String level=nRows+"_"+nCols+":"+currL.get(0);
			for(int l=1;l<currL.size();l++){
				level=level+"_"+currL.get(l);
			}
			canBeSolved.add(level);
		}
		
		Alpha.addToFinalResult(canBeSolved);
		found+=canBeSolved.size();
		System.out.println("Thread "+myListIndex+": "+canBeSolved.size()+" levels containing single connection(s) added.");
		//Alpha.addToFinalResult(Checker.keepSimpleSymetric(primary,init,last,start));
		//Alpha.addToFinalResult(Checker.removeSimple(canBeSolved,init,last,start,myListIndex));
		notifyCompleted2();
		if(finalRun){
			allLevels.clear();
			Alpha.setCompleted();
		}else{
			allLevels.clear();
		}
		
	}
	
	/**HARD Mode. Minimum of 7x5 and maximum of 8x8.
	 * (Suggested)
	 * No isolated cells. Must require crossing..  */
	private void algorithm3(){
		Alpha.increaseLaunch();
		long start= System.currentTimeMillis();
	//	for(int i=init;i<=last;i++){
		boolean foundOne=false;
		for(int i=0;i<allLevels.size() && !Alpha.hasEnoughFound();i++){
		//Use the line below instead of the one above if we want just one level in a set of 10.
		//for(int i=0;i<allLevels.size() && !Alpha.hasEnoughFound() && !foundOne;i++){
			LinkedList<Integer> current=null;
			try{
			current=allLevels.get(i);
			}catch(Exception e){
				System.out.println("Caught an exception.");
			}
			int singleConnections=0;
			boolean valid=true;
			for(int j=0;current!=null && j<current.size() && valid && singleConnections==0;j++ ){
				int connections=0;
				int analyzed=current.get(j);
				if(analyzed>nCols && current.contains(analyzed-nCols)){
					connections++;
				}
				if(analyzed%nCols!=0 && current.contains(analyzed+1)){
					connections++;
				}
				if(analyzed%nCols!=1 && current.contains(analyzed-1)){
					connections++;
				}
				if(analyzed<((nCols*(nRows-1))+1) && current.contains(analyzed+nCols)){
					connections++;
				}
				if(connections==1){
					singleConnections++;
				}
				if(connections==0){
					valid=false;
				}
			}
			if(valid && 0==singleConnections ){
				//System.out.println("Level valid and has no single cell.");
				boolean goodToAdd=false;
				
				if(isConnected(current)){
					//System.out.println("Level not disconnected.");
					for(int s=0;s<current.size()&&!goodToAdd;s++){
						//Add a condition in the checker to make sure that the level can not
						//be solved without one or more crossings. 
						goodToAdd=Checker.canBeComplexSolvedFrom2(current.get(s), current, nRows, nCols);
					}
				}
				
				if(goodToAdd){
					foundOne=true;
					validIndexes.add(i);
				}else{
					//System.out.println("Discarded an unsolvable or disconnected level.");
				}
			}
		}
		//removeDisconnected();
		for(int k=0;k<validIndexes.size();k++){
			LinkedList<Integer> currL=allLevels.get(validIndexes.get(k));
			String level=nRows+"_"+nCols+":"+currL.get(0);
			for(int l=1;l<currL.size();l++){
				level=level+"_"+currL.get(l);
			}
			canBeSolved.add(level);
		}
		
		Alpha.addToFinalResult(canBeSolved);
		found+=canBeSolved.size();
		System.out.println("Thread "+myListIndex+": "+canBeSolved.size()+" levels containing single connection(s) added.");
		//Alpha.addToFinalResult(Checker.keepSimpleSymetric(primary,init,last,start));
		//Alpha.addToFinalResult(Checker.removeSimple(canBeSolved,init,last,start,myListIndex));
		notifyCompleted2();
		if(finalRun){
			allLevels.clear();
			System.out.println("ABC");
			Alpha.setCompleted();
		}else{
			allLevels.clear();
		}
	}
	
	/** New HARD Mode. Minimum of 7x5 and maximum of 8x8.
	 * (Suggested)
	 * No isolated cells. Must require crossing..  */
	private void algorithm4(){
		Alpha.increaseLaunch();
		long start= System.currentTimeMillis();
	//	for(int i=init;i<=last;i++){
		boolean foundOne=false;
		for(int i=0;i<allLevels.size() && !Alpha.hasEnoughFound();i++){
		//Use the line below instead of the one above if we want just one level in a set of 10.
		//for(int i=0;i<allLevels.size() && !Alpha.hasEnoughFound() && !foundOne;i++){
			LinkedList<Integer> current=null;
			try{
			current=allLevels.get(i);
			}catch(Exception e){
				System.out.println("Caught an exception.");
			}
			int singleConnections=0;
			boolean valid=true;
			int singleOne=0;
			for(int j=0;current!=null && j<current.size() && valid ;j++ ){
				int connections=0;
				int analyzed=current.get(j);
				if(analyzed>nCols && current.contains(analyzed-nCols)){
					connections++;
				}
				if(analyzed%nCols!=0 && current.contains(analyzed+1)){
					connections++;
				}
				if(analyzed%nCols!=1 && current.contains(analyzed-1)){
					connections++;
				}
				if(analyzed<((nCols*(nRows-1))+1) && current.contains(analyzed+nCols)){
					connections++;
				}
				if(connections==1){
					singleConnections++;
					if(singleOne==0){
						singleOne=analyzed;
					}
				}else if(connections==0){
					valid=false;
				}
			}
			
			if(valid && singleConnections==1 ){
				boolean goodToAdd=true;
				if(singleOne!=0){
					goodToAdd=Checker.canBeComplexSolvedFrom2(singleOne, current, nRows, nCols);
				}
				if(goodToAdd){
					foundOne=true;
					validIndexes.add(i);
				}else{
					//System.out.println("Discarded an unsolvable level.");
				}
			}
		}
		//removeDisconnected();
		for(int k=0;k<validIndexes.size();k++){
			LinkedList<Integer> currL=allLevels.get(validIndexes.get(k));
			String level=nRows+"_"+nCols+":"+currL.get(0);
			for(int l=1;l<currL.size();l++){
				level=level+"_"+currL.get(l);
			}
			canBeSolved.add(level);
		}
		
		Alpha.addToFinalResult(canBeSolved);
		found+=canBeSolved.size();
		System.out.println("Thread "+myListIndex+": "+canBeSolved.size()+" levels containing single connection(s) added.");
		//Alpha.addToFinalResult(Checker.keepSimpleSymetric(primary,init,last,start));
		//Alpha.addToFinalResult(Checker.removeSimple(canBeSolved,init,last,start,myListIndex));
		notifyCompleted2();
		if(finalRun){
			allLevels.clear();
			System.out.println("ABC");
			Alpha.setCompleted();
		}else{
			allLevels.clear();
		}
	}
	
	/**Removes the levels made of two or more disconnected graphs. */
	private void removeDisconnected(){
	//	System.out.println("Removing the disconnected graphs.");
		long start= System.currentTimeMillis();
		LinkedList<Integer> validLevels=new LinkedList<Integer>();
		for(int k=0;k<validIndexes.size();k++){
			LinkedList<Integer> currL=allLevels.get(validIndexes.get(k));
			LinkedList<Integer> accessible=new LinkedList<Integer>();
			populate(currL.get(0),accessible,currL);
			if(checkIncluded(accessible,currL)){
				validLevels.add(validIndexes.get(k));
			};
		}
		long finish= System.currentTimeMillis();
	//	System.out.println("From "+validIndexes.size()+" levels to "+validLevels.size()+" levels in "+(finish-start)+" ms.");
		validIndexes=validLevels;
	}
	
	private boolean isConnected(LinkedList<Integer> input){
		LinkedList<Integer> accessible=new LinkedList<Integer>();
		populate(input.get(0),accessible,input);
		return checkIncluded(accessible,input);	
	}
	
	/**Adds the specified cell (focus) and tries to add its neighboring cells. */
	private void populate(int focus, LinkedList<Integer> reachable,LinkedList<Integer> mustReach){
		reachable.add(focus);
		
		if(focus>nCols && mustReach.contains(focus-nCols) && !reachable.contains(focus-nCols)){
			populate(focus-nCols,reachable,mustReach);
		}
		if(focus<((nCols*(nRows-1))+1) && mustReach.contains(focus+nCols) && !reachable.contains(focus+nCols)){
			populate(focus+nCols,reachable,mustReach);
		}
		if(focus%nCols!=0 && mustReach.contains(focus+1) && !reachable.contains(focus+1)){
			populate(focus+1,reachable,mustReach);
		}
		if(focus%nCols!=1 && mustReach.contains(focus-1) && !reachable.contains(focus-1)){
			populate(focus-1,reachable,mustReach);
		}
	}
	
	/**Returns true when the network built contains all the cells chosen for the level. */
	private boolean checkIncluded(LinkedList<Integer> network,LinkedList<Integer> mandate){
		boolean result=true;
		for(int i=0;i<mandate.size()&&result;i++){
			result=network.contains(mandate.get(i));
		}
		return result;
	}
	
	public static synchronized void notifyCompleted(){
		Alpha.increaseCompleted();
		activeThreads--;
		if(waitingList.size()!=0){
			waitingList.poll().start();
			activeThreads++;
		//	System.out.println("Active threads: "+activeThreads+" Waiting list size: "+waitingList.size());
		}
	}
	
	public static synchronized void notifyCompleted2(){
		
		//THIS IS WHERE THE NUMBER OF SOLUTIONS TO KEEP IS DETEMINED.
		if(found>=5000){
			Alpha.setCompleted();
		}
		Alpha.increaseCompleted();
		activeThreads--;
		launchAdditional2();
		//	System.out.println("Active threads: "+activeThreads+" Waiting list size: "+waitingList.size());
	}
	
	/*public static synchronized void notifyCompleted2(){
		if(found>=100){
			Alpha.setCompleted();
		}
		Alpha.increaseCompleted();
		activeThreads--;
		launchAdditional2();
		//	System.out.println("Active threads: "+activeThreads+" Waiting list size: "+waitingList.size());
	}*/
	
	public static synchronized void notifyVoidCompleted(){
		//Alpha.increaseCompleted();
		activeThreads--;
		if(waitingList.size()!=0){
			waitingList.poll().start();
			activeThreads++;
		//	System.out.println("Active threads: "+activeThreads+" Waiting list size: "+waitingList.size());
		}else{
			Alpha.setCompleted();
		}
	}
	
	public static synchronized void notifyVoidCompleted2(){
		//Alpha.increaseCompleted();
		activeThreads--;
		launchAdditional2();
	}
	
//	public static synchronized void requestLaunch(Approver approver){
//		if(activeThreads>=20){
//			waitingList.add(approver);
//		}else{
//			try{
//				approver.start();
//				activeThreads++;
//			}catch(Exception e){
//				activeThreads--;
//			}
//		}
//	}
	
	public static synchronized void prepareForLaunch(Approver approver){
		waitingList.add(approver);
	}
	
	public static void setTotalNumberOfThreads(int number){
		totalThreads=number;
	}
	
	public static synchronized void launchAdditional(){
		if(activeThreads>=20){
		   //Nothing to do. 
	}else{
		try{
			if(suggested.isEmpty() && !Alpha.hasEnoughFound() ){
				
			//	System.out.println("POSSIBLE STEP: "+step*2);
				if(step<(int)(totalThreads/2-1)){
					step=step*2;
					prepareIndexesForStep(step);
				}else{
					Alpha.setCompleted();
				}
				
			}else{
			if(!Alpha.hasEnoughFound()){
			//	System.out.println("Starting # "+suggested.peek());
				waitingList.get(suggested.pop()).start();
				activeThreads++;
			}else{
			//	System.out.println("Status was set to completed. No launch.");
			}
				
			}
		//	approver.start();
			
		}catch(Exception e){
			System.out.println("Approver: launchAdditional problem."+e);
			activeThreads--;
		}
	}
	}
	
	public static synchronized void launchAdditional2(){
		if(activeThreads>=25){
		   //Nothing to do. 
	}else{
		try{
			if(!waitingList.isEmpty() ){
				waitingList.pop().start();
				activeThreads++;
			}else{
			Alpha.setCompleted();
			}
		//	approver.start();
			
		}catch(Exception e){
			System.out.println("Approver: launchAdditional problem."+e);
			activeThreads--;
		}
	}
	}
	
	/**Determines the indexes of the threads that should be executed. */
	private static void prepareIndexesForStep(int step){
		suggested.clear();
		for(int i=1;i<step;i=i+2){
			suggested.add((int)Math.floor((totalThreads*i)/step));
		}
		launchAdditional();
	}
	
	public static void beginProcessing(){
		while(activeThreads<20){
			//Use method below when there are too many/ complicated levels to process and limit the 
			//number of results needed to 100.
			//launchAdditional();
			//Use method below to have all levels processed. 
			launchAdditional2();
		}
	}
	
	public static synchronized void printStatus(){
		if(found!=lastDisplayed){
			SimpleDateFormat sdf= new SimpleDateFormat(" HH:mm:ss");
			Date now=new Date();
			String strDate=sdf.format(now);
			lastDisplayed=found;
			System.out.println("Active threads: "+ activeThreads+"  Found so far: "+found+ (Alpha.hasEnoughFound()? " Enough" : " Not enough yet")+strDate);
		}else{
			
		}
		
	}
	
	/**Returns the isolated cell, the cell from which the search must start. */
	static int getStartingCell(LinkedList<Integer> level,int nRows,int nCols){
		
		for(int j=0;level!=null && j<level.size()  ;j++ ){
			int connections=0;
			int analyzed=level.get(j);
			if(analyzed>nCols && level.contains(analyzed-nCols)){
				connections++;
			}
			if(analyzed%nCols!=0 && level.contains(analyzed+1)){
				connections++;
			}
			if(analyzed%nCols!=1 && level.contains(analyzed-1)){
				connections++;
			}
			if(analyzed<((nCols*(nRows-1))+1) && level.contains(analyzed+nCols)){
				connections++;
			}
			if(connections==1){
				return analyzed;
			}
		}
		return 1;
	}
}
