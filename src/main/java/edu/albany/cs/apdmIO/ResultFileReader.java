package edu.albany.cs.apdmIO;

import org.apache.commons.lang3.ArrayUtils;

import edu.albany.cs.scoreFuncs.FuncType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResultFileReader implements ResultIO {

    private ArrayList<Record> allRecords;
    private String resultHeader;

    public ResultFileReader(File file) throws IOException {
        this(file.getPath());
    }


    public ResultFileReader(String fileName) throws IOException {

        List<String> allLines = Files.readAllLines(Paths.get(fileName));
        for (int i = 0; i < allLines.size(); i++) {
            String currentLine = allLines.get(i);
            if (i == 0) { // skip first line
                resultHeader = currentLine;
                continue;
            }
            Record record = new Record();
            String[] items = currentLine.split(recordSplit);
            record.FileName = items[0].trim();
            record.AlgoName = items[1].trim();
            record.DataSet = items[2].trim();
            record.ScoreFuncID = FuncType.valueOf(items[3].trim());
            record.Score = Double.parseDouble(String.format("%.4f", items[4].trim()));
            record.RunTime = Double.parseDouble(String.format("%.4f", items[5].trim()));
            record.Precision = Double.parseDouble(String.format("%.4f", items[6].trim()));
            record.Recall = Double.parseDouble(String.format("%.4f", items[7].trim()));
            record.Fmeasure = Double.parseDouble(String.format("%.4f", items[8].trim()));
            record.TPR = Double.parseDouble(String.format("%.4f", items[9].trim()));
            record.FPR = Double.parseDouble(String.format("%.4f", items[10].trim()));
            record.NoiseLevel = Double.parseDouble(String.format("%.4f", items[11].trim()));
            record.Para0 = Double.parseDouble(String.format("%.4f", items[12].trim()));
            record.Para1 = Double.parseDouble(String.format("%.4f", items[13].trim()));
            record.Para2 = Double.parseDouble(String.format("%.4f", items[14].trim()));
            record.Para3 = Double.parseDouble(String.format("%.4f", items[15].trim()));
            double[] values = null;
            for (String value : items[16].split(itemSplit)) {
                values = ArrayUtils.add(values, Double.parseDouble(String.format("%.4f", value.trim())));
            }
            record.ScoreValues = values;
            int[] resultNodes = null;
            for (String value : items[16].split(itemSplit)) {
                resultNodes =
                        ArrayUtils.add(resultNodes, Integer.parseInt(String.format("%.4f", value.trim())));
            }
            record.ResultNodes = resultNodes;
            allRecords.add(record);
        }
    }

    public String getResultHeader() {
        return resultHeader;
    }

    public Record getRecordByFileName(String fileName) {
        for (Record rec : allRecords) {
            if (rec.FileName.equals(fileName)) {
                return rec;
            }
        }
        return null;
    }

    public List<Record> getAllRecords() {
        return allRecords;
    }

    public HashMap<String, Double> getAllPrecision() {
        HashMap<String, Double> precs = new HashMap<String, Double>();
        for (Record rec : allRecords) {
            String fileName = rec.FileName;
            precs.put(fileName, rec.Precision);
        }
        return precs;
    }

    public HashMap<String, Double> getAllRecall() {
        HashMap<String, Double> recs = new HashMap<String, Double>();
        for (Record rec : allRecords) {
            String fileName = rec.FileName;
            recs.put(fileName, rec.Precision);
        }
        return recs;
    }

    public boolean checkAlreadyDone(String fileName, FuncType func) {
        for (Record record : allRecords) {
            if (func.equals(record.ScoreFuncID)) {
                if (record.FileName.equals(fileName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String args[]) throws IOException {
        new ResultFileReader("./tmp/text.txt");
    }

}
