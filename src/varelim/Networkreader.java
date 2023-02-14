package varelim;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that reads in a network from a .bif file and puts the variables and probabilities at the right places.
 * 
 * @author Marcel de Korte, Moira Berens, Djamari Oetringer, Abdullahi Ali, Leonieke van den Bulk
 * 
 * Modified to use the new Varaible & Condition based- data structure.
 * @author Pepe Tiebosch
 */
public class Networkreader {

	private ArrayList<Variable> vs = new ArrayList<Variable>();

	/**
	 * Constructor reads in the data file and adds the variables and its
	 * probabilities to the designated arrayLists.
	 * 
	 * @param file, the name of the .bif file that contains the network.
	 */
	public Networkreader(String file) {
		BufferedReader br = null;
		try {
			String cur; // Keeping track of current line observed by BufferedReader
			Variable curVar = null;
			br = new BufferedReader(new FileReader(file)); 
			try {
				while ((cur = br.readLine()) != null) {
					if (cur.contains("variable")) { 
						//Add variable to the list
						String varName = cur.substring(9, cur.length() - 2);
						cur = br.readLine();
						ArrayList<String> possibleValues = searchForValues(cur);
						vs.add(new Variable(varName, possibleValues));
					}
					if (cur.contains("probability")) { 
						// Conditional to check for parents of selected variable
						curVar = searchForParents(cur);
					}
					if (cur.contains("table")) { 
						// Conditional to find probabilities of 1 row and add Probabilities to
						// probability list
						searchForProbs(cur, curVar);
					}
					if (cur.contains(")") && cur.contains("(") && !cur.contains("prob")) {
						// Conditional to find probabilities of more than 1 row;
						// add probabilities to probability list
						searchForProbs(cur, curVar);
					}
				}
			} catch (IOException e) {
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
				System.exit(0);
			}
		} catch (FileNotFoundException e) {
			System.out.println("This file does not exist.");
			System.exit(0);
		}
	}

	/**
	 * Searches for a row of probabilities in a string
	 * 
	 * @param a string s
	 * @return a ProbRow
	 */
	private Map<Condition, Double> searchForProbs(String s, Variable curVar) throws IllegalArgumentException {
		if(curVar == null) {
			throw new IllegalArgumentException("This network is malformed");
		}
		Map<Condition, Double> probabilities = new HashMap<>();

		int beginIndex = s.indexOf(')') + 2;
		if (s.contains("table")) {
			beginIndex = s.indexOf('e') + 2;
		}

		int endIndex = s.length() - 1;
		String subString = s.substring(beginIndex, endIndex);
		String[] probsString = subString.split(", ");
		double[] probs = new double[probsString.length];
		for (int i = 0; i < probsString.length; i++) {
			probs[i] = Double.parseDouble(probsString[i]);
		}
		
		if (s.contains("table")) {
			for(int i=0; i<probs.length; i++) {
				ObsVar obsVar = new ObsVar(curVar, curVar.getValues().get(i));
				Condition cond = new Condition(Arrays.asList(obsVar));
				probabilities.put(cond, probs[i]);
			}
		} else {
			beginIndex = s.indexOf('(') + 1;
			endIndex = s.indexOf(')');
			subString = s.substring(beginIndex, endIndex);
			String[] stringValues = subString.split(", ");
			
			for(int i=0; i<probs.length; i++) {
				ArrayList<ObsVar> obsVars = new ArrayList<ObsVar>();
				obsVars.add(new ObsVar(curVar, curVar.getValues().get(i)));
				for(int j = 0; j < curVar.getNrOfParents(); j++) {
					obsVars.add(new ObsVar(curVar.getParents().get(j), stringValues[j]));
				}
				Condition cond = new Condition(obsVars);
				probabilities.put(cond, probs[i]);
			}
		}
		curVar.addProbabilities(probabilities);

		return probabilities;
	}

	/**
	 * Searches for values in a string
	 * 
	 * @param a string s
	 * @return a list of values
	 */
	public ArrayList<String> searchForValues(String s) {
		int beginIndex = s.indexOf('{') + 2;
		int endIndex = s.length() - 3;
		String subString = s.substring(beginIndex, endIndex);
		String[] valueArray = subString.split(", ");
		return new ArrayList<String>(Arrays.asList(valueArray));
	}

	/**
	 * Method to check parents of chosen variable.
	 * 
	 * @param cur, which gives the current line.
	 */
	private Variable searchForParents(String cur) {
		if (cur.contains("|")) { // Variable has parents
			ArrayList<Variable> parents = new ArrayList<>();
			String varName = cur.substring(14, cur.indexOf("|") - 1);
			Variable var = getVarByName(varName);

			String sub = cur.substring(cur.indexOf('|') + 2, cur.indexOf(')') - 1);
			while (sub.contains(",")) { // Variable has multiple parents
				String current = sub.substring(0, sub.indexOf(','));
				sub = sub.substring(sub.indexOf(',') + 2);
				parents.add(getVarByName(current));
			}
			parents.add(getVarByName(sub));
	
			var.setParents(parents);
			return var;
		} else {
			String varName = cur.substring(14, cur.indexOf(")") - 1);
			return getVarByName(varName);
		}
	}

	/**
	 * Gets a variable from variable Vs when the name is given
	 * 
	 * @param name
	 * @return variable with name as name
	 */
	private Variable getVarByName(String name) {
		Variable var = null;
		for(Variable v : vs) {
			if(name.equals(v.getName())) {
				var = v;
				break;
			}
		}
		return var;
	}

	/**
	 * Getter of the variables
	 * 
	 * @return the network as list of variables
	 */
	public ArrayList<Variable> getVariables() {
		return vs;
	}

}