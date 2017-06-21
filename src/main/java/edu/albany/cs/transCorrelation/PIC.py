import numpy as np
from math import *
import matplotlib.pyplot as plt
import numpy as np
import random,time
from sets import Set
import itertools,sys
from tqdm import *

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
    try:
        c2 = (sin(xx) + xx) * (sin(pA) - sin(pB)) ** 2 / sin(xx / 2) ** 2
    except:
        print Lat_A, Lng_A, Lat_B, Lng_B
    
    dr = flatten / 8 * (c1 - c2)
    distance = ra * (xx + dr)
    distance =distance/ 1.609344 
    return distance


def PIC(weatherEvent,trafficEvent,r,timeThreshold,pair_dist):
    pic=0.0
    for wev in tqdm(weatherEvent,):
        for tev in trafficEvent:
            pairs=str(min(tev[4],wev[4]))+"_"+str(min(tev[4],wev[4]))           
            if np.abs(tev[3]-wev[3])>timeThreshold:                                
                continue
            if not pair_dist.has_key(pairs):
                continue
                      
            if pair_dist[pairs]<=r:
                #print r,pair_distance
                pic+=1.0            
    return pic



def main():
    ite=1
    output=open("PICResult.txt","a+")   
    
            
    rel_max_dist=20
    
    evetnFileName="F:/workspace/git/WeatherTransportationProject/data/events/WholeYearWETevents_New.txt"
    weatherEvent0=[]
    trafficEvent0=[]
    sta_loc=Set()
    tmc_loc=Set()
    with open(evetnFileName,"r") as eF:
        for line in eF.readlines():
            terms=line.strip().split()
            #terms=map(int,terms) 
            if int(terms[0])==0:                
                weatherEvent0.append((int(terms[0]),float(terms[1]),float(terms[2]),int(terms[3]),int(terms[4])))
                sta_loc.add((int(terms[4]),float(terms[1]),float(terms[2])))
            else:
                trafficEvent0.append((int(terms[0]),float(terms[1]),float(terms[2]),int(terms[3]),int(terms[4])))
                tmc_loc.add((int(terms[4]),float(terms[1]),float(terms[2])))
        
     
    AllEvent=weatherEvent0+trafficEvent0
    weatherEventNum=len(weatherEvent0)
    trafficEventNum=len(trafficEvent0)
    print "Weather Event:",len(weatherEvent0)
    print "Traffic Event:",len(trafficEvent0)
    
    pair_dist={}
    for (a,b) in itertools.combinations(sta_loc, 2):
        if a[1]==b[1] and a[2]==b[2]:
            dist=0
        else:
            dist=calcDistance(a[1], a[2],b[1],b[2])
        if dist>rel_max_dist:
            continue               
        pair_dist[str(min(a,b))+"_"+str(max(a,b))]=dist
        
    for (a,b) in itertools.combinations(tmc_loc, 2):
        if a[1]==b[1] and a[2]==b[2]:
            dist=0
        else:
            dist=calcDistance(a[1], a[2],b[1],b[2])
        if dist>rel_max_dist:
            continue              
        pair_dist[str(min(a,b))+"_"+str(max(a,b))]=dist 
        
    for (a,b) in list(itertools.product( sta_loc,tmc_loc)):
        if a[1]==b[1] and a[2]==b[2]:
            dist=0
        else:
            dist=calcDistance(a[1], a[2],b[1],b[2])
        if dist>rel_max_dist:
            continue              
        pair_dist[str(min(a,b))+"_"+str(max(a,b))]=dist 
    print "All-pairs",len(pair_dist)   
    timeThresholds=[2,3,4,5]  
    radius=[5,10,15,20]  #5,9,13,17,21,25   
    
    for timeThreshold in timeThresholds:        
        for r in radius:
            t0=time.time()
            sys.stdout.write("r=%d timeRadius=%d "%(r,timeThreshold))
            testStatisticsScore=PIC(weatherEvent0,trafficEvent0,r,timeThreshold,pair_dist)            
            sys.stdout.write("TestScore=%f "%(testStatisticsScore))    
               
#             above=0.0
#             for i in range(ite):
#                 tempAll=AllEvent
#                 
#                 random.shuffle(tempAll)
#                 weatherEvent=tempAll[:weatherEventNum]
#                 trafficEvent=tempAll[weatherEventNum:]           
#                                
#                 #score=PIC(weatherEvent,trafficEvent,r,timeThreshold)
#                 score=1.0
#                 if testStatisticsScore<=score:
#                     above+=1.0
#                 if i%100==0:
#                     print 'i=',i
#             print "%d %d %f %f "%(timeThreshold,r,1.0*above/ite,above)
#             output.write(str(timeThreshold)+" "+str(r)+" "+ str(1.0*above/ite)+" "+str(above))
#             output.flush()
            sys.stdout.write(" ",time.time()-t0,'sec ....',"\n")

    output.close()
if __name__ =='__main__':
    main()