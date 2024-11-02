import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BackEnd {

	public static void main(String[] args) throws IOException {
		
		FileReader fileReader = new FileReader (args[0]);
		BufferedReader reader = new BufferedReader (fileReader);
		
		// Get list of clauses from input
		ArrayList<ArrayList<String>> truthVals = new ArrayList<ArrayList<String>>();
		
		String line = null;
		while ((!(line = reader.readLine()).equals("0"))) {
			ArrayList<String> individual = new ArrayList<String>();
			String[] splitLine = line.split("\\s+");
			for (int i = 0; i < splitLine.length; i++) {
				individual.add(splitLine[i]);
			}
			truthVals.add(individual);
		}
		
		// Store information after "0" for writing to file later
		ArrayList<ArrayList<String>> key = new ArrayList<ArrayList<String>>();
		while ((line = reader.readLine()) != null) {
			ArrayList<String> component = new ArrayList<String>();
			String[] splitLine = line.split("\\s+");
			for (int i = 0; i < splitLine.length; i++) {
				component.add(splitLine[i]);
			}
			key.add(component);
		}
		
		ArrayList<String> output = new ArrayList<String>();
		
		for (int i = 0; i < key.size(); i++) {
			if (key.get(i).get(1).charAt(0) == 'J') {
				if (truthVals.get(i).get(1).equals("T")) {
					output.add(key.get(i).get(1));
				}
			}
		}
		BufferedWriter writer = new BufferedWriter (new FileWriter("backEndOutput.txt"));
		
		for (int j = 0; j < output.size(); j++) {
			writer.write(output.get(j));
			writer.newLine();
		}
		reader.close();
		writer.close();
	}
}
