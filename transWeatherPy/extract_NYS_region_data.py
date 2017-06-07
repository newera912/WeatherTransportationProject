import os
import urllib2
import urllib
from datetime import datetime, timedelta
import os,time
from numpy import mod
import pygrib

def ext_NYS_region():
    ### NYS boundry :-79.76, 40.48, -71.79, 45.02
    ### from http://boundingbox.klokantech.com/

    lat1=40.48
    lat2 = 45.92
    lon1=-79.76
    lon2=-71.79

    ### Model level pressure in millibars
    mb=0
    base = datetime(2016, 1, 11)
    numdays = 31
    date_list = [base + timedelta(days=x) for x in range(0, numdays)]
    for get_day in date_list[:1]:

        year = get_day.year
        month = get_day.month
        day = get_day.day
        date="%04d%02d%02d"%(year,month,day)
        file_path="/media/wolf/disk2/noaa_HRRR_Rawdata/"+str(date)+"/models/hrrr/"
        print file_path
        for file in os.listdir(file_path)[:1]:
            print file_path,file
            hour=file[6:8]
            fileName=file_path+file
            grbs = pygrib.open(fileName)
            subFile = open(file_path+date +"_"+hour+"_hrrr_wrfprsf00.grb2", "a+")
            # var_nameList=[]
            # for grb in grbs:
            #
            #     var_nameList.append(str(grb).split(":")[1])
            # #for var in list(set(var_nameList)):
            #    print var
            ### Extract Temperature

            ### temperature level=500
            grbs1=grbs.select(name="Temperature",level=mb)
            grbs2=grbs.select(name="U component of wind",level=mb)
            grbs3=grbs.select(name="V component of wind",level=mb)
            # print len(grbs1)
            # print len(grbs2),len(grbs3)
            for grb in grbs2:
            #     for key in grb.keys():
            #        print key
                print grb
            grb=grbs.readline()
            lats, lons = grb.latlons()
            print lats.shape, lats.min(), lats.max(), lons.shape, lons.min(), lons.max()
            #     sub_data=grb.data(lat1=lat1,lat2=lat2,lon1=lon1,lon2=lon2)
            #     print sub_data[0],sub_data[1],sub_data[2]
            #     grb['data']=sub_data
            #     msg=grb.tostring()
            #     #data, lats.lons = grbs
            #     subFile.write(msg)
            # subFile.close()


if __name__ == '__main__':
    ext_NYS_region()