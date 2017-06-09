import numpy as np
def mad_fun(a, axis=None):
    """
    Compute *Median Absolute Deviation* of an array along given axis.
    """

    # Median along given axis, but *keeping* the reduced axis so that
    # result can still broadcast against a.
    med = np.median(a, axis=axis, keepdims=True)
    mad = np.median(np.absolute(a - med), axis=axis)  # MAD along given axis

    return mad   

var_types=["temp","temp9","press","wind","windDir","windMax","rh","rad"]

root="F:/workspace/git/Graph-MP/outputs/mesonetPlots/"
var_median_mad=[]
for var_type in var_types:
    
    fileName=root+var_type+"_CaseStudy/CP/2/multiVAR_TopK_result-CP_baseMeanDiff_20_s_2_wMax_18_filter_TIncld_0.7.txt"
    scores=[]
    with open(fileName,"r") as fN:
        for line in fN.readlines():
            line=line.strip()
            terms=line.split()
            scores.append(float(terms[0]))
    print len(scores)
    meadian=np.median(scores)
    mad=mad_fun(scores)
    min=np.min(scores)
    max=np.max(scores)
    var_median_mad.append((var_type,meadian+2*mad,meadian,mad,min,max))
for terms in var_median_mad:
    print terms
       