package GraphClasses;

import DocumentClasses.MapUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by cgels on 10/15/17.
 */
public class PageRank {
    DirectedGraph graph;
    HashMap<Integer, Double> previousDistances;
    HashMap<Integer, Double> pageRankOld;
    HashMap<Integer, Double> pageRankNew;

    public PageRank(DirectedGraph graph) {
        this.graph = graph;
        pageRankOld = initPageRank(graph);
    }

    public double iterateOnce() {
        pageRankNew = pageRank(pageRankOld, graph);
        double distance = findDistance(pageRankOld, pageRankNew);
        //for next iteration
        pageRankOld = (HashMap<Integer, Double>) pageRankNew.clone();
        return distance;
    }

    public ArrayList<Integer> getTopKRankings(int k) {
        if (pageRankNew != null) {
            return MapUtilities.sortByValueDescending(pageRankNew)
                               .entrySet()
                               .stream()
                               .limit(k)
                               .map(node -> node.getKey())
                               .collect(Collectors.toCollection(ArrayList::new));
        }
        return new ArrayList<>();
    }

    public ArrayList<Double> getTopKDistances(int k) {
        ArrayList<Double> distances = new ArrayList<>();
        for (Integer nodeId : getTopKRankings(k)) {
            distances.add(previousDistances.get(nodeId));
        }
        return distances;
    }

    private HashMap<Integer, Double> initPageRank(DirectedGraph graph) {
        final double prob = 1.0 / graph.nodeCount();
        Iterator<Integer> nodes = graph.nodeIterator();
        HashMap<Integer, Double> pageRank = new HashMap<>();

        nodes.forEachRemaining(nodeId -> pageRank.put(nodeId, prob));

        return pageRank;
    }

    private HashMap<Integer, Double> pageRank(HashMap<Integer, Double> pageRankOld, DirectedGraph graph) {
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


    private HashMap<Integer, Double> normalize(HashMap<Integer, Double> pageRank) {
        double total = pageRank.entrySet()
                               .stream()
                               .mapToDouble(node -> node.getValue())
                               .sum();

        pageRank.entrySet()
                .stream()
                .forEach(node -> pageRank.put(node.getKey(), node.getValue() / total));

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
     * Finds L1 norm between 2 iterations of page rank -- stores node per distance.
     */
    public double findDistance(HashMap<Integer, Double> pageRankOld, HashMap<Integer, Double> pageRankNew) {
        // store distances per node
        previousDistances = new HashMap<>();
        double distance = 0.0;


        for (Map.Entry<Integer, Double> node : pageRankNew.entrySet()) {
            double dist = Math.abs(node.getValue() - pageRankOld.getOrDefault(node.getKey(), 0.0));
            distance += dist;
            previousDistances.put(node.getKey(), dist);
        }

        return distance;
    }


}
