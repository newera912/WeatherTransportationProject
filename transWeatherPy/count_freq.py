
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
r=[]
with open("dump2","r") as f:
    for line in f.readlines():   
        print line     
#         terms=line.strip().split(",")
#         for i in range(int(terms[0]),int(terms[1])):
#             r.append(i)
# print len(r)