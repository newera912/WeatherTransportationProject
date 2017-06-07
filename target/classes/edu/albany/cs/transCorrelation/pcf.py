import numpy as np
from math import *
import numpy as np
import random,time,math
import collections

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




    
def PCF(weatherEvent,trafficEvent,pair_distance,r,timeThreshold,A,h):
    PI=math.pi
    Gr=0
    k=len(weatherEvent)
    l=len(trafficEvent)
    for wev in weatherEvent:
        for tev in trafficEvent:
            if tev[0]==wev[0]:                
                continue
            if  np.abs(tev[2]-wev[2])>timeThreshold:
                continue
            if wev[0]==1:
                temp=wev
                wev=tev
                tev=temp
            
            pair=str(wev[1])+"-"+str(tev[1])
            if pair_distance.has_key(pair): 
                d=np.abs(pair_distance[pair]-r)               
                if d<=h :
                    Gr+=3.0*(1-(d*d)/(h*h))/(4.0*h)           
    Gr=(A*A/(2*PI*r*k*l))*Gr
    return Gr



def main():
    ite=500
    output=open("PCFresult.txt","a+")
    root="F:/workspace/git/Graph-MP/data/trafficData/"    
    """read TMC and stations information"""
    lat=[]
    lon=[]
    station={}    
    tmcs={}
    tmcID={}
    # stationID={}
    with open(root+"StationLatLong.txt","r") as sF:
        for i,line in enumerate(sF.readlines()):
            line=line.strip().split()
            station[i]=line[1]+" "+line[2]+" "+line[0]
            lat.append(float(line[1]))
            lon.append(float(line[2]))
    #         stationID[i]=line[0]    
    
    
    with open(root+"TMCsCenterLatLon.txt","r") as tF:
        for j,line in enumerate(tF.readlines()):
            #print j+1,line.strip()
            line=line.strip().split()
            tmcs[j]=line[1]+" "+line[2]+" "+line[0]
            tmcID[j]=line[0]
            lat.append(float(line[1]))
            lon.append(float(line[2]))
    
    
    """ Calculate Rec region """
    lat_len=calcDistance(np.max(lat), np.abs(np.min(lon)), np.max(lat),np.abs(np.max(lon)))
    lon_len=calcDistance(np.max(lat), np.abs(np.min(lon)), np.min(lat),np.abs(np.min(lon)))
    A=lat_len*lon_len
    
    print '%f X %f =A=%f mile^2'%(lat_len,lon_len,A)
    
    h=0.5
    radius=[5,8,11,14,17,20,23,26]
    MAX_DISTANCE=np.max(radius)
    print 'maximum',MAX_DISTANCE
    pair_distance={}
    for i in range(len(station)):
        for j in range(len(tmcs)):
            sta=map(float,station[i].split()[:2])
            tmc=map(float,tmcs[j].split()[:2])
            pairDist=calcDistance(sta[0],sta[1],tmc[0],tmc[1])
            
            if pairDist<=MAX_DISTANCE:
                pair_distance[str(i)+"-"+str(j)]=pairDist
            else:
                continue
            
    
    evetnFileName="F:/workspace/git/Graph-MP/data/events/March10_events.txt"
    weatherEvent=[]
    trafficEvent=[]
    with open(evetnFileName,"r") as eF:
        for line in eF.readlines():
            terms=line.strip().split()
            terms=map(int,terms) 
            if terms[0]==0:
                weatherEvent.append(terms)
            else:
                trafficEvent.append(terms)
     
    AllEvent=weatherEvent+trafficEvent
    weatherEventNum=len(weatherEvent)
    trafficEventNum=len(trafficEvent)
    print "Weather Event:",len(weatherEvent)
    print "Traffic Event:",len(trafficEvent)
    
    timeThresholds=[0,1,2,3,4,5]    
    
    for timeThreshold in timeThresholds:
        
        for r in radius:
            testStatisticsScore=PCF(weatherEvent,trafficEvent,pair_distance,r,timeThreshold,A,h)            
            print r,testStatisticsScore        
            above=0
            for i in range(ite):
                tempAll=AllEvent
                
                random.shuffle(tempAll)
                weatherEvent=tempAll[:weatherEventNum]
                trafficEvent=tempAll[weatherEventNum:]
                 
                               
                score=PCF(weatherEvent,trafficEvent,pair_distance,r,timeThreshold,A,h)
                
                if testStatisticsScore<score:
                    above+=1
                if i%100==0:
                    print 'i=',i
            print "%d %d %f %f "%(timeThreshold,r,1.0*above/ite,above)
            output.write(str(timeThreshold)+" "+str(r)+" "+ str(1.0*above/ite)+" "+str(above)+"\n")

    output.close()
if __name__ =='__main__':
    main()