import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Driver {
	private static Scanner userScanner = new Scanner(System.in);
	private static HashMap<String, Location> availableLocations;
	private static EulerPath<Location> eulerPath;
	private static Visitor<Location> locationVisitor = new LocationVisitor();

	public static void main(String[] args) throws IOException {
		int userChoice = 0;
		while (userChoice != 10) {
			userChoice = showMenu();
			navigateOption(userChoice);
			System.in.read();
		}
	}

	public static HashMap<String, Location> readInputFile() {
		Scanner inputFile = openInputFile();
		if (inputFile == null) {
			System.out.println("\tUnable To Open Input File");
			System.exit(0);
		}
		System.out.println("\tSuccessfully Opened Input File");
		if (inputFile.hasNext() == false) {
			System.out.println("\tInput File Is Empty");
			System.exit(0);
		}
		HashMap<String, Location> availableLocations = new HashMap<>();
		Location currentLocation;
		while (inputFile.hasNext()) {
			String[] vertices = inputFile.nextLine().split("[,]");
			currentLocation = availableLocations.get(vertices[0]);
			if (currentLocation == null) {
				currentLocation = new Location(vertices[0]);
				availableLocations.put(vertices[0], currentLocation);
			}
			for (int i = 1; i < vertices.length; i++) {
				Location adjacentLocation = availableLocations.get(vertices[i]);
				if (adjacentLocation == null) {
					adjacentLocation = new Location(vertices[i]);
					availableLocations.put(vertices[i], adjacentLocation);
				}
				eulerPath.addEdge(currentLocation, adjacentLocation, 0);
			}
		}
		return availableLocations;
	}

	public static int showMenu() throws IOException {
		int trialLimit = 3;
		for (;;) {
			System.out.println("\n\t-----------------------------------------------------");
			System.out.println("\tType A Number To Go To The Option");
			System.out.println("\t\t1. Read Input File");
			System.out.println("\t\t2. Add Edge");
			System.out.println("\t\t3. Remove Edge");
			System.out.println("\t\t4. Undo Removal");
			System.out.println("\t\t5. Display Graph");
			System.out.println("\t\t6. Depth First Traversal");
			System.out.println("\t\t7. Breadth First Traversal");
			System.out.println("\t\t8. Find Euler Path");
			System.out.println("\t\t9. Save as Text File");
			System.out.println("\t\t10. Exit");
			System.out.println();
			System.out.print("\tYour Choice: ");
			userScanner = new Scanner(System.in);
			try {
				int userChoice = userScanner.nextInt();
				if (userChoice < 1 || userChoice > 10)
					System.out.println("\n\tInvalid Number! Please Type Another Number.");
				else
					return userChoice;
			} catch (InputMismatchException exception) {
				trialLimit--;
				if (trialLimit == 0) {
					System.out.println("\n\tYou Have Exceeded Invalid Input Trial Limits!");
					System.out.println("\tThe Program Is Forced To End");
					System.exit(0);
				}
				System.out.println("\n\tInvalid Input! You Can Type Again " + trialLimit + " Times.");
				System.in.read();
			}
		}
	}

	public static void navigateOption(int option) throws IOException {
		switch (option) {
		case 1:
			eulerPath = new EulerPath<>();
			userScanner.nextLine();
			availableLocations = readInputFile();
			break;
		case 2:
			if (eulerPath != null)
				addingEdge();
			else
				System.out.println("\tYou have to read input file first");
			break;
		case 3:
			if (eulerPath != null)
				removingEdge();
			else
				System.out.println("\tYou have to read input file first");
			break;
		case 4:
			if (eulerPath != null)
				undoRemovingEdge();
			else
				System.out.println("\tYou have to read input file first");
			break;
		case 5:
			if (eulerPath != null)
				displayingGraph();
			else
				System.out.println("\tYou have to read input file first");
			break;
		case 6:
			if (eulerPath != null)
				depthFirstTraversal();
			else
				System.out.println("\tYou have to read input file first");
			break;
		case 7:
			if (eulerPath != null)
				breadthFirstTraversal();
			else
				System.out.println("\tYou have to read input file first");
			break;
		case 8:
			if (eulerPath != null)
				findingEulerPath();
			else
				System.out.println("\tYou have to read input file first");
			break;
		case 9:
			if (eulerPath != null)
				saveEulerPathAsTextFile();
			else
				System.out.println("\tYou have to read input file first");
			break;
		case 10:
			System.out.println("\tThank You For Using The Program");
			break;
		}
	}

	public static void addingEdge() {
		System.out.println("\t-----------------------------------------------------");
		System.out.println("\t\tAdd Edge\n");
		userScanner.nextLine();
		System.out.print("\n\tEnter Location 1: ");
		String location1 = userScanner.nextLine();
		if (!availableLocations.containsKey(location1)) {
			System.out.println("\n\tFailed To Add Edge.\n\t\"" + location1 + "\" Is Not In The Graph.");
			return;
		}
		System.out.print("\n\tEnter Location 2: ");
		String location2 = userScanner.nextLine();
		if (!availableLocations.containsKey(location2)) {
			System.out.println("\n\tFailed To Add Edge.\n\t\"" + location2 + "\" Is Not In The Graph.");
			return;
		}
		Location source = availableLocations.get(location1);
		Location destination = availableLocations.get(location2);
		eulerPath.addEdge(source, destination, 0);
		System.out.println("\n\tSuccessfully Added Edges Between \"" + location1 + "\" And \"" + location2 + "\"");
	}

	public static void removingEdge() {
		System.out.println("\t-----------------------------------------------------");
		System.out.println("\t\tRemove Edge\n");
		userScanner.nextLine();
		System.out.print("\n\tEnter Location 1: ");
		String location1 = userScanner.nextLine();
		if (!availableLocations.containsKey(location1)) {
			System.out.println("\tFailed To Add Edge.\n\t\"" + location1 + "\" Is Not In The Graph.");
			return;
		}
		System.out.print("\n\tEnter Location 2: ");
		String location2 = userScanner.nextLine();
		if (!availableLocations.containsKey(location2)) {
			System.out.println("\tFailed To Add Edge.\n\t\"" + location2 + "\" Is Not In The Graph.");
			return;
		}
		Location source = availableLocations.get(location1);
		Location destination = availableLocations.get(location2);
		if (eulerPath.remove(source, destination))
			System.out.println("\tSuccessfully Removed Edges Between \"" + location1 + "\" And \"" + location2 + "\"");
	}

	public static void undoRemovingEdge() throws IOException {
		System.out.println("\t-----------------------------------------------------");
		System.out.println("\t\tUndo Removing Edge\n");
		userScanner.nextLine();
		int trialLimit = 3;
		for (;;) {
			try {
				System.out.print("\n\tEnter Number of Times To Undo: ");
				int undoTimes = userScanner.nextInt();
				eulerPath.undoRemoval(undoTimes);
				return;
			} catch (InputMismatchException exception) {
				trialLimit--;
				if (trialLimit == 0) {
					System.out.println("\n\tYou Have Exceeded Invalid Input Trial Limits!");
					System.out.println("\tThe Program Is Forced To End");
					System.exit(0);
				}
				System.out.println("\n\tInvalid Input! You Can Type Again " + trialLimit + " Times.");
				System.in.read();
			}
		}
	}

	public static void displayingGraph() throws IOException {
		System.out.println("\t-----------------------------------------------------");
		System.out.println("\t\tDisplay Graph\n");
		System.out.println("\t____ ADJACENCY LISTS ____\n");
		eulerPath.showAdjTable();
	}

	public static void depthFirstTraversal() {
		System.out.println("\t-----------------------------------------------------");
		System.out.println("\t\tDemonstrate Traversal\n");
		userScanner.nextLine();
		System.out.print("\tEnter Starting Location: ");
		Location startLocation = availableLocations.get(userScanner.nextLine());
		if (startLocation != null) {
			System.out.print("\n\t___ DEPTH FIRST TRAVERSAL ___\n");
			eulerPath.depthFirstTraversal(startLocation, locationVisitor);
			((LocationVisitor) locationVisitor).resetLineBreakSignal();
		} else {
			System.out.println("\t\"" + startLocation + "\" Is Not In The Graph");
		}
	}

	public static void breadthFirstTraversal() {
		System.out.println("\t-----------------------------------------------------");
		System.out.println("\t\tDemonstrate Traversal\n");
		userScanner.nextLine();
		System.out.print("\tEnter Starting Location: ");
		Location startLocation = availableLocations.get(userScanner.nextLine());
		if (startLocation != null) {
			System.out.print("\n\n\t___ BREADTH FIRST TRAVERSAL ___\n");
			eulerPath.breadthFirstTraversal(startLocation, locationVisitor);
			((LocationVisitor) locationVisitor).resetLineBreakSignal();
		} else {
			System.out.println("\t\"" + startLocation + "\" Is Not In The Graph");
		}
	}

	public static void findingEulerPath() {
		System.out.println("\t-----------------------------------------------------");
		System.out.println("\t\tFind Euler Path\n");
		userScanner.nextLine();
		System.out.print("\tEnter Starting Location: ");
		String locationName = userScanner.nextLine();
		Location startLocation = availableLocations.get(locationName);
		if (startLocation != null) {
			if (eulerPath.isEulerPath()) {
				eulerPath.findEulerPath(eulerPath.vertexSet.get(startLocation));
				System.out.println("\n\n\tDo You Want To Save Solution? (Yes/No)");
				System.out.print("\tYour Choice: ");
				// userScanner.nextLine();
				boolean yes = userScanner.nextLine().equalsIgnoreCase("yes");
				if (yes) {
					String filename;
					System.out.print("\n\tEnter Where You Want To Save: ");
					filename = userScanner.nextLine();
					File file = new File(filename);
					try {
						eulerPath.saveEulerPath(new PrintWriter(file));
						System.out.println("\tSuccessfully Saved Euler Path In " + filename);
					} catch (IOException exception) {
						System.out.println("\tFailed To Save Euler ");
					}
				}
			}
		} else {
			System.out.println("\t\"" + locationName + "\" Is Not In The Graph");
		}
	}

	public static void saveEulerPathAsTextFile() {
		System.out.println("\t-----------------------------------------------------");
		System.out.println("\t\tSave \n");
		userScanner.nextLine();
		String filename;
		System.out.print("\tEnter Where You Want To Save: ");
		filename = userScanner.nextLine();
		File file = new File(filename);
		try {
			eulerPath.saveAsTextFile(new PrintWriter(file));
			System.out.println("Successfully Save The Graph As Text File In" + filename);
		} catch (IOException exception) {
			System.out.println("\tFailed To Save The Graph As Text File");
		}
	}

	/**
	 * 
	 * Open an input file to read
	 * 
	 * @return a Scanner to read input file
	 * 
	 */
	public static Scanner openInputFile() {
		String filename;
		Scanner scanner = null;
		System.out.print("\tEnter the input filename: ");
		filename = userScanner.nextLine();
		File file = new File(filename);
		try {
			scanner = new Scanner(file);
		} // end try
		catch (FileNotFoundException fe) {
			System.out.println("\tCan't open input file\n");
			return null; // array of 0 elements
		} // end catch
		return scanner;
	}
	
	public static void readInputFile(Graph<String> graph) {
			Scanner inputFile = openInputFile();
			if (inputFile == null) {
				System.out.println("No input, code bricked. Try again");
				System.exit(1);
			}
			String temp = null;
			String city = null, neighbor = null;
			temp = inputFile.nextLine();
			city = temp;
			boolean isName = true;
			while (inputFile.hasNextLine()) {
				temp = inputFile.nextLine();
				if (temp.equals("x")) {
					isName = !isName;
				} else {
					if (isName) {
						city = temp;
					} else {
						neighbor = temp;
						graph.addEdge(city, neighbor, 0);
					}
				}
			}
		}
}
