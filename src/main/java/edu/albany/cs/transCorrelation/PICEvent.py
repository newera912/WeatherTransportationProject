import numpy as np
from math import *
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
    for wev in weatherEvent:
        for tev in trafficEvent:
            tempPIC=0.0
            pairs=str(min(tev[5],wev[5]))+"_"+str(max(tev[5],wev[5]))
            if not pair_dist.has_key(pairs):
                continue 
            if pair_dist[pairs]>r:
                continue
            overlap=getOverlap([wev[3],wev[4]], [tev[3],tev[4]])
            if overlap>0:
                tempPIC=1.0
            elif wev[4]<tev[3] and wev[4]+timeThreshold>=tev[3]:
                tempPIC=1.0
            elif tev[4]<wev[3] and tev[4]+timeThreshold>=wev[4]:
                tempPIC=1.0
#             if tempPIC>0.0:
#                 #print "Dist<",round(pair_dist[pairs]),"Station-ID:",wev[5]%100,wev[3]/1000,wev[3]%1000,"~",wev[4]%1000,"| TMC-ID:",tev[5]%100,tev[3]/1000,tev[3]%1000,"~",tev[4]%1000
#                 print round(pair_dist[pairs]),wev[5]%100,wev[3]/1000,wev[3]%1000,"~",wev[4]%1000,tev[5]%100,tev[3]/1000,tev[3]%1000,"~",tev[4]%1000
#                 
            pic+=tempPIC
                
            
                
                      
    return pic

def getOverlap(a, b):
    return max(0, min(a[1], b[1]) - max(a[0], b[0]))

def round(x):    
    return np.round(100.0*x)/100.0  

def main():
    ite=1000
    output=open("PICResultBlock.txt","a+")      
            
    rel_max_dist=20
    
    evetnFileName="WholeYearWETevents_Blocks100.txt"
    weatherEvent0=[]
    trafficEvent0=[]
    sta_loc=Set()
    tmc_loc=Set()
    with open(evetnFileName,"r") as eF:
        for line in eF.readlines():
            terms=line.strip().split()
            #terms=map(int,terms) 
            if int(terms[0])==0:                
                weatherEvent0.append((int(terms[0]),float(terms[1]),float(terms[2]),int(terms[3]),int(terms[4]),int(terms[5])))
                sta_loc.add((int(terms[5]),float(terms[1]),float(terms[2])))
            else:
                trafficEvent0.append((int(terms[0]),float(terms[1]),float(terms[2]),int(terms[3]),int(terms[4]),int(terms[5])))
                tmc_loc.add((int(terms[5]),float(terms[1]),float(terms[2])))      
  
    
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
        pair_dist[str(min(a[0],b[0]))+"_"+str(max(a[0],b[0]))]=dist
        
    for (a,b) in itertools.combinations(tmc_loc, 2):
        if a[1]==b[1] and a[2]==b[2]:
            dist=0
        else:
            dist=calcDistance(a[1], a[2],b[1],b[2])
        if dist>rel_max_dist:
            continue              
        pair_dist[str(min(a[0],b[0]))+"_"+str(max(a[0],b[0]))]=dist
        
    for (a,b) in list(itertools.product( sta_loc,tmc_loc)):
        if a[1]==b[1] and a[2]==b[2]:
            dist=0
        else:
            dist=calcDistance(a[1], a[2],b[1],b[2])
        if dist>rel_max_dist:
            continue              
        pair_dist[str(min(a[0],b[0]))+"_"+str(max(a[0],b[0]))]=dist
    print "All-pairs",len(pair_dist)   
    timeThresholds=[2,3,4,5]  
    radius=[5,10,15,20]  #5,9,13,17,21,25   
    
    for timeThreshold in timeThresholds:        
        for r in radius:
            t0=time.time()
            print("r=%d timeRadius=%d "%(r,timeThreshold))
            testStatisticsScore=PIC(weatherEvent0,trafficEvent0,r,timeThreshold,pair_dist)            
                
               
            above=0.0
            for i in tqdm(range(ite)):
                tempAll=AllEvent
                  
                random.shuffle(tempAll)
                weatherEvent=tempAll[:weatherEventNum]
                trafficEvent=tempAll[weatherEventNum:]           
                                 
                score=PIC(weatherEvent,trafficEvent,r,timeThreshold,pair_dist)
                #score=1.0
                if testStatisticsScore<=score:
                    above+=1.0
#                 if i%100==0:
#                     sys.stdout.write('i='+str(i)+" ")
            sys.stdout.write("\n%d %f %f \n"%(testStatisticsScore,above,1.0*above/ite))
            output.write(str(timeThreshold)+" "+str(r)+" "+str(above)+" "+ str(1.0*above/ite)+"\n")
            output.flush()
             
    
    output.close()
if __name__ =='__main__':
    main()