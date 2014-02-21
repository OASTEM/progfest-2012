package src;

import java.util.ArrayList;
import java.text.DecimalFormat;

public class NauticalNavigation {
	
	// Contains all the different paths
	private ArrayList<ArrayList<int[]>> paths;
	
	// a list of the results of navigation
	public ArrayList<String> results;
	
	/**
	 * Constructor
	 * @param input list representation of file
	 */
	public NauticalNavigation(ArrayList<String> input){
		parseInput(input);
	}
	
	/**
	 * formats a double so it has 4 decimal points
	 * @param num the number to format
	 * @return the formatted number
	 */
	private String formatDouble(double num){
		DecimalFormat df = new DecimalFormat("0.0000");
		String temp = df.format(num);
		return temp;
	}
	
	/**
	 * get the angle
	 * USE TAN = opp / adj
	 * @param dest the x and y coords
	 * @param quadrant the quadrant located 
	 * @return the angle in radians acording to the pdf.
	 */
	private double getAngle(int[] dest, int quadrant){
		double angle = Math.atan( ( (double)Math.abs(dest[1]) ) / ( (double)Math.abs(dest[0]) ) );
		angle = Math.abs(angle);
		if(quadrant == 4){
			angle = (2*Math.PI) - angle; 
		}
		else if(quadrant == 3){
			angle = Math.PI + angle;
		}
		else if(quadrant == 2){
			angle = Math.PI - angle;
		}
		return angle;
	}
	
	/**
	 * gets the distance between two points
	 * @param start the first point
	 * @param end the second point
	 * @return the distance between the points
	 */
	private double getDistance(int[] start, int[] end){
		double x = (double)(start[0] - end[0]);
		double y = (double)(start[1] - end[1]);
		double dist = Math.sqrt(
				Math.pow(x, 2.0) +
				Math.pow(y, 2.0));
		return dist;	
	}
	
	/**
	 * gets the next coordinate by shifting (adding x values, adding y values)
	 * @param start the starting point
	 * @param end the ending point
	 * @return the next destination!
	 */
	private int[] getNextDestination(int[] start, int[] end){
		int[] xy = new int[2];
		xy[0] = start[0] + end[0];
		xy[1] = start[1] + end[1];
		return xy;
	}
	
	/**
	 * gets the quadrant a point is located in (on a cartesian coordinate plane)
	 * @param point the point to find the quadrant 
	 * @return the quadrant, represented using an int (I -> 1, II -> 2, III -> 3, IV -> 4)
	 */
	private int getQuadrant(int[] point){
		if(point[0] > 0){
			if(point[1] > 0){
				return 1;
			}
			else{
				return 4;
			}
		}
		else{
			if(point[1] > 0){
				return 2;
			}
			else{
				return 3;
			}
		}
	}
	
	/**
	 * parses the list input, puts the data in the respective lists
	 * @param list the list to parse
	 */
	private void parseInput(ArrayList<String> list){
		paths = new ArrayList<ArrayList<int[]>>();
		ArrayList<int[]> temp = new ArrayList<int[]>();
		for(String line : list){
			if(line.equals("0")){
				paths.add(temp);
				temp = new ArrayList<int[]>();
			}
			else {
				temp.add(parseLine(line));
			}
		}
		paths.add(temp);
	}
	
	/**
	 * parses a single line, converting it into a int[] coordinate
	 * @param line (two numbers with a white space between them)
	 * @return an int[] array representation of a coordinate
	 */
	private int[] parseLine(String line){
		String num1 = line.substring(0,line.indexOf(" "));
		String num2 = line.substring(line.indexOf(" ")+1);
		int[] xyPair = new int[2];
		xyPair[0] = numConvert(num1);
		xyPair[1] = numConvert(num2);
		return xyPair;
	}
	
	/**
	 * finds resulting shortest-distance line between the origin and the ending
	 * finds the angle of that line
	 * gets the distance saved using that line over the points
	 * adds all that data into the results list
	 */
	public void navigate(){
		results = new ArrayList<String>();
		for(ArrayList<int[]> path : paths){
			int[] dest = new int[2];
			double distance = 0;
			for(int[] coord : path){
				int[] xy = getNextDestination(dest,coord);
				distance += getDistance(dest,xy);
				dest = xy;
			}
			double angle = getAngle(dest, getQuadrant(dest));
			double magnitude = getDistance(new int[2], dest);
			double saved = distance - magnitude;
			String line = formatDouble(angle)+" "+formatDouble(magnitude)+" "+formatDouble(saved);
			results.add(line);
		}
	}
	
	/**
	 * converts a String into an int
	 * @param num the String to convert
	 * @return the number as a int
	 */
	private int numConvert(String num){
		int temp = Integer.parseInt(num);
		return temp;
	}
}
