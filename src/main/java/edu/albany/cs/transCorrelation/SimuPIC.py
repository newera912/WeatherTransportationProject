import numpy as np
from math import *

import numpy as np
import random,time

import SimuData



    
def PIC(weatherEvent,trafficEvent,r):
    pic=0.0
    dist=[]
    for wev in weatherEvent:
        for tev in trafficEvent:
            tDist=SimuData.dist(np.array(wev[1]),np.array(tev[1]))
            dist.append(tDist)
            if tDist<=r:
                pic+=1.0            
    return pic,dist



def main():
    ite=1
    fileName="SimuPICresultPairDist3.txt"
    print fileName
    output=open("SimuPICresultdebug3.txt","a+") 
    distOut=open(fileName,"a+") 
    radiusData=[5,10,15,20,25,30,35,40,45,50,60,70,80,90,100]#,6,8,12,16]
    radius=[5,10,15,20,25,30,35,40,45,50,60,70,80,90,100]
    for rData in radiusData:
        weatherEvent0,trafficEvent0,dist=SimuData.getEvents(rData,500)
        weatherEventNum=len(weatherEvent0)
        trafficEventNum=len(trafficEvent0)
        
#         print "Weather Event:",len(weatherEvent0)
#         print "Traffic Event:",len(trafficEvent0)
        AllEvent=weatherEvent0+trafficEvent0   
                
        for r in radius:
            testStatisticsScore,dist=PIC(weatherEvent0,trafficEvent0,r) 
            distOut.write(str(rData)+" "+" ".join(map(str,dist))+"\n")
            distOut.flush()           
            print rData,r,testStatisticsScore        
#             above=0.0
#             for i in range(ite):
#                 tempAll=AllEvent
#                 
#                 random.shuffle(tempAll)
#                 weatherEvent=tempAll[:weatherEventNum]
#                 trafficEvent=tempAll[weatherEventNum:]
#                  
#                                
#                 score=PIC(weatherEvent,trafficEvent,r)
#                 #print score
#                 if testStatisticsScore<=score:
#                     above+=1.0
# #                 if i%100==0:
# #                     print 'i=',i
#             #print "%d %f %f "%(r,1.0*above/ite,above)
#             output.write(str(rData)+" "+str(r)+" "+ str(1.0*above/ite)+" "+str(above)+"\n")
    
    output.close()
    time.sleep(1000)
if __name__ =='__main__':
    main()