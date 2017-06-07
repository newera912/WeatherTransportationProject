# import pygrib
# #hrrr.t00z.wrfprsf00.grib2
# #2016010100_hrrr_wrfprsf.grib2
# grbs = pygrib.open('hrrr.t16z.wrfprsf00.grib2')
# grb=grbs[2]
# print grb.values.shape
# print grbs[1].data()[0].shape
# print grbs[1].data()[1][0][0],grbs[1].data()[1][0][256],grbs[1].data()[1][246][0],grbs[1].data()[1][246][256]
# print grbs[1].data()[2][0][0],grbs[1].data()[2][0][246],grbs[1].data()[2][246][0],grbs[1].data()[2][246][256]
#
#
# # grbs = pygrib.open('hrrr.t00z.wrfprsf00.grib2')
# for grb in grbs:
#     print grb


import numpy as np
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