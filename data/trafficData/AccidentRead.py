import json
import numpy as np

geoJson=open('I90_accident.txt')

data=json.load(geoJson)
print data
tmc_geo=[]
for tmc in data:
    print tmc

    # box_str = tmc['geojson']
    #print box_str
    #box = json.loads(box_str)
#     print len(box_str['coordinates']),box_str['coordinates']
#     print len(box_str['coordinates'][0]),box_str['coordinates'][0]
#     print len(box_str['coordinates'][0][0]),box_str['coordinates'][0][0]

# 
# for info in tmc_geo:
#     print ("%s %f %f"%(info[1],info[2],info[3]))
    #print i,centroid(box_str)
    #{"type": "Point", "coordinates": [5, 5]})