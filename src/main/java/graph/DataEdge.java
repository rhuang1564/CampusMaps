package graph;

/**
 * <b>DataEdge</b> represents an outgoing edge with data and the node that it points to.
 * This class is Immutable.  Does not contain the origin of the edge.
 *  @spec.specfield destination : T  // node that this edge points to
 *  @spec.specfield data : E        // The data of the edge.
 */
public class DataEdge<T,E> {
    //node that this edge points to
    private final T destination;
    
    //data of this edge
    private final E data;

    // Abstraction Function:
    //  AF(this) = a edge that
    //      does not have any specific origin but
    //      points to the destination of this.destination and
    //      has a data of this.data
    //
    //  Rep Invariant:
    //      data != null && destination != null

    /**
     * @spec.effects creates a data edge with data <var>data</var> and points to node,<var>destination</var>
     * @param data data of new edge
     * @param destination node that the edge points to
     * @spec.requires destination, data to be immutable or never to be modified
     */
    public DataEdge(T destination, E data){
        this.data = data;
        this.destination = destination;
        checkRep();
    }

    /**
     * Returns the data of this edge
     *
     * @return data of this
     */
    public E getData(){
        return data;
    }

    /**
     * Returns destination of this edge
     *
     * @return destination
     */
    public T getDestination() {
        return destination;
    }

    /**
     * Returns true iff <var>o</var> represents same edge as this edge.
     *
     * @param o object to compare against
     * @return true iff <var>o</var> represents same edge as this in terms of destination and data.
     */
    @Override
    public boolean equals(Object o){
        if(!(o instanceof DataEdge)){
            return false;
        }
        DataEdge other = (DataEdge) o;
        return other.getDestination().equals(destination) && other.getData().equals(data);
    }

    /**
     * Returns hash code of this edge
     *
     * @return hash code of this edge
     */
    @Override
    public int hashCode(){
        return 31*destination.hashCode() ^ data.hashCode();
    }

    private void checkRep(){
        assert data != null;
        assert destination != null;
    }
}
