import numpy as np
from math import *
import matplotlib.pyplot as plt
import numpy as np
# input Lat_A 
# input Lng_A 
# input Lat_B  
# input Lng_B 
# output distance mile
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



station=[]
# stationID={}
with open("StationLatLong.txt","r") as sF:
    for i,line in enumerate(sF.readlines()):
        line=line.strip().split()
        station.append((line[0],float(line[1]),float(line[2])))
#         stationID[i]=line[0]


tmcs=[]
tmcID={}

with open("I90WestTMCLatLon.txt","r") as tF:
    for j,line in enumerate(tF.readlines()):
        #print j+1,line.strip()
        line=line.strip().split()
        tmcs.append((line[0],float(line[1]),float(line[2])))
        tmcID[j]=line[0]
print tmcID
sta_tmc_dist=[[] for i in range(len(station))]
distances=[]
for i,s in enumerate(station):
    for t in tmcs:  
        dist=round(calcDistance(t[1], t[2], s[1], s[2])*100.0)/100.0     
#         temp+=str(calcDistance(t[1], t[2], s[1], s[2]))+" "
        sta_tmc_dist[i].append((t[0],dist))
        distances.append(dist) 
    #print '>>',sta_tmc_dist[i]
    sta_tmc_dist[i].sort(key=lambda x:x[1])
    print sta_tmc_dist[i]
tmc_check=[]
for j in range(len(tmcs)):
    temp=""
    for i in range(10):
        temp+=sta_tmc_dist[i][j][0]+"("+str(sta_tmc_dist[i][j][1])+") "
        if j<=15:
            tmc_check.append(sta_tmc_dist[i][j][0])    
    print temp
print len(tmc_check)
print len(list(set(tmc_check)))
distances=sorted(distances)[:150]

fig=plt.figure(1)
min=int(np.min(distances))
max=int(np.max(distances))
bins=[i for i in range(min-1,max+1)]
plt.hist(distances,bins=bins)
axes = plt.gca()

# axes.set_ylim([0,10])
# axes.set_xlim([0,18])
plt.title("I90-West Distance histogram")
plt.xlabel("mile")
plt.ylabel("Frequency")
plt.grid()
fig = plt.gcf()
plt.show()  
fig.savefig('I90-West Distance histogram.png')  
plt.close()
print "\n\n" 
