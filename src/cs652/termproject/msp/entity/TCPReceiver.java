package cs652.termproject.msp.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPReceiver extends Thread{

	private ServerSocket listener;
	private int portNum;
	private BufferedReader input;
	
	public TCPReceiver(){
		
	}
	public TCPReceiver(int portNum){
		this.portNum = portNum;
		try {
			listener = new ServerSocket(portNum);
		} catch (IOException e) {
 			e.printStackTrace();
		}
	}
	
	public void listen(){

//		ServerSocket listener = new ServerSocket(9090);
        try {
            while (true) {
                Socket socket = listener.accept();
                try {
                	input = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                	System.out.println(input.readLine());
                	
                	PrintWriter out =
                            new PrintWriter(socket.getOutputStream(), true);
//                        out.println("Welcome client..");
                        
                	
                }finally {
                    socket.close();
                }            
            }
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void Close(){
		try {
            listener.close();
        }catch(IOException e){
        	
        }
	}
	public void run(){
		listen();
	}
}
