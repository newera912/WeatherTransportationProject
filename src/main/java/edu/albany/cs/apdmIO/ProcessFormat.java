package edu.albany.cs.apdmIO;

import edu.albany.cs.base.Constants;
import edu.albany.cs.scoreFuncs.FuncType;
import edu.albany.cs.scoreFuncs.Function;
import edu.albany.cs.scoreFuncs.ScoreFunctionFactory;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class ProcessFormat {

    public static FuncType[]
            funcs = new FuncType[]{
            FuncType.KulldorffStat,
            FuncType.EBPStat,
            FuncType.EMSStat,};

    public static void citHepPh() throws IOException {
        String fileNodesName = "output/citHepPh/edgeLasso_citHepPh_resultNodes.txt";
        String fileTableName = "output/citHepPh/edgeLasso_citHepPh_resultTable.txt";
        String resultFileName = "output/citHepPh/edgeLasso_citHepPhResult.txt";

        fileNodesName = "output/citHepPh/graphLaplacian_citHepPh_resultNodes.txt";
        fileTableName = "output/citHepPh/graphLaplacian_citHepPh_resultTable.txt";
        resultFileName = "output/citHepPh/graphLaplacian_citHepPhResult.txt";

        //		fileNodesName = "output/citHepPh/ltss_citHepPh_resultNodes.txt";
        //		fileTableName = "output/citHepPh/ltss_citHepPh_resultTable.txt";
        //		resultFileName = "output/citHepPh/ltss_citHepPhResult.txt" ;
        //
        fileNodesName = "output/citHepPh/eventTree_citHepPH_resultNodes.txt";
        fileTableName = "output/citHepPh/eventTree_citHepPH_resultTable.txt";
        resultFileName = "output/citHepPh/eventTree_citHepPhResult.txt";

        fileNodesName = "output/citHepPh/nphgs_citHepPh_resultNodes.txt";
        fileTableName = "output/citHepPh/nphgs_citHepPh_resultTable.txt";
        resultFileName = "output/citHepPh/nphgs_citHepPhResult.txt";

        fileNodesName = "output/citHepPh/additiveScan_citHepPh_resultNodes.txt";
        fileTableName = "output/citHepPh/additiveScan_citHepPh_resultTable.txt";
        resultFileName = "output/citHepPh/additiveScan_citHepPhResult.txt";

        fileNodesName = "output/citHepPh/depthFirstScan_citHepPh_resultNodes.txt";
        fileTableName = "output/citHepPh/depthFirstScan_citHepPh_resultTable.txt";
        resultFileName = "output/citHepPh/depthFirstScan_citHepPhResult.txt";

        HashMap<String, int[]> fileMapNodes = new HashMap<String, int[]>();
        for (String line : Files.readAllLines(Paths.get(fileNodesName))) {
            line = line.trim();
            String[] items = line.split(" ");
            String fileID = items[0];
            int[] nodes = null;
            for (int i = 1; i < items.length; i++) {
                nodes = ArrayUtils.add(nodes, Integer.parseInt(items[i]));
            }
            fileMapNodes.put(fileID, nodes);
        }

        HashMap<String, Double> fileMapResult = new HashMap<String, Double>();
        for (String line : Files.readAllLines(Paths.get(fileTableName))) {
            line = line.trim();
            String[] items = line.split("\t");
            String fileID = items[5];
            double runTime = Double.parseDouble(items[2]);
            fileMapResult.put(fileID, runTime);
        }

        for (String key : fileMapNodes.keySet()) {
            APDMInputFormat apdm = null;
            if (key.endsWith(".txt")) {
                apdm = new APDMInputFormat(new File("data/citHepPh/graph/" + key));
            } else {
                apdm = new APDMInputFormat(new File("data/citHepPh/graph/" + key + ".txt"));
            }

            double[] c = apdm.data.counts;
            double[] b = apdm.data.averCounts;
            double[] sigma = new double[apdm.data.numNodes];
            for (int i = 0; i < c.length; i++) {
                sigma[i] = 0.01D;
            }
            double[] x = new double[apdm.data.numNodes];
            int[] resultNodes = fileMapNodes.get(key);
            for (int i = 0; i < x.length; i++) {
                if (ArrayUtils.contains(resultNodes, i)) {
                    x[i] = 1.0D;
                } else {
                    x[i] = 0.0D;
                }
            }
            double runTime = fileMapResult.get(key);
            for (FuncType funcID : funcs) {
                Function func = ScoreFunctionFactory.getFunction(funcID, b, c, sigma);
                ArrayList<Double> fValues = new ArrayList<Double>();
                fValues.add(func.getFuncValue(x));
                //write result table
                //fileName ; rumTime ; functionID ; iters ; funcValues ; nodes
                FileWriter fileWriter = new FileWriter(new File(resultFileName), true);
                fileWriter.write(key + " ; " + runTime + " ; " + funcID + " ; " + fValues.size() + " ; ");
                if (fValues.size() == 1) {
                    fileWriter.write(fValues.get(0) + " ; ");
                } else {
                    for (int i = 0; i < fValues.size() - 1; i++) {
                        fileWriter.write(fValues.get(i) + ",");
                    }
                    fileWriter.write(fValues.get(fValues.size() - 1) + " ; ");
                }

                if (resultNodes != null) {
                    if (resultNodes.length == 1) {
                        fileWriter.write(resultNodes[0] + " ; ");
                    } else {
                        String str = "";
                        for (int i = 0; i < resultNodes.length - 1; i++) {
                            str = str + resultNodes[i] + ",";
                        }
                        str = str + resultNodes[resultNodes.length - 1];
                        fileWriter.write(str + "\n");
                    }
                }
                fileWriter.close();
            }
        }
    }


    public static void crimeOfChicago() throws IOException {
        String fileNodesName = "output/CrimeOfChicago/edgeLasso_CrimeOfChicago_resultNodes.txt";
        String fileTableName = "output/CrimeOfChicago/edgeLasso_CrimeOfChicago_resultTable.txt";
        String resultFileName = "output/poisson/edgeLasso_CrimeOfChicagoResult.txt";

//    fileNodesName = "output/CrimeOfChicago/graphLaplacian_CrimeOfChicago_resultNodes.txt"; 
//    fileTableName = "output/CrimeOfChicago/graphLaplacian_CrimeOfChicago_resultTable.txt"; 
//    resultFileName = "output/poisson/graphLaplacian_CrimeOfChicagoResult.txt" ;

//          fileNodesName = "output/CrimeOfChicago/ltss_CrimeOfChicago_resultNodes.txt"; 
//          fileTableName = "output/CrimeOfChicago/ltss_CrimeOfChicago_resultTable.txt"; 
//          resultFileName = "output/poisson/ltss_CrimeOfChicagoResult.txt" ;
        //
//    fileNodesName = "output/CrimeOfChicago/eventTree_CrimeOfChicago_resultNodes.txt"; 
//    fileTableName = "output/CrimeOfChicago/eventTree_CrimeOfChicago_resultTable.txt"; 
//    resultFileName = "output/poisson/eventTree_CrimeOfChicagoResult.txt" ;
//
        fileNodesName = "output/CrimeOfChicago/nphgs_CrimeOfChicago_resultNodes.txt";
        fileTableName = "output/CrimeOfChicago/nphgs_CrimeOfChicago_resultTable.txt";
        resultFileName = "output/poisson/nphgs_CrimeOfChicagoResult.txt";
//
//    fileNodesName = "output/CrimeOfChicago/additiveScan_CrimeOfChicago_resultNodes.txt"; 
//    fileTableName = "output/CrimeOfChicago/additiveScan_CrimeOfChicago_resultTable.txt"; 
//    resultFileName = "output/poisson/additiveScan_CrimeOfChicagoResult.txt" ;
//
//    fileNodesName = "output/CrimeOfChicago/depthFirstScan_CrimeOfChicago_resultNodes.txt"; 
//    fileTableName = "output/CrimeOfChicago/depthFirstScan_CrimeOfChicago_resultTable.txt"; 
//    resultFileName = "output/poisson/depthFirstScan_CrimeOfChicagoResult.txt" ;

        HashMap<String, int[]> fileMapNodes = new HashMap<String, int[]>();
        for (String line : Files.readAllLines(Paths.get(fileNodesName))) {
            line = line.trim();
            String[] items = line.split(" ");
            String fileID = items[0];
            int[] nodes = null;
            for (int i = 1; i < items.length; i++) {
                nodes = ArrayUtils.add(nodes, Integer.parseInt(items[i]));
            }
            fileMapNodes.put(fileID, nodes);
        }

        HashMap<String, Double> fileMapResult = new HashMap<String, Double>();
        for (String line : Files.readAllLines(Paths.get(fileTableName))) {
            line = line.trim();
            String[] items = line.split("\t");
            String fileID = items[5];
            double runTime = Double.parseDouble(items[2]);
            fileMapResult.put(fileID, runTime);
        }

        for (String key : fileMapNodes.keySet()) {
            APDMInputFormat apdm = null;
            int date;
            if (key.endsWith(".txt")) {
                apdm = new APDMInputFormat(new File("data/CrimeOfChicago/graph/" + key));
                date = Integer.parseInt(key.split("APDM-")[1].split(".txt")[0]);
            } else {
                apdm = new APDMInputFormat(new File("data/CrimeOfChicago/graph/" + key + ".txt"));
                date = Integer.parseInt(key.split("APDM-")[1]);
            }

            double[] c = apdm.data.counts;
            double[] b = apdm.data.averCounts;
            for (int i = 0; i < b.length; i++) {
                if (b[i] <= 0.0D) {
                    b[i] = 0.01D;
                }
            }
            double[] x = new double[apdm.data.numNodes];
            int[] resultNodes = fileMapNodes.get(key);
            for (int i = 0; i < x.length; i++) {
                if (ArrayUtils.contains(resultNodes, i)) {
                    x[i] = 1.0D;
                } else {
                    x[i] = 0.0D;
                }
            }
            double runTime = fileMapResult.get(key);
            FuncType[] funcs = new FuncType[]{FuncType.EBPStat};
            for (FuncType funcID : funcs) {
                b = getPreviousBase(date);
                Function func = ScoreFunctionFactory.getFunction(funcID, b, c, null);
                ArrayList<Double> fValues = new ArrayList<Double>();
                fValues.add(func.getFuncValue(x));

                try {
                    FileWriter fileWriter = new FileWriter(new File(resultFileName), true);
                    fileWriter.write(
                            key + " " + funcID + " " + func.getFuncValue(x) + " " + runTime + " " + resultNodes.length + "\n");
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//        
//        //write result table
//        //fileName ; rumTime ; functionID ; iters ; funcValues ; nodes
//        FileWriter fileWriter = new FileWriter(new File(resultFileName),true) ;
//        fileWriter.write( key + " ; " + runTime +" ; "+funcID+" ; "+fValues.size()+" ; ");
//        if(fValues.size() == 1){
//          fileWriter.write(fValues.get(0)+" ; ") ;
//        }else{
//          for(int i = 0 ; i < fValues.size() -1 ; i++){
//            fileWriter.write(fValues.get(i) +",");
//          }
//          fileWriter.write(fValues.get(fValues.size() -1) +" ; ");
//        }
//
//        if(resultNodes != null){
//          if(resultNodes.length == 1){
//            fileWriter.write( resultNodes[0] +" ; ");   
//          }else{
//            String str = "" ;
//            for(int i=0;i<resultNodes.length - 1;i++){ 
//              str = str+resultNodes[i]+","; 
//            }
//            str = str + resultNodes[resultNodes.length - 1] ;
//            fileWriter.write(str+"\n");
//          }
//        }
//        fileWriter.close();
            }
        }
    }
    

    public static double[] getPreviousBase(int date) {
        double[] b = null;
        BufferedReader br = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader("./data/CrimeOfChicago/CrimeGraph/" + Integer.toString(date - 1) + "_nodes.txt"));
            while ((sCurrentLine = br.readLine()) != null) {
                String[] lineInfo = sCurrentLine.split(" ");
                b = ArrayUtils.add(b, Double.parseDouble(lineInfo[1].trim()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        for (int i = 0; i < b.length; i++) {
            if (b[i] <= 0.0D) {
                b[i] = 1.0D;
            }
        }
        return b;
    }


    public static void waterNetwork() throws IOException {
        String fileNodesName = "output/WaterNetwork/edgeLasso_waterNetwork_resultNodes.txt";
        String fileTableName = "output/WaterNetwork/edgeLasso_waterNetwork_resultTable.txt";
        String resultFileName = "output/WaterNetwork/edgeLasso_waterNetworkResult.txt";

        fileNodesName = "output/WaterNetwork/graphLaplacian_waterNetwork_resultNodes.txt";
        fileTableName = "output/WaterNetwork/graphLaplacian_waterNetwork_resultTable.txt";
        resultFileName = "output/WaterNetwork/graphLaplacian_waterNetworkResult.txt";

        fileNodesName = "output/WaterNetwork/ltss_waterNetwork_resultNodes.txt";
        fileTableName = "output/WaterNetwork/ltss_waterNetwork_resultTable.txt";
        resultFileName = "output/WaterNetwork/ltss_waterNetworkResult.txt";

        fileNodesName = "output/WaterNetwork/nphgs_waterNetwork_resultNodes.txt";
        fileTableName = "output/WaterNetwork/nphgs_waterNetwork_resultTable.txt";
        resultFileName = "output/WaterNetwork/nphgs_waterNetworkResult.txt";


        fileNodesName = "output/WaterNetwork/eventTree_waterNetwork_resultNodes.txt";
        fileTableName = "output/WaterNetwork/eventTree_waterNetwork_resultTable.txt";
        resultFileName = "output/WaterNetwork/eventTree_waterNetworkResult.txt";

        fileNodesName = "output/WaterNetwork/additiveScan_waterNetwork_resultNodes.txt";
        fileTableName = "output/WaterNetwork/additiveScan_waterNetwork_resultTable.txt";
        resultFileName = "output/WaterNetwork/additiveScan_waterNetworkResult.txt";

        fileNodesName = "output/WaterNetwork/depthFirstScan_waterNetwork_resultNodes.txt";
        fileTableName = "output/WaterNetwork/depthFirstScan_waterNetwork_resultTable.txt";
        resultFileName = "output/WaterNetwork/depthFirstScan_waterNetworkResult.txt";

        HashMap<String, int[]> fileMapNodes = new HashMap<String, int[]>();
        for (String line : Files.readAllLines(Paths.get(fileNodesName))) {
            line = line.trim();
            String[] items = line.split(" ");
            String fileID = items[0];
            int[] nodes = null;
            for (int i = 1; i < items.length; i++) {
                nodes = ArrayUtils.add(nodes, Integer.parseInt(items[i]));
            }
            fileMapNodes.put(fileID, nodes);
        }

        HashMap<String, Double> fileMapResult = new HashMap<String, Double>();
        for (String line : Files.readAllLines(Paths.get(fileTableName))) {
            line = line.trim();
            String[] items = line.split("\t");
            String fileID = items[5];
            double runTime = Double.parseDouble(items[2]);
            fileMapResult.put(fileID, runTime);
        }

        for (String key : fileMapNodes.keySet()) {
            APDMInputFormat apdm = null;
            if (key.endsWith(".txt")) {
                apdm = new APDMInputFormat(new File("data/WaterNetworkData/source_12500/" + key));
            } else {
                apdm = new APDMInputFormat(new File("data/WaterNetworkData/source_12500/" + key + ".txt"));
            }

            double[] b = new double[apdm.data.numNodes]; // let it be noise ratio
            double[] c = new double[apdm.data.numNodes];
            double[] sigma = new double[apdm.data.numNodes];
            for (int i = 0; i < apdm.data.numNodes; i++) {
                if (apdm.data.PValue[i] < Constants.AlphaMax) {
                    c[i] = 1.0D;
                } else {
                    c[i] = 0.0D;
                }
                b[i] = 0.01D;
            }
            double std = Math.sqrt(StatUtils.variance(c));
            for (int i = 0; i < apdm.data.numNodes; i++) {
                sigma[i] = std;
            }
            double[] x = new double[apdm.data.numNodes];
            int[] resultNodes = fileMapNodes.get(key);
            for (int i = 0; i < x.length; i++) {
                if (ArrayUtils.contains(resultNodes, i)) {
                    x[i] = 1.0D;
                } else {
                    x[i] = 0.0D;
                }
            }
            double runTime = fileMapResult.get(key);
            for (FuncType funcID : funcs) {
                Function func = ScoreFunctionFactory.getFunction(funcID, b, c, sigma);
                ArrayList<Double> fValues = new ArrayList<Double>();
                fValues.add(func.getFuncValue(x));
                //write result table
                //fileName ; rumTime ; functionID ; iters ; funcValues ; nodes
                FileWriter fileWriter = new FileWriter(new File(resultFileName), true);
                fileWriter.write(key + " ; " + runTime + " ; " + funcID + " ; " + fValues.size() + " ; ");
                if (fValues.size() == 1) {
                    fileWriter.write(fValues.get(0) + " ; ");
                } else {
                    for (int i = 0; i < fValues.size() - 1; i++) {
                        fileWriter.write(fValues.get(i) + ",");
                    }
                    fileWriter.write(fValues.get(fValues.size() - 1) + " ; ");
                }

                if (resultNodes != null) {
                    if (resultNodes.length == 1) {
                        fileWriter.write(resultNodes[0] + " ; ");
                    } else {
                        String str = "";
                        for (int i = 0; i < resultNodes.length - 1; i++) {
                            str = str + resultNodes[i] + ",";
                        }
                        str = str + resultNodes[resultNodes.length - 1];
                        fileWriter.write(str + "\n");
                    }
                }
                fileWriter.close();
            }
        }
    }


    public static void transportation(String date) throws IOException {
        String fileNodesName = "output/Transportation/EdgeLasso/edgeLasso_transportation_resultNodes_" + date + ".txt";
        String fileTableName = "output/Transportation/EdgeLasso/edgeLasso_transportation_resultTable_" + date + ".txt";
        String resultFileName = "output/Transportation/edgeLasso_transportationResult.txt";

//    fileNodesName = "output/Transportation/GraphLaplacian/graphLaplacian_transportation_resultNodes_"+date+".txt"; 
//    fileTableName = "output/Transportation/GraphLaplacian/graphLaplacian_transportation_resultTable_"+date+".txt"; 
//    resultFileName = "output/Transportation/graphLaplacian_transportationResult.txt" ;
//    //
//    fileNodesName = "output/Transportation/LTSS/ltss_transportation_resultNodes_"+date+".txt"; 
//    fileTableName = "output/Transportation/LTSS/ltss_transportation_resultTable_"+date+".txt"; 
//    resultFileName = "output/Transportation/ltss_transportationResult.txt" ;
        //
//    fileNodesName = "output/Transportation/NPHGS/nphgs_transportation_resultNodes_"+date+".txt"; 
//    fileTableName = "output/Transportation/NPHGS/nphgs_transportation_resultTable_"+date+".txt"; 
//    resultFileName = "output/Transportation/nphgs_transportationResult.txt" ;
        //
        //
//    fileNodesName = "output/Transportation/EventTree/eventTree_transportation_resultNodes_"+date+".txt"; 
//    fileTableName = "output/Transportation/EventTree/eventTree_transportation_resultTable_"+date+".txt"; 
//    resultFileName = "output/Transportation/eventTree_transportationResult.txt" ;
        //
        fileNodesName = "output/Transportation/AdditiveScan/additiveScan_Transportation_resultNodes_" + date + ".txt";
        fileTableName = "output/Transportation/AdditiveScan/additiveScan_Transportation_resultTable_" + date + ".txt";
        resultFileName = "output/Transportation/additiveScan_transportationResult.txt";
        //
        fileNodesName = "output/Transportation/DepthFirst/depthFirstScan_Transportation_resultNodes_" + date + ".txt";
        fileTableName = "output/Transportation/DepthFirst/depthFirstScan_Transportation_resultTable_" + date + ".txt";
        resultFileName = "output/Transportation/depthFirstScan_transportationResult.txt";

        HashMap<String, int[]> fileMapNodes = new HashMap<String, int[]>();
        for (String line : Files.readAllLines(Paths.get(fileNodesName))) {
            line = line.trim();
            String[] items = line.split(" ");
            String fileID = items[0];
            int[] nodes = null;
            for (int i = 1; i < items.length; i++) {
                nodes = ArrayUtils.add(nodes, Integer.parseInt(items[i]));
            }
            fileMapNodes.put(fileID, nodes);
        }

        HashMap<String, Double> fileMapResult = new HashMap<String, Double>();
        for (String line : Files.readAllLines(Paths.get(fileTableName))) {
            line = line.trim();
            String[] items = line.split("\t");
            String fileID = items[5];
            double runTime = Double.parseDouble(items[2]);
            fileMapResult.put(fileID, runTime);
        }

        for (String key : fileMapNodes.keySet()) {
            APDMInputFormat apdm = null;
            if (key.endsWith(".txt")) {
                apdm = new APDMInputFormat(new File("data/Transportation/" + date + "/" + key));
            } else {
                apdm = new APDMInputFormat(new File("data/Transportation/" + date + "/" + key + ".txt"));
            }
            double[] c = apdm.data.speed;
            double[] b = apdm.data.meanSpeed;
            for (int i = 0; i < b.length; i++) {
                if (b[i] <= 0.0D) {
                    b[i] = 0.01D;
                }
            }
            double[] x = new double[apdm.data.numNodes];
            int[] resultNodes = fileMapNodes.get(key);
            for (int i = 0; i < x.length; i++) {
                if (ArrayUtils.contains(resultNodes, i)) {
                    x[i] = 1.0D;
                } else {
                    x[i] = 0.0D;
                }
            }
            double runTime = fileMapResult.get(key);
            for (FuncType funcID : funcs) {
                Function func = ScoreFunctionFactory.getFunction(funcID, b, c, null);
                ArrayList<Double> fValues = new ArrayList<Double>();
                fValues.add(func.getFuncValue(x));
                //write result table
                //fileName ; rumTime ; functionID ; iters ; funcValues ; nodes
                FileWriter fileWriter = new FileWriter(new File(resultFileName), true);
                fileWriter.write(key + " ; " + runTime + " ; " + funcID + " ; " + fValues.size() + " ; ");
                if (fValues.size() == 1) {
                    fileWriter.write(fValues.get(0) + " ; ");
                } else {
                    for (int i = 0; i < fValues.size() - 1; i++) {
                        fileWriter.write(fValues.get(i) + ",");
                    }
                    fileWriter.write(fValues.get(fValues.size() - 1) + " ; ");
                }

                if (resultNodes != null) {
                    if (resultNodes.length == 1) {
                        fileWriter.write(resultNodes[0] + " ; ");
                    } else {
                        String str = "";
                        for (int i = 0; i < resultNodes.length - 1; i++) {
                            str = str + resultNodes[i] + ",";
                        }
                        str = str + resultNodes[resultNodes.length - 1];
                        fileWriter.write(str + "\n");
                    }
                }
                fileWriter.close();
            }
        }
    }

    public static void main(String args[]) throws IOException {
        //waterNetwork() ;
//    String[] dates = new String[]{"2013-07-01","2013-07-02","2013-07-03","2013-07-04","2013-07-05","2013-07-06","2013-07-07","2013-07-08","2013-07-09","2013-07-10",
//        "2013-07-11","2013-07-12","2013-07-13","2013-07-14","2013-07-15","2013-07-16","2013-07-17","2013-07-18","2013-07-19","2013-07-20",
//        "2013-07-21","2013-07-22","2013-07-23","2013-07-24","2013-07-25","2013-07-26","2013-07-27","2013-07-28","2013-07-29","2013-07-30","2013-07-31",};
//    for(String date:dates){
//    	System.out.println("processing : "+date);
//      transportation(date) ;  
//    }

        //citHepPh() ;
        crimeOfChicago();
//    FileWriter fileWriter = new FileWriter("output/Transportation/fusedLasso_tranportationResult.txt",true);
//    for(File file : new File("output/Transportation/FusedLasso/").listFiles()){
//      for(String line : Files.readAllLines(Paths.get("output/Transportation/FusedLasso/", file.getName()))){
//        System.out.println("line : "+line);
//        fileWriter.write(line+"\n");
//      }
//    }
//    fileWriter.close();
//	  for(File file:new File("./data/Transportation/2013-07-01").listFiles()){
//		  APDMInputFormat apdm  = new APDMInputFormat(file);
//		  int count = 0 ;
//		  for(double d:apdm.data.PValue){
//			  if(d <= 0.05){
//				  count ++ ;
//			  }
//		  }
//		  System.out.println(count);
//	  }
    }
}
