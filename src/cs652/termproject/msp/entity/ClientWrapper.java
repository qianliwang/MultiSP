package cs652.termproject.msp.entity;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import cs652.termproject.msp.Conf;
import cs652.termproject.msp.ConnectionInfo;
import cs652.termproject.msp.Utils;

public class ClientWrapper {
	
	private ArrayList<TCPSender> dataServerList;
	private Client client;
	private TCPSender dataOwnerSender;
	
	public ClientWrapper() throws FileNotFoundException{
		dataServerList = new ArrayList<TCPSender>();
		client = new Client();
		initializeDataOwnerSender(Conf.DATAOWNER_CONF_PATH);
	}
/*	
	public ClientWrapper(String confFilePath) throws FileNotFoundException{
		dataServerList = new ArrayList<TCPSender>();
		client = new Client();
		initializeDataOwnerSender(Conf.DATAOWNER_CONF_PATH);
//		initializeDataServerList(confFilePath);
	}
*/	
	public double[][] query(int lowerBound, int upperBound){
		String query = Conf.CMD_CLIENT_QUERY+","+2+","+lowerBound+","+upperBound;
		String response;
		String singleServerResultStr[];
		double result[][] = new double[dataServerList.size()][];
		int count=0;
		double singleServerResult[];
		for(TCPSender sender: dataServerList){
			//open connection, issue query to all servers, then close it.
			sender.openConnection();
			response = sender.send(query);
			sender.closeConnection();
			
			singleServerResultStr = response.split(",");
			System.out.print("Server "+count+": ");
			singleServerResult = new double[singleServerResultStr.length];
			for(int i=0;i<singleServerResultStr.length;i++){
//				tempList.add(Double.parseDouble(singleServerResultStr[i]));
				singleServerResult[i] = Double.parseDouble(singleServerResultStr[i]);
				System.out.print(singleServerResultStr[i]+",");
			}
			result[count] = singleServerResult;
			System.out.println();
			count++;
		}
		/*
		result = new double[dataServerList.size()][maxLength];
		for(int i=0;i<dataServerList.size();i++){
			for(int j=0;j<allServerResult.get(i).size();j++){
				result[i][j] = allServerResult.get(i).get(j);
			}
		}
		*/
		return result;
	}
	
	public void requestConf(){
		String query = ""+Conf.CMD_CLIENT_REQUEST_CONF;
		dataOwnerSender.openConnection();
		String response = dataOwnerSender.send(query);
		System.out.println("Receive configuration from the data owner: "+response);
		dataOwnerSender.closeConnection();
		initializeDataServerList(response);
	}
	
	public void requestCoefficient(){
		String query = ""+Conf.CMD_CLIENT_REQUEST_COEFFICIENT;
		dataOwnerSender.openConnection();
		String response = dataOwnerSender.send(query);
		System.out.println("Receive coefficient from the data owner : "+response);
		dataOwnerSender.closeConnection();
		client.coefficients = new Matrix(response);
	}
	
	public boolean isAuthentic(double[][] data){
		return client.isAuthentic(data);
	}
	
	public Matrix correctMatrix(double[][] data){
		return client.correctMatrix(data);
	}
	
	private void initializeDataServerList(String info){
		ArrayList<ConnectionInfo> serverInfoList = new ArrayList<ConnectionInfo>();
		Scanner scanner = new Scanner(info);
		String tempLine = scanner.nextLine();
		String dataStr[] = tempLine.split(";");
		int serverNum = Integer.parseInt(dataStr[0]);
		String connectionStr[];
		ConnectionInfo serverInfo;
		for(int i=1;i<=serverNum;i++){
			tempLine = dataStr[i];
			connectionStr = tempLine.split(",");
			serverInfo = new ConnectionInfo(connectionStr[0],Integer.parseInt(connectionStr[1]));
			serverInfoList.add(serverInfo);
		}
		this.client.numOfServers = serverNum;
		this.client.numOfItems = Integer.parseInt(dataStr[dataStr.length-1]);
		scanner.close();
		TCPSender sender;
		for(ConnectionInfo sInfo:serverInfoList){
			sender = new TCPSender(sInfo.serverAddr,sInfo.port);
			this.dataServerList.add(sender);
		}
	}
/*	
	private void initializeDataServerList(String filePath){
		ArrayList<ConnectionInfo> serverInfoList = Utils.loadServerInfo(filePath);
		TCPSender sender;
		for(ConnectionInfo sInfo:serverInfoList){
			sender = new TCPSender(sInfo.serverAddr,sInfo.port);
			this.dataServerList.add(sender);
		}
	}
*/	
	private void initializeDataOwnerSender(String filePath) throws FileNotFoundException{
		ConnectionInfo dataOwnerCI = Utils.getDataOwnerConnectionInfo(filePath);
		dataOwnerSender = new TCPSender(dataOwnerCI.serverAddr,dataOwnerCI.port);
	}
	
}
