package cs652.termproject.msp.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPSender {
	private String serverAddress;
	private int serverPort;
	private PrintWriter out;
	private Socket s;
	
	public TCPSender(){
		
	}
	public TCPSender(String serverAddress,int serverPort){
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		
	}
	
	public void openConnection(){
		try {
			this.s = new Socket(serverAddress, serverPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String send(String query){
		String answer = "Fail to connect "+serverAddress+","+serverPort;
		try {
			
			out = new PrintWriter(s.getOutputStream(), true);
			out.println(query);
			
			BufferedReader input =
		            new BufferedReader(new InputStreamReader(s.getInputStream()));
			
		    answer = input.readLine();
//		    System.out.println(answer);
		   
		} catch (IOException e) {
			e.printStackTrace();
		}
		return answer;
        
	}
	public void closeConnection(){
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
