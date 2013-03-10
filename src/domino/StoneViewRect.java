package domino;

import java.awt.Graphics;

public class StoneViewRect extends StoneView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7731549707752922081L;
	private boolean isVertical;
	private int width = 30;
	
	public StoneViewRect(){
		this.setSize(30, 30);
		this.setVisible(true);
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		this.getGraphics().fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);		
	}
	
	public void setWidth(int width){
		this.width = width;
		if(getIsVertical()){
			this.setSize(width, 2 *width);
		} else{
			this.setSize(2 * width, width);
		}
	}
	
	public int getWidth(){
		return this.width;
	}
	
	public void setVertical(){
		this.isVertical = false;
	}
	
	public void setIsVertical(boolean isVertical){
		this.isVertical = isVertical;
	}
	
	public boolean getIsVertical(){
		return this.isVertical;
	}
}
