package labs;

import GraphClasses.DirectedGraph;
import GraphClasses.PageRank;


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

        PageRank pr = new PageRank(graph);
        pr.setVerbose(true);
        pr.setConvergenceThreshold(0.001);
        pr.iterateToConvergence();

    }




}
