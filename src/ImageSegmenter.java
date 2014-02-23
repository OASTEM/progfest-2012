package src;

import java.util.ArrayList;
import java.util.HashMap;

public class ImageSegmenter {

	// base name for a cluster group
	public static final String CLUSTER_NAME = "Cluster ";
	
	// threshold for double comparison
	public static final double DOUBLE_THRESHOLD = 0.0005;
	
	// if we dont have anymore changes, this be true
	public boolean isConverged = false;
	
	// the list of particles
	private ArrayList<double[]> particleList;
	
	// cluster lists:
	// centerClusters -> list of the centers clusters
	// clusterGroup -> list of clustergroups
	// clusterKey -> list of the keys for the hashmap
	private HashMap<String, double[]> centerClusters;
	private HashMap<String, ArrayList<double[]>> clusterGroup;
	private ArrayList<String> clusterKey;
	
	/**
	 * Constructor
	 * @param list The list to parse (input)
	 */
	public ImageSegmenter(ArrayList<String> list){
		centerClusters = new HashMap<String, double[]>();
		clusterKey = new ArrayList<String>();
		clusterGroup = new HashMap<String, ArrayList<double[]>>();
		particleList = new ArrayList<double[]>();
		convertToDouble(list);
		formGroups();
	}
	
	/**
	 * Using a double array and how many clusters we got to add the 
	 * cluster data into the cluster centers list, the cluster groups list,
	 * and the clusterkey list
	 * @param center the center cluster array
	 * @param centerClusterCount the center cluster number we are at
	 * @param addKeys true if this is our first time adding clusters, false if we are just consolidating
	 */
	private void addCenterCluster(double[] center, int centerClusterCount, boolean addKeys){
		String clusterName = CLUSTER_NAME + centerClusterCount;
		centerClusters.put(clusterName, center);
		clusterGroup.put(clusterName,  new ArrayList<double[]>());
		if(addKeys){
			clusterKey.add(clusterName);
		}
	}
	
	/**
	 * Assign the new centers by using the averages and creating
	 * a new cluster
	 * Clears the centers and groups lists to prep them for new data 
	 */
	private void assignNewCenters(){
		ArrayList<double[]> tempCenter = new ArrayList<double[]>();	
		int index = 0;
		for(int i = 0; i < clusterKey.size(); i++){
			tempCenter.add(createNewCenterCluster(clusterGroup.get(clusterKey.get(i)),index++));
		}
		centerClusters.clear();
		clusterGroup.clear();
		for(int i = 0; i < clusterKey.size(); i++){
			addCenterCluster(tempCenter.get(i), i, false);
		}
	}
	
	/**
	 * Changes a string into a double
	 * @param num the number as a String
	 * @return the number as a double
	 */
	private double changeStringToDouble(String num){
		try{
			double beenDoubled = Double.parseDouble(num);
			return beenDoubled;
		}
		catch(Exception e){
			return -1.0; //we should not be going here
		}
	}
	
	/**
	 * quickly checks if the array changed
	 * @param newGroup the list after consolidating
	 * @param oldGroup the list before consolidating
	 * @return true if there is no change, false if there is
	 */
	private boolean checkIfNoChange(ArrayList<double[]> newGroup, ArrayList<double[]> oldGroup){
		if(newGroup.size() == oldGroup.size()){
			for(int i = 0; i < newGroup.size(); i++){
				if(!compareDoubleArrays(newGroup.get(i), oldGroup.get(i))){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * compares two double arrays
	 * @param arrayOne an array
	 * @param arrayTwo another array
	 * @return true if the arrays are the same, false if the arrays are different
	 */
	private boolean compareDoubleArrays(double[] arrayOne, double[] arrayTwo){
		for(int i = 0; i < arrayOne.length;i++){
			if(Math.abs(arrayOne[i] - arrayTwo[i]) > DOUBLE_THRESHOLD){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * constantly assigns pixels to cluster center groups,
	 * creates new centers
	 * and assigns pixels to those centers 
	 * until no more changes
	 */
	public void consolidatePixels(){
		isConverged = true;
		HashMap<String, ArrayList<double[]>> toCompare = new HashMap<String, ArrayList<double[]>>(clusterGroup);
		groupPixels();
		for(String key : clusterKey){
			if(!checkIfNoChange(clusterGroup.get(key), toCompare.get(key))){
				isConverged = false;
			}
		}
	}
	
	/**
	 * converts an acceptable String into a cluster center 
	 * (must be for a center cluster data)
	 * @param centerCluster the String to make a center cluster
	 */
	private void convertCenterCluster(String centerCluster){
		int commaCount = 0;
		int index = 0;
		int clusterCount = 0;
		int commaIndex = 0;
		if(!centerCluster.substring(centerCluster.length()-1).equals(",")){
			centerCluster = centerCluster + ", ";
		}
		while(centerCluster.indexOf(",") >= 0){
			commaIndex = centerCluster.indexOf(",",index+1);
			if(commaIndex >= 0){
				commaCount++;
			}
			index = commaIndex;
			if(commaCount == 3){
				addCenterCluster(formDoubleArray(centerCluster.substring(0,index+1)), clusterCount++, true);
				centerCluster = centerCluster.substring(index+2);
				index = 0;
				commaCount = 0;
				commaIndex = 0;
			}
		}
	}
	
	/**
	 * converts the list into a double array list, adding it to 
	 * the particle list data
	 * @param list the list to be converted
	 */
	private void convertToDouble(ArrayList<String> list){
		convertCenterCluster(list.get(0));
		for(int i = 1; i < list.size(); i++){
			String thisLine = list.get(i);
			if(!thisLine.substring(thisLine.length()-1).equals(",")){
				thisLine = thisLine + ",";
			}
			particleList.add(formDoubleArray(thisLine));
		}
	}
	
	/**
	 * creates a new center cluster by averaging the corresponding
	 * cluster group data
	 * @param pixelGroup the group of pixels
	 * @param index the corresponding data (RGB) to average
	 * @return a double array representing the new cluster center
	 */
	private double[] createNewCenterCluster(ArrayList<double[]> pixelGroup, int index){
		double sum = 0.0;
		double count = 0.0;
		double[] newCenter = new double[3];
		for(int i = 0; i < 3; i++){
			if(i == index){
				for(double[] pixel : pixelGroup){
					sum += pixel[i];
					count++;
				}
				double avg = sum / count;
				newCenter[i] = avg;
			}
			else {
				newCenter[i] = 0.0;
			}
		}
		return newCenter;
	}
	
	/**
	 * finds which cluster is the closest to the pixel
	 * @param pixel the pixel we need to get center
	 * @return The key of the group that the pixel is closest to
	 */
	private String findClosestClusterCenter(double[] pixel){
		String lowestKey = clusterKey.get(0);
		double lowest = getClusterDistance(centerClusters.get(lowestKey), pixel);
		for(int i = 1; i < clusterKey.size(); i++){
			String tempKey = clusterKey.get(i);
			double temp = getClusterDistance(centerClusters.get(tempKey), pixel);
			if(temp < lowest){
				lowest = temp;
				lowestKey = tempKey;
			}
		}
		return lowestKey;
	}
	
	/**
	 * 
	 * @param doubles
	 * @return
	 */
	private double[] formDoubleArray(String doubles){
		double[] temp = new double[3];
		int index = 0;
		while(doubles.indexOf(",") >= 0){
			int commaIndex = doubles.indexOf(",");
			String num = doubles.substring(0,commaIndex);
			temp[index++] = changeStringToDouble(num);
			if(index < 3){
				doubles = doubles.substring(commaIndex+2);
			}
			else {
				doubles = "wow";
			}
		}
		return temp;
	}
	
	private void formGroups(){
		for(double[] pixel : particleList){
			String closest = findClosestClusterCenter(pixel);
			clusterGroup.get(closest).add(pixel);
		}
	}
	
	private double getClusterDistance(double[] cluster, double[] pixel){
		double answer = Math.sqrt( Math.pow( cluster[0] - pixel[0], 2) +
				Math.pow( cluster[1] - pixel[1], 2) +
				Math.pow( cluster[2] - pixel[2], 2) );
		return answer;
	}
	
	public int[] getClusterGroupLengths(){
		int[] lengths = new int[clusterKey.size()];
		for(int i = 0; i < clusterKey.size(); i++){
			lengths[i] = clusterGroup.get(clusterKey.get(i)).size();
		}
		return lengths;
	}
	
	public int getTotalParticleCount(){
		return particleList.size();
	}
	
	private void groupPixels(){
		assignNewCenters();
		formGroups();
	}
}
