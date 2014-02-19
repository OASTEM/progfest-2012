package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class NauticalNavigationRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException{
		if(args == null){
			System.out.println("no input detected");
			System.out.println("aborting");
		}
		else if(args[0].equals("-f")){
			ArrayList<String> input = new ArrayList<String>();
			String line;
			BufferedReader br = new BufferedReader(new FileReader(args[1]));
			while( (line=br.readLine()) != null ){
				input.add(line);
			}
			
		}
		else {
			System.out.println("lol what were you expecting");
			System.out.println("aborting");
		}
		//System.exit(0);
	}
}
