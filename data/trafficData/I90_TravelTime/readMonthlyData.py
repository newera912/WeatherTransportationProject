import os
import csv
import sys
import numpy as np
from calendar import monthrange
print sys.maxsize
csv.field_size_limit(922337203)
direc='West'
dataFile=open('I90'+direc+'TMCLatLon.txt','r')
 
edgeFile=open("edgeList.txt","w")
tmcID={}
for i,line in enumerate(dataFile.readlines()):
    line=line.strip().split()
    tmcID[line[0]]=i  
print tmcID
edgeList=[]
for i in range(len(tmcID)-1):    
    edgeList.append(str(i)+" "+str(i+1))
    edgeFile.write(str(i)+" "+str(i+1)+"\n")

 
edgeFile.close()



year=2016
months=[3,4,5,6,7,8,9,10,11,12]

for mon in months:
    dateList=[]
    a = monthrange(year, mon)
    for day in range(1, a[1]+1):
        date="%d%02d%02d"%(year,mon,day)
        dateList.append(date)
    for date in dateList:
        data=np.zeros((len(tmcID),288)) 
        count=0     
                    
            
        with open('I90west.csv',"r") as F:
            for line in F.readlines()[1:]:
                d=line.strip().split(",")
                if d[2]==date:
                    
                    timeSlot=int(d[1])
                    value=int(float(d[3]))
                    #print value
                    if not tmcID.has_key(d[0]):
                        continue
                    data[tmcID[d[0]]][timeSlot]=value
                    count+=1
                else: 
                    continue
        
        if np.sum(data)>0:                
            with open('./TravelTimeToWest/'+date+".txt","w") as output:
                for i in range(0,len(data)):
                    count2=0
                    for j in range(0,len(data[0])):
                        
                        f=0
                        if j==0 and data[i][j]==0:
                            f=j+1
                            while(data[i][f]==0):
                                if f+1==288:
                                    break
                                f+=1
                            data[i][j]=data[i][f]
                        elif data[i][j]==0:
                            f=j-1
                            while(data[i][f]==0):
                                if f-1==-288:
                                    break
                                f-=1
                            data[i][j]=data[i][f]
                        else:
                            a=0   
                        count2+=1        
                        output.write(str(data[i][j])+" ")
                    output.write("\n")
                    if count2!=288:
                        print count2
        print date,count,"/",77*288    
            #print d[0]
    #print len(tmcList),("".join(list(tmcList)))
