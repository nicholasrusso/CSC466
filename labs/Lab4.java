package labs;

import GraphClasses.DirectedGraph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * CSC466 - PageRank lab specification: https://docs.google.com/document/d/1Pxnb1mLM2M6xGxagZuq7hiKLjjokZ2GaME27kdIjFgQ/edit?usp=sharing
 * <p>
 * Created by cgels on 10/14/17.
 */
public class Lab4 {


    public static void main(String args[]) {
        final DirectedGraph graph = new DirectedGraph("./labs/graph.txt");
        System.out.println(String.format("Node Count: %d", graph.nodeCount()));
        System.out.println(String.format("Edge Count: %d", graph.getEdgeCount()));

        //TODO: implement pageRank algorithm
        HashMap<Integer, Double> pageRankOld = initPageRank(graph);
        HashMap<Integer, Double> pageRankNew = pageRank(pageRankOld, graph);

        double L1_distance = findDistance(pageRankOld, pageRankNew);
        System.out.println(L1_distance);



//        double L1_distance = findDistance(pageRankOld, pageRankNew);
//        double epsilon = .001;
//
//        while (L1_distance > epsilon) {
//            pageRankOld = (HashMap<Integer, Double>) pageRankNew.clone();
//            pageRankNew = pageRank(pageRankOld, graph);
//            L1_distance = findDistance(pageRankOld, pageRankNew);
//        }

    }

    public static HashMap<Integer, Double> initPageRank(DirectedGraph graph) {
        final double prob = 1.0 / graph.nodeCount();
        Iterator<Integer> nodes = graph.nodeIterator();
        HashMap<Integer, Double> pageRank = new HashMap<>();

        nodes.forEachRemaining(nodeId -> pageRank.put(nodeId, prob));

        return pageRank;
    }

    public static HashMap<Integer, Double> pageRank(HashMap<Integer, Double> pageRankOld, DirectedGraph graph) {
        HashMap<Integer, Double> pageRankNew = new HashMap<>();
        final double d = .9;
        final int N = graph.nodeCount();

        pageRankOld.entrySet()
                   .stream()
                   .forEach(node_i -> {
                       double x = (1.0 - d) / N;
                       double y = d * graph.getIncomingNodes(node_i.getKey())
                                           .stream()
                                           .mapToDouble(node_j -> pageRankOld.get(node_j) / (graph.getOutdegree(node_j) > 0 ? graph.getOutdegree(
                                                   node_j) : 1))
                                           .sum();

                       pageRankNew.put(node_i.getKey(), x * y);
                   });


        return normalize(pageRankNew);
    }


    private static HashMap<Integer, Double> normalize(HashMap<Integer, Double> pageRank) {
        double total = pageRank.entrySet()
                               .stream()
                               .mapToDouble(node -> node.getValue())
                               .sum();

        pageRank.entrySet().stream().forEach(node -> pageRank.put(node.getKey(), node.getValue() / total));

        double verify = pageRank.entrySet()
                                .stream()
                                .mapToDouble(node -> node.getValue())
                                .sum();

        if (verify < .99 || verify > 1.01) {
            System.out.println("Did NOT normalize correctly... ");
            System.out.println(verify);

        }

        return pageRank;
    }


    /**
     * Finds L1 norm between 2 iterations of page rank.
     */
    public static double findDistance(HashMap<Integer, Double> pageRankOld, HashMap<Integer, Double> pageRankNew) {
        return pageRankNew.entrySet()
                          .stream()
                          .mapToDouble(node -> Math.abs(node.getValue() - pageRankOld.getOrDefault(node.getKey(), 0.0)))
                          .sum();
    }


}
