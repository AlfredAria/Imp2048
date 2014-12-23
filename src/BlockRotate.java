

public class BlockRotate {
	protected static int l;
	protected static int[][] mblock;
	
	protected static void setup(int[][] block) {
		l = block.length;
		mblock = block;	
	}

	public static void rotateClockwise90(int[][] block) {
		setup(block);
		flipVertical();
		flipDiagnol();
	}
	
	public static void rotateClockwise270(int[][] block) {
		setup(block);
		flipHorizontal();
		flipDiagnol();
	}
	
	public static void rotateClockwise180(int[][] block) {
		setup(block);
		flipHorizontal();
		flipVertical();
	}
	
	protected static void flipVertical() {
		for (int y = 0; y < l/2.0; y ++) {
			for (int x = 0; x < l; x ++) {
				swap(y,x,l-y-1,x);
			}
		}
	}
	
	protected static void flipHorizontal() {
		for (int y = 0; y < l; y ++) {
			for (int x = 0; x < l/2.0; x ++) {
				swap(y,x,y,l-x-1);
			}
		}
	}
	
	protected static void flipDiagnol() {
		for (int y = 0; y < l; y ++) {
			for (int x = 0; x < y; x ++) {
				swap(y,x,x,y);
			}
		}
	}
	
	protected static void swap(int y1, int x1, int y2, int x2) {
		int tmp = mblock[y1][x1];
		mblock[y1][x1] = mblock[y2][x2];
		mblock[y2][x2] = tmp;
	}
	
	public static void print(int[][] block) {
		for (int[] b : block) {
			for (int i : b) {
				System.out.print(i + " ");
			} System.out.println();
		}System.out.println();
	}
	
	public static void main(String[] args) {
		int[][] a = {{1,2,3},{4,5,6},{7,8,9}};
		print(a);
		rotateClockwise90(a);print(a);
		rotateClockwise270(a);print(a);
		rotateClockwise180(a);print(a);
		rotateClockwise180(a);print(a);	
	}

}
