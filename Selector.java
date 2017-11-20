import java.util.LinkedList;

public class Selector extends Thread{
	/*Selects all simple symmetric levels from a set of 10000.
	 * Simple levels have no crossings. */
	
	private static int activeThreads=0;
	private static LinkedList<Selector> waitingList;
	private LinkedList<LinkedList<Integer>> allLevels;
	private LinkedList<Integer> validIndexes;
	private LinkedList<String> canBeSolved;
	//true when this approver is the last one. 
	boolean finalRun;
	int init;
	int last;
	int nCols;
	int nRows;
	int myListIndex;
	
	/**Checks the validity of levels at indexes initialIndex through lastIndex. */
	Selector(LinkedList<LinkedList<Integer>> levels, int initialIndex, int lastIndex,int cols,int rows, boolean lastRun, int myLI){
		allLevels=levels;
		init=initialIndex;
		last=lastIndex;
		validIndexes=new LinkedList<Integer>();
		canBeSolved=new LinkedList<String>();
		nCols=cols;
		nRows=rows;
		finalRun=lastRun;
		myListIndex=myLI;
	}
	
	public static void reset(){
		waitingList=new LinkedList<Selector>();
		activeThreads=0;
	}
	
	public void run(){
		long start= System.currentTimeMillis();
	//	for(int i=init;i<=last;i++){
		for(int i=0;i<allLevels.size();i++){
			LinkedList<Integer> current=null;
			try{
			current=allLevels.get(i);
			}catch(Exception e){
				System.out.println("Caught an exception.");
			}
			
			int singleConnections=0;
			boolean valid=true;
			for(int j=0;current!=null && j<current.size();j++){
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
			if(singleConnections<=2 && valid){
				validIndexes.add(i);
			}
			
		}
		removeDisconnected();
		for(int k=0;k<validIndexes.size();k++){
			LinkedList<Integer> currL=allLevels.get(validIndexes.get(k));
			String level=nRows+"_"+nCols+":"+currL.get(0);
			for(int l=1;l<currL.size();l++){
				level=level+"_"+currL.get(l);
			}
			canBeSolved.add(level);
		}
		
		long finish=System.currentTimeMillis();
	//	System.out.println(init+" through "+last+" produced "+canBeSolved.size()+" valid levels in "+(finish-start)+" ms.");
		LinkedList<String> valid=Checker.keepSimpleSymetric(canBeSolved,init,last,start);
		Alpha.addToFinalResult(valid);
		System.out.println("Thread "+myListIndex+": "+valid.size()+" simple symetric levels in "+(finish-start)+" ms.");
		notifyCompleted();
		if(finalRun){
			Alpha.setCompleted();
		}else{
			//Alpha.launchAdditionalThread2();
			//Alpha.freeContainer(myListIndex);
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
		activeThreads--;
		if(waitingList.size()!=0){
			waitingList.poll().start();
			activeThreads++;
		//	System.out.println("Active threads: "+activeThreads+" Waiting list size: "+waitingList.size());
		}
	}
	
	public static synchronized void requestLaunch(Selector selector){
		if(activeThreads>=20){
			waitingList.add(selector);
		}else{
			try{
				selector.start();
				activeThreads++;
			}catch(Exception e){
				activeThreads--;
			}
			
		}
	}

}
