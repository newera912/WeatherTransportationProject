import matplotlib.pyplot as plt
def draw(X,pre,rec,f1,resultFile):
    from operator import itemgetter
    
 
    fig=plt.figure(1)
    
    plt.plot(X,pre, 'r*-',linewidth='2.0', markersize=8,label='pre') 
    plt.plot(X,rec , 'bD-',linewidth='2.0', markersize=8,label='rec') 
    plt.plot(X,f1 , 'g>-',linewidth='2.0', markersize=8,label='f-score')  
    #plt.axvline(x=100, ymin=0.0, ymax=1.0) 
      
    plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
    #plt.plot([0.2,0.1,0.0],[0.5,0.5,0.5])
    
    plt.ylim([0.0,1.1])
    plt.xlabel('maximum win_size \in {12,18,24,30,36}')
    #plt.xlim([0.2,0.0])
    plt.legend(loc='best',fontsize=8)
    plt.title('Pre,Rec and f-score ['+fileName.split("/")[-1].split(".")[0].split("-")[1]+']')
    
    
    
    
    plt.tight_layout(pad=0.4, w_pad=0.5, h_pad=1.0)
    plt.show()
    fig.savefig(resultFile+'.png')
    plt.close()
                  
root="F:/workspace/git/Graph-MP/outputs/mesonetPlots/multi_CaseStudy/CP/"
#PRF1_result-CP_3_baseMeanDiff_20_s_3_wMax_36_filter_TIncld_0.7.txt
#multi_TopK_result-CP_baseMeanDiff_20_s_2_wMax_12_filter_TIncld_0.7.txt
methods=["PRF1_result-CP_baseMeanDiff_20"]#,
#methods=["PRF1_result-CP_3_baseMeanDiff_20"]
for i,m in enumerate(methods):
    print m
    for s in [2,3]:
        pre=[]
        rec=[]
        f1=[]
        print s
        outfile=root+str(s)+"/multi_"+m+"_s_"+str(s)+"_Plot2.png"
        for mwin in [12,18,24,30,36]:
            fileName=root+str(s)+"/prf1/multi_"+m+"_s_"+str(s)+"_wMax_"+str(mwin)+"_filter_TIncld_0.7.txt"
            with open(fileName,"r") as f:
                for line in f.readlines():
                    terms=line.split()
                    if terms[0]=="105":
                        print mwin,s,terms[1],terms[2],terms[3]
                        pre.append(terms[1])
                        rec.append(terms[2])
                        f1.append(terms[3])
        draw([12,18,24,30,36], pre, rec, f1, outfile)