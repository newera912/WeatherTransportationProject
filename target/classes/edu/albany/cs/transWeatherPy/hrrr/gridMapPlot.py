from pygmaps_ng import *
import os


dataFile=open('20160116.23.hrrr.wrfsfcf00.NYS_lats_lons_9Vars.247_257.txt','r')
#data=np.load('20160116.23.hrrr.wrfsfcf00.NYS_lats_lons_9Vars.247_257.txt')
N=493
lats=[]
lons=[]
for i,line in enumerate(dataFile.readlines()):
    if i<247:
        lats.extend(map(float,line.split()))
    elif i<=N:
        lons.extend(map(float,line.split()))
    else:
        print i
        break

print len(lons),len(lats)


dlat=[]
dlong=[]
mymap = Map()
app1 = App('test1',title="Grid positions")
mymap.apps.append(app1)

dataset1 = DataSet('data1', title="Show Stations" ,key_color='FF0088')
app1.datasets.append(dataset1)

for j in range(len(lats)):

    pt=[]
    pt.append(lats[j])
    pt.append(lons[j])


    #print "Station:"+sta[0]+"\nMin Temp:"+sta[1]+"\nMax Temp:"+sta[2]
    dataset1.add_marker(pt ,title="Show Grid",color="000000",text="Point#:"+str(j))
mymap.build_page(center=pt,zoom=14,outfile="grid_NYS_region_data_point_plot.html")
