import numpy as np
from math import *
import numpy as np
import random,time,math
import collections
import SimuData

    
def RIPLEY(weatherEvent,trafficEvent,r,A):
    
    Kr=0.0
    k=len(weatherEvent)
    l=len(trafficEvent)
    for wev in weatherEvent:
        for tev in trafficEvent:                           
            if SimuData.dist(np.array(wev[1]),np.array(tev[1]))<=r:
                Kr+=1.0            
    Kr=(A/(k*l))*Kr
    return Kr


def main():
    ite=1
    output=open("SimuRIPLYKresult.txt","a+") 
    
    radiusData=[5,10,15,20,25,30,35,40,45]
    radius=[5,10,15,20,25,30,35,40,45]
    PI=math.pi
    V=0.75*(PI*100**3)
    for rData in radiusData:
        weatherEvent0,trafficEvent0=SimuData.getEvents(rData,500)
        weatherEventNum=len(weatherEvent0)
        trafficEventNum=len(trafficEvent0)
#         print "Weather Event:",len(weatherEvent0)
#         print "Traffic Event:",len(trafficEvent0)
        AllEvent=weatherEvent0+trafficEvent0   
                
        for r in radius:
            testStatisticsScore=RIPLEY(weatherEvent0,trafficEvent0,r,V)            
            print rData,r,testStatisticsScore        
            above=0.0
            for i in range(ite):
                tempAll=AllEvent
                
                random.shuffle(tempAll)
                weatherEvent=tempAll[:weatherEventNum]
                trafficEvent=tempAll[weatherEventNum:]
                 
                               
                score=RIPLEY(weatherEvent,trafficEvent,r,V)
                
                if testStatisticsScore<=score:
                    above+=1.0
#                 if i%100==0:
#                     print 'i=',i
#             print "%d %f %f "%(r,1.0*above/ite,above)
            output.write(str(rData)+" "+str(r)+" "+ str(1.0*above/ite)+" "+str(above)+"\n")
    
    output.close()
if __name__ =='__main__':
    main()
    
