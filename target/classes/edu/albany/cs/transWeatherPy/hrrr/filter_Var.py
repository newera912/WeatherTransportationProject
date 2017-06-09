files =['20160116.23.hrrr.wrfprsf.NYS.grib2','20160116.23.hrrr.wrfsfcf00.NYS.grib2','hrrr.t00z.wrfprsf00.grib2','hrrr.t00z.wrfsfcf00.grib2','hrrr.t00z.wrfnatf00.grib2']  # example filename
for file in files[1:2]:
    grbs = pygrib.open(file)

    lat11 = 40.48
    lat22 = 45.92
    lon11 = -79.76
    lon22 = -71.79

    print "------------"+file+"--------------------------------------\n"
    # grb = grbs.read(10)[0] # read returns a list with the next N (N=1 in this case) messages.
    # print grb
    selected_var_list=['Temperature','Wind speed (gust)','Specific humidity','Visibility','Percent frozen precipitation','Total Precipitation','Precipitation rate','Snow cover','Snow depth']
    #selected_var_index=[601,587,607,584,612,614,613,604,605]
    data,lats,lons=grbs[1].data()
    (X,Y)=lats.shape
    output=open(".".join(file.split(".")[:-1])+"_lats_lons_9Vars."+str(X)+"_"+str(Y)+".txt","a+")
    print lats.shape

    #np.save(output,np.array(lats),fmt='%3.4f')
    #np.savetxt(output,np.array(lons),fmt='%3.4f')

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
        print(len(grb_l),var)
        grbs.select()
        for grb in grb_l:
            print grb
            data,lats, lons = grb.data()
            np.savetxt(output,np.array(data),fmt='%3.4f')
            print data.shape
            print lats.shape,lons.shape,lats.min(),lats.max(),lons.min(),lons.max()

    output.close()