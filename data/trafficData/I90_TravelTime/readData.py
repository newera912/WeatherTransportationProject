import os
import csv
import sys
import numpy as np
print sys.maxsize
csv.field_size_limit(922337203)

dataFile=open('TMCsCenterLatLon.txt','r')

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
edgeFile.write(str(0)+" "+str(len(tmcID)-1)+"\n")

edgeFile.close()

out_root="./rawData/"
roadSections=['lawson_I-90W_Ex29_Ex33','lawson_I-90W_Ex33_Ex40','lawson_I-90W_Ex40_Ex43','lawson_I-90W_Ex43_Ex48','lawson_I-90W_Ex48_Ex52A','lawson_I-90W_Mass_Ex25','lawson_I-90W_Mass_Ex25_Ex29']

      
for file in os.listdir('./'+roadSections[0]+'/' ):
    data=np.zeros((len(tmcID),288))
    #tmcList=set() 
    #child = os.path.join('%s%s' % (secs, file))
    count=0
    for sec in roadSections:
        
        fileName=sec+"/"+file
        with open(fileName,"r") as csvF:
            datac=csv.reader(csvF)
            next(datac, None)  # skip the headers
            for d in datac:
                timeSlot=int(d[1])
                value=int(float(d[2]))
                #print value
                data[tmcID[d[0]]][timeSlot]=value
                count+=1
    print count,"/",77*288
    with open(out_root+file.split(".")[0]+".txt","w") as output:
        for i in range(0,len(data)):
            count=0
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
                count+=1        
                output.write(str(data[i][j])+" ")
            output.write("\n")
            if count!=288:
                print count
            
            #print d[0]
    #print len(tmcList),("".join(list(tmcList)))
