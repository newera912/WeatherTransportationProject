package edu.albany.cs.transWeather;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;

import edu.albany.cs.apdmIO.APDMInputFormat;
import edu.albany.cs.base.Utils;
import edu.albany.cs.graph.TransWeatherRealGraph;
import edu.albany.cs.graphMP.GraphMP;
import edu.albany.cs.graphMP.MultiVarGraphMP;
import edu.albany.cs.scoreFuncs.EMSStat;
import edu.albany.cs.scoreFuncs.EMSStat2;
import edu.albany.cs.scoreFuncs.Function;

//import edu.albany.cs.scoreFuncs.MultiSourceProtestScanStat;

public class Multi5VarTransWeather {

	public static int verboseLevel = 0;
	public static ArrayList<ResultItem> allResultList = new ArrayList<ResultItem>();
	public static int resIndex = 0;
	public static Map<String, Double> threshold = new HashMap<String, Double>() {
		{
			put("temp", 9.83);
			put("temp9", 8.99);
			put("wind", 10.64);
			put("windMax", 18.31);
			put("rh", 31.69);
		}
	};

	public static void testSingleFileChangePoint(String[] filePaths,
			String resultFileName, String prf1File,
			int mwin, int sss) {
		long startTime = System.nanoTime();

		ArrayList<mvNode> mvNodes = new ArrayList<mvNode>();
		Multi5VarTransWeather mvTW = new Multi5VarTransWeather();

		MultiVarData mvData = new MultiVarData(filePaths, 5);
		for (Map.Entry<Integer, TransWeatherRealGraph> entry : mvData.twGraphList
				.entrySet()) {
			mvNodes.add(new mvNode(entry.getValue()));
		}

		String[] paths = filePaths[0].split("/");
		String date = paths[paths.length - 1].split("_")[0];

		// TransWeatherGraph graph = new TransWeatherGraph(apdm,"grid");
		if (verboseLevel > 0) {
			for (Map.Entry<Integer, TransWeatherRealGraph> entry : mvData.twGraphList
					.entrySet()) {
				System.out.println("####Date Type: " + entry.getValue().type
						+ " " + entry.getValue().typeID);
				System.out.println("X: "
						+ Arrays.toString(Arrays.copyOf(entry.getValue().x[0],
								5)));
				System.out
						.println("mean: "
								+ Arrays.toString(Arrays.copyOf(
										entry.getValue().mu, 5)));
				System.out.println("std: "
						+ Arrays.toString(Arrays.copyOf(entry.getValue().sigma,
								5)));

				// Utils.stop();
			}
		}

		/* Graph-MP Parameters */
		int s = sss;
		int g = 1;
		double B = s - g + 0.0D;
		int t = 3;

		int N = mvNodes.get(0).numNodes;

		/* data parameters */
		double[] c = new double[N]; // input value (observed counts or values)
		double[] b = new double[N]; // base, all set to zero
		Arrays.fill(b, 1.0D);

		int histStaPoint = 6;
		int sCount = 0;
		int maxWin = mwin;


		ArrayList<ResultItem> resultList = new ArrayList<ResultItem>();


		if (verboseLevel > 0) {

			System.out.println("X: "
					+ Arrays.toString(Arrays.copyOf(mvNodes.get(0).X[0], 10)));
			System.out.println("Y: "
					+ Arrays.toString(Arrays.copyOf(mvNodes.get(0).mean, 10)));

			System.out.println("X: "
					+ Arrays.toString(Arrays.copyOf(mvNodes.get(1).X[0], 10)));
			System.out.println("Y: "
					+ Arrays.toString(Arrays.copyOf(mvNodes.get(1).mean, 10)));

			System.out.println();
		}

		/* Generate Time Windows Parameters */

		System.out.print("[s=" + s + " MaxWinSize=" + maxWin + "] ");
		/*
		 * Generate all possible time window, window_size >=2 and less than
		 * maxWin
		 */
		for (int i = 72; i < mvNodes.get(1).X[0].length - 48; i++) {
			for (int j = 72; j < i + 1; j++) {
				if (i - j + 1 < 3 || i - j + 1 > maxWin) {
					continue;
				}
				// if(i-j+1!=18){
				// continue;
				// }

				sCount++; // count possible windows

				for (mvNode mvn : mvNodes) {
					Arrays.fill(mvn.cV, 0.0D);
					Arrays.fill(mvn.hist_base, 0.0D);
				}

				Arrays.fill(c, 0.0D);

				int idx = 0;
				int[] S = new int[i - j + 1]; // Window indices set

				for (int k = j; k < i + 1; k++) {
					S[idx] = k;
					idx++;
				}

				/* Set historical starting point */
				int starIdx = 0;
				if (S[0] > histStaPoint) {
					starIdx = S[0] - histStaPoint;
				}

				/*
				 * Calculate historical bases hist_base=previous_1hour_avg
				 */
				for (mvNode mvn : mvNodes) {
					for (int q = 0; q < N; q++) {
						double temp = 0.0D;

						for (int p = starIdx; p < S[0]; p++) {
							temp = temp + mvn.X[q][p];

						}
						if (S[0] == 0) {
							mvn.hist_base[q] = mvn.X[q][0];

						} else {
							mvn.hist_base[q] = temp / (S[0] - starIdx);

						}
						// System.out.println("Temp:"+temp+" "+(S[0]-starIdx)+" "+(i-j+1)+"="+winNormalizeFactor[q]);
					}
				}

				/** Changing point detection **/
				int index = 0;

				/* Temp */
				double emsScore = Double.NEGATIVE_INFINITY;
				Double[] tempResult = new Double[4];
				double tempDiff = 0.0D;

				double T1Mean = 0.0D;
				double T2Mean = 0.0D;

				for (int q = 0; q < N; q++) {
					for (mvNode mvn : mvNodes) {
						T1Mean = 0.0D;
						T2Mean = 0.0D;
						tempDiff = 0.0D;
						emsScore = Double.NEGATIVE_INFINITY;

						for (int p = 1; p < S.length - 1; p++) {

							tempResult = EMSScores(mvn.X[q], S[0], p,
									S[S.length - 1]);

							if (tempResult[0] > emsScore) {
								index = tempResult[1].intValue();
								emsScore = tempResult[0];
								T1Mean = tempResult[2];
								T2Mean = tempResult[3];

							}

						}
						tempDiff = Math.abs(T1Mean - mvn.hist_base[q]) >= Math
								.abs(T2Mean - mvn.hist_base[q]) ? Math
								.abs(T1Mean - mvn.hist_base[q]) : Math
								.abs(T2Mean - mvn.hist_base[q]);
						// System.out.println(tempDiff);
						mvn.cV[q] = tempDiff;

					}
					// c[q]=w[0]*mvNodes.get(0).cV[q]+w[1]*mvNodes.get(1).cV[q]+w[2]*mvNodes.get(2).cV[q];
				}// q station

				/***********************************************/
				int[] changedVarList = getChangeVarList(mvNodes, b, s);
				if (changedVarList.length < 1) {
					// System.out.println("No Changes..");
					continue;
				}
				// System.out.println("Before "+mvNodes.size()+" "+
				// changedVarList.length+" "+ArrayUtils.toString(changedVarList));

				ArrayList<mvNode> mvNodesChange = new ArrayList<mvNode>();
				for (int k = 0; k < mvNodes.size(); k++) {
					if (Arrays.asList(ArrayUtils.toObject(changedVarList))
							.contains(mvNodes.get(k).typeID)) {
						mvNodesChange.add(mvNodes.get(k));
					}

				}
				// System.out.println("After "+mvNodesChange.size());
				// para1 : b para2: c
				for (int k = 0; k < mvNodesChange.size(); k++) {
					mvNodesChange.get(k).func = new EMSStat(b,
							mvNodesChange.get(k).cV);

				}

				MultiVarGraphMP graphMP = new MultiVarGraphMP(mvNodesChange, s,
						g, B, t);
				// Utils.stop();

				// Function funcWind = new EMSStat(b,cWind);
				// GraphMP graphMPWind = new GraphMP(graphWind, s, g, B, t,
				// funcWind,cWind);

				// System.out.println("XX:"+Arrays.toString(XX));
				int debug = 0;
				if (debug == 1) {
					System.out
							.println("----------------------------------------------\n"
									+ i + " " + j + " " + Arrays.toString(S));

					System.out.println("XX:" + Arrays.toString(c));
					System.out.println("XXNorm:"
							+ Arrays.toString(mvNodes.get(0).hist_base));
					System.out.println("Result:"
							+ ArrayUtils.toString(graphMP.resultNodes_Tail)
							+ " score:" + graphMP.funcValue);
					System.out.println("Time Slots:" + ArrayUtils.toString(S));
					System.out.println("winMean:" + ArrayUtils.toString(b));
					System.out.println("Var Index: "
							+ ArrayUtils.toString(changedVarList));

				}

				double score = graphMP.funcValue;

				ArrayList<Integer> Stations = new ArrayList<Integer>();
				ArrayList<Integer> timeSlots = new ArrayList<Integer>();

				for (Integer staIdx : graphMP.resultNodes_Tail) {
					Stations.add(staIdx);
				}
				for (int slotIdx : S) {
					timeSlots.add(slotIdx);
				}

				ResultItem resItem = new ResultItem(resIndex, score,
						changedVarList, date, Stations, timeSlots);
				resultList.add(resItem);
				resIndex++;

			}// j
		}// i

		Collections.sort(resultList, new ResultItem().comparator);
		ArrayList<ResultItem> filResultList = filterResult(resultList);

		int mapCount = 0;
		int cutOff = 100;
		// for(int i=0;i<cutOff && i<filResultList.size();i++)
		for (int i = 0; i < cutOff && i < filResultList.size(); i++)

		{
			allResultList.add(filResultList.get(i));
			mapCount++;

			if (mapCount > 19) {
				break;
			}

		}

		System.out.print("[" + sCount + " " + mapCount + "] ");
		System.out.print("Running time: " + (System.nanoTime() - startTime)
				/ 1e9 + "]");

	}

	public static int[] getChangeVarList(ArrayList<mvNode> mvNodes, double[] b,
			int sss) {
		// TODO Auto-generated method stub
		int s = sss;
		int g = 1;
		double B = s - g + 0.0D;
		int t = 3;
		int[] varIdList = null;
		for (mvNode mvn : mvNodes) {
			Function func = new EMSStat(b, mvn.cV);

			GraphMP graphMP = new GraphMP(mvn.graph, s, g, B, t, func, mvn.cV);

			if (graphMP.funcValue >= threshold.get(mvn.type)) {
				varIdList = ArrayUtils.add(varIdList, mvn.typeID);
			}
		}
		if (varIdList == null) {
			varIdList = new int[0];
		}
		return varIdList;
	}

	public static Double[] EMSScores(double[] orgData, int startIdx, int idxT1,
			int endIdx) {
		double[] data = Arrays.copyOfRange(orgData, startIdx, endIdx + 1);
		Double[] score = new Double[4];
		double[] T1 = Arrays.copyOf(data, idxT1);
		double[] T2 = Arrays.copyOfRange(data, idxT1, data.length);
		int[] S = null;
		// int[] S_c=new int[T1.length];

		// System.out.println("data: "+data.length+" "+Arrays.toString(data));
		// System.out.println("T1: "
		// +T1.length+" "+Arrays.toString(T1)+" "+StatUtils.mean(T1)+" "+Math.sqrt(StatUtils.variance(T1)));
		// System.out.println("T2: "+T2.length+" "+Arrays.toString(T2)+" "+StatUtils.mean(T2)+" "+Math.sqrt(StatUtils.variance(T2)));

		double mu1 = StatUtils.mean(T1);
		double mu2 = StatUtils.mean(T2);
		double sigma1 = Math.sqrt(StatUtils.variance(T1));
		double sigma2 = Math.sqrt(StatUtils.variance(T2));
		double[] normT1 = new double[T1.length];
		double[] normT2 = new double[T2.length];
		double sigma = getCombSigma(data, mu1, mu2, idxT1);
		// System.out.println(">>"+ArrayUtils.toString(data));
		int idx = 0;

		if (mu1 < mu2) {
			S = new int[data.length - T1.length];
			for (int i = 0; i < data.length; i++) {
				data[i] = Math.round(((data[i] - mu1) / sigma) * 100.0D) / 100.0D;
			}
			idx = 0;
			for (int i = T1.length; i < data.length; i++) {
				S[idx++] = i;
			}
			score[1] = startIdx + idxT1 * 1.0D + 1.0;

		} else {
			S = new int[T1.length];
			for (int i = 0; i < data.length; i++) {
				data[i] = Math.round(((data[i] - mu2) / sigma) * 100.0D) / 100.0D;
			}
			idx = 0;
			for (int i = 0; i < T1.length; i++) {
				S[idx++] = i;
			}
			score[1] = startIdx + idxT1 * 1.0D;

		}
		score[2] = mu1;
		score[3] = mu2;
		// System.out.println("Norm T1: "+ArrayUtils.toString(normT1));
		// System.out.println("Norm T2: "+ArrayUtils.toString(normT2));

		// System.out.println("norm data: "+ArrayUtils.toString(data));
		EMSStat2 emsStat = new EMSStat2(data);

		score[0] = Math.round(emsStat.getFuncValue(S) * 100.0) / 100.0;
		// score[1]=Math.round(emsStat.getFuncValue(S_c)*100.0)/100.0;
		// System.out.println("S:=["+(startIdx+S[0])+",.,"+(startIdx+S[S.length-1])+"] EMS:= "+score[0]+" "+Math.round(StatUtils.mean(T1)*100.0D)/100.0+" "+Math.round(Math.sqrt(StatUtils.variance(T1))*100.0D)/100.0+" "+Math.round(StatUtils.mean(T2)*100.0D)/100.0+" "+Math.round(Math.sqrt(StatUtils.variance(T2))*100.0D)/100.0+" "+ArrayUtils.toString(data));
		return score;
	}

	/*********************************
	 * \ Assume exist two changing points
	 * 
	 * 
	 * \
	 *********************************/
	public static void testSingleFileChangePoint2(String singleFile,
			String windFile, String resultFileName, String gtFileName,
			String prf1File, int mwin, int sss) {
		long startTime = System.nanoTime();
		FileWriter fileWriter = null;
		FileWriter prf1Writer = null;
		APDMInputFormat apdm = new APDMInputFormat(singleFile);
		TransWeatherRealGraph graph = new TransWeatherRealGraph(apdm);
		String[] paths = singleFile.split("/");
		String date = paths[paths.length - 1].split("_")[0];
		try {
			prf1Writer = new FileWriter(prf1File, false);
			fileWriter = new FileWriter(resultFileName, false);
			// fileWriter.write("[Score] [Station index] [Time slots] \n");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// TransWeatherGraph graph = new TransWeatherGraph(apdm,"grid");
		if (verboseLevel > 0) {
			System.out.println("X: "
					+ Arrays.toString(Arrays.copyOf(graph.x, 5)));
			System.out.println("mean: "
					+ Arrays.toString(Arrays.copyOf(graph.mu, 5)));
			System.out.println("std: "
					+ Arrays.toString(Arrays.copyOf(graph.sigma, 5)));
			System.out
					.println("trueSubGraphSize: " + graph.trueSubGraph.length);
			Utils.stop();
		}

		/* Graph-MP Parameters */
		int s = sss;
		int g = 1;
		double B = s - g + 0.0D;
		int t = 3;

		double[][] X = graph.x;
		double[] mean = graph.mu;
		double[] c = new double[X.length];
		double[] b = new double[X.length];
		double[] hist_base = new double[X.length];
		Arrays.fill(b, 1.0D);

		int histStaPoint = 6;
		int sCount = 0;
		int maxWin = mwin;

		/* Result and Gound-True */
		// TreeMap<Double,String> resultMap = new
		// TreeMap<Double,String>(Collections.reverseOrder());
		ArrayList<ResultItem> resultList = new ArrayList<ResultItem>();
		ArrayList<ResultItem> trueResult = getGroundTruthRI(gtFileName);

		// double[] win_mean=null;

		if (verboseLevel > 0) {

			System.out
					.println("X: " + Arrays.toString(Arrays.copyOf(X[0], 10)));
			System.out
					.println("Y: " + Arrays.toString(Arrays.copyOf(mean, 10)));
			for (int i : apdm.data.trueSubGraphNodes) {
				System.out.print(X[i] + " ");
			}
			System.out.println();
			for (int i : apdm.data.trueSubGraphNodes) {
				System.out.print(mean[i] + " ");
			}

			System.out.println();
		}
		System.out.println("CP3: s=" + s + " MaxWinSize=" + maxWin);
		/* Generate Time Windows Parameters */

		/*
		 * Generate all possible time window, window_size >=2 and less than
		 * maxWin
		 */
		for (int i = 0; i < X[0].length - 12; i++) {
			for (int j = 0; j < i + 1; j++) {
				if (i - j + 1 < 3 || i - j + 1 > maxWin) {
					continue;
				}
				// if(i-j+1!=12){
				// continue;
				// }
				sCount++;

				Arrays.fill(c, 0.0D);
				Arrays.fill(hist_base, 0.0D);

				int idx = 0;

				// double[] winMean= new double[X.length];
				int[] S = new int[i - j + 1];

				for (int k = j; k < i + 1; k++) {
					S[idx] = k;
					idx++;
				}

				/* Set historical starting point */
				int starIdx = 0;
				if (S[0] > histStaPoint) {
					starIdx = S[0] - histStaPoint;
				}

				for (int q = 0; q < X.length; q++) {
					double temp = 0.0D;
					for (int p = starIdx; p < S[0]; p++) {
						temp = temp + X[q][p];
					}
					if (S[0] == 0) {
						hist_base[q] = X[q][0];
					} else {
						hist_base[q] = temp / (S[0] - starIdx);

					}

				}

				/** Changing point detection **/
				int index = 0;
				double emsScore = Double.NEGATIVE_INFINITY;
				Double[] temp = new Double[4];
				Double[] temp2 = new Double[4];
				double tempDiff = 0.0D;

				double T1Mean = 0.0D;
				double T2Mean = 0.0D;
				double T21Mean = 0.0D;
				double T3Mean = 0.0D;

				for (int q = 0; q < X.length; q++) {
					T1Mean = 0.0D;
					T2Mean = 0.0D;
					T21Mean = 0.0D;
					T3Mean = 0.0D;

					emsScore = Double.NEGATIVE_INFINITY;
					for (int p = 1; p < S.length - 1; p++) {
						for (int w = 1; w <= p; w++) {
							// EMSScores(X[q],S[0],p,S[S.length-1]);
							// System.out.println("p="+p+" w="+w+Arrays.toString(S));
							// EMSSCores2(data,startIdx,splitIndex,endindex)
							// /System.out.println("\n"+S[0]+" "+w+" "+S[p]+" |"+S[w]+" "+p+" "+S[S.length-1]);
							temp = EMSScores2(X[q], S[0], w, S[p]);
							temp2 = EMSScores2(X[q], S[w], p - w + 1,
									S[S.length - 1]);

							// System.out.println(Arrays.toString(temp));
							// /System.out.println(Arrays.toString(temp2));
							if (temp[3] - temp2[2] != 0.0D) {
								System.out
										.println(">>>Somthing wrong with S set splitting!!!="
												+ (temp[3] - temp2[2]));
							}
							if (temp[0] + temp2[0] > emsScore) {
								emsScore = temp[0] + temp2[0];
								T1Mean = temp[2];
								T2Mean = temp[3];
								T21Mean = temp2[2];
								T3Mean = temp2[3];

							}
						}
					}
					// System.out.println("==>"+sIndex);
					// XX[q]=tempDiff;
					// System.out.println("Mean base diff:- "+q+" "+winNormalizeFactor[q]+" "+T1Mean+" "+T2Mean+" "+Math.abs(T1Mean-winNormalizeFactor[q])+" "+
					// Math.abs(T2Mean-winNormalizeFactor[q])+" "+Math.abs(T3Mean-winNormalizeFactor[q])+" "+index+" "+ArrayUtils.toString(S));
					tempDiff = Math.max(
							Math.max(Math.abs(T1Mean - hist_base[q]),
									Math.abs(T2Mean - hist_base[q])),
							Math.abs(T3Mean - hist_base[q]));
					// System.out.println(tempDiff);

					// tempDiff=Math.max(Math.max(Math.abs(T1Mean-T2Mean),Math.abs(T2Mean-T3Mean)),Math.abs(T3Mean-T1Mean));

					c[q] = tempDiff;
				}// q

				/***********************************************/

				// para1 : b para2: c
				Function func = new EMSStat(b, c);
				GraphMP graphMP = new GraphMP(graph, s, g, B, t, func, c);
				// System.out.println("XX:"+Arrays.toString(XX));
				int debug = 0;
				if (debug == 1) {
					System.out
							.println("----------------------------------------------\n"
									+ i + " " + j + " " + Arrays.toString(S));
					System.out.println("XX:" + Arrays.toString(c));
					System.out.println("XXNorm:" + Arrays.toString(hist_base));
					System.out.println("Result:"
							+ ArrayUtils.toString(graphMP.resultNodes_Tail));
					System.out.println("Time Slots:" + ArrayUtils.toString(S));
					;
				}

				double score = graphMP.funcValue;
				ArrayList<Integer> Stations = new ArrayList<Integer>();
				ArrayList<Integer> timeSlots = new ArrayList<Integer>();

				for (Integer staIdx : graphMP.resultNodes_Tail) {
					Stations.add(staIdx);
				}
				for (int slotIdx : S) {
					timeSlots.add(slotIdx);
				}

				ResultItem resItem = new ResultItem(resIndex, score, date,
						Stations, timeSlots, trueResult);
				resultList.add(resItem);
				resIndex++;

			}// j
		}// i

		Collections.sort(resultList, new ResultItem().comparator);
		ArrayList<ResultItem> filResultList = filterResult(resultList);
		// Iterator<Entry<Double, String>> it = resultMap.entrySet().iterator();
		// Entry<Double, String> entry;
		int mapCount = 0;
		int cutOff = 100;
		// for(int i=0;i<cutOff && i<filResultList.size();i++)
		for (int i = 0; i < cutOff && i < filResultList.size(); i++)

		{
			try {
				// System.out.println(entry.getKey()+" "+entry.getValue());
				fileWriter
						.write(filResultList.get(i).score
								+ " "
								+ Arrays.toString(
										resultList.get(i).Stations.toArray())
										.replace("{", "").replace("}", "")
								+ " "
								+ Arrays.toString(
										resultList.get(i).timeSlots.toArray())
										.replace("{", "").replace("}", "")
								+ " " + date + "\n");//
				allResultList.add(filResultList.get(i));
				mapCount++;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (mapCount > 19) {
				break;
			}

		}
		try {
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(sCount + " " + mapCount);
		System.out.println("running time: " + (System.nanoTime() - startTime)
				/ 1e9);

	}

	public static Double[] EMSScores2(double[] orgData, int startIdx,
			int idxT1, int endIdx) {
		double[] data = Arrays.copyOfRange(orgData, startIdx, endIdx + 1);
		Double[] score = new Double[4];
		if (data.length == 2) {
			EMSStat2 emsStat2 = new EMSStat2(data);
			int[] S = new int[1];
			if (data[0] > data[1]) {
				S[0] = 0;
				score[0] = Math.round(emsStat2.getFuncValue(S) * 100.0) / 100.0;

			} else {
				S[0] = 1;
				score[0] = Math.round(emsStat2.getFuncValue(S) * 100.0) / 100.0;
			}
			score[1] = 1.0D;
			score[2] = data[0];
			score[3] = data[1];
			return score;
		}

		double[] T1 = Arrays.copyOf(data, idxT1);
		double[] T2 = Arrays.copyOfRange(data, idxT1, data.length);
		int[] S = null;

		// System.out.println("T1: "
		// +T1.length+" "+Arrays.toString(T1)+" "+StatUtils.mean(T1)+" "+Math.sqrt(StatUtils.variance(T1)));
		// System.out.println("data: "+data.length+" "+Arrays.toString(data)+" "+idxT1+" "+startIdx+" "+endIdx);
		// System.out.println("T2: "+T2.length+" "+Arrays.toString(T2)+" "+StatUtils.mean(T2)+" "+Math.sqrt(StatUtils.variance(T2)));

		// int[] S_c=new int[T1.length];

		double mu1 = StatUtils.mean(T1);
		double mu2 = StatUtils.mean(T2);
		double sigma1 = Math.sqrt(StatUtils.variance(T1));
		double sigma2 = Math.sqrt(StatUtils.variance(T2));
		double[] normT1 = new double[T1.length];
		double[] normT2 = new double[T2.length];
		double sigma = getCombSigma(data, mu1, mu2, idxT1);
		// System.out.println(">>"+ArrayUtils.toString(data));
		int idx = 0;

		if (mu1 < mu2) {
			S = new int[data.length - T1.length];
			for (int i = 0; i < data.length; i++) {
				data[i] = Math.round(((data[i] - mu1) / sigma) * 100.0D) / 100.0D;
			}
			idx = 0;
			for (int i = T1.length; i < data.length; i++) {
				S[idx++] = i;
			}
			score[1] = startIdx + idxT1 * 1.0D + 1.0;

		} else {
			S = new int[T1.length];
			for (int i = 0; i < data.length; i++) {
				data[i] = Math.round(((data[i] - mu2) / sigma) * 100.0D) / 100.0D;
			}
			idx = 0;
			for (int i = 0; i < T1.length; i++) {
				S[idx++] = i;
			}
			score[1] = startIdx + idxT1 * 1.0D;

		}
		score[2] = mu1;
		score[3] = mu2;
		// System.out.println("Norm T1: "+ArrayUtils.toString(normT1));
		// System.out.println("Norm T2: "+ArrayUtils.toString(normT2));

		// System.out.println("norm data: "+ArrayUtils.toString(data));
		EMSStat2 emsStat = new EMSStat2(data);

		score[0] = Math.round(emsStat.getFuncValue(S) * 100.0) / 100.0;
		// score[1]=Math.round(emsStat.getFuncValue(S_c)*100.0)/100.0;
		// System.out.println("S:=["+(startIdx+S[0])+",.,"+(startIdx+S[S.length-1])+"] EMS:= "+score[0]+" "+Math.round(StatUtils.mean(T1)*100.0D)/100.0+" "+Math.round(Math.sqrt(StatUtils.variance(T1))*100.0D)/100.0+" "+Math.round(StatUtils.mean(T2)*100.0D)/100.0+" "+Math.round(Math.sqrt(StatUtils.variance(T2))*100.0D)/100.0+" "+ArrayUtils.toString(data));
		return score;
	}

	public static double getCombSigma(double[] data, double mu1, double mu2,
			int idxT1) {
		double sigma = 0.0D;
		double temp1 = 0.0D;
		double temp2 = 0.0D;
		for (int i = 0; i < idxT1; i++) {
			temp1 += (data[i] - mu1) * (data[i] - mu1);
		}

		for (int i = idxT1; i < data.length; i++) {
			temp2 += (data[i] - mu2) * (data[i] - mu2);
		}
		sigma = Math.sqrt((temp1 + temp2) / data.length);

		return sigma;

	}

	public static double[] getAvgAbsChange(double[][] X, int[] S) {
		double[] avgChange = new double[X.length];
		for (int i = 0; i < X.length; i++) {
			double temp = 0.0D;
			for (int j = S[0]; j < S[0] + S.length - 1; j++) {
				// System.out.println(">>"+i+ " "
				// +j+" "+X.length+" "+X[i][j+1]+" "+X[i][j]);
				temp = temp + Math.abs(X[i][j + 1] - X[i][j]);
			}
			avgChange[i] = temp / (S.length - 1);
			// System.out.println("S:"+ArrayUtils.toString(S));
			// System.out.println(avgSlope[i]+" "+temp);
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
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				sum += a[i][j];
			}
		}
		return sum;
	}

	public static ArrayList<ResultItem> getGroundTruthRI(String fileName) {
		ArrayList<ResultItem> trueList = new ArrayList<ResultItem>();
		ResultItem rItem = null;
		int index = 0;
		try {
			for (String eachLine : Files.readAllLines(Paths.get(fileName))) {

				if (eachLine.trim().length() < 1) {
					break;
				}
				String[] terms = eachLine.split(" ");
				String date = terms[0];
				ArrayList<Integer> Stations = new ArrayList<Integer>();
				ArrayList<Integer> timeSlots = new ArrayList<Integer>();
				for (String str : (terms[1].replace("[", "").replace("]", ""))
						.split(",")) {
					Stations.add(Integer.parseInt(str.trim()));

				}
				int minIdx = Integer.parseInt(terms[2].replace("[", "")
						.replace("]", "").split(",")[0]);
				int maxIdx = Integer.parseInt(terms[2].replace("[", "")
						.replace("]", "").split(",")[1]);
				for (int i = minIdx; i <= maxIdx; i++) {
					timeSlots.add(i);
				}

				rItem = new ResultItem(index, date, Stations, timeSlots);
				trueList.add(rItem);
				index++;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(trueList);
		return trueList;
	}

	public static ArrayList<ResultItem> filterResult(
			ArrayList<ResultItem> result) {
		int origSize = result.size();
		ArrayList<ResultItem> filResultItems = result;
		// ArrayList<ResultItem> removeIdxList=new ArrayList<ResultItem>();
		Set<ResultItem> removeIdxList = new HashSet<>();
		for (int i = 0; i < filResultItems.size(); i++) {
			ResultItem itemi = filResultItems.get(i);

			for (int j = i + 1; j < filResultItems.size(); j++) {
				ResultItem itemj = filResultItems.get(j);
				if (itemi.index != itemj.index && itemi.date == itemj.date) {
					boolean include = itemi.timeSlots.get(0) <= itemj.timeSlots
							.get(0)
							&& itemi.timeSlots.get(itemi.timeSlots.size() - 1) >= itemj.timeSlots
									.get(itemj.timeSlots.size() - 1);
					if ((1.0D * ResultItem.intersection(itemi.timeSlots,
							itemj.timeSlots).size())
							/ itemi.timeSlots.size() > 0.6D
							|| include) {
						// if((1.0D*ResultItem.intersection(itemi.timeSlots,
						// itemj.timeSlots).size())/itemi.timeSlots.size()>0.7D
						// && (1.0D *ResultItem.intersection(itemi.Stations,
						// itemj.Stations).size())>0.0D){

						if (itemi.score >= itemj.score) {
							// System.out.println("Big i "+i+" "+j+" "+itemi.index+" "+itemj.index+" "+filResultItems.size()+" "+itemi.date+" "+itemj.date);

							removeIdxList.add(itemj);

						} else {
							System.err.println("Big j " + i + " " + j + " "
									+ itemi.index + " " + itemj.index + " "
									+ filResultItems.size() + " " + itemi.date
									+ " " + itemj.date);

							// if(i>=filResultItems.size())
							removeIdxList.add(itemi);

						}

					}
				}

			}// j
		}// i

		filResultItems.removeAll(removeIdxList);
		System.out.println("size: " + origSize + " - " + removeIdxList.size()
				+ " = " + filResultItems.size());
		return filResultItems;
	}



	public static void CaseStudy(int maxwin, int sss, String month) {
		long startTimeAll = System.nanoTime();
		allResultList = new ArrayList<ResultItem>();
		List<String> caseDates = (List) Arrays.asList("201603", "201604",
				"201605", "201606", "201607", "201608", "201609", "201610",
				"201611", "201612");
		// List<String> caseDates=(List)
		// Arrays.asList("20160301","20160302","20160303","20160304","20160305","20160306","20160307","20160308","20160309","20160310","20160311","20160312","20160313","20160314","20160315","20160316","20160317","20160318","20160319","20160320","20160321","20160322","20160323","20160324","20160325","20160326","20160327","20160328","20160329","20160330","20160331");

		String methodType = "CP";

		// "temp","temp9","press","wind","windDir","windMax","rh","rad"
		String[] var_types = { "temp", "temp9", "wind", "windMax", "rh" };

		// String folder="data/mesonet_data/trans/";
		boolean fileExistForAllVar = true;
		String fileName = "";
		String[] filePath = null;
		int fileCount = 0;
		for (File apdmFile : new File("data/mesonet_data/" + var_types[0]
				+ "_APDM/").listFiles()) {
			fileName = apdmFile.getName();
			if (!fileName.split("_")[0].startsWith(month)) {
				continue;
			}
			if (!caseDates.contains(fileName.split("_")[0].substring(0, 6))) {
				continue;
			}
			filePath = new String[var_types.length];
			for (int i = 0; i < var_types.length; i++) {
				filePath[i] = "data/mesonet_data/" + var_types[i] + "_APDM/"
						+ fileName;

			}
			if (!new File(Paths.get(
					"outputs/mesonetPlots/multi_CaseStudy/" + methodType + "/"
							+ sss + "/").toString()).exists()) {
				new File(Paths.get(
						"outputs/mesonetPlots/multi_CaseStudy/" + methodType
								+ "/" + sss + "/").toString()).mkdirs();
				new File(Paths.get(
						"outputs/mesonetPlots/multi_CaseStudy/" + methodType
								+ "/" + sss + "/prf1/").toString()).mkdirs();
			}

			for (String file : filePath) {
				File fileCheck = new File(file);
				if (!fileCheck.exists()) {
					fileExistForAllVar = false;
					System.out.println("Not available..: " + file);
					break;
				}
			}
			if (!fileExistForAllVar) {
				System.out.println("false.....");
				continue;
			}
			System.out.print(fileCount + " [Multi " + fileName + "] ");
			fileCount++;
			String outFile = "outputs/mesonetPlots/multi_CaseStudy/"
					+ methodType + "/" + sss + "/" + apdmFile.getName();
			String prf1File = "outputs/mesonetPlots/multi_CaseStudy/"
					+ methodType + "/" + sss + "/prf1/" + fileName;

			if (methodType.equals("CP")) {
				// System.out.println("---CP---");
				testSingleFileChangePoint(
						filePath,
						outFile,
						prf1File, maxwin, sss);
			} else {
				System.out.println("---CP3---");
				// testSingleFileChangePoint2(folderTemp+fileName,folderWind+fileName,
				// outFile,"outputs/mesonetPlots/multi_CaseStudy/true_values3.txt",prf1File,maxwin,sss);

			}

		}// file
		if (!fileExistForAllVar) {

		}
		// ArrayList<ResultItem> groundTrueItems =
		// getGroundTruthRI("outputs/mesonetPlots/multi_CaseStudy/true_values3.txt");
		ArrayList<ResultItem> resultItems = new ArrayList<ResultItem>();

		FileWriter allWriter = null;
		FileWriter allWriterOut = null;
		FileWriter allWriterOutTop = null;
		String prf1Filepath = sss + "/prf1/MarDecEvent_multi_PRF1_result-"
				+ methodType + "_baseMeanDiff_20_s_" + sss + "_wMax_" + maxwin
				+ "_filter_TIncld_0.7.txt";
		String resultFilepath = sss + "/MarDecEvent_multiGraphMP_TopK_result-"
				+ methodType + "_baseMeanDiff_20_s_" + sss + "_wMax_" + maxwin
				+ "_filter_TIncld_0.7.txt";
		String resultFilepath2 = sss
 + "/MarDecEvent_multiGraphMP_TopK_result-"
				+ methodType
				+ "_baseMeanDiff_20_s_" + sss + "_wMax_" + maxwin
				+ "_filter_TIncld_0.7_Top.txt";
		try {
			allWriter = new FileWriter("outputs/mesonetPlots/multi_CaseStudy/"
					+ methodType + "/" + prf1Filepath, false);
			allWriterOut = new FileWriter(
					"outputs/mesonetPlots/multi_CaseStudy/" + methodType + "/"
							+ resultFilepath, false);
			allWriterOutTop = new FileWriter(
					"outputs/mesonetPlots/multi_CaseStudy/" + methodType + "/"
							+ resultFilepath2, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Collections.sort(allResultList, new ResultItem().comparator);

		int mapCountAll = 0;
		for (int i = 0; i < allResultList.size(); i++) {
			ResultItem.printItem(i + 1, allResultList.get(i), true);
			resultItems.add(allResultList.get(i));
			try {
				allWriterOut
						.write(allResultList.get(i).score
								+ " "
								+ ArrayUtils.toString(allResultList.get(i).changedVarList)
								+ " "
								+ ArrayUtils.toString(allResultList.get(i).Stations)
								+ " "
								+ ArrayUtils.toString(allResultList.get(i).timeSlots)
								+ " " + allResultList.get(i).date + "\n");
				allWriterOutTop.write(i
						+ " "
						+ ArrayUtils
								.toString(allResultList.get(i).changedVarList)
								.replace(" ", "").replace("[", "")
								.replace("]", "")
						+ " "
						+ ArrayUtils.toString(allResultList.get(i).Stations)
								.replace(" ", "").replace("[", "")
								.replace("]", "") + " "
						+ allResultList.get(i).date + " "
						+ Collections.min(allResultList.get(i).timeSlots) + ","
						+ Collections.max(allResultList.get(i).timeSlots)
						+ "\n");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mapCountAll++;

			if (mapCountAll < 0) {
				break;
			}

		}


		getPRF1 prf1 = null;
		int maxK = resultItems.size() < 2000 ? resultItems.size() : 2000;
		for (int i = 10; i < maxK; i += 5) {
			// prf1=new getPRF1(i, resultItems, groundTrueItems.size());

			/** Print the detected pattern list in TOp 100 **/
			if (i == 105) {
				for (int r = 0; r < i; r++) {
					System.out.println("R>" + resultItems.get(r).detectIdxList);
				}
			}

		}
		try {
			allWriterOutTop.close();
			allWriterOut.close();
			allWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(mapCountAll);
		System.out.println("outputs/mesonetPlots/multi_CaseStudy/"
				+ prf1Filepath);

		// }//type
		System.out.println("Total running time: "
				+ (System.nanoTime() - startTimeAll) / 1e9);

	}

	public static void main(String[] args) {
		List<Integer> maxWin = (List) Arrays.asList(18);
		List<Integer> ss = (List) Arrays.asList(2);
		for (int mwin : maxWin) {
			for (int sss : ss) {
				CaseStudy(mwin, sss, "2016");
			}
		}
		// getGroundTruth("outputs/mesonetPlots/temp_CaseStudy/true_20160301.txt");
	}

}
