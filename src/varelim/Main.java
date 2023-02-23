package varelim;

import java.util.ArrayList;

/**
 * Main class to read in a network, add queries and observed variables, and run variable elimination.
 *
 * @author Marcel de Korte, Moira Berens, Djamari Oetringer, Abdullahi Ali, Leonieke van den Bulk
 * <p>
 * Modified to use the new Varaible & Condition based- data structure.
 * @author Pepe Tiebosch
 */
public class Main {
    private final static String networkName = "earthquake.bif"; // The network to be read in (format and other networks can be found on http://www.bnlearn.com/bnrepository/)



    public static void main(String[] args) {
		
		// Read in the network
		Networkreader reader = new Networkreader(networkName); 
		
		// Get the network from the reader
		ArrayList<Variable> variables = reader.getVariables();
		
		// Make user interface
		UserInterface ui = new UserInterface(variables);
		
		// Print variables and probabilities
		ui.printNetwork();
		
		// Ask user for query
		// ui.askForQuery(); 
		// Variable query = ui.getQueriedVariable(); 
		
		// Ask user for observed variables 
		// ui.askForObservedVariables(); 
		// ArrayList<ObsVar> observed = ui.getObservedVariables(); 
		
		// Turn this on if you want to experiment with different heuristics for bonus points (you need to implement the heuristics yourself)
		// ui.askForHeuristic();
		// String heuristic = ui.getHeuristic();
		
		// Print the query and observed variables
		// ui.printQueryAndObserved();

      

        // 



     // MANUAL TESTING suzyyy
        Variable B = variables.get(0); // quake -> B, 
        Variable E = variables.get(1); // quake -> E, 
        Variable A = variables.get(2); // quake -> A, 

        Variable query = variables.get(4); // Mary Calls
        Variable S = variables.get(0); //S
        Variable O = variables.get(2);

        ArrayList<ObsVar> obs = new ArrayList<ObsVar>();
        // ObsVar ev = new ObsVar(E, "high");
        obs.add(new ObsVar(E, "True")); // EVIDENCE EARTH == true

        VarElim ve = new VarElim(variables, query, obs);

        // necessary for elimination!!!!!!!!!!!!!!!!
        // why?
        for(Variable var: variables){
            var.setVarElim(ve);
        
        }
        ve.start();
        //ve.start();
    //     Factor out = ve.getFactors().get(2).sumOut(B);
    //     Factor out2 = ve.getFactors().get(2).sumOut(E);
    //     Factor kms = out.sumOut(E);

    //     System.out.println("outB1: " + out.getProbs());
    //     System.out.println("outE: " + out2.getProbs());
        
    //    // Factor output = out.multiply(out2);
    //     //System.out.println("output: " + output);
    //     kms.normalize();
    //     System.out.println("output after normalization: " + kms.getProbs());
    //     Factor f1 = ve.getFactors().get(0);
    //     Factor f2 = ve.getFactors().get(2);
    //     Factor fc = f1.multiply(f2);
      //  System.out.println(fc.getProbs());
       
  
        // System.out.println(fc);





         /* 
        ArrayList<Factor> initFactors = ve.getFactors();
       // ArrayList<Factor> initFactors = ve.makeFactors();
        Factor out = initFactors.get(2).sumOut(B);
        Factor out2 = initFactors.get(2).sumOut(E);
        Factor kms = out.sumOut(E);

        System.out.println("outB1: " + out.getProbs());
        System.out.println("outE: " + out2.getProbs());
        
        // Factor output = out.multiply(out2);
        System.out.println("output: " + kms.getProbs());
        kms.normalize();
        System.out.println("output after normalization: " + kms.getProbs());

        


		
		//PUT YOUR CALL TO THE VARIABLE ELIMINATION ALGORITHM HERE

		
	}
*/

/*
 * 
 * 
 * MANUAL TESTING NATALIA!!!!!!!!!!!!!!!
 * 
 */


/*    public static void main(String[] args) {

        // Read in the network
        Networkreader reader = new Networkreader(networkName);

        // Get the network from the reader
        ArrayList<Variable> variables = reader.getVariables();
        //System.out.println(variables.toString());
        // Make user interface

        UserInterface ui = new UserInterface(variables);
        // MANUAL TESTING
        Variable E = variables.get(1);

        Map<Condition, Double> probs = E.getProbabilities();
        //System.out.println(probs.keySet());

        Variable query = variables.get(4); // R
        Variable S = variables.get(0); //S
        Variable O = variables.get(2);
        ArrayList<ObsVar> obs = new ArrayList<ObsVar>();
        ObsVar ev = new ObsVar(E, "high");
        obs.add(new ObsVar(E, "True"));
        //System.out.println(evidence.getProbabilities().toString());


        VarElim ve = new VarElim(variables, query, obs);
        ArrayList<Factor> initFactors = ve.makeFactors();
        //System.out.println(initFactors.get(2).getProbs().toString());// E
        //obs.add(new ObsVar(E, networkName));
        //ve.reduceObserved();
        //System.out.println(ve.getFactors());
        // System.out.println(initFactors.toString());
        Factor out = initFactors.get(2).sumOut(S);
        System.out.println(out.getProbs());


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
    */
}
}