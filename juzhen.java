package org.ansj.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.filechooser.FileNameExtensionFilter;




public class juzhen {
    public ArrayList<juzhen_point> points=new ArrayList<juzhen_point>();
  //medicene_names专门用于存储药方的名字，这样能更快的计算几个药方中出现了几个不同的药名，再用于计算维度方便(hashset不会插入相同的)
    public HashSet<String> medicine_names = new HashSet<String>();
	public File file = null;
	public int clusterNum=-1;//簇编号
	
	public juzhen(){
	}
	public juzhen(int clusternum){
		this.clusterNum = clusternum;
	}
	public juzhen(File file) {
		this.file = file;
		set_juzhen();	
	}
	public static double Euclidean(juzhen p1, juzhen p2)
	{
		int dis = 0;
		
		for(juzhen_point point1:p1.points){
			boolean equal = false;
			for(juzhen_point point2:p2.points){
				if(point1.getMedicine_name().equals(point2.getMedicine_name())){
					equal =true;
					dis+=(point1.getMean()-point2.getMean())*(point1.getMean()-point2.getMean());
				}
			}
			if(equal==false){
				dis+=point1.getMean()*point1.getMean();
			}
		}

		for(juzhen_point point1:p2.points){
			boolean equal = false;
			for(juzhen_point point2:p1.points){
				if(point1.getMedicine_name().equals(point2.getMedicine_name())){
					equal =true;
				}
			}
			if(equal==false){
				dis+=point1.getMean()*point1.getMean();
			}
		}
		return Math.sqrt(dis);
	}
	//juzhen和juzhen_centroid的欧式距离求值
	public static double Euclidean(juzhen j1, juzhen_centroid j2){
			//首先把juzhen中有而juzhen_centroid没有的药品添加到juzhen_centroid中，药品的数目设置为0
	
			for(juzhen_point point1:j1.points){
				boolean find = false;
				for(juzhen_point point2:j2.points){
					if(point2.getMedicine_name().equals(point1.getMedicine_name())){
						find = true;
					}
				}
				if(!find){
					juzhen_point point = new juzhen_point(true);
					point.setMedicine_name(point1.getMedicine_name());
					point.setMean(0);
					j2.points.add(point);
					
				}
			}
			int dis = 0;
			for(juzhen_point point1:j2.points){
				boolean find = false;
				for(juzhen_point point2:j1.points){
					if(point1.getMedicine_name().equals(point2.getMedicine_name())){
//						System.out.println(point1.getMedicine_name());
						dis+=(point1.getMean()-point2.getMean())*(point1.getMean()-point2.getMean());
						find = true;
					}
				}
				if(find == false){
					dis+=point1.getMean()*point1.getMean();
				}
			}
	
//			for(juzhen_point p:j2.points){
//				System.out.print("name:"+p.getMedicine_name());
//				System.out.println("value:"+p.getMean());
//			}
//			System.out.println(dis);
			return Math.sqrt(dis);
	}
	
	public   void set_juzhen(){
		//读取病历
		try {
			BufferedReader in ;
			Pattern pattern = Pattern.compile("(([\\W]{1,3})(\\d{1,2}))");	
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
			String s;

			while ((s = in.readLine()) != null)
			{
				if(s=="\n")continue;
				Matcher matcher = pattern.matcher(s);
				
				while (matcher.find())		
				{
					
					//判断刚匹配出来的point在list中是否已经存在(上一行与下一行可能会有相同药品，此时相加)
					boolean find = false;
					if(matcher.group(2)!=null){
							String medicine_name = matcher.group(2).trim();
							medicine_names.add(medicine_name);
							for(juzhen_point p: points){
								if(p.getMedicine_name().equals(medicine_name)){
									p.add_sum(Integer.parseInt(matcher.group(3).trim()));
									p.num_increase();
									find=true;
								}
							}
							//如果矩阵中没有相应的矩阵节点
							if(find==false){	
								    juzhen_point point = new juzhen_point(false);
									point.setMedicine_name(medicine_name);
									point.setSum(Integer.parseInt(matcher.group(3).trim()));
									point.num_increase();
									points.add(point);	
						   }
					}	
			     }
			}
			
			in.close();
			} catch (UnsupportedEncodingException e1) {			
				e1.printStackTrace();
			} catch (IOException e) {					
			   e.printStackTrace();
			}
		
	}
	
	public  void show_juzhen(){
		for(juzhen_point p:points ){
			System.out.println(p.getMedicine_name());
			System.out.println(p.getSum());
			System.out.println(p.getNum());
			System.out.println(p.getMean());
		}
	}
	
	public void show_names(){
		for(String name:medicine_names){
			System.out.println(name);
		}
	}

}
