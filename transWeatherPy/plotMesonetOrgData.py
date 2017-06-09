import matplotlib.pyplot as plt
import numpy as np
def plot10seperate():
    mons=["201603","201604","201605","201606","201607","201608"]
    days=['01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31']
    rootpath="F:/workspace/git/TranWeatherProject/data/mesonet_data/"
    for mon in ["201604"]:
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
    #mons=["201609"]
    days=['01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31']
    sta_names={0:"BATA",1:"SBRI",2:"WATE",3:"JORD",4:"CSQR",5:"WEST",6:"COLD",7:"SPRA",8:"COBL",9:"STEP"}
    var_type="wind"
    rootpath="F:/workspace/git/TranWeatherProject/data/mesonet_data/"+var_type+"/"
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
            else:
                plt.ylim([-10.0,100.0])
                plt.ylabel('Temperature(F)')
                plt.title(mon+day +"Temperature")
            
            #plt.xticks(np.arange(min(X), max(X)+2, 12.0))
            plt.xticks(np.arange(min(X), max(X)+2, 12.0),label)
            plt.tick_params(axis='both', which='major', labelsize=7)
            plt.xlabel('Time from 00:00 ~23:59,each 5min')

            
            
            #plt.xlim([0.2,0.0])
            plt.legend(loc='best',fontsize=8)
            
            
            plt.grid()
            
            #plt.tight_layout(pad=0.4, w_pad=0.5, h_pad=1.0)
            #plt.show()
            fig.savefig('F:/workspace/git/TranWeatherProject/outputs/mesonetPlots/'+var_type+'_plots/'+str(mon+day)+'.png')
            plt.close()
          
plotSignle()           
    
