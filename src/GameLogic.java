import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class GameLogic {
	protected final int winCondition = 2048;
	protected final int[] newNumbers = {2,4};
	protected Random r = new Random();
	protected boolean lose = false, win = false;
	protected int[][][] predictedBoards;
	protected boolean[] predictedMovable;
	
	protected int brdSize;
	protected int[][] board;
	public GameLogic(int brdSize) {
		this.brdSize = brdSize;
		board = new int[brdSize][brdSize];
		predictedBoards = new int[4][][];
		predictedMovable = new boolean[4];
		generateNewNumbers();
		predict();
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
		for (int i = 0; i < brdSize; i ++) {
			board[i] = mergeLine(board[i]);
		}
		generateNewNumbers();		
		return board;
	}
	
	protected int[][] copy(int[][] oldBoard) {
		int[][] newBoard = new int[brdSize][brdSize];
		for (int y = 0; y < brdSize; y ++)
			for (int x = 0; x < brdSize; x ++)
				newBoard[y][x] = oldBoard[y][x];
		return newBoard;
	}
	
	protected int[][] moveLeft() {
		return move(copy(board));
	}
	
	protected int[][] moveRight() {
		int[][] nBrd = copy(board);
		BlockRotate.rotateClockwise180(nBrd);
		nBrd = move(nBrd);
		BlockRotate.rotateClockwise180(nBrd);
		return nBrd;
	}
	
	protected int[][] moveUp() {
		int[][] nBrd = copy(board);
		BlockRotate.rotateClockwise270(nBrd);
		nBrd = move(nBrd);
		BlockRotate.rotateClockwise90(nBrd);
		return nBrd;
	}
	
	protected int[][] moveDown() {
		int[][] nBrd = copy(board);
		BlockRotate.rotateClockwise90(nBrd);
		nBrd = move(nBrd);
		BlockRotate.rotateClockwise270(nBrd);
		return nBrd;
	}
	
	public boolean hasLost() {
		return lose;
	}
	
	protected void checkWon() {
		for (int y = 0; y < brdSize; y ++)
			for (int x = 0; x < brdSize; x ++)
				if (board[y][x] == 2048) {
					win = true;
					return;
				}
		win = false;
	}
	
	protected boolean equals(int[][] b1, int[][] b2) {
		for (int y = 0; y < brdSize; y ++)
			for (int x = 0; x < brdSize; x ++)
				if (b1[y][x] != b2[y][x])
					return false;
		return true;
	}
		
	/*
	 * Check for the following conditions:
	 * win? check for value 2048 in grids
	 * canMoveLeft? cache the predicted board, if identical with current, set false
	 * can... 4 all together
	 * lose? if all 4 predictions are false
	 */
	protected void predict() {
		checkWon();
		if(equals(predictedBoards[0] = moveLeft(), board)) predictedMovable[0] = false;
		else predictedMovable[0] = true;
		
		if(equals(predictedBoards[1] = moveUp(), board)) predictedMovable[1] = false;
		else predictedMovable[1] = true;

		if(equals(predictedBoards[2] = moveRight(), board)) predictedMovable[2] = false;
		else predictedMovable[2] = true;
		
		if(equals(predictedBoards[3] = moveDown(), board)) predictedMovable[3] = false;
		else predictedMovable[3] = true;
		
		if(!predictedMovable[0]&&!predictedMovable[1]&&!predictedMovable[2]&&!predictedMovable[3]) lose = true;
	}
	
	
	
	protected void generateNewNumbers() {
		// Name a list of 0 positions on the block
		ArrayList<Integer> zeros = new ArrayList<Integer>();
		for (int y = 0; y < brdSize; y ++)
			for (int x = 0; x < brdSize; x ++)
				if (board[y][x] == 0) zeros.add(y * brdSize + x);
		if (zeros.size() == 0) {
			lose = true;
			return;
		}
		zeros = generateNewNumberUtil(zeros);
		if (zeros.size() == 0) {
			return;
		}
		generateNewNumberUtil(zeros);
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
