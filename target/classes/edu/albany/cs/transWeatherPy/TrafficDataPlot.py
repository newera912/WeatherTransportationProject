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
                              

def drawVarPlot(fileName,varType,mon,day,sta_names,time_region,topN,i0,i1,i2,i3,i4,i5,i6,i7,i8,i9,i10,i11,i12,i13,i14): 
    
    
    day_data=[]
    with open(fileName,"r") as df:
        for line in df.readlines():
            terms=line.strip().split()
            sta_name=terms[0]
            data=map(float,terms[1:])
            day_data.append((sta_name,mon+day,data))   
    
            
    X=[i for i in range(0,len(day_data[0][2]))]  
    label=[(str(i)+"\n"+str(i*5/60)+"h") for i in range(0,len(day_data[0][2])+1,12)]
    labelY=[str(i) for i in range(0,100+1,5)]
    #print sta_names[int(day_data[0][0])]
    #print day_data[i3][2] 
    
    fig=plt.figure(1) 
            
   
    if i0!=0:         
        plt.plot(X,day_data[0][2],'b-',linewidth='0.5', markersize=5,label=sta_names[int(day_data[0][0])]+day_data[0][0])               
    if i1!=0:         
        plt.plot(X,day_data[1][2],'r-',linewidth='0.5', markersize=5,label=str(sta_names[int(day_data[1][0])])+day_data[1][0]) 
    if i2!=0:         
        plt.plot(X,day_data[2][2],'k-',linewidth='0.5', markersize=5,label=str(sta_names[int(day_data[2][0])])+day_data[2][0]) 
    if i3!=0:        
        plt.plot(X,day_data[3][2],'g-',linewidth='0.5', markersize=5,label=str(sta_names[int(day_data[3][0])])+day_data[3][0]) 
    if i4!=0:         
        plt.plot(X,day_data[4][2],'y-',linewidth='0.5', markersize=5,label=str(sta_names[int(day_data[4][0])])+day_data[4][0]) 
    if i5!=0:         
        plt.plot(X,day_data[5][2],'c-',linewidth='0.5', markersize=5,label=str(sta_names[int(day_data[5][0])])+day_data[5][0]) 
    if i6!=0:         
        plt.plot(X,day_data[6][2],'m-',linewidth='0.5', markersize=5,label=str(sta_names[int(day_data[6][0])])+day_data[6][0]) 
    if i7!=0:         
        plt.plot(X,day_data[7][2],color ='#B47CC7',linewidth='0.5', markersize=5,label=str(sta_names[int(day_data[7][0])])+day_data[7][0]) 
    if i8!=0:         
        plt.plot(X,day_data[8][2],color='#FBC15E',linewidth='0.5', markersize=5,label=str(sta_names[int(day_data[8][0])])+day_data[8][0]) 
    if i9!=0:         
        plt.plot(X,day_data[9][2],color='#e5ee38',linewidth='0.5', markersize=5,label=str(sta_names[int(day_data[9][0])])+day_data[9][0]) 
    if i10!=0:         
        plt.plot(X,day_data[10][2],color='#e5bc38',linewidth='0.5', markersize=5,label=str(sta_names[int(day_data[10][0])])+day_data[10][0]) 
    if i11!=0:         
        plt.plot(X,day_data[11][2],color='#e5cb38',linewidth='0.5', markersize=5,label=str(sta_names[int(day_data[11][0])])+day_data[11][0]) 
    if i12!=0:         
        plt.plot(X,day_data[12][2],color='#e5ee38',linewidth='0.5', markersize=5,label=str(sta_names[int(day_data[12][0])])+day_data[12][0]) 
    if i13!=0:         
        plt.plot(X,day_data[13][2],color='#e6ec38',linewidth='0.5', markersize=5,label=str(sta_names[int(day_data[13][0])])+day_data[13][0]) 
          
    if i14!=0:         
        plt.plot(X,day_data[14][2],color='#ccee38',linewidth='0.5', markersize=5,label=str(sta_names[int(day_data[14][0])])+day_data[14][0]) 
              
    plt.axvline(x=time_region[0], ymin=-1.0, ymax=50.0,color='k',linestyle='--')
    plt.axvline(x=time_region[1], ymin=-1.0, ymax=50.0,color='k',linestyle='--')
    plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
    plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
    #plt.ylim([-1.0,50.0])
    plt.title("Top"+str(topN+1)+" "+mon+day +varType)
    #plt.xticks(np.arange(min(X), max(X)+2, 12.0))
    plt.xticks(np.arange(min(X), max(X)+2, 12.0),label)
    #plt.yticks(np.arange(-1, 50, 5.0),labelY)
    plt.tick_params(axis='both', which='major', labelsize=7)
    plt.xlabel('Time from 00:00 ~23:59,each 5min')

    plt.grid()
    
    #plt.xlim([0.2,0.0])
    plt.legend(loc='best',fontsize=8)
   
#                 fig.subplots_adjust(bottom = 2)
#                 fig.subplots_adjust(top = 2)
#                 fig.subplots_adjust(right = 2)
#                 fig.subplots_adjust(left = 0)
    
    
    #plt.plot(X,day_data2[i][2],'r-',linewidth='1.0', markersize=5,label='Temp '+sta_names[int(day_data2[i][0])]+day_data2[i][0]) 
    fig.savefig('F:/workspace/git/Graph-MP/data/trafficData/multi_'+str(topN)+'_'+varType+'_'+str(mon+day)+'.png', dpi=300)
    fig.clf()   
                        
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
        

