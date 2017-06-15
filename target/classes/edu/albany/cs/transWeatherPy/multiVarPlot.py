import matplotlib.pyplot as plt
import numpy as np
from matplotlib.lines import lineStyles

import os                 
import time   
def loadTop(fileName):
    results=[]
    with open(fileName,"r") as rF:
        for i,line in enumerate(rF.readlines()):
            print line
            terms=line.strip().split(" ")
            results.append((int(terms[0]),map(int,terms[1].split(",")),map(int,terms[2].split(",")),terms[3],map(int,terms[4].split(","))))
            if i>19 :
                break
    return results        
                              

def drawVarPlot(fileName,varType,mon,day,sta_names,time_region,topN,relatedVar): 
    
    
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
    color=['b','r','k','g','y','c','','m','#B47CC7','#FBC15E','#e5ee38']        
   
    for i in relatedVar:        
        plt.plot(X,day_data[i][2],color=color[i],linewidth='1.2', markersize=5,label=sta_names[int(day_data[i][0])]+day_data[i][0])               
    
    plt.axvline(x=time_region[0], ymin=-1.0, ymax=50.0,color='r',linestyle='--',linewidth='1.5')
    plt.axvline(x=time_region[1], ymin=-1.0, ymax=50.0,color='r',linestyle='--',linewidth='1.5')
    plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.,fontsize=12)
    plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
    #plt.ylim([-1.0,50.0])
    plt.title("Top "+str(topN)+" "+mon+day+" " +varType)
    #plt.xticks(np.arange(min(X), max(X)+2, 12.0))
    plt.xticks(np.arange(min(X), max(X)+2, 12.0),label)
    #plt.yticks(np.arange(-1, 50, 5.0),labelY)
    plt.tick_params(axis='both', which='major', labelsize=7)
    plt.xlabel('Time from 00:00 ~23:59,every 5min')

    plt.grid()
    
    #plt.xlim([0.2,0.0])
    plt.legend(loc='best',fontsize=12)
   
#                 fig.subplots_adjust(bottom = 2)
#                 fig.subplots_adjust(top = 2)
#                 fig.subplots_adjust(right = 2)
#                 fig.subplots_adjust(left = 0)
    
    
    #plt.plot(X,day_data2[i][2],'r-',linewidth='1.0', markersize=5,label='Temp '+sta_names[int(day_data2[i][0])]+day_data2[i][0]) 
    fig.savefig('F:/workspace/git/WeatherTransportationProject/outputs/mesonetPlots/multi_CaseStudy/mvPlotsNew/Months/ACase_Study'+str(topN)+'_'+varType+'_'+str(mon+day)+'.png', dpi=300)
    fig.clf()   
                        
def plotCaseDaysSingleStation():
    #dates=["20160301","20160302","20160308","20160309","20160312","20160313","20160324","20160325","20160328","20160405","20160412","20160419","20160421","20160514","20160529","20160621","20160628","20160813","20160911","20160922"]
    
    
    topResults=loadTop("F:/workspace/git/WeatherTransportationProject/outputs/mesonetPlots/multi_CaseStudy/CP/2/AllYearEvent_multiGraphMP_TopK_result-CP_baseMeanDiff_20_s_2_wMax_18_filter_TIncld_0.720160412_Top.txt")
    #VARTYPE={0:"temp",1:"temp9",2:"press",3:"wind",4:"windDir",5:"windMax",6:"rh",7:"rad"}
    VARTYPE={0:"temp",1:"temp9",2:"wind",3:"windMax",4:"rh"}
    for result in topResults:
        dates=[]
        
        top=result[0]+1
        varTypes=result[1]
        vals=result[2]
        dates.append(result[3])
        relatedVar=[]
        for var in varTypes:
            relatedVar.append(int(var))
        
        mons=["201603","201604","201605","201606","201607","201608","201609"]
        days=['01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31']
        sta_names={0:"BATA",1:"SBRI",2:"WATE",3:"JORD",4:"CSQR",5:"WEST",6:"COLD",7:"SPRA",8:"COBL",9:"STEP"}
        
        
        #expRoot="F:/workspace/git/TranWeatherProject/data/mesonet_data/mesonetExpData/statExpData/"
        for mon in mons:
            for day in days:
                date=str(mon+day)
                
                if date not in dates:
                    #print "Not ",date
                    continue
                #expAvgs=expAvg(expRoot+mon+day+".txt")
                
                #var_type="wind"
                rootpath="F:/workspace/git/WeatherTransportationProject/data/mesonet_data/"
                
                
                
                for varypeIdx in varTypes:
                    fileName=""
                    var_type= VARTYPE[varypeIdx]
                    
                    fileName=rootpath+var_type+"/"+mon+day+".txt"
                    print fileName
                    
                    drawVarPlot(fileName,var_type,mon,day,sta_names,result[4],result[0],relatedVar)
                
                
                             
 


            
plotCaseDaysSingleStation()                     
        

