import matplotlib.pyplot as plt
import numpy as np
from matplotlib.lines import lineStyles

Light_cnames={'mistyrose':'#FFE4E1','navajowhite':'#FFDEAD','seashell':'#FFF5EE','papayawhip':'#FFEFD5','blanchedalmond':'#FFEBCD','white':'#FFFFFF','mintcream':'#F5FFFA','antiquewhite':'#FAEBD7','moccasin':'#FFE4B5','ivory':'#FFFFF0','lightgoldenrodyellow':'#FAFAD2','lightblue':'#ADD8E6','floralwhite':'#FFFAF0','ghostwhite':'#F8F8FF','honeydew':'#F0FFF0','linen':'#FAF0E6','snow':'#FFFAFA','lightcyan':'#E0FFFF','cornsilk':'#FFF8DC','bisque':'#FFE4C4','aliceblue':'#F0F8FF','gainsboro':'#DCDCDC','lemonchiffon':'#FFFACD','lightyellow':'#FFFFE0','lavenderblush':'#FFF0F5','whitesmoke':'#F5F5F5','beige':'#F5F5DC','azure':'#F0FFFF','oldlace':'#FDF5E6'}
def plot10seperate():
    mons=["201603","201604","201605","201606","201607","201608","201609","201610","201611","201612","201701","201702","201703","201704","201705","201706"]
    days=['01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31']
    rootpath="F:/workspace/git/TranWeatherProject/data/mesonet_data/"
    for mon in mons:
        for day in days:
            print mon+day
            fileName=rootpath+mon+day+".txt"
            day_data=[]
            with open(fileName,"r") as df:
                for line in df.readlines():
                    terms=line.strip().split()
                    sta_name=terms[0]
                    data=map(float,terms[1:])
                    day_data.append((sta_name,mon+day,data))
            X=[(i*5.0/60.0) for i in range(1,len(day_data[0][2]),1)]
            fig=plt.figure(1)
            fig.add_subplot(10,1,1)
            plt.plot(X,day_data[0][2],'b*-',linewidth='2.0', markersize=5,label='Temperature')         
                 
            plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
            #plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
            
            plt.ylim([-20.0,60.0])
            plt.xlabel('time Period')
            #plt.xlim([0.2,0.0])
            plt.legend(loc='best',fontsize=8)
            plt.title(day_data[0][0]+" Station Date: "+mon+day +"Temperature")
            
            fig.add_subplot(10,1,2)
            plt.plot(X,day_data[1][2],'b*-',linewidth='2.0', markersize=5,label='Temperature')         
                 
            plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
            #plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
            
            plt.ylim([-20.0,60.0])
            plt.xlabel('time Period')
            #plt.xlim([0.2,0.0])
            plt.legend(loc='best',fontsize=8)
            plt.title(day_data[1][0]+" Station Date: "+mon+day +"Temperature")
            
            fig.add_subplot(10,1,3)
            plt.plot(X,day_data[2][2],'b*-',linewidth='2.0', markersize=5,label='Temperature')         
                 
            plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
            #plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
            
            plt.ylim([-20.0,60.0])
            plt.xlabel('time Period')
            #plt.xlim([0.2,0.0])
            plt.legend(loc='best',fontsize=8)
            plt.title(day_data[2][0]+" Station Date: "+mon+day +"Temperature")
            
            fig.add_subplot(10,1,4)
            plt.plot(X,day_data[3][2],'b*-',linewidth='2.0', markersize=5,label='Temperature')         
                 
            plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
            #plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
            
            plt.ylim([-20.0,60.0])
            plt.xlabel('time Period')
            #plt.xlim([0.2,0.0])
            plt.legend(loc='best',fontsize=8)
            plt.title(day_data[3][0]+" Station Date: "+mon+day +"Temperature")
            
            fig.add_subplot(10,1,5)
            plt.plot(X,day_data[4][2],'b*-',linewidth='2.0', markersize=5,label='Temperature')         
                 
            plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
            #plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
            
            plt.ylim([-20.0,60.0])
            plt.xlabel('time Period')
            #plt.xlim([0.2,0.0])
            plt.legend(loc='best',fontsize=8)
            plt.title(day_data[4][0]+" Station Date: "+mon+day +"Temperature")
            
            fig.add_subplot(10,1,6)
            plt.plot(X,day_data[5][2],'b*-',linewidth='2.0', markersize=5,label='Temperature')         
                 
            plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
            #plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
            
            plt.ylim([-20.0,60.0])
            plt.xlabel('time Period')
            #plt.xlim([0.2,0.0])
            plt.legend(loc='best',fontsize=8)
            plt.title(day_data[5][0]+" Station Date: "+mon+day +"Temperature")
            
            fig.add_subplot(10,1,7)
            plt.plot(X,day_data[6][2],'b*-',linewidth='2.0', markersize=5,label='Temperature')         
                 
            plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
            #plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
            
            plt.ylim([-20.0,60.0])
            plt.xlabel('time Period')
            #plt.xlim([0.2,0.0])
            plt.legend(loc='best',fontsize=8)
            plt.title(day_data[6][0]+" Station Date: "+mon+day +"Temperature")
            
            fig.add_subplot(10,1,8)
            plt.plot(X,day_data[7][2],'b*-',linewidth='2.0', markersize=5,label='Temperature')         
                 
            plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
            #plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
            
            plt.ylim([-20.0,60.0])
            plt.xlabel('time Period')
            #plt.xlim([0.2,0.0])
            plt.legend(loc='best',fontsize=8)
            plt.title(day_data[7][0]+" Station Date: "+mon+day +"Temperature")
            
            fig.add_subplot(10,1,9)
            plt.plot(X,day_data[8][2],'b*-',linewidth='2.0', markersize=5,label='Temperature')         
                 
            plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
            #plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
            
            plt.ylim([-20.0,60.0])
            plt.xlabel('time Period From 00:00am ~23:59')
            #plt.xlim([0.2,0.0])
            plt.legend(loc='best',fontsize=8)
            plt.title(day_data[8][0]+" Station Date: "+mon+day +"Temperature")
            
            fig.add_subplot(10,1,10)
            plt.plot(X,day_data[9][2],'b*-',linewidth='2.0', markersize=5,label='Temperature')         
                 
            plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
            #plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
            
            plt.ylim([-20.0,60.0])
            plt.xlabel('time Period')
            #plt.xlim([0.2,0.0])
            plt.legend(loc='best',fontsize=8)
            plt.title(day_data[9][0]+" Station Date: "+mon+day +"Temperature")
            
            plt.tight_layout(pad=0.4, w_pad=0.5, h_pad=1.0)
            plt.show()
            fig.savefig('F:/workspace/git/TranWeatherProject/outputs/mesonetPlots/'+str(mon+day)+'.png')
            plt.close()
import os     
def plotSignle():
    mons=["201603","201604","201605","201606","201607","201608","201609"]
    #mons=["201604"]
    #mons=["201609"]
    days=['01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31']
    #days=[""]
    sta_names={0:"BATA",1:"SBRI",2:"WATE",3:"JORD",4:"CSQR",5:"WEST",6:"COLD",7:"SPRA",8:"COBL",9:"STEP"}
    var_type="precip"
    rootpath="F:/workspace/git/Graph-MP/data/mesonet_data/"+var_type+"/"
    for mon in mons:
        for day in days:
            fileName=rootpath+mon+day+".txt"
            print fileName
            day_data=[]
            if not os.path.exists(fileName):
                continue 
            with open(fileName,"r") as df:
                for line in df.readlines():
                    terms=line.strip().split()
                    sta_name=terms[0]
                    data=map(float,terms[1:])
                    day_data.append((sta_name,mon+day,data))
            X=[i for i in range(0,len(day_data[0][2]))]  
            label=[(str(i)+"\n"+str(i*5/60)+"h") for i in range(0,len(day_data[0][2])+1,12)]
            print sta_names[int(day_data[0][0])]          
            fig=plt.figure(1)            
            plt.plot(X,day_data[0][2],'b-',linewidth='1.0', markersize=5,label=sta_names[int(day_data[0][0])]+day_data[0][0])         
            plt.plot(X,day_data[1][2],'r-',linewidth='1.0', markersize=5,label=str(sta_names[int(day_data[1][0])])+day_data[1][0]) 
            plt.plot(X,day_data[2][2],'k-',linewidth='1.0', markersize=5,label=str(sta_names[int(day_data[2][0])])+day_data[2][0]) 
            plt.plot(X,day_data[3][2],'g-',linewidth='1.0', markersize=5,label=str(sta_names[int(day_data[3][0])])+day_data[3][0]) 
            plt.plot(X,day_data[4][2],'y-',linewidth='1.0', markersize=5,label=str(sta_names[int(day_data[4][0])])+day_data[4][0]) 
            plt.plot(X,day_data[5][2],'c-',linewidth='1.0', markersize=5,label=str(sta_names[int(day_data[5][0])])+day_data[5][0]) 
            plt.plot(X,day_data[6][2],'m-',linewidth='1.0', markersize=5,label=str(sta_names[int(day_data[6][0])])+day_data[6][0]) 
            plt.plot(X,day_data[7][2],color ='#B47CC7',linewidth='1.0', markersize=5,label=str(sta_names[int(day_data[7][0])])+day_data[7][0]) 
            plt.plot(X,day_data[8][2],color='#FBC15E',linewidth='1.0', markersize=5,label=str(sta_names[int(day_data[8][0])])+day_data[8][0]) 
            plt.plot(X,day_data[9][2],color='#e5ee38',linewidth='1.0', markersize=5,label=str(sta_names[int(day_data[9][0])])+day_data[9][0]) 
               
            plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
            plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
            
            
            
            if var_type=="wind":
                plt.ylim([-5.0,70.0])
                plt.ylabel('Avg. Wind Speed(mph)')
                plt.title(mon+day +"Every 5min Avg. Wind")
            elif type=="temp":
                plt.ylim([-10.0,100.0])
                plt.ylabel('Temperature(F)')
                plt.title(mon+day +"Temperature")
            else:
                plt.ylim([-1.0,2.0])
                plt.ylabel('Precipitation Est (Inch)')
                plt.title(mon+day +"Precipitation")
            #plt.xticks(np.arange(min(X), max(X)+2, 12.0))
            print len(X)
            plt.xticks(np.arange(min(X), max(X)+2, 12.0),label)
            plt.tick_params(axis='both', which='major', labelsize=7)
            plt.xlabel('Time from 00:00 ~23:59,each 5min')

            
            
            #plt.xlim([0.2,0.0])
            plt.legend(loc='best',fontsize=8)
            
            
            plt.grid()
            
            #plt.tight_layout(pad=0.4, w_pad=0.5, h_pad=1.0)
            #plt.show()
            fig.savefig('F:/workspace/git/Graph-MP/outputs/mesonetPlots/'+var_type+'_plots/'+str(mon+day)+'.png')
            plt.close()

def expAvg(fileName):
    expAvgs=[]
    expMin=[]
    expMax=[]
    with open(fileName,"r") as oF:
        for line in oF.readlines():
            expAvgs.append(float(line.strip().split()[0]))
            expMin.append(float(line.strip().split()[1]))
            expMax.append(float(line.strip().split()[3]))
    return expAvgs,expMin,expMax

def plotCaseDays():
    dates=["20160301","20160302","20160308","20160309","20160312","20160313","20160324","20160325","20160328","20160405","20160412","20160419","20160421","20160514","20160529","20160621","20160628","20160813","20160911","20160922"]
    mons=["201603","201604","201605","201606","201607","201608","201609"]
    days=['01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31']
    sta_names={0:"BATA",1:"SBRI",2:"WATE",3:"JORD",4:"CSQR",5:"WEST",6:"COLD",7:"SPRA",8:"COBL",9:"STEP"}
    var_type="temp"
    rootpath="F:/workspace/git/TranWeatherProject/data/mesonet_data/"+var_type+"/"
    #expRoot="F:/workspace/git/TranWeatherProject/data/mesonet_data/mesonetExpData/statExpData/"
    for mon in mons:
        for day in days:
            date=str(mon+day)
            
#             if date not in dates:
#                 print "Not ",date
#                 continue
            #expAvgs=expAvg(expRoot+mon+day+".txt")
            fileName=rootpath+mon+day+".txt"
            print fileName
            day_data=[]
            if not os.path.exists(fileName):
                print "File Not Found",fileName
                continue 
            with open(fileName,"r") as df:
                for line in df.readlines():
                    terms=line.strip().split()
                    sta_name=terms[0]
                    data=map(float,terms[1:])
                    day_data.append((sta_name,mon+day,data))
            X=[i for i in range(0,len(day_data[0][2]))]  
            label=[(str(i)+"\n"+str(i*5/60)+"h") for i in range(0,len(day_data[0][2])+1,12)]
            labelY=[str(i) for i in range(0,100+1,5)]
            print sta_names[int(day_data[0][0])]          
            fig=plt.figure(1)            
            plt.plot(X,day_data[0][2],'b-',linewidth='2.0', markersize=5,label=sta_names[int(day_data[0][0])]+day_data[0][0])         
            plt.plot(X,day_data[1][2],'r-',linewidth='2.0', markersize=5,label=str(sta_names[int(day_data[1][0])])+day_data[1][0]) 
            plt.plot(X,day_data[2][2],'k-',linewidth='2.0', markersize=5,label=str(sta_names[int(day_data[2][0])])+day_data[2][0]) 
            plt.plot(X,day_data[3][2],'g-',linewidth='2.0', markersize=5,label=str(sta_names[int(day_data[3][0])])+day_data[3][0]) 
            plt.plot(X,day_data[4][2],'y-',linewidth='2.0', markersize=5,label=str(sta_names[int(day_data[4][0])])+day_data[4][0]) 
            plt.plot(X,day_data[5][2],'c-',linewidth='2.0', markersize=5,label=str(sta_names[int(day_data[5][0])])+day_data[5][0]) 
            plt.plot(X,day_data[6][2],'m-',linewidth='2.0', markersize=5,label=str(sta_names[int(day_data[6][0])])+day_data[6][0]) 
            plt.plot(X,day_data[7][2],color ='#B47CC7',linewidth='2.0', markersize=5,label=str(sta_names[int(day_data[7][0])])+day_data[7][0]) 
            plt.plot(X,day_data[8][2],color='#FBC15E',linewidth='2.0', markersize=5,label=str(sta_names[int(day_data[8][0])])+day_data[8][0]) 
            plt.plot(X,day_data[9][2],color='#e5ee38',linewidth='2.0', markersize=5,label=str(sta_names[int(day_data[9][0])])+day_data[9][0]) 
               
            plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
            plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
            
            
            
            if var_type=="wind":
                #plt.ylim([-5.0,70.0])
                plt.ylabel('Avg. Wind Speed(mph)')
                plt.title(mon+day +"Every 5min Avg. Wind")
            else:
                plt.ylim([-10.0,100.0])
                plt.ylabel('Temperature(F)')
                plt.title(mon+day +"Temperature")
            
            #plt.xticks(np.arange(min(X), max(X)+2, 12.0))
            plt.xticks(np.arange(min(X), max(X)+2, 12.0),label)
            #plt.yticks(np.arange(0, 100, 5.0),labelY)
            plt.tick_params(axis='both', which='major', labelsize=7)
            plt.xlabel('Time from 00:00 ~23:59,every 5min')

            
            
            #plt.xlim([0.2,0.0])
            plt.legend(loc='best',fontsize=8)
            
            
            plt.grid()
            
            #plt.tight_layout(pad=0.4, w_pad=0.5, h_pad=1.0)
            #plt.show()
            fig.savefig('F:/workspace/git/Graph-MP/outputs/mesonetPlots/'+var_type+'_CaseStudy/'+str(mon+day)+'.png', dpi=300)
            plt.close()
def plotSingleDays():
    fileName="F:/workspace/git/Graph-MP/data/mesonet_data/test_4.txt"
    sta_names={0:"BATA",1:"SBRI",2:"WATE",3:"JORD",4:"CSQR",5:"WEST",6:"COLD",7:"SPRA",8:"COBL",9:"STEP"}
    
    day_data=[]
    with open(fileName,"r") as df:
        for line in df.readlines():
            terms=line.strip().split()
            sta_name=terms[0]
            data=map(float,terms[1:288])
            day_data.append((sta_name,'201603001',data))
    X=[i for i in range(0,len(day_data[0][2]))]  
    label=[(str(i)+"\n"+str(i*5/60)+"h") for i in range(0,len(day_data[0][2])+1,12)]
    labelY=[str(i) for i in range(0,100+1,5)]
    print sta_names[int(day_data[0][0])]          
    fig=plt.figure(1)            
    plt.plot(X,day_data[0][2],'b-',linewidth='1.0', markersize=5,label=sta_names[int(day_data[0][0])]+day_data[0][0])         
    plt.plot(X,day_data[1][2],'r-',linewidth='1.0', markersize=5,label=str(sta_names[int(day_data[1][0])])+day_data[1][0]) 
    plt.plot(X,day_data[2][2],'k-',linewidth='1.0', markersize=5,label=str(sta_names[int(day_data[2][0])])+day_data[2][0]) 
    plt.plot(X,day_data[3][2],'g-',linewidth='1.0', markersize=5,label=str(sta_names[int(day_data[3][0])])+day_data[3][0]) 
    plt.plot(X,day_data[4][2],'y-',linewidth='1.0', markersize=5,label=str(sta_names[int(day_data[4][0])])+day_data[4][0]) 
    plt.plot(X,day_data[5][2],'c-',linewidth='1.0', markersize=5,label=str(sta_names[int(day_data[5][0])])+day_data[5][0]) 
    plt.plot(X,day_data[6][2],'m-',linewidth='1.0', markersize=5,label=str(sta_names[int(day_data[6][0])])+day_data[6][0]) 
    plt.plot(X,day_data[7][2],color ='#B47CC7',linewidth='1.0', markersize=5,label=str(sta_names[int(day_data[7][0])])+day_data[7][0]) 
    plt.plot(X,day_data[8][2],color='#FBC15E',linewidth='1.0', markersize=5,label=str(sta_names[int(day_data[8][0])])+day_data[8][0]) 
    plt.plot(X,day_data[9][2],color='#e5ee38',linewidth='1.0', markersize=5,label=str(sta_names[int(day_data[9][0])])+day_data[9][0]) 
       
    plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
    plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
    
    
    
#     if var_type=="wind":
#         #plt.ylim([-5.0,70.0])
#         plt.ylabel('Avg. Wind Speed(mph)')
#         plt.title(mon+day +"Every 5min Avg. Wind")
#     else:
#         plt.ylim([-10.0,100.0])
#         plt.ylabel('Temperature(F)')
#         plt.title(mon+day +"Temperature")
    plt.ylim([-10.0,100.0])
    plt.ylabel('Temperature(F)')
    plt.title('201603001 ' +"Temperature")
    
    #plt.xticks(np.arange(min(X), max(X)+2, 12.0))
    plt.xticks(np.arange(min(X), max(X)+2, 12.0),label)
    #plt.yticks(np.arange(0, 100, 5.0),labelY)
    plt.tick_params(axis='both', which='major', labelsize=7)
    plt.xlabel('Time from 00:00 ~23:59,each 5min')

    
    
    #plt.xlim([0.2,0.0])
    plt.legend(loc='best',fontsize=8)
    
    
    plt.grid()
    
    #plt.tight_layout(pad=0.4, w_pad=0.5, h_pad=1.0)
    #plt.show()
    fig.savefig('F:/workspace/git/Graph-MP/data/mesonet_data/201603001_4.png', dpi=300)
    plt.close()
            
import time   
def loadTop(fileName):
    results=[]
    with open(fileName,"r") as rF:
        for i,line in enumerate(rF.readlines()):
            terms=line.strip().split(" ")
            results.append((int(terms[0]),map(int,terms[1].split(",")),terms[2],map(int,terms[3].split(","))))
            if i>19 :
                break
    return results        
                              
                        
def plotCaseDaysSingleStation():
    #dates=["20160301","20160302","20160308","20160309","20160312","20160313","20160324","20160325","20160328","20160405","20160412","20160419","20160421","20160514","20160529","20160621","20160628","20160813","20160911","20160922"]
    
    vars=['i0','i1','i2','i3','i4','i5','i6','i7','i8','i9']
    topResults=loadTop("F:/workspace/git/Graph-MP/outputs/mesonetPlots/multi_CaseStudy/CP/2/20multi_TopK_result-CP_baseMeanDiff_20_s_2_wMax_18_filter_TIncld_0.7_Top.txt")
    
    for result in topResults:
        dates=[]
        
        top=result[0]+1
        vals=result[1]
        dates.append(result[2])
        for i,var in enumerate(vars):
            if i in vals:
                exec "%s=%s"%(vars[i], 1)
            else:
                exec "%s=%s"%(vars[i], 0)
        print i0,i1,i2,i3,i4,i5,i6,i7,i8,i9
        
    #     i0=0
    #     i1=0
    #     i2=0
    #     i3=1
    #     i4=1
    #     i5=1
    #     i6=1
    #     i7=0
    #     i8=0
    #     i9=0
        
        mons=["201603","201604","201605","201606","201607","201608","201609"]
        days=['01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31']
        sta_names={0:"BATA",1:"SBRI",2:"WATE",3:"JORD",4:"CSQR",5:"WEST",6:"COLD",7:"SPRA",8:"COBL",9:"STEP"}
        var_type="wind"
        rootpath="F:/workspace/git/Graph-MP/data/mesonet_data/"+var_type+"/"
        rootpath2="F:/workspace/git/Graph-MP/data/mesonet_data/temp/"
        rootpath3="F:/workspace/git/Graph-MP/data/mesonet_data/precip/"
        #expRoot="F:/workspace/git/TranWeatherProject/data/mesonet_data/mesonetExpData/statExpData/"
        for mon in mons:
            for day in days:
                date=str(mon+day)
                
                if date not in dates:
                    #print "Not ",date
                    continue
                #expAvgs=expAvg(expRoot+mon+day+".txt")
                fileName=rootpath+mon+day+".txt"
                fileName2=rootpath2+mon+day+".txt"
                fileName3=rootpath3+mon+day+".txt"
                print fileName
                
                if not os.path.exists(fileName):
                    print "File Not Found",fileName
                    continue 
                if not os.path.exists(fileName2):
                    print "File Not Found",fileName2
                    continue 
                if not os.path.exists(fileName3):
                    print "File Not Found",fileName2
                    continue
                
                day_data=[]
                with open(fileName,"r") as df:
                    for line in df.readlines():
                        terms=line.strip().split()
                        sta_name=terms[0]
                        data=map(float,terms[1:])
                        day_data.append((sta_name,mon+day,data))
                
                day_data2=[]
                with open(fileName2,"r") as df2:
                    for line in df2.readlines():
                        terms=line.strip().split()
                        sta_name=terms[0]
                        data=map(float,terms[1:])
                        day_data2.append((sta_name,mon+day,data))
                
                day_data3=[]
                with open(fileName3,"r") as df3:
                    for line in df3.readlines():
                        terms=line.strip().split()
                        sta_name=terms[0]
                        data=map(float,terms[1:])
                        day_data3.append((sta_name,mon+day,data))
                        
                X=[i for i in range(0,len(day_data[0][2]))]  
                label=[(str(i)+"\n"+str(i*5/60)+"h") for i in range(0,len(day_data[0][2])+1,12)]
                labelY=[str(i) for i in range(0,100+1,5)]
                print sta_names[int(day_data[0][0])]
                print day_data[i3][2] 
                
                fig=plt.figure(1) 
                        
               
                if i0!=0:         
                    plt.plot(X,day_data[0][2],'b-',linewidth='0.5', markersize=5,label='Wind '+sta_names[int(day_data[0][0])]+day_data[0][0])               
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
                      
                plt.axvline(x=result[3][0], ymin=-1.0, ymax=50.0,color='k',linestyle='--')
                plt.axvline(x=result[3][1], ymin=-1.0, ymax=50.0,color='k',linestyle='--')
                plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
                plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
                plt.ylim([-1.0,50.0])
                plt.title("Top"+str(result[0]+1)+" "+mon+day +"Wind")
                #plt.xticks(np.arange(min(X), max(X)+2, 12.0))
                plt.xticks(np.arange(min(X), max(X)+2, 12.0),label)
                plt.yticks(np.arange(-1, 50, 5.0),labelY)
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
                fig.savefig('F:/workspace/git/Graph-MP/outputs/mesonetPlots/multi_CaseStudy/mvPlots/'+str(top)+'_wind_'+str(mon+day)+'.png', dpi=300)
                fig.clf()
                fig=plt.figure(2) 
                if i0!=0:         
                    plt.plot(X,day_data2[0][2],'b-',linewidth='0.5', markersize=5)               
                if i1!=0:         
                    plt.plot(X,day_data2[1][2],'r-',linewidth='0.5', markersize=5) 
                if i2!=0:         
                    plt.plot(X,day_data2[2][2],'k-',linewidth='0.5', markersize=5) 
                if i3!=0:         
                    plt.plot(X,day_data2[3][2],'g-',linewidth='0.5', markersize=5) 
                if i4!=0:         
                    plt.plot(X,day_data2[4][2],'y-',linewidth='0.5', markersize=5) 
                if i5!=0:         
                    plt.plot(X,day_data2[5][2],'c-',linewidth='0.5', markersize=5) 
                if i6!=0:         
                    plt.plot(X,day_data2[6][2],'m-',linewidth='0.5', markersize=5) 
                if i7!=0:         
                    plt.plot(X,day_data2[7][2],color ='#B47CC7',linewidth='0.5', markersize=5) 
                if i8!=0:         
                    plt.plot(X,day_data2[8][2],color='#FBC15E',linewidth='0.5', markersize=5) 
                if i9!=0:         
                    plt.plot(X,day_data2[9][2],color='#e5ee38',linewidth='0.5', markersize=5) 
             
    #                 if var_type=="wind":
    #                     plt.ylim([-1.0,50.0])
    #                     plt.ylabel('Avg. Wind Speed(mph)')
    #                     plt.title(mon+day +"Every 5min Avg. Wind")
    #                 else:
    #                     plt.ylim([-10.0,100.0])
    #                     plt.ylabel('Temperature(F)')
    #                     plt.title(mon+day +"Temperature")
                plt.axvline(x=result[3][0], ymin=-10.0, ymax=100.0,color='k',linestyle='--')
                plt.axvline(x=result[3][1], ymin=-10.0, ymax=100.0,color='k',linestyle='--')
                plt.ylim([-10.0,100.0])
                plt.title("Top"+str(result[0]+1)+" "+mon+day +"Temperature ")
                #plt.xticks(np.arange(min(X), max(X)+2, 12.0))
                plt.xticks(np.arange(min(X), max(X)+2, 12.0),label)
                plt.yticks(np.arange(0, 100, 5.0),labelY)
                plt.tick_params(axis='both', which='major', labelsize=7)
                plt.xlabel('Time from 00:00 ~23:59,each 5min')
                plt.grid()
                
                
                #plt.xlim([0.2,0.0])
                plt.legend(loc='best',fontsize=8)
                
                
               
#                 
#                 fig.subplots_adjust(bottom = 0)
#                 fig.subplots_adjust(top = 1)
#                 fig.subplots_adjust(right = 1)
#                 fig.subplots_adjust(left = 0)
                #plt.tight_layout(pad=0.4, w_pad=0.5, h_pad=1.0)
                #plt.show()
                fig.savefig('F:/workspace/git/Graph-MP/outputs/mesonetPlots/multi_CaseStudy/mvPlots/'+str(top)+'_temp_'+str(mon+day)+'.png', dpi=300)
                fig.clf()
                
                fig=plt.figure(3) 
                if i0!=0:         
                    plt.plot(X,day_data3[0][2],'b-',linewidth='0.5', markersize=5)               
                if i1!=0:         
                    plt.plot(X,day_data3[1][2],'r-',linewidth='0.5', markersize=5) 
                if i2!=0:         
                    plt.plot(X,day_data3[2][2],'k-',linewidth='0.5', markersize=5) 
                if i3!=0:         
                    plt.plot(X,day_data3[3][2],'g-',linewidth='0.5', markersize=5) 
                if i4!=0:         
                    plt.plot(X,day_data3[4][2],'y-',linewidth='0.5', markersize=5) 
                if i5!=0:         
                    plt.plot(X,day_data3[5][2],'c-',linewidth='0.5', markersize=5) 
                if i6!=0:         
                    plt.plot(X,day_data3[6][2],'m-',linewidth='0.5', markersize=5) 
                if i7!=0:         
                    plt.plot(X,day_data3[7][2],color ='#B47CC7',linewidth='0.5', markersize=5) 
                if i8!=0:         
                    plt.plot(X,day_data3[8][2],color='#FBC15E',linewidth='0.5', markersize=5) 
                if i9!=0:         
                    plt.plot(X,day_data3[9][2],color='#e5ee38',linewidth='0.5', markersize=5) 
             
    #                 if var_type=="wind":
    #                     plt.ylim([-1.0,50.0])
    #                     plt.ylabel('Avg. Wind Speed(mph)')
    #                     plt.title(mon+day +"Every 5min Avg. Wind")
    #                 else:
    #                     plt.ylim([-10.0,100.0])
    #                     plt.ylabel('Temperature(F)')
    #                     plt.title(mon+day +"Temperature")
                plt.axvline(x=result[3][0], ymin=-0.2, ymax=2.0,color='k',linestyle='--')
                plt.axvline(x=result[3][1], ymin=-0.2, ymax=2.0,color='k',linestyle='--')
                plt.ylim([-0.2,2.0])
                plt.title("Top"+str(result[0]+1)+" "+mon+day +"Precipitation ")
                #plt.xticks(np.arange(min(X), max(X)+2, 12.0))
                plt.xticks(np.arange(min(X), max(X)+2, 12.0),label)
                #plt.yticks(np.arange(-0.2, 2.0, 0.5),labelY)
                plt.tick_params(axis='both', which='major', labelsize=7)
                plt.xlabel('Time from 00:00 ~23:59,each 5min')
                plt.grid()
                
                
                #plt.xlim([0.2,0.0])
                plt.legend(loc='best',fontsize=8)
                
                
                
                
#                 fig.subplots_adjust(bottom = 0)
#                 fig.subplots_adjust(top = 1)
#                 fig.subplots_adjust(right = 1)
#                 fig.subplots_adjust(left = 0)
                #plt.tight_layout(pad=0.4, w_pad=0.5, h_pad=1.0)
                #plt.show()
                fig.savefig('F:/workspace/git/Graph-MP/outputs/mesonetPlots/multi_CaseStudy/mvPlots/'+str(top)+'_precip_'+str(mon+day)+'.png', dpi=300)
                fig.clf()
                plt.close()
                
                             
 

def plotAllDays():
    root="F:/workspace/git/WeatherTransportationProject/"
    #dates=["20160301","20160302","20160308","20160309","20160312","20160313","20160324","20160325","20160328","20160405","20160412","20160419","20160421","20160514","20160529","20160621","20160628","20160813","20160911","20160922"]
    dates=[]
    #"201603","201604","201605","201606","201607","201608"
    mons=["201609","201610","201611","201612","201701","201702","201703","201704","201705","201706"]
    days=['01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31']
    sta_names={0:"BATA",1:"SBRI",2:"WATE",3:"JORD",4:"CSQR",5:"WEST",6:"COLD",7:"SPRA",8:"COBL",9:"STEP"}
    var_types=["temp","temp9","press","wind","windDir","windMax","rh","rad"]
    #var_types=["wind"]
    for var_type in var_types:
        rootpath=root+"data/mesonet_data/"+var_type+"/"
        #expRoot="F:/workspace/git/Graph-MP/data/mesonet_data/mesonetExpData/statExpData/"
        for mon in mons:
            for day in days:
                date=str(mon+day)
                
#                 if date in dates:
#                     print "Not ",date
#                     continue
                
                fileName=rootpath+mon+day+".txt"
                print fileName
                day_data=[]
                if not os.path.exists(fileName):
                    print "File Not Found",fileName
                    continue 
                with open(fileName,"r") as df:
                    for line in df.readlines():
                        terms=line.strip().split()
                        sta_name=terms[0]
                        data=map(float,terms[1:])
                        day_data.append((sta_name,mon+day,data))
                X=[i for i in range(0,len(day_data[0][2]))]  
                label=[(str(i)+"\n"+str(i*5/60)+"h") for i in range(0,len(day_data[0][2])+1,12)]
                labelY=[str(i) for i in range(0,100+1,5)]
                print sta_names[int(day_data[0][0])]          
                fig=plt.figure(1)            
                plt.plot(X,day_data[0][2],'b-',linewidth='1.5', markersize=5,label=sta_names[int(day_data[0][0])]+day_data[0][0]) 
                plt.plot(X,day_data[1][2],'r-',linewidth='1.5', markersize=5,label=str(sta_names[int(day_data[1][0])])+day_data[1][0]) 
                plt.plot(X,day_data[2][2],'k-',linewidth='1.5', markersize=5,label=str(sta_names[int(day_data[2][0])])+day_data[2][0]) 
                plt.plot(X,day_data[3][2],'g-',linewidth='1.5', markersize=5,label=str(sta_names[int(day_data[3][0])])+day_data[3][0]) 
                plt.plot(X,day_data[4][2],'y-',linewidth='1.5', markersize=5,label=str(sta_names[int(day_data[4][0])])+day_data[4][0]) 
                plt.plot(X,day_data[5][2],'c-',linewidth='1.5', markersize=5,label=str(sta_names[int(day_data[5][0])])+day_data[5][0]) 
                plt.plot(X,day_data[6][2],'m-',linewidth='1.5', markersize=5,label=str(sta_names[int(day_data[6][0])])+day_data[6][0]) 
                plt.plot(X,day_data[7][2],color ='#B47CC7',linewidth='1.5', markersize=5,label=str(sta_names[int(day_data[7][0])])+day_data[7][0]) 
                plt.plot(X,day_data[8][2],color='#FBC15E',linewidth='1.5', markersize=5,label=str(sta_names[int(day_data[8][0])])+day_data[8][0]) 
                plt.plot(X,day_data[9][2],color='#e5ee38',linewidth='1.5', markersize=5,label=str(sta_names[int(day_data[9][0])])+day_data[9][0]) 
                    
                plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
                plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
                
                
                
                if var_type=="wind":
                    plt.ylim([-5.0,70.0])
                    plt.ylabel('Average Wind Speed(mph)')
                    plt.title(mon+day +" Every 5min Average Wind Speed")
                elif var_type=="windMax":
                    plt.ylim([-5.0,70.0])
                    plt.ylabel('Max Wind Speed(mph)')
                    plt.title(mon+day +"Every 5min Max Wind")
                elif var_type=="windDir":
                    #plt.ylim([-5.0,70.0])
                    plt.ylabel('Max Wind Speed(mph)')
                    plt.title(mon+day +" Wind Direction Degree")
                elif var_type=="temp":
                    plt.ylim([-10.0,100.0])
                    plt.ylabel('Temperature(F)')
                    plt.title(mon+day +" 2m Temperature")
                elif var_type=="temp9":
                    plt.ylim([-10.0,100.0])
                    plt.ylabel('Temperature(F)')
                    plt.title(mon+day +" 9m Temperature")
                elif var_type=="press":
                    #plt.ylim([-10.0,100.0])
                    plt.ylabel('Pressure(mbar)')
                    plt.title(mon+day +" Pressure")
                elif var_type=="rad":
                    #plt.ylim([-10.0,100.0])
                    plt.ylabel('Solar Radiation(W/m^2)')
                    plt.title(mon+day +" Solar Radiation") 
                elif var_type=="rh":
                    plt.ylim([0.0,100.0])
                    plt.ylabel('Relative Humidity %')
                    plt.title(mon+day +" rh")
                           
                
                
                #plt.xticks(np.arange(min(X), max(X)+2, 12.0))
                plt.xticks(np.arange(min(X), max(X)+2, 12.0),label)
                #plt.yticks(np.arange(0, 100, 5.0),labelY)
                plt.tick_params(axis='both', which='major', labelsize=7)
                plt.xlabel('Time from 00:00 ~23:59,every 5min')
    
                
                
                #plt.xlim([0.2,0.0])
                plt.legend(loc='best',fontsize=10)
                
                
                plt.grid()
                
                #plt.tight_layout(pad=0.4, w_pad=0.5, h_pad=1.0)
                #plt.show()
                fig.savefig(root+'/outputs/mesonetPlots/'+var_type+'_plots/'+str(mon+day)+'.png')
                plt.close()
def plotTravTimeAllDays():
    import matplotlib
    #dates=["20160301","20160302","20160308","20160309","20160312","20160313","20160324","20160325","20160328","20160405","20160412","20160419","20160421","20160514","20160529","20160621","20160628","20160813","20160911","20160922"]
    dates=[]
    mons=["201603","201604","201605","201606","201607","201608","201609"]
    days=['01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31']
   
    var_types=["TravelTimeToWest","TravelTimeToWest"]
    #var_types=["wind"]
    
    colors=[]
    for name, hex in matplotlib.colors.cnames.iteritems():
        if name not in Light_cnames.keys():
            colors.append(hex)
    
      
    
    for var_type in var_types:
        rootpath="F:/workspace/git/Graph-MP/data/trafficData/I90_TravelTime/"+var_type+"/"
        #expRoot="F:/workspace/git/Graph-MP/data/mesonet_data/mesonetExpData/statExpData/"
        for mon in mons:
            for day in days:
                date=str(mon+day)
                
#                 if date in dates:
#                     print "Not ",date
#                     continue
                
                fileName=rootpath+mon+day+".txt"
                print fileName
                day_data=[]
                if not os.path.exists(fileName):
                    print "File Not Found",fileName
                    continue 
                with open(fileName,"r") as df:
                    for idx,line in enumerate(df.readlines()):
                        terms=line.strip().split()
                        sta_name="TMC "+str(idx)
                        data=map(float,terms)
                        day_data.append((sta_name,mon+day,data))
                X=[i for i in range(0,len(day_data[0][2]))]  
                label=[(str(i)+"\n"+str(i*5/60)+"h") for i in range(0,len(day_data[0][2])+1,12)]
                labelY=[str(i) for i in range(0,100+1,5)]
                print len(day_data)        
                fig=plt.figure(1) 
                for i in range(len(day_data)):
                    plt.plot(X,day_data[i][2],colors[i],linewidth='0.5', markersize=5,label=day_data[i][0]) 
                  
#                 art = []
#                 lgd = plt.legend(loc=3, bbox_to_anchor=(0, -0.5), ncol=5)
#                 art.append(lgd)
                #plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
                plt.ylabel('Traveling Time (sec)')
                
                
                if var_type=="TravelTimeToWest":                 
                    plt.title(mon+day +" Travel Time I90 East To West")
                else:
                    plt.title(mon+day +" Travel Time I90 West To East")
               
                           
                
                
                #plt.xticks(np.arange(min(X), max(X)+2, 12.0))
                plt.xticks(np.arange(min(X), max(X)+2, 12.0),label)
                #plt.yticks(np.arange(0, 100, 5.0),labelY)
                plt.tick_params(axis='both', which='major', labelsize=7)
                plt.xlabel('Time: 00:00 ~ 23:59,every 5min')
    
                
                
                #plt.xlim([0.2,0.0])
                plt.ylim([0.0,3600.0])
#                 plt.legend(loc='best',fontsize=10)
                
                
                plt.grid()
                
                #plt.tight_layout(pad=0.4, w_pad=0.5, h_pad=1.0)
                #plt.show()
                fig.savefig('F:/workspace/git/Graph-MP/outputs/trafficData/'+var_type+'_plots/'+str(mon+day)+'.png')
                plt.close()            
plotAllDays()                    
        

