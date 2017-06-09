package edu.albany.cs.taffic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import edu.albany.cs.transWeather.ResultItem;

public class TestTravelTimeDebug {

	public static int verboseLevel = 0;
	public static ArrayList<ResultItem> allResultList = new ArrayList<ResultItem>();
	public static int resIndex = 0;
	public static int ss = 2;
	public static int maxWinSize = 12;

	public static void testSingleFileOneChangePoint(String singleFile,
			String resultFileName, int mwin, int sss) {
		long startTime = System.nanoTime();
		FileWriter fileWriter = null;
		APDMInputFormat apdm = new APDMInputFormat(singleFile);
		TransWeatherRealGraph graph = new TransWeatherRealGraph(apdm);
		String[] paths = singleFile.split("/");
		String date = paths[paths.length - 1].split("_")[0];
		try {
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

		/* data parameters */
		double[][] X = graph.x; // data Matrix, X_ij mean the ith station jth
								// time slot value
		double[] mean = graph.mu;
		double[] c = new double[X.length]; // input value (observed counts or
											// values)
		double[] b = new double[X.length]; // base, all set to zero
		Arrays.fill(b, 1.0D);
		double[] hist_base = new double[X.length];
		int histStaPoint = 6;
		int sCount = 0;
		int maxWin = mwin;
		/* Result and Ground-True */
		// TreeMap<Double,String> resultMap = new
		// TreeMap<Double,String>(Collections.reverseOrder());
		ArrayList<ResultItem> resultList = new ArrayList<ResultItem>();

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

		/* Generate Time Windows Parameters */

		System.out.println("s=" + s + " MaxWinSize=" + maxWin);
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

				sCount++; // count possible windows
				Arrays.fill(c, 0.0D);
				Arrays.fill(hist_base, 0.0D);
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
					// System.out.println("Temp:"+temp+" "+(S[0]-starIdx)+" "+(i-j+1)+"="+winNormalizeFactor[q]);
				}

				/** Changing point detection **/
				int index = 0;
				double emsScore = Double.NEGATIVE_INFINITY;
				Double[] temp = new Double[4];
				double tempDiff = 0.0D;

				double T1Mean = 0.0D;
				double T2Mean = 0.0D;

				for (int q = 0; q < X.length; q++) {
					T1Mean = 0.0D;
					T2Mean = 0.0D;
					tempDiff = 0.0D;
					emsScore = Double.NEGATIVE_INFINITY;
					for (int p = 1; p < S.length - 1; p++) {

						temp = EMSScores(X[q], S[0], p, S[S.length - 1]);

						if (temp[0] > emsScore) {
							index = temp[1].intValue();
							// sIndex=temp[4].intValue();
							emsScore = temp[0];
							// tempDiff=Math.abs(temp[2]-temp[3]);
							T1Mean = temp[2];
							T2Mean = temp[3];

						}
					}
					// System.out.println(">>"+Arrays.toString(temp)+Arrays.toString(S));
					// System.out.println("==>"+sIndex);
					// XX[q]=tempDiff;
					// System.out.println("Mean base diff:- "+q+" "+winNormalizeFactor[q]+" "+T1Mean+" "+T2Mean+" "+Math.abs(T1Mean-winNormalizeFactor[q])+" "+
					// Math.abs(T2Mean-winNormalizeFactor[q])+" "+index+" "+ArrayUtils.toString(S));
					tempDiff = Math.abs(T1Mean - hist_base[q]) >= Math
							.abs(T2Mean - hist_base[q]) ? Math.abs(T1Mean
							- hist_base[q]) : Math.abs(T2Mean - hist_base[q]);
					// System.out.println(tempDiff);
					c[q] = tempDiff;
				}

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
							+ ArrayUtils.toString(graphMP.resultNodes_Tail)
							+ " score:" + graphMP.funcValue);
					System.out.println("Time Slots:" + ArrayUtils.toString(S));
					System.out.println("winMean:" + ArrayUtils.toString(b));
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
				// System.out.println(score);
				// Utils.stop();
				ResultItem resItem = new ResultItem(resIndex, score, date,
						Stations, timeSlots);
				resultList.add(resItem);
				resIndex++;

			}// j
		}// i

		Collections.sort(resultList, new ResultItem().comparator);
		ArrayList<ResultItem> filResultList = filterResult(resultList);

		int mapCount = 0;
		int cutOff = 100;

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
	public static void testSingleFileTwoChangePoint(String singleFile,
			String resultFileName, int mwin, int sss) {
		long startTime = System.nanoTime();
		FileWriter fileWriter = null;

		APDMInputFormat apdm = new APDMInputFormat(singleFile);
		TransWeatherRealGraph graph = new TransWeatherRealGraph(apdm);
		String[] paths = singleFile.split("/");
		String date = paths[paths.length - 1].split("_")[0];
		try {

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

		/* Time Windows Parameters */
		int histStaPoint = 6;
		int sCount = 0;
		int maxWin = mwin;

		/* Changing Pattern Result */

		ArrayList<ResultItem> resultList = new ArrayList<ResultItem>();


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
						Stations, timeSlots);
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

	public static double TwoBreakingPoint(double[] X, int[] S, double hist_base) {
		int index = 0;
		double emsScore = Double.NEGATIVE_INFINITY;
		Double[] temp = new Double[4];
		Double[] temp2 = new Double[4];
		double tempDiff = 0.0D;

		double T1Mean = 0.0D;
		double T2Mean = 0.0D;
		double T21Mean = 0.0D;
		double T3Mean = 0.0D;
		for (int p = 1; p < S.length - 1; p++) {
			for (int w = 1; w <= p; w++) {
				temp = EMSScores2(X, S[0], w, S[p]);
				temp2 = EMSScores2(X, S[w], p - w + 1, S[S.length - 1]);

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

			}// w
		}// p
		if (T1Mean < T2Mean && T2Mean > T3Mean) {
			T2Mean = T2Mean+Math.abs(T1Mean - T2Mean) + Math.abs(T2Mean - T3Mean);
		}else if(T1Mean > T2Mean && T2Mean < T3Mean){
			T2Mean = T2Mean- Math.abs(T1Mean - T2Mean) - Math.abs(T2Mean - T3Mean);
		}
			

		tempDiff = Math.max(
				Math.max(Math.abs(T1Mean - hist_base),
						Math.abs(T2Mean - hist_base)),
				Math.abs(T3Mean - hist_base));
		
		return tempDiff;

	}
    
	
	
	public static double OneBreakingPoint(double[] X, int[] S, double hist_base) {
		/** Changing point detection **/
		int index = 0;
		double emsScore = Double.NEGATIVE_INFINITY;
		Double[] temp = new Double[4];
		double tempDiff = 0.0D;
		double T1Mean = 0.0D;
		double T2Mean = 0.0D;

		for (int p = 1; p < S.length - 1; p++) {

			temp = EMSScores(X, S[0], p, S[S.length - 1]);

			if (temp[0] > emsScore) {
				index = temp[1].intValue();
				// sIndex=temp[4].intValue();
				emsScore = temp[0];
				// tempDiff=Math.abs(temp[2]-temp[3]);
				T1Mean = temp[2];
				T2Mean = temp[3];

			}
		}

		tempDiff = Math.abs(T1Mean - hist_base) >= Math.abs(T2Mean - hist_base) ? Math
				.abs(T1Mean - hist_base) : Math.abs(T2Mean - hist_base);

		return tempDiff;
	}

	public static void testSingleFile23ChangePointDebug(String singleFile,
			String resultFileName, int mwin, int sss) {
		long startTime = System.nanoTime();
		FileWriter fileWriter = null;

		APDMInputFormat apdm = new APDMInputFormat(singleFile);
		TransWeatherRealGraph graph = new TransWeatherRealGraph(apdm);
		String[] paths = singleFile.split("/");
		String date = paths[paths.length - 1].split("_")[0];
		try {

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

		/* Time Windows Parameters */
		int histStaPoint = 6;
		int sCount = 0;
		int maxWin = mwin;

		/* Changing Pattern Result */

		ArrayList<ResultItem> resultList = new ArrayList<ResultItem>();

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
		System.out.println("s=" + s + " MaxWinSize=" + maxWin);

		/*
		 * Generate all possible time window, window_size >=2 and less than
		 * maxWin
		 */
		for (int i = 72; i < X[0].length - 48; i++) {
			for (int j = 72; j < i + 1; j++) {
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
				/** Checking there one or two changing points in the window */
				for (int q = 0; q < X.length; q++) {
					double average = getWindowAverage(X[q], S);
					c[q] = (average - hist_base[q] > 0) ? average
							- hist_base[q] : 0.0D;

					// if (S.length == 3) {
					// c[q] = Math.abs(X[q][S[1]] - hist_base[q]);
					// } else if (S.length == 4) {
					// c[q] = Math.abs((X[q][S[1]] + X[q][S[2]]) / 2.0
					// - hist_base[q]);
					// } else {
					// double tempDiff1 = OneBreakingPoint(X[q], S,
					// hist_base[q]);
					// double tempDiff2 = TwoBreakingPoint(X[q], S,
					// hist_base[q]);
					//
					// if (tempDiff1 > tempDiff2) {
					// // if (S[S.length - 1] == 287) {
					// // Utils.stop();
					// // System.out.format("temp1 %f %d %d %d\n", tempDiff1,
					// // q, S[0], S[S.length - 1]);
					// // }
					// c[q] = Math.abs(average - hist_base[q]);
					//
					// } else {
					// // if (S[S.length - 1] == 287) {
					// // Utils.stop();
					// // System.out.format("temp2 %f %d %d %d\n", tempDiff2,
					// // q, S[0], S[S.length - 1]);
					// // }
					// c[q] = Math.abs(average - hist_base[q]);
					//
					// }
					// }
					// double winSum = 0.0D;
					// for (int k = S[0]; k <= S[S.length - 1]; k++) {
					// winSum += X[q][k];
					// }
					//
					// double win_mean = winSum / S.length;
					// c[q] = Math.abs(hist_base[q] - win_mean);

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
						Stations, timeSlots, c);

				// if (Stations.contains(17) && timeSlots.contains(235)) {
				// System.out.println(score + " " + timeSlots.toString());
				// Utils.stop();
				// }
				resultList.add(resItem);
				resIndex++;

			}// j

		}// i

		Collections.sort(resultList, new ResultItem().comparator);
		// ArrayList<ResultItem> filResultList = filterResult(resultList);
		ArrayList<ResultItem> filResultList = resultList;
		// Iterator<Entry<Double, String>> it = resultMap.entrySet().iterator();
		// Entry<Double, String> entry;
		int mapCount = 0;
		int cutOff = 200;
		// for(int i=0;i<cutOff && i<filResultList.size();i++)
		for (int i = 0; i < cutOff && i < filResultList.size(); i++)

		{
			try {
				// System.out.println(entry.getKey()+" "+entry.getValue());
				fileWriter.write(filResultList.get(i).score
						+ " "
						+ Arrays.toString(
								filResultList.get(i).Stations.toArray())
								.replace("{", "").replace("}", "")
						+ " "
						+ Arrays.toString(
								filResultList.get(i).timeSlots.toArray())
								.replace("{", "").replace("}", "") + " " + date
						+ filResultList.get(i).c.toString()
						+ "\n");//

			allResultList.add(filResultList.get(i));
			if (filResultList.get(i).Stations.size() > 1) {
				mapCount++;
			}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (mapCount > 49) {
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

	public static void testSingleFile23ChangePoint(String singleFile,
			String resultFileName, int mwin, int sss) {
		long startTime = System.nanoTime();
//		FileWriter fileWriter = null;

		APDMInputFormat apdm = new APDMInputFormat(singleFile);
		TransWeatherRealGraph graph = new TransWeatherRealGraph(apdm);
		String[] paths = singleFile.split("/");
		String date = paths[paths.length - 1].split("_")[0];
//		try {
//
//			fileWriter = new FileWriter(resultFileName, false);
//			// fileWriter.write("[Score] [Station index] [Time slots] \n");
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
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

		/* Time Windows Parameters */
		int histStaPoint = 6;
		int sCount = 0;
		int maxWin = mwin;

		/* Changing Pattern Result */

		ArrayList<ResultItem> resultList = new ArrayList<ResultItem>();

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
		System.out.print("s=" + s + " MaxWinSize=" + maxWin + " ");

		/*
		 * Generate all possible time window, window_size >=2 and less than
		 * maxWin
		 */
		for (int i = 72; i < X[0].length - 48; i++) {
			for (int j = 72; j < i + 1; j++) {
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
				/** Checking there one or two changing points in the window */
				for (int q = 0; q < X.length; q++) {
					double average = getWindowAverage(X[q], S);
					c[q] = (average - hist_base[q] > 0) ? average
							- hist_base[q] : 0.0D;

					// if (S.length == 3) {
					// c[q] = Math.abs(X[q][S[1]] - hist_base[q]);
					// } else if (S.length == 4) {
					// c[q] = Math.abs((X[q][S[1]] + X[q][S[2]]) / 2.0
					// - hist_base[q]);
					// } else {
					// double tempDiff1 = OneBreakingPoint(X[q], S,
					// hist_base[q]);
					// double tempDiff2 = TwoBreakingPoint(X[q], S,
					// hist_base[q]);
					//
					// if (tempDiff1 > tempDiff2) {
					// // if (S[S.length - 1] == 287) {
					// // Utils.stop();
					// // System.out.format("temp1 %f %d %d %d\n", tempDiff1,
					// // q, S[0], S[S.length - 1]);
					// // }
					// c[q] = Math.abs(average - hist_base[q]);
					//
					// } else {
					// // if (S[S.length - 1] == 287) {
					// // Utils.stop();
					// // System.out.format("temp2 %f %d %d %d\n", tempDiff2,
					// // q, S[0], S[S.length - 1]);
					// // }
					// c[q] = Math.abs(average - hist_base[q]);
					//
					// }
					// }
					// double winSum = 0.0D;
					// for (int k = S[0]; k <= S[S.length - 1]; k++) {
					// winSum += X[q][k];
					// }
					//
					// double win_mean = winSum / S.length;
					// c[q] = Math.abs(hist_base[q] - win_mean);

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
						Stations, timeSlots);
				// Utils.stop();
				// if (Stations.contains(17) && Stations.contains(18)) {
				// System.out.println(score + " " + timeSlots.toString());
				// }
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
//			try {
//				// System.out.println(entry.getKey()+" "+entry.getValue());
//				fileWriter
//						.write(filResultList.get(i).score
//								+ " "
//								+ Arrays.toString(
//										filResultList.get(i).Stations.toArray())
//										.replace("{", "").replace("}", "")
//								+ " "
//								+ Arrays.toString(
//										filResultList.get(i).timeSlots.toArray())
//										.replace("{", "").replace("}", "")
//								+ " " + date + "\n");//
				
				allResultList.add(filResultList.get(i));
				if(filResultList.get(i).Stations.size()>1){
					mapCount++;
				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

			if (mapCount > 19) {
				break;
			}

		}

		// try {
		// fileWriter.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		System.out.print(sCount + " " + mapCount + " ");
		System.out.println("running time: " + (System.nanoTime() - startTime)/1e9);

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

	public static double getWindowAverage(double[] X, int[] S) {
		double avg = 0.0D;
		double temp = 0.0;
		for (int i = S[0]; i < S[S.length - 1]; i++) {
			temp += X[i];
		}
		avg = 1.0D * temp / S.length;
		return avg;
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
							/ itemi.timeSlots.size() > 0.01D
							|| include) {

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
				} // same date,and idx_i!=idx_j

			}// j
		}// i

		filResultItems.removeAll(removeIdxList);
		System.out.print("Filter size: " + origSize + " - "
				+ removeIdxList.size() + " = " + filResultItems.size() + " ");
		return filResultItems;
	}

	public static void CaseStudy(int maxwin, int sss) {
		long startTimeAll = System.nanoTime();
		allResultList = new ArrayList<ResultItem>();

		List<String> caseDates = (List<String>) Arrays.asList("20160811");

		int count = 0;
		String direc = "W";
		String ex = "W621Debug";
		String methodType = "CPBest";
		System.out.println("---" + methodType + "---" + direc);
		int fcount = 0;
		for (String type : Arrays.asList("travelTime")) {// ,"wind","temp","temp9","press","wind","windDir","windMax","rh","rad")){
			String folder = "data/trafficData/I90_TravelTime/"
					+ direc.toLowerCase() + type
					+ "_APDM/";
			String fileName = "";
			for (File apdmFile : new File(folder).listFiles()) {
				fileName = apdmFile.getName();

				if (!new File(Paths.get(
						"outputs/trafficData/" + type + "_CaseStudy/"
								+ methodType + "/" + sss + "/").toString())
						.exists()) {
					new File(Paths.get(
							"outputs/trafficData/" + type + "_CaseStudy/"
									+ methodType + "/" + sss + "/").toString())
							.mkdirs();
					new File(Paths.get(
							"outputs/trafficData/" + type + "_CaseStudy/"
									+ methodType + "/" + sss + "/prf1/")
							.toString()).mkdirs();
				}

				if (!caseDates.contains(fileName.split("_")[0])) {
					continue;
				}
				// if (!fileName.split("_")[0].startsWith("201604")) {
				// continue;
				// }
				System.out.print(fcount + " " + type + " " + fileName + " ");
				fcount++;
				String outFile = "outputs/trafficData/" + type + "_CaseStudy/"
						+ methodType + "/" + sss + "/prf1/"
						+ apdmFile.getName();
				if (methodType.equals("CP")) {
					System.out.println("---CP---");
					testSingleFileOneChangePoint(folder + apdmFile.getName(),
							outFile, maxwin, sss);
				} else if (methodType.equals("CP2")) {
					System.out.println("---CP2---");
					testSingleFileTwoChangePoint(folder + apdmFile.getName(),
							outFile, maxwin, sss);
				}else {
					testSingleFile23ChangePoint(
							folder + apdmFile.getName(),
							outFile, maxwin, sss);

				}
				count++;
			}// file

			ArrayList<ResultItem> resultItems = new ArrayList<ResultItem>();

			FileWriter allWriterOut = null;
			FileWriter allWriterOutTop = null;
			FileWriter singleWriterOutTop = null;
			FileWriter multiWriterOutTop = null;
			String resultFilepath = sss
 + "/" + ex
					+ "I90traffic_AllYearEvent_TopK_result_baseMeanDiff_20_s_"
					+ sss + "_wMax_" + maxwin
					+ "_filter_TIncld_0.7.txt";
			String resultFilepath2 = sss
 + "/" + ex
					+ "I90traffic_AllYearEvent_TopK_result_baseMeanDiff_20_s_"
					+ sss + "_wMax_" + maxwin
					+ "_filter_TIncld_0.7_Top.txt";
			String resultFilepathSignle = sss
 + "/" + ex
					+ "I90traffic_AllYearEvent_TopK_result_baseMeanDiff_20_s_"
					+ sss + "_wMax_" + maxwin
					+ "_filter_TIncld_0.7_Top_single.txt";
			String resultFilepathMulti = sss
 + "/" + ex
					+ "I90traffic_AllYearEvent_TopK_result_baseMeanDiff_20_s_"
					+ sss + "_wMax_" + maxwin
					+ "_filter_TIncld_0.7_Top_multi.txt";

			try {
				allWriterOut = new FileWriter("outputs/trafficData/" + type
						+ "_CaseStudy/" + methodType + "/" + resultFilepath,
						false);
				allWriterOutTop = new FileWriter("outputs/trafficData/" + type
						+ "_CaseStudy/" + methodType + "/" + resultFilepath2,
						false);
				singleWriterOutTop = new FileWriter("outputs/trafficData/"
						+ type + "_CaseStudy/" + methodType + "/"
						+ resultFilepathSignle, false);
				multiWriterOutTop = new FileWriter("outputs/trafficData/"
						+ type + "_CaseStudy/" + methodType + "/"
						+ resultFilepathMulti, false);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Collections.sort(allResultList, new ResultItem().comparator);

			int mapCountAll = 0;
			for (int i = 0; i < allResultList.size(); i++) {
				ResultItem.printItem(i + 1, allResultList.get(i));
				resultItems.add(allResultList.get(i));
				try {
					if (allResultList.get(i).Stations.size() < 2) {

						singleWriterOutTop
								.write(i+ " "
										+ ArrayUtils
												.toString(
														allResultList.get(i).changedVarList)
												.replace(" ", "")
												.replace("[", "")
												.replace("]", "")
										+ " "
										+ ArrayUtils
												.toString(
														allResultList.get(i).Stations)
												.replace(" ", "")
												.replace("[", "")
												.replace("]", "")
										+ " "
										+ allResultList.get(i).date
										+ " "
										+ Collections.min(allResultList.get(i).timeSlots)
										+ ","
										+ Collections.max(allResultList.get(i).timeSlots)
										+ "\n");
						allWriterOut
						.write("S "+allResultList.get(i).score
								+ " "
								+ ArrayUtils.toString(allResultList.get(i).Stations)
								+ " "
								+ ArrayUtils.toString(allResultList.get(i).timeSlots)
								+ " " + allResultList.get(i).date + "\n");
					} else {
						multiWriterOutTop
								.write(i+ " "
										+ ArrayUtils
												.toString(
														allResultList.get(i).changedVarList)
												.replace(" ", "")
												.replace("[", "")
												.replace("]", "")
										+ " "
										+ ArrayUtils
												.toString(
														allResultList.get(i).Stations)
												.replace(" ", "")
												.replace("[", "")
												.replace("]", "")
										+ " "
										+ allResultList.get(i).date
										+ " "
										+ Collections.min(allResultList.get(i).timeSlots)
										+ ","
										+ Collections.max(allResultList.get(i).timeSlots)
										+ "\n");
						allWriterOut
						.write("M "+allResultList.get(i).score
								+ " "
								+ ArrayUtils.toString(allResultList.get(i).Stations)
								+ " "
								+ ArrayUtils.toString(allResultList.get(i).timeSlots)
								+ " " + allResultList.get(i).date + "\n");

					}

					
					allWriterOutTop
							.write(i
									+ " "
									+ ArrayUtils
											.toString(
													allResultList.get(i).changedVarList)
											.replace(" ", "").replace("[", "")
											.replace("]", "")
									+ " "
									+ ArrayUtils
											.toString(
													allResultList.get(i).Stations)
											.replace(" ", "").replace("[", "")
											.replace("]", "")
									+ " "
									+ allResultList.get(i).date
									+ " "
									+ Collections.min(allResultList.get(i).timeSlots)
									+ ","
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

			try {
				allWriterOut.close();
				allWriterOutTop.close();
				singleWriterOutTop.close();
				multiWriterOutTop.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(mapCountAll);

			allResultList.clear();
		}// type
		System.out.println("Total running time: "
				+ (System.nanoTime() - startTimeAll) / 1e9);

	}

	public static void main(String[] args) {

		List<Integer> maxWin = (List<Integer>) Arrays.asList(12);// ,24,36);//,12,18,24,30,36);
		List<Integer> ss = (List<Integer>) Arrays.asList(5);

		for (int mwin : maxWin) {
			for (int sss : ss) {
				CaseStudy(mwin, sss);
			}
		}

	}

}
