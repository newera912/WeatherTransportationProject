from operator import itemgetter
import numpy as np
import csv,sys,os
from datetime import date, timedelta

def timeToSlot(time):
    hour=int(time[:2])
    minute=int(time[2:])
    return hour*12+(minute/5)

root="/home/apdm01/workspace/git/WeatherTransportationProject/"
#root="F:/workspace/git/WeatherTransportationProject/"
sta_names={"BATA":0,"SBRI":1,"WATE":2,"JORD":3,"CSQR":4,"WEST":5,"COLD":6,"SPRA":7,"COBL":8,"STEP":9}
mons=["201603","201604","201605","201606","201607","201608","201609","201610","201611","201612"]
# mons=["201608"]
# mons=["201609","201610","201611","201612","201701","201702","201703","201704","201705","201706"]
days=['01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31']
type={"temp":2,"temp9":3,"rad":8,"windDir":7,"windMax":6,"rh":4,"press":9,"wind":5} #old order
#type={"temp":4,"temp9":5,"rad":6,"windDir":11,"windMax":10,"rh":7,"press":8,"wind":9}
#"press","windDir","windMax","rh","precip","temp","temp9","wind"

dateList=[]
<<<<<<< HEAD
d1 = date(2016, 3, 1)  # start date
d2 = date(2017, 8, 31)  # end date
=======
d1 = date(2016, 9, 1)  # start date
d2 = date(2017, 6, 30)  # end date
>>>>>>> a29ac963e094b6afdc75c72742207366e35d54c5
delta = d2 - d1         # timedelta

for j,i in enumerate(range(delta.days + 1)):
    dateList.append(str(d1 + timedelta(days=i)).replace("-",""))
    
indict=np.zeros((10,len(dateList)))
dateIdx={}
dayIdx=0
for k,v in type.items():
    sys.stdout.write(k+" ")
    if not os.path.exists(root+"data/mesonet_data/"+k+"/"):
        os.makedirs(root+"data/mesonet_data/"+k+"/") 
    for mon in mons:
        for day in days:            
            cur_date=mon+day
            if cur_date in dateList:                               
                output=open(root+"data/mesonet_data/"+k+"/"+cur_date+".txt","a+")
            else:
                continue
            data=[]
            if dateIdx.has_key(cur_date):
                i=1
            else:
                #print cur_date,dayIdx
                dateIdx[cur_date]=dayIdx
                dayIdx+=1
            with open(root+"data/output.txt","r") as oF:
                 
                for line in oF.readlines():
                    sta_name=line.strip().split("-")[0]
                    if sta_name not in sta_names.keys():
                        continue
                    if mon!=line.strip().split("-")[1][:-4]:
                        #print sta_name,mons,line.strip().split("-")[1][:-4]
                        continue
                     
                    fileName=root+"data/data_request/"+line.strip()
                     
                    sta_y_m=line.strip().split(".")[0]
                    #print sta_y_m
                    temp_line=[-1.0 for ii in range(288)]
                    with open(fileName,"r") as csvF:
                        datac=csv.reader(csvF)
                        next(datac, None)  # skip the headers
                        #staName=str(sta_names[sta_name])
                        #output.write(staName+" ")
                        dayList=[]
                        valueCheck=0
                        count=0
                        prev_value=-1
                        for d in datac: 
                            if str(d[1].strip()[:8])!=cur_date:
                                #print d[1].strip()[:8],cur_date
                                continue
                            slot=timeToSlot(str(d[1].strip()[9:13]))
                            indict[sta_names[sta_name]][dateIdx[cur_date]]=1
                            tempr=str(d[int(v)].strip())
                            if len(tempr)==0:
                                #print tempr
                                tempr=prev_value
                                indict[sta_names[sta_name]][dateIdx[cur_date]]=0
                                valueCheck=-1
                            else:
                                 
                                prev_value=tempr
                            #print tempr,d[1].strip()[:8],cur_date
                           
                            temp_line[slot]=float(tempr)
                            count+=1
#                         if valueCheck<0:
#                             indict[sta_names[sta_name]][dateIdx[cur_date]]=0
#                         else:
#                             indict[sta_names[sta_name]][dateIdx[cur_date]]=1
<<<<<<< HEAD
=======

>>>>>>> a29ac963e094b6afdc75c72742207366e35d54c5
                        #print sta_name,cur_date,count
                        for j in range(len(temp_line)):                            
                            f=0
                            if j==0 and temp_line[j]==-1.0:
                                f=j+1
                                while(temp_line[f]==-1.0):
                                    if f+1==288:
                                        break
                                    f+=1
                                temp_line[j]=temp_line[f]
                            elif temp_line[j]==-1.0:
                                f=j-1
                                while(temp_line[f]==-1.0):
                                    if f-1==-288:
                                        break
                                    f-=1
                                temp_line[j]=temp_line[f]
                            else:
                                a=0    
                        data.append((sta_names[sta_name]," ".join(map(str,temp_line))+"\n"))
            sys.stdout.write(cur_date+" ")
            #print data[0][0],data[0][1]
            data=sorted(data,key=itemgetter(0))
            #print data[0][0],data[1][0],data[2][0]
            for e in data:
                output.write(str(e[0])+" "+e[1])                
            output.close
    sys.stdout.write("\n")
idxDate={v:k for k, v in dateIdx.items()}
missDays=0
for i in range(len(dateList)):
    summ=0
    temp=idxDate[i]+" "
    for j in range(10):
        summ+=indict[j][i]
        temp+=str(int(indict[j][i]))+" "
    if summ==0:
        missDays+=1
        continue
#     if summ==10:
#         print temp
    print temp  
    #print indict
print missDays           
print np.sum(indict,axis=1)            

