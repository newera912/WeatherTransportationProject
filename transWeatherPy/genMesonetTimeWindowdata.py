from operator import itemgetter

sta_names={"BATA":0,"SBRI":1,"WATE":2,"JORD":3,"CSQR":4,"WEST":5,"COLD":6,"SPRA":7,"COBL":8,"STEP":9}
mons=["201604","201605","201606","201607","201609"]
days=['01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31']
type={"temp":2,"wind":5}

import csv

for k,v in type.items():
    for mon in mons:
        for day in days:
            cur_date=mon+day
            output=open("F:/workspace/git/TranWeatherProject/data/mesonet_data/"+k+"/"+cur_date+".txt","a+")
            data=[]
            
            with open("F:/workspace/git/TranWeatherProject/data/output.txt","r") as oF:
                
                for line in oF.readlines():
                    sta_name=line.strip().split("-")[0]
                    if sta_name not in sta_names.keys():
                        continue
                    if mon!=line.strip().split("-")[1][:-4]:
                        #print sta_name,mons,line.strip().split("-")[1][:-4]
                        continue
                    
                    fileName="F:/workspace/git/TranWeatherProject/data/data_request/"+line.strip()
                    
                    sta_y_m=line.strip().split(".")[0]
                    #print sta_y_m
                    temp_line=""
                    with open(fileName,"r") as csvF:
                        datac=csv.reader(csvF)
                        next(datac, None)  # skip the headers
                        #staName=str(sta_names[sta_name])
                        #output.write(staName+" ")
                        dayList=[]
                        count=0
                        prev_value=-100
                        for d in datac: 
                            if str(d[1].strip()[:8])!=cur_date:
                                #print d[1].strip()[:8],cur_date
                                continue
                            
                            tempr=str(d[int(v)].strip())
                            if len(tempr)==0:
                                #print tempr
                                tempr=prev_value
                            else:
                                prev_value=tempr
                            #print tempr,d[1].strip()[:8],cur_date
                            temp_line+=str(tempr)+" "
                            count+=1
                        print sta_name,cur_date,count   
                        data.append((sta_names[sta_name],temp_line+"\n"))
            print data[0][0],data[0][1]
            data=sorted(data,key=itemgetter(0))
            print data[0][0],data[1][0],data[2][0]
            for e in data:
                output.write(str(e[0])+" "+e[1])                
            output.close
            
            
