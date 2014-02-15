package src;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageSegmenterRunner {

	/**
	 * 
	 * @param args The input fileString
	 */
	public static void main(String[] args) {
		try{
			// comment this line when testing input.
			// ArrayList<String> input = readInFile(args[0]);
			
			// Comment this line when compiling as jar
			ArrayList<String> input = readInFile("particledata");
			ImageSegmenter segmenter = new ImageSegmenter(input);
			//System.out.println("how about here");
			while(segmenter.isConverged == false){
				//System.out.println("doing this");
				segmenter.consolidatePixels();
			}
			printOutFractions(segmenter.getTotalParticleCount(), segmenter.getClusterGroupLengths());
		}
		catch(Exception e){
			System.out.println("File not Found, UTF-8 encoding was not used, or file was UTF-8 encoded with BOM");
			e.printStackTrace();
		}
	}
	
	private static ArrayList<String> readInFile(String fileName) throws IOException{
		//System.out.println(Paths.get(fileName));
		List<String> text = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
		//System.out.println("do we get here");
		return new ArrayList<String>(text);
	}
	
	private static void printOutFractions(int length, int[] groupLengths){
		double lengthDub = (double) length;
		//System.out.println(lengthDub);
		for(int group : groupLengths){
			double groupDub = (double) group;
			//System.out.println("Group "+groupDub);
			double fraction = groupDub / lengthDub;
			DecimalFormat df = new DecimalFormat("0.000");
			String fractionStr = df.format(fraction);
			System.out.println(fractionStr);
		}
	}

}
