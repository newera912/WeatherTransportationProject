package edu.albany.cs.graphMP;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;

import edu.albany.cs.base.Utils;
import edu.albany.cs.headApprox.HeadApprox;
import edu.albany.cs.tailApprox.TailApprox;
import edu.albany.cs.transWeather.mvNode;

/**
 * Algorithm 1 : Graph-Mp Aglorithm in our IJCAI paper.
 *
 * @author Baojian bzhou6@albany.edu
 */
public class MultiVarGraphMP {

	/** 1Xn dimension, input data */
//	private final double[] c;
	private final int numOfNodes;
	/** graph info */
	private final HashSet<Integer> nodes;
	private final ArrayList<Integer[]> edges;
	private final ArrayList<Double> edgeCosts;
	private final int[] trueSubGraph;
	private ArrayList<mvNode> mvnodes;
	/** the total sparsity of S */
	private final int s;
	/** the maximum number of connected components formed by F */
	private final int g;
	/** bound on the total weight w(F) of edges in the forest F */
	private final double B;
	/** number of iterations */
	private final int t;
	
	//private final Function function;

	/** results */
	public double[] x;
	public int[] resultNodes_supportX;
	public int[] resultNodes_Tail = null;
	public double funcValue = -1.0D;
	public double runTime;

	private int verboseLevel = 0;

						//ArrayList<mvNode> mvNodes, int s,int g, double B, int t
	public MultiVarGraphMP(ArrayList<mvNode> mvNodes, int s,int g, double B, int t) {
		// TODO Auto-generated constructor stub
		this.mvnodes=mvNodes;
		this.numOfNodes=mvNodes.get(0).numNodes;
		
		this.edges = mvNodes.get(0).graph.edges;
		this.edgeCosts = mvNodes.get(0).graph.edgeCosts;
		this.trueSubGraph = mvNodes.get(0).graph.trueSubGraph;
		this.nodes = new HashSet<>();
		this.s = s;
		this.g = g;
		this.B = B;
		this.t = t;		
		x = run();
	}

	private double[] run() {

		long startTime = System.nanoTime();
		double[] x = initializeRandom();
		//System.out.println("initial X: "+ArrayUtils.toString(x));
		ArrayList<Double> fValues = new ArrayList<>();
		for (int i = 0; i < this.t; i++) { // t iterations
			if (verboseLevel > 0) {
				System.out.println("------------iteration: " + i + "------------");
			}
			//fValues.add(function.getFuncValue(x));
			double[] gradientF = getSigmGradiant(x);
			gradientF = normalizeGradient(x, gradientF);
			/** head approximation */
			HeadApprox head = new HeadApprox(edges, edgeCosts, gradientF, s, g, B, trueSubGraph);
			ArrayList<Integer> S = Utils.unionSets(head.bestForest.nodesInF, support(x));
			/** tail approximation */
			double[] b = getSigmaArgMinFx(S);
			TailApprox tail = new TailApprox(edges, edgeCosts, b, s, g, B, trueSubGraph);
			/** calculate x^{i+1} */
			for (int j = 0; j < b.length; j++) {
				x[j] = 0.0D;
			}
			for (int j : tail.bestForest.nodesInF) {
				x[j] = b[j];
			}
			if (verboseLevel > 0) {
				System.out.println("number of head nodes : " + head.bestForest.nodesInF.size()+" "+ArrayUtils.toString(head.bestForest.nodesInF));
				System.out.println("number of tail nodes : " + tail.bestForest.nodesInF.size()+" "+ArrayUtils.toString(tail.bestForest.nodesInF));
			}
			resultNodes_Tail = Utils.getIntArrayFromIntegerList(tail.bestForest.nodesInF);
		}
		resultNodes_supportX = getSupportNodes(x);
		funcValue = getSigmaFuncValue(x);
		runTime = (System.nanoTime() - startTime) / 1e9;
		return x;
	}

	private double getSigmaFuncValue(double[] x) {
		// TODO Auto-generated method stub
		double SigmaFuncValue=0.0D;
		for(mvNode mvn:mvnodes){
			SigmaFuncValue+=mvn.func.getFuncValue(x);
		}
		
		
		return SigmaFuncValue;
	}

	private double[] getSigmaArgMinFx(ArrayList<Integer> S) {
		// TODO Auto-generated method stub
		double[] SigmaArgMinFx=new double[numOfNodes];
		for(mvNode mvn:mvnodes){
			SigmaArgMinFx=sumElementWise(SigmaArgMinFx, mvn.func.getArgMinFx(S));
		}
		
		
		return SigmaArgMinFx;
	}

	/**
	 * in order to fit the gradient, we need to normalize the gradient if the
	 * domain of function is within [0,1]^n
	 * 
	 * @param x
	 *            input vector x
	 * @param gradient
	 *            gradient vector
	 * @return normGradient the normalized gradient
	 */
	private double[] normalizeGradient(double[] x, double[] gradient) {
		double[] normalizedGradient = new double[numOfNodes];
		for (int i = 0; i < numOfNodes; i++) {
			if ((gradient[i] < 0.0D) && (x[i] == 0.0D)) {
				normalizedGradient[i] = 0.0D;
			} else if ((gradient[i] > 0.0D) && (x[i] == 1.0D)) {
				normalizedGradient[i] = 0.0D;
			} else {
				normalizedGradient[i] = gradient[i];
			}
		}
		return normalizedGradient;
	}

	private double[] initializeRandom() {
		double[] x0 = new double[numOfNodes];
		Random rand = new Random();
		while(StatUtils.sum(x0)==0.0D){
		for (int i = 0; i < numOfNodes; i++) {
			if (rand.nextDouble() < 0.5D) {
				x0[i] = 1.0D;
			} else {
				x0[i] = 0.0D;
			}
		}
		}
		return x0;
	}

	/**
	 * get a support of a vector
	 *
	 * @param x
	 *            array x
	 * @return a subset of nodes corresponding the index of vector x with
	 *         entries not equal to zero
	 */
	public ArrayList<Integer> support(double[] x) {
		if (x == null) {
			return null;
		}
		ArrayList<Integer> nodes = new ArrayList<>();
		for (int i = 0; i < x.length; i++) {
			if (x[i] != 0.0D) {
				nodes.add(i);
			}
		}
		return nodes;
	}
	
	public double[] getSigmGradiant(double[] x){
		double[] graient=new double[x.length];
		for(mvNode mvn:mvnodes){
			graient=sumElementWise(graient, mvn.func.getGradient(x));
		}
		
		return graient;
	}
	
	public double[] sumElementWise(double[] A,double[] B){
		double[] C=new double[A.length];
		for (int i = 0; i < A.length; ++i) {
		    C[i] = A[i] + B[i];
		}
		return C;
	}
	/**
	 * get the nodes returned by algorithm
	 *
	 * @param x
	 *            array x
	 * @return the result nodes
	 */
	private int[] getSupportNodes(double[] x) {
		int[] nodes = null;
		for (int i = 0; i < x.length; i++) {
			if (x[i] != 0.0D) {
				/** get nonzero nodes */
				nodes = ArrayUtils.add(nodes, i);
			}
		}
		return nodes;
	}
}
