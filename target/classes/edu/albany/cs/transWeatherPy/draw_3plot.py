import matplotlib.pyplot as plt
import time
import os
import sys
import numpy as np
import collections

def draw_3d_plot(var_pre_rec,signal_level,noise_level):
    import numpy as np
    import matplotlib.pyplot as plt
    from mpl_toolkits.mplot3d import Axes3D
    from operator import itemgetter
    #signal_level=[0.2,0.4,0.6,0.8,1.0]
    #noise_level=[0.0,0.02,0.04,0.06,0.08]
    
    for n_level in noise_level:
        trueSub_pre=[[] for i in range(3)]
        trueSub_rec=[[] for i in range(3)]
        trueSub_f=[[] for i in range(3)]
        for terms in var_pre_rec:            
            if terms[0]==0.05:
                trueSub_pre[0].append(terms[3])
                trueSub_rec[0].append(terms[4])
                trueSub_f[0].append(terms[5])
            elif terms[0]==0.1 :
                trueSub_pre[1].append(terms[3])
                trueSub_rec[1].append(terms[4])
                trueSub_f[1].append(terms[5])
            elif terms[0]==0.15:
                trueSub_pre[2].append(terms[3])
                trueSub_rec[2].append(terms[4])
                trueSub_f[2].append(terms[5])
        #print trueSub_pre[0] 
        #print trueSub_pre[1]  
        #print trueSub_pre[2] 
    for i in range(3):    
        fig = plt.figure()
        ax = Axes3D(fig)
        X = signal_level
        Y = noise_level
        X, Y = np.meshgrid(X, Y)
        
        fig = plt.figure()
        ax = fig.gca(projection='3d')
        
        ax.plot_trisurf(X, Y, Z, cmap=cm.jet, linewidth=0.2)
        
        plt.show()
    
def draw_Fix_Noise(var_pre_rec,signal_level,noise_level):
    from operator import itemgetter
    #signal_level=[0.2,0.4,0.6,0.8,1.0]
    #noise_level=[0.0,0.02,0.04,0.06,0.08]
    
    for n_level in noise_level:
        trueSub_pre=[[] for i in range(3)]
        trueSub_rec=[[] for i in range(3)]
        trueSub_f=[[] for i in range(3)]
        for terms in var_pre_rec:            
            if terms[0]==0.05 and terms[1]==n_level:
                trueSub_pre[0].append((terms[2],terms[3]))
                trueSub_rec[0].append((terms[2],terms[4]))
                trueSub_f[0].append((terms[2],terms[5]))
            elif terms[0]==0.1 and terms[1]==n_level:
                trueSub_pre[1].append((terms[2],terms[3]))
                trueSub_rec[1].append((terms[2],terms[4]))
                trueSub_f[1].append((terms[2],terms[5]))
            elif terms[0]==0.15 and terms[1]==n_level:
                trueSub_pre[2].append((terms[2],terms[3]))
                trueSub_rec[2].append((terms[2],terms[4]))
                trueSub_f[2].append((terms[2],terms[5]))
        #print trueSub_pre[0] 
        #print trueSub_pre[1]  
        #print trueSub_pre[2] 
        pre_005=[]
        pre_010=[]
        pre_015=[]
        pre_005=[t[1] for t in sorted(trueSub_pre[0], key=itemgetter(0))]
        pre_010=[t[1] for t in sorted(trueSub_pre[1], key=itemgetter(0))]  
        pre_015=[t[1] for t in sorted(trueSub_pre[2], key=itemgetter(0))] 
        
        rec_005=[]
        rec_010=[]
        rec_015=[]
        
        rec_005=[t[1] for t in sorted(trueSub_rec[0], key=itemgetter(0))]
        rec_010=[t[1] for t in sorted(trueSub_rec[1], key=itemgetter(0))]  
        rec_015=[t[1] for t in sorted(trueSub_rec[2], key=itemgetter(0))] 
        
        f_005=[]
        f_010=[]
        f_015=[]
        
        f_005=[t[1] for t in sorted(trueSub_f[0], key=itemgetter(0))]
        f_010=[t[1] for t in sorted(trueSub_f[1], key=itemgetter(0))]  
        f_015=[t[1] for t in sorted(trueSub_f[2], key=itemgetter(0))]
        
        print 'N-tru:',trueSub_pre[0] 
        print 'N-pre:',pre_005,'\n',signal_level
        
        fig=plt.figure(1)
        plt.subplot(311)
        plt.plot(signal_level,  pre_005 , 'r*-',linewidth='2.0', markersize=8,label='pre') 
        plt.plot(signal_level,  rec_005 , 'bD-',linewidth='2.0', markersize=8,label='rec') 
        plt.plot(signal_level,  f_005 , 'g>-',linewidth='2.0', markersize=8,label='f-score')  
             
        plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
        #plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
        
        plt.ylim([0.0,1.1])
        plt.xlabel('Signal level')
        #plt.xlim([0.2,0.0])
        plt.legend(loc='best',fontsize=8)
        plt.title('Pre,Rec and f-score of true-Subgraph-ratio=0.05,Noise Level='+str(n_level))
        
        plt.subplot(312)
        plt.plot(signal_level,  pre_010 , 'r*-',linewidth='2.0', markersize=8,label='pre') 
        plt.plot(signal_level,  rec_010 , 'bD-',linewidth='2.0', markersize=8,label='rec')  
        plt.plot(signal_level,  f_010 , 'g>-',linewidth='2.0', markersize=8,label='f-score')       
        plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
        #plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
        
        plt.ylim([0.0,1.1])
        plt.xlabel('Signal level')
        #plt.xlim([0.2,0.0])
        plt.legend(loc='best',fontsize=8)
        plt.title('Pre,Rec and f-score of true-Subgraph-ratio=0.10,Noise Level='+str(n_level))
        
        plt.subplot(313)
        plt.plot(signal_level,  pre_015 , 'r*-',linewidth='2.0', markersize=8,label='pre') 
        plt.plot(signal_level,  rec_015 , 'bD-',linewidth='2.0', markersize=8,label='rec') 
        plt.plot(signal_level,  f_015 , 'g>-',linewidth='2.0', markersize=8,label='f-score')        
        plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
        #plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
        
        plt.ylim([0.0,1.1])
        plt.xlabel('Signal level')
        #plt.xlim([0.2,0.0])
        plt.legend(loc='best',fontsize=8)
        plt.title('Pre,Rec and f-score of true-Subgraph-ratio=0.15,Noise Level='+str(n_level))
        
        
        
        plt.tight_layout(pad=0.4, w_pad=0.5, h_pad=1.0)
        #plt.show()
        fig.savefig('F:/workspace/git/TranWeatherProject/outputs/Temprature_test_plots/Test2_STD_fixed_Noise_'+str(n_level)+'.png')
        plt.close()
          
def draw_Fix_Signal(var_pre_rec,signal_level,noise_level):
    from operator import itemgetter
    
    #signal_level=[0.2,0.4,0.6,0.8,1.0]
    #noise_level=[0.0,0.02,0.04,0.06,0.08]
    
    for s_level in signal_level:
        trueSub_pre=[[] for i in range(3)]
        trueSub_rec=[[] for i in range(3)]
        trueSub_f=[[] for i in range(3)]
        for terms in var_pre_rec:            
            if terms[0]==0.05 and terms[2]==s_level:
                trueSub_pre[0].append((terms[1],terms[3]))
                trueSub_rec[0].append((terms[1],terms[4]))
                trueSub_f[0].append((terms[1],terms[5]))
            elif terms[0]==0.1 and terms[2]==s_level:
                trueSub_pre[1].append((terms[1],terms[3]))
                trueSub_rec[1].append((terms[1],terms[4]))
                trueSub_f[1].append((terms[1],terms[5]))
            elif terms[0]==0.15 and terms[2]==s_level:
                trueSub_pre[2].append((terms[1],terms[3]))
                trueSub_rec[2].append((terms[1],terms[4]))
                trueSub_f[2].append((terms[1],terms[5]))
       
        pre_005=[]
        pre_010=[]
        pre_015=[]
        pre_005=[t[1] for t in sorted(trueSub_pre[0], key=itemgetter(0))]
        pre_010=[t[1] for t in sorted(trueSub_pre[1], key=itemgetter(0))]  
        pre_015=[t[1] for t in sorted(trueSub_pre[2], key=itemgetter(0))] 
        
        rec_005=[]
        rec_010=[]
        rec_015=[]
        
        rec_005=[t[1] for t in sorted(trueSub_rec[0], key=itemgetter(0))]
        rec_010=[t[1] for t in sorted(trueSub_rec[1], key=itemgetter(0))]  
        rec_015=[t[1] for t in sorted(trueSub_rec[2], key=itemgetter(0))] 
        
        f_005=[]
        f_010=[]
        f_015=[]
        
        f_005=[t[1] for t in sorted(trueSub_f[0], key=itemgetter(0))]
        f_010=[t[1] for t in sorted(trueSub_f[1], key=itemgetter(0))]  
        f_015=[t[1] for t in sorted(trueSub_f[2], key=itemgetter(0))]
        
        
        print 'S-tru:',trueSub_pre[0] 
        print 'S-pre:',pre_005,'\n'
        
        #print pre_010  
        #print pre_015
        fig=plt.figure(1)
        plt.subplot(311)
        plt.plot(noise_level,  pre_005 , 'r*-',linewidth='2.0', markersize=8,label='pre') 
        plt.plot(noise_level,  rec_005 , 'bD-',linewidth='2.0', markersize=8,label='rec') 
        plt.plot(noise_level,  f_005 , 'g>-',linewidth='2.0', markersize=8,label='f-score')  
             
        plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
        #plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
        
        plt.ylim([0.0,1.1])
        plt.xlabel('Noise level')
        #plt.xlim([0.2,0.0])
        plt.legend(loc='best',fontsize=8)
        plt.title('Pre,Rec and f-score of true-Subgraph-ratio=0.05,Signal Level='+str(s_level))
        
        plt.subplot(312)
        plt.plot(noise_level,  pre_010 , 'r*-',linewidth='2.0', markersize=8,label='pre') 
        plt.plot(noise_level,  rec_010 , 'bD-',linewidth='2.0', markersize=8,label='rec')  
        plt.plot(noise_level,  f_010 , 'g>-',linewidth='2.0', markersize=8,label='f-score')       
        plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
        #plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
        
        plt.ylim([0.0,1.1])
        plt.xlabel('Noise level')
        #plt.xlim([0.2,0.0])
        plt.legend(loc='best',fontsize=8)
        plt.title('Pre,Rec and f-score of true-Subgraph-ratio=0.10,Signal Level='+str(s_level))
        
        plt.subplot(313)
        plt.plot(noise_level,  pre_015 , 'r*-',linewidth='2.0', markersize=8,label='pre') 
        plt.plot(noise_level,  rec_015 , 'bD-',linewidth='2.0', markersize=8,label='rec') 
        plt.plot(noise_level,  f_015 , 'g>-',linewidth='2.0', markersize=8,label='f-score')        
        plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
        #plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
        
        plt.ylim([0.0,1.1])
        plt.xlabel('Noise level')
        #plt.xlim([0.2,0.0])
        plt.legend(loc='best',fontsize=8)
        plt.title('Pre,Rec and f-score of true-Subgraph-ratio=0.15,Signal Level='+str(s_level))
        
        
        
        plt.tight_layout(pad=0.4, w_pad=0.5, h_pad=1.0)
        #plt.show()
        fig.savefig('F:/workspace/git/TranWeatherProject/outputs/Temprature_test_plots/Test2_STD_fixed_signal_'+str(s_level)+'.png')
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
    
    fileName="F:/workspace/git/TranWeatherProject/outputs/ADPM_Noise_Signal_Test_result_STD_Signal2.txt"
    noise_level=[0.0,0.02,0.04,0.06,0.08];
    signal_level=[1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0];
    
#     signal_level=[0.2,0.4,0.6,0.8,1.0]
#     noise_level=[0.0,0.02,0.04,0.06,0.08]
#     fileName="F:/workspace/git/TranWeatherProject/outputs/ADPM_Noise_Signal_Test_result12.txt"
    var_pre_rec=[]
    X_Y_Z=[[],[],[]]
    for line in open(fileName,"r").readlines():
        terms=line.split()
        rfileName=terms[0].split("/")[-1]
        pre=float(terms[1])
        rec=float(terms[2])
        vars=rfileName.split("_")
        #print vars[2],vars[4],vars[6][:-4],pre,rec,f_score(pre, rec)
        var_pre_rec.append((float(vars[2]),float(vars[4]),float(vars[6][:-4]),pre,rec,f_score(pre, rec)))

    draw_Fix_Noise(var_pre_rec,signal_level,noise_level)    
    draw_Fix_Signal(var_pre_rec,signal_level,noise_level)
    
        
