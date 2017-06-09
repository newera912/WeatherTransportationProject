import operator

def getWeatherEvent(fileName):
    weatherEvents=[]
    wDates=[]
    with open(fileName,"r") as wF:
        for count,line in enumerate(wF.readlines()):
            if count>200:
                continue
            terms=line.strip().split(" ")            
            stations=map(int,terms[2].split(","))
            date=terms[3]
            sta_slot=int(terms[4].split(",")[0])
            end_slot=int(terms[4].split(",")[1])
            slots=[i for i in range(sta_slot,end_slot)]            
            for sta in stations:
                for timeSlot in slots:
                    time=date
                    #print sta,time
                    weatherEvents.append((sta,time))
            wDates.append(date)
    return weatherEvents,wDates
    
def getTrafficEvent(fileName):
    trafficEvents=[]
    tDates=[]
    with open(fileName,"r") as wF:
        for count,line in enumerate(wF.readlines()):
            if count>200:
                continue
            terms=line.strip().split(" ")            
            tmcs=map(int,terms[2].split(","))
            date=terms[3]
            sta_slot=int(terms[4].split(",")[0])
            end_slot=int(terms[4].split(",")[1])
            slots=[i for i in range(sta_slot,end_slot)]            
            for tmc in tmcs:
                for timeSlot in slots:
                    time=date
                    #print sta,time
                    trafficEvents.append((tmc,time))
            tDates.append(date)
    return trafficEvents,tDates

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
    weatherFileName="F:/workspace/git/Graph-MP/outputs/mesonetPlots/multi_CaseStudy/CP/2/Event201603_multiGraphMP_TopK_result-CP_baseMeanDiff_20_s_2_wMax_18_filter_TIncld_0.7_Top.txt"
    weatherEvents=getWeatherEvent(weatherFileName)
    
    print weatherEvents
    
    trafficFielName="F:/workspace/git/Graph-MP/data/trafficData/Mar_I90Event.txt"
    trafficEvents=getTrafficAccident(trafficFielName)
    with open("F:/workspace/git/Graph-MP/data/events/March_events.txt","w") as output:
        for event in weatherEvents:
            output.write("0 "+str(event[0])+" "+str(event[1])+"\n")
        for event in trafficEvents:
            output.write(str(event[0])+" "+str(event[1])+" "+str(event[2])+"\n")
            
def main():
    weatherFileName="F:/workspace/git/Graph-MP/outputs/mesonetPlots/multi_CaseStudy/CP/2/AllYearEvent_multiGraphMP_TopK_result-CP_baseMeanDiff_20_s_2_wMax_18_filter_TIncld_0.7_Top.txt"
    weatherEvents,wDates=getWeatherEvent(weatherFileName)
    
   
    
    trafficFielName="F:/workspace/git/Graph-MP/outputs/trafficData/travelTime_CaseStudy/CP/15/I90trafficEAllYearEvent_TopK_result-CP_baseMeanDiff_20_s_15_wMax_12_filter_TIncld_0.7_Top_multi.txt"
    trafficEvents,tDates=getTrafficEvent(trafficFielName)
    
    trafficFielName="F:/workspace/git/Graph-MP/outputs/trafficData/travelTime_CaseStudy/CP/15/I90trafficWAllYearEvent_TopK_result-CP_baseMeanDiff_20_s_15_wMax_12_filter_TIncld_0.7_Top_multi.txt"
    trafficEvents2,tDates2=getTrafficEvent(trafficFielName)
    trafficEvents+=trafficEvents2
    tDates+=tDates2
    print "#Weather: ",len(weatherEvents)
    print "#Traffic: ",len(trafficEvents)
    
    wDatesD={}
    tDatesD={}
    for w in wDates:
        date=int(w)
        if wDatesD.has_key(date):
            wDatesD[date]+=1
        else:
            wDatesD[date]=1
            
    for t in tDates:
        date=int(t)
        if tDatesD.has_key(date):
            tDatesD[date]+=1
        else:
            tDatesD[date]=1
    sorted_wDates = sorted(wDatesD.items(), key=operator.itemgetter(1), reverse=True) 
    sorted_tDates = sorted(tDatesD.items(), key=operator.itemgetter(1), reverse=True)    
    print "Weather: ",len(sorted_wDates),sorted_wDates
    print "Traffic: ",len(sorted_tDates),sorted_tDates   
    keys_a = set(wDatesD.keys())
    keys_b = set(tDatesD.keys())
    intersection = keys_a & keys_b # '&' operator is used for set intersection
    print len(intersection),intersection
    
#     with open("F:/workspace/git/Graph-MP/data/events/WholeYearI90West_events.txt","w") as output:
#         for event in weatherEvents:
#             output.write("0 "+str(event[0])+" "+str(event[1])+"\n")
#         for event in trafficEvents:
#             output.write("1 "+str(event[0])+" "+str(event[1])+"\n")
        
    
    
    

if __name__ =='__main__':
    main()