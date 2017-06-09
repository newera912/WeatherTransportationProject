
# week={}
# with open("dump","r") as f:
#     for line in f.readlines():
#         w=line.strip()
#         if week.has_key(w):
#             week[w]+=1
#         else:
#             week[w]=1
# for k,v in week.items():
#     print k,v
import matplotlib.pyplot as plt
import numpy as np

idx=0
r=[]
with open("dump2","r") as f:
    for line in f.readlines():   
        if(idx>100):
            break  
        idx+=1
        terms=line.strip().split(",")
        for i in range(int(terms[0]),int(terms[1])):
            r.append(i)
print len(r)
bins=[i for i in range(0,288,12)]
label=[(str(i)+"\n"+str(i*5/60)+"h") for i in range(0,288+1,12)]

numpy_hist = plt.figure()

plt.hist(r, bins=bins)
plt.xticks(np.arange(0, 288+2, 12.0),label)
plt.title('Sep. Top 100 result Time region Histogram')
plt.show()