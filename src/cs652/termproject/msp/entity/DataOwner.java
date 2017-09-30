package cs652.termproject.msp.entity;

import java.util.Arrays;

public class DataOwner{
//	a.	generate Coefficients : generate coefficients of each servers
//	b.	get Servers: return the name,IP,port  of and which data are stored on each server
//	c.	distribute Data: generate parity data and distribute all data to servers
	final int precision =3; // A coefficient should be like 0.xxx
	int numOfServers;
	int numOfItems;
	int numOfMalicious;
	
	Matrix coefficients,dataToDistribute;
	
	public DataOwner(){
		
	}
	
	
	
	public int getNumOfServers() {
		return numOfServers;
	}



	public void setNumOfServers(int numOfServers) {
		this.numOfServers = numOfServers;
	}



	public int getNumOfItems() {
		return numOfItems;
	}



	public void setNumOfItems(int numOfItems) {
		this.numOfItems = numOfItems;
	}



	public Matrix getCoefficients() {
		return coefficients;
	}



	public void setCoefficients(Matrix coefficients) {
		this.coefficients = coefficients;
	}



	public Matrix getDataToDistribute() {
		return dataToDistribute;
	}



	public void setDataToDistribute(Matrix dataToDistribute) {
		this.dataToDistribute = dataToDistribute;
	}



	public DataOwner(int n,int m,int e){
		this.numOfItems=m;
		this.numOfServers=n;
		this.numOfMalicious=e;
	}
	
	public Matrix distributeData(int len)
	/**
	 * Generate raw data of len, 
	 * generate the coefficient matrix 
	 * and then generate the final matrix which would be distributed to all the servers
	 */
	{
		int[] rawdata = generateData(len);
		Arrays.sort(rawdata);
		int numRows = len/this.numOfItems+(len%this.numOfItems==0?0:1)+2;
		double[][] data = new double[numRows][this.numOfItems];
		for(int i=0;i<this.numOfItems;i++){
			data[0][i]=0;
			data[numRows-1][i]=2*len+1;
		}
		for(int i=1;i<numRows;i++){
			for(int j=0;j<this.numOfItems;j++)data[i][j]=((i-1)*this.numOfItems+j<len)?rawdata[(i-1)*this.numOfItems+j]:(2*len+1);
		}
		Matrix raw = new Matrix(data);
		if(this.coefficients==null)this.coefficients=this.generateCoefficients();
		dataToDistribute= raw.multiply(this.coefficients.transform());
		return dataToDistribute;
	}
	
	public double[] getDataToServer(int n)
	/**
	 * Get the data distributed to server n
	 */
	{
		return this.dataToDistribute.getCol(n);
	}
	public Matrix generateCoefficients()
	/**
	 * Generate the coefficient matrix
	 */
	{
		double uPrecision = Math.pow(10, this.precision);
		double [][] coefficients = new double[this.numOfServers][this.numOfItems];
		for(int i=0;i<this.numOfItems;i++)coefficients[i][i]=1;
		for(int i=this.numOfItems;i<this.numOfServers;i++){
			double sum = 0,sum2=0;
			for(int j=0;j<this.numOfItems;j++){
				coefficients[i][j]=Math.random();
				sum+=coefficients[i][j];
			}
			for(int j=0;j<this.numOfItems-1;j++){
				coefficients[i][j]=(int)(coefficients[i][j]/sum*uPrecision)/uPrecision;
				sum2+=coefficients[i][j];
			}
			coefficients[i][this.numOfItems-1]=Math.round(uPrecision-sum2*uPrecision)/(uPrecision);			
		}
		this.coefficients= new Matrix(coefficients);
		return this.coefficients;
	}
	
	public int[] generateData(int len)
	/**
	 * Generate a size of integers whose size is len
	 */
	{
		int[] ret = new int[len];
		for(int i=0;i<len;i++)ret[i]=(int)(Math.random()*(2*len))+1;
		return ret;
	}
	
	public static void main(String[] args){
		DataOwner owner = new DataOwner(4,2,1);
		System.out.println(owner.generateCoefficients().toString());
		System.out.println(owner.distributeData(10).toString());
	}

}
