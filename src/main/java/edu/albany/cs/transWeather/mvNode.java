package edu.albany.cs.transWeather;
import edu.albany.cs.graph.TransWeatherRealGraph;
import edu.albany.cs.scoreFuncs.Function;

/**
 * Multi variable Node class
 * 
 */

public class mvNode {
	public int numNodes = 0;
	public double[][] X = null;
	public double[] mean = null;
	public double[] std = null;
	public double[] hist_base = null;
	public double[] cV = null;
	public String type;
	public int typeID;
	public TransWeatherRealGraph graph;
	public Function func;

	public mvNode(TransWeatherRealGraph twGraph) {
		graph = twGraph;
		X = twGraph.x; // data Matrix, X_ij mean the ith station jth time
						// slot value
		numNodes = X.length;
		mean = twGraph.mu;
		std = twGraph.sigma; // input value (observed counts or values)
		hist_base = new double[numNodes];
		cV = new double[numNodes];
		type = twGraph.type;
		typeID = twGraph.typeID;
		func = null;
	}
}