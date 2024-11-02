import java.util.ArrayList;

public class Atom {
	
	int hole;
	int time;
	int key;
	int x;
	int y;
	int z;
	String readable;
	

	public Atom (int h, int t, int k) {
		hole = h;
		time = t;
		key = k;
		readable = "Peg(" + h + "," + t + ")";
	}
	
	public Atom (int xPos, int yPos, int zPos, int t, int k) {
		x = xPos;
		y = yPos;
		z = zPos;
		time = t;
		key = k;
		readable = "Jump(" + x + "," + y + "," + z + "," + time + ")";
	}
}