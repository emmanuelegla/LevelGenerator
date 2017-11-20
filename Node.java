import java.util.Iterator;
import java.util.LinkedList;

public class Node{
	
	/** Number of solutions currently added to the linked list containing the solutions.*/
	public static int numberOfLevelsAdded;
	
	/*Maximum number of levels to add to the list containing the solutions.   */
	public static int numberOfLevelsRequested=1000;
	
	/**The levels are generated on a grid 40x40 whose first cell is identified by the number 1. */
	
	/*Number of rows on the grid currently used to generate the levels. */
	private static int rows;
	
	/*Number of columns on the grid currently used to generate the levels. */
	private static int columns;
	
	/*Used to differentiate the 4 types  of movements allowed in the grid.  */
	public enum Direction{UP,DOWN,RIGHT,LEFT};
			
		public String path;
		public int currentCell;
		public Node one;
		public Node two;
		public Node three;
		public Node(){
			one=null;
			two=null;
			three=null;
			path=null;
		}
		
		public void setPath(String myPath){
			path=myPath;
		}
			/**Contains all the levels generated. */
		private static LinkedList<String> levelsGenerated;
		
		private static LinkedList<String> uniqueLevelsGenerated;
		
		/**Contains all the levels generated. */
		private static LinkedList<String> levelsRotated;
		
		/**Contains all the levels generated. */
		private static LinkedList<String> results;
		
		public static void generateLevels(int nmbCellsNeeded, Direction direction,int initialCell, int rows_number, int columns_number){
			//FmyFrame.numbCols=columns;
			//FmyFrame.numbRows=rows;
			
			rows=rows_number;
			columns=columns_number;
			String initialPath=new String(Integer.toString(initialCell));
			Node root=new Node();
			root.setPath(initialPath);
			clearLists();
			System.out.println("Creating levels containing "+nmbCellsNeeded+" cells in a "+ rows_number+"x"+columns_number+" grid starting at cell " +initialCell);
		//	root.generate(initialPath, nmbCellsNeeded-1, Node.Direction.RIGHT, initialCell);
			root.generate2(initialPath, nmbCellsNeeded-1, Node.Direction.RIGHT, initialCell);
		}
		
		public static void generateFrom(int nmbCellsNeeded, Direction direction, String base, int rows_number, int columns_number){
			rows=rows_number;
			columns=columns_number;
			Node root=new Node();
			root.setPath(base);
			clearLists();
			System.out.println("Creating levels containing "+nmbCellsNeeded+" cells in a "+ rows_number+"x"+columns_number+" grid starting from path " +base);
			//Change the initial cell variable to the cell number that we will try to reach.
			//root.generate2(base, nmbCellsNeeded-1, Node.Direction.RIGHT, initialCell);
		}
		
		public void generate(String currentPath,int remainingCells,Direction moveRequested,int lastCellVisited){
			int newCell=0;
			/*Indicates that the move requested can't be performed when true. */
			boolean flag=false;
			
			switch(moveRequested){
			
			//Make sure that the move is graphically possible.
			case UP : if(lastCellVisited<=columns){
				flag=true;
			}else{
				newCell=(int)(lastCellVisited-columns);
			}
			break;
			
			case DOWN:
				if(lastCellVisited>=((columns*(rows-1))+1)){
					flag=true;
				}else{
					newCell=(int)(lastCellVisited +columns);
				}
				break;
				
			case RIGHT: if(lastCellVisited%columns==0){
				flag=true;
				}else{
					newCell=(int)(lastCellVisited +1);
				}
				break;
	
			case LEFT: 
				if(lastCellVisited%columns==1){
					flag=true;
				}else{
					newCell=(int)lastCellVisited-1;
				}
				break;
			}
			if(!flag){
				
				//Make sure that there is no connection between the new last two cells already in the link
				String s1=lastCellVisited+"_"+newCell;
				String s2=newCell+"_"+lastCellVisited;
				
				if(currentPath.contains(s1)|| currentPath.contains(s2)){
					flag=true;
				}
			}
			
			if(!flag){
				//flag= currentPath.startsWith(newCell+"_")||currentPath.contains("_"+newCell+"_") ;
				//Flag is true means that the cell at the position specified can't be added.
			}
			
			if(!flag){
				path=currentPath+"_"+newCell;
				
				if(remainingCells>1 ){
					one=new Node();
					two=new Node();
					three=new Node();
					String s3=""+newCell;
					
					if(moveRequested==Direction.UP){
						if(currentPath.contains(s3)){
							if( numberOfLevelsAdded<numberOfLevelsRequested){
								one.generate(path, remainingCells, Direction.LEFT, newCell);
								two.generate(path, remainingCells, Direction.UP , newCell);
								three.generate(path, remainingCells, Direction.RIGHT, newCell);
							}	
						}else{
							if( numberOfLevelsAdded<numberOfLevelsRequested){
								one.generate(path, remainingCells-1, Direction.LEFT, newCell);
								two.generate(path, remainingCells-1, Direction.UP , newCell);
								three.generate(path, remainingCells-1, Direction.RIGHT, newCell);
							}
						}	
					}
					else if(moveRequested==Direction.RIGHT){
						if(currentPath.contains(s3)){
							if( numberOfLevelsAdded<numberOfLevelsRequested){
								one.generate(path, remainingCells, Direction.UP, newCell);
								two.generate(path, remainingCells, Direction.RIGHT, newCell);
								three.generate(path, remainingCells, Direction.DOWN, newCell);
							}
						}else{
							if( numberOfLevelsAdded<numberOfLevelsRequested){
								one.generate(path, remainingCells-1, Direction.UP, newCell);
								two.generate(path, remainingCells-1, Direction.RIGHT, newCell);
								three.generate(path, remainingCells-1, Direction.DOWN, newCell);
							}
						}
					}
					else if(moveRequested==Direction.DOWN){
						if(currentPath.contains(s3)){
							if( numberOfLevelsAdded<numberOfLevelsRequested){
								one.generate(path, remainingCells, Direction.RIGHT, newCell);
								two.generate(path, remainingCells, Direction.DOWN, newCell);
								three.generate(path, remainingCells, Direction.LEFT, newCell);
							}	
						}else{
							if( numberOfLevelsAdded<numberOfLevelsRequested){
								one.generate(path, remainingCells-1, Direction.RIGHT, newCell);
								two.generate(path, remainingCells-1, Direction.DOWN, newCell);
								three.generate(path, remainingCells-1, Direction.LEFT, newCell);
							}
						}
					}
					else{
						if(currentPath.contains(s3)){
							if( numberOfLevelsAdded<numberOfLevelsRequested){
								one.generate(path, remainingCells, Direction.DOWN, newCell);
								two.generate(path, remainingCells, Direction.LEFT, newCell);
								three.generate(path, remainingCells, Direction.UP, newCell);
							}	
						}else{
							
							if( numberOfLevelsAdded<numberOfLevelsRequested){
								one.generate(path, remainingCells-1, Direction.DOWN, newCell);
								two.generate(path, remainingCells-1, Direction.LEFT, newCell);
								three.generate(path, remainingCells-1, Direction.UP, newCell);
							}
						}
					}
					//one.generateLevels(path, remainingCells-1, moveRequested, lastCellVisited);
				}else{
					levelsGenerated.add(path);
					//System.out.println("Added "+path);
					numberOfLevelsAdded++;
				}
			}else{	
			}
		}
		
		
		
		public void generate2(String currentPath,int remainingCells,Direction moveRequested,int lastCellVisited){
			
			//Condition to limit the number of levels to process to 100000
			if(levelsGenerated.size()<100000){
				int newCell=0;
				/*Indicates that the move requested can't be performed when true. */
				boolean flag=false;
				
				switch(moveRequested){
				
				//Make sure that the move is graphically possible.
				case UP : if(lastCellVisited<=columns){
					flag=true;
				}else{
					newCell=(int)(lastCellVisited-columns);
				}
				break;
				
				case DOWN:
					if(lastCellVisited>=((columns*(rows-1))+1)){
						flag=true;
					}else{
						newCell=(int)(lastCellVisited +columns);
					}
					break;
					
				case RIGHT: if(lastCellVisited%columns==0){
					flag=true;
					}else{
						newCell=(int)(lastCellVisited +1);
					}
					break;
		
				case LEFT: 
					if(lastCellVisited%columns==1){
						flag=true;
					}else{
						newCell=(int)lastCellVisited-1;
					}
					break;
				}
				if(!flag){
					
					//Make sure that there is no connection between the new last two cells already in the link
					String s1=lastCellVisited+"_"+newCell;
					String s2=newCell+"_"+lastCellVisited;
					
					if(currentPath.contains(s1)|| currentPath.contains(s2)){
						flag=true;
					}
				}
				
				boolean notANewCell=currentPath.startsWith(newCell+"_")||currentPath.contains("_"+newCell+"_") ;
				
				if(!flag){
					path=currentPath+"_"+newCell;
					
					if(remainingCells>1 ){
						//We must add another cell, because there was 2 or more cells remaining.
						one=new Node();
						two=new Node();
						three=new Node();
						if(moveRequested==Direction.UP){
							if(notANewCell){
									one.generate2(path, remainingCells, Direction.LEFT, newCell);
									two.generate2(path, remainingCells, Direction.UP , newCell);
									three.generate2(path, remainingCells, Direction.RIGHT, newCell);
							}else{
									one.generate2(path, remainingCells-1, Direction.LEFT, newCell);
									two.generate2(path, remainingCells-1, Direction.UP , newCell);
									three.generate2(path, remainingCells-1, Direction.RIGHT, newCell);
							}	
						}
						else if(moveRequested==Direction.RIGHT){
							if(notANewCell){
									one.generate2(path, remainingCells, Direction.UP, newCell);
									two.generate2(path, remainingCells, Direction.RIGHT, newCell);
									three.generate2(path, remainingCells, Direction.DOWN, newCell);
							}else{
									one.generate2(path, remainingCells-1, Direction.UP, newCell);
									two.generate2(path, remainingCells-1, Direction.RIGHT, newCell);
									three.generate2(path, remainingCells-1, Direction.DOWN, newCell);
							}
						}
						else if(moveRequested==Direction.DOWN){
							if(notANewCell){
									one.generate2(path, remainingCells, Direction.RIGHT, newCell);
									two.generate2(path, remainingCells, Direction.DOWN, newCell);
									three.generate2(path, remainingCells, Direction.LEFT, newCell);
							}else{
									one.generate2(path, remainingCells-1, Direction.RIGHT, newCell);
									two.generate2(path, remainingCells-1, Direction.DOWN, newCell);
									three.generate2(path, remainingCells-1, Direction.LEFT, newCell);
							}
						}
						else{
							if(notANewCell){
									one.generate2(path, remainingCells, Direction.DOWN, newCell);
									two.generate2(path, remainingCells, Direction.LEFT, newCell);
									three.generate2(path, remainingCells, Direction.UP, newCell);
									
							}else{
									one.generate2(path, remainingCells-1, Direction.DOWN, newCell);
									two.generate2(path, remainingCells-1, Direction.LEFT, newCell);
									three.generate2(path, remainingCells-1, Direction.UP, newCell);
							}
						}
						//one.generateLevels(path, remainingCells-1, moveRequested, lastCellVisited);
					}else{
						levelsGenerated.add(path);
						//System.out.println("Added "+path);
						//numberOfLevelsAdded++;
					}
				}else{	
					//Nothing to do, impossible to add a node to the current path.
				}
				
			}
			else{
				//Nothing to do, we already have 100000 levels to process.
			}
	
		}
		
		/**Initializes the list used to store the levels generated. */
		public static void clearLists(){
			levelsGenerated=new LinkedList<String>();
			levelsRotated=new LinkedList<String>();
			uniqueLevelsGenerated=new LinkedList<String>();
			results=new LinkedList<String>();
			numberOfLevelsAdded=0;
		}
		
		/**Returns the levels created. */
		public static LinkedList<String> getLevelsGenerated(){
			deleteOversizedLevels();
			
			return levelsRotated;
		}
		
		/**Returns the levels created. */
		public static LinkedList<String> getLevelsGenerated2(){			 
			 System.out.println("Deleting identical levels.");
			long a=System.currentTimeMillis();
			uniqueLevelsGenerated.add(levelsGenerated.get(0));
			for(int i=1;i<levelsGenerated.size();i++){
				boolean hasIdentical=false;
				String[] currLevel=levelsGenerated.get(i).split("_");
				for(int j=0;j<uniqueLevelsGenerated.size()&&!hasIdentical;j++){
					boolean identical=true;
					String [] comparedTo=uniqueLevelsGenerated.get(j).split("_");
					//Now it's time to compare. 
					for (int k=0;k<currLevel.length&&identical;k++){
						boolean present=false;
						String beingChecked=currLevel[k];
						for(int l=0;l<comparedTo.length&&!present;l++){
							if(beingChecked.equals(comparedTo[l])){
								present=true;
							}
						}
						if(!present){
							identical=false;
						}
					}
					if(identical){
						hasIdentical=true;
					}
				}
				if(!hasIdentical){
					uniqueLevelsGenerated.add(levelsGenerated.get(i));
					//System.out.println("Added path "+uniqueLevelsGenerated.getLast());
				}
			}
			long b=System.currentTimeMillis();
			System.out.println("Deletion took "+(b-a)+" milliseconds.");
			System.out.println("Went from "+levelsGenerated.size()+" to "+uniqueLevelsGenerated.size()+" levels");
		
			System.out.println("Transfering the final results.");
			for(int s=0;s<uniqueLevelsGenerated.size();s++){
				results.add(Integer.toString(rows)+"_"+Integer.toString(columns)+":"+uniqueLevelsGenerated.get(s));
			}
			return results;

		}
		
		public static LinkedList<String> getLevelsGenerated3(){
			//LinkedList<String>[] simpleSets;
			//LinkedList<String>[] complexSets;
			System.out.println("Beginning of sort.");
			long startSort=System.currentTimeMillis();
			LinkedList<String> noCrossings=new LinkedList<String>();
			LinkedList<String> withCrossings=new LinkedList<String>();
			long initial=System.currentTimeMillis();
			for(int i=0;i<levelsGenerated.size();i++){
				//int highestC=0;
				int numbOfDoubles=0;
				String curr=levelsGenerated.get(i);
				//String[] a=curr.split(":");
				String [] b=curr.split("_");
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
				//		highestC=count;
					}
				}
				if(numbOfDoubles>=6){
					//numOfDoubles is always pair. The number of crossings is actually half of it.
					//IMPORTANT RULE ! THE SORTING RULE IS ABOVE.
					withCrossings.add(curr);
				}else{
					noCrossings.add(curr);
				}
				
			}
			long endSort=System.currentTimeMillis();
			System.out.println("Sort took "+(endSort-startSort)+" milliseconds.");
			System.out.println("Total: "+levelsGenerated.size()+"  Simple: "+noCrossings.size()+"  Complex: "+withCrossings.size());
			System.out.println("Deletion of identical levels in both list has begun.");
			
			
			
			
			
			long startDelete=System.currentTimeMillis();
			LinkedList<String> simple=new LinkedList<String>();
			LinkedList<String> complex = new LinkedList<String>();
			
			
			//Deleting identical levels in simple list
			simple.add(noCrossings.get(0));
			for(int i=1;i<noCrossings.size();i++){
				boolean hasIdentical=false;
				String[] currLevel=noCrossings.get(i).split("_");
				for(int j=0;j<simple.size()&&!hasIdentical;j++){
					boolean identical=true;
					String [] comparedTo=simple.get(j).split("_");
					//Now it's time to compare. 
					for (int k=0;k<currLevel.length&&identical;k++){
						boolean present=false;
						String beingChecked=currLevel[k];
						for(int l=0;l<comparedTo.length&&!present;l++){
							if(beingChecked.equals(comparedTo[l])){
								present=true;
							}
						}
						if(!present){
							identical=false;
						}
					}
					if(identical){
						hasIdentical=true;
					}
				}
				if(!hasIdentical){
					simple.add(noCrossings.get(i));
					//System.out.println("Added path "+uniqueLevelsGenerated.getLast());
				}
			}
			
			System.out.println("Went from "+noCrossings.size()+" simple to "+simple.size());
			
			//Deleting identical levels in complex list
			complex.add(withCrossings.get(0));
			for(int i=1;i<withCrossings.size();i++){
				boolean hasIdentical=false;
				String[] currLevel=withCrossings.get(i).split("_");
				for(int j=0;j<complex.size()&&!hasIdentical;j++){
					boolean identical=true;
					String [] comparedTo=complex.get(j).split("_");
					//Now it's time to compare. 
					for (int k=0;k<currLevel.length&&identical;k++){
						boolean present=false;
						String beingChecked=currLevel[k];
						for(int l=0;l<comparedTo.length&&!present;l++){
							if(beingChecked.equals(comparedTo[l])){
								present=true;
							}
						}
						if(!present){
							identical=false;
						}
					}
					if(identical){
						hasIdentical=true;
					}
				}
				if(!hasIdentical){
					complex.add(withCrossings.get(i));
					//System.out.println("Added path "+uniqueLevelsGenerated.getLast());
				}
			}
			System.out.println("Went from "+withCrossings.size()+" complex to "+complex.size());
			
			LinkedList<String> result = new LinkedList<String>();
			long startFilter= System.currentTimeMillis();
			for(int i=0;i<complex.size();i++){
				boolean hasIdentical=false;
				String[] currLevel=complex.get(i).split("_");
				for(int j=0;j<simple.size()&&!hasIdentical;j++){
					boolean identical=true;
					String [] comparedTo=simple.get(j).split("_");
					//Now it's time to compare. 
					for (int k=0;k<currLevel.length&&identical;k++){
						boolean present=false;
						String beingChecked=currLevel[k];
						for(int l=0;l<comparedTo.length&&!present;l++){
							if(beingChecked.equals(comparedTo[l])){
								present=true;
							}
						}
						if(!present){
							identical=false;
						}
					}
					if(identical){
						hasIdentical=true;
					}
				}
				if(!hasIdentical){
					result.add(Integer.toString(rows)+"_"+Integer.toString(columns)+":"+complex.get(i));
				}
			}
			long endFilter=System.currentTimeMillis();
			
			System.out.println("Went from "+complex.size()+" complex to "+result.size()+ " valid levels in "+(endFilter-startFilter)+" milliseconds.");
			
			return result;
		}
		
		private static void deleteOversizedLevels(){
			Iterator<String> it=levelsGenerated.iterator();
			System.out.println("Initial number of solutions: "+levelsGenerated.size());
			while(it.hasNext()){
				
				String current=it.next();
				if(!it.hasNext()){
					System.out.println("Last reached: "+current);
				}
				//System.out.println("Solution: "+current);
				int width=getLevelWidth(current);
				int height=getLevelHeight(current);
				//System.out.println("w: "+width+" h: "+height);
				if(!((width<=8 && height<=13)||(width<=13 && height <=8))){
					it.remove();
				}else{
					if(width>8){
						levelsRotated.add(rotate(current));
					}else{
						levelsRotated.add(current);
					}
				}
			}
			
			System.out.println("After grid size check: "+levelsRotated.size());
		}
		
		/**Returns the number of rows currently occupied by the cells of the level. */
		public static int getLevelHeight(String path){
			String [] theCells= path.split("_");
			int min=Integer.parseInt(theCells[0]);
			int max=Integer.parseInt(theCells[0]);
			for(int k=1;k<theCells.length;k++){
				if(Integer.parseInt(theCells[k])<min){
					min=Integer.parseInt(theCells[k]);
				}
			}
			
			for(int k=1;k<theCells.length;k++){
				if(Integer.parseInt(theCells[k])>max){
					max=Integer.parseInt(theCells[k]);
				}
			}
			
			return ((max-min)/40 +1);
			
		}
		
		/**Returns the number of columns currently occupied by the cells of the level. */
		public static int getLevelWidth(String path){
			String [] theCells= path.split("_");
			int minModulo=(Integer.parseInt(theCells[0])-1)%40;
			int maxModulo=(Integer.parseInt(theCells[0])-1)%40;
			for(int k=1;k<theCells.length;k++){
				if((Integer.parseInt(theCells[k])-1)%40<minModulo){
					minModulo=(Integer.parseInt(theCells[k])-1)%40;
				}
			}
			
			for(int k=1;k<theCells.length;k++){
				if((Integer.parseInt(theCells[k])-1)%40>maxModulo){
					maxModulo=(Integer.parseInt(theCells[k])-1)%40;
				}
			}
			return maxModulo-minModulo+1;
		}
		
		/**Rotates the level to make sure that the height is bigger than the width. */
		public static String rotate(String path){
			
			String [] theCells= path.split("_");
			
			int minModulo=(Integer.parseInt(theCells[0])-1)%40;
			for(int k=1;k<theCells.length;k++){
				if((Integer.parseInt(theCells[k])-1)%40<minModulo){
					minModulo=(Integer.parseInt(theCells[k])-1)%40;
				}
			}
			
			
			int min=Integer.parseInt(theCells[0]);
			for(int k=1;k<theCells.length;k++){
				if(Integer.parseInt(theCells[k])<min){
					min=Integer.parseInt(theCells[k]);
				}
			}
			
			int moduloDifference=(min-1)%40 -minModulo;
			
			int rotationCell=min-moduloDifference;
			
			for(int i=0;i<theCells.length;i++){
				int relativeRow=(Integer.parseInt(theCells[i])-rotationCell)/40;
				int relativeColumn=(Integer.parseInt(theCells[i])-rotationCell)%40;
				theCells[i]=""+(rotationCell-(40*relativeColumn)+relativeRow);
			}
			String newPath=""+theCells[0];
			for(int i=1;i<theCells.length;i++){
				newPath=newPath+"_"+theCells[i];
			}
			return newPath;
		}
		
		/**Returns the levels rotated. */
		public LinkedList<String> getRotatedLevels(){
			
			return levelsRotated;
		}
		
		public static LinkedList<String> getLevels(){
			return levelsGenerated;
		}
		
		public static LinkedList<String> adaptToGrid(LinkedList<String> list){
			
			System.out.println("Adapting the numbers to the grid for display.");
			LinkedList<String> result=new LinkedList<String>();
			Iterator<String> i=list.iterator();
			
			while(i.hasNext()){
				String[] theCells=i.next().split("_");
				String[] newCells=new String[theCells.length];
				for(int p=0;p<theCells.length;p++){
					newCells[p]=Integer.toString(getNewCell(Integer.parseInt(theCells[p])));
				}
				String s=newCells[0];
				for(int k=1;k<newCells.length;k++){
					s=s+"_"+newCells[k];
				}
				result.add(s);
			}
			System.out.println("Successfully adapted the levels.");
			return result;
		}
		
		/**Returns the column in which this cell is in. */
		private static int getColumn(int number){
			return (number%(columns)!=0 ? number%(columns) : columns);
		}
		
		/**Returns the row in which this cell is in. */
		private static int getRow(int number){
			return number%columns==0 ? number/columns :(number/columns)+1;
		}
		
		public static int getNewCell(int oldCell){
			int myNewRow=getRow(oldCell);
			int myNewColumn=getColumn(oldCell);
			//System.out.println("Cell: "+oldCell+" Row: "+myNewRow+" Column: "+myNewColumn);
			//System.out.println("Converted "+oldCell+" to "+(((myNewRow-1)*8)+myNewColumn));
			return (((myNewRow-1)*8)+myNewColumn);
			
		}
	}