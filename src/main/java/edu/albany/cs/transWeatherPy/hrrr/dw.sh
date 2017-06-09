#!/bin/bash

# Brian Blaylock
# Untar hrrr files in the Horel archive for an entire month (or change the days for a set of days)
# and moves them to the directory the script is run in.

echo ''
set year = '2016'
set month = '01'
set day= '16'
foreach hour ( 00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20 21 22 23 )
   echo grabbing this day {$year}{$month}{$day}
   
   ## Add wget command here if you need to download the models.tar.gz file
   # wget https://api.mesowest.utah.edu/archive/{$year}{$month}{$day}/models.tar.gz .
   ## Then you need to adjust the location of the file you want to untar below in the tar command
   
   #untar just the hrrr model data we need for WPS (pressure fields)
   echo 'Skeeping the wrong message:'
   wgrib2 hrrr.t{$hour}z.wrfprsf00.grib2 -pdt | egrep -v "^600:" | wgrib2 -i hrrr.t{$hour}z.wrfprsf00.grib2 -grib hrrr.t{$hour}z.wrfprsf00_temp.grib2
   wgrib2 hrrr.t{$hour}z.wrfprsf00_temp.grib2 -small_grib -79.76:-71.79 40.48:45.92 {$year}{$month}{$day}.{$hour}.hrrr.wrfprsf.grib2
   rm hrrr.t{$hour}z.wrfprsf00_temp.grib2 
   
   ls
   echo ''
end

echo ''
echo 'completed'

    
