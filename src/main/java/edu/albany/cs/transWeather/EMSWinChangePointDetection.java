package edu.albany.cs.transWeather;

import java.util.Arrays;
import java.util.function.IntToDoubleFunction;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.stat.StatUtils;

import edu.albany.cs.scoreFuncs.EMSStat;
import edu.albany.cs.scoreFuncs.EMSStat2;

public class EMSWinChangePointDetection {
		
	public static Double[] EMSScores(double[] orgData,int idxT1){
		double[] data=Arrays.copyOf(orgData, orgData.length);
		Double[] score=new Double[2];
		double[] T1=Arrays.copyOf(data, idxT1+1);
		double[] T2=Arrays.copyOfRange(data, idxT1+1,data.length);
		int[] S=null;
		//int[] S_c=new int[T1.length];
		System.out.println(T1.length+" "+Arrays.toString(T1)+" "+StatUtils.mean(T1)+" "+Math.sqrt(StatUtils.variance(T1)));
		System.out.println(T2.length+" "+Arrays.toString(T2)+" "+StatUtils.mean(T2)+" "+Math.sqrt(StatUtils.variance(T2)));
		//System.out.println(T1.length+" "+Arrays.toString(T1));
		double mu1=StatUtils.mean(T1);
		double mu2=StatUtils.mean(T2);
		double sigma1=Math.sqrt(StatUtils.variance(T1));
		double sigma2=Math.sqrt(StatUtils.variance(T2));
		double[] normT1=new double[T1.length];
		double[] normT2=new double[T2.length];
		double sigma=getCombSigma(data, mu1, mu2, idxT1);
		//System.err.println(ArrayUtils.toString(data));
		int idx=0;

		
	if(mu1<mu2){			
		S=new int[data.length-T1.length];
		for(int i=0;i<data.length;i++){
			data[i]=Math.round(((data[i]-mu1)/sigma)*100.0D)/100.0D;			
		}
		idx=0;
		for(int i=T1.length;i<data.length;i++){
			S[idx++]=i;
		}
		score[1]=idxT1*1.0D+1.0;
	}else{	
		S=new int[T1.length];
		for(int i=0;i<data.length;i++){
			data[i]=Math.round(((data[i]-mu2)/sigma)*100.0D)/100.0D;			
		}
		idx=0;
		for(int i=0;i<T1.length;i++){
			S[idx++]=i;
		}
		score[1]=idxT1*1.0D;
	}
	
		//System.out.println("Norm T1: "+ArrayUtils.toString(normT1));
		//System.out.println("Norm T2: "+ArrayUtils.toString(normT2));
		
		
		//System.out.println("norm data: "+ArrayUtils.toString(data));
		EMSStat2 emsStat=new EMSStat2(data);
		
		score[0]=Math.round(emsStat.getFuncValue(S)*100.0)/100.0;
		//score[1]=Math.round(emsStat.getFuncValue(S_c)*100.0)/100.0;
		System.out.println("S:=["+S[0]+",.,"+S[S.length-1]+"] EMS:= "+score[0]+" "+Math.round(StatUtils.mean(T1)*100.0D)/100.0+" "+Math.round(Math.sqrt(StatUtils.variance(T1))*100.0D)/100.0+" "+Math.round(StatUtils.mean(T2)*100.0D)/100.0+" "+Math.round(Math.sqrt(StatUtils.variance(T2))*100.0D)/100.0+" "+ArrayUtils.toString(data));
		return score; 
	}
	
	public static double getCombSigma(double[] data,double mu1,double mu2,int idxT1){
		double sigma=0.0D;
		double temp1=0.0D;
		double temp2=0.0D;
		for(int i=0;i<idxT1;i++){
			temp1+=(data[i]-mu1)*(data[i]-mu1);
		}
		
		for(int i=idxT1;i<data.length;i++){
			temp2+=(data[i]-mu2)*(data[i]-mu2);
		}
		sigma=Math.sqrt((temp1+temp2)/data.length);
		
		return sigma;
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//double[] data={16.05, 16.82, 16.17, 16.00, 16.07, 15.04, 17.77, 12.88, 16.21, 16.61, 17.05, 14.82, 8.15, 7.70, 9.73, 6.89, 7.49, 7.64, 8.56, 9.49, 7.59, 7.66, 8.54, 8.77, 8.40, 7.77, 7.74, 8.77, 7.96, 8.34, 8.74, 8.55, 7.49, 7.68, 6.93, 8.55};
		double[] data={11.07, 9.90, 9.90, 9.81, 7.74, 9.80, 9.54, 9.45, 10.20, 10.57, 11.91, 10.52, 25.29, 25.06, 25.38, 25.46, 24.78, 24.31, 25.14, 25.25, 23.52, 25.74, 24.11, 24.92, 27.08, 24.45, 26.63, 25.09, 25.39, 24.67, 24.24, 25.50, 25.80, 25.38, 25.12, 26.10};
		//double[] data={8.42, 9.65, 8.41, 6.81, 9.31, 5.67, 8.74, 7.39, 8.91, 8.21, 15.86, 18.05, 17.52, 15.63, 15.88, 15.10, 15.08, 14.75, 16.71, 15.90, 15.87, 17.32, 15.84, 16.57, 17.53, 15.79, 16.00, 17.26, 16.55, 16.20, 14.39, 17.16, 18.09, 16.35, 16.71, 15.04};
		//double[] data={8.57, 9.24, 9.31, 7.87, 7.01, 7.76, 7.73, 7.40, 7.20, 8.51, 8.46, 7.75, 18.16, 16.34, 14.58, 14.95, 16.40, 16.05, 15.36, 15.16, 15.42, 17.28, 15.67, 17.15, 16.75, 15.10, 16.39, 15.83, 14.64, 16.69, 16.11, 17.56, 17.21, 16.68, 15.22, 15.92};
		System.out.println(ArrayUtils.toString(data));
		double score=Double.NEGATIVE_INFINITY;
		Double[] temp=new Double[2];
		int index=0;
		for(int i=1;i<data.length-2;i++){
			System.out.println("------------- "+i+" -------------");
			//data=new double[]{8.57, 9.24, 9.31, 7.87, 7.01, 7.76, 7.73, 7.40, 7.20, 8.51, 8.46, 7.75, 18.16, 16.34, 14.58, 14.95, 16.40, 16.05, 15.36, 15.16, 15.42, 17.28, 15.67, 17.15, 16.75, 15.10, 16.39, 15.83, 14.64, 16.69, 16.11, 17.56, 17.21, 16.68, 15.22, 15.92};
			temp=EMSScores(data, i);
			
			if(temp[0]>score){
				index=temp[1].intValue();
				score=temp[0];
			}
		}
		System.out.print("T1:"+(index));
		
			}

}
