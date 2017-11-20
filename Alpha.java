import java.util.LinkedList;
public class Alpha {
	
	private static int nCols;
	private static int nRows;
	public static LinkedList<LinkedList<Integer>> setOfCellsToExclude;
	public static LinkedList<LinkedList<Integer>> setOfLevels;
	public static LinkedList<String> levelsThatCanBeSolved;
	public static LinkedList<Integer> indexesOfValidLevels;
	public static LinkedList<String> result;
	//New version of setOfCellsToExclude.
	//Contains linkedlist containing each 10000 levels that will be processed by different threads.
	private static LinkedList<LinkedList<LinkedList<Integer>>> boss;
	//Number of the new configuration, level.
	private static int newLevelNumber=0;
	public static int completedRuns;
	private static int maxCell;
	private static boolean done;
	//See launchThreads method.
	private static int threadsLaunched;
	private static int thousandThreads;
	private static int launchedThreads=0;
	private static int completedThreads=0;
	public static LinkedList<LinkedList<Integer>>[] newBoss;
	
	/**Creates all the different cell combinations for  
	 * @throws InterruptedException */
	@SuppressWarnings("unchecked")
	public static void generateConfigs(int cols, int rows, int nmbCellsToExclude,String difficulty) throws InterruptedException{

		launchedThreads=0;
		completedThreads=0;
		long start=System.currentTimeMillis();
		Approver.reset();
		indexesOfValidLevels=new LinkedList<Integer>();
		setOfLevels= new LinkedList<LinkedList<Integer>>();
		setOfCellsToExclude= new LinkedList<LinkedList<Integer>>();
		levelsThatCanBeSolved= new LinkedList<String>();
		boss=new LinkedList<LinkedList<LinkedList<Integer>>>();
		result= new LinkedList<String>();
		done=false;
		System.out.println("Generating different sets of cells.");
		nCols=cols;
		nRows=rows;
		maxCell=nRows*nCols;
		//newBoss= (LinkedList<LinkedList<Integer>>[]) new LinkedList[(int)Math.ceil(total/10000)];
		done=false;
		for(int i=0;i<nRows*nCols;i++){
			//Exclude the first cell and request that the appropriate amount of cells get removed. 
			LinkedList<Integer> temp=new LinkedList<Integer>();
			temp.add(i+1);
			exclude2(temp,nmbCellsToExclude-1,difficulty);
		}
		long finish=System.currentTimeMillis();
		System.out.println("Generated the configs in "+(finish-start)+" ms: "+
		(boss.getLast().size()+((boss.size()-1)*10))+" levels.");
		Thread.sleep(2000);
		Approver.setTotalNumberOfThreads(boss.size());
		Approver.beginProcessing();
	//	Approver last=new Approver(boss.getLast(),0,boss.getLast().size()-1,nCols,nRows,true,(int)Math.floor(newLevelNumber/10),difficulty);
	//	last.start();
		//launchStableThreads2(); // Config 1
		//fastCheckCalidConfigs();// Config 2
		//result=Checker.removeSimple(levelsThatCanBeSolved);// Config 2
	}
	
	public static void generateSymetricConfigs(int cols, int rows, int nmbCellsToExclude) throws InterruptedException{

		launchedThreads=0;
		completedThreads=0;
		
		long start=System.currentTimeMillis();
		Selector.reset();
		indexesOfValidLevels=new LinkedList<Integer>();
		setOfLevels= new LinkedList<LinkedList<Integer>>();
		setOfCellsToExclude= new LinkedList<LinkedList<Integer>>();
		levelsThatCanBeSolved= new LinkedList<String>();
		boss=new LinkedList<LinkedList<LinkedList<Integer>>>();
		result= new LinkedList<String>();
		done=false;
		System.out.println("Generating different sets of cells.");
		nCols=cols;
		nRows=rows;
		maxCell=nRows*nCols;
		//newBoss= (LinkedList<LinkedList<Integer>>[]) new LinkedList[(int)Math.ceil(total/10000)];
		done=false;
		for(int i=0;i<nRows*nCols;i++){
			//Exclude the first cell and request that the appropriate amount of cells get removed. 
			LinkedList<Integer> temp=new LinkedList<Integer>();
			temp.add(i+1);
			exclude3(temp,nmbCellsToExclude-1);
		}
		long finish=System.currentTimeMillis();
		System.out.println("Generated the configs in "+(finish-start)+" ms: "+
		(boss.getLast().size()+((boss.size()-1)*10000))+" levels.");
		Thread.sleep(2000);
		Selector last=new Selector(boss.getLast(),0,boss.getLast().size()-1,nCols,nRows,true,(int)Math.floor(newLevelNumber/10000));
		last.start();
	}
	
	public static void generateRandomConfigs(int cols, int rows, int nmbCellsToExclude) throws InterruptedException{

		launchedThreads=0;
		completedThreads=0;
		
		long start=System.currentTimeMillis();
		Randomer.reset();
		indexesOfValidLevels=new LinkedList<Integer>();
		setOfLevels= new LinkedList<LinkedList<Integer>>();
		setOfCellsToExclude= new LinkedList<LinkedList<Integer>>();
		levelsThatCanBeSolved= new LinkedList<String>();
		boss=new LinkedList<LinkedList<LinkedList<Integer>>>();
		result= new LinkedList<String>();
		done=false;
		System.out.println("Generating different sets of cells.");
		nCols=cols;
		nRows=rows;
		maxCell=nRows*nCols;
		//newBoss= (LinkedList<LinkedList<Integer>>[]) new LinkedList[(int)Math.ceil(total/10000)];
		done=false;
		for(int i=0;i<nRows*nCols;i++){
			//Exclude the first cell and request that the appropriate amount of cells get removed. 
			LinkedList<Integer> temp=new LinkedList<Integer>();
			temp.add(i+1);
			exclude4(temp,nmbCellsToExclude-1);
		}
		long finish=System.currentTimeMillis();
		System.out.println("Generated the configs in "+(finish-start)+" ms: "+
		(boss.getLast().size()+((boss.size()-1)*10000))+" levels.");
		Thread.sleep(2000);
		Randomer last=new Randomer(boss.getLast(),0,boss.getLast().size()-1,nCols,nRows,true,(int)Math.floor(newLevelNumber/10000));
		last.start();
	}
	/**Creates the cell to include in a level based on the cells that are excluded
	 * from that level.  */
	private static void convertToCells(){
		long start =System.currentTimeMillis();
		System.out.println("Converting the cells to exclude to cells to include.");
		for(int i=0;i<setOfCellsToExclude.size();i++){
			LinkedList<Integer> curr=setOfCellsToExclude.get(i);
			LinkedList<Integer> level=new LinkedList<Integer>();
			for(int j=1;j<maxCell+1;j++){
				if(!curr.contains(j)){
					level.add(j);
				}
			}
			setOfLevels.add(level);
		}
		long finish=System.currentTimeMillis();
		System.out.println("Completed in "+(finish-start)+" ms.");
	}
	
	
	
	/**Removes the levels that can't be solved. */
	private static void fastCheckCalidConfigs(){
		System.out.println("Deleting the levels that can't be solved.");
		long start=System.currentTimeMillis();		
		//This is temporary.
		setOfLevels=setOfCellsToExclude;
		for(int i=0;i<setOfLevels.size();i++){
			LinkedList<Integer> current=setOfLevels.get(i);
			int oddVertices=0;
			boolean valid=true;
			for(int j=0;j<current.size();j++){
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
				if((connections%2)!=0){
					oddVertices++;
				}
				if(connections==0){
					valid=false;
				}
			}
			
			if((oddVertices==0|| oddVertices==2) && valid){
				indexesOfValidLevels.add(i);
			}
			System.out.println("Checked "+i);
		}
		
		//Call the disconnected graphs remover here, before converting the levels to their String format.
		removeDisconnected();
		
		for(int k=0;k<indexesOfValidLevels.size();k++){
			LinkedList<Integer> currL=setOfLevels.get(indexesOfValidLevels.get(k));
			String level=nRows+"_"+nCols+":"+currL.get(0);
			for(int l=1;l<currL.size();l++){
				level=level+"_"+currL.get(l);
			}
			levelsThatCanBeSolved.add(level);
		}
		
		long finish=System.currentTimeMillis();
		System.out.println("Went from "+setOfLevels.size()+" levels to "+levelsThatCanBeSolved.size()+" valid levels in "+(finish-start)+" ms.");
		
	}
	
	/**Returns true if it is possible to solve this level by building on the current path. */
	private static boolean solveIfNotSolved(String base,LinkedList<Integer> cells, int lastAdded){
		boolean solved=true;
		for(int i=0;i<cells.size()&&solved;i++){
			if(!base.contains("_"+cells.get(i)+"_")){
				solved=false;
			}
		}
		//Try adding top
		if(!solved && lastAdded>nCols && cells.contains(lastAdded-nCols) && !base.contains("_"+(lastAdded-nCols)+"_"+lastAdded+"_") && !base.contains("_"+lastAdded+"_"+(lastAdded-nCols)+"_")){
			solved=solveIfNotSolved(base+(lastAdded-nCols)+"_",cells,(lastAdded-nCols));
		}
		//Try adding right
		if(!solved && lastAdded%nCols!=0 && cells.contains(lastAdded+1) && !base.contains("_"+(lastAdded+1)+"_"+lastAdded+"_") && !base.contains("_"+lastAdded+"_"+(lastAdded+1)+"_")){
			solved=solveIfNotSolved(base+(lastAdded+1)+"_",cells,(lastAdded+1));
		}
		//Try adding bottom
		if(!solved && lastAdded<((nCols*(nRows-1))+1) && cells.contains(lastAdded+nCols) && !base.contains("_"+(lastAdded+nCols)+"_"+lastAdded+"_") && !base.contains("_"+lastAdded+"_"+(lastAdded+nCols)+"_")){
			solved=solveIfNotSolved(base+(lastAdded+nCols)+"_",cells,(lastAdded+nCols));
		}
		//Try adding left
		if(!solved && lastAdded%nCols!=1 && cells.contains(lastAdded-1) && !base.contains("_"+(lastAdded-1)+"_"+lastAdded+"_") && !base.contains("_"+lastAdded+"_"+(lastAdded-1)+"_")){
			solved=solveIfNotSolved(base+(lastAdded-1)+"_",cells,(lastAdded-1));
		}
		return solved;
	}
	
	/**Exclude @param left cells from the grid. */
	private static void exclude(LinkedList<Integer> excludedSoFar, int left){
		//If there is only one cell to remove, remove if possible and add to the static container.
		int last=excludedSoFar.getLast();
		int diff=maxCell-last;
		for(int i=0;i<diff;i++){
			@SuppressWarnings("unchecked")
			LinkedList<Integer> temp=(LinkedList<Integer>)excludedSoFar.clone();
			temp.add(last+i+1);
			if(left>1){
				exclude(temp,left-1);
			}else{
				//left==1 and we excluded that last cell 4 rows above.
	
				if(newLevelNumber%10000==0){
					//A new container of 10000 levels has to be generated.
					boss.add(new LinkedList<LinkedList<Integer>>());
					boss.getLast().add(temp);
				}else{
					boss.getLast().add(temp);
				}
				newLevelNumber++;
				//setOfCellsToExclude.add(temp);
			}
		}
	}
	
	/**Exclude @param left cells from the grid.
	 * Used to retain levels that have at least one forced crossing. */
	private static void exclude2(LinkedList<Integer> excludedSoFar, int left,String d){
		//If there is only one cell to remove, remove if possible and add to the static container.
		int last=excludedSoFar.getLast();
		//Number of cells available (that can be added to the level)
		int diff=maxCell-last;
		
		//Modify and make sure that there are enough cells remaining even before starting 
		//to loop through the remaining cells.
		for(int i=0;i<diff;i++){
			@SuppressWarnings("unchecked")
			LinkedList<Integer> temp=(LinkedList<Integer>)excludedSoFar.clone();
			temp.add(last+i+1);
			if(left>1){
				if((left-1)<=maxCell-(last+i+1)){
					exclude2(temp,left-1,d);
				}
			}else{
				//left==1 and we excluded that last cell 4 rows above.
				
				if(newLevelNumber%10==0){
					//A new container of 10000 levels has to be generated.
					if(boss.size()!=0){
						//Launch thread to process current last container.
						Approver current=new Approver(boss.getLast(),0,9,nCols,nRows,false,newLevelNumber/10,d);
					//	Approver.requestLaunch(current);
						
						Approver.prepareForLaunch(current);
					}
					boss.add(new LinkedList<LinkedList<Integer>>());
					boss.getLast().add(temp);
				}else{
					boss.getLast().add(temp);
				}
				newLevelNumber++;
				//setOfCellsToExclude.add(temp);
			}
		}
	}
	
	/**Selects levels without crossings that are symetric. */
	private static void exclude3(LinkedList<Integer> excludedSoFar, int left){
		//If there is only one cell to remove, remove if possible and add to the static container.
		int last=excludedSoFar.getLast();
		//Number of cells available (that can be added to the level)
		int diff=maxCell-last;
		
		//Modify and make sure that there are enough cells remaining even before starting 
		//to loop through the remaining cells.
		for(int i=0;i<diff;i++){
			@SuppressWarnings("unchecked")
			LinkedList<Integer> temp=(LinkedList<Integer>)excludedSoFar.clone();
			temp.add(last+i+1);
			if(left>1){
				if((left-1)<=maxCell-(last+i+1)){
					exclude3(temp,left-1);
				}
			}else{
				//left==1 and we excluded that last cell 4 rows above.
				
				if(newLevelNumber%10000==0){
					//A new container of 10000 levels has to be generated.
					if(boss.size()!=0){
						//Launch thread to process current last container.
						Selector current=new Selector(boss.getLast(),0,9999,nCols,nRows,false,newLevelNumber/10000);
						Selector.requestLaunch(current);
						//current.start();
						//System.out.println("Requested Launch of thread thread #"+ newLevelNumber/10000);
					}
					boss.add(new LinkedList<LinkedList<Integer>>());
					boss.getLast().add(temp);
				}else{
					boss.getLast().add(temp);
				}
				newLevelNumber++;
				//setOfCellsToExclude.add(temp);
			}
		}
	}
	
	/** Randomly selects simple levels. */
	private static void exclude4(LinkedList<Integer> excludedSoFar, int left){
		//If there is only one cell to remove, remove if possible and add to the static container.
		int last=excludedSoFar.getLast();
		//Number of cells available (that can be added to the level)
		int diff=maxCell-last;
		
		//Modify and make sure that there are enough cells remaining even before starting 
		//to loop through the remaining cells.
		for(int i=0;i<diff;i++){
			@SuppressWarnings("unchecked")
			LinkedList<Integer> temp=(LinkedList<Integer>)excludedSoFar.clone();
			temp.add(last+i+1);
			if(left>1){
				if((left-1)<=maxCell-(last+i+1)){
					exclude4(temp,left-1);
				}
			}else{
				//left==1 and we excluded that last cell 4 rows above.
				
				if(newLevelNumber%10000==0){
					//A new container of 10000 levels has to be generated.
					if(boss.size()!=0){
						//Launch thread to process current last container.
						Randomer current=new Randomer(boss.getLast(),0,9999,nCols,nRows,false,newLevelNumber/10000);
						Randomer.requestLaunch(current);
						//current.start();
						//System.out.println("Requested Launch of thread thread #"+ newLevelNumber/10000);
					}
					boss.add(new LinkedList<LinkedList<Integer>>());
					boss.getLast().add(temp);
				}else{
					boss.getLast().add(temp);
				}
				newLevelNumber++;
				//setOfCellsToExclude.add(temp);
			}
		}
	}
	
	/**Returns the levels produced. */
	public static LinkedList<String> getProduct(){
		return result;
	}
	
	/**Removes the levels made of two or more disconnected graphs. */
	private static void removeDisconnected(){
		System.out.println("Removing the disconnected graphs.");
		long start= System.currentTimeMillis();
		LinkedList<Integer> validLevels=new LinkedList<Integer>();
		for(int k=0;k<indexesOfValidLevels.size();k++){
			LinkedList<Integer> currL=setOfLevels.get(indexesOfValidLevels.get(k));
			LinkedList<Integer> accessible=new LinkedList<Integer>();
			populate(currL.get(0),accessible,currL);
			if(checkIncluded(accessible,currL)){
				validLevels.add(indexesOfValidLevels.get(k));
			};
		}
		long finish= System.currentTimeMillis();
		System.out.println("From "+indexesOfValidLevels.size()+" levels to "+validLevels.size()+" levels in "+(finish-start)+" ms.");
		indexesOfValidLevels=validLevels;
	}
	
	/**Adds the specified cell (focus) and tries to add its neighboring cells. */
	private static void populate(int focus, LinkedList<Integer> reachable,LinkedList<Integer> mustReach){
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
	private static boolean checkIncluded(LinkedList<Integer> network,LinkedList<Integer> mandate){
		boolean result=true;
		for(int i=0;i<mandate.size()&&result;i++){
			result=network.contains(mandate.get(i));
		}
		return result;
	}
	
	public static synchronized void addToFinalResult(LinkedList<String> input){
		result.addAll(input);
	}
	
	public static void setCompleted(){
		if(!done){
			done =true;
			System.out.println("Alpha: Status just got set to completed.");
		}
	}
	
	public static synchronized boolean getCompleted(){
		return done && (launchedThreads==completedThreads);
	}
	
	public static synchronized boolean hasEnoughFound(){
		return done;
	}
	
	private static void launchThreads(){
		//Number of threads of 10000 configurations.
		int i=(int)Math.floor(setOfCellsToExclude.size()/10000);
		int lastIndex=0;
		completedRuns=0;

		for(int b=0;b<i;b++){
	//		Approver current=new Approver(setOfCellsToExclude,b*10000,(b*10000)+9999,nCols,nRows,false);
	//		current.start();
		}
		while(completedRuns<i){
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	//	Approver last=new Approver(setOfCellsToExclude,10000*i,setOfCellsToExclude.size()-1,nCols,nRows,true);
	//	last.run();
		
	}
	/*
	private static void launchStableThreads(){
		//Number of threads of 10000 configurations.
		thousandThreads=(int)Math.floor(setOfCellsToExclude.size()/10000);
		int lastIndex=0;
		completedRuns=0;
		b=0;
		while(b<thousandThreads &&b<10){
			Approver current=new Approver(setOfCellsToExclude,b*10000,(b*10000)+9999,nCols,nRows,false);
			current.start();
			b++;
		}
	}
	
	public static void launchAdditionalThread(){
		completedRuns++;
		if(completedRuns<thousandThreads){
			Approver current=new Approver(setOfCellsToExclude,b*10000,(b*10000)+9999,nCols,nRows,false);
			current.start();
			b++;
		}else{
			Approver last=new Approver(setOfCellsToExclude,10000*thousandThreads,setOfCellsToExclude.size()-1,nCols,nRows,true);
			last.run();
		}
	}
	*/
	
	private static void launchStableThreads2(){
		//Number of threads of 10000 configurations.
		int total=(boss.getLast().size()+((boss.size()-1)*10000));
		thousandThreads=(int)Math.floor(total/10000);
		completedRuns=0;
		threadsLaunched=0;
		if(thousandThreads!=0){
			while(threadsLaunched<thousandThreads &&threadsLaunched<10){
			//	Approver current=new Approver(boss.get(threadsLaunched),0,9999,nCols,nRows,false);
			//	current.start();
				threadsLaunched++;
			}
		}else{
			//Approver last=new Approver(boss.getLast(),0,boss.getLast().size()-1,nCols,nRows,true);
		//	last.run();
		}
		
	}
	
	public static synchronized void launchAdditionalThread2(){
		completedRuns++;
		if(threadsLaunched<thousandThreads){
		//	Approver current=new Approver(boss.get(threadsLaunched),0,9999,nCols,nRows,false);
		//	current.start();
			threadsLaunched++;
			//System.out.println("Completed runs: "+completedRuns+" Launched thread #: "+(threadsLaunched));
		}else{
		//	Approver last=new Approver(boss.getLast(),0,boss.getLast().size()-2,nCols,nRows,true);
		//	last.run();
		}
	}
	
	public static synchronized void increaseLaunch(){
		launchedThreads++;
	}
	
	public static synchronized void increaseCompleted(){
		completedThreads++;
	}
	
	public static synchronized void freeContainer(int i){
		boss.get(i).clear();
	}
	public static int factorial(int n) {
        int fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }

}
