import java.util.Random;

public class Pattern {

	boolean[][] blocks;
	boolean[] up, right, down, left;

	public Pattern() {
		blocks = patternList();
		up = new boolean[5];
		right = new boolean[5];
		down = new boolean[5];
		left = new boolean[5];
		
		
		for (int i = 0; i < 5; i++) {
			up[i] = blocks[i][0];
			right[i] = blocks[4][i];
			down[i] = blocks[i][4];
			left[i] = blocks[0][i];
		}
	}

	public boolean isCompatible(Pattern pattern, int direction) {
		// 0 - UP
		// 1 - DOWN
		// 2 - RIGHT
		// 3 - LEFT
		boolean[] a;
		boolean[] b;
		switch (direction) {
		case 0: a = this.up; b = pattern.down; break;
		case 1: a = this.down; b = pattern.up; break;
		case 2: a = this.right; b = pattern.left; break;
		default: a = this.left; b = pattern.right; break;
		}
		for (int i = 0; i < 5; i++){
			if ((a[i] == true) && (b[i] == true)) {
				return true;
			}
		}
		return false;
	}

	public boolean[][] patternList() {
		
		int n = 5;
		boolean [][][] pList = new boolean[n][5][5];
		
		pList[0][0] = stringToRow("10010");
		pList[0][1] = stringToRow("10000");
		pList[0][2] = stringToRow("00110");
		pList[0][3] = stringToRow("10010");
		pList[0][4] = stringToRow("10110");
		

		pList[1][0] = stringToRow("10011");
		pList[1][1] = stringToRow("10000");
		pList[1][2] = stringToRow("00000");
		pList[1][3] = stringToRow("00010");
		pList[1][4] = stringToRow("10110");

		pList[2][0] = stringToRow("10101");
		pList[2][1] = stringToRow("10100");
		pList[2][2] = stringToRow("00101");
		pList[2][3] = stringToRow("10001");
		pList[2][4] = stringToRow("11101");

		pList[3][0] = stringToRow("00100");
		pList[3][1] = stringToRow("00100");
		pList[3][2] = stringToRow("00000");
		pList[3][3] = stringToRow("00100");
		pList[3][4] = stringToRow("00100");
		

		pList[4][0] = stringToRow("11000");
		pList[4][1] = stringToRow("10000");
		pList[4][2] = stringToRow("00000");
		pList[4][3] = stringToRow("00011");
		pList[4][4] = stringToRow("00011");
		
		Random random = new Random();
		return pList[random.nextInt(n)];
	}

	private boolean[] stringToRow(String string) {
		boolean[] b = new boolean[string.length()];
		for (int i = 0; i < string.length(); i++){
			if (string.charAt(i) == '0') {
				b[i] = true;
			} else {
				b[i] = false;
			}
		}
		return b;
	}
}
