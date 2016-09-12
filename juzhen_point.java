package org.ansj.test;

public class juzhen_point {
	private String medicine_name;//药品名称
	private int sum;//药品出现的总数
	private int num;//就诊次数
	private float mean;//药品出现的平均数
	private boolean is_centroid;//是否是中心点的矩阵点
	
	
	public juzhen_point(boolean is_centroid){
		num=0;
		sum=0;
		mean=0;
		this.is_centroid = is_centroid;
//		medicine_name = new String();
	}
	public String getMedicine_name() {
		return medicine_name;
	}
	public void setMedicine_name(String medicine_name) {
		this.medicine_name = medicine_name;
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	public void add_sum(int num){
		this.sum+=num;
	}
	public int getNum() {
		return num;
	}
	public void num_increase() {
		this.num ++;
	}
	public float  getMean() {
		if(is_centroid==false){//是普通矩阵的药方则药计算总数除以药方次数的平均值返回
//			DecimalFormat df = new DecimalFormat("0");//格式化小数 
//			this.mean = Integer.parseInt(this.sum/this.num);
//			System.out.println("wtf0");
			return (this.sum/this.num);
		}else{
//			System.out.println("wtf1");
//			DecimalFormat df = new DecimalFormat("0");//格式化小数 
//			this.mean = Integer.parseInt(df.format(this.mean));
			return mean;//是中心点矩阵则直接返回
		}
	}
	public void setMean(float mean) {
		this.mean = mean;
	}
	
	public boolean equals(juzhen_point p2) {
//		System.out.println("ffffffffffffffffffffffffffffff");
		if((this.getMedicine_name().equals(p2.getMedicine_name()))&&(this.getMean()==p2.getMean()))
			return true;
		return false;
	}	

		
	
}
