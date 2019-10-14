package pathfinder;

import graph.DataEdge;
import graph.DirectedGraph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
In the pathfinder homework, the text user interface calls these methods to talk
to your model. In the campuspaths homework, your graphical user interface
will ultimately make class to these methods (through a web server) to
talk to your model the same way.

This is the power of the Model-View-Controller pattern, two completely different
user interfaces can use the same model to display and interact with data in
different ways, without requiring a lot of work to change things over.
*/

/**
 * This class represents the connection between the view and controller and the model
 * for the pathfinder and campus paths applications.  This class is immutable
 *
 * @spec.specfield buildings : Set of CampusBuilding // Buildings in this and the information for each of them
 * @spec.specfield paths : Set of CampusPath // Paths and their locations that are in this
 */
public class ModelConnector {

  //true if we want to perform expensive checkreps
  private final boolean DEBUG = false;
  //Graph representation of the campus map with its nodes being Points that represent ends of paths and edges having
  //data about how the distance between those two points.
  private final DirectedGraph<Point, DataEdge<Point,Double>> mapGraph;
  //Maps shortName of a building to its longName
  private final Map<String, String> buildingName;
  //Maps shortName of a building to the Point that represents its location
  private final Map<String, Point> buildingPosition;

  //  Abstraction Function:
  //      AF(this) = A model of a campus map that has buildingName mapping buildings'
  //          short names to their long names, buildingPositions that maps buildings' short names to their position
  //          represented as a Point, and paths on the map represented by a DirectedGraph with nodes representing
  //          the ends of paths as Points, and Edges representing the actual path between points with their data being
  //          the distance between two Points (or Nodes)
  //
  //  Rep Invariant:
  //      mapGraph,buildingName,buildingPosition != null &&
  //      buildingName.keySet().equals(buildingPosition.keySet) &&
  //      for any Point p in buildingPosition.values(), mapGraph.getNodes().contains(p);


  /**
   * Creates a new {@link ModelConnector} and initializes it to contain data about
   * pathways and buildings or locations of interest on the campus of the University
   * of Washington, Seattle. When this constructor completes, the dataset is loaded
   * and prepared, and any method may be called on this object to query the data.
   * @spec.effects creates a populated ModelConnector
   */
  public ModelConnector() {
    buildingName = new HashMap<>();
    buildingPosition = new HashMap<>();
    populateBuildings();

    mapGraph = new DirectedGraph<>();
    populatePaths();

    checkRep();
  }

  /**
   * Populates information for the buildings in this with data given by CampusPathsParser.parseCampusBuilding
   * Since this is immutable should only be called as part of the constructor. Also because of this reason,
   * the Rep of this might not be correct after executing this method.
   * @spec.modifies this
   * @spec.effects this.buildings
   */
  private void populateBuildings(){
    List<CampusBuilding> buildings = CampusPathsParser.parseCampusBuildings();
    //adds all the buildings into a buildingInfo
    for(CampusBuilding building : buildings){
      buildingName.put(building.getShortName(),building.getLongName());
      buildingPosition.put(building.getShortName(),new Point(building.getX(),building.getY()));
    }

  }
  /**
   * Populates information for the paths in this with data given by CampusPathsParser.parseCampusPaths
   * Since this is immutable should only be called as part of the constructor. Also because of this reason,
   * the Rep of this might not be correct after executing this method.
   * @spec.modifies this
   * @spec.effects this.buildings
   */
  private void populatePaths(){
    List<CampusPath> paths = CampusPathsParser.parseCampusPaths();
    for(CampusPath path : paths){
      Point currStart = new Point(path.getX1(),path.getY1());
      Point currEnd = new Point(path.getX2(),path.getY2());
      if(!mapGraph.getNodes().contains(currStart)){
        mapGraph.addNode(currStart);
      }
      if(!mapGraph.getNodes().contains(currEnd)){
        mapGraph.addNode(currEnd);
      }
      mapGraph.addEdge(currStart, new DataEdge<>(currEnd,path.getDistance()));
    }
  }

  /**
   * @param shortName The short name of a building to query.
   * @return {@literal true} iff the short name provided exists in this campus map.
   */
  public boolean shortNameExists(String shortName) {
    return buildingName.containsKey(shortName);
  }

  /**
   * @param shortName The short name of a building to look up.
   * @return The long name of the building corresponding to the provided short name.
   * @throws IllegalArgumentException if the short name provided does not exist.
   */
  public String longNameForShort(String shortName) {
    if(!shortNameExists(shortName)){
      throw new IllegalArgumentException();
    }
    return buildingName.get(shortName);
  }

  /**
   * @return The mapping from all the buildings' short names to their long names in this campus map.
   */
  public Map<String, String> buildingNames() {
    return new HashMap<>(buildingName);
  }

  /**
   * Finds the shortest path, by distance, between the two provided buildings.
   *
   * @param startShortName The short name of the building at the beginning of this path.
   * @param endShortName   The short name of the building at the end of this path.
   * @return A path between {@code startBuilding} and {@code endBuilding}, or {@literal null}
   * if none exists.
   * @throws IllegalArgumentException if {@code startBuilding} or {@code endBuilding} are
   *                                  {@literal null}, or not valid short names of buildings in
   *                                  this campus map.
   */
  public Path<Point> findShortestPath(String startShortName, String endShortName) {
    if(startShortName == null || endShortName == null || !buildingPosition.containsKey(startShortName) || !buildingPosition.containsKey(endShortName)){
      throw new IllegalArgumentException();
    }
    return WeightedPathfinder.dijkstra(mapGraph,buildingPosition.get(startShortName),buildingPosition.get(endShortName));
  }

  private void checkRep() {
    assert mapGraph != null;
    assert buildingName != null;
    assert buildingPosition != null;
    assert buildingName.keySet().equals(buildingPosition.keySet());
    Set<Point> campusPoints = mapGraph.getNodes();
    if (DEBUG) {
      for (Point buildingPoint : buildingPosition.values()) {
        assert campusPoints.contains(buildingPoint);
      }
    }
  }
}
