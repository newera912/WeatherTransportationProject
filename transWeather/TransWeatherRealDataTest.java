package edu.albany.cs.transWeather;

import java.awt.Window;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;

import edu.albany.cs.apdmIO.APDMInputFormat;
import edu.albany.cs.base.Utils;
import edu.albany.cs.graph.TransWeatherGraph;
import edu.albany.cs.graph.TransWeatherRealGraph;
import edu.albany.cs.graphMP.GraphMP;
import edu.albany.cs.scoreFuncs.ElevatedMeanScanStat;
import edu.albany.cs.scoreFuncs.ElevatedMeanScanStat2;
import edu.albany.cs.scoreFuncs.Function;
import edu.albany.cs.scoreFuncs.MultiSourceProtestScanStat;

public class TransWeatherRealDataTest {
	
	public static int verboseLevel = 0;
	
	public static double[] getAvgAbsSlope(double[][] X,int[] S){
		double[] avgSlope=new double[X.length];
		for(int i=0;i<X.length;i++){
			double temp=0.0D;
			for(int j=S[0];j<S[0]+S.length-1;j++){
				//System.out.println(">>"+i+ " " +j+" "+X.length+" "+X[i][j+1]+" "+X[i][j]);
				temp=temp+Math.abs(X[i][j+1]-X[i][j]);				
			}
			avgSlope[i]=temp/(S.length-1);
			//System.out.println("S:"+ArrayUtils.toString(S));
			//System.out.println(avgSlope[i]+" "+temp);
		}
		return avgSlope;
	}
	public static void testSingleFile(String singleFile, String resultFileName) {
		long startTime = System.nanoTime();
		FileWriter fileWriter=null;
		APDMInputFormat apdm = new APDMInputFormat(singleFile);
		TransWeatherRealGraph graph = new TransWeatherRealGraph(apdm);
		
		try {
			fileWriter = new FileWriter(resultFileName, true);
			fileWriter.write("[Score] [Station index] [Time slots] \n");
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

		int s =2;
		int g = 1;
		double B = s - g + 0.0D;
		int t = 3;
		boolean singleNodeInitial = false;
		
		TreeMap<Double,String> resultMap = new TreeMap<Double,String>(Collections.reverseOrder());
		
		double[][] X = graph.x;
		double[] mean = graph.mu;
		double[] XX=null;
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

		
	    

	    int histStaPoint=24;
		int sCount=0;
		int maxWin=72;
		/* Generate all possible window, window_size >=2 and less than maxWin*/
		for(int i=0;i<X[0].length-12;i++){
			for(int j=0;j<i+1;j++){
				if(i-j+1<2 || i-j+1>maxWin){
					continue;
				}
				sCount++;
				
				
				XX=new double[X.length];
				
				int idx=0;
				double[] winNormalizeFactor= new double[X.length];
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
					System.out.println("Temp:"+temp+" "+(S[0]-starIdx)+" "+(i-j+1));					
				}
					
				for(int q=0;q<X.length;q++){
					double temp=0.0D;
					for(int p:S){
						temp=temp+X[q][p]/winNormalizeFactor[q];
					}
					
					XX[q]=temp/(S.length-1);
//					if(q>4 && q<8){
//						//System.out.println("q>>"+q);
//						XX[q]=XX[q];
//					}
				}
				//double[] avgAbsSlope=getAvgAbsSlope(X, S);
				///for(int k=0;k<X.length;k++) win_mean[k]=1.0;
				
				
				
				Function func = new ElevatedMeanScanStat2(XX);
				GraphMP graphMP = new GraphMP(graph, s, g, B, t, singleNodeInitial, func,XX);
				System.out.println("----------------------------------------------\n"+i+" "+j+" "+Arrays.toString(S));
				System.out.println("XX:"+Arrays.toString(XX));
				System.out.println("XXNorm:"+Arrays.toString(winNormalizeFactor));
				System.out.println("Result:"+ArrayUtils.toString(graphMP.resultNodes_Tail));
				System.out.println("Time Slots:"+ArrayUtils.toString(S));
				//Utils.stop();
				//System.out.println(graphMP.funcVal+ " "+"["+ ArrayUtils.toString(graphMP.resultNodes_Tail).replace("{", "").replace("}", "")+"] ["+ArrayUtils.toString(S).replace("{", "").replace("}", "")+"]");
				resultMap.put(graphMP.funcVal,"["+ ArrayUtils.toString(graphMP.resultNodes_Tail).replace("{", "").replace("}", "")+"] ["+ArrayUtils.toString(S).replace("{", "").replace("}", "")+"]");
			
			}//j
		}//i
		
		Iterator<Entry<Double, String>> it = resultMap.entrySet().iterator();
	    Entry<Double, String> entry;
	    int mapCount=0;
	    while(it.hasNext())
	    {
	        entry = it.next();
	        try {
	        	//System.out.println(entry.getKey()+" "+entry.getValue());
				fileWriter.write(entry.getKey()+" "+entry.getValue()+"\n" );			
				mapCount++;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        if(mapCount>99){
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
	public static void testRealData(){
		int count=0;
		for(String type:Arrays.asList("temp")){//,"wind")){		
		String folder="data/mesonet_data/"+type+"_APDM/";
		for(File apdmFile : new File(folder).listFiles()){
			if(count>1){
				count++;
				continue;
				
			}
			String outFile="outputs/mesonetPlots/"+type+"_norm/"+apdmFile.getName();
			System.out.println(type+" "+apdmFile.getName());
			testSingleFile(folder+apdmFile.getName(), outFile);
			count++;
		}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//testSingleFile("data/temperatureData/apdm/testGraph_0.05_20160116.23.hrrr.wrfsfcf00.NYS_TempData.247_257.txt", "outputs/ADPM_Noise_Signal_Test_result.txt");
		//testSingleFile("data/Grid-Data-100/APDM-GridData-100-precen-0.05-noise_0.txt", "outputs/Test_result.txt");
		//testGraph_0.1_20160116.23.hrrr.wrfsfcf00.NYS_TempData.247_257.txt
		testRealData();
	}

}
