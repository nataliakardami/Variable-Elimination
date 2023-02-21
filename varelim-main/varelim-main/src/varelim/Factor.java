package varelim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class Factor {
    private ArrayList<Variable> involved;
    private Map<Condition, Double> probs;
    private Variable simpleVar;


    /**
     * Constructor for a one dimensional factor
     */
    public Factor(Variable var) {
        this.simpleVar = var;
        this.probs = var.getProbabilities();
        this.involved = var.getParents();
        this.involved.add(var);
    }

    public Factor(ArrayList<Variable> var, Map<Condition, Double> probs) {
        this.probs = probs;
        this.involved = var;
        //his.involved.add(var);
    }

    public Factor(Factor o){
        this.involved = o.getInvolved();
        this.probs = o.getProbs();
        this.simpleVar = o.getSimpleVar();
    }


    // ****** FACTOR OPERATIONS *******


    /**
     * Reduces the factor object by the evidence variables provided.
     * Removes the irrelevant probability values from this.probs
     *
     * @param observed
     * @return self
     */
    public Factor reduce(ArrayList<ObsVar> observed) {

        Iterator<Condition> iterator = probs.keySet().iterator(); // iterate over the map of probs

        System.out.println(probs.size());

        for (ObsVar var : observed) {
            //String obsVal = var.getValue();
            while (iterator.hasNext()) {
                Condition key = iterator.next();

                if (key.mention(var.getVar())) {
                    if (!key.contains(var)) {
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


    public Factor sumOut(Variable elim) {
        
        System.out.println(probs.size());
        ArrayList<ObsVar> poss = new ArrayList<>();
        ArrayList<Condition> conds = new ArrayList<>();
        ArrayList<Variable> vars1 = new ArrayList<>(this.involved);
        ArrayList<Double> summed = new ArrayList<Double>();

        HashMap<Condition, Double> newprobs = new HashMap<>();
        Double sumd = 0.0;

        vars1.remove(elim);
        
        // remove duplicates of vars
        ArrayList<Variable> vars = new ArrayList<>(vars1.stream().distinct().collect(Collectors.toList()));


        // loop over condition
        for (String val1 : vars.get(0).getValues()) {
            if(vars.size() >=2 ){
                for (String val2 : vars.get(1).getValues()) {
                    poss.add(new ObsVar(vars.get(1), val2));
                    poss.add(new ObsVar(vars.get(0), val1));
                    // poss.add(new ObsVar(vars.get(1), val2));
                    conds.add(new Condition(poss));
                    poss.clear();
                }
            }
            else{
                poss.add(new ObsVar(vars.get(0), val1));
                // poss.add(new ObsVar(vars.get(1), val2));
                conds.add(new Condition(poss));
                poss.clear();
            }

        }

        Condition match = null;
        sumd = 0.0;
        ObsVar observation = null;

        for (Condition cond : conds) { // e | a

           for (String possibility : elim.getValues()) {
                Iterator<Condition> iterator = probs.keySet().iterator(); // iterate over the map of probs
            
                observation = new ObsVar(elim, possibility);  // s=m

                while (iterator.hasNext()) {

                    Condition row = iterator.next(); // E | a , s

                    // for some reason the equals function did not work with
                    // an ObsVar as a parameter, so I worked around it : )
                    ArrayList<ObsVar> q = new ArrayList<ObsVar>();
                    q.add(observation);
                    Condition help = new Condition(q);
                    
                    boolean bool1 = row.contains(cond);
                    boolean bool2 = row.contains(help);
                    
                    if (bool1 && bool2) {
                        sumd += probs.get(row);
                        match = cond; // Condition
                        newprobs.put(match, sumd);

                    }

                }


            }

            newprobs.put(match,sumd);
            summed.add(sumd);

            sumd = 0.0;

        }

        return new Factor(vars, newprobs);
    }
   



    public void normalize() {


        Map<Condition, Double> map = getProbs();
        Iterator<Condition> iterator = map.keySet().iterator(); // iterate over the map of probs
        double sum = 0;


        while (iterator.hasNext()){
            Condition key = iterator.next();

            sum += map.get(key);
        }

        iterator = map.keySet().iterator(); // iterate over the map of probs
        
        Map<Condition, Double> newProbs = new HashMap<>();


        while (iterator.hasNext()){
            Condition key = iterator.next();

            double temp = map.get(key);

            newProbs.put(key, temp/sum);
        }

        this.setProbs(newProbs);


    }



    public Factor multiply(Factor f1) {
        Factor f2 = new Factor(this); 

        Map<Condition, Double> newprobs = new HashMap<>();
        
        ArrayList<Variable> common = new ArrayList<>();
        ArrayList<Variable> f1_unique = new ArrayList<>(f1.involved);
        ArrayList<Variable> f2_unique = new ArrayList<>(f2.involved);
        
        for (Variable var1:f1_unique){
            for (Variable var2:f2_unique){
                if (var1.equals(var2)){
                    f1_unique.remove(var1);
                    f2_unique.remove(var1);
                    common.add(var1);
                }
            }
        }

        // unique variables in both factors
        f1_unique.removeAll(common);
        f2_unique.removeAll(common);

        // ---------------------------

        Condition cond = null;

        double multi = 1.0;

        // loop f1 unique and values
        for(Variable f1_var: f1_unique){ 
            for (String val1: f1_var.getValues()){

                // loop f2 unique and values
                for(Variable f2_var: f2_unique){
                    for(String val2: f2_var.getValues()){
                        
                        //loop through every common variable 
                        // and it's possible values
                        for(Variable var: common){
                            for (String value: var.getValues()){

                                ArrayList<ObsVar> poss_val = new ArrayList<>(); // list for the conditions

                                ObsVar temp1 = new ObsVar(f1_var, val1); // parameter
                                ObsVar temp2 = new ObsVar(f2_var, val2); // this factor
                                ObsVar comm = new ObsVar(var, value); // common var

                                // get first prob X = x, Y = y
                                poss_val.add(comm); // common
                                poss_val.add(temp1); // f1
                                // System.out.println("f1: " + f1.getProbs());

                                Condition kms = new Condition(poss_val);
                                // System.out.println(kms);


                                double x1 = 0;

                                Map<Condition, Double> map = f1.getProbs();
                                Iterator<Condition> iterator = map.keySet().iterator(); // iterate over the map of probs

                                while (iterator.hasNext()) {
                                    Condition key = iterator.next();
                    
                                    if (key.contains(kms)) {
                                        x1 = map.get(key);
                                    }
                    
                                }                             


                            
                                poss_val.clear();

                                // get second prob Y = y, Z = z
                                poss_val.add(comm);
                                poss_val.add(temp2);
                                
                                
                                kms = new Condition(poss_val);

                                map = f2.getProbs();
                                iterator = map.keySet().iterator(); // iterate over the map of probs

                                double x2 = 0;
                                
                                while (iterator.hasNext()) {
                                    Condition key = iterator.next();
                    
                                    if (key.contains(kms)) {
                                        x2 = map.get(key);
                                    }
                    
                                } 

                                multi = x1 * x2;

                                // for the final condition
                                poss_val.add(temp1);
                                
                                cond = new Condition(poss_val);

                                newprobs.put(cond, multi);


                            }

                        }
                    }

                }
            }
        }
    
        f2_unique.addAll(f1_unique);
        f2_unique.addAll(common);
        return new Factor(f2_unique, newprobs);
    }


    // ****** SETTERS AND GETTERS *******
    public ArrayList<Variable> getInvolved() {
        return involved;
    }

    public int size(){
        return involved.size();
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
