package tests;

import GraphClasses.DirectedGraph;
import GraphClasses.PageRank;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by cgels on 10/18/17.
 */
public class PageRankTest {

    private DirectedGraph g;

    @Before
    public void setup() {
        g = new DirectedGraph(null);
    }

    @Test
    public void findDistance() throws Exception {
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(4, 2);
        g.addEdge(3, 1);


        PageRank pr = new PageRank(g);
        assertEquals(0.45, pr.iterateOnce(), .01);
        assertEquals(.405, pr.iterateOnce(), .01);
        assertEquals(.3645, pr.iterateOnce(), .01 );
    }

    @Test
    public void testConvergence() throws Exception {
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(4, 2);
        g.addEdge(3, 1);


        PageRank pr = new PageRank(g);
        pr.setConvergenceThreshold(.001);

        int iters = pr.iterateToConvergence();

//        ArrayList<Integer> expected = new ArrayList<>();
//        expected.add(0, )
//        ArrayList<Integer> ranking = pr.getTopKRankings(4);
//
//        for (int i = 0; i < ranking.size(); i++) {
//            assertEquals(expected.get(i), ranking.get(i));
//        }

    }



}