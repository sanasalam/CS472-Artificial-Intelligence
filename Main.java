import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		
		String test = args[0];
		Object[] output = extractExpression (test, 0);
		
		Tree tree = (Tree) output[0];
		String source = null;
		String destination = null;
		String object = null;
		
		for (int i = 0; i < tree.children.size(); i++) {
			int symbols = 0;
			if (tree.children.get(i).root.equals("VP")) {
				for(int j = 0; j < tree.children.get(i).children.size(); j++) {
					if (tree.children.get(i).children.get(j).root.equals("Verb")) {
						String verb = tree.children.get(i).children.get(j).children.get(0).root;
						
						if ((verb.equals("gave")) || (verb.equals("got"))) {
							symbols++;
							System.out.println("transfer(X1)");
						}	
						else {
							symbols = symbols + 2;
							System.out.println("transfer(X1)");
							System.out.println("transfer(X2)");
						}
					}
					if (tree.children.get(i).children.get(j).root.equals("NP")) {
						symbols++;
						for (int k = 0; k < tree.children.get(i).children.get(j).children.size(); k++) {
							Tree child = tree.children.get(i).children.get(j).children.get(k);
							if (child.root.equals("Adj")) {
								System.out.println(child.children.get(0).root + "(X" + (symbols) + ")");
							}
							if (child.root.equals("Noun")) {
								object = "X" + symbols;
								System.out.println(child.children.get(0).root + "(X" + (symbols) + ")");
							}
						}
					}
					if (tree.children.get(i).children.get(j).root.equals("PP")) {
						for (int k = 0; k < tree.children.get(i).children.get(j).children.size(); k++)
							if (tree.children.get(i).children.get(j).children.get(k).root.equals("NP")) {
								symbols++;
								for (int l = 0; l < tree.children.get(i).children.get(j).children.get(k).children.size(); l++) {
										Tree child = tree.children.get(i).children.get(j).children.get(k).children.get(l);
									if (child.root.equals("Adj")) {
										System.out.println(child.children.get(0).root + "(X" + (symbols) + ")");
									}
									if (child.root.equals("Noun")) {
										destination = "X" + symbols;
										System.out.println(child.children.get(0).root + "(X" + (symbols) + ")");
									}
							}
						}
					}
				}
			}
			else if (tree.children.get(i).root.equals("NP")) {
				for (int j = 0; j < tree.children.get(i).children.size(); j++) {
					if (tree.children.get(i).children.get(j).root.equals("Name")) {
						source = tree.children.get(i).children.get(j).children.get(0).root;
					}
				}
			}
		}
		System.out.println("source(X1, " + source + ")");
		System.out.println("destination(X1, " + destination + ")");
		System.out.println("object(X1, " + destination + ")");
		
	}
	
	public static Object[] extractSymbol(String s, int k) {
		
		Object[] node = new Object[2];
	    while (whiteSpace(s.charAt(k))) {
	    		k++;   
	    }
	    if (s.charAt(k) == '(' || s.charAt(k) == ',' || s.charAt(k) == ')') {
	    		node[0] = Character.toString(s.charAt(k));
	    		node[1] = k+1;
	    		return node;
	    }
	    if (!Character.isLetter((s.charAt(k))))
	    		System.out.println("Invalid input at index " + k);
	    String m = "";
	    while (Character.isLetter((s.charAt(k)))) {
	    		m = m + Character.toString(s.charAt(k));
	    		k++;
	    }
	    node[0] = m;
	    node[1] = k;
	    return node;
	}
	
	public static Object[] extractExpression(String s, int k) {
		
		Object [] returnode = new Object [2];
		Object [] node = extractSymbol(s,k);
		String m = (String) node[0];
		int q = (int) node[1];
	    if (!(alphabeticalString(m))) 
	    		System.out.println(("Invalid input at index " + k));
	    ArrayList<Tree> emptyList = new ArrayList<Tree>();
	    Tree t = new Tree(m, emptyList);
	    Object [] node2 = extractSymbol(s,q);
	    String n = (String) node2[0];
	    int peek = (int) node2[1];
	    
	    if (!(n.equals("("))) {
	    	 	returnode[0] = t;
	    	 	returnode [1] = q;
	    	 	return returnode;
	     }
	    	
	    q = peek;
	    do {
	    	 	Object[] expression = extractExpression(s,q);
	    	 	Tree c = (Tree) expression[0];
	    	 	int q2 = (int) expression[1];
	    	 	t.addChild(c);
	    	 	Object[] symbol = extractSymbol(s, q2);
	    	 	n = (String) symbol[0];
	    	 	q = (int) symbol[1];
	    	 	if (n.equals(")")) {
	    	 		returnode[0] = t;
	    	 		returnode[1] = q;
	    	 		return returnode;
	    	 	}
	    	 	if (!(n.equals(",")))
	    	 		System.out.println("Invalid input at index " + q);
	     } while (n.equals(","));
	     return null;
	}
	
	private static boolean alphabeticalString(String m) {
		
		for (int i = 0; i < m.length(); i++) {
			if (!(Character.isLetter(m.charAt(i))))
				return false;
		}
		return true;
	}

	public static boolean whiteSpace(char input) {
		if (input == ' ')
			return true;
		return false;
	}
}