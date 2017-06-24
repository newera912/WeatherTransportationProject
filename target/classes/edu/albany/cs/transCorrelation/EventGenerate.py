import numpy as np

def getWeatherEvent(fileName):
    weatherEvents=[]
    with open(fileName,"r") as wF:
        for count,line in enumerate(wF.readlines()):
            if count>150:
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

def main():
    root="F:/workspace/git/Graph-MP/data/trafficData/I90_TravelTime/"
    tmcLoc="I90EastTMCLatLon.txt"    
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
            station[i]=(float(line[1]),float(line[2]))
           
    
    
    with open(root+"I90EastTMCLatLon.txt","r") as tF:
        for j,line in enumerate(tF.readlines()):
            #print j+1,line.strip()
            line=line.strip().split()
            tmcsE[j]=(float(line[1]),float(line[2]))
            tmcIDE[j]=line[0]
           
    
    with open(root+"I90WestTMCLatLon.txt","r") as tF:
        for j,line in enumerate(tF.readlines()):
            #print j+1,line.strip()
            line=line.strip().split()
            tmcsW[j]=(float(line[1]),float(line[2]))
            tmcIDW[j]=line[0]
                 
    weatherFileName="F:/workspace/git/Graph-MP/outputs/mesonetPlots/multi_CaseStudy/CP/2/AllYearEvent_multiGraphMP_TopK_result-CP_baseMeanDiff_20_s_2_wMax_18_filter_TIncld_0.7_Top.txt"
    weatherEvents=getWeatherEvent(weatherFileName)
    
    print weatherEvents
    
    trafficFielName="F:/workspace/git/WeatherTransportationProject/outputs/trafficData/travelTime_CaseStudy/CPBest/5/E621I90traffic_AllYearEvent_TopK_result_baseMeanDiff_20_s_5_wMax_18_filter_TIncld_0.7_Top_multi.txt"
    trafficEventsE=getTrafficEvent(trafficFielName,0,100)
    
    
    trafficFielName="F:/workspace/git/WeatherTransportationProject/outputs/trafficData/travelTime_CaseStudy/CPBest/5/W621I90traffic_AllYearEvent_TopK_result_baseMeanDiff_20_s_5_wMax_18_filter_TIncld_0.7_Top_multi.txt"
    trafficEventsW=getTrafficEvent(trafficFielName,0,100)
    
    print len(weatherEvents),len(trafficEventsE)+len(trafficEventsW)
    statOutTMC=open("F:/workspace/git/WeatherTransportationProject/data/events/stat_tmc.txt","w")
    statOutW=open("F:/workspace/git/WeatherTransportationProject/data/events/stat_w.txt","w")
    with open("WholeYearWETevents_100.txt","w") as output:
        for event in weatherEvents:
            output.write("0 "+str(station[event[0]][0])+" "+str(station[event[0]][1])+" "+str(event[1])+" "+str("1"+"%02d"%event[0])+"\n")
            statOutW.write("")
        for event in trafficEventsE:
            output.write("1 "+str(round(tmcsE[event[0]][0]))+" "+str(round(tmcsE[event[0]][1]))+" "+str(event[1])+" "+str("2"+"%02d"%event[0])+"\n")
        for event in trafficEventsW:
            output.write("1 "+str(round(tmcsW[event[0]][0]))+" "+str(round(tmcsW[event[0]][1]))+" "+str(event[1])+" "+str("3"+"%02d"%event[0])+"\n")   
            

def round(x):    
    return np.round(100.0*x)/100.0    

if __name__ =='__main__':
    main()