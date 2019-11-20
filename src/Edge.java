   /** One edge of the graph (only used by Graph constructor) */

public class Edge {
	
	String ID;
    public final String n1, n2;
    public final double dist;
    
    public Edge(String ID, String n1, String n2, double dist) {
    	this.ID = ID;
        this.n1 = n1;
        this.n2 = n2;
        this.dist = dist;
    }
    
   }