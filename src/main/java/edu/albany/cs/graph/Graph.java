package edu.albany.cs.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang3.ArrayUtils;

import edu.albany.cs.apdmIO.APDMInputFormat;

/**
 * @author baojian
 *
 */
public abstract class Graph {

	/** node size of this graph */
	public final int[] V;
	public final int numOfEdges;
	public final int numOfNodes;
	public final ArrayList<ArrayList<Integer>> arrayListAdj;
	public final int[][] integerAdj;
	public final int[] trueSubGraph;
	public final ArrayList<Integer[]> edges;
	public final ArrayList<Double> edgeCosts;

	public Graph(APDMInputFormat apdm) {
		integerAdj = apdm.data.graphAdj;
		arrayListAdj = apdm.data.graphAdjList;
		numOfEdges = apdm.data.numEdges;
		numOfNodes = apdm.data.numNodes;
		edges = apdm.data.intEdges;
		edgeCosts = apdm.data.identityEdgeCosts;

		int[] v = new int[numOfNodes];
		for (int i = 0; i < numOfNodes; i++) {
			v[i] = i;
		}
		V = v;
		trueSubGraph = apdm.data.trueSubGraphNodes;
	}

	public Graph(int n) {
		numOfNodes = n;
		int[] v = new int[numOfNodes];
		for (int i = 0; i < numOfNodes; i++) {
			v[i] = i;
		}
		V = v;
		integerAdj = null;
		arrayListAdj = null;
		numOfEdges = 0;
		edges = null;
		edgeCosts = null;
		trueSubGraph = null;
	}

	private HashMap<Integer, ArrayList<Integer>> getSubGraph(int[] S) {
		HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
		HashSet<Integer> nodes = new HashSet<Integer>();
		for (int i : S) {
			nodes.add(i);
		}
		for (Integer node : nodes) {
			ArrayList<Integer> neis = new ArrayList<Integer>();
			for (int e : this.integerAdj[node]) {
				if (nodes.contains(e)) {
					neis.add(e);
				}
			}
			result.put(node, neis);
		}
		for (int node : S) {
			System.out.println(result.get(node).toString());
		}
		return result;
	}

	public int[] getPruningSubGraph(int[] S) {
		PruningAlgorithm pruning = new PruningAlgorithm(getSubGraph(S));
		return pruning.result;
	}

	@Override
	public String toString() {
		return "Graph [V=" + Arrays.toString(V) + ", numOfEdges=" + numOfEdges + ", numOfNodes=" + numOfNodes
				+ ", arrayListAdj=" + arrayListAdj + ", integerAdj=" + Arrays.toString(integerAdj) + ", trueSubGraph="
				+ Arrays.toString(trueSubGraph) + ", edges=" + edges + ", edgeCosts=" + edgeCosts + "]";
	}

	public class PruningAlgorithm {
		private final HashMap<Integer, ArrayList<Integer>> graph;

		/** the result is a subset of nodes */
		public int[] result;

		PruningAlgorithm(HashMap<Integer, ArrayList<Integer>> graph) {
			this.graph = graph;
			this.result = run();
		}

		private int[] run() {

			HashSet<Integer> result = new HashSet<Integer>();

			HashMap<Integer, ArrayList<Integer>> dynamicGraph = new HashMap<Integer, ArrayList<Integer>>();
			for (Integer key : graph.keySet()) {
				dynamicGraph.put(key, new ArrayList<Integer>(graph.get(key)));
			}
			System.out.println("size before pruning: " + dynamicGraph.size());
			while (true) {
				HashSet<Integer> currentLeaves = getAllLeaves(dynamicGraph);
				System.out.println(currentLeaves.toString());

				if (dynamicGraph.size() <= 1) {
					break;
				}

				if (currentLeaves.isEmpty()) {
					break;
				} else {

					for (Integer node : dynamicGraph.keySet()) {
						ArrayList<Integer> neis = dynamicGraph.get(node);
						ArrayList<Integer> newNeis = new ArrayList<Integer>();
						for (Integer i : neis) {
							if (!currentLeaves.contains(i)) {
								newNeis.add(i);
							}
						}
						dynamicGraph.replace(node, newNeis);
					}
					if (dynamicGraph.size() <= 1) {
						break;
					}
					for (Integer leaf : currentLeaves) {
						System.out.println("leaf: " + leaf);
						dynamicGraph.remove(leaf);
						if (dynamicGraph.size() <= 1) {
							break;
						}
					}
					if (dynamicGraph.size() <= 1) {
						break;
					}
					System.out.println("size after pruning: " + dynamicGraph.size());
				}
			}
			for (Integer node : dynamicGraph.keySet()) {
				result.add(node);
			}
			int[] nodes = null;
			for (int i : result) {
				nodes = ArrayUtils.add(nodes, i);
			}
			return nodes;
		}

		private HashSet<Integer> getAllLeaves(HashMap<Integer, ArrayList<Integer>> graph) {
			System.out.println("----------------");
			HashSet<Integer> leaves = new HashSet<Integer>();
			if (graph.size() >= 1) {
				for (Integer key : graph.keySet()) {
					System.out.println(": " + graph.get(key));
					if (graph.get(key).size() == 1) {
						leaves.add(key);
					}
				}
			}
			return leaves;
		}
	}

}
