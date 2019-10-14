package pathfinder;

import graph.DataEdge;
import graph.DirectedGraph;
import pathfinder.datastructures.Path;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Utility to find a Path in a DirectedGraph with weighted edges that are represented by DataEdges with data
 * with type Double.  Does not represent an ADT
 */
public class WeightedPathfinder {

    // This class does not represent an ADT.
    // Rep Inv and Abstraction Function goes here

    /**
     * Performs Dijkstra's Algorithm to find the shortest path in <var>graph</var>, with edges having weights as data,
     * between <var>start</var> and <var>dest</var>
     * @param graph a DirectedGraph to perform dijkstra's on with weights as Double's as the data for edges
     * @param start node to start from when performing dijkstra's
     * @param dest targeted destination node
     * @param <T> node type
     * @spec.requires graph,start,dest != null AND graph.getNodes().contains(start) AND graph.getNodes().contains(dest)
     * AND all edges of the graph are non-negative
     * @return Path with type T that is the shortest path, in respected to the weights of edges between nodes,
     * between <var>start</var> and <var>dest</var>.
     * Will return null if no path can be found
     * Will return an empty Path if start.equals(dest)
     */
    public static <T> Path<T> dijkstra(DirectedGraph<T, DataEdge<T,Double>> graph, T start, T dest){
        PriorityQueue<Path<T>> active = new PriorityQueue<>((t1, t2)-> Double.compare(t1.getCost(),t2.getCost()));

        Set<T> known = new HashSet<>();

        active.add(new Path<>(start));

        //active is empty when visited all possible nodes that can be reach from start
        while(!active.isEmpty()){
            Path<T> currPath = active.remove();
            if(currPath.getEnd().equals(dest)){
                return currPath;
            }
            else if(!known.contains(currPath.getEnd())){
                Set<DataEdge<T,Double>> newEdges = graph.getOutEdges(currPath.getEnd());
                //adds all an path for each edge that comes from the current node being processed
                for(DataEdge<T,Double> currEdge : newEdges){
                    if(!known.contains(currEdge.getDestination())){
                        active.add(currPath.extend(currEdge.getDestination(),currEdge.getData()));
                    }
                }
                known.add(currPath.getEnd());
            }
        }
        return null;
    }

}
