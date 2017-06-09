import matplotlib.pyplot as plt
import numpy as np
 
w_station_data=[]
w_slot_data=[]
  
with open("F:/workspace/git/Graph-MP/data/events/stat_w.txt") as f:
    for line in f.readlines():
        terms=line.strip().split()
        w_station_data.append(int(terms[0]))
        w_slot_data.append(int(terms[2]))
  
fig=plt.figure()
label=[str(i*5/60)+"h" for i in range(0,288,12)]
plt.hist(w_slot_data, bins=range(0, 288, 12))  # plt.hist passes it's arguments to np.histogram
plt.title("Weather changing pattern time distribution histogram")
plt.xlabel("Time Hours")
plt.ylabel("Changing Pattern Count")
plt.xticks(np.arange(0, 288, 12.0),label)
plt.show()
fig.savefig('weatherSlot.png', dpi=300,bbox_inches='tight')
fig.clf() 
 
fig=plt.figure()
label=[str(i) for i in range(10)]
plt.hist(w_station_data, bins=range(10))  # plt.hist passes it's arguments to np.histogram
plt.title("Weather station event count histogram")
plt.xlabel("Station ID")
plt.ylabel("Changing Pattern Count")
plt.xticks(np.arange(10),label)
plt.show()
fig.savefig('wStations.png', dpi=300,bbox_inches='tight')
fig.clf() 
 
te_data=[]
te_slot_data=[]
 
with open("F:/workspace/git/Graph-MP/data/events/Estat_tmc.txt") as f:
    for line in f.readlines():
        terms=line.strip().split()
        te_data.append(int(terms[0]))
        te_slot_data.append(int(terms[2]))
 
fig=plt.figure()
label=[str(i*5/60)+"h" for i in range(0,288,12)]
plt.hist(te_slot_data, bins=range(0, 288, 12))  # plt.hist passes it's arguments to np.histogram
plt.title("I90 West To East traffic event time distribution histogram")
plt.xlabel("Time Hours")
plt.ylabel("Changing Pattern Count")
plt.xticks(np.arange(0, 288, 12.0),label)
plt.show()
fig.savefig('teSlot.png', dpi=300,bbox_inches='tight')
fig.clf() 
 
 
fig=plt.figure()
label=[str(i) for i in range(0,54,5)]
plt.hist(te_data, bins=range(54))  # plt.hist passes it's arguments to np.histogram
plt.title("I90 West to East TMC event count histogram")
plt.xlabel("TMC ID")
plt.ylabel("Changing Pattern Count")
plt.xticks(np.arange(0, 54,5),label)
plt.show()
fig.savefig('teTMC.png', dpi=300,bbox_inches='tight')
fig.clf() 
 
tw_data=[]
tw_slot_data=[]
 
with open("F:/workspace/git/Graph-MP/data/events/Wstat_tmc.txt") as f:
    for line in f.readlines():
        terms=line.strip().split()
        tw_data.append(int(terms[0]))
        tw_slot_data.append(int(terms[2]))
 
fig=plt.figure()
label=[str(i*5/60)+"h" for i in range(0,288,12)]
plt.hist(tw_slot_data, bins=range(0, 288, 12))  # plt.hist passes it's arguments to np.histogram
plt.title("West To East I90 traffic changing pattern time distribution histogram")
plt.xlabel("Time Hours")
plt.ylabel("Changing Pattern Count")
plt.xticks(np.arange(0, 288, 12.0),label)
plt.show()
fig.savefig('twSlot.png', dpi=300,bbox_inches='tight')
fig.clf() 
 
 
fig=plt.figure()
label=[str(i) for i in range(0,52,5)]
plt.hist(tw_data, bins=range(52))  # plt.hist passes it's arguments to np.histogram
plt.title("I90 East to West related TMC event count histogram")
plt.xlabel("TMC ID")
plt.ylabel("Changing Pattern Count")
plt.xticks(np.arange(0, 52,5),label)
plt.show()
fig.savefig('twTMC.png', dpi=300,bbox_inches='tight')
fig.clf() 