import numpy as np

with open("F:/workspace/git/Graph-MP/data/trafficData/mar_Accident_raw.txt","r") as f:
    for line in f.readlines():
        tmcID=line.strip().split()[0]
        date=line.strip().split()[1].split("T")[0].replace("-","")
        terms=map(int,line.strip().split()[1].split("T")[1].split(":"))
        timeSlot=int(terms[0]*12+np.ceil(terms[1]/5.0))
        for i in [-3,-2,-1,0,1,2,3]:
                time=timeSlot+i
                if time<0 or time>287:
                    continue
                print "1",tmcID,date+"%03d"%time
            
            