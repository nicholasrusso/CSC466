package GraphClasses;

import DocumentClasses.MapUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by cgels on 10/15/17.
 */
public class PageRank {
    protected DirectedGraph graph;
    protected HashMap<Integer, Double> perNodeDistances; // stores node-wise distances between iteration t+1 and t.
    protected HashMap<Integer, Double> pageRankOld;
    protected HashMap<Integer, Double> pageRankNew;

    private int numIterations = 0;
    private double prevDistance = 0;
    private boolean verbose = false;

    // algorithm constants
    private double d = 0.9;
    private int maxIterations = 1000;
    private double convergenceThreshold = 0.0001;

    /**
     * Initializes page rank algorithm based on provided DirectedGraph.
     * */
    public PageRank(DirectedGraph graph) {
        this.graph = graph;
        pageRankOld = initPageRank();
    }

    /**
     * Creates the initial state of probability graph based on the directed graph.
     * */
    private HashMap<Integer, Double> initPageRank() {
        HashMap<Integer, Double> pageRank = new HashMap<>();
        final double prob = 1.0 / graph.nodeCount();
        graph.nodeStream().forEach(node_i -> pageRank.put(node_i, prob));
        return pageRank;
    }

    /**
     * Computes the i+1 step of power iteration using results from i-th iteration.
     * */
    private HashMap<Integer, Double> pageRank_next() {
        final int N =  graph.nodeCount();
        final double x = (1.0 - d) / N;  //chance that we surf random link

        if (pageRankNew == null) {
            pageRankNew = new HashMap<>();
        }

//        HashMap<Integer, Double> pageRankNew = new HashMap<>();
        graph.nodeStream().forEach(node_i -> pageRankNew.put(node_i, x + pageRank_i(node_i)));

        return normalizedPageRank();
    }

    /**
     * Sums the previous pageRank score for each node (j) pointing to i-th node.
     * The total quantity is scaled by the probability 'd' that we explore these links.
     * */
    private double pageRank_i(Integer node_i) {
        ArrayList<Integer> incomingNodes = graph.getIncomingNodes(node_i);
        // get contribution from each node pointing to node_i && evenly distribute PR(j) among all its outbound links
        double sum = incomingNodes.stream()
                                  .mapToDouble(node_j -> pageRankOld.get(node_j) / graph.getOutdegree(node_j))
                                  .sum();
        //scale total by probability that we follow a related link
        return d * sum;
    }


    /**
     * Takes in the newly computed pageRank values and normalizes probabilities of each node.
     * */
    private HashMap<Integer, Double> normalizedPageRank() {
        HashMap<Integer, Double> normalizedPR = new HashMap<>();
        double total = pageRankNew.entrySet()
                               .stream()
                               .mapToDouble(Map.Entry::getValue)
                               .sum();

        pageRankNew.entrySet()
                .stream()
                .forEach(node_i -> normalizedPR.put(node_i.getKey(), node_i.getValue() / total));

        verifyNormalizedPR(normalizedPR);

        return normalizedPR;
    }

    /**
     * Finds L1 norm between current and previous iterations of page rank -- stores node per distance.
     */
    public double findDistance(HashMap<Integer, Double> pageRankOld, HashMap<Integer, Double> pageRankNew) {
        perNodeDistances = new HashMap<>();
        double distance = 0.0;


        for (Map.Entry<Integer, Double> node : pageRankNew.entrySet()) {
            double dist_i = Math.abs(node.getValue() - pageRankOld.get(node.getKey()));
            if (verbose) {
                System.out.println(String.format("Diff for Node: %d", node.getKey()));
                System.out.println(dist_i);
            }
            distance += dist_i;
            perNodeDistances.put(node.getKey(), dist_i);
        }

        return distance;
    }




    /**
     * Carries out 1 step in page rank power iteration algorithm and returns difference in distance from previous iteration.
     * */
    public double iterateOnce() {
        pageRankNew = pageRank_next();
        double distance = findDistance(pageRankOld, pageRankNew);
        double diff = Math.abs(distance - prevDistance);
        prevDistance = distance;
        numIterations++; // increment internal counter

        pageRankOld = (HashMap<Integer, Double>) pageRankNew.clone(); // update variables for next iteration.

        if (verbose) { // if interval verbose flag set output distance
            System.out.println(distance);
        }



        return diff;
    }


    /**
     * Iterates until difference in distance is < .0001 OR no convergence after MAX_ITERATIONS.
     * */
    public int iterateToConvergence() {
        // compute first
        double diff = iterateOnce();

        while (diff > convergenceThreshold) {
            if (numIterations == maxIterations) {
                if (verbose) System.out.println(String.format("Did not converge after %d iterations.", maxIterations));
                break;
            }

            diff = iterateOnce();
        }

        return numIterations;
    }


    /**
     * Returns the top k nodes according to current iteration of page rank algorithm.
     * */
    public ArrayList<Integer> getTopKRankings(int k) {
        if (pageRankNew != null) {
            return MapUtilities.sortByValueDescending(pageRankNew)
                               .entrySet()
                               .stream()
                               .limit(k)
                               .map(Map.Entry::getKey)
                               .collect(Collectors.toCollection(ArrayList::new));
        }
        return new ArrayList<>();
    }


    /**
     * Method to expose distances for the top k nodes for current iteration of page rank.
     * */
    public ArrayList<Double> getTopKDistances(int k) {
        ArrayList<Double> distances = new ArrayList<>();
        for (Integer nodeId : getTopKRankings(k)) {
            distances.add(perNodeDistances.get(nodeId));
        }
        return distances;
    }

    /**
     * Helper method to check that we normalized probabilities correctly (Sum of all nodes should sum to 1).
     * */
    private void verifyNormalizedPR(HashMap<Integer, Double> normalizedPR) {
        double sumPR = normalizedPR.entrySet()
                                   .stream()
                                   .mapToDouble(Map.Entry::getValue)
                                   .sum();

        if (!(sumPR > .99 && sumPR < 1.01)) {
            System.err.println(String.format("Iteration (%d) did not normalizedPageRank probability to 1", numIterations));
        }

    }

    // SETTERS

    /**
     * Set verbose flag to print out periodic updates of power iteration and other computations.
     * */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Set the maximum number of iterations allowed before giving up.
     * */
    public void setMaxIterations(int limit) {
        this.maxIterations = limit;
    }


    /**
     * Set custom convergence threshold for power iteration stopping condition.
     * Constraints: 0 < epsilon < 1
     *
     * */
    public void setConvergenceThreshold(double epsilon) {
        if (epsilon > 0 && epsilon < 1) {
            this.convergenceThreshold = epsilon;
        }
    }

}
