package varelim;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

/**
 * Class that handles the communication with the user.
 * 
 * @author Marcel de Korte, Moira Berens, Djamari Oetringer, Abdullahi Ali, Leonieke van den Bulk
 * 
 * Modified to use the new Varaible & Condition based- data structure.
 * @author Pepe Tiebosch
 */
public class UserInterface {
	
	private ArrayList<Variable> variables;
	private Variable query = null;
	private ArrayList<ObsVar> obs = new ArrayList<ObsVar>();
	private String line;
	private String heuristic;
	private Scanner scan;

	/**
	 * Constructor of the user interface.
	 * @param variables, variables of the network
	 */
	public UserInterface(ArrayList<Variable> variables) {
		this.variables = variables;
	}
	
	/**
	 * Asks for a query from the user.
	 */
	public void askForQuery() {
		System.out.println("\nWhich variable(s) do you want to query? Please enter in the number of the variable.");
		for (int i = 0; i < variables.size(); i++) {
			System.out.println("Variable " + i + ": " + variables.get(i).getName());
		}
		scan = new Scanner(System.in);
		line = scan.nextLine();
		if (line.isEmpty()) {
			System.out.println("You have not chosen a query value. Please choose a query value.");
			askForQuery();
		}
		try {
			int queriedVar = Integer.parseInt(line);
			if (queriedVar >= 0 && queriedVar < variables.size()) {
				query = variables.get(queriedVar);
			} else {
				System.out.println("This is not a correct index. Please choose an index between " + 0 + " and "
						+ (variables.size() - 1) + ".");
				askForQuery();
			}
		} catch (NumberFormatException ex) {
			System.out.println("This is not a correct index. Please choose an index between " + 0 + " and "
					+ (variables.size() - 1) + ".");
			askForQuery();
		}
	}

	/**
	 * Ask the user for observed variables in the network.
	 */
	public void askForObservedVariables() {

		obs.clear();
		System.out.println("Which variable(s) do you want to observe? Please enter in the number of the variable, \n"
				+ "followed by a comma and the value of the observed variable. Do not use spaces. \n"
				+ "If you want to query multiple variables, delimit them with a ';' and no spaces.\n"
				+ "Example: '2,True;3,False'");
		for (int i = 0; i < variables.size(); i++) {
			String values = "";
			for (int j = 0; j < variables.get(i).getNumberOfValues() - 1; j++) {
				values = values + variables.get(i).getValues().get(j) + ", ";
			}
			values = values + variables.get(i).getValues().get(variables.get(i).getNumberOfValues() - 1);

			System.out.println("Variable " + i + ": " + variables.get(i).getName() + " - " + values);
		}
		scan = new Scanner(System.in);
		line = scan.nextLine();
		if (line.isEmpty()) {
		} else {
			if (!line.contains(",")) {
				System.out.println("You did not enter a comma between values. Please try again");
				askForObservedVariables();
				return;
			} else {
				while (line.contains(";")) { // Multiple observed variables
					try {
						int queriedVar = Integer.parseInt(line.substring(0, line.indexOf(",")));
						String bool = line.substring(line.indexOf(",") + 1, line.indexOf(";"));
						changeVariableToObserved(queriedVar, bool);
						line = line.substring(line.indexOf(";") + 1); // Continue
																		// with
																		// next
																		// observed
																		// variable.
					} catch (NumberFormatException ex) {
						System.out.println("This is not a correct input. Please choose an index between " + 0 + " and "
								+ (variables.size() - 1) + ".");
						askForObservedVariables();
						return;
					}
				}
				if (!line.contains(";")) { // Only one observed variable
					try {

						int queriedVar = Integer.parseInt(line.substring(0, line.indexOf(",")));
						String bool = line.substring(line.indexOf(",") + 1);
						changeVariableToObserved(queriedVar, bool);
					} catch (NumberFormatException ex) {
						System.out.println("This is not a correct input. Please choose an index between " + 0 + " and "
								+ (variables.size() - 1) + ".");
						askForObservedVariables();
					}
				}
			}
		}
	}

	/**
	 * Checks whether a number and value represent a valid observed value or not and if so, adds it to the
	 * observed list. If not, asks again for new input.
	 */
	public void changeVariableToObserved(int queriedVar, String value) {
		Variable ob;
		if (queriedVar >= 0 && queriedVar < variables.size()) {
			ob = variables.get(queriedVar);
			if (ob.isValueOf(value)) {
				ObsVar obsVartemp = new ObsVar(ob, value);
				obs.add(obsVartemp); //Adding observed variable type to the list
			}

			else {
				System.out.println("Apparently you did not fill in the value correctly. You typed: \"" + value
						+ "\"Please try again");
				askForObservedVariables();
				return;
			}
		} else {
			System.out.println("You have chosen an incorrect index. Please choose an index between " + 0 + " and "
					+ (variables.size() - 1));
			askForObservedVariables();
		}
	}

	/**
	 * Print the network that was read-in (by printing the variables, parents and probabilities).
	 * 
	 */
	public void printNetwork() {
		System.out.println("The variables:");
		for (int i = 0; i < variables.size(); i++) {
			String values = "";
			for (int j = 0; j < variables.get(i).getNumberOfValues() - 1; j++) {
				values = values + variables.get(i).getValues().get(j) + ", ";
			}
			values = values + variables.get(i).getValues().get(variables.get(i).getNumberOfValues() - 1);
			System.out.println((i + 1) + ") " + variables.get(i).getName() + " - " + values); // Printing
																						// the
																						// variables.
		}
		System.out.println("\nThe probabilities:");
		for (int i = 0; i < variables.size(); i++) {
			if (variables.get(i).getNrOfParents() == 1) {
				System.out.println(variables.get(i).getName() + " has parent "
						+ variables.get(i).getParents().get(0).getName());
			} else if (variables.get(i).getNrOfParents() > 1) {
				String parentsList = "";
				for (int j = 0; j < variables.get(i).getNrOfParents(); j++) {
					parentsList = parentsList + variables.get(i).getParents().get(j).getName();
					if (!(j == variables.get(i).getNrOfParents()-1)) {
						parentsList = parentsList + " and ";
					}
				}
				System.out.println(variables.get(i).getName() + " has parents "
						+ parentsList);
			} else {
				System.out.println(variables.get(i).getName() + " has no parents.");
			}
			Map<Condition, Double> probs = variables.get(i).getProbabilities();
			for (Entry<Condition, Double> prob : probs.entrySet()) {
				System.out.println(prob.getKey() + " => " + prob.getValue());	
			}
			System.out.println();
		}
	}

	/**
	 * Prints the query and observed variables given in by the user.
	 * 
	 */
	public void printQueryAndObserved() {
		System.out.println("\nThe queried variable(s) is/are: "); // Printing
																	// the
																	// queried
																	// variables.
		System.out.println(query.getName());
		if (!obs.isEmpty()) {
			System.out.println("The observed variable(s) is/are: "); // Printing
																		// the
																		// observed
																		// variables.
			for (int m = 0; m < obs.size(); m++) {
				System.out.println(obs.get(m).getVar().getName());
				System.out.println("This variable has the value: " + obs.get(m).getValue());
			}
		}
	}
	
	/**
	 * Asks for a heuristic.
	 */
	public void askForHeuristic() {
		System.out.println("Supply a heuristic. Input 1 for least-incoming, 2 for fewest-factors and enter for random");
		scan = new Scanner(System.in);
		line = scan.nextLine();
		if (line.isEmpty()) {
			heuristic = "empty";
			System.out.println("You have chosen for random");
		} else if (line.equals("1")) {
			heuristic = "least-incoming";
			System.out.println("You have chosen for least-incoming");
		} else if (line.equals("2")) {
			heuristic = "fewest-factors";
			System.out.println("You have chosen for fewest-factors");
		} else {
			System.out.println(line + " is not an option. Please try again");
			askForHeuristic();
		}
		//scan.close();
	}

	/**
	 * Getter of the observed variables.
	 * 
	 * @return a list of observed variables given by the user.
	 */
	public ArrayList<ObsVar> getObservedVariables() {
		return obs;
	}

	/**
	 * Getter of the queried variables.
	 * 
	 * @return the variable the user wants to query.
	 */
	public Variable getQueriedVariable() {
		return query;
	}
	
	/**
	 * Getter of the heuristic.
	 * 
	 * @return the name of the heuristic.
	 */
	public String getHeuristic() {
		return heuristic;
	}
}
