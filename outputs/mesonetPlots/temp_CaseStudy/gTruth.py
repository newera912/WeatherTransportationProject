
with open("F:/workspace/git/Graph-MP/outputs/mesonetPlots/temp_CaseStudy/true_values.txt","r") as pf:
    for line in pf.readlines():
        terms=line.strip().split()
        stats=terms[1].replace("[","").replace("]","").replace(",","_")
        range=terms[2].replace("[","").replace("]","").replace(",","_")
        outFile=open("F:/workspace/git/Graph-MP/data/mesonet_data/caseStudyTempGT/"+terms[0]+".txt","a+")
        outFile.write((stats+" "+range+"\n"))
        outFile.close()
        