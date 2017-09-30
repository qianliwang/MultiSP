package cs652.termproject.msp.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import cs652.termproject.msp.Conf;
import cs652.termproject.msp.ConnectionInfo;
import cs652.termproject.msp.Utils;

public class DataOwnerWrapper {
	private DataOwner dataOwner;
	private ArrayList<TCPSender> senderList;
	private DataOwnerReceiver receiver;
	
/*	
	public DataOwnerWrapper(){
		dataOwner = new DataOwner();
		senderList = new ArrayList<TCPSender>();
		receiver = new DataOwnerReceiver();

	}
	public DataOwnerWrapper(int port){
		dataOwner = new DataOwner();
		senderList = new ArrayList<TCPSender>();
		receiver = new DataOwnerReceiver(port);
	}
*/	
	public DataOwnerWrapper(int port,String confPath){
		
		senderList = new ArrayList<TCPSender>();
		receiver = new DataOwnerReceiver(port);
		initializeSenderList(confPath);
	// Initialize DataOwner instance	
		int numOfServers = senderList.size();
		dataOwner = new DataOwner();
		dataOwner.setNumOfServers(numOfServers);
		dataOwner.setNumOfItems(numOfServers-2);
		dataOwner.distributeData(Conf.NUM_OF_DATA_ITEMS);
		
	}
	
	public void setPort(int portNum){
		receiver.setPort(portNum);
	}
	public void listenPorts(){
		receiver.start();
	}
	
	public void distributeData(){
		
		double dataToServer[];
		int serverNum = 0;
		for(TCPSender sender:senderList){
			
			dataToServer = dataOwner.getDataToServer(serverNum);
			sender.openConnection();
			sender.send(Conf.CMD_DATAOWNER_DISTRIBUTE_DATA+","+dataToServer.length+","+Utils.joinDoubleArray(dataToServer));
			sender.closeConnection();
			serverNum++;
		}
	}
	private void initializeSenderList(String filePath){
		ArrayList<ConnectionInfo> serverInfoList = Utils.loadServerInfo(filePath);
		TCPSender sender;
		for(ConnectionInfo sInfo:serverInfoList){
			sender = new TCPSender(sInfo.serverAddr,sInfo.port);
			this.senderList.add(sender);
		}
	}
	
	
	private class DataOwnerReceiver extends Thread{
		private ServerSocket listener;
		private int portNum;
		private BufferedReader input;
		
		public DataOwnerReceiver(){
			
		}
		public DataOwnerReceiver(int portNum){
			this.portNum = portNum;
	/*		
			try {
				listener = new ServerSocket(portNum);
			} catch (IOException e) {
	 			e.printStackTrace();
			}
	*/		
		}
		
		public void setPort(int portNum){
			this.portNum = portNum;
		}
		
		public void listen(){
			String query;
	        try {
	        	ServerSocket listener = new ServerSocket(this.portNum);
	            while (true) {
	                Socket socket = listener.accept();
	                try {
	                	input = new BufferedReader(
	                            new InputStreamReader(socket.getInputStream()));
	                	PrintWriter out =
	                            new PrintWriter(socket.getOutputStream(), true);
//	                	System.out.println(input.readLine());
	                	query = input.readLine();
	                	String answer = "";
	                	if(!query.equals("") && query != null){
	                		int cmd = Integer.parseInt(query);
	                		if(cmd==Conf.CMD_CLIENT_REQUEST_CONF){
	                			ArrayList<ConnectionInfo> serverInfoList = Utils.loadServerInfo(Conf.DATASERVER_CONF_PATH);
	                			answer = serverInfoList.size()+";";
	                			for(ConnectionInfo si:serverInfoList){
	                				answer += si.serverAddr+","+si.port+";";
	                			}
	                			answer += serverInfoList.size()-2;
	                			System.out.println("Receive: CMD_CLIENT_REQUEST_CONF\n");
		                	}else if(cmd==Conf.CMD_CLIENT_REQUEST_COEFFICIENT){
		                		answer = dataOwner.coefficients.toString();
		                		System.out.println("Receive: CMD_CLIENT_REQUEST_COEFFICIENT");
		                	}else{
		                		
		                	}
	              
	                		out.println(answer);
	                		out.flush();
	                		out.close();
	                	}
	                	
//	                        out.println("Welcome client..");
	                        
	                	
	                }finally {
	                    socket.close();
	                }            
	            }
	        } catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/*public void Close(){
			try {
	            listener.close();
	        }catch(IOException e){
	        	
	        }
		}*/
		public void run(){
			listen();
		}
	} 
}
