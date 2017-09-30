package cs652.termproject.msp.test;

import java.io.FileNotFoundException;

import cs652.termproject.msp.Conf;
import cs652.termproject.msp.ConnectionInfo;
import cs652.termproject.msp.Utils;
import cs652.termproject.msp.entity.DataOwnerWrapper;

public class DataOwnerTest {
	public static void main(String args[]) throws FileNotFoundException{
		System.out.println("This is Data Owner Tester.");

		String confFilePath = Conf.DATASERVER_CONF_PATH;
		String selfConfPath = Conf.DATAOWNER_CONF_PATH;
		ConnectionInfo ci = Utils.getDataOwnerConnectionInfo(selfConfPath);
		
		DataOwnerWrapper dataOwnerWrapper = new DataOwnerWrapper(ci.port,confFilePath);

		dataOwnerWrapper.listenPorts();
		dataOwnerWrapper.distributeData();
	}
}
