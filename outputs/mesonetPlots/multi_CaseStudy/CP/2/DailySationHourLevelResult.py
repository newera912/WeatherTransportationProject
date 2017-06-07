import matplotlib.pyplot as plt
import numpy as np
from sets import Set
import math,os

from collections import defaultdict
from operator import itemgetter, attrgetter  
#from pandas.util.doctools import idx

months=["201603","201604","201605","201606","201607","201608","201609"]
freq=[]
for mon in months:
    fileName=mon+"_multiGraphMP_TopK_result-CP_baseMeanDiff_20_s_2_wMax_18_filter_TIncld_0.7_Top.txt"
    date_patt = []
    dateSet=Set()
    with open(fileName,"r") as f:        
        for line in f.readlines():
            terms=line.strip().split()
            topK=terms[0]
            station=map(int,terms[2].split(","))
            date=terms[3]
            start=5*int(terms[4].split(",")[0])
            end=5*int(terms[3].split(",")[0])
            start=terms[4].split(",")[0]
            end=terms[4].split(",")[1]
            #time="%d:%02d"%(start / 60, start % 60)+"~"+"%d:%02d"%(end / 60, end % 60)
            date_patt.append((date,station,int(start),int(end)))
            dateSet.add(date)
#     d = dict((k, tuple(v)) for k, v in date_patt.iteritems())
#     for k,v in date_patt: 
#         print k,v
#     date_tuple=[]
#     for k in d.keys():
#         date_tuple.append((k,len(d[k]),",".join(d[k])))
        #print k,len(d[k]),d[k]
    
    for date in date_patt:
        if date[0]=="20160301":
            print date
    time.sleep(1000)
    dateSet=sorted(dateSet)
    date_idx={}
    idx=0
    for d in list(dateSet):
        date_idx[d]=idx
        idx+=1
    mon_daily=[[] for i in range(len(date_idx))]
    for i in range(len(mon_daily)):
        mon_daily[i]=np.zeros((24,10))  
    for term in date_patt:
        date=term[0]   
        stats=list(term[1])
        start=int(term[2])
        end=int(term[3])
        s=int(math.floor(start/12.0))
        e=int(math.ceil(end/12.0))
        if e==24 :
            e=23
        #print date,stats,start,end,s,e
        for hour in range(s,e+1): 
            for stat in stats:
                print date_idx[date],hour,stat            
                mon_daily[date_idx[date]][hour][stat]=1
    root="F:/workspace/git/Graph-MP/outputs/mesonetPlots/hourlyPatterns/"+mon+"/"
    if os.path.exists(root)==False:
        os.makedirs(root)
    idx_date = dict(zip(date_idx.values(),date_idx.keys()))
    for i in range(len(mon_daily)):
        with open(root+idx_date[i]+".txt","w") as f:
            for h in range(24):
                print i,h,mon_daily[i][h].tolist()
                f.write(" ".join(map(str,map(int,mon_daily[i][h].tolist())))+"\n")
    