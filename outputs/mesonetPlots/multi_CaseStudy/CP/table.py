import datetime
import numpy as np
import matplotlib.pyplot as plt
var_type={0:"temp",1:"temp9",2:"press",3:"wind",4:"windDir",5:"windMax",6:"rh",7:"rad"}
days=["Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"]
sta_names={0:"BATA",1:"SBRI",2:"WATE",3:"JORD",4:"CSQR",5:"WEST",6:"COLD",7:"SPRA",8:"COBL",9:"STEP"}
        
time_region=[]
top=1
with open("table.txt","r") as f:
    for i,line in enumerate(f.readlines()):
        terms=line.strip().split()        
        vars=[var_type[i] for i in map(int,terms[0].split(","))]
        stations=[i for i in map(int,terms[1].split(","))]
        start= 5*map(int,terms[2].split(","))[0]
        end=5*map(int,terms[2].split(","))[-1]        
        time=["%d:%02d" % (start / 60, start % 60),"%d:%02d" % (end / 60, end % 60)]
        date=terms[3][:4]+"-"+terms[3][4:6]+"-"+terms[3][6:]
        dt=datetime.datetime.strptime(terms[3][:4]+"-"+terms[3][4:6]+"-"+terms[3][6:], "%Y-%m-%d")
        print top,time[0]+"~"+time[1],date,dt.strftime("%A"),terms[1]
        time_region.append((start,end,stations))
        top+=1

fig=plt.figure(1)

for sta in range(10):
    fig=plt.figure(1)
    print sta,"-----------------------------"
    for i,(start,end,stations) in enumerate(time_region):
        #print stations,sta
        if sta in stations:    
            start=start/5
            end=end/5
        else:
            start=0
            end=0
        
            
        
        #plt.axvline(x=i, ymin=start/5, ymax=end/5,color='r') 
        plt.plot([i, i], [start, end], 'b-', lw=2)
            
    #plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
    #plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
    X=[i for i in range(0,288)]
    label=[str(i*5/60)+":00" for i in range(0,288+1,12)]
                    
    plt.ylim([0,288])
    plt.yticks(np.arange(min(X), max(X)+2, 12.0),label,fontsize=10)
    plt.ylabel('Time(Hour)')
    plt.xlabel('Ranking')
    plt.xlim([0,250])
    #plt.legend(loc='best',fontsize=14)
    plt.title('The '+sta_names[sta] +' station top K changing pattern time distribution plot',fontsize=12)
    #plt.tight_layout(pad=0.4, w_pad=0.5, h_pad=1.0)
    #plt.show()
    fig.savefig('./'+sta_names[sta]+'_timeRegion_plot.png',dpi=200)
    plt.close()         
        
