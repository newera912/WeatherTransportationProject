import numpy as np
import random,math

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
    distance=[]
    origin=[0,0,0]
    for i in range(n):
        a=getPointsInSphere(R, origin)
        b=[R+50,R+50,R+50]
        while dist(b,[0,0,0])>=R:       
            #print a
            b=getPointsInSphere(r, a)
        
        #print r,i,a,b,dist(np.array(a), np.array(b))
        wEvents.append((0,a))
        tEvents.append((1,b))
        distance.append(dist(a,b))
       
    return wEvents,tEvents,distance
    
