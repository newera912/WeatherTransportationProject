
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
    
def getTrafficEvent(fileName):
    trafficEvents=[]
    with open(fileName,"r") as wF:
        for count,line in enumerate(wF.readlines()):
            if count>120:
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

def main2():
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
    
    trafficFielName="F:/workspace/git/Graph-MP/outputs/trafficData/travelTime_CaseStudy/CPBest/10/I90trafficEAllYearEvent_TopK_result_baseMeanDiff_20_s_10_wMax_12_filter_TIncld_0.7_Top_multi.txt"
    trafficEventsE=getTrafficEvent(trafficFielName)
    
    trafficFielName="F:/workspace/git/Graph-MP/outputs/trafficData/travelTime_CaseStudy/CPBest/10/I90trafficWAllYearEvent_TopK_result_baseMeanDiff_20_s_10_wMax_12_filter_TIncld_0.7_Top_multi.txt"
    trafficEventsW=getTrafficEvent(trafficFielName)
    
    print len(weatherEvents),len(trafficEventsE)+len(trafficEventsW)
    statOutWTMC=open("F:/workspace/git/Graph-MP/data/events/Wstat_tmc.txt","w")
    statOutETMC=open("F:/workspace/git/Graph-MP/data/events/Estat_tmc.txt","w")
    statOutW=open("F:/workspace/git/Graph-MP/data/events/stat_w.txt","w")
    with open("F:/workspace/git/Graph-MP/data/events/WholeYearWETevents_New.txt","w") as output:
        for event in weatherEvents:
            output.write("0 "+str(station[event[0]][0])+" "+str(station[event[0]][1])+" "+str(event[1])+"\n")
            statOutW.write(str(event[0])+" "+str(event[1][:8])+" "+str(event[1][8:])+"\n")
        for event in trafficEventsE:
            output.write("1 "+str(tmcsE[event[0]][0])+" "+str(tmcsE[event[0]][1])+" "+str(event[1])+"\n")
            statOutWTMC.write(str(event[0])+" "+str(event[1][:8])+" "+str(event[1][8:])+"\n")
        for event in trafficEventsW:
            output.write("1 "+str(tmcsW[event[0]][0])+" "+str(tmcsW[event[0]][1])+" "+str(event[1])+"\n") 
            statOutETMC.write(str(event[0])+" "+str(event[1][:8])+" "+str(event[1][8:])+"\n")  
            
def main():
    weatherFileName="F:/workspace/git/Graph-MP/outputs/mesonetPlots/multi_CaseStudy/CP/2/AllYearEvent_multiGraphMP_TopK_result-CP_baseMeanDiff_20_s_2_wMax_18_filter_TIncld_0.7_Top.txt"
    weatherEvents=getWeatherEvent(weatherFileName)
    
    print weatherEvents
    
    trafficFielName="F:/workspace/git/Graph-MP/outputs/trafficData/travelTime_CaseStudy/CPBest/15/I90trafficEAllYearEvent_TopK_result_baseMeanDiff_20_s_15_wMax_12_filter_TIncld_0.7_Top_multi.txt"
    trafficEvents=getTrafficEvent(trafficFielName)
    with open("F:/workspace/git/Graph-MP/data/events/WholeYearI90East_events.txt","w") as output:
        for event in weatherEvents:
            output.write("0 "+str(event[0])+" "+str(event[1])+"\n")
        for event in trafficEventsE:
            output.write("1 "+str(tmcsE[event[0]][0])+" "+str(tmcsE[event[0]][1])+" "+str(event[1])+"\n")
        for event in trafficEventsW:
            output.write("1 "+str(tmcsW[event[0]][0])+" "+str(tmcsW[event[0]][1])+" "+str(event[1])+"\n")    
        
    
    
    

if __name__ =='__main__':
    main2()