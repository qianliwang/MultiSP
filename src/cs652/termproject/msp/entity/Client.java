package cs652.termproject.msp.entity;

public class Client {
	Matrix coefficients;
	int numOfServers;
	int numOfItems;
	final double threshold = 0.0001;
	public double[] query(double low, double up)
	/** 
	 * Issue a range query to all servers and
	 * reconstruct the matrix
	 * 
	 */
	{
		
		return null;
	}
	public boolean isAuthentic(double[][] data){
		Matrix subMatrix = new Matrix(getSubMatrix(data));
		Matrix dataSub = subMatrix.getSubMatrix(this.numOfItems);
		if(subMatrix.getDifference(dataSub.multiply(this.coefficients.transform()))<this.threshold)return true;
		return false;
	}
	
	public Matrix correctMatrix(double[][] data){
		Matrix subMatrix = new Matrix(getSubMatrix(data));
		Matrix ret;
		// Here we only consider one malicious data server;
		for(int i=0;i<this.numOfServers;i++){
			Matrix tempCoeff = this.coefficients.removeRow(i);
			Matrix tempSubMatrix = subMatrix.removeCol(i);

			ret = tempSubMatrix.multiply(tempCoeff).multiply(tempCoeff.transform().multiply(tempCoeff).getInvert());
			
			if(ret.multiply(tempCoeff.transform()).getDifference(tempSubMatrix)<this.threshold){
				System.out.println("Server "+ i +" is malicious!");
				return ret;
			}
			
		}
		return null;
	}
	
	public double[][] getSubMatrix(double[][] data)
	/**
	 * Recover the matrix based on each server's response
	 * data is the sub-query result frome each server
	 */
	{
		double first = data[0][0],last = data[this.numOfItems-1][data[this.numOfItems-1].length-1];
//		System.out.println(first+" , "+ last);
		int rows;
		if(data[this.numOfItems-1][0]<first)rows = data[this.numOfItems-1].length-1;
		else rows = data[this.numOfItems-1].length;
		double[][] ret = new double[rows][this.numOfServers];
		int[] start = new int[this.numOfServers];
		for(int i=0;i<this.numOfServers;i++){
			for(int j=0;j<rows;j++){
				while(data[i][start[i]]<first && start[i]<data[i].length)start[i]++;
				if(start[i]<data[i].length){
					ret[j][i]=data[i][start[i]];
					start[i]++;
				}
			}
		}
		return ret;
	}
/*	
	public static void main(String[] args){
		double[][] data = {{4.0,9.0,12.0},{5,9,13},{2,6,10,14},{3,7,11},{2,6,10,14},{5.5,9.5,13.5}};
		Client client = new Client();
		client.numOfServers=6;
		client.numOfItems=4;
		double[][] submatrix = client.getSubMatrix(data);
		System.out.println(new Matrix(submatrix).toString());
		double[][] coeff = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1},{0.1,0.2,0.3,0.4},{0.25,0.25,0.25,0.25}};
		client.coefficients = new Matrix(coeff);
		client.correctMatrix(data);
	}
*/	
}
