package graph.implTest;

import graph.DataEdge;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LabeledEdgeTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    /**
     * checks that Java asserts are enabled, and exits if not
     */
    @Before
    public void testAssertsEnabled() {
        CheckAsserts.checkAssertsEnabled();
    }


    DataEdge<String,String> edge;

    @Before
    public void setUp() {
        edge = new DataEdge<>( "node1","edge1");
    }

    @Test
    public void testGetLabel(){
        assertEquals("edge1",edge.getData());
    }

    @Test
    public void testGetDestination(){
        assertEquals("node1",edge.getDestination());
    }

    @Test
    public void testEqualsSameObject(){
        assertTrue(edge.equals(edge));
    }

    @Test
    public void testEqualsEquivalent(){
        assertTrue(edge.equals(new DataEdge<>("node1","edge1")));
    }

    @Test
    public void testNotEqualSameDestination(){
        assertTrue(!edge.equals(new DataEdge<>("node1","edge2")));
    }

    @Test
    public void testNotEqualSameLabel(){
        assertTrue(!edge.equals(new DataEdge<>("node2","edge1")));
    }

    @Test
    public void testHashCodeEquivalant(){
        assertTrue(edge.hashCode()== (new DataEdge<>("node1","edge1")).hashCode());
    }

}
