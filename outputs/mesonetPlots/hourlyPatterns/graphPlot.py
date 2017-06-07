import matplotlib.pyplot as plt
import networkx as nx
import os
G=nx.Graph()

posp=[[0,0.1],[1,0.0],[2,0.1],[4,0.1],[6,0.25],[7,0.1],[8,0.25],[10,0],[12,0.0],[14,0.1]]
for node in range(10):
    G.add_node(node,pos=(posp[node][0],posp[node][1]))
edges=[(0,1),(1,2),(2,3),(3,4),(3,5),(4,5),(4,6),(5,6),(5,7),(6,7),(7,8),(7,9),(8,9)]
abnode=[1,3,4]

months=["201603","201604","201605","201606","201607","201608","201609"]
for mon in months[:1]:    
    root="F:/workspace/git/Graph-MP/outputs/mesonetPlots/hourlyPatterns/"+mon+"/"
    outRoot="F:/workspace/git/Graph-MP/outputs/mesonetPlots/hourlyPatternsGraph/"+mon+"_png/"
    if os.path.exists(outRoot)==False:
        os.makedirs(outRoot)
    for name in os.listdir(root):
        with open(root+name,"r") as f:
            data=[]
            for line in f.readlines():
                data.append(map(int,line.strip().split()))
            for i,d in enumerate(data):
                
                color_map = []
                for node in G:
                    if d[node]==1:
                        color_map.append('red')
                    else: 
                        color_map.append('green')  
                G.add_edges_from(edges)
                fig=plt.figure(1) 
                print name.split('.')[0]+'-hour-'+str(i)
                plt.title(name.split('.')[0]+'-hour-'+str(i)+" Red stations have changing patterns") 
                nx.draw(G,nx.get_node_attributes(G, 'pos'),with_labels=True,node_color = color_map,node_size=400.0)  # networkx draw()
                plt.draw()  # pyplot draw()    
                plt.tight_layout()            
                fig.savefig(outRoot+name.split('.')[0]+'-hour-'+str(i)+'.jpg', bbox_inches="tight")
                plt.close()
                