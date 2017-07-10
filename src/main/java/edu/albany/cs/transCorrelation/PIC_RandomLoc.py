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
    pairType=np.zeros(4)
    for wev in weatherEvent:
        for tev in trafficEvent:
            
#             if wev[0]==tev[0]:
#                 continue
            pairs=str(min(tev[4],wev[4]))+"_"+str(max(tev[4],wev[4]))       
            if tev[3]-wev[3]>timeThreshold or tev[3]-wev[3]<0:                                                              
                continue
            if not pair_dist.has_key(pairs):
                continue
                      
            if pair_dist[pairs]<=r:
                if tev[0]!=wev[0] and wev[0]==0:
                    pairType[0]+=1.0
                elif tev[0]!=wev[0] and wev[0]==1:
                    pairType[1]+=1.0
                elif tev[0]==wev[0] and wev[0]==1:
                    pairType[2]+=1.0
                elif tev[0]==wev[0] and wev[0]==0:
                    pairType[3]+=1.0
                    
                pic+=1.0  
                      
    return pic,pairType

def round(x,a):    
    return np.round(10.0**a*x)/10.0**a    

def main():
    ite=10
    
    timeThresholds=[3] 
    output=open("PICResult100_RandomNetLocdebug"+str(timeThresholds[0])+".txt","a+") 
    radius=[25]  #5,9,13,17,21,25  5,10,15,20,25,30,35,40,45,50,55,60
    rel_max_dist=np.max(radius)
    
    evetnFileName="WholeYearWETevent_100_50.txt"
    weatherEvent0=[]
    trafficEvent0=[]
    sta_loc=Set()
    tmc_loc=Set()
    lat=[]
    lon=[]
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
            dist=0.0
        else:
            dist=calcDistance(a[1], a[2],b[1],b[2])
        if dist>rel_max_dist:
            continue               
        pair_dist[str(min(a[0],b[0]))+"_"+str(max(a[0],b[0]))]=dist
        
    for (a,b) in itertools.combinations(tmc_loc, 2):
        if a[1]==b[1] and a[2]==b[2]:
            dist=0.0
        else:
            dist=calcDistance(a[1], a[2],b[1],b[2])
        if dist>rel_max_dist:
            continue              
        pair_dist[str(min(a[0],b[0]))+"_"+str(max(a[0],b[0]))]=dist 
        
    for (a,b) in list(itertools.product( sta_loc,tmc_loc)):
        if a[1]==b[1] and a[2]==b[2]:
            dist=0.0
        else:
            dist=calcDistance(a[1], a[2],b[1],b[2])
        if dist>rel_max_dist:
            continue              
        pair_dist[str(min(a[0],b[0]))+"_"+str(max(a[0],b[0]))]=dist 
    print "All-pairs",len(pair_dist)  
    
    coordinates=list(sta_loc) + list(tmc_loc)
    for timeThreshold in timeThresholds:        
        for r in radius:
            t0=time.time()
            print("Geo Radius=%d Time Radius=%d "%(r,timeThreshold))
            testStatisticsScore,pairType=PIC(weatherEvent0,trafficEvent0,r,timeThreshold,pair_dist)    
            print testStatisticsScore
            output.write(str(testStatisticsScore)+" ")
            output.flush()   
            above=0.0
            for i in tqdm(range(ite)):                
                weatherEvent=[]
                trafficEvent=[]
                for event in weatherEvent0:
                    lat_lon=random.choice(coordinates)                    
                    weatherEvent.append((event[0],lat_lon[1],lat_lon[2],event[3],lat_lon[0])) 
                for event in trafficEvent0:
                    lat_lon=random.choice(coordinates)
                    trafficEvent.append((event[0],lat_lon[1],lat_lon[2],event[3],lat_lon[0]))
                          
                                
                
                if testStatisticsScore<=score:
                    above+=1.0
                score,pairType=PIC(weatherEvent,trafficEvent,r,timeThreshold,pair_dist)
                output.write(str(score)+"| ")
                output.flush()
#                 if i%100==0:
#                     sys.stdout.write('i='+str(i)+" ")
            output.write("\n")
            output.flush()
            sys.stdout.write("\nTest Statistics PIC=%d p-value=%f \n\n"%(testStatisticsScore,1.0*above/ite))
            output.write(str(timeThreshold)+" "+str(r)+" "+str(above)+" "+ str(1.0*above/ite)+"\n")
            output.flush()
             

    output.close()
if __name__ =='__main__':
    main()