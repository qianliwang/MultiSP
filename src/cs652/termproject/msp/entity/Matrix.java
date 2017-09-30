package cs652.termproject.msp.entity;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class Matrix {
	RealMatrix matrix;
	
	public Matrix(double[][] data){
		this.matrix = MatrixUtils.createRealMatrix(data);
	}
	public Matrix(RealMatrix matrix){
		this.matrix=matrix;
	}
	public Matrix(Matrix matrix){
		this.matrix = MatrixUtils.createRealMatrix(matrix.getMatrix());
	}
	public double[][] getMatrix(){
		return this.matrix.getData();
	}
	public Matrix getInvert(){
		return new Matrix(new LUDecomposition(this.matrix).getSolver().getInverse());
	}
	
	public Matrix multiply(Matrix mul){
		return new Matrix(this.matrix.multiply(mul.matrix));
	}
	
	public Matrix transform(){
		return new Matrix(this.matrix.transpose());
	}
	public Matrix removeCol(int n){
		double[][] data = new double[this.matrix.getRowDimension()][this.matrix.getColumnDimension()-1];
		int flag = 0;
		for(int i=0;i<this.matrix.getRowDimension();i++){
			flag = 0;
			for(int j=0;j<this.matrix.getColumnDimension();j++){
				if(j>=n&&flag==0){
					flag = 1;
					continue;
				}
				data[i][j-flag]=this.matrix.getEntry(i, j);
			}
		}
		Matrix ret = new Matrix(data);
		return ret;
	}
	public Matrix removeRow(int n){
		return this.transform().removeCol(n).transform();
	}
	
	public int getColumnDimension(){
		return matrix.getColumnDimension();
	}
	public int getRowDimension(){
		return matrix.getRowDimension();
	}
	public double getEntry(int i,int j){
		return matrix.getEntry(i, j);
	}
	public void setEntry(int i,int j,double val){
		matrix.setEntry(i, j, val);
	}
	public double[] getRow(int n){
		return matrix.getRow(n);
	}
	public double[] getCol(int n){
		return matrix.getColumn(n);
	}
	@Override
	public String toString() {
		String ret =matrix.getRowDimension()+","+matrix.getColumnDimension();
		for(int i=0;i<matrix.getRowDimension();i++){
			for(int j=0;j<matrix.getColumnDimension();j++)
				ret=ret+","+matrix.getEntry(i,j);
		}
		return ret;
	}
	public Matrix(String text){
		String[] segs = text.split(",");
		int row = Integer.parseInt(segs[0]);
		int col = Integer.parseInt(segs[1]);
		double[][] data = new double[row][col];
		for(int i=0;i<row;i++)
			for(int j=0;j<col;j++)
				data[i][j]=Double.parseDouble(segs[i*col+j+2]);
		this.matrix = MatrixUtils.createRealMatrix(data);
	}
	public double getDifference(Matrix other){
		RealMatrix diff = this.matrix.subtract(other.matrix);
		double ret = 0;
		for(int i=0;i<diff.getRowDimension();i++)
			for(int j=0;j<diff.getColumnDimension();j++){
				ret+=diff.getEntry(i, j)*diff.getEntry(i, j);
			}
		return ret;
	}	
	public Matrix getSubMatrix(int cols){
		double[][] data=new double[this.matrix.getRowDimension()][cols];
		this.matrix.copySubMatrix(0, this.matrix.getRowDimension()-1, 0, cols-1, data);
		return new Matrix(data);
	}
	public static void main(String[] args){
		double[][] data= {{1.0,2.0,3.0},{1.1,2.2,3.3}};
		Matrix matrix = new Matrix(data);
		String text = matrix.toString();
		Matrix matrix2 = new Matrix(text);
		System.out.println(matrix2.getSubMatrix(3));
		
	}
	
}
