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
    public Factor(ArrayList<Variable> var, Map<Condition,Double> probs){
        this.probs = probs;
        this.involved = var;
        //his.involved.add(var);
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


    public Factor sum(Variable elim){
        Iterator<Condition> iterator = probs.keySet().iterator(); // iterate over the map of probs
    
        System.out.println(probs.size());
        ArrayList<ObsVar> poss = new ArrayList<>();
        ArrayList<Condition> conds = new ArrayList<>();
        ArrayList<Variable> vars = new ArrayList<>(this.involved);
        ArrayList<Double> summed = new ArrayList<Double>();

        HashMap<Condition,Double> newprobs = new HashMap<>();
        Double sumd = 0.0;
        ArrayList<Double> lists = new ArrayList<>();
      
        ArrayList<Double> matches = new ArrayList<Double>();
        vars.remove(elim);

        // loop over condition
        
        for (String val1:vars.get(0).getValues()){
            for (String val2:vars.get(1).getValues()){
                poss.add(new ObsVar(vars.get(0),val1));
                poss.add(new ObsVar(vars.get(1),val2));
                conds.add(new Condition(poss));
                poss.clear();
            }
           
        }
        System.out.println(conds.toString());
        Condition match = null;
        sumd = 0.0;

        while (iterator.hasNext()){
           // sumd = 0.0;
           lists.clear();
        
        
            for (Condition cond: conds){ // e | a
                
                Condition row = iterator.next(); // E | a,s
            
                for (String possibility:elim.getValues()){
                     
                    ObsVar observation = new ObsVar(elim, possibility); // s=m 
                    if(row.contains(cond) && row.contains(observation)){
                        sumd += probs.get(row); 
                        lists.add(probs.get(row));
                        match = cond; // Condition
                        matches.add(sumd); 
                        newprobs.put(match,sumd);
                        
                    }
                    
                }


            }

            // newprobs.put(match,sumd);
            
            //matches.clear();
            //sumd += probs.get(match);
            summed.add(sumd);
            
            sumd = 0.0;
           

        }
       
      
        System.out.println("matches "+matches.toString());
        System.out.println(newprobs.size());
        return new Factor(vars,newprobs);
    }


    

    public Factor sumOut(Variable elim){
        // Iterator<Condition> iterator = getProbs().keySet().iterator();
        // involved 

        System.out.println(probs.size());


        ArrayList<Variable> copy = new ArrayList<>(this.involved);
        copy.remove(elim);
        Map<Condition,Double> sums = new HashMap<Condition, Double>();

        if(true){
            Variable var = copy.get(0);

            for(String val: var.getValues()){
                Iterator<Condition> iterator = getProbs().keySet().iterator();
                
                if(copy.size() > 1){
                    Variable other = copy.get(1);
                    
                    for(String other_val : other.getValues()){
                        // this ^ for loop and this while might need to be switched??
                        while (iterator.hasNext()){ // this terminates after the pair young high has been 
                            Condition key = iterator.next();
                            double sum = 0;

                            ObsVar ob1 = new ObsVar(var, val);
                            ObsVar ob2 = new ObsVar(other, other_val);

                            ArrayList<ObsVar> observed = new ArrayList<>();
                            observed.add(ob1);
                            observed.add(ob2);
                            
                            if (key.contains(ob1) && key.contains(ob2)){ // NEVER TRUE
                                sum += this.probs.get(key);
                            }
                            //****  alt condition */
                            if (key.contains(new Condition(observed))){ //this loops
                               // sum += this.probs.get(key);
                                sums.put(new Condition(observed), sum);
                                
                            }
                            //iterator.next();
                        if (sum>0){
                           //sums.put(new Condition(observed), sum);
                        }
                            
                          
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
        ArrayList<Double> summed = new ArrayList<Double>();

        HashMap<Condition,Double> newprobs = new HashMap<>();
        Double sumd = 0.0;
        ArrayList<Double> lists = new ArrayList<>();
      
        ArrayList<Double> matches = new ArrayList<Double>();
        vars.remove(elim);

        // loop over condition
        
        for (String val1:vars.get(0).getValues()){
            for (String val2:vars.get(1).getValues()){
                poss.add(new ObsVar(vars.get(0),val1));
                poss.add(new ObsVar(vars.get(1),val2));
                conds.add(new Condition(poss));
                poss.clear();
            }
           
        }
        System.out.println(conds.toString());
        Condition match = null;
        sumd = 0.0;
        while (iterator.hasNext()){
           // sumd = 0.0;
           lists.clear();
            
            Condition row = iterator.next();
            
            for (Condition cond:conds){
                
                
                if(row.contains(cond)){
                    sumd = probs.get(row); 
                    lists.add(probs.get(row));
                    match = cond; // Condition
                    matches.add(sumd); 
                    newprobs.put(match,sumd);
                    
                }
                
            }

            summed.add(sumd);
            
            sumd = 0.0;
           

        }
       
      
        System.out.println("matches "+matches.toString());
        System.out.println(newprobs.size());
        return new Factor(vars,newprobs);
    }

    public Factor kms(Variable elim){
         
        System.out.println(probs.size());
        ArrayList<ObsVar> poss = new ArrayList<>();
        ArrayList<Condition> conds = new ArrayList<>();
        ArrayList<Variable> vars = new ArrayList<>(this.involved);
        //ArrayList<Double> summed = new ArrayList<Double>();

        HashMap<Condition,Double> newprobs = new HashMap<>();
        Double sumd = 0.0;
      
        ArrayList<Condition> matches = new ArrayList<Condition>();
        Condition match;
        vars.remove(elim);

        // loop over condition
        
        for (String val1:vars.get(0).getValues()){
            for (String val2:vars.get(1).getValues()){
                poss.add(new ObsVar(vars.get(0),val1));
                poss.add(new ObsVar(vars.get(1),val2));
                conds.add(new Condition(poss));
                poss.clear();
            }
           
        }
        // ****** new attempt ***********
        ArrayList<String> elimVals = elim.getValues();

        for (int i=0;i<elimVals.size();i++){
            ObsVar obs = new ObsVar(elim, elimVals.get(i));
            Iterator<Condition> iterator = probs.keySet().iterator();
            Condition row = iterator.next();
            while (iterator.hasNext()){

                if (row.contains(obs)){
                    sumd+= probs.get(row);
                    
                }
               row = iterator.next();
            }
            newprobs.put(row, sumd);
            sumd = 0.0;


        }



        return new Factor(vars,probs);

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
