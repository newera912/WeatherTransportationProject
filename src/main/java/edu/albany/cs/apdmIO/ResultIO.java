package edu.albany.cs.apdmIO;


public interface ResultIO {
    public static String lineSplit = "\n";
    public static String recordSplit = ",";
    public static String itemSplit = " ";
    public static String resultHeader =
            "FileName,AlgoName,DataSet,ScoreFuncID,Score,RunTime,Precision,Recall,"
                    + "Fmeasure,TPR,FPR,NoiseLevel,Para0,Para1,Para2,Para3,ScoreValues,ResultNodes";
}
