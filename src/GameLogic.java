import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class GameLogic {

	protected final int winCondition = 2048;
	protected Random r = new Random();
	protected int[] newNumbers = {2,4};
	
	protected int brdSize;
	protected int[][] board;
	public GameLogic(int brdSize) {
		this.brdSize = brdSize;
		board = new int[brdSize][brdSize];
		generateNewNumbers();
	}
	
	/*
	 * For test only
	 */
	protected GameLogic(int[][] initialBoard) {
		this.brdSize = initialBoard.length;
		board = initialBoard;
	}
	
	public int[][] getBoard() {
		return board;
	}
	
	protected int[][] move(int[][] board) {
		boolean boardChanged = false;
		for (int i = 0; i < brdSize; i ++) {
			int[] line = mergeLine(board[i]);
			if(equals(line,board[i])==false) {
				boardChanged = true;
				board[i] = line;
			}
		}
		if (boardChanged)
			generateNewNumbers();
		return board;
	}
	
	protected boolean equals(int[] l1, int[] l2) {
		if(l1 == l2) return true;
		for (int i = 0; i < brdSize; i ++)
			if(l1[i] != l2[i]) return false;
		return true;
	}
	
	protected int[][] copy(int[][] oldBoard) {
		int[][] newBoard = new int[brdSize][brdSize];
		for (int y = 0; y < brdSize; y ++)
			for (int x = 0; x < brdSize; x ++)
				newBoard[y][x] = oldBoard[y][x];
		return newBoard;
	}
	
	protected int[][] moveLeft() {
		return move(board);
	}
	
	protected int[][] moveRight() {
		BlockRotate.rotateClockwise180(board);
		move(board);
		BlockRotate.rotateClockwise180(board);
		return board;
	}
	
	protected int[][] moveUp() {
		BlockRotate.rotateClockwise270(board);
		move(board);
		BlockRotate.rotateClockwise90(board);
		return board;
	}
	
	protected int[][] moveDown() {
		BlockRotate.rotateClockwise90(board);
		move(board);
		BlockRotate.rotateClockwise270(board);
		return board;
	}
	
	protected void generateNewNumbers() {
		// Name a list of 0 positions on the block
		ArrayList<Integer> zeros = new ArrayList<Integer>();
		for (int y = 0; y < brdSize; y ++)
			for (int x = 0; x < brdSize; x ++)
				if (board[y][x] == 0) zeros.add(y * brdSize + x);
		if (zeros.size() == 0) return;
		zeros = generateNewNumberUtil(zeros);
		if (zeros.size() == 0) return;
		generateNewNumberUtil(zeros);
	}
	
	public boolean getWon() {
		for (int y = 0; y < brdSize; y ++) 
			for (int x = 0; x < brdSize; x ++)
				if (board[y][x] == winCondition) return true;
		return false;
	}
	
	public boolean getLose() {
		if(!isFull()) return false;
		for (int y = 0; y < brdSize - 1; y ++) 
			for (int x = 0; x < brdSize - 1; x ++) 
				if (board[y][x] == board[y][x+1] || board[y][x] == board[y+1][x]) return false;
		if (board[brdSize-2][brdSize-1] == board[brdSize-1][brdSize-1] ||
			board[brdSize-1][brdSize-2] == board[brdSize-1][brdSize-1]) return false;
		return true;
	}
	
	protected boolean isFull() {
		for (int y = 0; y < brdSize; y ++) 
			for (int x = 0; x < brdSize; x ++)
				if (board[y][x] == 0) return false;
		return true;		
	}
	
	
	protected ArrayList<Integer> generateNewNumberUtil(ArrayList<Integer> blanks) {
		int idx = r.nextInt(blanks.size());
		int num = blanks.get(idx);
		board[num / brdSize][num % brdSize] = newNumbers[r.nextInt(2)];
		blanks.remove(idx);
		return blanks;
	}
	
	
	/*
	 * Merge a line leftward
	 * input: brdSize array
	 * return: solved brdSize array
	 */
	protected int[] mergeLine(int[] line) {
		LinkedList<Integer> l1 = new LinkedList<Integer> ();
		LinkedList<Integer> l2 = new LinkedList<Integer> ();
		// A list containing numbers a,b,c,d,..., combine any two consecutive numbers into their sum if they are equal. 
		for (int i = 0; i < brdSize; i ++) {
			if (line[i] > 0) l1.offer(line[i]);
		}
		if (l1.size() == 0) return line;
		int i1 = l1.poll();
		while(l1.size() > 0) {
			int i2 = l1.poll();
			if (i1 == i2) {
				l2.offer(i1*2);
				if (l1.size() > 0) {
					i1 = l1.poll();
				} else {
					i1 = 0; // Indicate that the last element is paired
					break;
				}
			} else {
				l2.offer(i1);
				i1 = i2;
			}
		} 
		if (i1 > 0) l2.offer(i1); // If the last element is not paired
		int[] newLine = new int[brdSize];
		int i;
		int s = l2.size();
		for (i = 0; i < s; i ++)
			newLine[i] = l2.poll();
		for (;i < brdSize; i ++)
			newLine[i] = 0;
		return newLine;
	}
	
	protected static void prn(int[] i) {
		for (int ii : i) System.out.print(ii + ",");
		System.out.println();
	
	}
	
	protected static void mergeTestCase(int size, int[] array) {
		GameLogic b = new GameLogic(size);
		/*
		 * The maximum number of valid combines is size - 1 {2,2,4,8,...}
		 */
		for (int i = 0; i < size - 1; i ++) {
			prn(array);
			array = b.mergeLine(array);
		} prn(array);
	}
	
	protected static void gameTestCase(int[] moves, int[][] puzzle) {
		GameLogic g = new GameLogic (puzzle);
		for (int move : moves) {
			BlockRotate.print(g.board);
			switch (move) {
			case 0: g.moveLeft(); break;
			case 1: g.moveUp(); break;
			case 2: g.moveRight(); break;
			case 3: g.moveDown(); break;
			}
		} BlockRotate.print(g.board);
	}
	
	public static void main(String[] args) {
//		mergeTestCase(3,new int[] {2,2,4});
//		mergeTestCase(4,new int[] {4,4,2,2});
//		mergeTestCase(4,new int[] {2,4,0,4});		
//		mergeTestCase(4,new int[] {0,0,2,2});
//		mergeTestCase(4,new int[] {16,8,0,8});
//		mergeTestCase(5,new int[] {16,8,4,2,2});
//		mergeTestCase(10,new int[] {16,0,8,0,4,0,2,0,2,0});
		int[][] game = {{2,2,2,2},{4,4,4,4},{8,8,8,8},{16,16,16,16}};
		gameTestCase(new int[] {0,2,0,2,0,2}, game);
	
	}

}
