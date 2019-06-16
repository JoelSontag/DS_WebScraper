import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class PrimsMST {
	
	private HashMap<String, VertexMETA> graph = new HashMap<String, VertexMETA>();
	PriorityQueue<Edge> sortedEdges = new PriorityQueue<Edge>(new WeightComparator());
	HashSet<String> unvisited = new HashSet<String>();
	HashSet<String> visited = new HashSet<String>();
	
	public void addVertex(VertexMETA v) {
		if(graph.containsKey(v.getID())) {
			String id = v.getID();
			v = graph.get(id);
		}
		else {
			graph.put(v.getID(), v);
		}
		for(Edge e : v.getEdges()) {
			e.setVertex1(v);
			if(graph.containsKey(e.getVertex2().getID()))
				e.setVertex2(graph.get(e.getVertex2().getID()));
		}
	}
	
	public void findMST() {
		unvisited.addAll(graph.keySet());
		Queue<Edge> mst = new LinkedList<Edge>();
		for(VertexMETA v : graph.values())
			sortedEdges.addAll(v.getEdges());
		while(!unvisited.isEmpty()) {
			Edge edge = sortedEdges.stream().filter(e -> !visited.contains(e.getVertex2().getID())).findFirst().orElse(null);
			mst.add(edge);
			sortedEdges.remove(edge);
			unvisited.remove(edge.getVertex2().getID());
			visited.add(edge.getVertex2().getID());
		}
		int counter = 0;
		System.out.println("Starting vertex:\n" + mst.peek().getVertex1().getID());
		for (Edge edge : mst) {
			counter++;
			System.out.println("Edge " + counter + ":\n --- " + edge.getWeight() + " --- "+ edge.getVertex2().getID());
		}
	}
}
