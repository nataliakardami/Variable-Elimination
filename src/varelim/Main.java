package varelim;

import java.util.ArrayList;

/**
 * Main class to read in a network, add queries and observed variables, and run variable elimination.
 * 
 * @author Marcel de Korte, Moira Berens, Djamari Oetringer, Abdullahi Ali, Leonieke van den Bulk
 * 
 * Modified to use the new Varaible & Condition based- data structure.
 * @author Pepe Tiebosch
 */
public class Main {
	private final static String networkName = "survey.bif"; // The network to be read in (format and other networks can be found on http://www.bnlearn.com/bnrepository/)

	public static void main(String[] args) {
		
		// Read in the network
		Networkreader reader = new Networkreader(networkName); 
		
		// Get the network from the reader
		ArrayList<Variable> variables = reader.getVariables();
		Variable E = variables.get(2);
		System.out.println(E.getProbabilities());
		// Make user interface
		//UserInterface ui = new UserInterface(variables);
		
		// Print variables and probabilities
		//ui.printNetwork();
		
		// Ask user for query
		//ui.askForQuery(); 
		// Variable query = ui.getQueriedVariable(); 
		
		// Ask user for observed variables 
		//ui.askForObservedVariables(); 
		// ArrayList<ObsVar> observed = ui.getObservedVariables(); 
		
		// Turn this on if you want to experiment with different heuristics for bonus points (you need to implement the heuristics yourself)
		// ui.askForHeuristic();
		// String heuristic = ui.getHeuristic();
		
		// Print the query and observed variables
		//ui.printQueryAndObserved();
		
		
		//PUT YOUR CALL TO THE VARIABLE ELIMINATION ALGORITHM HERE
		
	}
}