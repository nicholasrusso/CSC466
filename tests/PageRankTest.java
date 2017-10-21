package tests;

import GraphClasses.DirectedGraph;
import GraphClasses.PageRank;
import org.junit.Before;
import org.junit.Test;

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
//        pr.setVerbose(true);
        // distance 1 - 0 = .45 (iteration 0 distance = 0)
        assertEquals(0.45, pr.iterateOnce(), .01);
        // distance 2 - 1 = .405
        assertEquals(.045, pr.iterateOnce(), .01);
        // distance 3 - 2 = .3645
        assertEquals(.0405, pr.iterateOnce(), .01 );
    }

    @Test
    public void testConvergence() throws Exception {
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(4, 2);
        g.addEdge(3, 1);


        PageRank pr = new PageRank(g);
        pr.setConvergenceThreshold(.01);

        int iters = pr.iterateToConvergence();
    }

}