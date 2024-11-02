import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class NaiveBayes {

	public static void main(String[] args) throws IOException {
		
		FileReader fileReader = new FileReader ("prog4Data.csv");
		BufferedReader reader = new BufferedReader (fileReader);

		int rows = 1000;
		int columns = 6;
		
		int attributes = 5;
		int attributeVals = 4;
		int classifications = 3;
		
		String[][] input = new String[rows][columns];
		String line;
		
		reader.readLine();
		
		int index = 0;
		
		while ((line = reader.readLine()) != null) {
			
			String[] row = line.split(",");
			input[index] = row;
			
			index++;
		}
		
		int training = Integer.parseInt(args[0]);
		int testing = Integer.parseInt(args[1]);
		boolean verbose = false;
		if (args.length ==3) {
			if (args[2].equals("-v"))
					verbose = true;
		}
		
		// Tally classifications
		
		int ones = 0;
		int twos = 0;
		int threes = 0;
		
		int [] a1 = new int [12];
		int [] a2 = new int [12];
		int [] a3 = new int [12];
		int [] a4 = new int [12];
		int [] a5 = new int [12];
		
		int [] one = new int [20];
		int [] two = new int [20];
		int [] three = new int [20];
		
		int oneIndex = 0;
		int twoIndex = 0;
		int threeIndex = 0;
		
		for (int i = 0; i < training; i++) {
			if (Integer.parseInt(input[i][5]) == 1) {
				ones++;
				if (Integer.parseInt(input[i][0]) == 1)
					a1[0]++;
				else if (Integer.parseInt(input[i][0]) == 2)
					a1[1]++;
				else if (Integer.parseInt(input[i][0]) == 3)
					a1[2]++;
				else if (Integer.parseInt(input[i][0]) == 4)
					a1[3]++;
				if (Integer.parseInt(input[i][1]) == 1)
					a2[0]++;
				else if (Integer.parseInt(input[i][1]) == 2)
					a2[1]++;
				else if (Integer.parseInt(input[i][1]) == 3)
					a2[2]++;
				else if (Integer.parseInt(input[i][1]) == 4)
					a2[3]++;
				if (Integer.parseInt(input[i][2]) == 1)
					a3[0]++;
				else if (Integer.parseInt(input[i][2]) == 2)
					a3[1]++;
				else if (Integer.parseInt(input[i][2]) == 3)
					a3[2]++;
				else if (Integer.parseInt(input[i][2]) == 4)
					a3[3]++;
				if (Integer.parseInt(input[i][3]) == 1)
					a4[0]++;
				else if (Integer.parseInt(input[i][3]) == 2)
					a4[1]++;
				else if (Integer.parseInt(input[i][3]) == 3)
					a4[2]++;
				else if (Integer.parseInt(input[i][3]) == 4)
					a4[3]++;
				if (Integer.parseInt(input[i][4]) == 1)
					a5[0]++;
				else if (Integer.parseInt(input[i][4]) == 2)
					a5[1]++;
				else if (Integer.parseInt(input[i][4]) == 3)
					a5[2]++;
				else if (Integer.parseInt(input[i][4]) == 4)
					a5[3]++;
			}
			else if (Integer.parseInt(input[i][5]) == 2) {
				twos++;
				if (Integer.parseInt(input[i][0]) == 1)
					a1[4]++;
				else if (Integer.parseInt(input[i][0]) == 2)
					a1[5]++;
				else if (Integer.parseInt(input[i][0]) == 3)
					a1[6]++;
				else if (Integer.parseInt(input[i][0]) == 4)
					a1[7]++;
				if (Integer.parseInt(input[i][1]) == 1)
					a2[4]++;
				else if (Integer.parseInt(input[i][1]) == 2)
					a2[5]++;
				else if (Integer.parseInt(input[i][1]) == 3)
					a2[6]++;
				else if (Integer.parseInt(input[i][1]) == 4)
					a2[7]++;
				if (Integer.parseInt(input[i][2]) == 1)
					a3[4]++;
				else if (Integer.parseInt(input[i][2]) == 2)
					a3[5]++;
				else if (Integer.parseInt(input[i][2]) == 3)
					a3[6]++;
				else if (Integer.parseInt(input[i][2]) == 4)
					a3[7]++;
				if (Integer.parseInt(input[i][3]) == 1)
					a4[4]++;
				else if (Integer.parseInt(input[i][3]) == 2)
					a4[5]++;
				else if (Integer.parseInt(input[i][3]) == 3)
					a4[6]++;
				else if (Integer.parseInt(input[i][3]) == 4)
					a4[7]++;
				if (Integer.parseInt(input[i][4]) == 1)
					a5[4]++;
				else if (Integer.parseInt(input[i][4]) == 2)
					a5[5]++;
				else if (Integer.parseInt(input[i][4]) == 3)
					a5[6]++;
				else if (Integer.parseInt(input[i][4]) == 4)
					a5[7]++;
			}
			else {
				threes++;
				if (Integer.parseInt(input[i][0]) == 1)
					a1[8]++;
				else if (Integer.parseInt(input[i][0]) == 2)
					a1[9]++;
				else if (Integer.parseInt(input[i][0]) == 3)
					a1[10]++;
				else if (Integer.parseInt(input[i][0]) == 4)
					a1[11]++;
				if (Integer.parseInt(input[i][1]) == 1)
					a2[8]++;
				else if (Integer.parseInt(input[i][1]) == 2)
					a2[9]++;
				else if (Integer.parseInt(input[i][1]) == 3)
					a2[10]++;
				else if (Integer.parseInt(input[i][1]) == 4)
					a2[11]++;
				if (Integer.parseInt(input[i][2]) == 1)
					a3[8]++;
				else if (Integer.parseInt(input[i][2]) == 2)
					a3[9]++;
				else if (Integer.parseInt(input[i][2]) == 3)
					a3[10]++;
				else if (Integer.parseInt(input[i][2]) == 4)
					a3[11]++;
				if (Integer.parseInt(input[i][3]) == 1)
					a4[8]++;
				else if (Integer.parseInt(input[i][3]) == 2)
					a4[9]++;
				else if (Integer.parseInt(input[i][3]) == 3)
					a4[10]++;
				else if (Integer.parseInt(input[i][3]) == 4)
					a4[11]++;
				if (Integer.parseInt(input[i][4]) == 1)
					a5[8]++;
				else if (Integer.parseInt(input[i][4]) == 2)
					a5[9]++;
				else if (Integer.parseInt(input[i][4]) == 3)
					a5[10]++;
				else if (Integer.parseInt(input[i][4]) == 4)
					a5[11]++;
			}
		}
		
		int [] a6 = new int [3];
		a6 [0] = ones;
		a6 [1] = twos;
		a6 [2] = threes;
		
		if (verbose) {
		for (int i = 0; i < classifications; i++) {
			double lp = -((Math.log ((a6[i] + 0.1)/(training + 0.3)))/Math.log(2));
			
			System.out.format("%.4f", lp);
			System.out.print("    ");
		}
		System.out.println();
		System.out.println();
		
		double lp2;
		
		for (int i = 0; i < attributes; i++) {
			for (int j = 0; j < 12; j++) {
				if (i ==0) {
					if (j < 4)
						lp2 = -((Math.log ((a1[j] + 0.1)/(ones + 0.4)))/Math.log(2));
					else if (j < 8)
						lp2 = -((Math.log ((a1[j] + 0.1)/(twos + 0.4)))/Math.log(2));
					else
						lp2 = -((Math.log ((a1[j] + 0.1)/(threes + 0.4)))/Math.log(2));
					
					System.out.format("%.4f", lp2);
					System.out.print("    ");
					if (j==3 || j==7 || j==11)
						System.out.println();
					if (j==11)
						System.out.println();
				}
				else if (i ==1) {
					if (j < 4)
						lp2 = -((Math.log ((a2[j] + 0.1)/(ones + 0.4)))/Math.log(2));
					else if (j < 8)
						lp2 = -((Math.log ((a2[j] + 0.1)/(twos + 0.4)))/Math.log(2));
					else
						lp2 = -((Math.log ((a2[j] + 0.1)/(threes + 0.4)))/Math.log(2));
					
					System.out.format("%.4f", lp2);
					System.out.print("    ");
					if (j==3 || j==7 || j==11)
						System.out.println();
					if (j==11)
						System.out.println();
				}
				else if (i ==2) {
					if (j < 4)
						lp2 = -((Math.log ((a3[j] + 0.1)/(ones + 0.4)))/Math.log(2));
					else if (j < 8)
						lp2 = -((Math.log ((a3[j] + 0.1)/(twos + 0.4)))/Math.log(2));
					else
						lp2 = -((Math.log ((a3[j] + 0.1)/(threes + 0.4)))/Math.log(2));
					
					System.out.format("%.4f", lp2);
					System.out.print("    ");
					if (j==3 || j==7 || j==11)
						System.out.println();
					if (j==11)
						System.out.println();
				}
				else if (i ==3) {
					if (j < 4)
						lp2 = -((Math.log ((a4[j] + 0.1)/(ones + 0.4)))/Math.log(2));
					else if (j < 8)
						lp2 = -((Math.log ((a4[j] + 0.1)/(twos + 0.4)))/Math.log(2));
					else
						lp2 = -((Math.log ((a4[j] + 0.1)/(threes + 0.4)))/Math.log(2));
					
					System.out.format("%.4f", lp2);
					System.out.print("    ");
					if (j==3 || j==7 || j==11)
						System.out.println();
					if (j==11)
						System.out.println();
				}
				else if (i ==4) {
					if (j < 4)
						lp2 = -((Math.log ((a5[j] + 0.1)/(ones + 0.4)))/Math.log(2));
					else if (j < 8)
						lp2 = -((Math.log ((a5[j] + 0.1)/(twos + 0.4)))/Math.log(2));
					else
						lp2 = -((Math.log ((a5[j] + 0.1)/(threes + 0.4)))/Math.log(2));
					
					System.out.format("%.4f", lp2);
					System.out.print("    ");
					if (j==3 || j==7)
						System.out.println();
				}
			}
		}
		}
		
		int [] complete = new int[60];
		for (int i = 0; i < complete.length; i++) {
			if (i < 12)
				complete[i] = a1[i];
			else if (i < 24)
				complete[i] = a2[i-12];
			else if (i < 36)
				complete[i] = a3[i-24];
			else if (i < 48)
				complete[i] = a4[i-36];
			else
				complete[i] = a5[i-48];
		}
		
		int [] testClassifications = new int [testing];
		int testIndex = 0;
		
		for (int i = input.length-testing; i < input.length; i++) {
			double min = Double.MAX_VALUE;
			int minIndex = -1;
			for (int j = 0; j < classifications; j++) {
				double sum = 0;
				for (int k = 0; k < attributes; k++) {
					int attributeVal = Integer.parseInt(input[i][k]);
					int indexComplete = 12*k + 4*j + (attributeVal-1);
					if (j == 0) {
						sum = sum + -((Math.log ((complete[indexComplete] + 0.1)/(ones + 0.4)))/Math.log(2));
					}
					else if (j == 1)
						sum = sum + -((Math.log ((complete[indexComplete] + 0.1)/(twos + 0.4)))/Math.log(2));
					else if (j == 2)
						sum = sum + -((Math.log ((complete[indexComplete] + 0.1)/(threes + 0.4)))/Math.log(2));
				}
				sum = sum + -((Math.log ((a6[j] + 0.1)/(training + 0.3)))/Math.log(2));
				if (sum <= min) {
					
					min = sum;
					minIndex = j;
				}
			}
			testClassifications[testIndex] = minIndex+1;
			testIndex++;
		}
		int indexTC = 0;
		double accurate = 0;
		double threeAccurate = 0;
		double threeReturn = 0;
		double threeCorrect = 0;
		
		for (int i = input.length-testing; i < input.length; i++) {
			if (testClassifications[indexTC] == Integer.parseInt(input[i][5]))
				accurate++;
			if (testClassifications[indexTC] == 3)
				threeReturn++;
			if (Integer.parseInt(input[i][5]) == 3)
				threeCorrect++;
			if ((testClassifications[indexTC] == 3) && (Integer.parseInt(input[i][5]) == 3))
				threeAccurate++;
			indexTC++;
		}
		
		double accuracy = accurate/testing;
		double precision = threeAccurate/threeReturn;
		double recall = threeAccurate/threeCorrect;
		if (verbose) {
			System.out.println();
			System.out.println();
		}
		System.out.print("Accuracy = ");
		System.out.format("%.4f", accuracy);
		System.out.print("    ");
		System.out.print("Precision = ");
		if (threeReturn ==0)
			System.out.print(threeAccurate + "/" + threeReturn);
		else
			System.out.format("%.4f", precision);
		System.out.print("    ");
		System.out.print("Recall = ");
		if (threeCorrect ==0)
			System.out.print(threeAccurate + "/" + threeCorrect);
		else
			System.out.format("%.4f", recall);
		System.out.println();
	}
}