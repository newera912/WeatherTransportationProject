import numpy as np

def madF(data, axis=None):
    return np.mean(np.abs(data - np.mean(data, axis)), axis)
def round(x,a):    
    return np.round(10.0**a*x)/10.0**a  

types=["temp", "temp9", "press", "wind", "windDir", "windMax", "rh", "rad"]

#rad_CaseStudy/CP3/2/CP3_s_2_wMax_18_filter_TIncld_0.7.txt
for type in types:
    fileName=type+"_CaseStudy/CP3/2/CP3_s_2_wMax_18_filter_TIncld_0.7.txt"
    scores=[]
    with open(fileName,"r") as f:
        for i,line in enumerate(f.readlines()):
            score=float(line.strip().split()[0])
            if i>2999 or score<1.0:
                continue
            scores.append(score)
    median=round(np.median(scores),2)
    mad=round(madF(scores),2)
    min=round(np.min(scores),2)
    max=round(np.max(scores),2)
    print "{} {} | {} {] {} {}".foramt(type,median+2*mad,median,mad,min,max)
    
    