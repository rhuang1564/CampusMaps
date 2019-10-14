package graph.implTest;

import graph.DataEdge;
import graph.DirectedGraph;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Set;

import static org.junit.Assert.assertTrue;

public class DirectedGraphTest {

    @Rule public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    /** checks that Java asserts are enabled, and exits if not */
    @Before
    public void testAssertsEnabled() {
        CheckAsserts.checkAssertsEnabled();
    }

    //add nodes to graph with all of the names in names

    DirectedGraph<String, DataEdge<String,String>> graph;

    @Before
    public void setUp(){
        graph = new DirectedGraph<>();
    }

    @Test
    public void testIsEmptyAfterConstructed(){
        assertTrue(graph.isEmpty());
    }

    @Test
    public void testNoNodesAfterConstructed(){
        assertTrue(graph.getNodes().isEmpty());
    }

    @Test
    public void testAddMultipleNodes(){
        String[] names = {"node1","node2","node3","node4","node5","node6"};
        for(String curr: names) {
            graph.addNode(curr);
        }
        boolean isCorrect = true;
        for(String curr: names){
            isCorrect = isCorrect && graph.getNodes().contains(curr);
        }
        assertTrue(isCorrect);
    }

    @Test
    public void testIsNotEmptyAfterAddingNodes(){
        graph.addNode("node1");
        assertTrue(!graph.isEmpty());
    }

    @Test
    public void testAddSelfEdge(){
        graph.addNode(("node1"));
        graph.addEdge("node1", new DataEdge<>("node1","edge1"));
        Set<DataEdge<String,String>> edges = graph.getOutEdges("node1");
        assertTrue(edges.contains(new DataEdge<>("node1","edge1")) && edges.size() == 1);
    }

    @Test
    public void testAddMultipleEdgesBetweenNodes(){
        graph.addNode("node1");
        graph.addNode("node2");
        graph.addEdge("node1",new DataEdge<>("node2","edge1"));
        graph.addEdge("node1",new DataEdge<>("node2","edge2"));
        Set<DataEdge<String,String>> edges = graph.getOutEdges("node1");
        assertTrue(edges.contains(new DataEdge<>("node2","edge1")) &&
                edges.contains(new DataEdge<>("node2","edge2")) && edges.size() == 2);

    }

    @Test
    public void testAddSingleEdge(){
        graph.addNode("node1");
        graph.addNode("node2");
        graph.addEdge("node1",new DataEdge<>("node2","edge1"));
        Set<DataEdge<String,String>> edges = graph.getOutEdges("node1");
        assertTrue(edges.contains(new DataEdge<>("node2","edge1")) &&
                edges.size() == 1);
    }

    @Test
    public void testGetOutEdgesForNodeWithNoEdges(){
        graph.addNode("node1");
        assertTrue(graph.getOutEdges("node1").isEmpty());
    }

    @Test
    public void testGetOutEdgesForNonExistentNode(){
        assertTrue(graph.getOutEdges("node1")== null);
    }



}
