import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class EulerPath<E> extends Graph<E> {

	/*
	 * According to Euler: If a graph has more than 2 vertices of odd degree,
	 * there cannot be an Euler Path Conditions for Euler path: Number of odd
	 * vertices must be two
	 * 
	 * 
	 * Need something to keep track number of edges First check if graph has an
	 * Euler path, within a method: a) Have local counter variable initially set
	 * to 0, keeps track of number of odd vertices b) Iterate through the vertex
	 * set in Graph.java checking the number of edges for each vertex c) If, at
	 * the end of the method, there exists exactly 2 odd vertices, than an Euler
	 * path exists, continue with the program d) Otherwise let the user know
	 * that an Euler path doesn't exist, quit program
	 *
	 *
	 * Need: 1) ArrayList<Pair> to keep track of traversals, prevent going back
	 * to same edge 2) Stack<Vertex> to keep track of vertices 3) Queue<Vertex>
	 * end result of algorithm, shows where an Euler path exists
	 *
	 * 
	 * Algorithm for finding Euler path: a) Declare an empty stack and queue b)
	 * If there are any odd vertices, start at those vertices c) If no odd
	 * vertices, start at any vertex d) From current vertex, pick any neighbor
	 * to go to. Remove the edge connecting the neighbor to the current vertex -
	 * If all edges have been traversed, add current vertex to queue, pop from
	 * stack and set as current vertex - Else add current vertex to stack, go to
	 * any neighbor, remove edge between current vertex and selected neighbor e)
	 * Repeat d until stack is empty f) Return the queue of vertices, the queue
	 * shows Euler path g) Since this algorithm deleted edges, need to rebuild
	 * graph at the end in case user wants to find another Euler path
	 */

	private LinkedStack<Vertex<E>> traversalStack;
	private LinkedStack<Vertex<E>> eulerPathStack;
	private ArrayList<Vertex<E>> startingVertices;
	private ArrayList<Edge<E>> edgeList;
	private Random rand;

	
	private ArrayList<Vertex<E>> vertices = new ArrayList<>();
	
	public EulerPath() {
		traversalStack = new LinkedStack<>();
		startingVertices = new ArrayList<>();
		eulerPathStack = new LinkedStack<>();
		edgeList = new ArrayList<>();
		rand = new Random();
	}

	public void checkForEulerPath() {
		// Check to see if an Euler Path exists
		if (!hasEulerPath()) {
			System.out.println("An Euler Path does not exist for the current graph");
			return;
		} else {
			// If an Euler path exists, find a path then display it
			findEulerPath();
			showEulerPath();
		}
	}

	// Check to see if an Euler path exists. Iterate through each vertex's adjList and count
	// the number of edges. Look for odd number of edges
	protected boolean hasEulerPath() {
		int numOddVertices = 0;
		Iterator<Vertex<E>> vertexSetItr = vertexSet.values().iterator(); // Iterate through the graph's vertexSet
		Iterator<Map.Entry<E, Pair<Vertex<E>, Double>>> vertexAdjItr; // Iterate through each vertex's adjList

		while (vertexSetItr.hasNext()) {
			int numEdges = 0; // To count the number of edges for each vertex
			Vertex<E> currentVertex = vertexSetItr.next(); // Get next vertex in vertexSet
			vertices.add(currentVertex);
			vertexAdjItr = currentVertex.iterator(); // Get iterator for currentvertex's adjList
			while (vertexAdjItr.hasNext()) {
				numEdges++; // increase for every entry in currentVertex's adjList
				vertexAdjItr.next();
			}
			if (numEdges % 2 != 0) { // increase if number of edges for current vertex is odd
				numOddVertices++;
				// Add to startingVertices list if currentVertex has an odd number of edges
				startingVertices.add(currentVertex); 
			}
		}
		// Euler path for a graph exists if number of odd vertices for given graph is exactly 2
		if (numOddVertices == 2)
			return true;
		else
			return false;
	}

	protected void findEulerPath() {
		// From the list of startingVertices, pick one randomly to begin
		int startIndex = getRandIndex(startingVertices);
		Vertex<E> currentVertex = startingVertices.get(startIndex);
		Vertex<E> neighborVertex;
		boolean done = false; // Exit flag
	
		do {
			neighborVertex = getANeighbor(currentVertex); // Get a neighbor vertex from currentVertex
			if (neighborVertex == null) {	// if currentVertex doesn't have a neighbor
				eulerPathStack.push(currentVertex);
				currentVertex = traversalStack.pop();
				// Exit condition: if the traversalStack is empty && currentVertex does not have a un-traversed edge
				if(traversalStack.isEmpty() && !hasANeighbor(currentVertex)) {
					eulerPathStack.push(currentVertex);
					done = true;
				}
			} else {
				// else if currentVertex does have a neighbor
				traversalStack.push(currentVertex);
				currentVertex = neighborVertex;
			}
		} while (!done);
	}
	
	protected boolean hasANeighbor(Vertex<E> currentVertex) {
		int numNeighbors = currentVertex.adjList.size();
		int neighborsUsed = 0;
		
		for(int i = 0; i < edgeList.size(); i++) {
			Edge<E> currentEdge = edgeList.get(i);
			if( currentVertex.equals(currentEdge.getFirstVertex()) || currentVertex.equals(currentEdge.getSecondVertex()) )
				neighborsUsed++;
		}
		return numNeighbors != neighborsUsed;
	}
	
	protected Vertex<E> getANeighbor(Vertex<E> currentVertex) {
		ArrayList<Vertex<E>> neighborsList = new ArrayList<>(); // Stores currentVertex's neighbors
		Iterator<Pair<Vertex<E>, Double>> currentVertexAdj_Itr = currentVertex.adjList.values().iterator();
		Vertex<E> neighborVertex = null;
		int index;

		// Iterate through currentVertex's hashMap and populate listOfNeighbors
		while (currentVertexAdj_Itr.hasNext()) {
			Vertex<E> cVertex = currentVertexAdj_Itr.next().first;
			neighborsList.add(cVertex);
		}

		while (true) {
			// If currentVertex's edges have all been traversed
			if (neighborsList.isEmpty()) 
				return null;
		
			// Get a random index from neighborsList
			index = getRandIndex(neighborsList);
			neighborVertex = neighborsList.get(index);
			
			// Make new edge with currentVertex and neighborVertex
			Edge<E> newEdge = new Edge<>(currentVertex, neighborVertex);
			
			// If edgeList already contains newEdge, remove the neighborVertex from neighborsList in order to 
			// prevent the same neighborVertex from being selecteed
			if (edgeList.contains(newEdge)) {
				neighborsList.remove(index);
			} else {
			// else if edgeList does not contain newEdge, add it and return neighborVertex
				edgeList.add(newEdge);
				return neighborVertex;
			}
		}
	}

	public void showEulerPath() {
		Vertex<E> currentVertex;
		System.out.print("Euler Path Vertices: ");
		while (!eulerPathStack.isEmpty()) {
			currentVertex = eulerPathStack.pop();
			System.out.print(currentVertex.getData() + " ");
		}
		System.out.println();
	}

	// A helper method, returns a random index from an ArrayList passed into this method
	// Enables different Euler paths to be found for a given graph
	private int getRandIndex(ArrayList<Vertex<E>> verticesList) {
		return rand.nextInt(verticesList.size());
	}
}
