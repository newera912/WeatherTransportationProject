head="#################################################################\n"+\
"#APDM Input Graph, this input graph includes 3 sections:\n"+\
"#section1 : general information\n"+\
"#section2 : nodes\n"+\
"#section3 : edges\n"+\
"#section4 : trueSubGraph (Optional)\n"+\
"#\n"+\
"#if nodes haven't information set weight to null\n"+\
"#################################################################\n"+\
"SECTION1 (General Information)\n"+\
"numNodes = 10\n"+\
"numEdges = 13\n"+\
"usedAlgorithm = NULL\n"+\
"dataSource = NewYorkCityTaxi\n"+\
"END\n"+\
"################################################################\n"+\
"SECTION2 (Nodes Information)\n"+\
"NodeID PValue \n"
section3="END\n"+\
"#################################################################\n"+\
"SECTION3 (Edges Information)\n"+\
"EndPoint0 EndPoint1 Weight\n"+\
"0 1 1.000000\n"+\
"1 2 1.000000\n"+\
"2 3 1.000000\n"+\
"3 4 1.000000\n"+\
"3 5 1.000000\n"+\
"4 5 1.000000\n"+\
"4 6 1.000000\n"+\
"5 6 1.000000\n"+\
"5 7 1.000000\n"+\
"6 7 1.000000\n"+\
"7 8 1.000000\n"+\
"7 9 1.000000\n"+\
"8 9 1.000000\n"+\
"END\n"+\
"#################################################################\n"+\
"SECTION4 (TrueSubGraph Information)\n"+\
"EndPoint0 EndPoint1 Weight\n"

tail="END\n"+\
"#################################################################\n"

case1=[0,1,2,8,9]
case2=[1,8]
case3=[3,4,5,8,9]
case4=[5,6,8,9]

for v in range(2,11):
    with open("F:/workspace/git/Graph-MP/data/simu10/case4/simu10_case4_signal_"+str(v)+".txt","w") as oF:
        nodes=""
        true=""
        for i in range(10):
            if i in case4:
                nodes+=str(i)+" "+str(float(v))+"\n"
                true+=str(i)+" "+str(i)+" 1.0\n"
            else:
                nodes+=str(i)+" 1.0\n"
        oF.write(head)
        oF.write(nodes)
        oF.write(section3)
        oF.write(true)
        oF.write(tail)
print head
print section3