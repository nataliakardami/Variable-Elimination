package varelim;

import java.util.ArrayList;
import java.util.Map;

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
		//System.out.println(variables.toString());
		// Make user interface
		
		UserInterface ui = new UserInterface(variables);
		// MANUAL TESTING
		Variable E = variables.get(2);

		Map<Condition,Double> probs = E.getProbabilities();
		//System.out.println(probs.keySet());

		Variable query = variables.get(4); // R
		Variable evidence = variables.get(1); //S
		ArrayList<ObsVar> obs = new ArrayList<ObsVar>();
		ObsVar ev = new ObsVar(E, "high");
		obs.add(new ObsVar(evidence, "F"));
		//System.out.println(evidence.getProbabilities().toString());
		


		VarElim ve = new VarElim(variables,query,obs);
		ArrayList<Factor> initFactors = ve.makeFactors();
		//System.out.println(initFactors.get(2).getProbs().toString());// E
		//obs.add(new ObsVar(E, networkName));
		ve.reduceObserved();
		System.out.println(ve.getFactors());
		System.out.println(initFactors.get(2).getProbs().toString());
		System.out.println(initFactors.get(2).sumOut(evidence).getProbs());



		
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