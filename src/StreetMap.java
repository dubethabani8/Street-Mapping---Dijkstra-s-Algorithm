import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StreetMap extends JComponent implements ActionListener{
	
	public Graph map;
	boolean show;
	JFrame frame;
	JPanel panel;
	String start;
	String end;
	JComboBox<String> sourcesM, destM, mapsM;
	String maps[] = new String[3];
	//StreetMap m;
	String inputFile;
	
	public StreetMap() throws Exception {
		frame = new JFrame();
		panel = new JPanel();
	}
	
	public void create(String inputFile) throws Exception {
		panel.removeAll();
		this.map = new Graph(inputFile);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setSize(1200, 1000);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.add(panel, BorderLayout.NORTH);
		frame.setBackground(Color.WHITE);
		//frame.setVisible(true);
		switch(inputFile) {
		case "nys.txt":
			maps[0] = "New York State";
			maps[2] = "Monroe County";
			maps[1] = "University of Rochester";
			break;
		case "monroe.txt":
			maps[2] = "New York State";
			maps[0] = "Monroe County";
			maps[1] = "University of Rochester";
			break;
		case "ur.txt":
			maps[2] = "New York State";
			maps[1] = "Monroe County";
			maps[0] = "University of Rochester";
			break;
		}
		
		panel.setBackground(Color.CYAN);
		this.mapsM = new JComboBox<String>(maps);
		JButton mapsB = new JButton("Change Map");
		this.sourcesM = new JComboBox<String>(map.locations);
		this.destM = new JComboBox<String>(map.locations);
		JLabel sourceL = new JLabel("Get directions from: ");
		JLabel destL = new JLabel(" to: ");
		JButton okB = new JButton("Get directions");
		okB.addActionListener(this);
		mapsB.addActionListener(this);
		panel.add(mapsM);
		panel.add(mapsB);
		panel.add(sourceL);
		panel.add(sourcesM);
		panel.add(destL);
		panel.add(destM);
		panel.add(okB);
		repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Get directions")) {
		start = sourcesM.getSelectedItem().toString();
		end = destM.getSelectedItem().toString();
		map.dijkstra(start);
		System.out.println("DIRECTIONS FROM " + start + " TO " + end + ": ");
		System.out.println();
		map.printPath(end);
		repaint();
		}
		else if(e.getActionCommand().equals("Change Map")) {
			String mp = mapsM.getSelectedItem().toString();
			String inputFile = null;;
			switch(mp) {
			case "New York State":
				inputFile = "nys.txt";
				break;
			case "Monroe County":
				inputFile = "monroe.txt";
				break;
			case "University of Rochester":
				inputFile = "ur.txt";
				break;
			}
			System.out.println("inputFile "+ inputFile);
			try {
				create(inputFile);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		frame.setVisible(true);
		
	}


	
	@Override
	public void paintComponent(Graphics g) {
		int w = getWidth();
		int h = getHeight();
		
		g.setColor(Color.GREEN);

		for(Edge e: map.edges) {
			Node n1 = map.graph.get(e.n1);
			Node n2 = map.graph.get(e.n2);
			//For intersection 1:
			double n1Lat = n1.lat;
			double n1Lon = n1.lon;
			double n1RealX = map.getDistance(map.maxLat, map.minLon, map.maxLat, n1Lon);
			double n1RealY = map.getDistance(map.maxLat, map.minLon, n1Lat, map.minLon);
			double n1ScaledX = (n1RealX/map.realWidth) * w;
			double n1ScaledY = (n1RealY/map.realHeight) * h;
			//For intersection 2:
			double n2Lat = n2.lat;
			double n2Lon = n2.lon;
			double n2RealX = map.getDistance(map.maxLat, map.minLon, map.maxLat, n2Lon);
			double n2RealY = map.getDistance(map.maxLat, map.minLon, n2Lat, map.minLon);
			double n2ScaledX = (n2RealX/map.realWidth) * w;
			double n2ScaledY = (n2RealY/map.realHeight) * h;
			//Draw edge from n1 to n2:
			g.drawLine((int)n1ScaledX, (int)n1ScaledY, (int)n2ScaledX, (int)n2ScaledY);
			
		}
		//Draw directions
		if(!(end != null && start != null && (map.graph.containsKey(end) && map.graph.containsKey(start))))
			frame.setVisible(true);
		else{
			g.setColor(Color.RED);
			Node start = map.graph.get(this.start);
			Node current = map.graph.get(this.end);
			do {
				Node previous = null;
				if(current.previous != null)
					previous = current.previous;
				else
					break;
				//For prev
				double pLat = previous.lat;
				double pLon = previous.lon;
				double pRealX = map.getDistance(map.maxLat, map.minLon, map.maxLat, pLon);
				double pRealY = map.getDistance(map.maxLat, map.minLon, pLat, map.minLon);
				double pScaledX = (pRealX/map.realWidth) * w;
				double pScaledY = (pRealY/map.realHeight) * h;
				//For current
				double cLat = current.lat;
				double cLon = current.lon;
				double cRealX = map.getDistance(map.maxLat, map.minLon, map.maxLat, cLon);
				double cRealY = map.getDistance(map.maxLat, map.minLon, cLat, map.minLon);
				double cScaledX = (cRealX/map.realWidth) * w;
				double cScaledY = (cRealY/map.realHeight) * h;
				//Draw connecting line
				g.drawLine((int)pScaledX, (int)pScaledY, (int)cScaledX, (int)cScaledY);
				//Next
				current = previous;
			}
			while(current != start);
		}
		/*Find largest separations of latitudes and longitudes using my function and scale
		 * that for length and width*/
		
	}

	public static void main(String[] args) throws Exception {
		System.out.println();
		String inputFile;
		if(args.length != 0)
			inputFile = args[0]; //get from args[]
		else
			inputFile = "ur.txt";
		StreetMap m = new StreetMap();
		m.create(inputFile);
		if(args.length == 0) m.frame.setVisible(true);
		if(args.length != 0) {
			if(args[1].equals("--show") && args.length <= 2) {
				m.frame.setVisible(true);
			}
			else if((args[1].equals("--directions") && args[2].equals("--show")) || args[1].equals("--show")) {
				m.start = args[3];
				m.end = args[4];
				m.map.dijkstra(m.start);
				System.out.println("DIRECTIONS FROM " + m.start + " TO " + m.end + ": ");
				System.out.println();
				m.map.printPath(m.end);
				m.frame.setVisible(true);
			}
			else {
				m.start = args[2];
				m.end = args[3];
				m.map.dijkstra(m.start);
				System.out.println("DIRECTIONS FROM " + m.start + " TO " + m.end + ": ");
				System.out.println();
				m.map.printPath(m.end);
				m.create(inputFile);
				//m.repaint();
				m.setVisible(false);
			}
		}
		System.out.println();
		

	}




}
