package varelim;

import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;
public class VarElim {
    private Variable query;
    private ArrayList<ObsVar> observed;
    private ArrayList<Variable> hidden;
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
    public VarElim(UserInterface ui, ArrayList<Variable> vars){
        this.query = ui.getQueriedVariable();
        this.observed = ui.getObservedVariables();
        this.hidden = vars;
    }

     public void make_factors(){
        ArrayList<Factor> f = new ArrayList<Factor>();
        //PriorityQueue ??
        f.add(new Factor(query));
        for (Variable var:hidden){
            f.add(new Factor(var));
        }
    
     }
}
