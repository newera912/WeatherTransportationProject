import matplotlib.pyplot as plt
import numpy as np
from matplotlib.pyplot import xlabel, ylabel




xlabels=["Testing Radius","Testing Radius","Testing Radius"]
mt=["PIC","RIPLEY K-Func","PCF"]
for m,settings in enumerate(["PairCorrelation"]):#5,15,20]:
    method_num=3
    if m==2:
        method_num=2
    pValue=[[] for i in range(method_num)]    
    x_label=[]
    titles=[]
    #"0.5","1.0","2.0","3.0","5.0"]:#"0.5","1.0","2.0","3.0","5.0","10.0"]:
        
    methods=["SimuPICresult6","SimuRIPLEYKresult6","SimuPCFresult6"]
    for i,method in enumerate(methods):
        pValue[i]=[[] for j in range(9)]
        if i==2 and m==2:
            continue
        fileName="./"+method+".txt"
        xp=[]       
        x_label=[]
        with open(fileName,"r") as f:
            dRadius=""
            x_label=[]
            j=0
            for line in f.readlines():
                terms=line.strip().split()                
                x_label.append(int(terms[1]))
                titles.append(int(terms[0]))
                if dRadius=="":
                    dRadius=str(terms[0])                      
                    pValue[i][j].append(float(terms[2]))                
                else:
                    if dRadius==str(terms[0]):
                        pValue[i][j].append(float(terms[2]))
                    else:
                        j+=1
                        dRadius=str(terms[0])
                        print i,j,len(pValue[i]),dRadius,str(terms[0])
                        pValue[i][j].append(float(terms[2]))
                    
    print pValue
    x_label=sorted(list(set(x_label)))                    
    x_label=map(str, x_label)                
    
    titles=sorted(list(set(titles)))                    
    titles=map(str, titles)                
                    
        
    if m<2:    
        print titles[i],"0,1",len(pValue[0])
        x=[i for i in range(len(x_label))]   
        

        for i in range(len(pValue[0])): 
            fig=plt.figure()
            plt.plot(x, pValue[0][i],'rx-',linewidth='0.8', markersize=8,label=mt[0]) 
            plt.plot(x, pValue[1][i],'bo-',linewidth='0.8', markersize=8,label=mt[1])
            plt.plot(x, pValue[2][i],'g^-',linewidth='0.8', markersize=8,label=mt[2])
           
           
            plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,ncol=1, mode="expand", borderaxespad=0.)
            plt.xticks(x,x_label,fontsize=12)  
            plt.yticks(fontsize=12)    
            plt.ylim([0.0,1.1])
            plt.xlabel(xlabels[m],fontsize=12)
            plt.ylabel('P-Value',fontsize=12)
            plt.title("Data Real Radius = "+titles[i],fontsize=12)
            plt.legend(loc='best',fontsize=12)        
            plt.grid()
            #plt.plot(X, Avg, yerr=Min_Max, fmt='o',label='Average With Min. & Max values')
            
            
            #plt.show()
            fig.savefig('./'+settings+" "+titles[i]+'_200.png',dpi=300)
            plt.close()
   