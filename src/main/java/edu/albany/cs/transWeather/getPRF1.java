package edu.albany.cs.transWeather;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;

import edu.albany.cs.transWeather.ResultItem;
	
public class getPRF1 {
	public int K;
	public double pre;
	public double rec;
	public double fscore;
	public getPRF1(int topK,ArrayList<ResultItem> result,int groundTrueLength){
		this.K=topK;
		PRF1(result,groundTrueLength);
	}

	private void PRF1(ArrayList<ResultItem> result, int groundTrueLength) {
		// TODO Auto-generated method stub
		double interSectNum=0.0D;
		double wrongResultNum=0.0D;
		double rightResultNum=0.0D;
		ArrayList<Double> unDetec=new ArrayList<Double>();
		double[] indicate=new double[groundTrueLength];
		for(int i=0;i<this.K;i++){			
			if(result.get(i).detectIdxList.size()==0){
				
				wrongResultNum+=1.0D;
				continue;
			}
			for(int j:result.get(i).detectIdxList){
				indicate[j]=1.0D;
				rightResultNum+=1.0D;
			}
		}
		for(int i=0;i<indicate.length;i++){
			if(indicate[i]!=1.0D){
				unDetec.add(i*1.0D);
			}
		}
		interSectNum=StatUtils.sum(indicate);
		
		System.out.println("Undetected"+ArrayUtils.toString(unDetec));
		System.out.println(K+" "+interSectNum+" "+wrongResultNum+" "+rightResultNum);
		if(interSectNum==0){
			this.pre=0.001D;
			this.rec=0.001D;
			this.fscore=0.001D;
		}
		
		this.pre=interSectNum/(wrongResultNum+interSectNum);
		this.rec=interSectNum/groundTrueLength;
		
		if(this.pre==0 || this.rec==0){
			this.fscore=0.001D;
		}else{
			this.fscore=(2*this.pre*this.rec)/(this.pre+this.rec);
		}
		
	}
	
	
	

}
