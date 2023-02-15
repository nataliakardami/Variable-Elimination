package varelim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Factor {
    private ArrayList<Variable> involved;
    private Map<Condition,Double> probs;
    private Variable simpleVar;


    /**
     * Constructor for a one dimensional factor
     */
    public Factor(Variable var){
       this.simpleVar = var;
       this.probs = var.getProbabilities();
       this.involved = var.getParents();
       this.involved.add(var);
    }


    // ****** FACTOR OPERATIONS *******
    
    public Factor reduce(ArrayList<ObsVar> observed){
        Iterator<Condition> iterator = probs.keySet().iterator(); // iterate over the map of probs

        for (ObsVar var:observed){
            //String obsVal = var.getValue();
            while (iterator.hasNext()){
                Condition key = iterator.next();

                if (key.contains(var)){

                }

            }
    
        }
        return this;
    }

    public Factor sumOut(){
        return this;
    }
    public Factor normalize(){
        return this;
    }

    public Factor multiply(Factor f){
        return f;
    }


    // ****** SETTERS AND GETTERS *******
    public ArrayList<Variable> getInvolved() {
        return involved;
    }
    
    public Map<Condition, Double> getProbs() {
        return probs;
    }

    public Variable getSimpleVar() {
        return simpleVar;
    }

    public void setSimpleVar(Variable simpleVar) {
        this.simpleVar = simpleVar;
    }



    
}
