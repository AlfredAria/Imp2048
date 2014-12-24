import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class GamePanel extends JPanel implements KeyListener {

	// Game logic components
	protected final int boardSize = 4;
	protected int[][]   boardData = new int[boardSize][boardSize];
	protected GameLogic logic = new GameLogic(boardSize);
	
	// UI components
	protected JFrame parent;
	protected final int tileWidth  = 65;
	protected final int tileHeight = 65;
	protected final int tileMargin = 5; 
	protected final Font fontSize   = new Font("Arial", Font.BOLD, 9);
	public final int panelWidth = boardSize * (tileWidth + tileMargin) + tileMargin;
	public final int panelHeight= boardSize * (tileHeight+ tileMargin) + tileMargin;
//	protected JLabel label = new JLabel("");
	
	public GamePanel(JFrame parent) {
		super(new BorderLayout());
//		label.setSize(panelWidth, panelHeight);
//		this.add(label, BorderLayout.CENTER);
		this.parent = parent;
		this.setOpaque(true);
		this.setVisible(true);
		this.setBounds(0,0,panelHeight, panelWidth);
		boardData = logic.getBoard();
		this.repaint();
	}
	
	public void keyPressed(KeyEvent arg0) {
		if (logic.getLose()) {
			JOptionPane.showMessageDialog(null, "You lost.", 
					"Message", JOptionPane.WARNING_MESSAGE, new ImageIcon("C:\\Users\\01420819\\workspace\\Imp2048\\fail.png"));
			parent.removeKeyListener(this);
		}
		if (logic.getWon()) JOptionPane.showMessageDialog(null, "You won.",
					"Message", JOptionPane.WARNING_MESSAGE, new ImageIcon("C:\\Users\\01420819\\workspace\\Imp2048\\tick.jpg"));
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
		for (int y = 0; y < boardSize; y ++) {
			for (int x = 0; x < boardSize; x ++) {
				g.setColor(Color.BLUE);
				int dx = (x+1)*tileMargin + x*tileWidth;
				int dy = (y+1)*tileMargin + y*tileHeight;
				g.fillRect(dx, dy, tileWidth, tileHeight);
				if (boardData[y][x] != 0) {
					g.setFont(fontSize);
					g.setColor(Color.WHITE);
					g.drawString(boardData[y][x] + "", dx + tileWidth*2/3, dy + tileHeight*2/3);
				}
			}
		}
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
				GamePanel panel = new GamePanel(frame); 
				frame.addKeyListener(panel);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setContentPane(panel);
				frame.setBounds(0, 0, panel.panelWidth, panel.panelHeight);
				frame.setVisible(true);
			}
		});

	}

}
