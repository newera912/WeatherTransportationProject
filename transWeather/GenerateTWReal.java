package edu.albany.cs.transWeather;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;

import edu.albany.cs.base.Edge;

public class GenerateTWReal {

	private int numOfTrueNodes;
	private double restartProb;	
	private ArrayList<ArrayList<Integer>> adj;	
	private int iterations;

	public Double[] dataValue = null;
	public Double[] mean = null;
	public Double[] std = null;
	public ArrayList<Edge> treEdges;
	public double trueSubGraphRatio;
	public GenerateTWReal(ArrayList<ArrayList<Integer>> adj, Double[] dataVlaue,double mean,double std,double restartProb, double trueSubGraphRatio, int iterations) throws NumberFormatException, IOException {
		
		this.dataValue = dataVlaue;		
		this.adj = adj;
		this.numOfTrueNodes = (int) ((dataValue.length + 0.0D) * trueSubGraphRatio);
		this.restartProb = restartProb;
		this.iterations = iterations;
		this.trueSubGraphRatio=trueSubGraphRatio;
		this.mean=new Double[dataVlaue.length];
		this.std=new Double[dataVlaue.length];
		for(int i=0;i<dataVlaue.length;i++){
			this.mean[i]=mean;
			this.std[i]=std;
			
		}
		generateGridDataWithNoise();
	}
	public GenerateTWReal(){
		
	}
	private ArrayList<Edge> getEdgeFromfile(String fileName) throws NumberFormatException, IOException{
		ArrayList<Edge> trueEdges=new ArrayList<Edge>();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String sCurrentLine;
		while((sCurrentLine = br.readLine()) != null){
			int e1=Integer.parseInt(sCurrentLine.split(" ")[0]);
			int e2=Integer.parseInt(sCurrentLine.split(" ")[1]);
			trueEdges.add(new Edge(e1, e2, 1, 1.0D));
		}
		return trueEdges;
	}
	private void generateGridDataWithNoiseThreshold() throws NumberFormatException, IOException {

		int[] trueNodes = null;
		ArrayList<Integer[]> edgeLsitArrayList=new ArrayList<Integer[]>();
		String fileName="data/semi-synthetic-data/semi-data/"+trueSubGraphRatio+".txt";
		while(true){
		boolean flag=false;
		double newsZeroCount=0.0D;
		File f = new File(fileName);
		if(f.exists()) { 
			System.out.println("Exist...."+fileName);
			flag=true;
			treEdges=getEdgeFromfile(fileName);
		}else{
			treEdges = randomWalk(adj, numOfTrueNodes, restartProb, iterations);		 
			 
		}
		for (Edge e : treEdges) {
			if (!ArrayUtils.contains(trueNodes, e.i)) {
				trueNodes = ArrayUtils.add(trueNodes, e.i);
			}
			if (!ArrayUtils.contains(trueNodes, e.j)) {
				trueNodes = ArrayUtils.add(trueNodes, e.j);
			}
		}
		
		for(int i:trueNodes){
			if(dataValue[i]==0.0D)
				newsZeroCount+=1.0;
		}
		if(newsZeroCount/trueNodes.length <=0.3 && !flag){
			FileWriter trEdgeFile = new FileWriter(fileName,false);
			for (Edge e : treEdges) {
				trEdgeFile.write(e.i+" "+e.j+"\n");
				}
			trEdgeFile.close();
			System.out.println("Zero Count Percent:_"+(newsZeroCount*100.0D)/trueNodes.length+"%");
			break;
		}else if(flag){
			break;
		}else{
			System.out.println(">>>Failed Zero Count Percent:_"+(newsZeroCount*100.0D)/trueNodes.length+"%");
		}
		
		}
		
		for (int i : trueNodes) {
		
				//dataValue[i] = (double) new PoissonDistribution(q_news * (double) median_news_counts[i]).sample();
				
				dataValue[i] = (double) new NormalDistribution(this.mean[i],this.std[i]).sample();
		}
		
	}
	private void generateGridDataWithNoise() {

		int[] trueNodes = null;
		treEdges = randomWalk(adj, numOfTrueNodes, restartProb, iterations);
		for (Edge e : treEdges) {
			if (!ArrayUtils.contains(trueNodes, e.i)) {
				trueNodes = ArrayUtils.add(trueNodes, e.i);
			}
			if (!ArrayUtils.contains(trueNodes, e.j)) {
				trueNodes = ArrayUtils.add(trueNodes, e.j);
			}
		}
		System.out.println(ArrayUtils.toString(trueNodes));
		for (int i : trueNodes) {
		
			dataValue[i] = (double) new NormalDistribution(2*this.mean[i],this.std[i]).sample();
			//dataValue[i] = (double) new PoissonDistribution(q_news * (double) median_news_counts[i]).sample();
			
			}
		
	}
	public ArrayList<Edge> randomWalk_interface(ArrayList<ArrayList<Integer>> arr, int numTrueNodes, double restartProb,
			double iterations) {
		return randomWalk(arr, numTrueNodes, restartProb, iterations);
	}
	private ArrayList<Edge> randomWalk(ArrayList<ArrayList<Integer>> arr, int numTrueNodes, double restartProb,
			double iterations) {
		System.out.println("Random Walk Running.....");
		double minCut = Double.MAX_VALUE;
		ArrayList<Edge> bestTrueSubGraph = null;
		for (int i = 0; i < iterations; i++) {
			System.out.println("Random walk:  "+i +"th iteration");
			ArrayList<Edge> trueSubGraph = new ArrayList<Edge>();
			Random random = new Random();
			int start = random.nextInt(arr.size());
			int size = numTrueNodes;
			HashSet<Integer> h = new HashSet<Integer>();
			h.add(start);
			int count = 0;
			int initialStart = start;
			while (h.size() < size) {
				int randomNeibIndex = random.nextInt(arr.get(start).size());
				int next = arr.get(start).get(randomNeibIndex);
				if (h.contains(next)) {
				} else {
					h.add(next);
					trueSubGraph.add(new Edge(start, next, count++, 1.0D));
				}

				BinomialDistribution bi = new BinomialDistribution(1, restartProb);
				if (bi.sample() == 1) {
					start = initialStart;
				} else {
					start = next;
				}
			}
			if (trueSubGraph.size() != (h.size() - 1)) {
				System.out.println("random walk must be wrong... ");
				System.exit(0);
			}
			double cut = getGraphCut(h, arr);
			if (cut < minCut) {
				minCut = cut;
				bestTrueSubGraph = trueSubGraph;
				System.out.println("current best cut: " + minCut);
			}
		}
		return bestTrueSubGraph;
	}

	private double getGraphCut(HashSet<Integer> trueSubGraph, ArrayList<ArrayList<Integer>> arr) {
		double cut = 0.0D;
		for (int i : trueSubGraph) {
			for (int j : arr.get(i)) {
				if (!trueSubGraph.contains(j)) {
					cut += 1.0D;
				}
			}
		}
		return cut;
	}

}
