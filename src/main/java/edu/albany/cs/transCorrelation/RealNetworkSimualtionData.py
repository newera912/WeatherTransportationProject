import numpy as np
from collections import defaultdict
from math import *
from datetime import date, datetime, timedelta
import random
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

def getWeatherEvent(fileName):
    weatherEvents=[]
    with open(fileName,"r") as wF:
        for count,line in enumerate(wF.readlines()):
            if count>300:
                continue
            terms=line.strip().split(" ")            
            stations=map(int,terms[2].split(","))
            date=terms[3]
            sta_slot=int(terms[4].split(",")[0])
            end_slot=int(terms[4].split(",")[1])
            slots=[i for i in range(sta_slot,end_slot)]            
            for sta in stations:
                for timeSlot in slots:
                    time=date+"%03d"%timeSlot
                    #print sta,time
                    weatherEvents.append((sta,time))
    return weatherEvents
    
def getTrafficEvent(fileName,start_index,max_event):
    trafficEvents=[]
    with open(fileName,"r") as wF:
        for count,line in enumerate(wF.readlines()):
            if count>max_event or count <start_index:
                continue
            terms=line.strip().split(" ")            
            tmcs=map(int,terms[2].split(","))
            date=terms[3]
            sta_slot=int(terms[4].split(",")[0])
            end_slot=int(terms[4].split(",")[1])
            slots=[i for i in range(sta_slot,end_slot)]            
            for tmc in tmcs:
                for timeSlot in slots:
                    time=date+"%03d"%timeSlot
                    #print sta,time
                    trafficEvents.append((tmc,time))
    return trafficEvents

def getTrafficAccident(fileName):
    root="F:/workspace/git/Graph-MP/data/trafficData/"    
    """read TMC and stations information"""
    
    
    tmcID={}    
    with open(root+"TMCsCenterLatLon.txt","r") as tF:
        for j,line in enumerate(tF.readlines()):
            #print j+1,line.strip()
            line=line.strip().split()           
            tmcID[line[0]]=j
          
    trafficEvents=[]
    with open(fileName,"r") as wF:
        for count,line in enumerate(wF.readlines()):
            
            terms=line.strip().split(" ")   
            print terms          
            trafficEvents.append((terms[0],tmcID[terms[1]],terms[2]))
    return trafficEvents    


def perdelta(start, end, delta):
    curr = start
    while curr < end:
        yield curr
        curr += delta

def Case3():
    root="F:/workspace/git/WeatherTransportationProject/data/trafficData/I90_TravelTime/"
    tmcLoc="I90EastTMCLatLon.txt"  
    outFile="RNSimuEvents_Case3.txt"  
    
    """read TMC and stations information"""    
    station={}    
    tmcsE={}
    tmcIDE={}
    tmcsW={}
    tmcIDW={}
    # stationID={}
    with open(root+"StationLatLong.txt","r") as sF:
        for i,line in enumerate(sF.readlines()):
            line=line.strip().split()
            station[i+100]=(float(line[1]),float(line[2]))
           
    
    
    with open(root+"I90EastTMCLatLon.txt","r") as tF:
        for j,line in enumerate(tF.readlines()):
            line=line.strip().split()
            tmcsE[j+200]=(float(line[1]),float(line[2]))
            
           
    
    with open(root+"I90WestTMCLatLon.txt","r") as tF:
        for j,line in enumerate(tF.readlines()):
            line=line.strip().split()
            tmcsW[j+300]=(float(line[1]),float(line[2]))
    
    maxDist=10        
    stat_tmc=defaultdict(list) 
    for stat in station.keys():
        for te in tmcsE.keys():
            if calcDistance(station[stat][0], station[stat][1], tmcsE[te][0], tmcsE[te][1])<=maxDist:
                stat_tmc[stat].append(te)   
        for te in tmcsW.keys():
            if calcDistance(station[stat][0], station[stat][1], tmcsW[te][0], tmcsW[te][1])<=maxDist:
                stat_tmc[stat].append(te)
    for k,v in stat_tmc.items():
        print k,v 
    
    true_stations=[100,102,103]
    true_dates=["20160301","20160401","20160501","20160601","20160701","20160801","20160901"]  
    
    """list the dates except the true dates"""
    dates=[]
    for result in perdelta(date(2016, 3, 1), date(2016, 9, 30), timedelta(days=1)):
        d=str(result).replace("-","")
        dates.append(d)
#         else:
#             print d
    random.shuffle(dates)
    wDates=dates#[:len(dates)/2]
    tDates=dates#[len(dates)/2:]
    
#     print wDates
#     print tDates
    weatherEvents=[]
    trafficEvents=[]
    """each event length fixed to 3"""
    #add true cooccurrence events
    for d in true_dates:
        for s in true_stations:            
            for t in stat_tmc[s]:
                start_times=random.randrange(75, 235)
                for start_time in range(start_times,start_times+3):
                    weatherEvents.append((s,d+"%03d"%(start_time)))
                    trafficEvents.append((t,d+"%03d"%(start_time)))
    print len(weatherEvents)
    print len(trafficEvents)          
    
    #add non-cooccurrence weather events
    for d in random.sample(wDates,100):
        for s in random.sample(station.keys(),5):
            start_times=random.randrange(75, 235)
            for start_time in range(start_times,start_times+3):
                weatherEvents.append((s,d+"%03d"%(start_time)))
    
    #add non-cooccurrence traffic events            
    for d in random.sample(tDates,50):
        for t in random.sample(tmcsE.keys(),5):
            start_times=random.randrange(75, 235)
            for start_time in range(start_times,start_times+3):
                trafficEvents.append((t,d+"%03d"%(start_time)))
    
    #add non-cooccurrence traffic events            
    for d in random.sample(tDates,50):
        for t in random.sample(tmcsW.keys(),5):
            start_times=random.randrange(75, 235)
            for start_time in range(start_times,start_times+3):
                trafficEvents.append((t,d+"%03d"%(start_time)))
    
    
    print len(weatherEvents)
    print len(trafficEvents)                  
    with open(outFile,"w") as output:
        for event in weatherEvents:
            output.write("0 "+str(station[event[0]][0])+" "+str(station[event[0]][1])+" "+str(event[1])+" "+str(event[0])+"\n")
            
        for event in trafficEvents:
            if tmcsE.has_key(event[0]):
                output.write("1 "+str(round(tmcsE[event[0]][0]))+" "+str(round(tmcsE[event[0]][1]))+" "+str(event[1])+" "+str(event[0])+"\n")
            else:
                output.write("1 "+str(round(tmcsW[event[0]][0]))+" "+str(round(tmcsW[event[0]][1]))+" "+str(event[1])+" "+str(event[0])+"\n")
    
            

def Case1():
    root="F:/workspace/git/WeatherTransportationProject/data/trafficData/I90_TravelTime/"
    tmcLoc="I90EastTMCLatLon.txt"  
    outFile="RNSimuEvents_Case1.txt"  
    
    """read TMC and stations information"""    
    station={}    
    tmcsE={}
    tmcIDE={}
    tmcsW={}
    tmcIDW={}
    # stationID={}
    with open(root+"StationLatLong.txt","r") as sF:
        for i,line in enumerate(sF.readlines()):
            line=line.strip().split()
            station[i+100]=(float(line[1]),float(line[2]))
           
    
    
    with open(root+"I90EastTMCLatLon.txt","r") as tF:
        for j,line in enumerate(tF.readlines()):
            line=line.strip().split()
            tmcsE[j+200]=(float(line[1]),float(line[2]))
            
           
    
    with open(root+"I90WestTMCLatLon.txt","r") as tF:
        for j,line in enumerate(tF.readlines()):
            line=line.strip().split()
            tmcsW[j+300]=(float(line[1]),float(line[2]))
    
    maxDist=10        
    stat_tmc=defaultdict(list) 
    for stat in station.keys():
        for te in tmcsE.keys():
            if calcDistance(station[stat][0], station[stat][1], tmcsE[te][0], tmcsE[te][1])<=maxDist:
                stat_tmc[stat].append(te)   
        for te in tmcsW.keys():
            if calcDistance(station[stat][0], station[stat][1], tmcsW[te][0], tmcsW[te][1])<=maxDist:
                stat_tmc[stat].append(te)
    for k,v in stat_tmc.items():
        print k,v 
    
    true_stations=[100,102,103]
    true_dates=["20160301","20160401","20160501","20160601","20160701","20160801","20160901"]  
    
    """list the dates except the true dates"""
    dates=[]
    for result in perdelta(date(2016, 3, 1), date(2016, 9, 30), timedelta(days=1)):
        d=str(result).replace("-","")
        if d not in true_dates:
            dates.append(d)
#         else:
#             print d
    random.shuffle(dates)
    wDates=dates[:len(dates)/2]
    tDates=dates[len(dates)/2:]
    
#     print wDates
#     print tDates
    weatherEvents=[]
    trafficEvents=[]
    """each event length fixed to 3"""
    #add true cooccurrence events
    for d in true_dates:
        for s in true_stations:            
            for t in stat_tmc[s]:
                start_times=random.randrange(75, 235)
                for start_time in range(start_times,start_times+3):
                    weatherEvents.append((s,d+"%03d"%(start_time)))
                    trafficEvents.append((t,d+"%03d"%(start_time)))
    print len(weatherEvents)
    print len(trafficEvents)          
    
    #add non-cooccurrence weather events
    for d in random.sample(wDates,100):
        for s in random.sample(station.keys(),5):
            start_times=random.randrange(75, 235)
            for start_time in range(start_times,start_times+3):
                weatherEvents.append((s,d+"%03d"%(start_time)))
    
    #add non-cooccurrence traffic events            
    for d in random.sample(tDates,50):
        for t in random.sample(tmcsE.keys(),5):
            start_times=random.randrange(75, 235)
            for start_time in range(start_times,start_times+3):
                trafficEvents.append((t,d+"%03d"%(start_time)))
    
    #add non-cooccurrence traffic events            
    for d in random.sample(tDates,50):
        for t in random.sample(tmcsW.keys(),5):
            start_times=random.randrange(75, 235)
            for start_time in range(start_times,start_times+3):
                trafficEvents.append((t,d+"%03d"%(start_time)))
    
    
    print len(weatherEvents)
    print len(trafficEvents)                  
    with open(outFile,"w") as output:
        for event in weatherEvents:
            output.write("0 "+str(station[event[0]][0])+" "+str(station[event[0]][1])+" "+str(event[1])+" "+str(event[0])+"\n")
            
        for event in trafficEvents:
            if tmcsE.has_key(event[0]):
                output.write("1 "+str(round(tmcsE[event[0]][0]))+" "+str(round(tmcsE[event[0]][1]))+" "+str(event[1])+" "+str(event[0])+"\n")
            else:
                output.write("1 "+str(round(tmcsW[event[0]][0]))+" "+str(round(tmcsW[event[0]][1]))+" "+str(event[1])+" "+str(event[0])+"\n")
    
           

def round(x):    
    return np.round(100.0*x)/100.0    

if __name__ =='__main__':
    Case1()