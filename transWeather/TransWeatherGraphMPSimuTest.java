package edu.albany.cs.transWeather;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import edu.albany.cs.apdmIO.APDMInputFormat;
import edu.albany.cs.base.Utils;
import edu.albany.cs.graph.TransWeatherGraph;
import edu.albany.cs.graphMP.GraphMP;
import edu.albany.cs.scoreFuncs.ElevatedMeanScanStat;
import edu.albany.cs.scoreFuncs.Function;
import edu.albany.cs.scoreFuncs.MultiSourceProtestScanStat;

public class TransWeatherGraphMPSimuTest {
	
	public static int verboseLevel = 0;
	
	public static void testSingleFile(String singleFile, String resultFileName) {
		long startTime = System.nanoTime();
		APDMInputFormat apdm = new APDMInputFormat(singleFile);
		TransWeatherGraph graph = new TransWeatherGraph(apdm);
		//TransWeatherGraph graph = new TransWeatherGraph(apdm,"grid");
		if (verboseLevel > 0) {
			System.out.println("X: " + Arrays.toString(Arrays.copyOf(graph.x, 5)));
			System.out.println("mean: " + Arrays.toString(Arrays.copyOf(graph.mu, 5)));
			System.out.println("std: " + Arrays.toString(Arrays.copyOf(graph.sigma, 5)));			
			System.out.println("trueSubGraphSize: " + graph.trueSubGraph.length);
			Utils.stop();
		}

		int s = graph.trueSubGraph.length/10;
		int g = 1;
		double B = s - g + 0.0D;
		int t = 3;
		boolean singleNodeInitial = false;
		
		double[] X = graph.x;
		double[] mean = graph.mu;
		

		if (verboseLevel > 0) {
			
			System.out.println("X: " + Arrays.toString(Arrays.copyOf(X, 10)));
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
		System.out.println("X: " + Arrays.toString(Arrays.copyOf(graph.x, 10)));
		System.out.println("Mu: " + Arrays.toString(Arrays.copyOf(graph.mu, 10)));
		Function func = new ElevatedMeanScanStat(graph.mu, graph.x);
		GraphMP graphMP = new GraphMP(graph, s, g, B, t, singleNodeInitial, func);
		int[] resultNodes = graphMP.resultNodes_Tail;
		int[] trueNodes = graph.trueSubGraph;

		if (verboseLevel > 0) {
			Arrays.sort(resultNodes);
			Arrays.sort(trueNodes);
			System.out.println(resultNodes.length+" "+Arrays.toString(resultNodes));
			System.out.println(trueNodes.length+" "+Arrays.toString(trueNodes));
		}

		int[] intersect = Utils.intersect(resultNodes, trueNodes);
		double precision = (intersect.length * 1.0D / resultNodes.length * 1.0D);
		double recall = (intersect.length * 1.0D / trueNodes.length * 1.0D);
		try {
			if (resultFileName == null) {
			} else {
				FileWriter fileWriter = new FileWriter(resultFileName, true);
				fileWriter.write(singleFile+ " " + precision + " " + recall + "\n");
				fileWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("--------------------------------------------------");
		//System.out.println("q_twitter: " + q_twitter + "    q_news: " + q_news);
		System.out.println("precision: " + precision);
		System.out.println("recall: " + recall);
		System.out.println("running time: " + (System.nanoTime() - startTime) / 1e9);
		System.out.println("--------------------------------------------------");
		System.out.println();
		System.out.println();
	}
	public static void test_Noise_Signal(){
		String folder="data/NYS_Test_Temp_stdSignal2/20160101/";
		for(File apdmFile : new File(folder).listFiles()){
			testSingleFile(folder+apdmFile.getName(), "outputs/ADPM_Noise_Signal_Test_result_STD_Signal2.txt");
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//testSingleFile("data/temperatureData/apdm/testGraph_0.05_20160116.23.hrrr.wrfsfcf00.NYS_TempData.247_257.txt", "outputs/ADPM_Noise_Signal_Test_result.txt");
		//testSingleFile("data/Grid-Data-100/APDM-GridData-100-precen-0.05-noise_0.txt", "outputs/Test_result.txt");
		//testGraph_0.1_20160116.23.hrrr.wrfsfcf00.NYS_TempData.247_257.txt
		test_Noise_Signal();
	}

}
