import numpy as np
from math import *
import numpy as np
import random,time,math
import collections
import SimuData
import threading


exitFlag = 0
class myThread (threading.Thread):
    def __init__(self,rData,ite,output,radius):
        threading.Thread.__init__(self)        
        self.rData=rData
        self.ite=ite
        self.radius=radius
        self.output=output
    def run(self):
        weatherEvent0,trafficEvent0=SimuData.getEvents(self.rData,500)
        weatherEventNum=len(weatherEvent0)
        trafficEventNum=len(trafficEvent0)
        print "Weather Event:",len(weatherEvent0)
        print "Traffic Event:",len(trafficEvent0)
        AllEvent=weatherEvent0+trafficEvent0   
                
        for r in self.radius:
            testStatisticsScore=PCF(weatherEvent0,trafficEvent0,r)            
            print self.rData,r,testStatisticsScore        
            above=0.0
            for i in range(self.ite):
                tempAll=AllEvent
                
                random.shuffle(tempAll)
                weatherEvent=tempAll[:weatherEventNum]
                trafficEvent=tempAll[weatherEventNum:]
                 
                               
                score=PCF(weatherEvent,trafficEvent,r)
                print score
                if testStatisticsScore<=score:
                    above+=1.0
                if i%100==0:
                    print 'i=',i
            print "%d %f %f "%(r,1.0*above/self.ite,above)
            self.output.write(str(self.rData)+" "+str(r)+" "+ str(1.0*above/self.ite)+" "+str(above)+"\n")
            self.output.flush()
    
def PCF(weatherEvent,trafficEvent,h):
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
    #Gr=*Gr
    return Gr

def main():
    ite=500
    
    output=open("SimuPCFresult4.txt","a+") 
    
    radiusData=[10,20,30,40]
    radius=[5,10,15,20,25,30,35,40,45]
    h=hh
    PI=math.pi
    V=0.75*(PI*100**3)
    #print V,PI
    for rData in radiusData:
        weatherEvent0,trafficEvent0=SimuData.getEvents(rData,500)
        weatherEventNum=len(weatherEvent0)
        trafficEventNum=len(trafficEvent0)
        print "Weather Event:",len(weatherEvent0)
        print "Traffic Event:",len(trafficEvent0)
        AllEvent=weatherEvent0+trafficEvent0   
                
        for r in radius:
            testStatisticsScore=PCF(weatherEvent0,trafficEvent0,r)            
            print rData,r,testStatisticsScore        
            above=0.0
            for i in range(ite):
                tempAll=AllEvent
                
                random.shuffle(tempAll)
                weatherEvent=tempAll[:weatherEventNum]
                trafficEvent=tempAll[weatherEventNum:]
                 
                               
                score=PCF(weatherEvent,trafficEvent,r)
                print score
                if testStatisticsScore<=score:
                    above+=1.0
                if i%100==0:
                    print 'i=',i
            print "%d %f %f "%(r,1.0*above/ite,above)
            output.write(str(rData)+" "+str(r)+" "+ str(1.0*above/ite)+" "+str(above)+"\n")
    
    output.close()

def main2():
    ite=2
    
    output=open("SimuPCFresult4.txt","a+") 
    
    radiusData=[10,20,30,40]
    radius=[5,10,15,20,25,30,35,40,45]
    
    PI=math.pi
    V=0.75*(PI*100**3)
    #print V,PI
    threads=[]
    for rData in radiusData:
        threads.append(myThread(rData,ite,output,radius))
    for thread in threads:
        thread.start()
    for thread in threads:
        thread.join()
    print "Exiting the Program!!!"            
    
    output.close()
    
if __name__ =='__main__':
    main2()


