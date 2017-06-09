package edu.albany.cs.apdmIO;

import edu.albany.cs.scoreFuncs.FuncType;

public class Record {
    public String FileName;
    public String AlgoName;
    public String DataSet;
    public FuncType ScoreFuncID;
    public double Score;
    public double RunTime;
    public double Precision;
    public double Recall;
    public double Fmeasure;
    public double TPR;
    public double FPR;
    public double NoiseLevel;
    public Object Para0;
    public Object Para1;
    public Object Para2;
    public Object Para3;
    public double[] ScoreValues;
    public int[] ResultNodes;
}