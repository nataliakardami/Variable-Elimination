package varelim;

import java.util.ArrayList;
import java.util.Map;

public class VarElim {
    private Variable query;
    private ArrayList<ObsVar> observed;
    private ArrayList<Variable> variables;
    ArrayList<Factor> factors;
        /*
    function:
        1) make factors
        2) reduce / restrict (take query variable and only use the portion that is relevant
         e.g a = young, before summing out)
        3) for all hidden variables eliminate H
          - multiply all factors that contain H -> factor g
          - sum out the variable H from factor g
        4) multiply and normalize remaining factors

     */

     /**
      * Constructor for UI
      * @param ui
      * @param vars
      */
    public VarElim(UserInterface ui, ArrayList<Variable> vars){
        this.query = ui.getQueriedVariable();
        this.observed = ui.getObservedVariables();
        this.variables = vars;
        this.factors = makeFactors();
    }
    /**
     * Constructor for manual input 
     * @param vars
     * @param query
     * @param observed
     */
    public VarElim(ArrayList<Variable> vars, Variable query, ArrayList<ObsVar> observed){
        this.query = query;
        this.observed = observed;
        this.variables = vars;
        this.factors = makeFactors();
    }
    /**
     * Compiles an Arraylist of all the initial factors
     * by iterating over each node of the network (besides the observed)
    */
     public  ArrayList<Factor> makeFactors(){
        ArrayList<Factor> f = new ArrayList<Factor>();
        //PriorityQueue ??
        //f.add(new Factor(query));
        for (Variable var:this.variables){
            f.add(new Factor(var));
        }
        return f;
     }

     public void reduceObserved(){
            for (Factor f:factors){
                f.reduce(observed);
            }

     }


     // ********** SETTERS AND GETTERS ************
     public ArrayList<Factor> getFactors() {
         return factors;
     }
}
