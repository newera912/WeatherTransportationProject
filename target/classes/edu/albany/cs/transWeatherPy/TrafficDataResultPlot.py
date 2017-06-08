import matplotlib.pyplot as plt
import numpy as np
from matplotlib.lines import lineStyles

import os
import random 
                
import time  
Light_cnames={'mistyrose':'#FFE4E1','navajowhite':'#FFDEAD','seashell':'#FFF5EE','papayawhip':'#FFEFD5','blanchedalmond':'#FFEBCD','white':'#FFFFFF','mintcream':'#F5FFFA','antiquewhite':'#FAEBD7','moccasin':'#FFE4B5','ivory':'#FFFFF0','lightgoldenrodyellow':'#FAFAD2','lightblue':'#ADD8E6','floralwhite':'#FFFAF0','ghostwhite':'#F8F8FF','honeydew':'#F0FFF0','linen':'#FAF0E6','snow':'#FFFAFA','lightcyan':'#E0FFFF','cornsilk':'#FFF8DC','bisque':'#FFE4C4','aliceblue':'#F0F8FF','gainsboro':'#DCDCDC','lemonchiffon':'#FFFACD','lightyellow':'#FFFFE0','lavenderblush':'#FFF0F5','whitesmoke':'#F5F5F5','beige':'#F5F5DC','azure':'#F0FFFF','oldlace':'#FDF5E6'}
 
def loadTop(fileName):
    results=[]
    with open(fileName,"r") as rF:
        for i,line in enumerate(rF.readlines()):
            terms=line.strip().split()
            results.append((int(terms[0]),map(int,terms[1].split(",")),terms[2],map(int,terms[3].split(","))))
            if i>199 :
                break
    return results        
                              

def drawVarPlot(fileName,varType,dates,sta_names,time_region,topN,vals,colors,SM,dir,ex,root,mWin): 
    
    
    day_data=[]
    with open(fileName,"r") as df:
        for line in df.readlines():
            terms=line.strip().split()
            sta_name="TMC"
            data=map(float,terms[1:])
            day_data.append((sta_name,dates,data))   
    
            
    X=[i for i in range(0,len(day_data[0][2]))]  
    label=[(str(i)+"\n"+str(i*5/60)+"h") for i in range(0,len(day_data[0][2])+2,12)]
    labelY=[str(i) for i in range(0,100+1,5)]
    #print sta_names[int(day_data[0][0])]
    #print day_data[i3][2] 
    
    fig=plt.figure(1)
    y_min=0.0     
    y_max=0.0   
         
    for i in vals:
        temp_max=np.max(day_data[i][2][time_region[0]:time_region[1]])
        if y_max<temp_max:
            y_max=temp_max
        plt.plot(X,day_data[i][2],color=colors[i],linewidth='1.2', markersize=5,label="TMC-"+str(i))               
         
    plt.axvline(x=time_region[0], ymin=-1.0, ymax=50.0,color='r',linestyle='--')
    plt.axvline(x=time_region[1], ymin=-1.0, ymax=50.0,color='r',linestyle='--')
    plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
    plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
    if y_max<1800:
        plt.ylim([0.0,1800])
    else:
        plt.ylim([0.0,y_max+100])
    if dir[0]=='E':
        plt.title("Top"+str(topN)+" "+dates+" "+varType+" I90 to East")
    else:
        plt.title("Top"+str(topN)+" "+dates+" "+varType+" I90 to West")
        
    
    #plt.xticks(np.arange(min(X), max(X)+2, 12.0))
    plt.xticks(np.arange(min(X), max(X)+2, 12.0),label)
    #plt.yticks(np.arange(-1, 50, 5.0),labelY)
    plt.tick_params(axis='both', which='major', labelsize=7)
    plt.xlabel('Time: 00:00 ~23:59,each 5min')

    plt.grid()
    
    #plt.xlim([0.2,0.0])
    lgd=plt.legend(loc='center right', bbox_to_anchor=(1.1, 0.5),fontsize=8)

    #plt.plot(X,day_data2[i][2],'r-',linewidth='1.0', markersize=5,label='Temp '+sta_names[int(day_data2[i][0])]+day_data2[i][0]) 
    
            
    if SM=="single":        
        fig.savefig(root+ex+"-"+str(mWin)+"/"+ex+'_single_'+str(topN)+'_'+varType+'_'+dates+'.png', dpi=300,bbox_extra_artists=(lgd,), bbox_inches='tight')
    else:
        fig.savefig(root+ex+"-"+str(mWin)+"/"+ex+'_multi_'+str(topN)+'_'+varType+'_'+dates+'.png', dpi=300,bbox_extra_artists=(lgd,), bbox_inches='tight')
    
    fig.clf()   
                        
def plotCaseDaysSingleStation():
    #dates=["20160301","20160302","20160308","20160309","20160312","20160313","20160324","20160325","20160328","20160405","20160412","20160419","20160421","20160514","20160529","20160621","20160628","20160813","20160911","20160922"]
    import matplotlib
    
    colors=[]
    cnames=matplotlib.colors.cnames
    for name, hex in cnames.iteritems():
        if name not in Light_cnames.keys():
            colors.append(hex) 
#     for i,c in enumerate(colors):
#         print i,c  
    root="F:/workspace/git/WeatherTransportationProject/outputs/trafficData/travelTime_CaseStudy/CPBest/"
    mWin=24
    ex="W621"
    dir="W621"
    #dir='E'
    for SM in ['multi']:
        if not os.path.exists(root+ex+"-"+str(mWin)+"/"):
            os.makedirs(root+ex+"-"+str(mWin)+"/")
        topResults=loadTop(root+"5/"+ex+"I90traffic_AllYearEvent_TopK_result_baseMeanDiff_20_s_5_wMax_"+str(mWin)+"_filter_TIncld_0.7_Top_"+SM+".txt")
        VARTYPE={0:"travelTime"}
        topk=0
        for result in topResults:
            
            
            top=topk+1
            topk+=1
            varTypes=[0]
            vals=result[1]
            dates=result[2]
           
            sta_names={}
            for i in range(len(vals)):
                sta_names[i]="TMC_"
            
            if dir[0]=='W':
                rootpath="F:/workspace/git/WeatherTransportationProject/data/trafficData/I90_TravelTime/TravelTimeToWest/"
            else:
                rootpath="F:/workspace/git/WeatherTransportationProject/data/trafficData/I90_TravelTime/TravelTimeToEast/"

            
            
            for varypeIdx in varTypes:
                fileName=""
                var_type= VARTYPE[varypeIdx]
                
                fileName=rootpath+"/"+dates+".txt"
                print str(SM)+" "+str(top)+" "+fileName.split("/")[-1]+" "+dir +str(mWin)
                
                drawVarPlot(fileName,var_type,dates,sta_names,result[3],top,vals,colors,SM,dir,ex,root,mWin)
           
plotCaseDaysSingleStation()                     
        

