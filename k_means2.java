package org.ansj.test;

import java.io.File;
import java.util.HashSet;
import java.util.Random;


class k_means_method{
	public  juzhen[] ju_zhens;
	public  int N=0;//总点数
	public  int D=0;//维数
	public  int K=3;//簇数
	public  juzhen_centroid centroid[];        //中心点
	public  juzhen_centroid oldCentroid[];    //上一次的中心点，用于确定中心点是否不再改变
	
	//计算维度,也就是计算不同药方中一共出现多少种药
	public  int demensions(juzhen[] ju_zhens){
			HashSet<String> names = new HashSet<String>();
			for(juzhen jz:ju_zhens){
				names.addAll(jz.medicine_names);
			}
			return names.size();	
	}
	public void init(){		
			String url = "C:\\Users\\Administrator\\Desktop\\big_data";//样本文件
			File[] files = (new File(url)).listFiles();
			N=files.length;
			ju_zhens=new juzhen[N];
			
			for(int i = 0;i<files.length;i++){
				if(files[i].isFile()){
					ju_zhens[i]=new juzhen(files[i]);
				}
			}
			D=demensions(ju_zhens);
			
			//初始化中心点
			centroid = new juzhen_centroid[K];
			oldCentroid = new juzhen_centroid[K];
			juzhen[] ju_zhens2 = new juzhen[3];
			String url2 = "C:\\Users\\Administrator\\Desktop\\centroid";
			File[] files2 = (new File(url2)).listFiles();
			for(int i = 0;i<files2.length;i++){
				if(files2[i].isFile()){
					System.out.println(files2[i].getName());
					ju_zhens2[i]=new juzhen(files2[i]);
				}
			}
			
			
			//把随机的K个点赋值给中心点
//			for(int i=0;i<K;i++){
//				Random rand = new Random();
//				int ran = (int)(rand.nextDouble()*files.length);
//				System.out.println("ran:"+ran);
//				centroid[i]=new juzhen_centroid(i,ju_zhens[ran]);
//				oldCentroid[i]=new juzhen_centroid(i,ju_zhens[i]);
//				//oldCentroid[i]=centroid[i];
//				juzhen_centroid.copy(oldCentroid[i], centroid[i]);
//			}
			centroid[0]=new juzhen_centroid(0,ju_zhens2[0]);	
			centroid[1]=new juzhen_centroid(1,ju_zhens2[2]);
			centroid[2]=new juzhen_centroid(2,ju_zhens2[1]);	
			for(int i=0;i<3;i++){
				oldCentroid[i]=new juzhen_centroid(i,ju_zhens[i]);
				juzhen_centroid.copy(oldCentroid[i], centroid[i]);
			}
			
	}
	//计算出所有属于这一个簇的所有点中离中心点最近的点，然后把这个点赋值给中心点，这样能减少孤立点对分类的影响
	public int neatest_juzhen(int clusterNum){
		int min_i = 0;
		double minDis = 99999999;
		for(int i=0;i<ju_zhens.length;i++){	
			if((ju_zhens[i].clusterNum==clusterNum)){
				double curDis =  juzhen_centroid.Euclidean(ju_zhens[i], centroid[clusterNum]);
				if(curDis<minDis){
					minDis = curDis;
					min_i = i;
				}
			}
		}
		System.out.println("min_i:"+min_i);
		return min_i;
	}
	/*更新中心点，更新中心点的思想是首先设置每个簇的中心点的值（药品克数）都为零，然后算出该药品在该簇中的总克数，然后除以该簇的点数就是中心点的值*/
	public  void updateCentroid(int clusterNum)
	{		
		int clusterSize = 0;	
		
		//只要是同一簇的点把总值赋给中心点
		for (int i = 0; i < N; ++i)
			if (ju_zhens[i].clusterNum == clusterNum)
			{
				clusterSize++;
				
		//ju_zhen_add函数实现把第一个矩阵和第二个矩阵相加，如果药品有相同则药的数量相加，一方有一方没有则有的一方赋给中心点，最后将该中心点返回
				centroid[clusterNum]= juzhen_centroid.juzhen_add(centroid[clusterNum],ju_zhens[i],clusterNum);
		//		System.out.println("asd i:"+i);
		//		showCentroid();
			}
		if (clusterSize == 0)
			return;
//		System.out.println("---------"+clusterSize);
		centroid[clusterNum].calculte_mean(clusterSize);
//		showCentroid();
	}
	 /*更新中心点的接口函数*/
	public void updateCentroids(){
		
		for (int i = 0; i < K; ++i){
			centroid[i].set_zero();
			updateCentroid(i);
		}
//		for(int i=0;i<K;++i){
//			centroid[i]=new juzhen_centroid(i, ju_zhens[neatest_juzhen(i)]);
//		}
	}
	
	
	/*判断算法是否终止*/
	public   Boolean stop(){
		/*如果每一个中心点都与上一次的中心点相同，则算法终止，否则更新oldCentroid*/
		boolean same = true;
		for (int i = 0; i < K; ++i)
			if (!juzhen_centroid.equal(oldCentroid[i], centroid[i])){
				juzhen_centroid.copy(oldCentroid[i],centroid[i]);
//				oldCentroid[i]=centroid[i];
	//			System.out.println("equal?"+(juzhen_centroid.equal(oldCentroid[i], centroid[i])));
				same = false;
			}
		return same;
	}
	
	/*分配数据点到哪个簇*/
	public  void assignPoint(int x){
		double minDis = 99999999;
		int minIndex = ju_zhens[x].clusterNum;
		for (int i = 0; i < K; ++i)
		{
//			System.out.println("x:"+x);
			double curDis =  juzhen_centroid.Euclidean(ju_zhens[x], centroid[i]);
	//		System.out.println("i:"+i);
//			System.out.println("curDIs:"+curDis);
	//		System.out.println("minDis:"+minDis);
			
			if (curDis < minDis)
			{
				minDis = curDis;
				minIndex = i;
		//		System.out.println("change");
			}
			
		}
	//	System.out.println("minIndex:"+minIndex);
//		System.out.println(ju_zhens[x].clusterNum );
		ju_zhens[x].clusterNum = minIndex;
//		System.out.println(ju_zhens[x].clusterNum );
	}
	/*分配数据点到哪个簇的接口函数*/
	public  void assign(){
		for (int i = 0; i < N; ++i)
			assignPoint(i);
	}
	public void test(){
		juzhen_centroid j1 = new juzhen_centroid(0,ju_zhens[0]);
		juzhen_centroid j2 = new juzhen_centroid(1,ju_zhens[1]);
		juzhen_centroid.copy(j1, j2);
		
	//	j2.points.get(0).setMean(2);
	//	j1.points.get(0).setMean(1);
		
		System.out.println(j1.points.get(0).getMean());
		System.out.println(j2.points.get(0).getMean());
		System.out.println(juzhen_centroid.equal(j1, j2));
		System.out.println("-------------------------------");
		j1.points.get(0).setMedicine_name("砒霜");
		System.out.println(j1.points.get(0).getMedicine_name());
		System.out.println(j2.points.get(0).getMedicine_name());
		System.out.println("-------------------------------2");
		j2.clusterNum =10;
		System.out.println(j1.clusterNum);
		System.out.println(j2.clusterNum);
	}
	public void test2(){
		juzhen_centroid j1 = new juzhen_centroid(0,ju_zhens[0]);
		juzhen_centroid j2 = new juzhen_centroid(1,ju_zhens[1]);
		juzhen_centroid.copy(j1, j2);
		
	//	j2.points.get(0).setMean(2);
	//	j1.points.get(0).setMean(1);
		
		System.out.println(juzhen_centroid.Euclidean(ju_zhens[1], ju_zhens[10]));
		System.out.println(juzhen_centroid.Euclidean(ju_zhens[1], j1));
		System.out.println(juzhen_centroid.Euclidean(ju_zhens[1], j2));
		System.out.println(juzhen_centroid.Euclidean(ju_zhens[1], j1));
		System.out.println(juzhen_centroid.Euclidean(ju_zhens[3], j2));
		System.out.println(juzhen_centroid.Euclidean(ju_zhens[1], j2));
	
		
	}
	public void test3(){
		juzhen_centroid j1 = new juzhen_centroid();
		juzhen_centroid j2 = new juzhen_centroid();
		j1= juzhen_centroid.juzhen_add(centroid[1],ju_zhens[1],0);
		j2= juzhen_centroid.juzhen_add(j1,ju_zhens[2],0);
		System.out.println(juzhen_centroid.equal(j1, j2));
		System.out.println(j1==j2);
	}
	public void showCentroid() {
		for(int i=0;i<K;i++){
			System.out.println("簇"+i+"中心点：");
			System.out.println("clusterNum:"+centroid[i].clusterNum);
			for(juzhen_point p:centroid[i].points){
				System.out.print("name:"+p.getMedicine_name());
				System.out.println("value:"+p.getMean());
			}
		}
		
	}
	
	public void show_right(){
		
	}
}
public class k_means2{
	
	public   static void main(String[] args){
		k_means_method k2 = new k_means_method();
		k2.init();
		int k =0;
		do{
			k2.assign();
			k2.updateCentroids();
	//		k2.showCentroid();
			System.out.println("次数:"+(++k));
		}while(!k2.stop());
		for(juzhen j:k2.ju_zhens){
			System.out.println(j.file+"属于簇"+j.clusterNum);
			System.out.println(j.file.getName());
		}
		int temp1 = 0,temp2=0,temp3=0;
		int  temp1_1=0,temp2_1=0,temp3_1 =0;
		for(juzhen j:k2.ju_zhens){
			
			if(j.file.getName().startsWith("类别一")){
				temp1++;
			}else if(j.file.getName().startsWith("类别二")){
				temp2++;
			}else if(j.file.getName().startsWith("类别三")){
				temp3++;
			}
			
	          if(j.file.getName().startsWith("类别一")&&(j.clusterNum==0)){
	        	  temp1_1++;
	          }else if(j.file.getName().startsWith("类别二")&&(j.clusterNum==1)){
	        	  temp2_1++;
	          }else if(j.file.getName().startsWith("类别三")&&(j.clusterNum==2)){
	        	  temp3_1++;
	          }
			
		}
		
		System.out.println("类别一正确数为："+temp1_1+"；总数为："+temp1);
		System.out.println("类别二正确数为："+temp2_1+"；总数为："+temp2);
		System.out.println("类别三正确数为："+temp3_1+"；总数为："+temp3);
		System.out.println("正确总数为："+(temp1_1+temp2_1+temp3_1)+"；总数为："+(temp1+temp2+temp3));
//		k2.showCentroid();
//		System.out.println(juzhen_centroid.Euclidean(k2.ju_zhens[0], k2.centroid[0]));
//		System.out.println(juzhen_centroid.Euclidean(k2.ju_zhens[4], k2.centroid[0]));
//		System.out.println(juzhen_centroid.Euclidean(k2.ju_zhens[4], k2.centroid[1]));
//		System.out.println(juzhen_centroid.Euclidean(k2.ju_zhens[4], k2.centroid[2]));
	
//		k2.test2();
		
	}
}