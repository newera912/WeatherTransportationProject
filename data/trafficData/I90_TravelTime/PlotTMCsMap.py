from pygmaps_ng import *
import os


dataFile=open('TMCsCenterLatLon.txt','r')

lat_lon=[]
for i,line in enumerate(dataFile.readlines()):
    line=line.strip().split()
    lat_lon.append((line[0],float(line[1]),float(line[2])))    



dlat=[]
dlong=[]
mymap = Map()
app1 = App('Show',title="TMC positions")
mymap.apps.append(app1)

dataset1 = DataSet('TMC Data', title="Show TMCs" ,key_color='FF0088')
app1.datasets.append(dataset1)

for j in range(len(lat_lon)):
    pt=[]
    pt.append(lat_lon[j][1])
    pt.append(lat_lon[j][2])

    dataset1.add_marker(pt ,title="Show TMC",color="000000",text="TMC:"+str(lat_lon[j][0]))
mymap.build_page(center=pt,zoom=14,outfile="TMC_plot.html")
