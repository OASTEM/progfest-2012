package src;

import java.util.ArrayList;
import java.util.HashMap;

public class ImageSegmenter {

	public static final String CLUSTER_NAME = "Cluster ";
	public static final double DOUBLE_THRESHOLD = 0.0005; 
	public boolean isConverged = false;
	private ArrayList<double[]> particleList;
	//private ArrayList<double[]> centerClusters;
	private HashMap<String, double[]> centerClusters;
	private HashMap<String, ArrayList<double[]>> clusterGroup;
	private ArrayList<String> clusterKey;
	
	public ImageSegmenter(ArrayList<String> list){
		//list = new ArrayList<String>(list);
		centerClusters = new HashMap<String, double[]>();
		//System.out.println("here");
		clusterKey = new ArrayList<String>();
		//System.out.println("here");
		clusterGroup = new HashMap<String, ArrayList<double[]>>();
		//System.out.println("here");
		particleList = new ArrayList<double[]>();
		//System.out.println("here");
		convertToDouble(list);
		//System.out.println("here2");
		formGroups();
		
	}
	
	private void convertToDouble(ArrayList<String> list){
		convertCenterCluster(list.get(0));
		//System.out.println("here2");
		for(int i = 1; i < list.size(); i++){
			String thisLine = list.get(i);
			if(!thisLine.substring(thisLine.length()-1).equals(",")){
				thisLine = thisLine + ",";
			}
			particleList.add(formDoubleArray(thisLine));
		}
	}
	
	private void convertCenterCluster(String centerCluster){
		int commaCount = 0;
		int index = 0;
		int clusterCount = 0;
		int commaIndex = 0;
		if(!centerCluster.substring(centerCluster.length()-1).equals(",")){
			centerCluster = centerCluster + ", ";
		}
		while(centerCluster.indexOf(",") >= 0){
			//System.out.println(centerCluster);
			//System.out.println(commaIndex+":"+index);
			commaIndex = centerCluster.indexOf(",",index+1);
			if(commaIndex >= 0){
				commaCount++;
			}
			//System.out.println(commaIndex+":"+index);
			index = commaIndex;
			if(commaCount == 3){
				addCenterCluster(formDoubleArray(centerCluster.substring(0,index+1)), clusterCount++, true);
				//System.out.println(index);
				centerCluster = centerCluster.substring(index+2);
				index = 0;
				commaCount = 0;
				commaIndex = 0;
			}
			//System.out.println(commaIndex+":"+index);
		}
	}
	
	private void addCenterCluster(double[] center, int centerClusterCount, boolean addKeys){
		String clusterName = CLUSTER_NAME + centerClusterCount;
		centerClusters.put(clusterName, center);
		clusterGroup.put(clusterName,  new ArrayList<double[]>());
		if(addKeys){
			clusterKey.add(clusterName);
		}
	}
	
	/**
	 * creates a double array of the input String
	 * @param doubles the String representation of the cluster (doubles) (add a comma at the end pls)
	 * @return a double array of the cluster
	 */
	private double[] formDoubleArray(String doubles){
		//System.out.println(doubles);
		double[] temp = new double[3];
		int index = 0;
		//int commaIndex;
		while(doubles.indexOf(",") >= 0){
			//System.out.println("The String:"+doubles);
			int commaIndex = doubles.indexOf(",");
			//System.out.println(commaIndex);
			String num = doubles.substring(0,commaIndex);
			//System.out.println(num);
			temp[index++] = changeStringToDouble(num);
			//System.out.println(doubles);
			if(index < 3){
				doubles = doubles.substring(commaIndex+2);
			}
			else {
				doubles = "wow";
			}
		}
		//temp[index] = changeStringToDouble(doubles);
		return temp;
	}
	
	private double changeStringToDouble(String num){
		try{
			double beenDoubled = Double.parseDouble(num);
			return beenDoubled;
		}
		catch(Exception e){
			return -1.0; //we should not be going here
		}
	}
	
	private double getClusterDistance(double[] cluster, double[] pixel){
		double answer = Math.sqrt( Math.pow( cluster[0] - pixel[0], 2) +
				Math.pow( cluster[1] - pixel[1], 2) +
				Math.pow( cluster[2] - pixel[2], 2) );
		//System.out.println("an answer:"+answer);
		return answer;
	}
	
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
	
	private void formGroups(){
		for(double[] pixel : particleList){
			String closest = findClosestClusterCenter(pixel);
			//System.out.println(closest);
			clusterGroup.get(closest).add(pixel);
		}
	}
	
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
				//System.out.println(avg+" avg");
				newCenter[i] = avg;
			}
			else {
				newCenter[i] = 0.0;
			}
		}
		return newCenter;
	}
	
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
	
	private void groupPixels(){
		assignNewCenters();
		formGroups();
	}
	
	private boolean compareDoubleArrays(double[] arrayOne, double[] arrayTwo){
		for(int i = 0; i < arrayOne.length;i++){
			//System.out.println(arrayOne[i]+":"+arrayTwo[i]);
			//System.out.println(Math.abs(arrayOne[i] - arrayTwo[i]));
			if(Math.abs(arrayOne[i] - arrayTwo[i]) > DOUBLE_THRESHOLD){
				
				return false;
			}
		}
		return true;
	}
	
	private boolean checkIfNoChange(ArrayList<double[]> newGroup, ArrayList<double[]> oldGroup){
		//System.out.println(newGroup.size() +":"+oldGroup.size());
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
	
	public void consolidatePixels(){
		isConverged = true;
		HashMap<String, ArrayList<double[]>> toCompare = new HashMap<String, ArrayList<double[]>>(clusterGroup);
		groupPixels();
		for(String key : clusterKey){
			if(!checkIfNoChange(clusterGroup.get(key), toCompare.get(key))){
				isConverged = false;
			}
			//System.out.println(isConverged);
		}
	}
	
	public int getTotalParticleCount(){
		return particleList.size();
	}
	
	public int[] getClusterGroupLengths(){
		int[] lengths = new int[clusterKey.size()];
		for(int i = 0; i < clusterKey.size(); i++){
			lengths[i] = clusterGroup.get(clusterKey.get(i)).size();
		}
		return lengths;
	}
	
	//private double 
}
