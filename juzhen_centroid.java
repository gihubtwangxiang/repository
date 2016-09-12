package org.ansj.test;

import java.util.ArrayList;

public class juzhen_centroid extends juzhen {
	public juzhen_centroid(){
		clusterNum = -1;
	}
	public juzhen_centroid(int clusternum,juzhen j) {
		super(clusternum);
		this.points =new ArrayList<juzhen_point>();
		this.clusterNum = clusternum;
	    for(juzhen_point p:j.points){
	    	juzhen_point point_temp = new juzhen_point(true);
			point_temp.setMean(p.getMean());
			point_temp.setMedicine_name(p.getMedicine_name());
		//	add_list.add(point_temp);
	    	this.points.add(point_temp);
	    }
	    for(String name:j.medicine_names){
	    	this.medicine_names.add(name);
	    }
	}
	public  void set_zero(){
		for(juzhen_point p:this.points){
			p.setMean(0);
		}
	}
	//矩阵中心点的相加，药品名两边都存在则药品数量相加，如果只有一边存在则这一边的药品名和药品数量赋给中心点
	public static juzhen_centroid juzhen_add(juzhen_centroid j1,juzhen j2,int clusternum){
			juzhen_centroid j3 =  new juzhen_centroid(clusternum,j2);
			ArrayList<juzhen_point> add_list = new ArrayList<juzhen_point>();
			for(juzhen_point point2:j1.points){
				boolean equal = false;
				for(juzhen_point point3:j3.points){
					if(point3.getMedicine_name().equals(point2.getMedicine_name())){
						point3.setMean(point3.getMean()+point2.getMean());
						equal = true;
					}
				}
				if(equal==false){
					juzhen_point point_temp = new juzhen_point(true);
					point_temp.setMean(point2.getMean());
					point_temp.setMedicine_name(point2.getMedicine_name());
					add_list.add(point_temp);
				}
			}
			j3.points.addAll(add_list);
			return j3;
	 }
	 public void calculte_mean(int clusterSize){
		    for(juzhen_point point1:this.points){
				 point1.setMean(point1.getMean()/clusterSize);
			}
	 }
//	 public static void copy(juzhen_centroid j1,juzhen_centroid j2){
//			j1.points = j2.points;
//			j1.medicine_names = j2.medicine_names;
//			j1.clusterNum = j2.clusterNum;
//		 	j1=j2;
//	}
	public static void copy(juzhen_centroid j1,juzhen_centroid j2){
//		j1.points = j2.points;
		j1.points = new ArrayList<juzhen_point>();
		for(juzhen_point p:j2.points){
			juzhen_point point_temp = new juzhen_point(true);
			point_temp.setMean(p.getMean());
			point_temp.setMedicine_name(p.getMedicine_name());
			
			j1.points.add(point_temp);
		}
		j1.clusterNum = j2.clusterNum;
		j1.medicine_names = j2.medicine_names;
	}
	public static boolean equal(juzhen_centroid j1,juzhen_centroid j2){
//		System.out.println("进入equal");
//		System.out.println(j1.points.get(0).getMedicine_name());
//		System.out.println(j1.points.get(0).getMean());
//		System.out.println(j2.points.get(0).getMedicine_name());
//		System.out.println(j2.points.get(0).getMean());
//		System.out.println("clusterNum equals?"+(j1.clusterNum==j2.clusterNum));
		System.out.println("points equals?"+(is_same(j1.points, j2.points)));
//		System.out.println("medicine_name equals?"+j1.medicine_names.equals(j2.medicine_names));
		if((j1.clusterNum==j2.clusterNum)&&(is_same(j1.points, j2.points))&&(j1.medicine_names.equals(j2.medicine_names))){
//			System.out.println("points equals?"+);
			return true;
		}else  return false;
	}
	//比较arraylist<juzhen_point>是否相同
	public static boolean is_same(ArrayList<juzhen_point> list1,ArrayList<juzhen_point> list2){
		boolean same = true;
		for(juzhen_point point1:list1){
			for(juzhen_point point2 :list2){
				if(point1.getMedicine_name().equals(point2.getMedicine_name())){
				    if(point1.getMean()!=point2.getMean()){
					same = false;
//					System.out.println("MEAN1:"+point1.getMean());
//					System.out.println("MEAN2:"+point2.getMean());
//					System.out.println("mean isnot equal");
				    }
				}
			}
		}
		return same;
		
	}
}
