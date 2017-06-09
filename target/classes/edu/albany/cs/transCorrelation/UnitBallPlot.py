from mpl_toolkits.mplot3d import Axes3D
import matplotlib.pyplot as plt
import numpy as np
import random,math
from itertools import product, combinations

def cuberoot(x):
    if 0<=x: return x**(1./3.)
    return -(-x)**(1./3.)

def dist(x,y):
    x=np.array(x)
    y=np.array(y)   
    return np.sqrt(np.sum((x-y)**2))

def getPointsInSphere(R,origin):
    
    phi = random.uniform(0,2*np.pi)
    costheta = random.uniform(-1,1)
    u = random.uniform(0,1)
    
    theta = np.arccos( costheta )
    r = R * cuberoot( u )
    #now you have a (r, theta, phi) group which can be transformed to (x, y, z) in the usual way
    
    x = r * math.sin( theta) * math.cos( phi )
    y = r * math.sin( theta) * math.sin( phi )
    z = r * math.cos( theta )
    #print '>>',x,y,z
    return x+origin[0],y+origin[1],z+origin[2]
def getEvents(r,n):
    R=100.0   
    wEvents=[]
    tEvents=[]
    origin=[0,0,0]
    for i in range(n):
        a=getPointsInSphere(R, origin)
        b=[150,150,150]
        while dist(b,[0,0,0])>=100:       
            #print a
            b=getPointsInSphere(r, a)
        
        #print r,i,a,b,dist(np.array(a), np.array(b))
        wEvents.append(a)
        tEvents.append(b)
       
    return wEvents,tEvents
weatherEvent0,trafficEvent0=getEvents(1,500)
fig = plt.figure()
ax = fig.gca(projection='3d')
ax.set_aspect("equal")


# draw sphere
r=100
u, v = np.mgrid[0:np.pi:50j, 0:2.0*np.pi:50j]
x = r*np.cos(u)*np.sin(v)
y = r*np.sin(u)*np.sin(v)
z = r*np.cos(v)
ax.plot_wireframe(x, y, z, color="k", linewidth=0.05)

# draw a point
for wev in weatherEvent0:
    ax.scatter(wev[0], wev[1], wev[2], color="r", s=2)
for tev in trafficEvent0:
    ax.scatter(tev[0], tev[1], tev[2], color="b", s=2)

ax.set_xlim([-100,100])
ax.set_ylim([-100,100])
ax.set_zlim([-100,100])
ax.set_aspect("equal")
plt.tight_layout()


plt.show()