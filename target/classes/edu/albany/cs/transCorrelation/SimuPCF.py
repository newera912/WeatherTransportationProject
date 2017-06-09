import numpy as np
from math import *
import numpy as np
import random,time,math
import collections
import SimuData,time

    
def PCF(weatherEvent,trafficEvent,h,A):
    PI=math.pi
    Gr=0.0
    k=len(weatherEvent)
    l=len(trafficEvent)
    for wev in weatherEvent:
        for tev in trafficEvent:
            d=SimuData.dist(np.array(wev[1]),np.array(tev[1]))
            #print d,SimuData.dist(np.array(wev[1]),np.array(tev[1])),r 
            if np.abs(d)<=h:
                #print "--------------"
                Gr+=0.75*h*(1-(d*d)/(h*h)) 
    #print Gr,A,(A*A/(2.0*PI*r*k*l)),(A*A/(2.0*PI*r*k*l))         
    Gr=(A*A/(2.0*PI*h*k*l))*Gr
    return Gr

def main():
    ite=500
    
    output=open("SimuPCFresult4.txt","a+") 
    
    radiusData=[5,10,15,20,25,30,35,40,45]
    radius=[5,10,15,20,25,30,35,40,45]
    #h=hh
    PI=math.pi
    V=0.75*(PI*100**3)
    #print V,PI
    for rData in radiusData:
        weatherEvent0,trafficEvent0=SimuData.getEvents(rData,500)
        weatherEventNum=len(weatherEvent0)
        trafficEventNum=len(trafficEvent0)
#         print "Weather Event:",len(weatherEvent0)
#         print "Traffic Event:",len(trafficEvent0)
        AllEvent=weatherEvent0+trafficEvent0   
                
        for r in radius:
            testStatisticsScore=PCF(weatherEvent0,trafficEvent0,r,V)            
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
#                 score=PCF(weatherEvent,trafficEvent,r,V,h)
#                 print score
#                 if testStatisticsScore<=score:
#                     above+=1.0
# #                 if i%100==0:
# #                     print 'i=',i
# #             print "%d %f %f "%(r,1.0*above/ite,above)
#             output.write(str(rData)+" "+str(r)+" "+ str(1.0*above/ite)+" "+str(above)+"\n")
#             output.flush()
    output.close()
    time.sleep(1000)
if __name__ =='__main__':
    main()


