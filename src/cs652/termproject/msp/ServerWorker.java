package cs652.termproject.msp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ServerWorker {
	
	private int data[];
	
	public ServerWorker(){
		
	}
	
	public void loadDataFromFile(String filePath){
		File f= new File(filePath);
		String tempLine;
		String dataStr[];
		
    	Scanner scanner;
		try {
			scanner = new Scanner(f);
			tempLine = scanner.nextLine();
			dataStr = tempLine.split("\t");
			
			this.data = new int[dataStr.length];
			
			for(int i=0;i<dataStr.length;i++){
				data[i] = Integer.parseInt(dataStr[i]);
			}
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public int[] processQuery(int lowerBound,int upperBound){
		
		int lowerPosition = binarySearch(this.data,lowerBound);
		int upperPostion = binarySearch(this.data,upperBound);
		int result[] = new int[upperPostion-lowerPosition+1];
		for(int i=0;i<result.length;i++){
			result[i] = this.data[i+lowerPosition];
		}
		return result;
	}
	
	public int binarySearch(int array[],int target){
		int leftIndex = 0;
		int rightIndex = array.length;
		int middleIndex;
		
		while(leftIndex<=rightIndex){
			middleIndex = (leftIndex+rightIndex)/2;
			if(array[middleIndex]>target){
				leftIndex = middleIndex+1;
			}else if(array[middleIndex]<target){
				rightIndex = rightIndex - 1;
			}else{
				return (middleIndex - 1)>=0 ? (middleIndex-1) : 0;
			}
		}
		return leftIndex - 1;
	}
}
