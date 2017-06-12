import java.util.*;
import java.io.*;


public class Team6Driver {
	public static Scanner userScanner = new Scanner(System.in);

	// opens a text file for input, returns a Scanner:
	public static Scanner openInputFile()
	{
		String filename;
        	Scanner scanner=null;
        
		System.out.print("Enter the input filename: ");
		filename = userScanner.nextLine();
        	File file= new File(filename);

        	try{
        		scanner = new Scanner(file);
        	}// end try
        	catch(FileNotFoundException fe){
        	   System.out.println("Can't open input file\n");
       	    return null; // array of 0 elements
        	} // end catch
        	return scanner;
	}
	
	public static void displayMenu(){
		//Display Menu

	    System.out.println("\n=====================================================");
		System.out.println("***************Euler Path Program********************");
	    System.out.println("                                                     ");
	    System.out.println("What would you like to do now?                       ");
	    System.out.println("Options:                                             ");
	    System.out.println("        1. Read a graph from a text file             ");
	    System.out.println("        2. Add an edge to the graph                  ");
	    System.out.println("        3. Remove an edge from the graph             ");
	    System.out.println("        4. Undo previous removal                     ");
	    System.out.println("        5. Display the graph(Depth-First traversal)  ");
	    System.out.println("        6. Display the graph(Breadth-First traversal)");
	    System.out.println("        7. Display the graph as an adjacency list    ");
	    System.out.println("        8. Solve the problem for the graph           ");
	    System.out.println("        9. Write the graph to a text file            ");
	    System.out.println("       10. Exit program                             ");
	    System.out.println("=====================================================");
	}
	
	public static void main(String[] args){
		Scanner input = new Scanner(System.in);
		int choice = 0;
			
		displayMenu();
	    System.out.print("Select option(1-10): ");
	    choice = input.nextInt();
		while(choice < 1 || choice > 10){
			System.out.print("Please enter an option in the range 1-10: ");
			choice = input.nextInt();
		}
		    
	    while(choice != 10){
		    
	    	//I think for most cases, or maybe just from (2-9), it
	    	//Should check if a graph was previously read from an input file.
	    	//Otherwise if nothing has been read and the user chooses options 2-9 it
	    	//will probably cause an error
		    switch(choice){
		    	case 1 :
		    		Scanner fileScanner = openInputFile();
		    		
		    		// check if file doesn't open and display error message
		    		if(fileScanner == null){
		    			System.out.println("Ending program");
		    			return;
		    		}
		    		
		    		//Read information(graph) from the input file
		    		
		    		fileScanner.close();
		    		
		    		break;
		    	case 2 :
		    		//Adds an edge to the graph
		    		break;
		    	case 3 : 
		    		//Removes an edge from the graph
		    		break;
		    	case 4 :
		    		//Reverts the previous removal(s)
		    		break;
		    	case 5 :
		    		//Display the graph using the Depth-First traversal
		    		break;
		    	case 6 :
		    		//Display the graph using the Breadth-First traversal
		    		break;
		    	case 7 :
		    		//Display the graph as an adjacency list
		    		break;
		    	case 8 :
		    		//Solves the graph problem
		    		break;
		    	case 9 :
		    		//Writes the graph to a text file
		    		break;
		    }
		    
				displayMenu();
			    System.out.print("Select option(1-10): ");
			    choice = input.nextInt();
			while(choice < 1 || choice > 10){
				System.out.print("Please enter an option in the range 1-10: ");
				choice = input.nextInt();
			}
	    }
		
		System.out.println("\nThanks for using the program.");
            
		
	}
}
