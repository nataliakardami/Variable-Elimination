package varelim;

/**
 * Observed variable, contains a pointer to a variable and its observed value
 * 
 * @author Pepe Tiebosch
 */
public class ObsVar {
	private Variable variable;
	private String value;
	
	public ObsVar(Variable var, String observed) {
		this.variable = var;
		this.value = observed;
	}
	

	public String getValue() {
		return this.value;
	}
	
	public Variable getVar() {
		return this.variable;
	}
	
	/**
	 * @return name of the variable stored
	 */
	public String getName() {
		return this.variable.getName();
	}
	
	/**
	 * Equality checker based only on name and Value
	 * @param other Observed Variable
	 */
	public boolean equals(ObsVar other) {
		if (other.getName().equals(getName()) && other.value.equals(value)) {
			return true;
		}
		return false;
	}
}
