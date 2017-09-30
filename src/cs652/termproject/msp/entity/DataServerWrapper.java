package cs652.termproject.msp.entity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import cs652.termproject.msp.Conf;
import cs652.termproject.msp.Utils;

public class DataServerWrapper {
	
	
	private DataServer dataServer;
	private DataServerReceiver receiver;
	private int portNum;
	private String localDataPath;

/*	
	public DataServerWrapper(){
		dataServer = new DataServer();
		receiver = new DataServerReceiver();
	}
*/	
	public DataServerWrapper(int portNum){
		dataServer = new DataServer();
		this.portNum = portNum;
		receiver = new DataServerReceiver(this.portNum);
		localDataPath = Conf.SINGLE_SERVER_DATA_PATH+portNum+File.separator+"data.txt";
	}
	public void listenPorts(){
		receiver.start();
	}
	

	
	private class DataServerReceiver extends Thread{
		private ServerSocket listener;
		private int portNum;
		private BufferedReader input;
		
		public DataServerReceiver(){
			
		}
		public DataServerReceiver(int portNum){
			this.portNum = portNum;
			
		}
		
		public void listen(){
			
			String query;
			String queryItem[];
	        try {
	        	ServerSocket listener = new ServerSocket(this.portNum);
	        	System.out.println("Listening port "+this.portNum);
	            while (true) {
	                Socket socket = listener.accept();
	                try {
	                	input = new BufferedReader(
	                            new InputStreamReader(socket.getInputStream()));
	                	PrintWriter out =
	                            new PrintWriter(socket.getOutputStream(), true);
//	                	System.out.println(input.readLine());
	                	query = input.readLine();
	                	queryItem = query.split(",");
	                	if(queryItem.length > 2){
	                		int cmd = Integer.parseInt(queryItem[0]);
	                		if(cmd==Conf.CMD_DATAOWNER_DISTRIBUTE_DATA){
	                			if(Integer.parseInt(queryItem[1]) == queryItem.length-2){
	                				System.out.print(this.portNum+" receive data from Data Owner: ");
	                				double data[] = new double[queryItem.length-2];
	                				for(int i=0;i<data.length;i++){
	                					data[i] = Double.parseDouble(queryItem[i+2]);
	                					System.out.print(data[i]+",");
	                				}
	                				System.out.println();
	             //Give the received distributed data to DataServer   				
//	                				dataServer.setData(data);
	             //Save the received distributed data to local disk   				
	                				Utils.saveToDisk(localDataPath, data);
	                			}else{
	                				System.err.println("DataServer receive distributed matrix data Exception, there should be "+queryItem[1]+" data items, but only receive "+(queryItem.length-2));
	                			}
		                	}else if(cmd==Conf.CMD_CLIENT_QUERY){
		                		double dataFromDisk[] = Utils.loadDataFromDisk(localDataPath);
		                		dataServer.setData(dataFromDisk);
		                		if(Integer.parseInt(queryItem[1]) == queryItem.length-2){
	                				double lowerBound = Double.parseDouble(queryItem[2]);
	                				double upperBound = Double.parseDouble(queryItem[3]);
	             //Give the lowerBound and upperBound to DataServer for query processing.   				
	                				double result[] = dataServer.query(lowerBound, upperBound);
	                				String response = Utils.joinDoubleArray(result);
	                				out.println(response);
	                				out.flush();
	                				out.close();
	                			}else{
	                				System.err.println("Exception, there should be "+queryItem[1]+" data items, but only receive "+(queryItem.length-2));
	                			}
		                	}else{
		                		
		                	}
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
