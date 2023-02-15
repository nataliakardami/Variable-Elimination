package varelim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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
        //Map<Condition,Double> temp = new HashMap<>();
        System.out.println(probs.size());
        //Set<Condition> toRemove = temp.keySet();

        for (ObsVar var:observed){
            //String obsVal = var.getValue();
            while (iterator.hasNext()){
                Condition key = iterator.next();

                if (key.contains(var)){
                    System.out.println("____________removing.....___________");
                    System.out.println(key.toString());
                    iterator.remove();

                }

            }
            
        }
        this.setProbs(probs);
        System.out.println(probs.size());
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
    public void setProbs(Map<Condition, Double> probs) {
        this.probs = probs;
    }



    
}
