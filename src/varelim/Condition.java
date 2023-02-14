package varelim;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a condition or current situation, a collection of variables with set-values.
 * 
 * @author Pepe Tiebosch
 */
public class Condition {
	private ArrayList<ObsVar> observed;
	
	public Condition(List<ObsVar> observed) {
		this.observed = new ArrayList<>(observed);
	}
	

	
	/**
	 * Check if a variable is mentioned in this condition
	 * @param v
	 */
	public boolean mention(Variable v) {
		for(ObsVar obs : observed) {
			if(obs.getVar().getName().equals(v.getName())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check if this condition contains a certain observed variable
	 * @param other
	 */
	public boolean contains(ObsVar other) {
		for (ObsVar obsVar : observed) {
			if(other.equals(obsVar)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * check if this condition contains all observed variables present in another condition
	 * @param other
	 */
	public boolean contains(Condition other) {
		for(ObsVar obsVar : other.getObserved()) {
			if(!contains(obsVar)) {
				return false;
			}
		}
		return true;
	}
	
	public int size() {
		return this.observed.size();
	}
	
	
	public ArrayList<ObsVar> getObserved() {
		return observed;
	}
	
	
	public String toString() {
		if(observed.isEmpty()) {
			return "No Condition";
		}
		String condition = observed.get(0).getName() + " = " + observed.get(0).getValue();
		for(int i = 1; i < observed.size(); i++) {
			condition += (i == 1 ? " | " : ", ") + observed.get(i).getName() + " = " + observed.get(i).getValue();
		}
		return condition;
	}
}
