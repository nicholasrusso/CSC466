package tests;

import GraphClasses.DirectedGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
/**
 * Created by cgels on 10/14/17.
 */
public class DirectedGraphTest {
    DirectedGraph graph;

    @Before
    public void setup() {
        graph = new DirectedGraph(null);
        assertEquals(0,graph.nodeCount());
    }

    @Test
    public void addNode() throws Exception {
        assertEquals(0,graph.nodeCount());
        assertFalse(graph.containsNode(1));
        graph.addNode(1);
        assertTrue(graph.containsNode(1));
        assertEquals(1,graph.nodeCount());
    }

    @Test
    public void addEdgeFromExistingNodes() throws Exception {
        graph.addNode(1);
        graph.addNode(2);

        //ensure nodes exist
        assertTrue(graph.containsNode(1));
        assertTrue(graph.containsNode(2));
        assertFalse(graph.containsDirectedEdge(1, 2));


        graph.addEdge(1, 2);
        assertTrue(graph.containsDirectedEdge(1, 2));
        assertFalse(graph.containsDirectedEdge(2, 1));

        ArrayList<Integer> incomingNodes = graph.getIncomingNodes(2);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);

        assertEquals(1, incomingNodes.size());
        assertEquals(expected.get(0), incomingNodes.get(0));

        // check that directed graph can have bidirectional relationships
        graph.addEdge(2, 1);
        assertTrue(graph.containsDirectedEdge(2, 1));
    }

    @Test
    public void addEdgeFromUnseenNodes() throws Exception {
        assertFalse(graph.containsNode(3));
        assertFalse(graph.containsNode(4));
        graph.addEdge(3, 4);
        //nodes added
        assertTrue(graph.containsNode(3));
        assertTrue(graph.containsNode(4));
        // proper directed edge
        assertTrue(graph.containsDirectedEdge(3, 4));
        assertFalse(graph.containsDirectedEdge(4, 3));
    }

    @Test
    public void getIncomingNodes() throws Exception {
        graph.addEdge(2, 1);
        graph.addEdge(3, 1);
        graph.addEdge(4, 1);

        List<Integer> neighbors = graph.getIncomingNodes(1);

        assertEquals(3, neighbors.size());
        assertTrue(neighbors.contains(2));
        assertTrue(neighbors.contains(3));
        assertTrue(neighbors.contains(4));
    }

    @Test
    public void getEdgeCount() throws Exception {
        assertEquals(0, graph.getEdgeCount());

        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(1, 3);

        assertEquals(3, graph.getEdgeCount());

    }


    @Test
    public void getIndegree() throws Exception {
        graph.addEdge(1, 2);
        assertEquals(0, graph.getIndegree(1));
        assertEquals(1, graph.getIndegree(2));
    }

    @Test
    public void getOutdegree() throws Exception {
        graph.addEdge(1, 2);
        assertEquals(1, graph.getOutdegree(1));
        assertEquals(0, graph.getOutdegree(2));
        graph.addEdge(1, 3);
        assertEquals(2, graph.getOutdegree(1));
    }

}