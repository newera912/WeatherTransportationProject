Hi All,
    Sorry for the late notice getting back to you. First, my website is here: http://operations.nysmesonet.org/~nbassill/ and my climate website is here: http://operations.nysmesonet.org/~nbassill/live/climate/index.php . Maybe you'll find them useful. 

I'm attaching a few files, including a .tar file of our station meta data. Those files should be pretty self-explanatory.  The coefficients.tar file has all of the data to calculate my "expected" climate normals, including high, low, average, std of high and low, and year-to-date precip. Each of the text files there has 365 values - one for each day.  I hope the files should be labeled in a self-explanatory way, but let me know if they're not. The predictor order goes: (1) intercept, (2), lat, (3) lon, (4) distance to water. The water.dat file is a file containing gridded lat, lon, and water body data (a flag, with 1 = water, and 0=land). It's tough to explain in words how I calculate the means, so I think it would be best if I just copied the segment of (NCL) code below, and hopefully that will be enough. However, I'd be happy to answer more questions. NCL is base 0. Basically it first reads in all of those text files and puts them in one data array. Then it reads in the meta data for whatever station you're currently using. Then, based on the site's meta data, it calculates the distance to water. After that is done, it calculates the yearly values for all of the variables above. Precipitation doesn't use water as a predictor right now for reasons that are not worth explaining. 
    fasc = "../*txt"   ; a unique identifier for file
    DASC = "./"         ; output dir
    FASC = "BIG.txt"    ; output file name

    system ("/bin/rm -f "+DASC+FASC)   ; rm any pre-existing file
    ncol = stringtoint(systemfunc("ls -1 ../*txt | wc -l"))

; Use UNIX "cat" to concatenate the files into one file.
    system ("cat "+fasc+" > "+DASC+FASC)

; You can now read the file via "asciiread".

    data  = asciiread(DASC+FASC,(/ncol,365/),"float")
    system ("/bin/rm -f "+DASC+FASC)   ; rm any pre-existing file

; meta files with info

meta = asciiread("station.ascii", -1, "string")
metadata = stringtofloat(meta(2:4))


; read the water data, and calculate distance from site

waterfile  = "water.dat"
waterdata = asciiread(waterfile,-1,"string")
delim = ","

waterlats    =  tofloat(str_get_field(waterdata,3,delim))
waterlons    =  tofloat(str_get_field(waterdata,4,delim))
wateryes     =  tofloat(str_get_field(waterdata,5,delim))

distance =  new((/dimsizes(waterlats)/),float)
distance = 10000
do i = 0,dimsizes(waterlats)-1
distance(i) = gc_latlon(metadata(0),metadata(1),waterlats(i),waterlons(i),2,4)
distance(i) = distance(i)*wateryes(i)
end do
distance@_FillValue = 0
print(min(distance))

;;;;;;;;;Here's the order of data: sdtmax(0-4),sdtmin(5-9),tavg(10-14),tmax(15-19),tmin(20-24),ytdp(25-28/9)

temps=new((/8,365/), "float")
temps(0,:)=data(15,:)+metadata(2)*data(16,:)+metadata(0)*data(17,:)+metadata(1)*data(18,:)+min(distance)*data(19,:)      ; tmax
temps(1,:)=temps(0,:)+(data(0,:)+metadata(2)*data(1,:)+metadata(0)*data(2,:)+metadata(1)*data(3,:))+min(distance)*data(4,:)   ; tmax+std
temps(2,:)=temps(0,:)-(data(0,:)+metadata(2)*data(1,:)+metadata(0)*data(2,:)+metadata(1)*data(3,:))+min(distance)*data(4,:)   ; tmax-std
temps(3,:)=data(20,:)+metadata(2)*data(21,:)+metadata(0)*data(22,:)+metadata(1)*data(23,:)+min(distance)*data(24,:)      ; tmin
temps(4,:)=temps(3,:)+(data(5,:)+metadata(2)*data(6,:)+metadata(0)*data(7,:)+metadata(1)*data(8,:))+min(distance)*data(9,:)   ; tmin+std
temps(5,:)=temps(3,:)-(data(5,:)+metadata(2)*data(6,:)+metadata(0)*data(7,:)+metadata(1)*data(8,:))+min(distance)*data(9,:)   ; tmin-std
temps(6,:)=data(10,:)+metadata(2)*data(11,:)+metadata(0)*data(12,:)+metadata(1)*data(13,:)+min(distance)*data(14,:)        ; tavg
temps(7,:)=.1*(data(25,:)+metadata(2)*data(26,:)+metadata(0)*data(27,:)+metadata(1)*data(28,:))  ; precip 

