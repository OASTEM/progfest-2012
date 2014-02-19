package src;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

public class TheRunner {

	/**
	 * @param args "ppp" "imgseg" "naunav"
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args == null){
			System.out.println(getError(0));
		}
		else{
			switch(args[0]){
			case "ppp":
				if(args[1].equals("-f")){
					try{
						
					}
					catch(Exception e){
						
					}
				}
				else{
					runPalindrome(args[1], args[2]);
				}
				break;
			case "imgseg":
				break;
			case "naunav":
				break;
			default:
				System.out.println(getError(1) +"'"+args[0]+"'"+getError(2));
			}
		}
	}
	
	/**
	 * Converts the String representation of the base into an int
	 * @param base the String representation of the base
	 * @return the int value of the base
	 */
	private static int convertThatBase(String base){
		int result = Integer.parseInt(base);
		return result;
	}
	
	private static String getError(int num){
		return ErrorMessages.values()[num].err;
	}
	
	private static ArrayList<String> parseFile(String fileName) throws IOException, FileNotFoundException{
		ArrayList<String> input = new ArrayList<String>();
		String line;
		BufferedReader bf = new BufferedReader(new FileReader(fileName));
		while( (line = bf.readLine()) != null){
			input.add(line);
		}
		return input;
	}
	
	private static String[] parseLine(String line){
		String num = line.substring(0, line.indexOf(","));
		String base = line.substring(line.indexOf(",")+2);
		String[] result = new String[2];
		result[0] = num;
		result[1] = base;
		return result;
	}
	
	private static void runImageSegmenter(){
		
	}
	
	private static void runPalindrome(String num, String base){
		int theBase = convertThatBase(base);
		PalindromeFinder pf = new PalindromeFinder(num, theBase);
		System.out.println(pf.getAnswer(pf.findPalindrome()));
	}
	
	private static void runPalindrome(ArrayList<String> list){
		for(String line : list){
			String[] lineData = parseLine(line);
			int base = convertThatBase(lineData[1]);
			PalindromeFinder pf = new PalindromeFinder(lineData[0], base);
			System.out.println(pf.getAnswer(pf.findPalindrome()));
		}
	}
}
