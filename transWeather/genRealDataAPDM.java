package edu.albany.cs.transWeather;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.math.*;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.MathUtils;
import org.omg.CORBA.DATA_CONVERSION;
import org.ujmp.core.util.MathUtil;

import edu.albany.cs.apdmIO.APDMInputFormat;
import edu.albany.cs.base.Edge;
import edu.albany.cs.base.Utils;
import edu.albany.cs.transWeather.GenerateTWReal;
import edu.uci.ics.jung.visualization.decorators.EdgeShape.Line;
public class genRealDataAPDM {
	public ArrayList<Edge> edges = new ArrayList<Edge>();
	public ArrayList<int[]> intEdges = new ArrayList<int[]>();

	private genRealDataAPDM() {

	}

	public static void generateTestGraph(String rootFolder, String singleFileName,String adjFile, double restartProb,
			double trueSubGraphRatio, Path resultOriginFilePath,int iterations) {

		try {
			String usedAlgorithm = "NULL";
			String dataSource = "TransWeatherData";
			ArrayList<Edge> edges = new ArrayList<Edge>();
			ArrayList<int[]> intEdges = new ArrayList<int[]>();
			ArrayList<Edge> trueSubGraphEdges = null;
			
			String[] attributeNames = new String[] { "index", "Temperature", "mean","std"};			
			Integer[] indexArr = null;			
			Double[] dataValue = null;
			
					
			//read data
			for (String eachLine : Files.readAllLines(Paths.get(rootFolder,singleFileName))) {

				
				if (eachLine.length()>0) {
					Integer index = Integer.parseInt(eachLine.split(" ")[0]);					
					double value = Double.parseDouble(eachLine.split(" ")[1]);
					//System.out.println(index+" "+value);
					
					indexArr = ArrayUtils.add(indexArr, index);
					dataValue = ArrayUtils.add(dataValue, value);
				}
				else{
					break;
				}
			}
			 DescriptiveStatistics stats = new DescriptiveStatistics();
			 for (int i = 0; i < dataValue.length; i++) {
		            stats.addValue(dataValue[i]);                
		    }
            
             //calculate average value
             double mean = stats.getMean();
             double std=stats.getStandardDeviation();
             
			/* read ajacent list*/
			int id=0;
			for (String eachLine : Files.readAllLines(Paths.get(rootFolder,adjFile))) {

				
				if (eachLine.length()>0) {
					int end0 = Integer.parseInt(eachLine.split(" ")[0]);					
					int end1 = Integer.parseInt(eachLine.split(" ")[1]);
					
					// double weight = eachLine.split(" ")[1];
					edges.add(new Edge(end0, end1, id, 1.0D));
					intEdges.add(new int[] { end0, end1 });
					//System.out.println(end0+" "+end1);
				}
				else{
					break;
				}
				id++;
			}
			System.out.println(intEdges.size());
			ArrayList<ArrayList<Integer>> adj = new ArrayList<>();
			for (int i = 0; i < dataValue.length; i++) {
				adj.add(new ArrayList<>());
			}
			for (int[] edge : intEdges) {
				if (!adj.get(edge[0]).contains(edge[1])) {
					adj.get(edge[0]).add(edge[1]);
				}
				if (!adj.get(edge[1]).contains(edge[0])) {
					adj.get(edge[1]).add(edge[0]);
				}
			}
			System.out.println(adj.get(4));
			//Double[] news_counts_c = Arrays.copyOf(dataValue, dataValue.length);
			

			GenerateTWReal gen = new GenerateTWReal(adj, dataValue,mean,std, restartProb, trueSubGraphRatio, iterations);

			trueSubGraphEdges = gen.treEdges;

			APDMInputFormat.generateAPDMFile(usedAlgorithm, dataSource, edges, trueSubGraphEdges,
					resultOriginFilePath.toString(),attributeNames, gen.dataValue,gen.mean,gen.std);

			
			System.out.println("done");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

/*	public static void generateSingleCase() {
		double restartProb = 0.5;
		double trueSubGraphRatio = 0.05;
		double q_twitter = 100.0D;
		double q_news = 0.01D;
		String rootFolder = "/home/baojian/Dropbox/data/ICDM-2016/mexicoData/";
		String date = "2014-03-02";
		String subFolder = rootFolder + "testGraph-" + date + "_restartProb_" + restartProb + "_trueRatio_"
				+ trueSubGraphRatio;
		if (!new File(subFolder).exists()) {
			new File(subFolder).mkdir();
		}
		String resultFileName = subFolder + "/testGraph_" + date + "_qTwitter_" + q_twitter + "_qNews_" + q_news
				+ "_.txt";
		int iterations = 1000;
		generateTestGraph(rootFolder, date, restartProb, trueSubGraphRatio, q_twitter, q_news,
				Paths.get(resultFileName), null, null, iterations);
	}*/

	public static void generateTestCase() {
		String rootFolder = "F:/workspace/git/TranWeatherProject/data/temperatureData/";
		String singleDatafile = "20160116.23.hrrr.wrfsfcf00.NYS_TempData.247_257.txt";
		String adjFile="NYS_grid_adjList_247X257.txt";
		double[] trueSubGraphRatioArr = { 0.05D,0.10D, 0.15D};
		double[] restartProbArr = {0.001D};
		
		for (double trueSubGraphRatio : trueSubGraphRatioArr) {
			for (double restartProb : restartProbArr) {
				
					System.out.println("--------------------------------------");
					String subFolder = "testGraph-" + singleDatafile.split(".h")[0] + "_restartProb_" + restartProb + "_trueRatio_"
							+ trueSubGraphRatio+"/";
					System.out.println(Paths.get(rootFolder, subFolder).toString());
					if (!new File(Paths.get(rootFolder, subFolder).toString()).exists()) {
						new File(Paths.get(rootFolder, subFolder).toString()).mkdir();
					}
					//double q_news = 1.0D / q_twitter;
					String resultFileNameOrigin = "testGraph_"+trueSubGraphRatio+"_" + singleDatafile;
					System.out.println(resultFileNameOrigin);
					int iterations = 10;
					generateTestGraph(rootFolder, singleDatafile,adjFile, restartProb, trueSubGraphRatio,
							Paths.get(rootFolder,resultFileNameOrigin).toAbsolutePath(),iterations);
				
			}
		}
	}
	public static double[][][] getRawMatrixData(String fileName,int varSize,int n,int m) throws NumberFormatException, IOException{
		
		double[][][] data=new double[varSize][n][m];
		int lineIdx=0;
		int varIdx=0;
		int rowIdx=0;
		
		for (String eachLine : Files.readAllLines(Paths.get(fileName))) {
			//System.out.println("matrix item: "+varIdx);
			int colIdx=0;			
			if (eachLine.length()>0) {
				//double[] values = new double[257];
				for(String strVal:eachLine.split(" ")){
					
					data[varIdx][rowIdx][colIdx++]=Double.parseDouble(strVal);
					};					
				}
			lineIdx++;
			rowIdx++;
			
			if(rowIdx%247==0){
				varIdx++;
				rowIdx=0;
			}
				
			}
			
			
		return data;
		
	}
	
	
	public void getAdjList(String AdjListFile) throws NumberFormatException, IOException{
		//ArrayList<Edge> edges = new ArrayList<Edge>();
		//ArrayList<int[]> intEdges = new ArrayList<int[]>();
		int id=0;
		for (String eachLine : Files.readAllLines(Paths.get(AdjListFile))) {
			
			if (eachLine.length()>0) {
				int end0 = Integer.parseInt(eachLine.split(" ")[0]);					
				int end1 = Integer.parseInt(eachLine.split(" ")[1]);
				
				// double weight = eachLine.split(" ")[1];
				this.edges.add(new Edge(end0, end1, id, 1.0D));
				this.intEdges.add(new int[] { end0, end1 });
				//System.out.println(end0+" "+end1);
			}
			else{
				break;
			}
			id++;
		}
	}
	public double[] getData(String line){
		double[] x=new double[line.split(" ").length-1];
		String[] dStr=line.replace("\n", "").split(" ");
		for(int i=1;i<=dStr.length-1;i++){
			x[i-1]=Double.parseDouble(dStr[i]);			
		}
		return x;
	}
	
	public void genSignleAPDMFile(File rawFile,String rootFolder,String outFolder) throws NumberFormatException, IOException{
		
		
		double[][] data=new double[10][288];
		double[] mean=new double[data.length];
		double[] std=new double[data.length];
		ArrayList<Edge> treEdges=null;
		String usedAlgorithm = "NULL";
		String dataSource = "TransWeatherRealData";		
		String[] attributeNames = new String[] {"Temperatures"," mean"," std "};
		
		if (!new File(Paths.get(outFolder).toString()).exists()) {
			new File(Paths.get(outFolder).toString()).mkdirs();
		}
		
	
	
		System.out.println(rawFile.getName().toString().split("\\.")[0]);
		String fileName=rawFile.getName().split("\\.")[0];
		String outFile=outFolder+fileName+"_APDM.txt";
		int idx=0;
		for (String eachLine : Files.readAllLines(Paths.get(rootFolder+rawFile.getName()))) {
			if(eachLine.trim().length()<1){
				break;
			}
			data[idx]=getData(eachLine);
			idx++;
		}
		
			
		
		//System.out.println(ArrayUtils.toString(data[1]));
		
		
		
			
		
		
		DescriptiveStatistics stats =null; 
		 for (int i = 0; i < data.length; i++) {
			 stats=new DescriptiveStatistics();
			 for(int j=0;j<data[0].length;j++){
				 stats.addValue(data[i][j]); 
			 }
			 mean[i]=stats.getMean();
			 std[i]=stats.getStandardDeviation();
                      
		 }
		 System.out.println(ArrayUtils.toString(mean));
		 System.out.println(ArrayUtils.toString(std));
		 
		//generateAPDMFile(String usedAlgorithm,
//			String dataSource, ArrayList<Edge> edges,
//			ArrayList<Edge> trueSubGraphEdges, String fileName,
//			String[] attributeNames,Double[][] data,Double[] mean, Double[] std)
			APDMInputFormat.generateAPDMFile(usedAlgorithm, dataSource, this.edges, treEdges,
				outFile,attributeNames, data,mean,std);
		 //Utils.stop();

//	

//		
//		

//        //calculate average value
//        double glb_mean = stats.getMean();
//        double glb_std=stats.getStandardDeviation();
//        for(int i=0;i<temperature.length;i++){
//			mean[i]=glb_mean;
//			std[i]=glb_std;
//			
//		}
//        //generate each nodes adjacent node link list
//		ArrayList<ArrayList<Integer>> nodeAdjLink = new ArrayList<>();
//		for (int i = 0; i < temperature.length; i++) {
//			nodeAdjLink.add(new ArrayList<>());
//		}
//		for (int[] edge : intEdges) {
//			if (!nodeAdjLink.get(edge[0]).contains(edge[1])) {
//				nodeAdjLink.get(edge[0]).add(edge[1]);
//			}
//			if (!nodeAdjLink.get(edge[1]).contains(edge[0])) {
//				nodeAdjLink.get(edge[1]).add(edge[0]);
//			}
//		}
//		for(double trueSubRatio:trueSubRatios){
//			int[] trueNodes = null;
//			int numOfTrueNodes=(int)(trueSubRatio*temperature.length);
//			treEdges = generateTWReal.randomWalk_interface(nodeAdjLink, numOfTrueNodes, restartProb, iterations);
//			for (Edge e : treEdges) {
//				if (!ArrayUtils.contains(trueNodes, e.i)) {
//					trueNodes = ArrayUtils.add(trueNodes, e.i);
//				}
//				if (!ArrayUtils.contains(trueNodes, e.j)) {
//					trueNodes = ArrayUtils.add(trueNodes, e.j);
//				}
//			}
//			for(double noiseLevel:noiseLevels){
//				for(double signalLevel:signalLevels){
//					/*1)add signal to true subgraph nodes and then add 2)randomly noise nodes*/
//					signalAddedData=addSignalNoise(temperature,trueNodes,noiseLevel,signalLevel,0,std[0]);
//					String outfileName=outFile+"APDM-"+filePath.split("/")[0]+"_trueRatio_"+trueSubRatio+"_noiseLevel_"+noiseLevel+"_signalLevels_"+signalLevel+".txt";
//					
//					
//				}
//			}
//			
//		}
		
	}
	public void genAllRealDataAPDM() throws NumberFormatException, IOException{
		String rootFolder="data/mesonet_data/wind/";
		//String filePath="20160101/2016-01-01-00-RawHRRRGridData.txt";
		String adjListFile="data/mesonet_data/edgeList.txt";
		String outFolder="data/mesonet_data/wind_APDM/";
		
		/* get adjacent List of the graph */
		getAdjList(adjListFile);
		System.out.println("Edge_size:"+this.edges.size());
		
		for(File rawFile : new File(rootFolder).listFiles()){
			genSignleAPDMFile(rawFile,rootFolder,outFolder);
		}
		
		
	}
	public static void main(String args[]) throws NumberFormatException, IOException {
//		double[][][] data=getRawMatrixData("data/NYS_Raw_Data/20160101/2016-01-01-00-RawHRRRGridData.txt", 11, 247,257);
//		double[][] lat=data[0];
//		double[][] lons=data[1];
//		System.out.println(data[0][0][0]+" "+data[10][246][256]);
//		System.out.println(data[0][0][0]+" "+data[1][0][0]);
//		System.out.println(lat[0][0]+" "+lons[1][0]);
		//generateTestCase();
		genRealDataAPDM tWgen=new genRealDataAPDM();
		tWgen.genAllRealDataAPDM();
	}

}
