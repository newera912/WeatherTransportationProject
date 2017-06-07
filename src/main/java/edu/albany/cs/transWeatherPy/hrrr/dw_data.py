import os
import urllib2
import urllib
from datetime import datetime, timedelta
import os,time
from numpy import mod

def dw_hrrr(request_date, outpath='./'):
    URL_list = []
    t0=time.time()
    year = request_date.year
    month = request_date.month
    day = request_date.day

    # Build the URL string we want to download. One for each field, hour, and forecast
    URL = 'https://api.mesowest.utah.edu/archive/%04d%02d%02d/' % (year, month, day)


    # Create a new directory for each field to keep things organized
    outdir = '%shrrr/' % (
        outpath)  # the path we want to save the file. Put it in a directory named by date
    if not os.path.exists(outdir):
        os.makedirs(outdir)

    FileName = 'models.tar.gz'

    # Download and save the file
    print 'Downloading:', URL + FileName
    url=URL + FileName
    print 'Saved:', outdir +'%04d%02d%02d_' % (year, month, day)+ FileName

    file_name = outdir +'%04d%02d%02d_' % (year, month, day)+ FileName
    u = urllib2.urlopen(url)
    f = open(file_name, 'wb')
    meta = u.info()
    file_size = int(meta.getheaders("Content-Length")[0])
    print "Downloading: %s Bytes: %s" % (file_name, file_size)

    file_size_dl = 0
    block_sz = 500*8192
    while True:
        buffer = u.read(block_sz)
        if not buffer:
            break

        file_size_dl += len(buffer)
        f.write(buffer)
        status = r"%10d  [%3.2f%%]" % (file_size_dl, file_size_dl * 100. / file_size)
        status = status + chr(8) * (len(status) + 1)
        print status,
        print '\n',

    f.close()
    os.system("tar -zxvf "+file_name+" --wildcards --no-anchored 'hrrr.t*z.wrfprsf00.grib2'")
    print time.time() - t0,'sec....'

if __name__ == '__main__':

    ## Create a range of dates to iterate over
    base = datetime(2016, 1, 11)
    numdays = 31
    date_list = [base + timedelta(days=x) for x in range(0, numdays)]

    for get_day in date_list:
        print get_day
        ## download only surface fields for analysis hours
        URLs = dw_hrrr(get_day)