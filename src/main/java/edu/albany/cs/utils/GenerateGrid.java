package edu.albany.cs.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.random.RandomDataGenerator;

import edu.albany.cs.apdmIO.APDMInputFormat;
import edu.albany.cs.base.ConnectedComponents;
import edu.albany.cs.base.Edge;

public class GenerateGrid {

	public static void main(String args[]) throws IOException {

		double[] percentageArr = new double[] { 0.05, 0.06, 0.07, 0.08, 0.09, 0.1 };
		int numOfNodes = 10000;
		double alpha = 0.15;
		int[] noiseLevel = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
				23, 24, 25, 26, 27, 28, 29, 30 };
		String folderName = "./simuDataSet/GridDataSet/Grid-Data-10000/";
		generateGridDataWithNoise(percentageArr, numOfNodes, alpha, noiseLevel, folderName);
	}

	public static void test() throws IOException {
		File file = new File("realDataSet/WaterData/source_12500");
		int[] noiseLevelArr = { 0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30 };
		double alpha = 0.15;
		for (File f : file.listFiles()) {
			int noiseLevel = Integer.parseInt(f.getName().split("_")[5].split("\\.")[0]);
			if (noiseLevel == 0) {

				APDMInputFormat apdm = new APDMInputFormat(f);
				int[] trueNodes = apdm.data.trueSubGraphNodes;

				ConnectedComponents cc = new ConnectedComponents(apdm.data.graphAdjList);
				System.out.println("number of connected components is : " + cc.computeCCSubGraph(trueNodes));
				for (int noise : noiseLevelArr) {

					System.out.println("true nodes size : " + trueNodes.length);
					double[] PValue = new double[apdm.data.numNodes];
					for (int i : trueNodes) {
						PValue[i] = generateRandomPValue(alpha, false);
					}
					int[] normalNodes = ArrayUtils.removeElements(
							new RandomDataGenerator().nextPermutation(PValue.length, PValue.length), trueNodes);
					for (int i : normalNodes) {
						PValue[i] = generateRandomPValue(alpha, true);
					}

					// -----------------------------------------------------------------------------------------------------------
					int noiseLevelInTrueNodes = (int) (((noise + 0.0) / 100) * (trueNodes.length));
					int[] alreadyDone = null;
					for (int k = 0; k < noiseLevelInTrueNodes; k++) {
						while (true) {
							int valueToFind = new Random().nextInt(trueNodes.length);
							if (!ArrayUtils.contains(alreadyDone, valueToFind)) {
								PValue[trueNodes[valueToFind]] = generateRandomPValue(alpha, true);
								alreadyDone = ArrayUtils.add(alreadyDone, valueToFind);
								break;
							}
						}
					}
					// -----------------------------------------------------------------------------------------------------------
					int noiseLevelInNormalNodes = (int) (((noise + 0.0) / 100)
							* (PValue.length - trueNodes.length + 0.0));
					alreadyDone = null;
					for (int j = 0; j < noiseLevelInNormalNodes; j++) {
						while (true) {
							int valueToFind = new Random().nextInt(normalNodes.length);
							if (!ArrayUtils.contains(alreadyDone, valueToFind)) {
								PValue[normalNodes[valueToFind]] = generateRandomPValue(alpha, false);
								alreadyDone = ArrayUtils.add(alreadyDone, valueToFind);
								break;
							}
						}
					}

					HashMap<int[], Double> trueSubGraphEdges = new HashMap<int[], Double>();
					for (int i : trueNodes) {
						trueSubGraphEdges.put(new int[] { i, i }, 1.0);
					}
					APDMInputFormat.generateAPDMFile(apdm.data.usedAlgorithm, apdm.data.dataSource, apdm.data.newEdges,
							PValue, null, null, trueSubGraphEdges, "./tmp/APDM-Water-source-12500_time_"
									+ f.getName().split("_")[2] + "_hour_noise_" + noise + ".txt");
				}
			}
		}
		// generateNewTrueSubGraph() ;
	}

	public static void generateNewTrueSubGraph() throws IOException {
		int[] noiseLevel = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
				25, 26, 27, 28, 29, 30 };
		// int[] noiseLevel =
		// {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20} ;
		// noiseLevel = new int[]{0} ;
		double alpha = 0.14;
		int numOfNodes = 400;
		// double[] percentage = new
		// double[]{0.10,0.15,0.20,0.25,0.30,0.40,0.50,0.60,0.70,0.80,0.90} ;
		double[] percentage = new double[] { 0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09, 0.10, 0.11, 0.12,
				0.13, 0.14, 0.15, 0.16, 0.17, 0.180000, 0.1900000, 0.200, 0.2100, 0.220000, 0.230000, 0.24000, 0.25,
				0.260000, 0.270000, 0.2800, 0.29, 0.3, 0.31 };
		percentage = new double[] { 0.10 };
		String folderName = "./simuDataSet/data10";
		/** 5% percentage of the nodes are anomalous nodes */
		generateGridDataWithNoise(percentage, numOfNodes, alpha, noiseLevel, folderName);
	}

	public static void generateGridDataWithNoise(double[] percentageArr, int numOfNodes, double alpha,
			int[] noiseLevelArr, String folderName) throws IOException {
		APDMInputFormat apdm = new APDMInputFormat(
				"simuDataSet/GridDataSet_BackUp/Grid-Data-" + numOfNodes + "/APDM-GridData-" + numOfNodes + "-0.txt");
		String usedAlgorithm = "NULL";
		String dataSource = "GridDataset";
		for (double percentage : percentageArr) {
			int numTrueNodes = (int) (percentage * numOfNodes);
			double[] PValue = new double[apdm.data.numNodes];
			int[] trueNodes = null;
			Arrays.fill(PValue, 1.0);
			ArrayList<Edge> treEdges = randomWalk(apdm.data.graphAdjList, numTrueNodes);

			for (Edge e : treEdges) {
				PValue[e.i] = generateRandomPValue(alpha, false);
				PValue[e.j] = generateRandomPValue(alpha, false);
				if (!ArrayUtils.contains(trueNodes, e.i)) {
					trueNodes = ArrayUtils.add(trueNodes, e.i);
				}
				if (!ArrayUtils.contains(trueNodes, e.j)) {
					trueNodes = ArrayUtils.add(trueNodes, e.j);
				}
			}
			int[] normalNodes = ArrayUtils
					.removeElements(new RandomDataGenerator().nextPermutation(PValue.length, PValue.length), trueNodes);
			for (int i : normalNodes) {
				PValue[i] = generateRandomPValue(alpha, true);
			}

			for (int noiseLevel : noiseLevelArr) {
				double[] copyedPValue = new double[PValue.length];
				System.arraycopy(PValue, 0, copyedPValue, 0, PValue.length);
				// -----------------------------------------------------------------------------------------------------------
				int noiseLevelInTrueNodes = (int) (((noiseLevel + 0.0) / 100) * (trueNodes.length));
				int[] alreadyDone = null;
				for (int k = 0; k < noiseLevelInTrueNodes; k++) {
					while (true) {
						int valueToFind = new Random().nextInt(trueNodes.length);
						if (!ArrayUtils.contains(alreadyDone, valueToFind)) {
							copyedPValue[trueNodes[valueToFind]] = generateRandomPValue(alpha, true);
							alreadyDone = ArrayUtils.add(alreadyDone, valueToFind);
							break;
						}
					}
				}
				// -----------------------------------------------------------------------------------------------------------
				int noiseLevelInNormalNodes = (int) (((noiseLevel + 0.0) / 100)
						* (copyedPValue.length - trueNodes.length + 0.0));
				alreadyDone = null;
				for (int j = 0; j < noiseLevelInNormalNodes; j++) {
					while (true) {
						int valueToFind = new Random().nextInt(normalNodes.length);
						if (!ArrayUtils.contains(alreadyDone, valueToFind)) {
							copyedPValue[normalNodes[valueToFind]] = generateRandomPValue(alpha, false);
							alreadyDone = ArrayUtils.add(alreadyDone, valueToFind);
							break;
						}
					}
				}
				File file = new File(folderName);
				if (!file.exists()) {
					file.mkdir();
				}
				String fileName = folderName + "/APDM-GridData-" + numOfNodes + "-precen-" + percentage + "-noise_"
						+ noiseLevel + ".txt";
				APDMInputFormat.generateAPDMFile(usedAlgorithm, dataSource, apdm.data.newEdges, copyedPValue, null,
						null, treEdges, fileName);
			}
		}

	}

	public static ArrayList<Edge> randomWalk(ArrayList<ArrayList<Integer>> arr, int numTrueNodes) {

		ArrayList<Edge> trueSubGraph = new ArrayList<Edge>();
		Random random = new Random();
		int start = random.nextInt(arr.size());
		int size = numTrueNodes;
		HashSet<Integer> h = new HashSet<Integer>();
		h.add(start);
		int count = 0;
		while (h.size() < size) {
			int next = arr.get(start).get(random.nextInt(arr.get(start).size()));
			h.add(next);
			trueSubGraph.add(new Edge(start, next, count++, 1.00000));
			start = next;
		}
		ArrayList<Edge> reducedTreEdges = new ArrayList<Edge>();
		for (Edge edge : trueSubGraph) {
			if (!checkExist(reducedTreEdges, edge)) {
				reducedTreEdges.add(edge);
			}
		}
		return reducedTreEdges;
	}

	public static boolean checkExist(ArrayList<Edge> trueSubGraph, Edge edge) {
		if (trueSubGraph.isEmpty()) {
			return false;
		}
		for (Edge ed : trueSubGraph) {
			if ((ed.i == edge.i && ed.j == edge.j) || (ed.i == edge.j && ed.j == edge.i)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param alpha
	 * @param normal
	 *            if it is true then return a normal p-value else return an
	 *            abnormal p-value
	 * @return
	 */
	public static double generateRandomPValue(double alpha, boolean normal) {
		Random random = new Random();
		if (normal == true) {
			double normalPValue = 0.0d;
			while (true) {
				double temp = random.nextDouble();
				if (temp > alpha && temp < 1.0) {
					normalPValue = temp;
					break;
				}
			}
			return normalPValue;
		} else {
			double abnormalPValue = 0.0d;
			while (true) {
				double temp = random.nextDouble();
				if (temp <= alpha && temp > 0.0) {
					abnormalPValue = temp;
					break;
				}
			}
			return abnormalPValue;
		}

	}

}
