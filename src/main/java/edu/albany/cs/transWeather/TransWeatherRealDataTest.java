package edu.albany.cs.transWeather;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;

import edu.albany.cs.apdmIO.APDMInputFormat;
import edu.albany.cs.base.Utils;
import edu.albany.cs.graph.TransWeatherRealGraph;
import edu.albany.cs.graphMP.GraphMP;
import edu.albany.cs.scoreFuncs.EMSStat;
import edu.albany.cs.scoreFuncs.EMSStat2;
import edu.albany.cs.scoreFuncs.Function;

//import edu.albany.cs.scoreFuncs.MultiSourceProtestScanStat;

public class TransWeatherRealDataTest {
	
	public static int verboseLevel = 0;
	public static ArrayList<ResultItem> allResultList = new ArrayList<ResultItem>();	
	public static int resIndex=0;
	public static int ss=2;
	public static int maxWinSize=12;
	
	public static void testSingleFile(String singleFile, String resultFileName,String gtFileName,String prf1File) {
		long startTime = System.nanoTime();
		FileWriter fileWriter=null;
		FileWriter prf1Writer=null;
		APDMInputFormat apdm = new APDMInputFormat(singleFile);
		TransWeatherRealGraph graph = new TransWeatherRealGraph(apdm);
		String[] paths=singleFile.split("/");
		String date=paths[paths.length-1].split("_")[0];
		try {
			prf1Writer = new FileWriter(prf1File, false);
			fileWriter = new FileWriter(resultFileName, false);
			//fileWriter.write("[Score] [Station index] [Time slots] \n");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//TransWeatherGraph graph = new TransWeatherGraph(apdm,"grid");
		if (verboseLevel > 0) {
			System.out.println("X: " + Arrays.toString(Arrays.copyOf(graph.x, 5)));
			System.out.println("mean: " + Arrays.toString(Arrays.copyOf(graph.mu, 5)));
			System.out.println("std: " + Arrays.toString(Arrays.copyOf(graph.sigma, 5)));			
			System.out.println("trueSubGraphSize: " + graph.trueSubGraph.length);
			Utils.stop();
		}
		
		/*Graph-MP Parameters*/
		int s =1000;
		int g = 1;
		double B = s - g + 0.0D;
		int t = 3;		
		double[][] X = graph.x;
		double[] mean = graph.mu;
		double[] XX=null;
		
		/*Result and Gound-True */
		//TreeMap<Double,String> resultMap = new TreeMap<Double,String>(Collections.reverseOrder());
		ArrayList<ResultItem> resultList = new ArrayList<ResultItem>();
		ArrayList<ResultItem> trueResult=getGroundTruthRI(gtFileName);
				
		//double[] win_mean=null;

		if (verboseLevel > 0) {
			
			System.out.println("X: " + Arrays.toString(Arrays.copyOf(X[0], 10)));
			System.out.println("Y: " + Arrays.toString(Arrays.copyOf(mean, 10)));
			for (int i : apdm.data.trueSubGraphNodes) {
				System.out.print(X[i] + " ");
			}
			System.out.println();
			for (int i : apdm.data.trueSubGraphNodes) {
				System.out.print(mean[i] + " ");
			}
		
			
			System.out.println();
		}
		
		/*Generate Time Windows Parameters*/
	    int histStaPoint=12;
		int sCount=0;
		int maxWin=36;
		/* Generate all possible time window, window_size >=2 and less than maxWin*/
		for(int i=0;i<X[0].length-12;i++){
			for(int j=0;j<i+1;j++){
				if(i-j+1<2 || i-j+1>maxWin){
					continue;
				}
//				if(i-j+1!=12){
//					continue;
//				}
				sCount++;				
				
				XX=new double[X.length];				
				int idx=0;
				double[] winNormalizeFactor= new double[X.length];
				double[] winMean= new double[X.length];
				int[] S=new int[i-j+1];
				
				for(int k=j;k<i+1;k++){
					S[idx]=k;
					idx++;								
				}				

				/* Set historical starting point*/
				int starIdx=0;
				if(S[0]>histStaPoint){
					starIdx=S[0]-histStaPoint;
				}				
				
				for(int q=0;q<X.length;q++){
					double temp=0.0D;
					for(int p=starIdx;p<S[0];p++){						
						temp=temp+X[q][p];
					}
					if(S[0]==0){						
					winNormalizeFactor[q]=X[q][0];					
					}else{
					winNormalizeFactor[q]=temp/(S[0]-starIdx);
					
					}
					//System.out.println("Temp:"+temp+" "+(S[0]-starIdx)+" "+(i-j+1)+"="+winNormalizeFactor[q]);					
				}
					
				for(int q=0;q<X.length;q++){
					double temp=0.0D;
					for(int p:S){
						temp=temp+Math.abs(1.0D*X[q][p]/winNormalizeFactor[q]);
					}
					
					XX[q]=Math.round(1000*temp/S.length)/1000.0D;
					
				}
				
				/************ Test change rate*************/
				double[] avgAbsSlope=getAvgAbsChange(X, S); //mean[k]/winNormalizeFactor[k]
				//(winNormalizeFactor[k]*XX[k])/mean[k]
				for(int k=0;k<X.length;k++){
					winMean[k]=1;
					//XX[k]=avgAbsSlope[k];
				}
				/************ Test change rate*************/
				
				//para1 : b para2: c
				Function func = new EMSStat(winMean,XX);
				GraphMP graphMP = new GraphMP(graph, s, g, B, t, func,XX);
				
				int debug=0;
				if(debug==1){
				System.out.println("----------------------------------------------\n"+i+" "+j+" "+Arrays.toString(S));
				System.out.println("XX:"+Arrays.toString(XX));
				System.out.println("XXNorm:"+Arrays.toString(winNormalizeFactor));
				System.out.println("Result:"+ArrayUtils.toString(graphMP.resultNodes_Tail));
				System.out.println("Time Slots:"+ArrayUtils.toString(S));
				System.out.println("winMean:"+ArrayUtils.toString(winMean));
				}
				//if(S[0]>185) 				Utils.stop();
				//Utils.stop();
				//System.out.println(graphMP.funcVal+ " "+"["+ ArrayUtils.toString(graphMP.resultNodes_Tail).replace("{", "").replace("}", "")+"] ["+ArrayUtils.toString(S).replace("{", "").replace("}", "")+"]");

				double score=graphMP.funcValue;				
				ArrayList<Integer> Stations=new ArrayList<Integer>();
				ArrayList<Integer> timeSlots=new ArrayList<Integer>();
				
				for(Integer staIdx:graphMP.resultNodes_Tail){
					Stations.add(staIdx);					
				}				
				for(int slotIdx:S){
					timeSlots.add(slotIdx);
				}
				
				ResultItem resItem=new ResultItem(resIndex,score, date, Stations, timeSlots,trueResult);
				resultList.add(resItem);
				resIndex++;
				
			}//j
		}//i
		
		Collections.sort(resultList, new ResultItem().comparator);
		ArrayList<ResultItem> filResultList=filterResult(resultList);
		//Iterator<Entry<Double, String>> it = resultMap.entrySet().iterator();
	    //Entry<Double, String> entry;
	    int mapCount=0;
	    int cutOff=100;
	    //for(int i=0;i<cutOff && i<filResultList.size();i++)
	    for(int i=0;i<cutOff;i++)
	    			
	    {
	        try {
	        	//System.out.println(entry.getKey()+" "+entry.getValue());
				fileWriter.write(filResultList.get(i).score+" "+Arrays.toString(resultList.get(i).Stations.toArray()).replace("{", "").replace("}", "")+" "+Arrays.toString(resultList.get(i).timeSlots.toArray()).replace("{", "").replace("}", "")+" "+date+"\n");//	
				allResultList.add(filResultList.get(i));
				mapCount++;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        if(mapCount>20){
	        	break;
	        }
	                  
	    } 
	    try {
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	 
		System.out.println(sCount+" "+mapCount);		
		System.out.println("running time: " + (System.nanoTime() - startTime) / 1e9);	
	
	}
	
	public static void testSingleFileChangePoint(String singleFile, String resultFileName,String gtFileName,int mwin,int sss) {
		long startTime = System.nanoTime();
		///FileWriter prf1Writer=null;
		APDMInputFormat apdm = new APDMInputFormat(singleFile);
		TransWeatherRealGraph graph = new TransWeatherRealGraph(apdm);
		String[] paths=singleFile.split("/");
		String date=paths[paths.length-1].split("_")[0];

		//TransWeatherGraph graph = new TransWeatherGraph(apdm,"grid");
		if (verboseLevel > 0) {
			System.out.println("X: " + Arrays.toString(Arrays.copyOf(graph.x, 5)));
			System.out.println("mean: " + Arrays.toString(Arrays.copyOf(graph.mu, 5)));
			System.out.println("std: " + Arrays.toString(Arrays.copyOf(graph.sigma, 5)));			
			System.out.println("trueSubGraphSize: " + graph.trueSubGraph.length);
			Utils.stop();
		}
		
		/*Graph-MP Parameters*/
		int s =sss;
		int g = 1;
		double B = s - g + 0.0D;
		int t = 3;	
		
		/*data parameters*/
		double[][] X = graph.x;           //data Matrix, X_ij mean the ith station jth time slot value
		double[] mean = graph.mu;
		double[] c=new double[X.length];  //input value (observed counts or values)
		double[] b= new double[X.length]; // base, all set to zero
		Arrays.fill(b, 1.0D);
		double[] hist_base= new double[X.length];
		int histStaPoint = 6;
		int sCount=0;
		int maxWin=mwin;
		/*Result and Gound-True */
		//TreeMap<Double,String> resultMap = new TreeMap<Double,String>(Collections.reverseOrder());
		ArrayList<ResultItem> resultList = new ArrayList<ResultItem>();
		ArrayList<ResultItem> trueResult=getGroundTruthRI(gtFileName);
				
		//double[] win_mean=null;

		if (verboseLevel > 0) {
			
			System.out.println("X: " + Arrays.toString(Arrays.copyOf(X[0], 10)));
			System.out.println("Y: " + Arrays.toString(Arrays.copyOf(mean, 10)));
			for (int i : apdm.data.trueSubGraphNodes) {
				System.out.print(X[i] + " ");
			}
			System.out.println();
			for (int i : apdm.data.trueSubGraphNodes) {
				System.out.print(mean[i] + " ");
			}
		
			
			System.out.println();
		}
		
		/*Generate Time Windows Parameters*/
	    
		System.out.print("[s=" + s + " MaxWinSize=" + maxWin + "] ");
		/* Generate all possible time window, window_size >=2 and less than maxWin*/
		for (int i = 72; i <X[0].length - 48; i++) {
			for (int j = 72; j < i + 1; j++) {
		
				if(i-j+1<3 || i-j+1>maxWin){
					continue;
				}
//				if(i-j+1!=18){
//					continue;
//				}
				
				sCount++;  //count possible windows 				
				Arrays.fill(c, 0.0D);
				Arrays.fill(hist_base, 0.0D);
				int idx=0;								
				int[] S=new int[i-j+1]; //Window indices set
				
				for(int k=j;k<i+1;k++){
					S[idx]=k;
					idx++;								
				}				

				/* Set historical starting point*/
				int starIdx=0;
				if(S[0]>histStaPoint){
					starIdx=S[0]-histStaPoint;
				}				
				
				/*Calculate historical bases
				 * hist_base=previous_1hour_avg
				 * */
				for(int q=0;q<X.length;q++){					
					double temp=0.0D;
					for(int p=starIdx;p<S[0];p++){						
						temp=temp+X[q][p];
					}
					if(S[0]==0){						
					hist_base[q]=X[q][0];					
					}else{
					hist_base[q]=temp/(S[0]-starIdx);
					
					}
					//System.out.println("Temp:"+temp+" "+(S[0]-starIdx)+" "+(i-j+1)+"="+winNormalizeFactor[q]);					
				}			
				
				
				/**Changing point detection**/
				int index=0;
				double emsScore=Double.NEGATIVE_INFINITY;
				Double[] temp=new Double[4];				
				double tempDiff=0.0D;
				
				double T1Mean=0.0D;
				double T2Mean=0.0D;
				
				for(int q=0;q<X.length;q++){	
					T1Mean=0.0D;
					T2Mean=0.0D;
					tempDiff=0.0D;
					emsScore=Double.NEGATIVE_INFINITY;
					for(int p=1;p<S.length-1;p++){	
						
						temp=EMSScores(X[q],S[0],p,S[S.length-1]);
						
						if(temp[0]>emsScore){
							index=temp[1].intValue();
							//sIndex=temp[4].intValue();
							emsScore=temp[0]; 
							//tempDiff=Math.abs(temp[2]-temp[3]);
							T1Mean=temp[2];
							T2Mean=temp[3];
														
						}
					}
					//System.out.println(">>"+Arrays.toString(temp)+Arrays.toString(S));
					//System.out.println("==>"+sIndex);
					//XX[q]=tempDiff;
					//Math.round(1000*temp/S.length)/1000.0D
					//System.out.println("Mean base diff:- i="+q+" b="+hist_base[q]+" T1M="+Math.round((100*T1Mean))/100.0D+" T2M="+Math.round((100*T2Mean))/100.0D+" D1="+Math.round(100*Math.abs(T1Mean-hist_base[q]))/100.0D+" D2="+ Math.round(100*Math.abs(T2Mean-hist_base[q]))/100.0D+" "+index+" "+ArrayUtils.toString(S));
					tempDiff=Math.abs(T1Mean-hist_base[q])>=Math.abs(T2Mean-hist_base[q])?Math.abs(T1Mean-hist_base[q]):Math.abs(T2Mean-hist_base[q]);
					//System.out.println(tempDiff);
					c[q]=tempDiff;
				}
				
				/***********************************************/
				
				
				//para1 : b para2: c
				Function func = new EMSStat(b,c);
				GraphMP graphMP = new GraphMP(graph, s, g, B, t, func,c);
				//System.out.println("XX:"+Arrays.toString(XX));
				int debug=0;
				if(debug==1){
				System.out.println("----------------------------------------------\n"+i+" "+j+" "+Arrays.toString(S));
				
				System.out.println("XX:"+Arrays.toString(c));
				System.out.println("XXNorm:"+Arrays.toString(hist_base));
				System.out.println("Result:"+ArrayUtils.toString(graphMP.resultNodes_Tail)+" score:"+graphMP.funcValue);
				System.out.println("Time Slots:"+ArrayUtils.toString(S));
				System.out.println("winMean:"+ArrayUtils.toString(b));
				}
				//if(S[0]>185) 				Utils.stop();
				//Utils.stop();
				//System.out.println(graphMP.funcVal+ " "+"["+ ArrayUtils.toString(graphMP.resultNodes_Tail).replace("{", "").replace("}", "")+"] ["+ArrayUtils.toString(S).replace("{", "").replace("}", "")+"]");

				double score=graphMP.funcValue;				
				ArrayList<Integer> Stations=new ArrayList<Integer>();
				ArrayList<Integer> timeSlots=new ArrayList<Integer>();
				
				for(Integer staIdx:graphMP.resultNodes_Tail){
					Stations.add(staIdx);					
				}				
				for(int slotIdx:S){
					timeSlots.add(slotIdx);
				}
				
				ResultItem resItem=new ResultItem(resIndex,score, date, Stations, timeSlots,trueResult,c);
				resultList.add(resItem);
				resIndex++;
				
			}//j
		}//i
		
		Collections.sort(resultList, new ResultItem().comparator);
		ArrayList<ResultItem> filResultList=filterResult(resultList);
		//Iterator<Entry<Double, String>> it = resultMap.entrySet().iterator();
	    //Entry<Double, String> entry;
	    int mapCount=0;
	    int cutOff=100;
	    //for(int i=0;i<cutOff && i<filResultList.size();i++)
	    for(int i=0;i<cutOff && i<filResultList.size();i++)
	    			
	    {
			// fileWriter.write(filResultList.get(i).score+" "+filResultList.get(i).subStatAvg+""+Arrays.toString(resultList.get(i).Stations.toArray()).replace("{",
			// "").replace("}",
			// "")+" "+Arrays.toString(resultList.get(i).timeSlots.toArray()).replace("{",
			// "").replace("}", "")+" "+date+"\n");//
				allResultList.add(filResultList.get(i));
				mapCount++;

	        if(mapCount>19){
	        	break;
	        }
	                  
	    } 


	 
		// System.out.println(sCount+" "+mapCount);
		System.out.print("[running time: " + (System.nanoTime() - startTime)
				/ 1e9 + "]");
	
	}
	
	public static Double[] EMSScores(double[] orgData,int startIdx,int idxT1,int endIdx){
		double[] data=Arrays.copyOfRange(orgData, startIdx,endIdx+1);
		Double[] score=new Double[4];
		double[] T1=Arrays.copyOf(data, idxT1);
		double[] T2=Arrays.copyOfRange(data, idxT1,data.length);
		int[] S=null;
		//int[] S_c=new int[T1.length];
		
//		System.out.println("data: "+data.length+" "+Arrays.toString(data));
//		System.out.println("T1: " +T1.length+" "+Arrays.toString(T1)+" "+StatUtils.mean(T1)+" "+Math.sqrt(StatUtils.variance(T1)));
//		System.out.println("T2: "+T2.length+" "+Arrays.toString(T2)+" "+StatUtils.mean(T2)+" "+Math.sqrt(StatUtils.variance(T2)));
		
		double mu1=StatUtils.mean(T1);
		double mu2=StatUtils.mean(T2);
		double sigma1=Math.sqrt(StatUtils.variance(T1));
		double sigma2=Math.sqrt(StatUtils.variance(T2));
		double[] normT1=new double[T1.length];
		double[] normT2=new double[T2.length];
		double sigma=getCombSigma(data, mu1, mu2, idxT1);
		//System.out.println(">>"+ArrayUtils.toString(data));
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
		score[1]=startIdx+idxT1*1.0D+1.0;
		
	}else{	
		S=new int[T1.length];
		for(int i=0;i<data.length;i++){
			data[i]=Math.round(((data[i]-mu2)/sigma)*100.0D)/100.0D;			
		}
		idx=0;
		for(int i=0;i<T1.length;i++){
			S[idx++]=i;
		}
		score[1]=startIdx+idxT1*1.0D;
		
	}
		score[2]=mu1;
		score[3]=mu2;
		//System.out.println("Norm T1: "+ArrayUtils.toString(normT1));
		//System.out.println("Norm T2: "+ArrayUtils.toString(normT2));
		
		
		//System.out.println("norm data: "+ArrayUtils.toString(data));
		EMSStat2 emsStat=new EMSStat2(data);
		
		score[0]=Math.round(emsStat.getFuncValue(S)*100.0)/100.0;
		//score[1]=Math.round(emsStat.getFuncValue(S_c)*100.0)/100.0;
		//System.out.println("S:=["+(startIdx+S[0])+",.,"+(startIdx+S[S.length-1])+"] EMS:= "+score[0]+" "+Math.round(StatUtils.mean(T1)*100.0D)/100.0+" "+Math.round(Math.sqrt(StatUtils.variance(T1))*100.0D)/100.0+" "+Math.round(StatUtils.mean(T2)*100.0D)/100.0+" "+Math.round(Math.sqrt(StatUtils.variance(T2))*100.0D)/100.0+" "+ArrayUtils.toString(data));
		return score; 
	}
	/*********************************\
	 * Assume exist two changing points
	 * 
	 * 
	\*********************************/
	public static void testSingleFileChangePoint2(String singleFile, String resultFileName,String gtFileName,int mwin,int sss) {
		long startTime = System.nanoTime();

		APDMInputFormat apdm = new APDMInputFormat(singleFile);
		TransWeatherRealGraph graph = new TransWeatherRealGraph(apdm);
		String[] paths=singleFile.split("/");
		String date=paths[paths.length-1].split("_")[0];

		//TransWeatherGraph graph = new TransWeatherGraph(apdm,"grid");
		if (verboseLevel > 0) {
			System.out.println("X: " + Arrays.toString(Arrays.copyOf(graph.x, 5)));
			System.out.println("mean: " + Arrays.toString(Arrays.copyOf(graph.mu, 5)));
			System.out.println("std: " + Arrays.toString(Arrays.copyOf(graph.sigma, 5)));			
			System.out.println("trueSubGraphSize: " + graph.trueSubGraph.length);
			Utils.stop();
		}
		
		/*Graph-MP Parameters*/
		int s =sss;
		int g = 1;
		double B = s - g + 0.0D;
		int t = 3;	
		
		double[][] X = graph.x;
		double[] mean = graph.mu;
		double[] c=new double[X.length];
		double[] b=new double[X.length];
		double[] hist_base= new double[X.length];
		Arrays.fill(b, 1.0D);
		
		int histStaPoint=6;
		int sCount=0;
		int maxWin=mwin;
		
		/*Result and Gound-True */
		//TreeMap<Double,String> resultMap = new TreeMap<Double,String>(Collections.reverseOrder());
		ArrayList<ResultItem> resultList = new ArrayList<ResultItem>();
		
				
		//double[] win_mean=null;

		if (verboseLevel > 0) {
			
			System.out.println("X: " + Arrays.toString(Arrays.copyOf(X[0], 10)));
			System.out.println("Y: " + Arrays.toString(Arrays.copyOf(mean, 10)));
			for (int i : apdm.data.trueSubGraphNodes) {
				System.out.print(X[i] + " ");
			}
			System.out.println();
			for (int i : apdm.data.trueSubGraphNodes) {
				System.out.print(mean[i] + " ");
			}
		
			
			System.out.println();
		}
		System.out.print("[s="+s+" MaxWinSize="+maxWin+"] ");
		/*Generate Time Windows Parameters*/
	   
		/* Generate all possible time window, window_size >=2 and less than maxWin*/
		for (int i = 72; i <X[0].length - 48; i++) {
			for (int j = 72; j < i + 1; j++) {
				if(i-j+1<3 || i-j+1>maxWin){
					continue;
				}
//				if(i-j+1!=12){
//					continue;
//				}
				sCount++;			
				
				Arrays.fill(c, 0.0D);
				Arrays.fill(hist_base, 0.0D);
				
				int idx=0;
				
				//double[] winMean= new double[X.length];
				int[] S=new int[i-j+1];
				
				for(int k=j;k<i+1;k++){
					S[idx]=k;
					idx++;								
				}				

				/* Set historical starting point*/
				int starIdx=0;
				if(S[0]>histStaPoint){
					starIdx=S[0]-histStaPoint;
				}				
				
				for(int q=0;q<X.length;q++){
					double temp=0.0D;
					for(int p=starIdx;p<S[0];p++){						
						temp=temp+X[q][p];
					}
					if(S[0]==0){						
					hist_base[q]=X[q][0];					
					}else{
					hist_base[q]=temp/(S[0]-starIdx);
					
					}
										
				}

		
				/**Changing point detection**/
				int index=0;
				double emsScore=Double.NEGATIVE_INFINITY;
				Double[] temp=new Double[4];
				Double[] temp2=new Double[4];
				double tempDiff=0.0D;
				
				double T1Mean=0.0D;
				double T2Mean=0.0D;
				double T21Mean=0.0D;
				double T3Mean=0.0D;
				
				for(int q=0;q<X.length;q++){	
					T1Mean=0.0D;
					T2Mean=0.0D;
					T21Mean=0.0D;
					T3Mean=0.0D;
					
					emsScore=Double.NEGATIVE_INFINITY;
					for(int p=1;p<S.length-1;p++){	
						for(int w=1;w<=p;w++){
							//EMSScores(X[q],S[0],p,S[S.length-1]);
							//System.out.println("p="+p+" w="+w+Arrays.toString(S));
							//EMSSCores2(data,startIdx,splitIndex,endindex)
							///System.out.println("\n"+S[0]+" "+w+" "+S[p]+" |"+S[w]+" "+p+" "+S[S.length-1]);
							temp=EMSScores2(X[q],S[0],w,S[p]);
							temp2=EMSScores2(X[q],S[w],p-w+1,S[S.length-1]);
							
							//System.out.println(Arrays.toString(temp));
							///System.out.println(Arrays.toString(temp2));
							if(temp[3]-temp2[2]!=0.0D){
								System.out.println(">>>Somthing wrong with S set splitting!!!="+(temp[3]-temp2[2]));
							}
							if(temp[0]+temp2[0]>emsScore){								
								emsScore=temp[0]+temp2[0]; 								
								T1Mean=temp[2];
								T2Mean=temp[3];
								T21Mean=temp2[2];
								T3Mean=temp2[3];
															
							}
						}
					}
					//System.out.println("==>"+sIndex);
					//XX[q]=tempDiff;
					//System.out.println("Mean base diff:- "+q+" "+winNormalizeFactor[q]+" "+T1Mean+" "+T2Mean+" "+Math.abs(T1Mean-winNormalizeFactor[q])+" "+ Math.abs(T2Mean-winNormalizeFactor[q])+" "+Math.abs(T3Mean-winNormalizeFactor[q])+" "+index+" "+ArrayUtils.toString(S));
					tempDiff=Math.max(Math.max(Math.abs(T1Mean-hist_base[q]),Math.abs(T2Mean-hist_base[q])),Math.abs(T3Mean-hist_base[q]));
					//System.out.println(tempDiff);
					
					//tempDiff=Math.max(Math.max(Math.abs(T1Mean-T2Mean),Math.abs(T2Mean-T3Mean)),Math.abs(T3Mean-T1Mean));
					
					
					c[q]=tempDiff;
				}//q
				
				/***********************************************/
				
				
				//para1 : b para2: c
				Function func = new EMSStat(b,c);
				GraphMP graphMP = new GraphMP(graph, s, g, B, t, func,c);
				//System.out.println("XX:"+Arrays.toString(XX));
				int debug=0;
				if(debug==1){
				System.out.println("----------------------------------------------\n"+i+" "+j+" "+Arrays.toString(S));
				System.out.println("XX:"+Arrays.toString(c));
				System.out.println("XXNorm:"+Arrays.toString(hist_base));
				System.out.println("Result:"+ArrayUtils.toString(graphMP.resultNodes_Tail));
				System.out.println("Time Slots:"+ArrayUtils.toString(S));
				;
				}
			
				double score=graphMP.funcValue;				
				ArrayList<Integer> Stations=new ArrayList<Integer>();
				ArrayList<Integer> timeSlots=new ArrayList<Integer>();
				
				for(Integer staIdx:graphMP.resultNodes_Tail){
					Stations.add(staIdx);					
				}				
				for(int slotIdx:S){
					timeSlots.add(slotIdx);
				}
				
				ResultItem resItem=new ResultItem(resIndex,score, date, Stations, timeSlots);
				resultList.add(resItem);
				resIndex++;
				
			}//j
		}//i
		
		Collections.sort(resultList, new ResultItem().comparator);
		ArrayList<ResultItem> filResultList=filterResult(resultList);
		//Iterator<Entry<Double, String>> it = resultMap.entrySet().iterator();
	    //Entry<Double, String> entry;
	    int mapCount=0;
	    int cutOff=100;
	    //for(int i=0;i<cutOff && i<filResultList.size();i++)
	    for(int i=0;i<cutOff && i<filResultList.size();i++)
	    			
	    {
			// System.out.println(entry.getKey()+" "+entry.getValue());
				allResultList.add(filResultList.get(i));
				mapCount++;
				

	        if(mapCount>19){
	        	break;
	        }
	                  
	    } 


	 
		//System.out.print("["+sCount+" "+mapCount+" ");		
		System.out.print("[running time: " + (System.nanoTime() - startTime) / 1e9+"]\n");	
	
	}
	
	public static Double[] EMSScores2(double[] orgData,int startIdx,int idxT1,int endIdx){
		double[] data=Arrays.copyOfRange(orgData, startIdx,endIdx+1);		
		Double[] score=new Double[4];
		if(data.length==2){
			EMSStat2 emsStat2=new EMSStat2(data);
			int[] S=new int[1];
				if(data[0]>data[1]){
					S[0]=0;
					score[0]=Math.round(emsStat2.getFuncValue(S)*100.0)/100.0;
					
				}else{
					S[0]=1;
					score[0]=Math.round(emsStat2.getFuncValue(S)*100.0)/100.0;
				}
				score[1]=1.0D;
				score[2]=data[0];
				score[3]=data[1];
				return score;
			}
		
		double[] T1=Arrays.copyOf(data, idxT1);
		double[] T2=Arrays.copyOfRange(data, idxT1,data.length);
		int[] S=null;
		
		//System.out.println("T1: " +T1.length+" "+Arrays.toString(T1)+" "+StatUtils.mean(T1)+" "+Math.sqrt(StatUtils.variance(T1)));
		//System.out.println("data: "+data.length+" "+Arrays.toString(data)+" "+idxT1+" "+startIdx+" "+endIdx);
		//System.out.println("T2: "+T2.length+" "+Arrays.toString(T2)+" "+StatUtils.mean(T2)+" "+Math.sqrt(StatUtils.variance(T2)));
		
		//int[] S_c=new int[T1.length];
	     
		
		double mu1=StatUtils.mean(T1);
		double mu2=StatUtils.mean(T2);
		double sigma1=Math.sqrt(StatUtils.variance(T1));
		double sigma2=Math.sqrt(StatUtils.variance(T2));
		double[] normT1=new double[T1.length];
		double[] normT2=new double[T2.length];
		double sigma=getCombSigma(data, mu1, mu2, idxT1);
		//System.out.println(">>"+ArrayUtils.toString(data));
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
		score[1]=startIdx+idxT1*1.0D+1.0;
		
	}else{	
		S=new int[T1.length];
		for(int i=0;i<data.length;i++){
			data[i]=Math.round(((data[i]-mu2)/sigma)*100.0D)/100.0D;			
		}
		idx=0;
		for(int i=0;i<T1.length;i++){
			S[idx++]=i;
		}
		score[1]=startIdx+idxT1*1.0D;
		
	}
		score[2]=mu1;
		score[3]=mu2;
		//System.out.println("Norm T1: "+ArrayUtils.toString(normT1));
		//System.out.println("Norm T2: "+ArrayUtils.toString(normT2));
		
		
		//System.out.println("norm data: "+ArrayUtils.toString(data));
		EMSStat2 emsStat=new EMSStat2(data);
		
		score[0]=Math.round(emsStat.getFuncValue(S)*100.0)/100.0;
		//score[1]=Math.round(emsStat.getFuncValue(S_c)*100.0)/100.0;
		//System.out.println("S:=["+(startIdx+S[0])+",.,"+(startIdx+S[S.length-1])+"] EMS:= "+score[0]+" "+Math.round(StatUtils.mean(T1)*100.0D)/100.0+" "+Math.round(Math.sqrt(StatUtils.variance(T1))*100.0D)/100.0+" "+Math.round(StatUtils.mean(T2)*100.0D)/100.0+" "+Math.round(Math.sqrt(StatUtils.variance(T2))*100.0D)/100.0+" "+ArrayUtils.toString(data));
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
	
	public static double[] getAvgAbsChange(double[][] X,int[] S){
		double[] avgChange=new double[X.length];
		for(int i=0;i<X.length;i++){
			double temp=0.0D;
			for(int j=S[0];j<S[0]+S.length-1;j++){
				//System.out.println(">>"+i+ " " +j+" "+X.length+" "+X[i][j+1]+" "+X[i][j]);
				temp=temp+Math.abs(X[i][j+1]-X[i][j]);				
			}
			avgChange[i]=temp/(S.length-1); 
			//System.out.println("S:"+ArrayUtils.toString(S));
			//System.out.println(avgSlope[i]+" "+temp);
		}
		return avgChange;
	}
	
	
	
	
	
	public static int sum(int[] a) {
		int sum = 0;
		for (int i = 0; i < a.length; i++)
			sum += a[i];
		return sum;
	}
	
	public static int sum(int[][] a) {
		int sum = 0;
		for (int i = 0; i < a.length; i++){
			for(int j=0;j<a[0].length;j++){
				sum += a[i][j];
			}
			}
		return sum;
	}
		

	public static ArrayList<ResultItem> getGroundTruthRI(String fileName){
		ArrayList<ResultItem> trueList=new ArrayList<ResultItem>();
		ResultItem rItem=null;
		int index=0;
		try {
			for (String eachLine : Files.readAllLines(Paths.get(fileName))) {
				
				if(eachLine.trim().length()<1){
					break;
				}
				String[] terms=eachLine.split(" ");
				String date=terms[0];
				ArrayList<Integer> Stations=new ArrayList<Integer>();
				ArrayList<Integer> timeSlots=new ArrayList<Integer>();
				for(String str:(terms[1].replace("[", "").replace("]", "")).split(",")){
					Stations.add(Integer.parseInt(str.trim()));
					
				}
				int minIdx=Integer.parseInt(terms[2].replace("[", "").replace("]", "").split(",")[0]);
				int maxIdx=Integer.parseInt(terms[2].replace("[", "").replace("]", "").split(",")[1]);
				for(int i=minIdx;i<=maxIdx;i++){
					timeSlots.add(i);
				}
				
					
				
				rItem=new ResultItem(index, date, Stations, timeSlots);
				trueList.add(rItem);
				index++;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(trueList);
		return trueList;
	}
	
	public static ArrayList<ResultItem> filterResult(ArrayList<ResultItem> result){
		int origSize=result.size();
		ArrayList<ResultItem> filResultItems=result;
		//ArrayList<ResultItem> removeIdxList=new ArrayList<ResultItem>();
		Set<ResultItem> removeIdxList = new HashSet<>();
		for(int i=0;i<filResultItems.size();i++){
			ResultItem itemi=filResultItems.get(i);
			
			for(int j=i+1;j<filResultItems.size();j++){
				ResultItem itemj=filResultItems.get(j);
				if(itemi.index!=itemj.index && itemi.date==itemj.date){
					boolean include=itemi.timeSlots.get(0)<=itemj.timeSlots.get(0) && itemi.timeSlots.get(itemi.timeSlots.size()-1)>=itemj.timeSlots.get(itemj.timeSlots.size()-1);
					if((1.0D*ResultItem.intersection(itemi.timeSlots, itemj.timeSlots).size())/itemi.timeSlots.size()>0.7D || include){
					 //if((1.0D*ResultItem.intersection(itemi.timeSlots, itemj.timeSlots).size())/itemi.timeSlots.size()>0.7D && (1.0D *ResultItem.intersection(itemi.Stations, itemj.Stations).size())>0.0D){
						
						if(itemi.score>=itemj.score){
							//System.out.println("Big i "+i+" "+j+" "+itemi.index+" "+itemj.index+" "+filResultItems.size()+" "+itemi.date+" "+itemj.date);
							
							removeIdxList.add(itemj);
							
						}else{
							System.err.println("Big j "+i+" "+j+" "+itemi.index+" "+itemj.index+" "+filResultItems.size()+" "+itemi.date+" "+itemj.date);
								
							//if(i>=filResultItems.size())
							removeIdxList.add(itemi);
							
						}
						
					}
				}
				
			}//j
		}//i
		
		filResultItems.removeAll(removeIdxList);
		System.out.print("[size: "+origSize+" - "+removeIdxList.size()+" = "+filResultItems.size()+"] ");
		return filResultItems;
	}
	
	
	
	public static void CaseStudy(int maxwin,int sss){
		long startTimeAll = System.nanoTime();
		allResultList = new ArrayList<ResultItem>();
		List<String> caseDates=(List) Arrays.asList("201603","201604","201605","201606","201607","201608","201609","201610","201611","201612");

		//String truthFolder="data/mesonet_data/caseStudyTempGT/";
		//String methodType="CP";
		String methodType = "CP";
		for (String type : Arrays.asList("temp", "temp9", "press", "wind",
				"windDir", "windMax", "rh", "rad")) {// "temp", "temp9",
														// "press", "wind",
														// "windDir", "windMax",
														// "rh", "rad")){
			int count = 0;
		String folder="data/mesonet_data/"+type+"_APDM/";
		//String folder="data/mesonet_data/trans/";
		String fileName="";
		for(File apdmFile : new File(folder).listFiles()){
			fileName=apdmFile.getName();				
			if (!new File(Paths.get("outputs/mesonetPlots/"+type+"_CaseStudy/"+methodType+"/"+sss+"/").toString()).exists()) {
				new File(Paths.get("outputs/mesonetPlots/"+type+"_CaseStudy/"+methodType+"/"+sss+"/").toString()).mkdirs();
				//new File(Paths.get("outputs/mesonetPlots/"+type+"_CaseStudy/"+methodType+"/"+sss+"/prf1/").toString()).mkdirs();
			}			
			if(!caseDates.contains(fileName.split("_")[0].substring(0, 6))){
				continue;
			}
			
			System.out.print(count+" ["+type+" "+fileName+" ");
			String outFile="outputs/mesonetPlots/"+type+"_CaseStudy/"+methodType+"/"+sss+"/"+apdmFile.getName();
			//String prf1File="outputs/mesonetPlots/"+type+"_CaseStudy/"+methodType+"/"+sss+"/prf1/"+fileName;
			if(methodType.equals("CP")){
					System.out.print("CP] ");
				testSingleFileChangePoint(folder+apdmFile.getName(), outFile,"outputs/mesonetPlots/temp_CaseStudy/true_values3.txt",maxwin,sss);
			}else{
				System.out.print ("CP3] ");
				testSingleFileChangePoint2(folder+apdmFile.getName(), outFile,"outputs/mesonetPlots/temp_CaseStudy/true_values3.txt",maxwin,sss);
				
			}
			count++;
		}//file
		
		ArrayList<ResultItem> groundTrueItems=getGroundTruthRI("outputs/mesonetPlots/temp_CaseStudy/true_values3.txt");
		ArrayList<ResultItem> resultItems=new ArrayList<ResultItem>();
		
		FileWriter allWriter = null;
		FileWriter allWriterOut = null;
			String prf1Filepath = sss + "/prf1/" + type + "_" + methodType
					+ "_s_" + sss + "_wMax_" + maxwin
					+ "_filter_TIncld_0.7.txt";
			String resultFilepath = sss + "/" + type + "_" + methodType + "_s_"
					+ sss + "_wMax_" + maxwin + "_filter_TIncld_0.7.txt";
		try {
			//allWriter = new FileWriter("outputs/mesonetPlots/"+type+"_CaseStudy/"+methodType+"/"+prf1Filepath, false);
			allWriterOut = new FileWriter("outputs/mesonetPlots/"+type+"_CaseStudy/"+methodType+"/"+resultFilepath, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Collections.sort(allResultList, new ResultItem().comparator);
	    
	    int mapCountAll=0;
	    for(int i=0;i<allResultList.size();i++)
	    {
	    	ResultItem.printItem(i+1,allResultList.get(i));
	        resultItems.add(allResultList.get(i));
	        try {
				allWriterOut.write(allResultList.get(i).score+" "+allResultList.get(i).subStatAvg+" "+" "+allResultList.get(i).subStatMin+" "+" "+allResultList.get(i).subStatMax+" "+ArrayUtils.toString(allResultList.get(i).Stations)+" "+ArrayUtils.toString(allResultList.get(i).timeSlots)+" "+allResultList.get(i).date+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        mapCountAll++;
	   
	        if(mapCountAll<0){
	        	break;
	        }
	                  
	    } 
	    
//	    try {
//			allWriter.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	    
	   
		
		try {
			allWriterOut.close();
			//allWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(mapCountAll);
		System.out.println("outputs/mesonetPlots/"+type+"_CaseStudy/"+prf1Filepath);
			
		}
			System.out.println("Total running time: " + (System.nanoTime() - startTimeAll) / 1e9);
		
	}
	
	public static void testRealData(){
		//0,1,7,8,11,12,23
		List<Integer> caseIndex=(List) Arrays.asList(0,1,7,8,11,12,23);
		
		int count=0;
		String truthFolder="/Graph-MP/data/mesonet_data/caseStudyTempGT/";
		for(String type:Arrays.asList("temp")){//,"wind")){		
		String folder="data/mesonet_data/"+type+"_APDM/";
		
				for(File apdmFile : new File(folder).listFiles()){
					
						if(!caseIndex.contains(count)){
							count++;
							continue;				
						}
						
						String outFile="outputs/mesonetPlots/"+type+"_norm/"+apdmFile.getName();
						System.out.println(type+" "+apdmFile.getName());
						testSingleFile(folder+apdmFile.getName(), outFile,"","");
						count++;
				}
		
		}//type
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//testSingleFile("data/temperatureData/apdm/testGraph_0.05_20160116.23.hrrr.wrfsfcf00.NYS_TempData.247_257.txt", "outputs/ADPM_Noise_Signal_Test_result.txt");
		//testSingleFile("data/Grid-Data-100/APDM-GridData-100-precen-0.05-noise_0.txt", "outputs/Test_result.txt");
		//testGraph_0.1_20160116.23.hrrr.wrfsfcf00.NYS_TempData.247_257.txt
		List<Integer> maxWin=(List) Arrays.asList(18);//,24,36);//,12,18,24,30,36);
		List<Integer> ss=(List) Arrays.asList(2);
		for(int mwin:maxWin){
			for(int sss:ss){
				CaseStudy(mwin,sss);
			}
		}
		//getGroundTruth("outputs/mesonetPlots/temp_CaseStudy/true_20160301.txt");
	}

}
