package cs652.termproject.msp.entity;

public class DataServer {
	double[] data;
	public String getIP(){
		return null;
	}
	public int getPort(){
		return 0;
	}
	public void getData()
	/**
	 * Get data distributed from data owner,
	 * data server works as a data receiver/listner
	 */
	{
		int n=20;
		data = new double[n];
		for(int i=0;i<n;i++)data[i]=1.0*i;
	}
	
	public void setData(double[] data){
		this.data = data;
	}
	
	public double[] query(double low,double up){
		int head=0,tail = data.length,mid;
		while(head<tail){
			if(head+1==tail)break;
			mid = (head+tail)/2;
			if(data[mid]>=low){
				tail= mid-1;
			}else{
				head = mid;
			}
		}
		int xhead = head;
		if(data[tail]<low)xhead=tail;
		head=0;
		tail = data.length;
		while(head<tail){
			if(head+1==tail)break;
			mid = (head+tail)/2;
			if(data[mid]<=up){
				head = mid+1;
			}else {
				tail = mid;
			}
		}
		int xtail = tail;
		if(data[head]>up)xtail=head;
		double[] ret = new double[xtail-xhead+1];
		for(int i=xhead;i<=xtail;i++)ret[i-xhead]=data[i];
		return ret;
	}
/*	
	public static void main(String[] args){
		DataServer ds = new DataServer();
		ds.getData();
		double[] result = ds.query(3.3, 7.7);
		for(double x:result)System.out.print(x+" ");
	}
*/	
}
