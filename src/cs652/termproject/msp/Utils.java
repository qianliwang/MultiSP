package cs652.termproject.msp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Utils {
	public static ArrayList<ConnectionInfo> loadServerInfo(String filePath){
		File f= new File(filePath);
		String tempLine;
		ConnectionInfo sInfo;
		String dataStr[];
		
		ArrayList<ConnectionInfo> serverInfoList = new ArrayList<ConnectionInfo>();
		
    	Scanner scanner;
		try {
			scanner = new Scanner(f);
			tempLine = scanner.nextLine();
			while(scanner.hasNextLine()){
				tempLine = scanner.nextLine();
				dataStr = tempLine.split(",");
				sInfo = new ConnectionInfo(dataStr[0],Integer.parseInt(dataStr[1]));
				serverInfoList.add(sInfo);
			}
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return serverInfoList;
	}
	public static String joinDoubleArray(double[] array){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<array.length-1;i++){
			sb.append(array[i]).append(",");
		}
		sb.append(array[array.length-1]);
		return sb.toString();
	}
	public static void saveToDisk(String outputPath, double [] data) throws FileNotFoundException{
		File f = new File(outputPath);
		f.getParentFile().mkdirs();
		PrintWriter out = new PrintWriter(outputPath);
		out.println(data.length);
		for(int i=0;i<data.length;i++){
			out.println(data[i]);			
		}
		out.close();
	}
	public static double[] loadDataFromDisk(String inputPath) throws FileNotFoundException{
		File f = new File(inputPath);
		Scanner scanner = new Scanner(f);
		String dataStr = null;
		int dataLength = Integer.parseInt(scanner.nextLine());
		double[] data = new double[dataLength];
		int i=0;
		while(scanner.hasNextDouble()){
			data[i] = scanner.nextDouble();
			i++;
		}
		scanner.close();
		return data;
	}
	public static ConnectionInfo getDataOwnerConnectionInfo(String inputPath) throws FileNotFoundException{
		File f = new File(inputPath);
		Scanner scanner = new Scanner(f);
		String dataStr = null;
		if(scanner.hasNextLine()){
			dataStr = scanner.nextLine();
		}
		String[] dataStrArray = dataStr.split(",");
		String dataOwnerAddr = dataStrArray[0];
		int dataOwnerPort = Integer.parseInt(dataStrArray[1]);
		ConnectionInfo ci = new ConnectionInfo(dataOwnerAddr,dataOwnerPort);
		return ci;
	}
}
