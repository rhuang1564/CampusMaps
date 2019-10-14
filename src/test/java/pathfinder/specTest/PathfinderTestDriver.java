package pathfinder.specTest;

import graph.DataEdge;
import graph.DirectedGraph;
import pathfinder.WeightedPathfinder;
import pathfinder.datastructures.Path;

import java.io.*;
import java.util.*;

/**
 * This class implements a testing driver which reads test scripts
 * from files for testing Graph.
 **/
public class PathfinderTestDriver {

  public static void main(String args[]) {
    try {
      if (args.length > 1) {
        printUsage();
        return;
      }

      PathfinderTestDriver td;

      if (args.length == 0) {
        td = new PathfinderTestDriver(new InputStreamReader(System.in),
                new OutputStreamWriter(System.out));
      } else {

        String fileName = args[0];
        File tests = new File (fileName);

        if (tests.exists() || tests.canRead()) {
          td = new PathfinderTestDriver(new FileReader(tests),
                  new OutputStreamWriter(System.out));
        } else {
          System.err.println("Cannot read from " + tests.toString());
          printUsage();
          return;
        }
      }

      td.runTests();

    } catch (IOException e) {
      System.err.println(e.toString());
      e.printStackTrace(System.err);
    }
  }

  private static void printUsage() {
    System.err.println("Usage:");
    System.err.println("to read from a file: java graph.specTest.GraphTestDriver <name of input script>");
    System.err.println("to read from standard in: java graph.specTest.GraphTestDriver");
  }

  /** String -> Graph: maps the names of graphs to the actual graph **/
  private final Map<String, DirectedGraph<String, DataEdge<String,Double>>> graphs = new HashMap<>();
  private final PrintWriter output;
  private final BufferedReader input;

  /**
   * @spec.requires r != null && w != null
   *
   * @effects Creates a new GraphTestDriver which reads command from
   * <tt>r</tt> and writes results to <tt>w</tt>.
   **/
  public PathfinderTestDriver(Reader r, Writer w) {
    input = new BufferedReader(r);
    output = new PrintWriter(w);
  }

  /**
   * @effects Executes the commands read from the input and writes results to the output
   * @throws IOException if the input or output sources encounter an IOException
   **/
  public void runTests()
          throws IOException
  {
    String inputLine;
    while ((inputLine = input.readLine()) != null) {
      if ((inputLine.trim().length() == 0) ||
              (inputLine.charAt(0) == '#')) {
        // echo blank and comment lines
        output.println(inputLine);
      }
      else
      {
        // separate the input line on white space
        StringTokenizer st = new StringTokenizer(inputLine);
        if (st.hasMoreTokens()) {
          String command = st.nextToken();

          List<String> arguments = new ArrayList<String>();
          while (st.hasMoreTokens()) {
            arguments.add(st.nextToken());
          }

          executeCommand(command, arguments);
        }
      }
      output.flush();
    }
  }

  private void executeCommand(String command, List<String> arguments) {
    try {
      if (command.equals("CreateGraph")) {
        createGraph(arguments);
      } else if (command.equals("AddNode")) {
        addNode(arguments);
      } else if (command.equals("AddEdge")) {
        addEdge(arguments);
      } else if (command.equals("ListNodes")) {
        listNodes(arguments);
      } else if (command.equals("ListChildren")) {
        listChildren(arguments);
      } else if (command.equals("FindPath")) {
        findPath(arguments);
      } else {
        output.println("Unrecognized command: " + command);
      }
    } catch (Exception e) {
      output.println("Exception: " + e.toString());
    }
  }

  private void createGraph(List<String> arguments) {
    if (arguments.size() != 1) {
      throw new CommandException("Bad arguments to CreateGraph: " + arguments);
    }

    String graphName = arguments.get(0);
    createGraph(graphName);
  }

  private void createGraph(String graphName) {
    // Insert your code here.

    graphs.put(graphName, new DirectedGraph<>());
    output.println("created graph " + graphName);
  }

  private void addNode(List<String> arguments) {
    if (arguments.size() != 2) {
      throw new CommandException("Bad arguments to addNode: " + arguments);
    }

    String graphName = arguments.get(0);
    String nodeName = arguments.get(1);

    addNode(graphName, nodeName);
  }

  private void addNode(String graphName, String nodeName) {
    // Insert your code here.

    DirectedGraph<String, DataEdge<String,Double>> graph = graphs.get(graphName);
    graph.addNode(nodeName);
    output.println("added node " + nodeName  + " to " + graphName);
  }

  private void addEdge(List<String> arguments) {
    if (arguments.size() != 4) {
      throw new CommandException("Bad arguments to addEdge: " + arguments);
    }

    String graphName = arguments.get(0);
    String parentName = arguments.get(1);
    String childName = arguments.get(2);
    Double edgeLabel = Double.parseDouble(arguments.get(3));

    addEdge(graphName, parentName, childName, edgeLabel);
  }

  private void addEdge(String graphName, String parentName, String childName,
                       Double edgeLabel) {
    // Insert your code here.

    DirectedGraph<String, DataEdge<String,Double>> graph = graphs.get(graphName);
    graph.addEdge(parentName,new DataEdge<>(childName,edgeLabel));
    output.println("added edge " + String.format("%.3f",edgeLabel) + " from " + parentName + " to " + childName + " in " + graphName);
  }

  private void listNodes(List<String> arguments) {
    if (arguments.size() != 1) {
      throw new CommandException("Bad arguments to listNodes: " + arguments);
    }

    String graphName = arguments.get(0);
    listNodes(graphName);
  }

  private void listNodes(String graphName) {
    // Insert your code here.

    DirectedGraph<String, DataEdge<String,Double>> graph = graphs.get(graphName);
    output.print(graphName + " contains:");
    ArrayList<String> nodes = new ArrayList<>(graph.getNodes());
    nodes.sort(String::compareTo);
    for(String curr : nodes){
      output.print(" " + curr);
    }
    output.println();
  }

  private void listChildren(List<String> arguments) {
    if (arguments.size() != 2) {
      throw new CommandException("Bad arguments to listChildren: " + arguments);
    }

    String graphName = arguments.get(0);
    String parentName = arguments.get(1);
    listChildren(graphName, parentName);
  }

  private void listChildren(String graphName, String parentName) {
    // Insert your code here.

    DirectedGraph<String, DataEdge<String,Double>> graph = graphs.get(graphName);
    output.print("the children of " + parentName + " in " + graphName + " are:");
    ArrayList<DataEdge<String,Double>> edges = new ArrayList<>(graph.getOutEdges(parentName));
    edges.sort(Comparator.comparing(DataEdge<String,Double>::getDestination).thenComparing(DataEdge::getData));
    for(DataEdge<String,Double> curr : edges){
      output.print(" " + curr.getDestination() + "(" + String.format("%.3f",curr.getData()) + ")");
    }
    output.println();
  }

  private void findPath(List<String> arguments) {
    if (arguments.size() != 3) {
      throw new CommandException("Bad arguments to FindPath: " + arguments);
    }

    String graphName = arguments.get(0);
    String node1 = arguments.get(1);
    String node2 = arguments.get(2);

    findPath(graphName, node1, node2);
  }

  private void findPath(String graphName, String node1, String node2) {
    String node1Space = node1.replace('_',' ');
    String node2Space = node2.replace('_',' ');

    DirectedGraph<String, DataEdge<String,Double>> graph = graphs.get(graphName);
    Set<String> nodes = graph.getNodes();
    if(!nodes.contains(node1Space) || !nodes.contains(node2Space)) {
      if (!nodes.contains(node1Space)) {
        output.println("unknown node " + node1Space);
      }
      if (!nodes.contains(node2Space)) {
        output.println("unknown node " + node2Space);
      }
    }
    else {
      output.println("path from " + node1Space + " to " + node2Space + ":");
      Path<String> path = WeightedPathfinder.dijkstra(graph,node1Space,node2Space);
      if(path == null){
        output.println("no path found");
      }
      else{
        for(Path.Segment curr : path){
          output.println(curr.getStart() + " to " + curr.getEnd() + String.format(" with weight %.3f",curr.getCost()));
        }
        output.println(String.format("total cost: %.3f", path.getCost()));
      }
    }
  }
  /**
   * This exception results when the input file cannot be parsed properly
   **/
  static class CommandException extends RuntimeException {

    public CommandException() {
      super();
    }
    public CommandException(String s) {
      super(s);
    }

    public static final long serialVersionUID = 3495;
  }
}
