#coding=utf-8

from math import *

# input Lat_A 
# input Lng_A 
# input Lat_B  
# input Lng_B 
# output distance mile
def calcDistance(Lat_A, Lng_A, Lat_B, Lng_B):
    ra = 6378.140  
    rb = 6356.755  
    flatten = (ra - rb) / ra  # �������
    rad_lat_A = radians(Lat_A)
    rad_lng_A = radians(Lng_A)
    rad_lat_B = radians(Lat_B)
    rad_lng_B = radians(Lng_B)
    pA = atan(rb / ra * tan(rad_lat_A))
    pB = atan(rb / ra * tan(rad_lat_B))
    xx = acos(sin(pA) * sin(pB) + cos(pA) * cos(pB) * cos(rad_lng_A - rad_lng_B))
    c1 = (sin(xx) - xx) * (sin(pA) + sin(pB)) ** 2 / cos(xx / 2) ** 2
    c2 = (sin(xx) + xx) * (sin(pA) - sin(pB)) ** 2 / sin(xx / 2) ** 2
    dr = flatten / 8 * (c1 - c2)
    distance = ra * (xx + dr)
    distance = 1.609344 * distance
    return distance

# Lat_A=32.060255; Lng_A=118.796877 #  
# Lat_B=39.904211; Lng_B=116.407395 # 
# distance=calcDistance(Lat_A,Lng_A,Lat_B,Lng_B)
# print('(Lat_A, Lng_A)=({0:10.3f},{1:10.3f})'.format(Lat_A,Lng_A))
# print('(Lat_B, Lng_B)=({0:10.3f},{1:10.3f})'.format(Lat_B,Lng_B))
# print('Distance={0:10.3f} mile'.format(distance))