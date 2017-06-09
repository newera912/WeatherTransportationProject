import time
import numpy as np
import matplotlib.pyplot as plt

method="RIPLEYK"
with open("Simu"+method+"resultNewScore.txt","r") as ff:
    for line in ff.readlines():
        terms=line.strip().split()
        dataRadius=int(terms[0])
        testingRadius=int(terms[1])
        testScore=float(terms[2])
        scores=map(float,terms[3:])
        print line
        print len(scores),scores
        print np.min(scores),np.max(scores)
        bandwith=int((np.max(scores)-np.min(scores))/100.0)
        if bandwith==0:
            bandwith=1
        fig=plt.figure()
        plt.hist(scores, bins=range(int(np.min(scores)), int(np.max(scores)) + bandwith, bandwith))  # plt.hist passes it's arguments to np.histogram
        plt.title(method+" Histogram with Data true Radius=" + str(dataRadius)+" Testing Radius="+str(testingRadius))
        plt.axvline(testScore, color='r', linestyle='dashed', linewidth=2)
        #plt.show()
        fig.savefig('./histogramPlot/'+method+'_dRadius_'+str(dataRadius)+"_tRadius_"+str(testingRadius)+'.png',dpi=300)
        plt.close()