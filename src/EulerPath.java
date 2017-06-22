import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class EulerPath<E> extends Graph<E> {

	/*
	 * Algorithm for finding Euler path: 
	 a) Declare an empty stack and queue 
	 b) Start at any odd vertex
	 d) From current vertex, pick any neighbor to go to. Remove the edge connecting the 
	 neighbor to the current vertex 
	 	-If all edges have been traversed, add current vertex to queue, pop from
	 	 stack and set as current vertex 
		 - Else add current vertex to stack, go to any neighbor, remove edge between 
		  current vertex and selected neighbor
	e) Repeat d until stack is empty and currentVertex has no more neighbors
	 */

	private LinkedStack<Vertex<E>> traversalStack;
	private LinkedQueue<Vertex<E>> eulerPathQueue;
	private ArrayList<Vertex<E>> startingVertices;
	private ArrayList<Edge<E>> edgeList;
	private Random rand;

	
	private ArrayList<Vertex<E>> vertices = new ArrayList<>();
	
	public EulerPath() {
		traversalStack = new LinkedStack<>();
		startingVertices = new ArrayList<>();
		eulerPathQueue = new LinkedQueue<>();
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
				eulerPathQueue.enqueue(currentVertex);
				currentVertex = traversalStack.pop();
				// Exit condition: if the traversalStack is empty && currentVertex does not have a un-traversed edge
				if(traversalStack.isEmpty() && !hasANeighbor(currentVertex)) {
					eulerPathQueue.enqueue(currentVertex);
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
		while (!eulerPathQueue.isEmpty()) {
			currentVertex = eulerPathQueue.enqueue();
			System.out.print(currentVertex.getData() + " ");
		}
		System.out.println();
	}

	// A helper method, returns a random index from an ArrayList passed into this method
	// Enables different Euler paths to be found for a given graph
	private int getRandIndex(ArrayList<Vertex<E>> verticesList) {
		return rand.nextInt(verticesList.size());
	}
	
	//_____________________________________________________________
		public boolean remove(E start, E end) {
		boolean removedOK = super.remove(start, end);
		if (removedOK)
			removedEdges.push(new Pair<E, E>(start, end));
		return removedOK;
	}

	protected void undoRemoval() {
		undoRemoval(1);
	}

	protected void undoRemoval(int times) {
		if (removedEdges.size() == 0) {
			System.out.println("\tThere Is Nothing To Undo");
			return;
		}
		if (times < 1) {
			System.out.println("\tNumber of Undos Cannot Be Less Than 1");
			return;
		}
		Pair<E, E> currentEdge;
		if (times >= removedEdges.size())
			times = removedEdges.size();
		while (times > 0) {
			currentEdge = removedEdges.pop();
			addEdge(currentEdge.first, currentEdge.second, 0);
			System.out.println(
					"\tSuccessfully Undid Edge Removal Between " + currentEdge.first + " And " + currentEdge.second);
			--times;
		}
	}
		public void saveEulerPath(PrintWriter printWriter) throws IOException {
		if (eulerPath.getLength() > 0) {
			final BufferedWriter bufferedWriter = new BufferedWriter(printWriter);
			bufferedWriter.write("ONE EULER PATH IS");
			bufferedWriter.newLine();
			eulerPath.traversePath(new PathStrategy<Vertex<E>>() {
				@Override
				public void implement(Vertex<E> firstNode, Vertex<E> secondNode) {
					try {
						bufferedWriter.write("+ " + firstNode.getData());
						bufferedWriter.newLine();
					} catch (IOException ex) {
						System.out.println("\tFailed To Save Graph As Text File");
					}
				}
			});
			bufferedWriter.write("+ " + eulerPath.getEntry(eulerPath.getLength()).getData());
			bufferedWriter.close();
		}
	}

}
