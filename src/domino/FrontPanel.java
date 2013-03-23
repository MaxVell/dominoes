package domino;

import java.awt.Graphics;

import javax.swing.JPanel;

public class FrontPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5522851265074995934L;

	public FrontPanel(int width, int height){
		setLayout(null);
		setSize(width, height);
		setVisible(true);
		setOpaque(false);
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
	}
}
