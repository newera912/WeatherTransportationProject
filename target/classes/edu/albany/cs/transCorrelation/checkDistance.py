import numpy as np
def count_scores(scores,high):
    return len([x for x in scores if x <= high])
r=0     
with open("SimuPICresultPairDist.txt","r") as ff:
    for line in ff.readlines():
        terms=line.strip().split()
        scores=map(float,terms[1:])
        
        if terms[0]==r:
            continue
        else:
            print len(scores),terms[0]
            r=terms[0]
        temp=0
        for x in xrange(5, 46,5):
            count=count_scores(scores, x)
            print "<",x,count,count-temp
            temp=count
        #raw_input("Press Enter to continue...")    
        