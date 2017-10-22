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

        PageRank pr = new PageRank(graph);
        pr.setConvergenceThreshold(0.001);
        int numIter = pr.iterateToConvergence();

        System.out.println(String.format("Iterations to converge: %d", numIter));
        System.out.println("Top 20 Documents:");
        System.out.println(pr.getTopKRankings(20));

    }




}
