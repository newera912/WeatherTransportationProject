package edu.albany.cs.transWeather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.lang3.ArrayUtils;


public class ResultItem {
	public int index;
	public double score;
	public String date;
	public ArrayList<Integer> changedVarList=new ArrayList<Integer>();
	public ArrayList<Integer> Stations;
	public ArrayList<Integer> timeSlots;
	public ArrayList<Integer> detectIdxList=new ArrayList<Integer>();
	public Comparator<ResultItem> comparator=null;
	public ArrayList<Double> c;
	public double subStatAvg=0.0D;
	public double subStatMax=0.0D;
	public double subStatMin=0.0D;
	public double changeDirection=0.0D;
    
	public ResultItem(int index,String date,ArrayList<Integer> Stations,ArrayList<Integer> timeSlots) {
		this.index=index;
		this.score=-1.0;
		this.date=date;
		this.Stations=Stations;
		this.timeSlots=timeSlots;
		this.comparator=new Comparator<ResultItem>() {
	        public int compare(ResultItem item1, ResultItem item2)
	        {
	        	if(item1.score > item2.score){  //larger return 1
	        		return -1;
	        	}else if(item1.score==item2.score){
	        		return 0;
	        	}else{
	            return  1;
	        	}
	        }
	    };
		
	}
	
	//resIndex,score, date, Stations, timeSlots
	public ResultItem(int index,double score,String date,ArrayList<Integer> Stations,ArrayList<Integer> timeSlots) {
		this.index=index;
		this.score=score;
		this.date=date;
		this.Stations=Stations;
		this.timeSlots=timeSlots;
		this.comparator=new Comparator<ResultItem>() {
	        public int compare(ResultItem item1, ResultItem item2)
	        {
	        	if(item1.score > item2.score){  //larger return 1
	        		return -1;
	        	}else if(item1.score==item2.score){
	        		return 0;
	        	}else{
	            return  1;
	        	}
	        }
	    };
		
	}
	public ResultItem(int index,double score,String date,ArrayList<Integer> Stations,ArrayList<Integer> timeSlots,ArrayList<ResultItem> groundTrue) {
		this.index=index;
		this.score=score;
		this.date=date;
		this.Stations=Stations;
		this.timeSlots=timeSlots;
		IsDetected(groundTrue);
		this.comparator=new Comparator<ResultItem>() {
	        public int compare(ResultItem item1, ResultItem item2)
	        {
	        	if(item1.score > item2.score){  //larger return 1
	        		return -1;
	        	}else if(item1.score==item2.score){
	        		return 0;
	        	}else{
	            return  1;
	        	}
	        }
	    };
		//System.out.println(this.detectIdxList);
		
	}
	
	public ResultItem(int index,double score,int[] changedVarList,String date,ArrayList<Integer> Stations,ArrayList<Integer> timeSlots,ArrayList<ResultItem> groundTrue) {
		this.index=index;
		this.score=score;
		this.date=date;
		this.Stations=Stations;
		this.timeSlots=timeSlots;
		for(int var:changedVarList){
			this.changedVarList.add(var);
		}
//		IsDetected(groundTrue);
//		this.comparator=new Comparator<ResultItem>() {
//	        public int compare(ResultItem item1, ResultItem item2)
//	        {
//	        	if(item1.score > item2.score){  //larger return 1
//	        		return -1;
//	        	}else if(item1.score==item2.score){
//	        		return 0;
//	        	}else{
//	            return  1;
//	        	}
//	        }
//	    };
		//System.out.println(this.detectIdxList);
		
	}

	public ResultItem(int index, double score, String date,
			ArrayList<Integer> Stations, ArrayList<Integer> timeSlots,
			double c[]) {
		double tempAvg = 0.0D;
		ArrayList<Integer> tempList = new ArrayList<Integer>();
		this.index = index;
		this.score = score;
		this.date = date;
		this.Stations = Stations;
		this.timeSlots = timeSlots;
		this.comparator = new Comparator<ResultItem>() {
			public int compare(ResultItem item1, ResultItem item2) {
				if (item1.score > item2.score) { // larger return 1
					return -1;
				} else if (item1.score == item2.score) {
					return 0;
				} else {
					return 1;
				}
			}
		};
		this.c = new ArrayList<Double>();
		for (int i : Stations) {
			// System.out.print(c[i] + " ");
			this.c.add((double) Math.round(c[i]));
			tempAvg += c[i];
			tempList.add(i);
		}

		// double avg_change = 1.0D * tempAvg / Stations.size();
		// // Remove the staions changing value is lower than average changing
		// // value;
		// // System.out.println("\nAvg-- " + avg_change);
		// // System.out.println(ArrayUtils.toString(this.Stations));
		// for (int i : tempList) {
		// if (c[i] < avg_change) {
		// this.Stations.remove(new Integer(i));
		// }
		// }
		// System.out.println(ArrayUtils.toString(this.Stations));


	}

	public ResultItem(int index,double score,String date,ArrayList<Integer> Stations,ArrayList<Integer> timeSlots,ArrayList<ResultItem> groundTrue,double c[]) {
		double tempAvg=0.0D;
		ArrayList<Double> sList=new ArrayList<Double>();
		this.index=index;
		this.score=score;
		this.date=date;
		this.Stations=Stations;
		this.timeSlots=timeSlots;
		IsDetected(groundTrue);
		this.comparator=new Comparator<ResultItem>() {
	        public int compare(ResultItem item1, ResultItem item2)
	        {
	        	if(item1.score > item2.score){  //larger return 1
	        		return -1;
	        	}else if(item1.score==item2.score){
	        		return 0;
	        	}else{
	            return  1;
	        	}
	        }
	    };
	    for(int i:Stations){
	    	tempAvg+=c[i];
	    	sList.add(c[i]);
	    }
	    this.subStatAvg=1.0D*tempAvg/Stations.size();
	    this.subStatMin=Collections.min(sList);
	    this.subStatMax=Collections.max(sList);
		//System.out.println(this.detectIdxList);
		
	}

	public ResultItem(){
		this.comparator=new Comparator<ResultItem>() {
	        public int compare(ResultItem item1, ResultItem item2)
	        {
	        	if(item1.score > item2.score){  //larger return 1
	        		return -1;
	        	}else if(item1.score==item2.score){
	        		return 0;
	        	}else{
	            return  1;
	        	}
	        }
	    };
		
	}
	

	public ArrayList<Integer> IsDetected(ArrayList<ResultItem> groundTrue){
		
		for(ResultItem ri:groundTrue){
			//System.out.println(ri.date+" "+this.date);
			if(!ri.date.equals(this.date)){
				//System.out.println(ri.date+" "+this.date);
				continue;
			}else{
				ArrayList<Integer> intList= intersection(ri.Stations,this.Stations);				
				if(intList.size()>0){
					ArrayList<Integer> intSlotList= intersection(ri.timeSlots, this.timeSlots);
					if(intSlotList.size()>0){
						//System.err.println(ri.index+" >>"+ri.timeSlots+ " "+this.timeSlots);
						this.detectIdxList.add(ri.index);
					}
				}
			}
		}
		return Stations;
	}
	 public static <T> ArrayList<T> intersection(ArrayList<T> list1, ArrayList<T> list2) {
		 ArrayList<T> list = new ArrayList<T>();		
	        for (T t : list1) {
	            if(list2.contains(t)) {
	                list.add(t);
	            }
	        }
	        //System.out.println(this.date+" "+list.size()+""+list1 +"¡¡"+list2);
	        return list;
	    }
	
	 public static void printItem(int top,ResultItem resultItem){
		 System.out.print("\nTop#"+top+" "+resultItem.index+" "+resultItem.score+" ");
		 System.out.print(ArrayUtils.toString(resultItem.Stations)+ " ");
		 System.out.print(ArrayUtils.toString(resultItem.timeSlots));
		 System.out.print(ArrayUtils.toString(resultItem.date));
		 
	 }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

	}

}
