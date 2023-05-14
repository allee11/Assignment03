import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Graph {
    ArrayList<PersonNode>[] adjacencyList;//adjacency list
    PersonNode[] personList;//list containing PersonNode, associated with each node's vertex number
    HashMap<String, String> connectors = new HashMap<>();//contains all connectors
    int size = -1;//numbers of nodes in graph
    int[] dfsList;//dfsnum list for finding connectors
    int[] backList;//back list for finding connectors
    boolean[] isVisited;//visited list for finding connectors
    PersonNode start;//source node for finding connectors


    public Graph (int vertices) {//create new Graph with number of vertices
        adjacencyList = new ArrayList[vertices];//size of adjacency list
        personList = new PersonNode[vertices];//size of person list

        for (int i = 0; i < adjacencyList.length; i++) {//give each position an ArrayList
            adjacencyList[i] = new ArrayList<>();
        }
    }

    public void addEdge(PersonNode v1, PersonNode v2) {
        if(personList[v1.getVertexNum()] == null) {//if empty, put a PersonNode based on vertex number
            personList[v1.getVertexNum()] = v1;

            if(v1.getVertexNum() > size) {
                size = v1.getVertexNum();//increase size if the vertex number is greater than size
            }
        }

        //same as above, but for v2
        if(personList[v2.getVertexNum()] == null) {
            personList[v2.getVertexNum()] = v2;

            if(v2.getVertexNum() > size) {
                size = v2.getVertexNum();
            }
        }


        //add each node to each other's list to create edge
        adjacencyList[v1.getVertexNum()].add(v2);
        adjacencyList[v2.getVertexNum()].add(v1);
    }

    int dfsnum = 0;//dfsnum for vertices

    public void findConnectors(File file) {
        for(int i=0; i < personList.length; i++) {
            if(personList[i] != null) {
                //create new arrays each time
                isVisited = new boolean[size+1];
                dfsList = new int[size+1];
                backList = new int[size+1];
                start = personList[i];//source node

                searchConnectors(personList[i].getVertexNum());//recursive function for finding connectors
                dfsnum = 0;//reset dfsnum
            }
        }

        if(file != null) {//check if there is a file
            try {
                File writeFile = file;
                FileWriter writer = new FileWriter(writeFile);

                for(Map.Entry<String, String> entry : connectors.entrySet()) {
                    //print out and write to file each connector
                    System.out.println(entry.getKey());
                    writer.write(entry.getKey() + "\n");
                }

                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            for(Map.Entry<String, String> entry : connectors.entrySet()) {
                //print out each connector
                System.out.println(entry.getKey());
            }
        }
    }

    //not sure how to explain most since I did my best to follow what was said on the assignment
    public void searchConnectors(int v) {
        if(connectors.containsKey(personList[v].getEmail())) {
            return;
        }

        isVisited[v] = true;//set to true since vertex is visited
        dfsList[v] = dfsnum++;//set the vertex dfs number
        backList[v] = dfsList[v];//set the vertex back number

        for (PersonNode w : adjacencyList[v]) {//check every neighbor of the node
            if(!isVisited[w.getVertexNum()]) {
                searchConnectors(w.getVertexNum());//recursive call if not visited

                if(dfsList[v] > backList[w.getVertexNum()]) {
                    backList[v] = Math.min(backList[v], backList[w.getVertexNum()]);
                }

                if(dfsList[v] <= backList[w.getVertexNum()] && start.getVertexNum() != v) {
                    if(!connectors.containsKey(personList[v].getEmail())) {
                        connectors.put(personList[v].getEmail(), personList[v].getEmail());
                    }
                }
            }  else {
                backList[v] = Math.min(dfsList[w.getVertexNum()], backList[v]);
            }
        }
    }
}
