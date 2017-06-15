package edu.albany.cs.transWeather;

import java.util.HashMap;
import java.util.Map;

import edu.albany.cs.apdmIO.APDMInputFormat;
import edu.albany.cs.graph.TransWeatherRealGraph;

public class MultiVarData {
	public Map<Integer,APDMInputFormat> apdmList=null;
	public Map<Integer,TransWeatherRealGraph> twGraphList=null;
	public int numberOfVariable=0;
	public Map<String, Integer> typeMap8 = new HashMap<String, Integer>() {
		{
		put("temp",0);put("temp9",1);put("press",2);put("wind",3);
		put("windDir",4);put("windMax",5);put("rh",6);put("rad",7);
	}};
	public Map<String, Integer> typeMap5 = new HashMap<String, Integer>() {
		{
			put("temp", 0);
			put("temp9", 1);
			put("wind", 2);
			put("windMax", 3);
			put("rh", 4);
		}
	};
	
	public MultiVarData(String[] filePaths, int varNum) {
		int index=0;
		String type="";
		apdmList=new HashMap<Integer, APDMInputFormat>() ;
		twGraphList=new HashMap<Integer, TransWeatherRealGraph>() ;
		for(String filePath:filePaths){
			//data/mesonet_data/"+var_types[0]+"_APDM/
			type=filePath.split("/")[2].split("_")[0];
			apdmList.put(index,new APDMInputFormat(filePath));
			if (varNum == 5) {
				twGraphList.put(index,
						new TransWeatherRealGraph(apdmList.get(index), type,
								typeMap5.get(type)));
			} else {
				twGraphList.put(index,
						new TransWeatherRealGraph(apdmList.get(index), type,
								typeMap8.get(type)));
			}
			index++;
		}
		numberOfVariable=filePaths.length;
		System.out.print("[Number of Var " + numberOfVariable + "] ");
	}
	
	public TransWeatherRealGraph getGraphWithID(int i){
		for(Map.Entry<Integer,TransWeatherRealGraph> entry : twGraphList.entrySet()){
			if(i==entry.getValue().typeID){
				return entry.getValue();
			}
		}
		return null;
	}
}
