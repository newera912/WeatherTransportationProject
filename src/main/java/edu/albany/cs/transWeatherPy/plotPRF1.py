import matplotlib.pyplot as plt
import time
import os
import sys
import numpy as np
import collections
          
def draw(X,pre,rec,f1,resultFile):
    from operator import itemgetter     
    fig=plt.figure(1)
    plt.plot(X[:20],pre[:20] , 'r*-',linewidth='2.0', markersize=8,label='pre') 
    plt.plot(X[:20],rec[:20] , 'bD-',linewidth='2.0', markersize=8,label='rec') 
    plt.plot(X[:20],f1[:20] , 'g>-',linewidth='2.0', markersize=8,label='f-score')  
    plt.axvline(x=105, ymin=0.0, ymax=1.0) 
    plt.axhline(y=pre[19])
    plt.axhline(y=rec[19]) 
    plt.axhline(y=f1[19])   
    plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
    #plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
    
    plt.ylim([0.0,1.1])
    plt.xlabel('K (Top K record)')
    #plt.xlim([0.2,0.0])
    plt.legend(loc='best',fontsize=8)
    plt.title('Pre,Rec and f-score ['+fileName.split("/")[-1].split(".")[0].split("-")[1]+']')
    
    
    
    
    plt.tight_layout(pad=0.4, w_pad=0.5, h_pad=1.0)
    plt.show()
    fig.savefig(resultFile+'.png')
    plt.close()
                      

def f_score(pre,rec):
    if pre==0.0 or rec==0.0:
        return 0.001
    else:
        return 2*(pre*rec)/(pre+rec)  
if __name__ == '__main__':
#     fileName="F:/workspace/git/TranWeatherProject/outputs/ADPM_Noise_Signal_Test_result_STD_Signal.txt"
#     noise_level=[0.0,0.02,0.04,0.06,0.08]
#     signal_level=[0.5,1.0,3.0,5.0,8.0,10.0,12.0]
    root="F:/workspace/git/Graph-MP/outputs/mesonetPlots/temp_CaseStudy/prf1/"
    for tdate in ["20160301"]:#,"20160302","20160308","20160309","20160312","20160313","20160324","20160325","20160328","20160405","20160412","20160419","20160421","20160514","20160529","20160621","20160628","20160813","20160911","20160922"]:
        fileName=root+tdate+"_APDM.txt"
        fileName="F:/workspace/git/Graph-MP/outputs/mesonetPlots/temp_CaseStudy/PRF1_result-CP_baseMeanDiff_20_s_2_wMax_24_filter_TIncld_0.7.txt"
    
#     signal_level=[0.2,0.4,0.6,0.8,1.0]
#     noise_level=[0.0,0.02,0.04,0.06,0.08]
#     fileName="F:/workspace/git/TranWeatherProject/outputs/ADPM_Noise_Signal_Test_result12.txt"
        var_pre_rec=[]
        X=[]
        pre=[]
        rec=[]
        f1=[]
        ln=0
        for line in open(fileName,"r").readlines():
            terms=line.split()
            
            print ln,terms
            ln+=1
            X.append(int(terms[0]))
            pre.append(float(terms[1]))
            rec.append(float(terms[2]))
            f1.append(float(terms[3]))
           
    
        draw(X,pre,rec,f1,root+fileName.split("/")[-1].split(".")[0])    
    
    
        
