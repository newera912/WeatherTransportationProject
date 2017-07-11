import matplotlib.pyplot as plt
import numpy as np
from matplotlib.lines import lineStyles

import os                 
import time   
def loadTop(fileName):
    results=[]
    with open(fileName,"r") as rF:
        for i,line in enumerate(rF.readlines()):
            terms=line.strip().split()
            results.append((int(terms[0]),map(int,terms[1].split(",")),terms[2],map(int,terms[3].split(","))))
            if i>19 :
                break
    return results        
                              

  
                        
def plotCaseDaysSingleStation():
    #dates=["20160301","20160302","20160308","20160309","20160312","20160313","20160324","20160325","20160328","20160405","20160412","20160419","20160421","20160514","20160529","20160621","20160628","20160813","20160911","20160922"]
    
    vars=['i0','i1','i2','i3','i4','i5','i6','i7','i8','i9','i10','i11','i12','i13','i14']
    topResults=loadTop("F:/workspace/git/Graph-MP/outputs/trafficData/TravlTime_CaseStudy/CP/5/debug_traffic_TopK_result-CP_baseMeanDiff_20_s_5_wMax_18_filter_TIncld_0.7_Top_multi.txt")
    VARTYPE={0:"TravlTime"}
    for result in topResults:
        dates=[]
        
        top=result[0]+1
        varTypes=[0]
        vals=result[1]
        dates.append(result[2])
        for i,var in enumerate(vars):
            if i in vals:
                exec "%s=%s"%(vars[i], 1)
            else:
                exec "%s=%s"%(vars[i], 0)
        print i0,i1,i2,i3,i4,i5,i6,i7,i8,i9,i10,i11,i12,i13,i14
        
        mons=["201606"]
        days=['01','02','03','04','05']
        sta_names={}
        for i in range(len(vars)):
            sta_names[i]="TMC_"
        
        
        #expRoot="F:/workspace/git/TranWeatherProject/data/mesonet_data/mesonetExpData/statExpData/"
        for mon in mons:
            for day in days:
                date=str(mon+day)
                
                if date not in dates:
                    #print "Not ",date
                    continue
                #expAvgs=expAvg(expRoot+mon+day+".txt")
                
                #var_type="wind"
                rootpath="F:/workspace/git/Graph-MP/data/trafficData/"
                
                
                
                for varypeIdx in varTypes:
                    fileName=""
                    var_type= VARTYPE[varypeIdx]
                    
                    fileName=rootpath+var_type+"/"+mon+day+".txt"
                    print fileName
                    
                    drawVarPlot(fileName,var_type,mon,day,sta_names,result[3],result[0],i0,i1,i2,i3,i4,i5,i6,i7,i8,i9,i10,i11,i12,i13,i14)
                
                
                             
 


            
plotCaseDaysSingleStation()                     
        

