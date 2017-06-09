import json
from geojson_utils import centroid
import numpy as np

def getCenter(poly):
    lat=[]
    lon=[]
    for mulLine in poly:
        for line in mulLine:
            lon.append(line[0])
            lat.append(line[1])
    center_lat=(np.min(lat)+np.max(lat))/2.0
    center_lon=(np.min(lon)+np.max(lon))/2.0
    return center_lat,center_lon        
            
            
geoJson=open('I90East_TMCLatLon.txt')
data=json.load(geoJson)
tmc_geo=[]
for i,tmc in enumerate(data):
    #print i,tmc

    box_str = tmc['geojson']
    #print box_str
    #box = json.loads(box_str)
#     print len(box_str['coordinates']),box_str['coordinates']
#     print len(box_str['coordinates'][0]),box_str['coordinates'][0]
#     print len(box_str['coordinates'][0][0]),box_str['coordinates'][0][0]
    lat_lon=getCenter(box_str['coordinates'])
    tmc_geo.append((i,str(tmc['tmc']),lat_lon[0],lat_lon[1]))
    #print i,tmc['tmc'],lat_lon[0],lat_lon[1]

tmc_geo=sorted(tmc_geo, key=lambda tup: tup[3], reverse=True)

for info in tmc_geo:
    print ("%s %f %f"%(info[1],info[2],info[3]))
    #print i,centroid(box_str)
    #{"type": "Point", "coordinates": [5, 5]})