package cs652.termproject.msp.test;

import java.util.ArrayList;

import cs652.termproject.msp.Conf;
import cs652.termproject.msp.ConnectionInfo;
import cs652.termproject.msp.Server;
import cs652.termproject.msp.Utils;
import cs652.termproject.msp.entity.DataServerWrapper;

public class DataServerManager {

	private ArrayList<ConnectionInfo> serverInfoList;
	private String confFilePath = Conf.DATASERVER_CONF_PATH;
	
	public DataServerManager(){
		serverInfoList = Utils.loadServerInfo(confFilePath);
	}
	
	public void test(){
		DataServerWrapper dswrapper;
		for(int i=0;i<serverInfoList.size();i++){
			dswrapper = new DataServerWrapper(serverInfoList.get(i).port);
			dswrapper.listenPorts();
		}
	}
	
	public static void main(String args[]){
		DataServerManager dataServerManager = new DataServerManager();
		dataServerManager.test();
	}
}
