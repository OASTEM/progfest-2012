package src;

import java.util.ArrayList;

public class NauticalNavigation {
	
	public NauticalNavigation(ArrayList<String> input){
		
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
	
	private double getDistance(int[] start, int[] end){
		double dist = Math.sqrt( 
				Math.pow(( (double)start[0] - (double)end[0] ), 2.0) +
				Math.pow(( (double)start[1] - (double)end[1] ), 2.0) );
		return dist;	
	}
	
	private int[] getNextDestination(int[] start, int[] end){
		int[] nextDest = new int[2];
		nextDest[0] = start[0] + end[0];
		nextDest[1] = start[1] + end[1];
		return nextDest;
	}
	
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
}
