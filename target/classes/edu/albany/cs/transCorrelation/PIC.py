import numpy as np
from math import *
import matplotlib.pyplot as plt
import numpy as np
import random,time


def calcDistance(Lat_A, Lng_A, Lat_B, Lng_B):
    ra = 6378.140  
    rb = 6356.755  
    flatten = (ra - rb) / ra  #
    rad_lat_A = radians(Lat_A)
    rad_lng_A = radians(Lng_A)
    rad_lat_B = radians(Lat_B)
    rad_lng_B = radians(Lng_B)
    pA = atan(rb / ra * tan(rad_lat_A))
    pB = atan(rb / ra * tan(rad_lat_B))
    xx = acos(sin(pA) * sin(pB) + cos(pA) * cos(pB) * cos(rad_lng_A - rad_lng_B))
    c1 = (sin(xx) - xx) * (sin(pA) + sin(pB)) ** 2 / cos(xx / 2) ** 2
    c2 = (sin(xx) + xx) * (sin(pA) - sin(pB)) ** 2 / sin(xx / 2) ** 2
    dr = flatten / 8 * (c1 - c2)
    distance = ra * (xx + dr)
    distance =distance/ 1.609344 
    return distance


def getGeoInfo(wev,tev,station,tmcs):
           
    
    
    if wev[0]==1:
        temp=tev
        tev=wev
        wev=temp
        
    sta_id=wev[1]   
    #print wev,tev
    Lat_A=float(station[sta_id].split()[0])
    Lng_A=float(station[sta_id].split()[1])        
#     else:
#         sta_id=wev[1]
#         Lat_A=float(tmcs[sta_id].split()[0])
#         Lng_A=float(tmcs[sta_id].split()[1])
    
            
    tmc_id=tev[1]        
    Lat_B=float(tmcs[tmc_id].split()[0])
    Lng_B=float(tmcs[tmc_id].split()[1])        
#     else:
#         tmc_id=tev[1]
#         Lat_B=float(station[tmc_id].split()[0])
#         Lng_B=float(station[tmc_id].split()[1])     
    
    return Lat_A, Lng_A, Lat_B, Lng_B

    
def PIC(weatherEvent,trafficEvent,r,timeThreshold):
    pic=0.0
    for wev in weatherEvent:
        for tev in trafficEvent:
            
            if np.abs(tev[2]-wev[2])>timeThreshold:
                                
                continue
            
            try:
                pair_distance=calcDistance(wev[1][0],wev[1][1],tev[1][0],tev[1][1])
            except:
                
                if wev[2]==tev[2]:
                    continue
                pair_distance=0.0
                              
            if pair_distance<=r:
                print r,pair_distance
                pic+=1.0            
    return pic



def main():
    ite=1
    output=open("PICResult.txt","a+")
    
   
    
    radius=[5,10,15,20,25]  #5,9,13,17,21,25
           
    
    evetnFileName="F:/workspace/git/Graph-MP/data/events/WholeYearWTvents.txt"
    weatherEvent0=[]
    trafficEvent0=[]
    with open(evetnFileName,"r") as eF:
        for line in eF.readlines():
            terms=line.strip().split()
            terms=map(int,terms) 
            if terms[0]==0:
                weatherEvent0.append((terms[0],station[int(terms[1])],int(terms[2])))
            else:
                trafficEvent0.append((terms[0],tmcs[int(terms[1])],int(terms[2])))
           
    AllEvent=weatherEvent0+trafficEvent0
    weatherEventNum=len(weatherEvent0)
    trafficEventNum=len(trafficEvent0)
    print "Weather Event:",len(weatherEvent0)
    print "Traffic Event:",len(trafficEvent0)
    
    timeThresholds=[10,2,3,4,5]    
    
    for timeThreshold in timeThresholds:        
        for r in radius:
            t0=time.time()
            testStatisticsScore=PIC(weatherEvent0,trafficEvent0,r,timeThreshold)            
            print "r=%d TestScore=%f timeRadius=%d"%(r,testStatisticsScore,timeThreshold)    
               
            above=0.0
            for i in range(ite):
                tempAll=AllEvent
                
                random.shuffle(tempAll)
                weatherEvent=tempAll[:weatherEventNum]
                trafficEvent=tempAll[weatherEventNum:]
                 
                               
                #score=PIC(weatherEvent,trafficEvent,r,timeThreshold)
                score=1.0
                if testStatisticsScore<=score:
                    above+=1.0
                if i%100==0:
                    print 'i=',i
            print "%d %d %f %f "%(timeThreshold,r,1.0*above/ite,above)
            output.write(str(timeThreshold)+" "+str(r)+" "+ str(1.0*above/ite)+" "+str(above))
            output.flush()
            print time.time()-t0,'sec ....'

    output.close()
if __name__ =='__main__':
    main()