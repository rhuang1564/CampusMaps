package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.ModelConnector;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.Map;
import java.util.TreeMap;

public class SparkServer {

  public static void main(String[] args) {
    CORSFilter corsFilter = new CORSFilter();
    corsFilter.apply();
    // The above two lines help set up some settings that allow the
    // React application to make requests to the Spark server, even though it
    // comes from a different server.
    // You should leave these two lines at the very beginning of main().

    Gson gson = new Gson();
    ModelConnector model = new ModelConnector();

    //gets a shortest path between two buildings with query params "origin" and "destination"
    Spark.get("/path",new Route(){
      @Override
      public Object handle(Request request, Response response) throws Exception {
        String origin = request.queryParams("origin");
        String dest = request.queryParams("destination");
        if(origin == null || dest == null){
          Spark.halt(400);
        }
        return gson.toJson(model.findShortestPath(origin,dest));
      }
    });

    //gets all the building on the map
    Spark.get("/buildings",new Route(){
      @Override
      public Object handle(Request request, Response response) throws Exception {
        Map<String,String> sortedBuildings = new TreeMap<>(model.buildingNames());
        return gson.toJson(sortedBuildings);
      }
    });
  }

}
