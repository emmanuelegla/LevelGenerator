import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

public class Splitter {
	
	public static void main(String[] args){
		FileInputStream fstream=null;
		try {
			fstream = new FileInputStream("C:/Users/Emmanuel/workspace/Levels Generator/New/Levels/"+args[0]);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		LinkedList<String> input=new LinkedList<String>();
		try {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	input.add(line);
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
		int fileNumber=1;
		
		while(input.size()>0){
			int a=0;
			PrintWriter currentFile = null;
			File output=new File("C:/Users/Emmanuel/workspace/Levels Generator/New/Levels/"+args[0]+"_"+fileNumber+"_"
					+".txt");
			try {
				currentFile=new PrintWriter(output,"UTF-8");
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while(a<10 &&input.size()>0){
				currentFile.println(input.pop());
				a++;
			}
			currentFile.close();
			fileNumber++;
		}
	}

}
