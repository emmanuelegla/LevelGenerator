import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;


/*CURRENTLY NOT USING THIS CLASS. Use class FmyFrame instead. */
public class Test {
	
	
	public static void main(String[] args){
		String test="_1_2_3_";
		String [] test2=test.split("_");
		System.out.println("Length after split is "+test2.length);
		for(int i=0;i<test2.length;i++){
			System.out.println("Position "+i+" "+test2[i]);
		}
		//		LinkedList<String> temporaryResults;
//		String allLevelsOrdered="";
//		
//		String initialPath=new String("820");
//		Node root=new Node();
//		root.setPath(initialPath);
//		if(args.length==0){
//			System.out.println("Please enter the number of cells needed in the path.");
//		}else{
//			Node.clearLists();
//			//root.generateLevels(initialPath, Integer.parseInt(args[0])-1, Node.Direction.RIGHT, 820);
//			temporaryResults=Node.getLevelsGenerated();
//			System.out.println("Initial number of solutions: "+temporaryResults.size());
//			
//			LinkedList<String> fixed=convertToGameGrid(temporaryResults);
//		}
	}
	
	public static LinkedList<String> convertToGameGrid(LinkedList<String> levels){
		
		LinkedList<String> standardized=new LinkedList<String>();
		Iterator<String> iterator=levels.iterator();
		while(iterator.hasNext()){
			String current=iterator.next();
			String [] theCells= current.split("_");
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
				
				theCells[i]=""+(1+(8*relativeRow)+relativeColumn);
				
			}
			String newPath=""+theCells[0];
			for(int i=1;i<theCells.length;i++){
				newPath=newPath+"_"+theCells[i];
			}
			standardized.add(newPath);
			//System.out.println(newPath);
			
		}
		
		return standardized;
	}
	
	
	

	
	
	
	
}
