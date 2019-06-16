import java.util.LinkedList;
import java.util.List;

public class VertexMETA {

	private VertexMETA prev;
	private String id;

	private List<Edge> edges = new LinkedList<Edge>();
	
	
	public VertexMETA(String id, List<Edge> edges) {
		this.id = id;
	 	this.edges = edges;
	}
	
	public VertexMETA(String id) {
		this.id = id;
	}
	
	public String getID() {
		return id;
	}
	
	public void addEdges(Edge e) {
		edges.add(e);
	}
	
	public List<Edge> getEdges() {
		return edges;
	}
	
	public void setPrev(VertexMETA v) {
		this.prev = v;
	}
	
	public VertexMETA getPrev() {
		return prev;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VertexMETA other = (VertexMETA) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
