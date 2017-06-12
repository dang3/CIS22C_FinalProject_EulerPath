import java.util.Iterator;
import java.util.Map;

public class EulerPath<E> extends Graph<E> {
	
	/*
	 * According to Euler: If a graph has more than 2 vertices of odd degree, there cannot be an Euler Path
	 * Conditions for Euler path: Number of odd vertices must be two
	 * 
	 * 
	 * Need something to keep track number of edges
	 * First check if graph has an Euler path, within a method:
	 * 		a)	Have local counter variable initially set to 0, keeps track of 
	 * 		   	number of odd vertices
	 * 		b) 	Iterate through the vertex set in Graph.java checking the number
	 * 		   	of edges for each vertex
	 * 		c) 	If, at the end of the method, there exists exactly 2 odd vertices,
	 * 			than an Euler path exists, continue with the program
	 * 		d) 	Otherwise let the user know that an Euler path doesn't exist, quit program
	 *
	 *
	 *	Need:
	 *	1) ArrayList<Pair> to keep track of traversals, prevent going back to same edge
	 *	2) Stack<Vertex> to keep track of vertices
	 *	3) Queue<Vertex> end result of algorithm, shows where an Euler path exists
	 *
	 * 
	 *  Algorithm for finding Euler path:
	 *  	a)	Declare an empty stack and queue 
	 *  	b) 	If there are any odd vertices, start at those vertices
	 *  	c) 	If no odd vertices, start at any vertex
	 *  	d) 	From current vertex, pick any neighbor to go to. Remove the edge connecting the neighbor to
	 *  		the current vertex
	 *  			- If all edges have been traversed, add current vertex to queue, pop from
	 *  			stack and set as current vertex
	 *  			- Else add current vertex to stack, go to any neighbor, remove
	 *  			edge between current vertex and selected neighbor 
	 *  	e)  Repeat d until stack is empty
	 *  	f) 	Return the queue of vertices, the queue shows Euler path
	 *  	g)	Since this algorithm deleted edges, need to rebuild graph at the end in case
	 *  		user wants to find another Euler path
	 */
	
	
	private LinkedQueue<Vertex<E>> eulerPathQueue;
	private Vertex<E> startingVertex;
	private LinkedStack<Vertex<E>> traversalStack;
	
	public EulerPath() {
		eulerPathQueue = new LinkedQueue<>();
		traversalStack = new LinkedStack<>();
	}
	
	// Makes the graph from reading input file
	private void makeGraph() {
		
	}
	
	
	public void checkForEulerPath() {
		// Check to see if an Euler Path exists
		if( !hasEulerPath() ) {
			System.out.println("An Euler Path does not exist for the current graph");
			return;
		}
		else {
			findEulerPath();
			showEulerPath();
		}
	}
	
	protected void findEulerPath() {
		Vertex<E> currentVertex = startingVertex;
		Vertex<E> neighbor;
		do {
			neighbor = getANeighbor(currentVertex);
			if( neighbor == null ) {
				eulerPathQueue.enqueue(currentVertex);
				currentVertex = traversalStack.pop();
			}
			else {
				traversalStack.push(currentVertex);
				currentVertex = neighbor;
			}
		} while(traversalStack.isEmpty());
	}
	
	private Vertex<E> getANeighbor(Vertex<E> currentVertex) {
		// Iterates through currentVertex's adjList HashMap, return the first Pair objects
		Iterator<Pair<Vertex<E>,Double>> currentVertexAdjItr = currentVertex.adjList.values().iterator();
		Pair<Vertex<E>, Double> neighborPair = null;
		Vertex<E> neighborVertex = null;
		
		// If a neighbor for currentVertex exists, return it, otherwise return null
		// will return the first available neighbor
		if(currentVertexAdjItr.hasNext()) {
			neighborPair = currentVertexAdjItr.next();
			neighborVertex = neighborPair.first;
			// Removes neighborVertex from currentVertex's adjList and vice versa, this removes the edge
			// connecting both vertices, to prevent traversing the same edge more than once
			currentVertex.adjList.remove(neighborVertex.data);	
			neighborVertex.adjList.remove(currentVertex.data);
		}
		return neighborVertex;
	}
	
	
	// Checks to see if an Euler path exists
	protected boolean hasEulerPath() {
		int numOddVertices = 0;
		
		// iterates through vertices of vertexSet
		Iterator<Vertex<E>> vertexSetItr = vertexSet.values().iterator(); 
		// iterates through each vertex's adjacency list
		Iterator<Map.Entry<E, Pair<Vertex<E>, Double>>> vertexAdjItr;
		
		while(vertexSetItr.hasNext()) {
			int numEdges = 0;	// keeps track of the number of edges for each vertex
								// Resets for the next vertex in the vertexSet
			Vertex<E> currentVertex = vertexSetItr.next();	// Get next vertex in vertexSet
			vertexAdjItr = currentVertex.iterator();	// Get iterator for current vertex's adj list
			while(vertexAdjItr.hasNext())
				numEdges++;	// increase for every entry in adj list
			
			if(numEdges%2 != 0)	{// increase if number of edges for current vertex is odd
				numOddVertices++;
				startingVertex = currentVertex;	// To start traversal at an odd vertex
			}
		}
		// Euler path for a graph exists if number of odd vertices for given graph is exactly 2
		if(numOddVertices == 2)
			return true;	
		else
			return false;
	}
	

	public void showEulerPath() {
		Vertex<E> currentVertex;
		System.out.print("Euler Path Vertices: ");
		while(!eulerPathQueue.isEmpty()) {
			currentVertex = eulerPathQueue.dequeue();
			System.out.print( currentVertex.getData() + " " );
		}
		System.out.println();
	}
	
	
	

}
