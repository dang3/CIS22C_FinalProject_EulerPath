
import java.util.*;
import java.text.*;


//------------------------------------------------------
public class GraphTester
{
	   // -------  main --------------
	   public static void main(String[] args)
	   {
	      // build graph
	      EulerPath<String> myGraph1 = new EulerPath<String>();
	      
//	      myGraph1.addEdge("A", "B", 0);
//	      myGraph1.addEdge("A", "C", 0);
//	      
//	      myGraph1.addEdge("B", "A", 0);
//	      myGraph1.addEdge("B", "C", 0);
//	      
//	      myGraph1.addEdge("C", "B", 0);
//	      myGraph1.addEdge("C", "A", 0);
//	      myGraph1.addEdge("C", "D", 0);
//	      myGraph1.addEdge("C", "F", 0);
//	      
//	      myGraph1.addEdge("D", "C", 0);
//	      myGraph1.addEdge("D", "E", 0);
//	      myGraph1.addEdge("D", "F", 0);
//	      
//	      myGraph1.addEdge("E", "D", 0);
//	      
//	      myGraph1.addEdge("F", "D", 0);
//	      myGraph1.addEdge("F", "C", 0);
	      //myGraph1.showAdjTable();
	      
	      myGraph1.addEdge("A", "C", 0);
	      myGraph1.addEdge("B", "C", 0);
	      myGraph1.addEdge("C", "A", 0);
	      myGraph1.addEdge("C", "B", 0);
	      myGraph1.addEdge("C", "D", 0);
	      myGraph1.addEdge("C", "E", 0);
	      myGraph1.addEdge("D", "C", 0);
	      myGraph1.addEdge("D", "F", 0);
	      myGraph1.addEdge("E", "C", 0);
	      myGraph1.addEdge("E", "F", 0);
	      myGraph1.addEdge("F", "D", 0);
	      myGraph1.addEdge("F", "E", 0);
	      myGraph1.addEdge("F", "G", 0);
	      myGraph1.addEdge("F", "H", 0);
	      myGraph1.addEdge("G", "F", 0);
	      myGraph1.addEdge("G", "H", 0);
	      myGraph1.addEdge("H", "F", 0);
	      myGraph1.addEdge("H", "G", 0);
	     // myGraph1.showAdjTable();
	      
	      
	      
	      myGraph1.checkForEulerPath();
	      
	     
	      
	      
	      
	      
	      
//	      myGraph1.addEdge("A", "B", 0);   myGraph1.addEdge("A", "C", 0);  myGraph1.addEdge("A", "D", 0);
//	      myGraph1.addEdge("B", "E", 0);   myGraph1.addEdge("B", "F", 0); 
//	      myGraph1.addEdge("C", "G", 0);
//	      myGraph1.addEdge("D", "H", 0);   myGraph1.addEdge("D", "I", 0); 
//	      myGraph1.addEdge("F", "J", 0);  
//	      myGraph1.addEdge("G", "K", 0);   myGraph1.addEdge("G", "L", 0); 
//	      myGraph1.addEdge("H", "M", 0);   myGraph1.addEdge("H", "N", 0);
//	      myGraph1.addEdge("I", "N", 0);

	    //  myGraph1.showAdjTable();
	      

	   }

}
