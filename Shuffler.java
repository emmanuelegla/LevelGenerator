import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.LinkedList;

public class Shuffler {
	/*Launch sample: 10 3 22_cells on 10_3.txt */
	/**Shuffles the levels stored in a file to order them randomly. */
	private static PrintWriter currentFile;
	
	public static void main(String[] args){
		alg1(args);
	}
	
	private static void alg1(String [] arguments){
		
		//Launch sample: 4 4 Beginner  
		/*First arg: row
		 * Second arg: Column
		 * Third arg: Subfolder, under C:\Users\Emmanuel\workspace\Levels Generator\New\  */
		LinkedList<String> toShuffle =new LinkedList<String>();
		File folder = new File("C:/Users/Emmanuel/workspace/Levels Generator/New/"+arguments[2]);
		File[] listOfFiles = folder.listFiles();
		LinkedList<File> toProcess = new LinkedList<File>();

		for (int i = 0; i < listOfFiles.length; i++) {
		  File file = listOfFiles[i];
		  if (file.isFile() && file.getName().contains("_"+arguments[0]+"_"+arguments[1]+"_")) {
		  toProcess.add(file);
		  } 
		}
		File output=new File("C:/Users/Emmanuel/workspace/Levels Generator/New/"+arguments[2]+"/"+arguments[0]+"_"+arguments[1]+"_"+"SHUFFLED"
				+".txt");
		try {
			currentFile=new PrintWriter(output,"UTF-8");
				
//		try {
//			currentFile=new PrintWriter(arguments[0]+"_"+args[1]+"_levels_"+"SHUFFLED"
//			+".txt","UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FileInputStream fstream=null;
		for(int i=0;i<toProcess.size();i++){
			try {
				//The name of the file (including the .txt extension should be the first argument sent to the main method.
				fstream = new FileInputStream(toProcess.get(i));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			try {
			    String line;
			    while ((line = br.readLine()) != null) {
			    	toShuffle.add(line);
			       }
			}catch(Exception e){
				e.printStackTrace();
			}
		}
//		for(int i=0;i<arguments.length-2;i++){
//			try {
//				//The name of the file (including the .txt extension should be the first argument sent to the main method.
//				fstream = new FileInputStream(arguments[i+2]);
//			} catch (FileNotFoundException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
//			try {
//			    String line;
//			    int counter=0;
//			    while ((line = br.readLine()) != null) {
//			    	toShuffle.add(line);
//			       }
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
		Collections.shuffle(toShuffle);
		for(int j=0;j<toShuffle.size();j++){
			currentFile.println(toShuffle.get(j));
		}
		currentFile.close();
		System.out.println(toProcess.size()+" files shuffled and mixed.");
		
	}
	
	private static void alg2(String [] arguments){
		LinkedList<String> toShuffle =new LinkedList<String>();
		File output=new File("/New/Beginner/"+arguments[0]+"_"+arguments[1]+"_"+"SHUFFLED"
				+".txt");
		try {
			currentFile=new PrintWriter(output,"UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FileInputStream fstream=null;
		for(int i=0;i<arguments.length-2;i++){
			try {
				//The name of the file (including the .txt extension should be the first argument sent to the main method.
				fstream = new FileInputStream(arguments[i+2]);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			try {
			    String line;
			    int counter=0;
			    while ((line = br.readLine()) != null) {
			    	toShuffle.add(line);
			       }
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		Collections.shuffle(toShuffle);
		for(int j=0;j<toShuffle.size();j++){
			currentFile.println(toShuffle.get(j));
		}
		currentFile.close();
		System.out.println(arguments.length-2+" files shuffled and mixed.");
		
	}

}
