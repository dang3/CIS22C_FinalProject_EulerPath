// Edges are represented by a pair of Vertices 
public class Edge<E> {
	public Vertex<E> firstVertex;
	public Vertex<E> secondVertex;
	
	public Edge(Vertex<E> src, Vertex<E> dest) {
		firstVertex = src;
		secondVertex = dest;
	}
	
	// Since our graphs are undirected, an edge connecting vertices A-->B is the same as B-->A, do not
	// want to double count
	public boolean equals(Object rhs) {
		Edge<E> rightOp = (Edge<E>) rhs;
		if( (firstVertex.equals( rightOp.firstVertex ) && secondVertex.equals(rightOp.secondVertex)) || 
			(firstVertex.equals(rightOp.secondVertex) && secondVertex.equals(rightOp.firstVertex)))
			return true;
		else
			return false;
	}
	   
	public Vertex<E> getFirstVertex() {
		return firstVertex;
	}
	
	public Vertex<E> getSecondVertex() {
		return secondVertex;
	}
	
	public String toString() {
		return firstVertex.data + "" + secondVertex.data;
	}
}
