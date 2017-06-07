output=open("F:/workspace/git/Graph-MP/data/mesonet_data/test_4.txt","w")
data=[[] for i in range(10)]
sta=[1]
subSignal=[0,0,1,0,3,0,3,5,3,0,0,5,0,0]
t_range=[72,85]
for s in range(10):
    for i in range(288):
        data[s].append(s+11.0)
    signal=10.0/(85-72)
    temp=0.0
    if s in sta:
        for t in range(t_range[0],t_range[1]+1):
            temp+=signal
            data[s][t]+=temp
        for i,t in enumerate(range(t_range[0],t_range[1]+1)):
            
            data[s][t]-=subSignal[i]
        for t in range(t_range[1]+1,len(data[s])):
            data[s][t]+=temp
    output.write(str(s)+" "+" ".join(map(str,data[s]))+" 100.0 100.0"+"\n")
    print data[s][70:90]

# output=open("F:/workspace/git/Graph-MP/data/mesonet_data/test_3.txt","w")
# data=[[] for i in range(10)]
# sta=[1]
# t_range=[72,85]
# for s in range(10):
#     for i in range(288):
#         data[s].append(s+11.0)
#     signal=10.0/(85-72)
#     temp=0.0
#     if s in sta:
#         for t in range(t_range[0],t_range[1]+1):
#             temp+=signal
#             data[s][t]+=temp
#         for t in range(t_range[1]+1,len(data[s])):
#             data[s][t]+=temp
#     output.write(str(s)+" "+" ".join(map(str,data[s]))+" 100.0 100.0"+"\n")
#     print data[s][70:90]
# output=open("F:/workspace/git/Graph-MP/data/mesonet_data/test_1.txt","w")
# data=[[] for i in range(10)]
# sta=[1]
# t_range=[72,85]
# for s in range(10):
#     for i in range(288):
#         data[s].append(s+11.0)
#     signal=10.0/(85-72)
#     temp=0.0
#     if s in sta:
#         for t in range(t_range[0],t_range[1]+1):
#             temp+=signal
#             data[s][t]-=temp
#         for t in range(t_range[1]+1,len(data[s])):
#             data[s][t]-=temp
#     output.write(str(s)+" "+" ".join(map(str,data[s]))+" 100.0 100.0"+"\n")
#     print data[s][70:90]


"""--------------------------------------------------"""
# output=open("F:/workspace/git/Graph-MP/data/mesonet_data/test_2.txt","w")
# data=[[] for i in range(10)]
# sta=[1]
# t_range=[67,85]
# for s in range(10):
#     for i in range(288):
#         data[s].append(s+11.0)
#     signal=10.0/(85-72)
#     
#     if s in sta:
#         temp=0.0
#         last_point=0.0
#         for t in range(t_range[0],t_range[0]+5):
#             temp+=signal
#             data[s][t]+=temp
#             last_point=data[s][t]
#         temp=0.0
#         tillEnd=0.0   
#         for t in range(t_range[0]+5,t_range[1]+1):
#             temp+=signal
#             data[s][t]=last_point-temp
#             tillEnd=last_point-temp
#         for t in range(t_range[1]+1,len(data[s])):
#             data[s][t]=tillEnd
#     output.write(str(s)+" "+" ".join(map(str,data[s]))+" 100.0 100.0"+"\n")
#     print data[s][67:90]