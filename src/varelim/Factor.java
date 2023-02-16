package varelim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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


    /**
     * Reduces the factor object by the evidence variables provided.
     * Removes the irrelevant probability values from this.probs
     * @param observed
     * @return self
     */
    public Factor reduce(ArrayList<ObsVar> observed){
       
        Iterator<Condition> iterator = probs.keySet().iterator(); // iterate over the map of probs
    
        System.out.println(probs.size());

        for (ObsVar var:observed){
            //String obsVal = var.getValue();
            while (iterator.hasNext()){
                Condition key = iterator.next();
                
            if (key.mention(var.getVar())){
                if (!key.contains(var)){
                    System.out.println("____________removing......___________");
                    System.out.println(key.toString());
                    iterator.remove();

                }
            }

            }
            
        }
        this.setProbs(probs);
        System.out.println(probs.size());
        return this;
        
    }

    public Factor sumOut(Variable elim){
        Iterator<Condition> iterator = getProbs().keySet().iterator();
        // involved 

        System.out.println(probs.size());


        ArrayList<Variable> copy = new ArrayList<>(this.involved);
        copy.remove(elim);
        Map<Condition,Double> sums = new HashMap<Condition, Double>();

        if(copy.size() > 0){
            Variable var = copy.get(0);

            for(String val: var.getValues()){
                
                if(copy.size() > 1){
                    Variable other = copy.get(1);
                    
                    for(String other_val : other.getValues()){
                        
                        while (iterator.hasNext()){
                            Condition key = iterator.next();
                            double sum = 0;

                            ObsVar ob1 = new ObsVar(var, val);
                            ObsVar ob2 = new ObsVar(other, other_val);

                            ArrayList<ObsVar> observed = new ArrayList<>();
                            observed.add(ob1);
                            observed.add(ob2);
                            
                            if (key.contains(ob1) && key.contains(ob2)){
                                sum += this.probs.get(key);
                            }
                            
                            sums.put(new Condition(observed), sum);
                        }
                    }
                }
                // else???
                // Map <C,V> add (condition, summed val)

            }
        }

        
        this.setProbs(sums);
        System.out.println(sums.size());
        return this;

        }
    
    
    /*
        input  = var
        copy = copy(involved)
        copy.remove(var)

        for( possible values of first var in copy)
            if (has nextVar)
            for(possible values of next var)
                if 'var = true ' and 'next var = true'
                    find next occurence
                    sum prob var and nextvar
                    store it
        
                
            
    */
    public Factor sumOut2(Variable elim){
        Iterator<Condition> iterator = probs.keySet().iterator(); // iterate over the map of probs
    
        System.out.println(probs.size());
        ArrayList<ObsVar> poss = new ArrayList<>();
        ArrayList<Condition> conds = new ArrayList<>();
        ArrayList<Variable> vars = new ArrayList<>(this.involved);
        vars.remove(elim);
        
        for (Variable inv:vars){ // make an array for
            for (String val:inv.getValues()){
                poss.add(new ObsVar(inv, val));
            }
            conds.add(new Condition(poss));
        }

        
        while (iterator.hasNext()){
            Condition key = iterator.next();
            
            for (Condition cond:conds){
            if (cond.contains(key)){
                probs.get(key);

            }
        }
           
                
            }

            
        
            // A,B involved 
            // C elim
            //
            // find all Conditions where "A=a,B=b", 
            // sum the corresponding values for all 
            // possible values of elim
            // entry.value = sum(elim.getPossibleValues())
        
            
        
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
