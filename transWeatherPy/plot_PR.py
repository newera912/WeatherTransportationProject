# -*- coding: utf-8 -*-
"""
Created on Mon Nov 16 20:46:45 2015
B lmbda1 lambda2 G(S) F(S) C(S) M(S)
@author: Adil
"""
from operator import itemgetter
import time
import matplotlib.pyplot as mp

X=[0.05,0.1,0.15]
pre=[0.6923412611826315,0.7662682602921647,0.728406395838115]
rec=[1.0,1.0,1.0]


fig = mp.figure(1)
   
l1=mp.plot(X,pre,'bo-',label='Precision') 
l2=mp.plot(X,rec,'ro-',label='Recall')
lns = l1+l2
labs = [l.get_label() for l in lns]
fig.legend(lns, labs, loc=4)
mp.title('Subgraph Size - Precision Plot')    
mp.xlabel('Subgraph rate')
mp.axis([0, 0.2, 0.00, 1.10])
mp.grid(True)




mp.show()           