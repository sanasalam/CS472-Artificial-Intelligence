import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MDP {

	public static void main(String[] args) throws IOException {
		
		FileReader fileReader = new FileReader (args[0]);
		BufferedReader reader = new BufferedReader (fileReader);
		
		String line = null;
		line = reader.readLine();
		String [] splitLine = line.split("\\s+");
		
		int nonTStates = Integer.parseInt(splitLine[0]);
		int terminalStates = Integer.parseInt(splitLine[1]);
		int rounds = Integer.parseInt(splitLine[2]);
		int outputFrequency = Integer.parseInt(splitLine[3]);
		int M = Integer.parseInt(splitLine[4]);
		
		// Reading terminal states and their rewards
		
		ArrayList<ArrayList<Integer>> terminalStateRewards = new ArrayList<ArrayList<Integer>>();
		
		line = reader.readLine();
		splitLine = line.split("\\s+");
		
		int [] tRewards = new int [splitLine.length];
		
		for (int i = 0; i < splitLine.length; i++) {
			
			if (i%2 ==0) {
				ArrayList<Integer> newTerminalState = new ArrayList<Integer>();
				newTerminalState.add(Integer.parseInt(splitLine[i]));
				newTerminalState.add(Integer.parseInt(splitLine[i+1]));
				terminalStateRewards.add(newTerminalState);
			}
			
			tRewards[i] = Integer.parseInt(splitLine[i]);
		}
		
		// Creating non-terminal states and putting them into a list
		// Creating count and total data structures
		
		ArrayList<ArrayList<Integer>> countList = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> totalList = new ArrayList<ArrayList<Integer>>();
		
		ArrayList<State> nonTerminalStates = new ArrayList<State>();

		while (((line = reader.readLine()) != null)) {
			
			splitLine = line.split("\\s+");
			
			int state = Character.getNumericValue((splitLine[0].charAt(0)));

			if (state >= nonTerminalStates.size()) {
				State newState = new State (splitLine);
				nonTerminalStates.add(newState);
				ArrayList<Integer> count = new ArrayList<Integer>();
				count.add(0);
				countList.add(count);
				ArrayList<Integer> total = new ArrayList<Integer>();
				total.add(0);
				totalList.add(total);
			}
			else {
				countList.get(state).add(0);
				totalList.get(state).add(0);
				nonTerminalStates.get(state).addAction(splitLine);
			}
		}
		// Rounds Loop
		
		for (int i = 0; i < rounds; i++) {
			
			ArrayList<Integer> statesEncountered = new ArrayList<Integer>();
			ArrayList<Integer> actionsEncountered = new ArrayList<Integer>();
			
			State startState = nonTerminalStates.get(new Random().nextInt(nonTerminalStates.size()));
			int stateKey = 0;
			
			while (nonTerminalStates.contains(startState)) {
				
				statesEncountered.add(startState.key);
				
				int action = chooseAction (startState, countList, totalList, tRewards, M);
				
				actionsEncountered.add(action);
				
				int n = ((startState.actions.get(action).size())/2);
				
				double [] probabilities = new double[n];
				
				int index = 0;
				for (int j = 0; j < startState.actions.get(action).size(); j++) {

					if (j%2 != 0) {
						probabilities[index] = (double) startState.actions.get(action).get(j);
						index++;
					}
				}
				
				double [] u = new double[n];
				
				u[0] = probabilities[0];
				
				for (int k = 1; k < n; k++) {
					u[k] = u[k-1] + probabilities[k];
				}

				int next = randomDist (u, n);
				
				int statePos = 0;
				if (next !=0)
					statePos = 2*next;
				
				stateKey = (int) startState.actions.get(action).get(statePos);
				
				boolean check = false;
				
				for (int k = 0; k < nonTerminalStates.size(); k++) {
					if (nonTerminalStates.get(k).key == stateKey) {
						startState = nonTerminalStates.get(k);
						check = true;
					}
				}
				if (check == false)
					startState = null;
			}
			int reward = 0;	
			
			for (int b = 0; b < tRewards.length; b++) {
				if (b%2 ==0) {
					if (tRewards[b] == stateKey)
						reward = tRewards[b+1];
				}
			}
			
			for (int a = 0; a < statesEncountered.size(); a++) {
				
				int oldCount = countList.get(statesEncountered.get(a)).get(actionsEncountered.get(a));
				int newCount = oldCount + 1;
				
				countList.get(statesEncountered.get(a)).set(actionsEncountered.get(a), newCount);
				
				int oldTotal = totalList.get(statesEncountered.get(a)).get(actionsEncountered.get(a));
				int newTotal = oldTotal + reward;
				
				totalList.get(statesEncountered.get(a)).set(actionsEncountered.get(a), newTotal);
			}
			
			if (outputFrequency > 0) {
				if ((i+1) % outputFrequency == 0) {
				
					ArrayList<Integer> best = bestActions (countList, totalList);
				
					System.out.println ("After " + (i+1) + " rounds");
					System.out.println ("Count");
					for (int e = 0; e < countList.size(); e++) {
						for (int f = 0; f < countList.get(e).size(); f++) {
							System.out.print("[" + e + "," + f + "]" + " = ");
							System.out.print(countList.get(e).get(f) + " ");							
						}
						System.out.println();
					}
				
					System.out.println ();
					System.out.println ("Total");
					for (int e = 0; e < totalList.size(); e++) {
						for (int f = 0; f < totalList.get(e).size(); f++) {
							System.out.print("[" + e + "," + f + "]" + " = ");
							System.out.print(totalList.get(e).get(f) + " ");
						}
					System.out.println();
					}
					System.out.println();
					System.out.print("Best Action: ");
					for (int z = 0; z < best.size(); z++) {
						if (z%2 ==0) {
							System.out.print(best.get(z) + ":" + best.get(z+1) + " ");
						}
					}
					System.out.println();
					System.out.println();
				}
			}
	}
		if (outputFrequency ==0) {
			ArrayList<Integer> best = bestActions (countList, totalList);
			System.out.print("Best Action: ");
			for (int z = 0; z < best.size(); z++) {
				if (z%2 ==0) {
					System.out.print(best.get(z) + ":" + best.get(z+1) + " ");
				}
			}
			System.out.println();
		}
	}
	
	public static int chooseAction (State state, ArrayList<ArrayList<Integer>> count, ArrayList<ArrayList<Integer>> total, int[] rewards, int M) {
		
		
		int n = state.actions.size();
		
		for (int j = 0; j < count.get(state.key).size(); j++) {
			
			if (count.get(state.key).get(j) ==0)
				return j;
		}
		
		int[] average = new int[n];
		
		for (int k = 0; k < n; k++) {
			average[k] = ((total.get(state.key).get(k))/(count.get(state.key).get(k)));
		}
		
		// Get smallest and largest terminal state rewards
		
		int smallest = rewards[0];
		int largest = rewards[0];
		
		for (int m = 1; m < rewards.length; m++) {
			if (rewards[m] < smallest)
				smallest = rewards[m];
			else if (rewards[m] > largest)
				largest = rewards[m];
		}
		
		double[] saverage = new double[n];
		
		for (int p = 0; p < saverage.length; p++) {
			saverage[p] = 0.25 + 0.75*((average[p]-smallest)/(largest-smallest));
		}
		
		int c = 0;
		
		for (int q = 0; q < n; q++) {
			c = c + count.get(state.key).get(q);
		}
		
		double[] up = new double [n];
		
		for (int r = 0; r < n; r++) {
			up[r] = Math.pow(saverage[r],(c/M));
		}
		
		double norm = 0; 
		
		for (int s = 0; s < n; s++) {
			norm = norm + up[s];
		}
		
		double[] p = new double [n];
		
		for (int t = 0; t < n; t++) {
			p[t] = up[t]/norm;
		}
		
		double [] u = new double[n];
		u[0] = p[0];
		
		for (int v = 1; v < n; v++) {
			u[v] = u[v-1] + p[v];
		}
		
		return randomDist (u, n);
	}
		
		
	
	public static int randomDist (double [] u, int n) {
		double x = Math.random();
		
		for (int y = 0; y < n-1; y++) {
			if (x < u[y])
				return y;
		}
		return n-1;
	}
	
	public static ArrayList<Integer> bestActions (ArrayList<ArrayList<Integer>> countList, ArrayList<ArrayList<Integer>> totalList) {
		
		ArrayList<Integer> bestActions = new ArrayList<Integer>();
		
		for (int x = 0; x < countList.size(); x++) {
			boolean zero = false;
			double bestActionVal = 0;
			int bestActionIndex = 0;
			if (countList.get(x).get(0) != 0) {
				bestActionVal = ((double) totalList.get(x).get(0))/((double) countList.get(x).get(0));
				bestActionIndex = 0;
			}
			else {
				bestActions.add(x);
				bestActions.add(-1);
				zero = true;
			}
			for (int y = 1; y < countList.get(x).size(); y++) {
				
				if (countList.get(x).get(y) == 0) {	
					
					if (zero == false) {
						bestActions.add(x);
						bestActions.add(-1);
						zero = true;
					}
				}
				else {
					double currentActionVal = ((double) totalList.get(x).get(y))/((double) countList.get(x).get(y));
					if (currentActionVal > bestActionVal) {
						bestActionVal = currentActionVal;
						bestActionIndex = y;
					}
				}
			}
			if (zero == false) {
				bestActions.add(x);
				bestActions.add(bestActionIndex);
			}
		}
		return bestActions;
	}
}