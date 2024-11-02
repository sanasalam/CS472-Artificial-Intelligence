import java.util.ArrayList;

public class Tree {
	
	String root;
	ArrayList<Tree> children = new ArrayList<Tree>();

	public Tree (String input, ArrayList<Tree> inputList) {
		root = input;
		children = inputList;
	}

	public void addChild(Tree c) {
		children.add(c);
	}
	
}
