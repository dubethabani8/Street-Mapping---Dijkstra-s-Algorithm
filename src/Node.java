import java.util.HashMap;
import java.util.Map;

/** One Node of the graph, complete with mappings to neighboring Nodes */
public class Node implements Comparable<Node>{
	
	public final String ID;
	public double dist = Double.MAX_VALUE; // MAX_VALUE assumed to be infinity
	public Node previous = null;
	public final Map<Node, Double> neighbours = new HashMap<>();
	public double lat, lon; //latitude and longitude

	public Node(String ID, double lat, double lon)
	{
		this.ID = ID;
		this.lat = lat;
		this.lon = lon;
	}

	public void printPath()
	{
		if (this == this.previous)
		{
			System.out.printf("%s", this.ID);
		}
		else if (this.previous == null)
		{
			System.out.printf("%s(no path)", this.ID);
		}
		else
		{
			this.previous.printPath();
			System.out.printf(" -> %s (%f miles)", this.ID, this.dist);

		}
	}

	public int compareTo(Node other)
	{
		if (dist == other.dist)
			return ID.compareTo(other.ID);

		return Double.compare(dist, other.dist);
	}

	@Override public String toString()
	{
		return "(" + ID + ", " + dist + ")";
	}
}