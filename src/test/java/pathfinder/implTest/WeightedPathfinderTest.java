package pathfinder.implTest;

import graph.DataEdge;
import graph.DirectedGraph;
import graph.implTest.CheckAsserts;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import pathfinder.WeightedPathfinder;
import pathfinder.datastructures.Path;

import static org.junit.Assert.assertTrue;

public class WeightedPathfinderTest {

    @Rule public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    DirectedGraph<String, DataEdge<String,Double>> graph;

    /** checks that Java asserts are enabled, and exits if not */
    @Before
    public void testAssertsEnabled() {
        CheckAsserts.checkAssertsEnabled();
    }

    @Before
    public void setUp(){
        graph = new DirectedGraph<>();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addNode("E");
        graph.addEdge("A", new DataEdge<>("B",1.0));
        graph.addEdge("B", new DataEdge<>("C",1.0));
        graph.addEdge("C", new DataEdge<>("A",1.0));
        graph.addEdge("C", new DataEdge<>("D",10.0));
        graph.addEdge("D", new DataEdge<>("A",1.0));
    }

    @Test
    public void testDijkstraBetweenSame(){
        Path<String> path = WeightedPathfinder.dijkstra(graph,"A","A");
        assertTrue(path.equals(new Path<>("A")));
    }

    @Test
    public void testDijkstraNonExistantPath(){
        Path<String> path = WeightedPathfinder.dijkstra(graph,"A","E");
        assertTrue(path == null);
    }

    @Test
    public void testDijkstraMultiplePathsAndCycles(){
        Path<String> path = WeightedPathfinder.dijkstra(graph,"A","D");
        assertTrue(path.equals(
                new Path<>("A").extend("B",1.0).extend("C",1.0)
                        .extend("D",10.0)));
    }


}
