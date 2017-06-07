import numpy as np
from _elementtree import Element

def gen_AdjList(rows, cols):
    n = rows*cols
    
    ### save edge list
    adj_file=open('data/NYS_grid_adjList_247X257.txt','w')
    #adj_file=open('data/NYS_grid_Temparature_247X257.txt','w')
    edgeList=[]
    #print M
    for r in xrange(rows):
        for c in xrange(cols):
            i = r*cols + c
            # Two inner diagonals
            if c > 0:
                edgeList.append((i-1,i))
                edgeList.append((i,i-1))
                #M[i-1][i] = M[i][i-1] = 1
            # Two outer diagonals
            if r > 0:
                edgeList.append((i-cols,i))
                edgeList.append((i,i-cols))
                
                
                #M[i-cols][i] = M[i][i-cols] = 1
    edgeList.sort(key=lambda tup: tup[0])
    for (e1,e2) in edgeList:
        adj_file.write(str(e1)+' '+str(e2)+'\n')
    return edgeList

def getMatrixData():
    dataFile=open('F:/workspace/git/TranWeatherProject/data/20160116.23.hrrr.wrfsfcf00.NYS_lats_lons_9Vars.247_257.txt','r')
    
    N=493
    data=[]
    temp=[]
    for i,line in enumerate(dataFile.readlines()):
        
        if (i+1)%247==0:            
            temp.append((map(float,line.split())))
            print 'data:',i,len(temp)
            data.append(temp)
            temp=[]
            
        else:
            print 'temp',i
            temp.append((map(float,line.split())))
        
    print i,len(data),len(data[10])
    return data


def main():
    data=getMatrixData()
    resultFile=open('F:/workspace/git/TranWeatherProject/data/20160116.23.hrrr.wrfsfcf00.NYS_TempData.247_257.txt','w')
    idx=0
    for row in data[2]:
        for col in row:
            resultFile.write(str(idx)+' '+str(col)+'\n')
            idx+=1
    resultFile.close()       
            
            

if __name__ == '__main__':
    main()
    