package varelim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

public class Factor {
    private ArrayList<Variable> involved;
    private Map<Condition, Double> probs;
    private Variable simpleVar;

    private double value;


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

    // just for the empty factor wich is a probability
    public Factor(Double val){
        this.value = val;
        this.involved = new ArrayList<>();
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

        if (this.probs.size()==1){
            return new Factor(new ArrayList<>(probs.values()).get(0));
        }


        this.setProbs(probs);
        return this;

    }


    /**
     * Returns a new factor with the relevant variables and probabilities,
     *  after summing out the variable given in the parameter.
     * 
     * @param elim the variable to be eliminated
     * @return new factor
     */
    public Factor sumOut(Variable elim) {
        
        ArrayList<ObsVar> poss = new ArrayList<>();
        ArrayList<Condition> conds = new ArrayList<>();
        ArrayList<Variable> vars1 = new ArrayList<>(this.involved);
        ArrayList<Double> summed = new ArrayList<Double>();

        HashMap<Condition, Double> newprobs = new HashMap<>();
        Double sumd = 0.0;

        vars1.remove(elim);
        
        // remove duplicates of vars
    
        ArrayList<Variable> vars = new ArrayList<Variable>(new LinkedHashSet<Variable>(vars1));

        // loop over condition
        for (String val1 : vars.get(0).getValues()) {
            if(vars.size() >= 2 ){
                for (String val2 : vars.get(1).getValues()) {
                    poss.add(new ObsVar(vars.get(1), val2));
                    poss.add(new ObsVar(vars.get(0), val1));
                    conds.add(new Condition(poss));
                    poss.clear();
                }
            }
            else{
                poss.add(new ObsVar(vars.get(0), val1));
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
   



    /**
     * Normalizes the probabilities of a factor, so all probabilities sum up to 1.
     */
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



    public Factor multiplication(Factor f1){
        Factor f2 = new Factor(this);

        Map<Condition, Double> newProbs = new HashMap<>();
        
        ArrayList<Variable> common = new ArrayList<>();
        ArrayList<Variable> f1_vars = new ArrayList<>(f1.involved);
        ArrayList<Variable> f2_vars = new ArrayList<>(f2.involved);

        Map<Condition, Double> map_f1 = f1.getProbs();
        Map<Condition, Double> map_f2 = f2.getProbs();



        ArrayList<Variable> vars = new ArrayList<>(f1_vars);
        vars.addAll(f2_vars);
        

        // to get all the distinct variables in a list
        ArrayList<Variable> allVariables = new ArrayList<>();

        for(Variable var: vars){
            if (!allVariables.contains(var)){
                allVariables.add(var);
            }
        }



        // check for empty factor
        if (f1_vars.size() == 0 && f2_vars.size() == 0){
            return new Factor(f1.getValue() * f2.getValue());
        }
        else if (f1_vars.size() == 0){
            Iterator<Condition> iterator = map_f2.keySet().iterator(); // iterate over the map of probs
            double multi = 0;
            Condition cond = null;
            while (iterator.hasNext()) {
                    cond = iterator.next();
                    multi = f1.getValue() * map_f2.get(cond);

                    newProbs.put(cond, multi);
            }

            return new Factor(allVariables, newProbs);
        }
        else if(f2_vars.size() == 0){
            Iterator<Condition> iterator = map_f1.keySet().iterator(); // iterate over the map of probs
            double multi = 0;
            Condition cond = null;

            while (iterator.hasNext()) {
                cond = iterator.next();
                multi = f2.getValue() * map_f1.get(cond);
                newProbs.put(cond, multi);
            }

            return new Factor(allVariables, newProbs);
        }
        


        // find common variables if any
        for (Variable var1: new ArrayList<>(f1_vars)){
            for (Variable var2: new ArrayList<>(f2_vars)){
                if (var1.equals(var2)){
                    common.add(var1);
                }
            }
        }

        
        
        

        // iterating over variables
        Iterator<Condition> it_f1 = map_f1.keySet().iterator(); // iterate over the map of probs
        
        while (it_f1.hasNext()) {
            Iterator<Condition> it_f2 = map_f2.keySet().iterator(); // iterate over the map of probs


            Condition key_f1 = it_f1.next();
            ArrayList<ObsVar> observed_f1 = key_f1.getObserved();

            while (it_f2.hasNext()) {
                    
                Condition key_f2 = it_f2.next();
                ArrayList<ObsVar> observed_f2 = key_f2.getObserved();

                ArrayList<ObsVar> allObserved = new ArrayList<>(observed_f2);
                allObserved.addAll(observed_f1);
                
                ArrayList<ObsVar> distinct_observed = new ArrayList<>();

                // remove duplicates in     !!!variables!!!     from this list
                for(ObsVar var: allObserved){
                    boolean bool = true;
                    for(ObsVar var2: distinct_observed){
                        if (var2.equals(var)){
                            bool = false;
                        }
                    }
                    
                    if (bool){
                        distinct_observed.add(var);
                    }
                }


                if (common.size() >0){
                    
                    int count = 0;

                    for (ObsVar var1: new ArrayList<>(observed_f1)){
                        for (ObsVar var2: new ArrayList<>(observed_f2)){
                            if (var1.equals(var2)){
                                count++;
                            }
                        }
                    }

                    if(count == common.size()){
                        Condition cond = new Condition(distinct_observed);
                        double multi = map_f1.get(key_f1) * map_f2.get(key_f2);

                        newProbs.put(cond, multi);
                    }

                }
                else{
                    Condition cond = new Condition(distinct_observed);
                    double multi = map_f1.get(key_f1) * map_f2.get(key_f2);

                    newProbs.put(cond, multi);
                }
                
            }
            

        }

        return new Factor(allVariables, newProbs);
    }
 

    

    // ****** SETTERS AND GETTERS *******
    public ArrayList<Variable> getInvolved() {
        return involved;
    }

    public double getValue(){
        return this.value;
    }

    /**
     * @return the dimension of the factor, aka the number of unique variables invovled
     */
    public int dimension(){
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
   
    public String name() {
       
        return "f("+ involved.toString() +")";
    }

    /**
     * Checks if the factor contains a variable.
     * Checks only the string value of the name
     * @param var
     * @return
     */
    public boolean contains(Variable var){
        for (Variable inv:involved){
            if (inv.getName().equals(var.getName())){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String name = "f(";
        for(Variable inv:involved){
            name += inv.getName()+" ";
        }
        return name+")";
    }


}
