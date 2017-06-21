from operator import itemgetter
import numpy as np

sta_names={"BATA":0,"SBRI":1,"WATE":2,"JORD":3,"CSQR":4,"WEST":5,"COLD":6,"SPRA":7,"COBL":8,"STEP":9}
mons=["201603","201604","201605","201606","201607","201608","201609"]
mons=["201608"]
days=['01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31']
type={"wind":5}#"rad":8,"windDir":7,"windMax":6,"rh":4,"precip":10,"temp":2,"temp9":3,"wind":5}
#"press","windDir","windMax","rh","precip","temp","temp9","wind"
import csv
indict=np.zeros((10,217))
dateIdx={}
dayIdx=0
for k,v in type.items():
    for mon in mons:
        for day in days:
            cur_date=mon+day
            output=open("F:/workspace/git/WeatherTransportationProject/data/mesonet_data/"+k+"/"+cur_date+".txt","a+")
            data=[]
            if dateIdx.has_key(cur_date):
                i=1
            else:
                dateIdx[cur_date]=dayIdx
                dayIdx+=1
            with open("F:/workspace/git/WeatherTransportationProject/data/output.txt","r") as oF:
                
                for line in oF.readlines():
                    sta_name=line.strip().split("-")[0]
                    if sta_name not in sta_names.keys():
                        continue
                    if mon!=line.strip().split("-")[1][:-4]:
                        #print sta_name,mons,line.strip().split("-")[1][:-4]
                        continue
                    
                    fileName="F:/workspace/git/WeatherTransportationProject/data/data_request/"+line.strip()
                    
                    sta_y_m=line.strip().split(".")[0]
                    #print sta_y_m
                    temp_line=""
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
                            temp_line+=str(tempr)+" "
                            count+=1
#                         if valueCheck<0:
#                             indict[sta_names[sta_name]][dateIdx[cur_date]]=0
#                         else:
#                             indict[sta_names[sta_name]][dateIdx[cur_date]]=1
                        #print sta_name,cur_date,count   
                        data.append((sta_names[sta_name],temp_line+"\n"))
            print cur_date
            #print data[0][0],data[0][1]
            data=sorted(data,key=itemgetter(0))
            #print data[0][0],data[1][0],data[2][0]
            for e in data:
                output.write(str(e[0])+" "+e[1])                
            output.close
idxDate={v:k for k, v in dateIdx.items()}
missDays=0
for i in range(217):
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
