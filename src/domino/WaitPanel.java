package domino;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class WaitPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -247262808377452798L;
	private StoneView stoneView;
	private boolean isTurn = false;
	private JLabel label;
	
	public WaitPanel(){
		this.stoneView = new StoneView(new Stone(1,1));
		this.add(stoneView);
		stoneView.setIsTurn(isTurn);
		stoneView.setIsOpen(true);
		stoneView.setWidth(getStoneWidth());
		this.setOpaque(false);
		label = new JLabel("Wait answer");
		label.setForeground(Color.BLACK);
		this.add(label);
		this.setSize(4 * getStoneWidth(),4 * getStoneWidth());
	//	this.setVisible(true);
		
	}

	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		drawPanel((Graphics2D)g);
		stoneView.setLocation(0, 0);
		label.setLocation(getStoneWidth(), 3 * getStoneWidth() );
		
	}
	
	public void setIsturn(boolean isTurn){
		this.isTurn = isTurn;
		this.stoneView.setIsTurn(isTurn);
	}
	
	private int getStoneWidth(){
	//	return this.dClient.getStoneWidth();
		return 30;
	}
	
	private void drawPanel(Graphics2D g){
		Composite originalComposite = g.getComposite();
		g.setColor(Color.WHITE);
		g.setComposite(makeComposite(0.5f));
		g.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		g.fillRoundRect(0, 0, 4* getStoneWidth(), 4* getStoneWidth(), 20, 20);
		g.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF );
		g.setComposite(originalComposite);
	}
	
	private AlphaComposite makeComposite(float alpha) {
		  int type = AlphaComposite.SRC_OVER;
		  return(AlphaComposite.getInstance(type, alpha));
		 }
}
