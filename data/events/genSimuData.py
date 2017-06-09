import math,random

stations=[]
for idx,i in enumerate(range(20,181,40)):
    stations.append((idx,0,i))
print stations

tmcs=[]
tmc_id=0
radius=[4,9,14]

for sta in stations:
    circle_x=sta[2]
    circle_y=sta[1]
    
    for circle_r in radius:
        for j in range(10):
            # random angle
            alpha = 2 * math.pi * random.random()
            # random radius
            r = circle_r
            # calculating coordinates
            x = r * math.cos(alpha) + circle_x
            y = r * math.sin(alpha) + circle_y
            print circle_x,circle_y,x,y,math.hypot(x - circle_x, y - circle_y)
            tmcs.append((tmc_id,x,y))
            tmc_id+=1

print tmcs

