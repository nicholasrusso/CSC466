package GraphClasses;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by cgels on 10/14/17.
 */
public class DirectedGraph {
    Set<Integer> nodes;
    // Node ID -> List of incoming Nodes -- (indegree(ID) = adjacency.get(ID).size()
    Map<Integer, ArrayList<Integer>> adjacencyList;
    Map<Integer, Integer> outDegrees;

    /**
     * Loads graph from data file with an expected format. See method: loadGraphFromFile
     * @param filePath - if null, instantiates totally empty graph.
     * */
    public DirectedGraph(String filePath) {
        nodes = new TreeSet<>();
        adjacencyList = new HashMap<>();
        outDegrees = new HashMap<>();

        if (filePath != null) loadGraphFromFile(filePath);
    }

    /**
     * Read directed graph from a file where each line follows the format: fromNodeID, unused data, toNodeID, unused data.
     * */
    private void loadGraphFromFile(String filePath) {
        Iterator<String> graphData;
        try {
            graphData = Files.lines(Paths.get(filePath))
                 .collect(Collectors.toList()).iterator();

            while(graphData.hasNext()) {
                String[] elements = graphData.next().split(",");

                Integer fromNode = Integer.parseInt(elements[0]);
                Integer toNode = Integer.parseInt(elements[2]);

                addEdge(fromNode, toNode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the number of distinct vertices in the graph.
     * */
    public int nodeCount() {
        return nodes.size();
    }


    /**
     * Return the total number of directed edges in the graph.
     * */
    public int getEdgeCount() {
        return adjacencyList.keySet()
                            .stream()
                            .mapToInt(nodeId -> getIncomingNodes(nodeId).size())
                            .sum();
    }


    /**
     * Check for existence of any vertex.
     * */
    public boolean containsNode(Integer nodeID) {
        return nodes.contains(nodeID);
    }

    /**
     * Check for existence of directed edge.
     * */
    public boolean containsDirectedEdge(Integer from, Integer to) {
        return getIncomingNodes(to).contains(from);
    }


    /**
     * Adds node to Set of vertices.
     * */
    public void addNode(Integer nodeID) {
        nodes.add(nodeID);
    }

    /**
     * Adds directed edge between 'fromNode' --> 'toNode' to adjacency list and updates outdegrees.
     * */
    public void addEdge(Integer fromNode, Integer toNode) {
        // add any new nodes before created an edge between them
        if (!containsNode(fromNode)) addNode(fromNode);
        if (!containsNode(toNode)) addNode(toNode);

        // update the existing list
        ArrayList<Integer> incomingNodes = getIncomingNodes(toNode);
        incomingNodes.add(fromNode);
        adjacencyList.put(toNode, incomingNodes);
        outDegrees.put(fromNode, getOutdegree(fromNode) + 1);
    }

    /**
     * Returns the out-degree of specified node.
     * */
    public int getOutdegree(Integer nodeID) {
        return outDegrees.getOrDefault(nodeID, 0);
    }

    /**
     * Returns the in-degree of specified node.
     * */
    public int getIndegree(Integer nodeID) {
        return adjacencyList.getOrDefault(nodeID, new ArrayList<>())
                            .size();
    }


    /**
     * Returns the list of incoming node IDs for specified node ID.
     * */
    public ArrayList<Integer> getIncomingNodes(Integer nodeID) {
        return adjacencyList.getOrDefault(nodeID, new ArrayList<>());
    }


}
