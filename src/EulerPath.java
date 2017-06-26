import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class EulerPath<E> extends Graph<E> {
	private LinkedStack<Vertex<E>> traversalStack;
	private LinkedQueue<Vertex<E>> eulerPathQueue;
	private ArrayList<Vertex<E>> startingVertices;
	private ArrayList<Edge<E>> edgeList;
	private LinkedStack<Pair<E,E>> removedEdges;
	private Random rand;
	
	public EulerPath() {
		traversalStack = new LinkedStack<>();
		startingVertices = new ArrayList<>();
		eulerPathQueue = new LinkedQueue<>();
		edgeList = new ArrayList<>();
		removedEdges = new LinkedStack<>();
		rand = new Random();
	}

	// Check for Euler path, if it exists find it
	public void checkForEulerPath() {
		if (!hasEulerPath()) {
			System.out.println("An Euler Path does not exist for the current graph");
			return;
		} else {
			findEulerPath();
			showEulerPath();
		}
	}

	// Check to see if an Euler path exists. Iterate through each vertex's adjList and count
	// the number of edges. Look for odd number of edges
	public boolean hasEulerPath() {
		int numOddVertices = 0;
		Iterator<Vertex<E>> vertexSetItr = vertexSet.values().iterator(); // Iterate through the graph's vertexSet
		Iterator<Map.Entry<E, Pair<Vertex<E>, Double>>> vertexAdjItr; // Iterate through each vertex's adjList

		while (vertexSetItr.hasNext()) {
			int numEdges = 0; 
			Vertex<E> currentVertex = vertexSetItr.next(); 
			vertexAdjItr = currentVertex.iterator();
			while (vertexAdjItr.hasNext()) {
				numEdges++; // increase for every entry in currentVertex's adjList
				vertexAdjItr.next();
			}
			if (numEdges % 2 != 0) { 
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

	// Start at any odd vertex, call it currentVertex. Get a neighbor at currentVertex. If no neighbor exists, 
	// enqueue currentVertex, pop from the stack and call it currentVertex. Else push currentVertex and call
	// neighborVertex currentVertex. Repeat until the stack is empty && currentVertex has no un-traversed edges
	protected void findEulerPath() {
		int startIndex = getRandIndex(startingVertices);
		Vertex<E> currentVertex = startingVertices.get(startIndex);
		Vertex<E> neighborVertex;
		boolean done = false; // Exit flag
	
		do {
			neighborVertex = getANeighbor(currentVertex); 
			if (neighborVertex == null) {
				eulerPathQueue.enqueue(currentVertex);
				currentVertex = traversalStack.pop();
				// exit condition
				if(traversalStack.isEmpty() && !hasANeighbor(currentVertex)) {
					eulerPathQueue.enqueue(currentVertex);
					done = true;
				}
			} else {
				traversalStack.push(currentVertex);
				currentVertex = neighborVertex;
			}
		} while (!done);
	}
	
	// Check if currentVertex has any more neighbors by checking the edgeList if it contains any instances
	// of currentVertex
	protected boolean hasANeighbor(Vertex<E> currentVertex) {
		int numNeighbors = currentVertex.adjList.size();	// number of currentVertex's available neighbors
		int neighborsUsed = 0;	// number of currentVertex's neighbors that have been visited 
		
		for(int i = 0; i < edgeList.size(); i++) {
			Edge<E> currentEdge = edgeList.get(i);
			if( currentVertex.equals(currentEdge.getFirstVertex()) || currentVertex.equals(currentEdge.getSecondVertex()) )
				neighborsUsed++;
		}
		return numNeighbors != neighborsUsed;
	}
	
	// Randomly returns a neighborVertex from currentVertex
	protected Vertex<E> getANeighbor(Vertex<E> currentVertex) {
		ArrayList<Vertex<E>> neighborsList = new ArrayList<>(); // Stores currentVertex's neighbors
		Iterator<Pair<Vertex<E>, Double>> currentVertexAdj_Itr = currentVertex.adjList.values().iterator();
		Vertex<E> neighborVertex = null;
		int index;

		while (currentVertexAdj_Itr.hasNext()) {
			Vertex<E> cVertex = currentVertexAdj_Itr.next().first;
			neighborsList.add(cVertex);
		}

		while (true) {
			if (neighborsList.isEmpty()) 
				return null;
		
			index = getRandIndex(neighborsList);
			neighborVertex = neighborsList.get(index);
			Edge<E> newEdge = new Edge<>(currentVertex, neighborVertex);
			
			if (edgeList.contains(newEdge)) {
				neighborsList.remove(index);
			} else {
				edgeList.add(newEdge);
				return neighborVertex;
			}
		}
	}

	public void showEulerPath() {
		Vertex<E> currentVertex;
		System.out.println("Euler Path Vertices:");
		int counter = 0;
		while (!eulerPathQueue.isEmpty()) {
			currentVertex = eulerPathQueue.dequeue();
			counter++;
			System.out.println(counter + ". " + currentVertex.getData());
		}
		System.out.println();
	}

	// A helper method, returns a random index from an ArrayList passed into this method
	// Enables different Euler paths to be found for a given graph
	private int getRandIndex(ArrayList<Vertex<E>> verticesList) {
		return rand.nextInt(verticesList.size());
	}
	
	public boolean remove(E start, E end)//Jae {
		boolean removedOK = super.remove(start, end);
		if (removedOK)
			removedEdges.push(new Pair<E, E>(start, end));
		return removedOK;
	}

	protected void undoRemoval()//Jae {
		undoRemoval(1);
	}

	protected void undoRemoval(int times)//Jae {
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
}
