package edu.albany.cs.apdmIO;

import edu.albany.cs.scoreFuncs.FuncType;
import edu.albany.cs.scoreFuncs.Function;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ResultFileWriter extends FileWriter implements ResultIO {

    /**
     * write a file using our result format
     *
     * @param file file will be created and written.
     * @throws IOException
     */
    public ResultFileWriter(File file) throws IOException {
        super(file);
        this.writerHeader();
    }

    /**
     * @param file
     * @param flag appends to existing file if flag equals true, otherwise create
     *             a new file
     * @throws IOException
     */
    public ResultFileWriter(File file, boolean flag) throws IOException {
        super(file, flag);
        if (flag == false || !file.exists() || file.length() == 0) {
            this.writerHeader();
        }
    }

    /**
     * Write a file using file name.
     *
     * @param fileName the file will be created and written.
     * @throws IOException
     */
    public ResultFileWriter(String fileName) throws IOException {
        super(fileName);
        this.writerHeader();
    }

    /**
     * Write a file using file name and flag.
     *
     * @param fileName
     * @param flag     or records will be appended to the file if flag equals true,
     *                 otherwise create a new file.
     * @throws IOException
     */
    public ResultFileWriter(String fileName, boolean flag) throws IOException {
        super(fileName, flag);
        if (flag == false || !new File(fileName).exists() || new File(fileName).length() == 0) {
            writerHeader();
        }
    }

    private void writerHeader() throws IOException {
        String headers = resultHeader;
        write(headers + lineSplit);
        flush();
        close();
    }


    /**
     * Write a record into result file.
     *
     * @param FileName
     * @param AlgoName
     * @param DataSet
     * @param ScoreFuncID
     * @param Score
     * @param RunTime
     * @param Precision
     * @param Recall
     * @param Fmeasure
     * @param TPR
     * @param FPR
     * @param NoiseLevel
     * @param Para0
     * @param Para1
     * @param Para2
     * @param Para3
     * @param ScoreValues
     * @param ResultNodes
     * @throws IOException
     */
    public void writerARecord(String FileName, String AlgoName, String DataSet, FuncType ScoreFuncID,
                              double Score, double RunTime, double Precision, double Recall, double Fmeasure, double TPR,
                              double FPR, double NoiseLevel, Object Para0, Object Para1, Object Para2, Object Para3,
                              double[] ScoreValues, int[] ResultNodes) throws IOException {

        StringBuffer record = new StringBuffer();
        record.append(FileName + recordSplit);
        record.append(AlgoName + recordSplit);
        record.append(DataSet + recordSplit);
        record.append(ScoreFuncID + recordSplit);
        record.append(Score + recordSplit);
        record.append(RunTime + recordSplit);
        record.append(Precision + recordSplit);
        record.append(Recall + recordSplit);
        record.append(Fmeasure + recordSplit);
        record.append(TPR + recordSplit);
        record.append(NoiseLevel + recordSplit);
        record.append(Para0 + recordSplit);
        record.append(Para1 + recordSplit);
        record.append(Para2 + recordSplit);
        record.append(Para3 + recordSplit);
        for (double score : ScoreValues) {
            record.append(score + itemSplit);
        }
        record.delete(record.length() - 1, record.length());
        record.append(recordSplit);
        for (int node : ResultNodes) {
            record.append(node + itemSplit);
        }
        record.delete(record.length() - 1, record.length());
        record.append(lineSplit);
        write(record.toString());
    }

    public void writerARecord(String FileName, String AlgoName, String DataSet, FuncType ScoreFuncID,
                              double Score, double RunTime, double[] ScoreValues, int[] ResultNodes) throws IOException {

        writerARecord(FileName, AlgoName, DataSet, ScoreFuncID, Score, RunTime, -1.0D, -1.0D, -1.0D,
                -1.0D, -1.0D, -1.0D, null, null, null, null, ScoreValues, ResultNodes);
    }

    public void writerARecord(String FileName, String AlgoName, String DataSet, FuncType ScoreFuncID,
                              double Score, double RunTime) throws IOException {

        writerARecord(FileName, AlgoName, DataSet, ScoreFuncID, Score, RunTime, null, null);
    }

    public void writerARecord(String FileName, String AlgoName, String DataSet, FuncType ScoreFuncID,
                              double Score, double RunTime, double[] ScoreValues) throws IOException {
        writerARecord(FileName, AlgoName, DataSet, ScoreFuncID, Score, RunTime, ScoreValues, null);
    }

    /**
     * write result table fileName ; rumTime ; functionID ; iters ; funcValues ;
     * nodes
     */
    public static void resultFileWrite(String fileName, double runTime, Function function,
                                       String resultFileName, ArrayList<Double> fValues, int[] resultNodes) {
        try {
            FileWriter fileWriter = new FileWriter(new File(resultFileName), true);
            fileWriter.write(fileName + " ; " + runTime + " ; " + function.getFuncID() + " ; "
                    + fValues.size() + " ; ");
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
                    fileWriter.write("\n");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * write result table fileName ; rumTime ; functionID ; iters ; funcValues ;
     * nodes
     */
    public static void resultFileWrite(String fileName, double runTime, Function function,
                                       String resultFileName, ArrayList<Double> fValues, int[] resultNodes, double precision, double recall, double fmeasure) {
        try {
            FileWriter fileWriter = new FileWriter(new File(resultFileName), true);
            fileWriter.write(fileName + " ; " + runTime + " ; " + function.getFuncID() + " ; "
                    + fValues.size() + " ; ");
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
                    fileWriter.write(precision + "," + recall + "," + fmeasure);
                    fileWriter.write("\n");
                } else {
                    String str = "";
                    for (int i = 0; i < resultNodes.length - 1; i++) {
                        str = str + resultNodes[i] + ",";
                    }
                    str = str + resultNodes[resultNodes.length - 1];
                    fileWriter.write(str + " ; " + precision + "," + recall + "," + fmeasure);
                    fileWriter.write("\n");
                }
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("resource")
    public static void main(String args[]) throws IOException {
        new ResultFileWriter("./tmp/text.txt", true).writerHeader();
    }

}
