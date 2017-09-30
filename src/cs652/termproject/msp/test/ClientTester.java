package cs652.termproject.msp.test;

import java.io.FileNotFoundException;

import cs652.termproject.msp.Conf;
import cs652.termproject.msp.entity.ClientWrapper;

public class ClientTester {

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("This is Client tester.");
		
		ClientWrapper c = new ClientWrapper();
		c.requestConf();
		c.requestCoefficient();
		int lowerBound = 200;
		int upperBound = 250;
		double data[][] = c.query(lowerBound, upperBound);
		if(!c.isAuthentic(data)){
			System.out.println(c.correctMatrix(data));
		}else{
			System.out.println("The query result is authentic.");
		}
	}

}
