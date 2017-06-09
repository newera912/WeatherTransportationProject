import pygrib
import os
import numpy as np

# file = 'hrrr.t00z.wrfnatf00.grib2' #example filename
#
# grbs = pygrib.open(file)
#
# #grb = grbs.read(10)[0] # read returns a list with the next N (N=1 in this case) messages.
# #print grb
#
# grb_l=grbs.select(name='Temperature')
# print len(grb_l)
# for grb in grb_l:
#     lats, lons = grb.latlons()
#     print grb
#
# print "--------------------------------------------------\n"
# file = 'hrrr.t00z.wrfprsf00.grib2'  # example filename
#
# grbs = pygrib.open(file)
#
# # grb = grbs.read(10)[0] # read returns a list with the next N (N=1 in this case) messages.
# # print grb
#
# grb_l = grbs.select(name='Temperature')
# print len(grb_l)
# for grb in grb_l:
#     #lats, lons = grb.latlons()
#     print grb

def gen_raw_data_extract_variables():
    #'01','02','03','04','05','06','07','08','09','10'
    days=['11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31']
    months=['01']    
    hours=['00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23']
        
    path='/media/wolf/disk2/noaa_HRRR_Rawdata/HRRR_DayBydata/2016'  
    outFolder='/home/wolf/workspace/TransWeatherProject/data/NYS_Raw_Data/'
    files =['20160116.23.hrrr.wrfprsf.NYS.grib2','20160116.23.hrrr.wrfsfcf00.NYS.grib2','hrrr.t00z.wrfprsf00.grib2','hrrr.t00z.wrfsfcf00.grib2','hrrr.t00z.wrfnatf00.grib2']  # example filename
    for month in months:    
        for day in days:
            subfolder=path+str(month)+str(day)+"/"
            for fileName in os.listdir(subfolder):
                hour=fileName.split(".")[1]
                grbs = pygrib.open(subfolder+fileName)
            
                lat11 = 40.48
                lat22 = 45.92
                lon11 = -79.76
                lon22 = -71.79
            
                print "------------"+fileName+"-------------\n"
                # grb = grbs.read(10)[0] # read returns a list with the next N (N=1 in this case) messages.
                # print grb
                selected_var_list=['Temperature','Wind speed (gust)','Specific humidity','Visibility','Percent frozen precipitation','Total Precipitation','Precipitation rate','Snow cover','Snow depth']
                #selected_var_index=[601,587,607,584,612,614,613,604,605]
                data,lats,lons=grbs[1].data()
                (X,Y)=lats.shape
                if not os.path.exists(outFolder+"2016"+str(month)+str(day)+"/"):
                    os.makedirs(outFolder+"2016"+str(month)+str(day)+"/")
                    
                output=open(outFolder+"2016"+str(month)+str(day)+"/2016-"+str(month)+"-"+str(day)+"-"+str(hour)+"-RawHRRRGridData.txt","a+")
                #print lats.shape
            
                np.savetxt(output,np.array(lats),fmt='%3.4f')
                np.savetxt(output,np.array(lons),fmt='%3.4f')
            
                ### write the lat_lon matrix
                for var in selected_var_list:
                    if var=='Temperature':
                        grb_l = grbs.select(name=var,level=0)
                    elif var=='Specific humidity':
                        try:
            
                            grb_l = grbs.select(name=var, typeOfLevel='heightAboveGround')
                        except:
                            print "666666666666"
                            grb_l = []
                            grb_l.append(grbs[607])
                    else:
                        grb_l = grbs.select(name=var)
                    #print(len(grb_l),var)
                    grbs.select()
                    for grb in grb_l:
                        print grb
                        data,lats, lons = grb.data()
                        np.savetxt(output,np.array(data),fmt='%3.4f')
                        #print data.shape
                        #print lats.shape,lons.shape,lats.min(),lats.max(),lons.min(),lons.max()
            
                output.close()
            #print 'Lats_X',lats[0][0],lats[0][1798],lats[1058][0],lats[1058][1798]
            #print 'Lons_X',lons[0][0],lons[0][1798],lons[1058][0],lons[1058][1798]

            # str_lon=0
            # end_lon=0
            # for i in range(len(lons[0])):
            #     if lons[0][i] <lon11:
            #         str_lon += 1
            #         #print count
            #     else:
            #         print 'Str_lon',lons[0][i], lons[0][i + 1]
            #         break
            # end_lon+=str_lon
            # for i in range(str_lon,len(lons[0])):
            #     if lons[0][i] <lon22:
            #         end_lon += 1
            #         print lons[0][i],lon22
            #     else:
            #         print 'End_lon',lons[0][i], lons[0][i + 1]
            #         break
            #
            # print 'NYS_lon',str_lon,end_lon
            # #print len(grb.data()),len(grb.data()[0]),len(grb.data()[1]),len(grb.data()[2])
            # sub_data,lats,lons = grb.data(lat1=lat11, lat2=lat22, lon1=lon11, lon2=lon22)
            # print sub_data.shape,lats.shape,lons.shape,lats.min(), lats.max(), lons.min(), lons.max()
            # count=0
            # temp=lons[0]
            # print temp
            # for i in range(len(lons)-1):
            #     if lons[i+1]>lons[i]:
            #         count+=1
            #         print count
            #     else:
            #         print lons[i],lons[i+1]
            #         break
            # print count



            #print sub_data.shape
            #print lats.shape,len(lats[0]),type(lats)
            #print "--",lats
            #print "--",lats[0],"\n==",grb.data()[0]
            # for i in range(len(grb.data())):
            #     for j in range(len(grb.data()[0])):
            #         print lats[i][j],lons[i][j],grb.data()[0][i][j],grb.data()[1][i][j],grb.data()[2][i][j]
        # for grb in grbs:
        #     lats, lons = grb.latlons()
        #     print lats.shape, lats.min(), lats.max(), lons.shape, lons.min(), lons.max()
        #     print grb.keys()
        #     print grb.data()
        #print inventory
        #for i,g in enumerate(gr):
          #print "#line"+str(i),g.typeOfLevel, g.level, g.name, g.validDate, g.analDate, g.forecastTime
        # line=grbs.readline()
        # while line!=None:
        #     print line
        #     line=grbs.readline()
    print "--------------------------------------------------\n"
if __name__ == '__main__':
    gen_raw_data_extract_variables()

