package edu.albany.cs.apdmIO;

import org.apache.commons.lang3.ArrayUtils;

import java.io.*;

/**
 * @author Baojian
 */
public class APDMOutputTableFormat {

    private String fileName;
    private FileWriter file;

    double[] precision;
    double[] recall;
    double[] fmeasure;
    double[] runningTime;
    double[] bjScore;
    int[] noiseLevel;
    int[] hour;
    String[] dataSource;

    public APDMOutputTableFormat(String fileName) {
        try {
            this.file = new FileWriter(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public APDMOutputTableFormat(FileWriter file) {
        this.file = file;
    }

    public void cleanFile() {
        File file = new File(fileName);
        PrintWriter writer;
        try {
            writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addOneColoum(double precision, double recall, double fmeasure, double runningTime,
                             double bjScore, int hour, int noiseLevel, String dataSource) {
        this.precision = ArrayUtils.add(this.precision, precision);
        this.recall = ArrayUtils.add(this.recall, recall);
        this.fmeasure = ArrayUtils.add(this.fmeasure, fmeasure);
        this.runningTime = ArrayUtils.add(this.runningTime, runningTime);
        this.bjScore = ArrayUtils.add(this.bjScore, bjScore);
        this.hour = ArrayUtils.add(this.hour, hour);
        this.noiseLevel = ArrayUtils.add(this.noiseLevel, noiseLevel);
        this.dataSource = (String[]) ArrayUtils.add(this.dataSource, dataSource);
        try {
            file.write("" + precision + " " + recall + " " + fmeasure + " " + runningTime + " " + bjScore + " " + hour + " " + noiseLevel + " " + dataSource);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
