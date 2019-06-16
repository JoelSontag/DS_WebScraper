
public class Edge {
	
	private VertexMETA v1;
	private VertexMETA v2;
	int weight;

	public Edge(VertexMETA v2, int weight) {
		this.v2 = v2;
		this.weight = weight;
	}
	public VertexMETA getVertex2() {
		return v2;
	}
	
	public VertexMETA getVertex1() {
		return v1;
	}
	
	public void setVertex2(VertexMETA v2) {
		this.v2 = v2;
	}
	
	public void setVertex1(VertexMETA v1) {
		this.v1 = v1;
	}
	
	public int getWeight() {
		return weight;
	}
}
