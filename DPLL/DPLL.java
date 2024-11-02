import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DPLL {

	public static void main(String[] args) throws IOException {
		
		FileReader fileReader = new FileReader (args[0]);
		BufferedReader reader = new BufferedReader (fileReader);
		
		// Get list of clauses from input
		ArrayList<ArrayList<Integer>> clauses = new ArrayList<ArrayList<Integer>>();
		
		String line = null;
		while ((!(line = reader.readLine()).equals("0"))) {
			ArrayList<Integer> clause = new ArrayList<Integer>();
			String[] splitLine = line.split("\\s+");
			for (int i = 0; i < splitLine.length; i++) {
				clause.add(Integer.parseInt(splitLine[i]));
			}
			clauses.add(clause);
		}
		
		// Get list of atoms from input
		ArrayList<Integer> atoms = new ArrayList<Integer>();
		
		// Store information after "0" for writing to file later
		ArrayList<String> key = new ArrayList<String>();
		while ((line = reader.readLine()) != null) {
			String[] splitLine = line.split("\\s+");
			atoms.add(Integer.parseInt(splitLine[0]));
			key.add(line);
		}
		// Create truth values array
		Boolean[] truthTable = new Boolean[atoms.size()];
		Boolean[] solution = new Boolean[atoms.size()];
		
		// Call dpl
		solution = dpl (atoms, clauses, truthTable);	solution = dpl (atoms, clauses, truthTable);	
		
		BufferedWriter writer = new BufferedWriter (new FileWriter("dpllOutput.txt"));
		
		if (solution != null) {
			for (int i = 0; i < atoms.size(); i++) {
				String sign = "";
				if (solution[i] == true)
					sign = "T";
				else
					sign = "F";
				writer.write(i+1 + " " + sign);
				writer.newLine();
			}
			writer.write("0");
			writer.newLine();
			for (int j = 0; j < key.size(); j++) {
				writer.write(key.get(j));
				if (j != key.size()-1)
					writer.newLine();
			}
		}
		else {
			writer.write("0");
			writer.newLine();
			for (int j = 0; j < key.size(); j++) {
				writer.write(key.get(j));
				if (j != key.size()-1)
					writer.newLine();
			}
		}
		writer.close();
	}
	
	public static Boolean[] dpl (ArrayList<Integer> atoms, ArrayList<ArrayList<Integer>> clauses, Boolean[] truthTable) {
		
		// Initialize easyCaseLeft so that while loop iterates at least once
		boolean easyCaseLeft = true;
		int pureLiteral;
		int singleton;

		while (easyCaseLeft) {
			if (clauses.size() ==0) {
				for (int i = 0; i < truthTable.length; i++) {
					if (truthTable[i] == null)
						truthTable[i] = true;
				}
				return truthTable;
			}
			else if (emptyClause(clauses)) {
				return null;
			}
			else if ((pureLiteral = hasPureLiteral(clauses, truthTable)) != 0) {
				
				truthTable = obviousAssign (pureLiteral, truthTable);
				for (int i = 0; i < clauses.size(); i++) {
					while ((i < clauses.size()) && (clauses.get(i).contains(pureLiteral))) {
						clauses.remove(i);
					}
				}
			}
			else if ((singleton = hasSingleton(clauses)) != 0) {
				
				truthTable = obviousAssign (singleton, truthTable);
				clauses = propagate (singleton, clauses, truthTable);	
			}
			else
				easyCaseLeft = false;
		}
		// Hard Case
		for (int i = 0; i < truthTable.length; i++) {
			
			if (truthTable[i] ==null) {
				truthTable[i] = true;
				@SuppressWarnings("unchecked")
				ArrayList<ArrayList<Integer>> clauses1 = (ArrayList<ArrayList<Integer>>) clauses.clone();
				clauses1 = propagate (i+1, clauses1, truthTable);
				
				Boolean[] truthTable1 = dpl (atoms, clauses1, truthTable);
				
				if (truthTable1 != null) {
					return truthTable1;
				}
				else {
					truthTable[i] = false;
					clauses1 = propagate (-1*(i+1), clauses, truthTable);
					return dpl(atoms, clauses, truthTable);
				}
			}
		}
		
		return null;
	}
	
	public static boolean easyCaseIn (ArrayList<ArrayList<Integer>> clauses) {
		return false;
	}
	
	public static boolean emptyClause (ArrayList<ArrayList<Integer>> clauses) {
		for (int i = 0; i < clauses.size(); i++) {
			if (clauses.get(i).size() ==0) {
				return true;
			}
		}
		return false;
	}
	
	public static Boolean[] obviousAssign (int literal, Boolean[] V) {
		int index = Math.abs(literal) - 1;
		
			if (literal > 0)
				V[index] = true;
			else {
				V[index] = false;

		}
		return V;
	}
	
	public static ArrayList<ArrayList<Integer>> propagate (int literal, ArrayList<ArrayList<Integer>> S, Boolean[] V) {

		int index = Math.abs(literal) - 1;
		int positive = Math.abs(literal);
		int negative = -1*positive;
		
		int i = 0;
		while (i < S.size()){
	        if (S.get(i).contains(literal)) {
	            S.remove(i);
	            i--;
	        }
	        else if (S.get(i).contains(-1*literal)) {
	           for (int j = 0; j < S.get(i).size(); j++) {
	        	   	if (S.get(i).get(j) == (-1*literal))
	        	   		S.get(i).remove(j);
	           }
	        }
	        i++;
		}
	    return S;
	}

	public static int hasSingleton (ArrayList<ArrayList<Integer>> S) {
		for (int i = 0; i < S.size(); i++) {
			if (S.get(i).size() ==1) {
				return S.get(i).get(0);
			}
		}
		return 0;
	}
	
	public static int hasPureLiteral (ArrayList<ArrayList<Integer>> clauseList, Boolean[] V) {
		// Initial values array
		int [] changes = new int[V.length];
		int [] values = new int [V.length];
		Boolean [] truthValues = new Boolean[V.length];
		// To store positive/negative sign of literal in clause
		int value = 0;
		boolean truthVal;
		
		for (int i = 0; i < clauseList.size(); i++) {
			for (int j = 0; j < clauseList.get(i).size(); j++) {
				int index = Math.abs(clauseList.get(i).get(j)) - 1;
				if (clauseList.get(i).get(j) < 0) {
					value = -1;
					truthVal = false;
					values[index] = value;
				}
				else {
					value = 1;
					truthVal = true;
					values[index] = value;
				}
				if (truthValues[index] ==null)
					truthValues[index] = truthVal;
				else {
					if (Boolean.compare(truthVal,truthValues[index]) != 0) {
						truthValues[index] = truthVal;
						changes[index]++;
					}
				}	
			}
		}
		for (int k = 0; k < truthValues.length; k++) {
			if (truthValues[k] != null) {
				if (changes[k] ==0) {
					return values[k]*(k+1);
				}
			}
		}
		return 0;
	}
}