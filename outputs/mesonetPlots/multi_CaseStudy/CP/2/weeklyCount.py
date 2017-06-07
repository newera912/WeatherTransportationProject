import matplotlib.pyplot as plt
import numpy as np
import datetime
import time
from isoweek import Week
# from datetime import datetime


months=["201603","201604","201605","201606","201607","201608","201609"]

from collections import defaultdict
from operator import itemgetter, attrgetter 


     
 
weeks=[] 
for mon in months:
    fileName=mon+"_multiGraphMP_TopK_result-CP_baseMeanDiff_20_s_2_wMax_18_filter_TIncld_0.7_Top.txt"
    date_patt = defaultdict(list)
    with open(fileName,"r") as f:        
        for i,line in enumerate(f.readlines()):           
            terms=line.strip().split()
            
            date=terms[3]
            dayOfYear=int(format(datetime.datetime(int(date[:4]),int(date[4:6]),int(date[6:])), '%j'))
#             #print date
            the_date = datetime.date(*time.strptime(date, '%Y%m%d')[:3])
  
            monthNumer, week_number, day_number = the_date.isocalendar()
#             print day_number
            weeks.append(dayOfYear)
fig=plt.figure(1)
bins=[i+1 for i in range(365)]
plt.hist(weeks,bins=bins)
axes = plt.gca()
label=[str(i)+"th\n"+str(datetime.datetime(2016, 1, 1) + datetime.timedelta(i - 1))[5:10]  for i in range(np.min(weeks),np.max(weeks)+1,31)]
#label=[str(Week(2016, i).monday())[5:] for i in range(np.min(weeks),np.max(weeks)+1,4)]                 
#axes.set_ylim([0,20])
axes.set_xlim([np.min(weeks)-1,np.max(weeks)+1])
plt.xticks(np.arange(np.min(weeks),np.max(weeks)+1,31),label,fontsize=10)
plt.yticks(fontsize=20)
plt.title("Daily changing pattern count frequency histogram",fontsize=16)
plt.xlabel("th Day",fontsize=12)
plt.ylabel("Changing Pattern Count",fontsize=20)
plt.grid()
plt.setp(plt.gca().get_xticklabels(), rotation=45, horizontalalignment='right')
fig = plt.gcf()
plt.show()  
fig.savefig('F:/workspace/git/Graph-MP/outputs/mesonetPlots/multi_CaseStudy/CP/2/Daily-Frequency histogram_.png')  
plt.close()

#  
# weeks=[] 
# for mon in months:
#     fileName=mon+"_multiGraphMP_TopK_result-CP_baseMeanDiff_20_s_2_wMax_18_filter_TIncld_0.7_Top.txt"
#     date_patt = defaultdict(list)
#     with open(fileName,"r") as f:        
#         for line in f.readlines():
#             terms=line.strip().split()
#             
#             date=terms[3]
#             the_date = datetime.date(*time.strptime(date, '%Y%m%d')[:3])
# 
#             _, week_number, day_number = the_date.isocalendar()
#             weeks.append(week_number)
# fig=plt.figure(1)
# bins=[i+1 for i in range(52)]
# plt.hist(weeks,bins=bins)
# axes = plt.gca()
# label=[str(i)+"th Week\n"+str(Week(2016, i).monday()).replace("-","")  for i in range(np.min(weeks),np.max(weeks)+1,4)]
#                  
# #axes.set_ylim([0,20])
# axes.set_xlim([np.min(weeks)-2,np.max(weeks)+2])
# plt.xticks(np.arange(np.min(weeks),np.max(weeks)+1,4),label,fontsize=14)
# plt.title("Weekly changing pattern count frequency histogram",fontsize=16)
# plt.xlabel("Week Number and date",fontsize=12)
# plt.ylabel("Changing Pattern Frequency",fontsize=16)
# plt.grid()
# plt.setp(plt.gca().get_xticklabels(), rotation=30, horizontalalignment='right')
# fig = plt.gcf()
# plt.show()  
# fig.savefig('F:/workspace/git/Graph-MP/outputs/mesonetPlots/multi_CaseStudy/CP/2/Weekly-Frequency histogram_.png')  
# plt.close()
#  
   