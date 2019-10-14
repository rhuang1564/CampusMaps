package graph;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <b>DirectedGraph</b> represents a mutable Directed Graph with nodes T, and edges, E, that extends DataEdge, which
 * contains data about its destination and has data. Every pair of nodes can have multiple edges between them, as long
 * as no two edges have the same data when they have the same origin and destination.  Also there should not be any
 * duplicate nodes
 *
 * @spec.specfield nodes : Set of T // The nodes (vertices) of the graph.
 * @spec.specfield data_edges : Set of E extends DataEdge //  Each edge has an origin, destination, and data,
 *                      with origin and destination being in this.nodes and no 2 edges are the same in all three fields.
 * @spec.specfield outgoing_edges : Set of E extends DataEdge // The graph has an outgoing_edges for each node in nodes
 *                      that contain all the edges that comes from that node.
 */
public class DirectedGraph<T,E extends DataEdge> {

    //maps a node to outgoing_edges.
    private final Map<T, Set<E>> nodeMap;
    //expensive checkrep is enabled if true
    private final boolean DEBUG = false;

    //  Abstraction Function:
    //      AF(this) = A Direction Graph, with nodes and DataEdges.
    //          if  this = a graph represented by {v_1={e_1_1,e_1_2,...},v_2={e_2_1,e_2_2,...},...} with
    //                  v_i being a vertex/node in this and e_i_j being the edge from v_i in this for any i,j
    //         then this.nodeMap maps v_k to {e_k_1,e_k_2,...} and
    //              {v_l={},...} represents that v_l has no out edges coming from it and
    //              {} represents a empty graph with no nodes or edges
    //
    //  Rep Invariant:
    //      nodeMap != null &&
    //      With the rep of this given in the AF,
    //          !(v_i.equals(v_j)) for any i != j &&
    //          !(e_i_j.equals(e_i_k)) for any i,j,k with j != k &&
    //          v_i != null for any i &&
    //          e_i_j != null for any i,j &&
    //          e_i_j should not point to a node that is not contained in this.nodeMap.keySet() for any i


    /**@spec.effects Constructs a new empty DirectedGraph*/
    public DirectedGraph(){
        nodeMap = new HashMap<>();
        checkRep();
    }

    /**
     * Gets all of the nodes in this
     *
     * @return nodes
     */
    public Set<T> getNodes(){
        checkRep();
        return new HashSet<>(nodeMap.keySet());
    }

    /**
     * Gets a Set of all the edges that originate from the node specified
     *
     * @param node that is in nodes
     * @spec.requires node != NULL
     * @return a Set of E that comes out of <var>node</var>.
     *      If <var>node</var> doesn't exist in this graph, will return NULL.
     *      If <var>node</var> is in this but does not have any out Edges, will return an empty Set
     */
    public Set<E> getOutEdges(T node){
        checkRep();
        Set<E> results = nodeMap.get(node);
        if(results == null){
            return null;
        }
        return new HashSet<>(results);
    }

    /**
     * Adds a node to this
     *
     * @param node the node to be added
     * @spec.requires this does not already contain <var>node</var> AND <var>node</var> != NULL AND
     *      node to be immutable or never to be modified
     * @spec.modifies this
     * @spec.effects adds <var>node</var> into this with no in edges or out edges
     */
    public void addNode(T node){
        checkRep();
        nodeMap.put(node,new HashSet<>());
        checkRep();
    }

    /**
     * Adds a <var>edge</var> that comes out of origin <var>origin</var>
     *
     * @param origin the node that the new edge comes from.
     * @param edge the new edge that contains data about its destination and data
     * @spec.requires <var>origin</var>,<var>edge</var> != NULL AND
     *      there should not exist a edge with exact same (origin, edge.getDestination(),edge.getData()) in this AND
     *      this should contain Nodes with names <var>origin</var> and edge.getDestination()
     * @spec.modifies this
     * @spec.effects Adds a Edge from <var>origin</var> to edge.getDestination()
     *      with the data edge.getData()
     */
    public void addEdge(T origin, E edge){
        checkRep();
        nodeMap.get(origin).add(edge);
        checkRep();
    }

    /**
     * @return true iff this contains no Nodes
     */
    public boolean isEmpty(){
        checkRep();
        return nodeMap.isEmpty();
    }

    private void checkRep() {
        assert nodeMap != null;
        if(DEBUG){
            Set<T> nodes = nodeMap.keySet();
            for(T currNode: nodeMap.keySet()){
                for(E currEdge : nodeMap.get(currNode)){
                    assert nodes.contains(currEdge.getDestination());
                }
            }
        }
        //since sets cannot have duplicates, we dont need to check if there are duplicate
        //nodes or edges.  Also since null is a primitive, we cannot store nulls as edges and nodes
        //in the set they are stored in
    }


}
