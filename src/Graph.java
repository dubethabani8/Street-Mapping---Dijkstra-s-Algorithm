import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

public class Graph {
	public HashMap<String, Node> graph; // mapping of Node IDs to Node objects, built from a set of Edges
	String locations[];
	public ArrayList<Edge> edges;
	double minLat, minLon, maxLat, maxLon;
	double realWidth, realHeight;

	public Graph(String inputFile) throws Exception {
		
		graph = new HashMap<String, Node>();
		edges = new ArrayList<Edge> ();
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String line;
		//Get the minimum value to use for the plotting scale
		minLat = Double.POSITIVE_INFINITY;
		minLon = Double.POSITIVE_INFINITY;
		maxLat = Double.NEGATIVE_INFINITY;
		maxLon = Double.NEGATIVE_INFINITY;
		while((line = br.readLine()) != null) {
			String arr[] = line.split("\t");
			if(arr[0].equals("i")) {
				String ID = arr[1];
				double lat = Double.parseDouble(arr[2]);
				double lon = Double.parseDouble(arr[3]);
				Node i = new Node(ID, lat, lon);
				graph.put(arr[1], i);
				//For scaling the graph
				if(lat < minLat) minLat = lat;
				if(lon < minLon) minLon = lon;
				if(lat > maxLat) maxLat = lat;
				if(lon > maxLon) maxLon = lon;
			}
			else if(arr[0].equals("r")) {
				String rID = arr[1];
				String i1ID = arr[2];
				String i2ID = arr[3];
				double dist = getDistance(graph.get(i1ID).lat, graph.get(i1ID).lon, graph.get(i2ID).lat, graph.get(i2ID).lon);
				Edge e = new Edge(rID, i1ID, i2ID, dist);
				edges.add(e);
			}
			
		}
		realWidth = getDistance(minLat, minLon, minLat, maxLon);
		realHeight = getDistance(minLat, minLon, maxLat, minLon);

	      for (Edge e : edges) {
	         graph.get(e.n1).neighbours.put(graph.get(e.n2), e.dist);
	         graph.get(e.n2).neighbours.put(graph.get(e.n1), e.dist);
	         
	      }
	      
	      this.locations = new String[graph.size()];
			int i=0;
			for(String e: graph.keySet()) {
				locations[i] = e;
				i++;
			}
	      
	}

	public double getDistance(double lat1, double lon1, double lat2, double lon2) {
	    double latDistance = Math.toRadians(lat1 - lat2);
	    double lngDistance = Math.toRadians(lon1 - lon2);

	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	      + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	      * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double AVERAGE_RADIUS_OF_EARTH_KM = 6371;
	    double distanceKm =  AVERAGE_RADIUS_OF_EARTH_KM * c;
	    return distanceKm/0.6214;
		}
	
	/** Runs dijkstra using a specified source vertex */ 
	   public void dijkstra(String startName) {
	      if (!graph.containsKey(startName)) {
	         System.err.printf("Graph doesn't contain start Node \"%s\"\n", startName);
	         return;
	      }
	      final Node source = graph.get(startName);
	      NavigableSet<Node> q = new TreeSet<>();
	 
	      // set-up vertices
	      for (Node v : graph.values()) {
	         v.previous = v == source ? source : null;
	         v.dist = v == source ? 0 : Integer.MAX_VALUE;
	         q.add(v);
	      }
	 
	      dijkstra(q);
	   }
	
	   /** Implementation of dijkstra's algorithm using a binary heap. */
	   public void dijkstra(final NavigableSet<Node> q) {      
	      Node u, v;
	      while (!q.isEmpty()) {
	 
	         u = q.pollFirst(); // vertex with shortest distance (first iteration will return source)
	         if (u.dist == Integer.MAX_VALUE) break; // we can ignore u (and any other remaining vertices) since they are unreachable
	 
	         //look at distances to each neighbor
	         for (Map.Entry<Node, Double> a : u.neighbours.entrySet()) {
	            v = a.getKey(); //the neighbor in this iteration
	 
	            final double alternateDist = u.dist + a.getValue();
	            if (alternateDist < v.dist) { // shorter path to neighbor found
	               q.remove(v);
	               v.dist = alternateDist;
	               v.previous = u;
	               q.add(v);
	            } 
	         }
	      }
	   }
	   
	   /** Prints a path from the source to the specified vertex */
	   public void printPath(String endName) {
	      if (!graph.containsKey(endName)) {
	         System.err.printf("Graph doesn't contain end Node \"%s\"\n", endName);
	         return;
	      }
	 
	      graph.get(endName).printPath();
	      System.out.println();
	   }

}
	
