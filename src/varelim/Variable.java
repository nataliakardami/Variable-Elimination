package varelim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to represent a variable.
 * 
 * @author Marcel de Korte, Moira Berens, Djamari Oetringer, Abdullahi Ali, Leonieke van den Bulk
 * 
 * Modified to use the new Varaible & Condition based- data structure.
 * @author Pepe Tiebosch
 */
public class Variable implements Comparable<Variable>{

	private String name;
	private ArrayList<String> possibleValues;
	private ArrayList<Variable> parents; // Note that parents is not set in the constructor, because of the .bif file layout
	private Map<Condition, Double> probabilities; // Note that probabilities are not set in the constructor because of the .bif file layour

	// private ArrayList<Variable> allVariables;
	private VarElim varElim;

	/**
	 * Constructor of the class.
	 * @param name, name of the variable.
	 * @param possibleValues, the possible values of the variable.
	 */
	public Variable(String name, ArrayList<String> possibleValues) {
		this.name = name;
		this.possibleValues = possibleValues;
		this.parents = new ArrayList<Variable>();
		this.probabilities = new HashMap<>();
		// this.allVariables = allVariables;
	}
	
	/**
	 * Transform variable and its values to string.
	 */
	public String toString() {
		String valuesString = "";
		for(int i = 0; i < possibleValues.size()-1; i++){
			valuesString = valuesString + possibleValues.get(i) + ", ";
		}
		valuesString = valuesString + possibleValues.get(possibleValues.size()-1);
		return name + " - " + valuesString;
	}
	
	public boolean equals(Variable other) {
		return (other.getName().equals(name) && other.possibleValues.equals(possibleValues));
	}

	public void addProbabilities(Map<Condition, Double> probabilities) throws IllegalArgumentException {
		for(Map.Entry<Condition, Double> entry : probabilities.entrySet()) {
			for(Variable parent : this.parents) {
				if(!entry.getKey().mention(parent)) {
					throw new IllegalArgumentException("Incomplete or Invalid condition ");
				}
			}
		}
		this.probabilities.putAll(probabilities);
	}
	
	public Map<Condition, Double> getProbabilities() {
		return this.probabilities;
	}

	/**
	 * Getter of the values.
	 * @return the values of the variable as a ArrayList of Strings.
	 */
	public ArrayList<String> getValues(){
		return possibleValues;
	}
	
	/**
	 * Check if string v is a value of the variable.
	 * @return a boolean denoting if possibleValues contains string v.
	 */
	public boolean isValueOf(String v) {
		return possibleValues.contains(v);
	}
	
	/**
	 * Getter of the amount of possible values of the variable.
	 * @return the amount of values as an int.
	 */
	public int getNumberOfValues() {
		return possibleValues.size();
	}

	/**
	 * Getter of the name of the variable.
	 * @return the name as a String.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Getter of the parents of the variable.
	 * @return the list of parents as an ArrayList of Variables.
	 */
	public ArrayList<Variable> getParents() {
		return parents;
	}
	
	/**
	 * Setter of the parents of the variable.
	 * @param the list of parents as an ArrayList of Variables.
	 */
	public void setParents(ArrayList<Variable> parents) {
		this.parents = parents;
	}

	/**
	 * Check if a variable has parents.
	 * @return a boolean denoting if the variable has parents.
	 */
	public boolean hasParents(){
		return parents != null;
	}
	
	/**
	 * Getter for the number of parents a variable has.
	 * @return the amount of parents as an int.
	 */
	public int getNrOfParents() {
		if(parents != null)
			return parents.size();
		return 0;
	}

	public void setVarElim(VarElim varElim){
		this.varElim = varElim;
	}


	public int getNrOfFactors(){

		ArrayList<Factor> list = varElim.getFactors();
		int count = 0;
		for (Factor f: list){
			for(Variable var: f.getInvolved()){
				if(var.equals(this)){
					count++;
				}
			}
		}

		return count;
	}



	/**
     * Chooses the correct function based on the string given.
     * @param heuristic a string to denote the heuristic.
     * @return evaluation of the variable
     */
    public int evaluate(String heuristic) {

        return switch (heuristic) {
            case "empty" -> 0;
            case "least-incoming" -> getNrOfParents();
            case "fewest factors" -> getNrOfFactors();
            default -> 0;
        };
    }

	/**
     * @param o the object to be compared.
     * @return 0
     */
    @Override
	public int compareTo(Variable o){

		// here we pass the heuristic as a string
        String heuristic = "combined";

        int comparison = this.evaluate(heuristic) - o.evaluate(heuristic);
        if (comparison > 0) {
            return 1;
        } else if (comparison < 0) {
            return -1;
        } else return 0;

	}



}
