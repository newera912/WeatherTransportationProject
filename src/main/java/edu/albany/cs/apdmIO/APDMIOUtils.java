package edu.albany.cs.apdmIO;

import java.io.File;
import java.util.HashMap;

/**
 * @author Baojian
 */
public class APDMIOUtils {

    private APDMOutputFormat apdmOut;
    private APDMInputFormat apdmIn;

    public APDMIOUtils(APDMOutputFormat apdmOut) {
        this.apdmOut = apdmOut;
    }

    public APDMIOUtils(APDMInputFormat apdmIn) {
        this.apdmIn = apdmIn;
    }

    public int getNumOfNodes() {
        return this.apdmIn.data.numNodes;
    }

    public static void main(String args[]) {
        HashMap<String, Double> precision = new HashMap<String, Double>();
        HashMap<String, Double> recall = new HashMap<String, Double>();
        String rootFolderName = "simuDataResult/SNAPDataSet/KDDGreedy/";//"realDataResult/WaterData/KDDGreedy"; //the folder you need to change
        File file = new File(rootFolderName);

        if (file.isDirectory()) {
        }

        for (String key : precision.keySet()) {
            System.out.println("key : " + key + " value: " + precision.get(key));
        }

        System.out.println();
        System.out.println();
        for (String key : recall.keySet()) {
            System.out.println("key : " + key + " value: " + recall.get(key));
        }
    }

}
