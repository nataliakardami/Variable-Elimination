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
		ui.askForQuery(); 
		Variable query = ui.getQueriedVariable(); 
		
		// Ask user for observed variables 
		ui.askForObservedVariables(); 
		ArrayList<ObsVar> observed = ui.getObservedVariables(); 
		
		// Turn this on if you want to experiment with different heuristics for bonus points (you need to implement the heuristics yourself)
		ui.askForHeuristic();
		String heuristic = ui.getHeuristic();
		
		// Print the query and observed variables
		ui.printQueryAndObserved();


        VarElim ve = new VarElim(variables, query, observed);

        // necessary for elimination!!!!!!!!!!!!!!!!
        for(Variable var: variables){
            var.setVarElim(ve);
        }

        long startTime = System.nanoTime();
   
        ve.start();

        long runtime = System.nanoTime() - startTime;

        double value = ve.getFactors().get(0).getValue();
        if (value != -1){
            System.out.println("Value:" + value);
        }
        else{
            System.out.println(ve.getFactors().get(0).getProbs());
        }

        System.out.println("Time elapsed in ms : " + runtime);



        /* 
        // MANUAL TESTING suzyyy
        //----------------------------------------------------------------------
        
        // eartquake.bif
        test1(variables);

        // survey.bif -> doesn't work
        // test2(variables);
        */

    }

    /**
     *  Tests survey.bif
     * @param variables arraylist f all variables
     */
    public static void test2(ArrayList<Variable> variables){
        Variable A = variables.get(0); // age, 
        Variable S = variables.get(1); // sex, 
        Variable E = variables.get(2); // education, 

        Variable O = variables.get(3); // work status
        Variable R = variables.get(4); // residential
        Variable T = variables.get(5); // transport

        ArrayList<ObsVar> obs = new ArrayList<ObsVar>();
        ObsVar ev = new ObsVar(A, "adult");
        obs.add(ev);
        // obs.add(new ObsVar(E, "True")); // EVIDENCE EARTH == true

        VarElim ve = new VarElim(variables, T, obs);

        // necessary for elimination!!!!!!!!!!!!!!!!
        for(Variable var: variables){
            var.setVarElim(ve);
        }
   
        ve.start();

        System.out.println(ve.getFactors().get(0).getProbs());
        System.out.println(ve.getFactors().get(0).getValue());

        
    }


    /**
     *  Tests eartquake.bif
     * @param variables arraylist f all variables
     */
    public static void test1(ArrayList<Variable> variables){
        // Variable B = variables.get(0); // quake -> B, 
        Variable E = variables.get(1); // quake -> E, 
        // Variable A = variables.get(2); // quake -> A, 

        Variable query = variables.get(4); // Mary Calls
        // Variable S = variables.get(0); //S
        // Variable O = variables.get(2);

        ArrayList<ObsVar> obs = new ArrayList<ObsVar>();
        // ObsVar ev = new ObsVar(E, "high");
        obs.add(new ObsVar(E, "True")); // EVIDENCE EARTH == true

        VarElim ve = new VarElim(variables, query, obs);

        // necessary for elimination!!!!!!!!!!!!!!!!
        for(Variable var: variables){
            var.setVarElim(ve);
        }
   
        ve.start();

        System.out.println(ve.getFactors().get(0).getProbs());
        System.out.println(ve.getFactors().get(0).getValue());
        
    }

}

