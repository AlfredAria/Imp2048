import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class GamePanel extends JPanel implements KeyListener {

	protected final int boardSize = 3;
	protected GameLogic logic = new GameLogic(boardSize);
	protected int[][] boardData = new int[boardSize][boardSize];
	
	public GamePanel() {
		this.setOpaque(true);
		this.setVisible(true);
		boardData = logic.getBoard();
		this.repaint();
	}
	
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getKeyCode()) {
		case KeyEvent.VK_LEFT:  
			boardData = logic.moveLeft();
			break;  
		case KeyEvent.VK_RIGHT:  
			boardData = logic.moveRight();
			break;  
		case KeyEvent.VK_UP:  
			boardData = logic.moveUp();
			break;  
		case KeyEvent.VK_DOWN:  
			boardData = logic.moveDown();
			break;
		}
		repaint();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		BlockRotate.print(boardData);
		if (logic.getLose()) System.out.println("Lost.");
		if (logic.getWon()) System.out.println("Won.");
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				GamePanel panel = new GamePanel(); 
				frame.setSize(400, 400);
				frame.addKeyListener(panel);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setContentPane(panel);
				frame.setVisible(true);
			}
		});

	}

}
