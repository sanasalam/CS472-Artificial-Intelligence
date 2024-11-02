import java.util.ArrayList;

public class State {
	
	public int key;
	public ArrayList<ArrayList<Number>> actions;
	
	public State (String[] input) {
		
		actions = new ArrayList<ArrayList<Number>>();
		key = Character.getNumericValue(input[0].charAt(0));
		
		ArrayList<Number> probabilities = new ArrayList<Number>();
		
		for (int i = 1; i < input.length; i++) {
			if (i%2 ==0)
				probabilities.add((Double.parseDouble(input[i])));
			else
				probabilities.add(Integer.parseInt(input[i]));
		}
		actions.add(probabilities);	
}
	
	public void addAction (String[] input) {
		
		ArrayList<Number> probabilities = new ArrayList<Number>();
		
		for (int i = 1; i < input.length; i++) {
			if (i%2 ==0)
				probabilities.add((Double.parseDouble(input[i])));
			else
				probabilities.add(Integer.parseInt(input[i]));
		}
		actions.add(probabilities);	
	}
}