import matplotlib.pyplot as plt
import numpy as np
months=["201603","201604","201605","201606","201607","201608","201609"]

from collections import defaultdict
from operator import itemgetter, attrgetter  
freq=[]
for mon in months:
    fileName=mon+"_multiGraphMP_TopK_result-CP_baseMeanDiff_20_s_2_wMax_18_filter_TIncld_0.7_Top.txt"
    date_patt = defaultdict(list)
    with open(fileName,"r") as f:        
        for line in f.readlines():
            terms=line.strip().split()
            topK=terms[0]
            date=terms[3]
            start=5*int(terms[4].split(",")[0])
            end=5*int(terms[3].split(",")[0])
            time="%d:%02d"%(start / 60, start % 60)+"~"+"%d:%02d"%(end / 60, end % 60)
            date_patt[date].append(topK+"->"+time)
    d = dict((k, tuple(v)) for k, v in date_patt.iteritems())
#     for k,v in date_patt: 
#         print k,v
    date_tuple=[]
    for k in d.keys():
        date_tuple.append((k,len(d[k]),",".join(d[k])))
        #print k,len(d[k]),d[k]
    
    date_tuple.sort(cmp=None, key=itemgetter(1), reverse=True)
    freq6_temp=0
    freq12_temp=0
    freq18_temp=0
    for t in date_tuple:
        cnt=int(t[1])
        if cnt<=6:
            freq6_temp+=1
        elif cnt<=12:
            freq12_temp+=1
        else:
            freq18_temp+=1
        #print t[0],t[1]
    freq.append((mon,freq6_temp,freq12_temp,freq18_temp))
data=[[] for i in range(3)] 
xLabel=[]  
for tp in freq:
    total=tp[1]+tp[2]+tp[3]
    print total
    xLabel.append(tp[0])
    data[0].append(tp[1]*100.0/total)
    data[1].append(tp[2]*100.0/total)
    data[2].append(tp[3]*100.0/total)


tick_pos = [i for i in range(len(months))]

print data[0][0]
print data[1][0]
print data[2][0]
for d in data:
    print d
X = range(len(months))
A_B=[i+j for i,j in zip(data[0],data[1])]
print A_B
bar_width = 1
fig=plt.figure(1)
plt.bar(X, data[0], color = 'b',width=0.4,alpha=0.9,edgecolor='white',label='count 1~6' )
plt.bar(X, data[1], color = 'r',width=0.4,alpha=0.9,edgecolor='white', bottom = data[0],label='count 6~12' )
plt.bar(X, data[2], color = 'g',width=0.4,alpha=0.9, edgecolor='white',bottom = A_B,label='count 13~18' )
plt.xticks(tick_pos, xLabel)
plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=1,ncol=3, mode="expand", borderaxespad=0.)
plt.ylabel("Percentage %")

# Let the borders of the graphic
plt.xlim([min(tick_pos)-0.2, max(tick_pos)+bar_width])
plt.ylim(-5, 110)
plt.gca().yaxis.grid(True)
# rotate axis labels
plt.setp(plt.gca().get_xticklabels(), rotation=45, horizontalalignment='right')
fig.savefig('F:/workspace/git/Graph-MP/outputs/mesonetPlots/multi_CaseStudy/CP/2/All_months_percentage_bar_plot_new.png')    
plt.show()
'''fig=plt.figure(1)
    bins=[i+1 for i in range(18)]
    plt.hist(freq,bins=bins)
    axes = plt.gca()
    
    axes.set_ylim([0,10])
    axes.set_xlim([0,18])
    plt.title(mon+" Change pattern daily count frequency histogram")
    plt.xlabel("count 1~18")
    plt.ylabel("Frequency")
    plt.grid()
    fig = plt.gcf()
    #plt.show()  
    fig.savefig('F:/workspace/git/Graph-MP/outputs/mesonetPlots/multi_CaseStudy/CP/2/'+mon+'-Frequency histogram_.png')  
    plt.close()
    print "\n\n" '''
    


""" 
for mon in months:
    fileName=mon+"_multiGraphMP_TopK_result-CP_baseMeanDiff_20_s_2_wMax_18_filter_TIncld_0.7_Top.txt"
    date_patt = defaultdict(list)
    with open(fileName,"r") as f:        
        for line in f.readlines():
            terms=line.strip().split()
            topK=terms[0]
            date=terms[3]
            start=5*int(terms[4].split(",")[0])
            end=5*int(terms[3].split(",")[0])
            time="%d:%02d"%(start / 60, start % 60)+"~"+"%d:%02d"%(end / 60, end % 60)
            date_patt[date].append(topK+"->"+time)
    d = dict((k, tuple(v)) for k, v in date_patt.iteritems())
#     for k,v in date_patt: 
#         print k,v
    date_tuple=[]
    for k in d.keys():
        date_tuple.append((k,len(d[k]),",".join(d[k])))
        #print k,len(d[k]),d[k]
     
    date_tuple.sort(cmp=None, key=itemgetter(1), reverse=True)
    freq=[]
    for t in date_tuple:
        freq.append(int(t[1]))
        print t[0],t[1]
    fig=plt.figure(1)
    bins=[i+1 for i in range(18)]
    plt.hist(freq,bins=bins)
    axes = plt.gca()
     
    axes.set_ylim([0,10])
    axes.set_xlim([0,18])
    plt.title(mon+" Change pattern daily count frequency histogram")
    plt.xlabel("count 1~18")
    plt.ylabel("Frequency")
    plt.grid()
    fig = plt.gcf()
    #plt.show()  
    fig.savefig('F:/workspace/git/Graph-MP/outputs/mesonetPlots/multi_CaseStudy/CP/2/'+mon+'-Frequency histogram_.png')  
    plt.close()
    print "\n\n"  
"""      