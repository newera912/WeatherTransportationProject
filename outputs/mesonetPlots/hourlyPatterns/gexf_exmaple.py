from gexf import Gexf

# # test helloworld.gexf 
# gexf = Gexf("Paul Girard","A hello world! file") 
# graph=gexf.addGraph("directed","static","a hello world graph")
# 
# graph.addNode("0","hello") 
# graph.addNode("1","World") 
# graph.addEdge("0","0","1")
# 
# output_file=open("hellowrld.gexf","w") 
# gexf.write(output_file)


 
gexf = Gexf('Your Name','28-11-2012')
#make an undirected dynamical graph
graph = gexf.addGraph("undirected","dynamic","date")
#you add nodes with a unique id
graph.addNode("0","0")
graph.addNode("1","1")
graph.addNode("2","2")
#make edge with unique id, the edge has time duration from start to end
graph.addEdge("0","0","1",start = '28-11-2012' , end = '29-11-2012')
graph.addEdge("1","2","1",start = '29-11-2012' , end = '30-11-2012')
#write the gexf format to fileout
fileOut=open("ex.gexf","w")
gexf.write(fileOut)