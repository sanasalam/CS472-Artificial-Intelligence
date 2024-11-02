import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FrontEnd {

	public static void main(String[] args) throws IOException {
		
		int numberHoles;
		int emptyHole;
		
		FileReader fileReader = new FileReader (args[0]);
		BufferedReader reader = new BufferedReader (fileReader);
		
		String line = reader.readLine();
		String[] splitLine = line.split("\\s+");
		
		numberHoles = Integer.parseInt(splitLine[0]);
		emptyHole = Integer.parseInt(splitLine [1]);
		
		int atomsKey = 1;
		
		// Constructing jump atom and adding them to array list
		
		ArrayList<Atom> jumpsList = new ArrayList<Atom>();
		
		while ((line = reader.readLine()) != null) {
			
			splitLine = line.split("\\s+");
			
			int x = Integer.parseInt(splitLine[0]);
			int y = Integer.parseInt(splitLine[1]);
			int z = Integer.parseInt(splitLine[2]);
			
			for (int i = 1; i <= numberHoles-2; i++) {
				Atom jump = new Atom (x, y, z, i, atomsKey);
				atomsKey++;
				jumpsList.add(jump);
				Atom jumpReverse = new Atom (z, y, x, i, atomsKey);
				atomsKey++;
				jumpsList.add(jumpReverse);
			}
		}
		reader.close();
		// Constructing peg atoms and adding them to array list
		
		ArrayList<Atom> pegsList = new ArrayList<Atom>();
		
		for (int i = 1; i <= numberHoles; i++) {
			for (int j = 1; j <= numberHoles-1; j++) {
				Atom peg = new Atom (i, j, atomsKey);
				pegsList.add(peg);
				atomsKey++;
			}
		}
		
		ArrayList<ArrayList<Integer>> clauses = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> causalAxioms = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> actions = new ArrayList<ArrayList<Integer>>();
		
		// PreCondition Clauses and Causal Axioms
		// Adding 'one action at a time' clauses here too bc already looping through jumpList
		
		for (int i = 0; i < jumpsList.size(); i++) {
			for (int j = 0; j < pegsList.size(); j++) {
				if(jumpsList.get(i).time == pegsList.get(j).time) {
					if (jumpsList.get(i).x == pegsList.get(j).hole) {
						ArrayList<Integer> clause = new ArrayList<Integer>();
						clause.add(-1*jumpsList.get(i).key);
						clause.add(pegsList.get(j).key);
						clauses.add(clause);
					}
					else if (jumpsList.get(i).y == pegsList.get(j).hole) {
						ArrayList<Integer> clause = new ArrayList<Integer>();
						clause.add(-1*jumpsList.get(i).key);
						clause.add(pegsList.get(j).key);
						clauses.add(clause);
					}
					else if (jumpsList.get(i).z == pegsList.get(j).hole) {
						ArrayList<Integer> clause = new ArrayList<Integer>();
						clause.add(-1*jumpsList.get(i).key);
						clause.add(-1*pegsList.get(j).key);
						clauses.add(clause);
					}
				}
				else if (jumpsList.get(i).time == (pegsList.get(j).time-1)) {
					if (jumpsList.get(i).x == pegsList.get(j).hole) {
						ArrayList<Integer> axiom = new ArrayList<Integer>();
						axiom.add(-1*jumpsList.get(i).key);
						axiom.add(-1*pegsList.get(j).key);
						causalAxioms.add(axiom);
					}
					else if (jumpsList.get(i).y == pegsList.get(j).hole) {
						ArrayList<Integer> axiom = new ArrayList<Integer>();
						axiom.add(-1*jumpsList.get(i).key);
						axiom.add(-1*pegsList.get(j).key);
						causalAxioms.add(axiom);
					}
					else if (jumpsList.get(i).z == pegsList.get(j).hole) {
						ArrayList<Integer> axiom = new ArrayList<Integer>();
						axiom.add(-1*jumpsList.get(i).key);
						axiom.add(pegsList.get(j).key);
						causalAxioms.add(axiom);
					}
				}
			}
			for (int k = i+1; k < jumpsList.size(); k++) {
				if (jumpsList.get(k).time == jumpsList.get(i).time) {
					ArrayList<Integer> action = new ArrayList<Integer>();
					action.add(-1*jumpsList.get(i).key);
					action.add(-1*jumpsList.get(k).key);
					actions.add(action);
				}
			}
		}
		
		// Frame Axioms and Starting State
		ArrayList<ArrayList<Integer>> frameAxiomsA = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> frameAxiomsB = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> startingState = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> endingState = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> end = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> endingCondition = new ArrayList<ArrayList<Integer>>();
		
		for (int i = 0; i < pegsList.size(); i++) {
			if (pegsList.get(i).time ==1) {
				if(pegsList.get(i).hole == emptyHole) {
					ArrayList<Integer> start = new ArrayList<Integer>();
					start.add(-1*pegsList.get(i).key);
					startingState.add(start);
				}
				else {
					ArrayList<Integer> start = new ArrayList<Integer>();
					start.add(pegsList.get(i).key);
					startingState.add(start);
				}
			}
			if (pegsList.get(i).time == numberHoles-1) {
				end.add(pegsList.get(i).key);
				for (int j = i+1; j < pegsList.size(); j++) {
					if (pegsList.get(j).time == numberHoles-1) {
						ArrayList<Integer> condition = new ArrayList<Integer>();
						condition.add(-1*pegsList.get(i).key);
						condition.add(-1*pegsList.get(j).key);
						endingCondition.add(condition);
					}
				}
			}
			if (pegsList.get(i).time < numberHoles-1) {
				ArrayList<Integer> axiomA = new ArrayList<Integer>();
				axiomA.add(-1*pegsList.get(i).key);
				axiomA.add(pegsList.get(i+1).key);
				ArrayList<Integer> axiomB = new ArrayList<Integer>();
				axiomB.add(pegsList.get(i).key);
				axiomB.add(-1*pegsList.get(i+1).key);
				for (int j = 0; j < jumpsList.size(); j++) {
					if (jumpsList.get(j).time == pegsList.get(i).time) {
						if ((jumpsList.get(j).x == pegsList.get(i).hole) || (jumpsList.get(j).y == pegsList.get(i).hole))
							axiomA.add(jumpsList.get(j).key);
						else if (jumpsList.get(j).z == pegsList.get(i).hole) {
							axiomB.add(jumpsList.get(j).key);
						}
					}
				}
				frameAxiomsA.add(axiomA);
				frameAxiomsB.add(axiomB);
			}
		}
		endingState.add(end);
		
		
		
		for (int i = 0; i < causalAxioms.size(); i++) {
			clauses.add(causalAxioms.get(i));
		}
		for (int i = 0; i < frameAxiomsA.size(); i++) {
			clauses.add(frameAxiomsA.get(i));
		}
		for (int i = 0; i < frameAxiomsB.size(); i++) {
			clauses.add(frameAxiomsB.get(i));
		}
		for (int i = 0; i < actions.size(); i++) {
			clauses.add(actions.get(i));
		}
		for (int i = 0; i < startingState.size(); i++) {
			clauses.add(startingState.get(i));
		}
		for (int i = 0; i < endingState.size(); i++) {
			clauses.add(endingState.get(i));
		}
		for (int i = 0; i < endingCondition.size(); i++) {
			clauses.add(endingCondition.get(i));
		}
		
		BufferedWriter writer = new BufferedWriter (new FileWriter("frontEndOutput.txt"));
	    
		for (int a = 0; a < clauses.size(); a++) {
			for (int b = 0; b < clauses.get(a).size(); b++) {
				writer.write(clauses.get(a).get(b) + " ");
			}
			writer.newLine();
		}
		writer.write("0");
		writer.newLine();
		for (int i = 0; i < jumpsList.size(); i++) {
			writer.write(jumpsList.get(i).key + " " + jumpsList.get(i).readable);
			writer.newLine();
		}
		for (int i = 0; i < pegsList.size(); i++) {
			writer.write(pegsList.get(i).key + " " + pegsList.get(i).readable);
			if (i != pegsList.size()-1)
				writer.newLine();
		}
		writer.close();
	}
}