package domino;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;

public class StoneView extends JPanel{
	private Stone stone;
	private int width;
	private boolean isVertical;
	private boolean isOpen;
	private boolean isPress;
	private Graphics2D g;
	private double theta;
	private Color color;
	private boolean turn;
//	private double deltaTheta;
	/**
	 * 
	 */
	private static final long serialVersionUID = -1435445423589581003L;
	
	public StoneView(){
		this.setSize(width, width);
		this.isVertical = true;
		this.theta = 0;
		this.isOpen = false;
		this.color = Color.BLACK;
//		deltaTheta = 0;
		this.setOpaque(false);
		this.setVisible(true);
	}

	public StoneView(Stone stone){
		this.stone = stone;
		this.setSize(width, width);
		this.isVertical = true;
		this.theta = 0;
		this.isOpen = false;
		this.color = Color.BLACK;
	//	deltaTheta = 0;
		this.setOpaque(false);
		this.setVisible(true);
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		setGraphics2D((Graphics2D)g);
		AffineTransform at = getGraphics2D().getTransform();
		if(getIsTurn()){
			transferStone(0.01);
			drawStone();
			getGraphics2D().setTransform(at);
			repaint();
			
		} else{
			transferStone();
			drawStone();
			getGraphics2D().setTransform(at);
		}
		
	}
	
	public boolean getIsTurn(){
		return this.turn;
	}
	
	public void setIsTurn(boolean turn){
		this.turn = turn;
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public Color getColor(){
		return this.color;
	}
	
	public boolean isPress(){
		return this.isPress;
	}
	
	public void setIsPress(boolean isPress){
		this.isPress = isPress;
	}
	
	public Stone getStone(){
		return this.stone;
	}
	
	public void setIsVertical(boolean isVertical){
		if(isVertical){
			setTheta(0);
			this.setSize(getStoneWidth() + 1, 2 * getStoneWidth() + 1);
		} else{
			setTheta(Math.PI / 2);
			this.setSize(2 * getStoneWidth() + 1, getStoneWidth() + 1);
		}
		this.isVertical = isVertical;
	}
	
	public void setTheta(double theta){
		this.theta = theta;
	}
	
	public boolean getIsVertical(){
		return this.isVertical;
	}
	
	public void setStone(Stone stone){
		this.stone = stone;
	}
	
	public void setWidth(int width){
		this.width = width;
	/*	if(isVertical){
			this.setSize(width + 1, 2 * width + 1);
		}else{
			this.setSize(2 * width + 1, width + 1);
		}*/
	}
	
	public int getStoneWidth(){
		return this.width;
	}
	
	public double getTheta(){
		return this.theta;
	}
	
	public int getStartNumber(boolean isLeft){
		if(isLeft){
			if(((getTheta() == 0) || (getTheta() == 3 * Math.PI / 2))){
				return this.stone.getFinish();
			} else{
				return this.stone.getStart();
			}
		} else{
			if(((getTheta() == 0) || (getTheta() == 3 * Math.PI / 2))){
				return this.stone.getStart();				
			} else{
				return this.stone.getFinish();
			}
		}
	}
	
	public int getEndNumber(boolean isRight){
		if(!isRight){
			if((getTheta() == 0) || (getTheta() == 3 * Math.PI / 2)){
				return this.stone.getStart();
			} else{
				return this.stone.getFinish();
			}
		}else{
			if((getTheta() == 0) || (getTheta() == 3 * Math.PI / 2)){
				return this.stone.getFinish();			
			} else{
				return this.stone.getStart();
			}
		}
	}
	
	private void transferStone(double delta){
		this.setSize(4 * width, 4 * width);
		setTheta(getTheta() + delta);
		double theta = getTheta();
		AffineTransform at;
    	at  = new AffineTransform();
    	at.setToRotation(theta, 2* width, 2 * width);
    	at.concatenate(new AffineTransform(1, 0.0, 0.0, 1, 3* width / 2, width));
  //  	AffineTransformOp atOp  = new AffineTransformOp(at,AffineTransformOp.TYPE_BILINEAR);
  //  	atOp.filter(ImgNoRotated, ImgRotated);
    	getGraphics2D().transform(at);
	}
	
	private int transferStone(){
		double theta = getTheta();
		if(theta == 0){
			return 0;
		}
		if(theta == Math.PI / 2){
			AffineTransform at;
        	at  = new AffineTransform();
        	at.setToRotation(-Math.PI/2, width / 2, width);
        	at.concatenate(new AffineTransform(1, 0.0, 0.0, 1, width / 2, width / 2));
      //  	AffineTransformOp atOp  = new AffineTransformOp(at,AffineTransformOp.TYPE_BILINEAR);
      //  	atOp.filter(ImgNoRotated, ImgRotated);
        	getGraphics2D().transform(at);
			return 0;
		}
		if(theta == Math.PI){
			AffineTransform ct = AffineTransform.getRotateInstance(theta, width / 2, width);
			getGraphics2D().transform(ct);
			return 0;
		}
		if(theta == 3 * Math.PI / 2){
			AffineTransform at;
        	at  = new AffineTransform();
        	at.setToRotation(Math.PI/2, width / 2, width);
        	at.concatenate(new AffineTransform(1, 0.0, 0.0, 1, -width / 2, -width / 2));
      //  	AffineTransformOp atOp  = new AffineTransformOp(at,AffineTransformOp.TYPE_BILINEAR);
      //  	atOp.filter(ImgNoRotated, ImgRotated);
        	getGraphics2D().transform(at);
			return 0;
		}
		return 0;
	}
	
	public boolean isOpen(){
		return this.isOpen;
	}
	
	private void drawStone(){
		if(isOpen()){
			drawOpenStone();
		} else{
			drawInvertedStone();
		}
	}

	private void drawInvertedStone(){
			drawInvertedStoneRect(0, width, width, width);
			drawInvertedStoneRect(0, 0, width, width);
	}
	
	private void drawInvertedStoneRect(int posx, int posy, int width, int height){
		getGraphics2D().setColor(Color.GRAY);
		getGraphics2D().fillRoundRect(posx, posy, width, height, 10, 10);
		int interval = 5;
		getGraphics2D().setColor(Color.BLACK);
		getGraphics2D().drawRoundRect(posx, posy, width, height, 10, 10);
		for(int i = 0; i < width; i += interval){
			getGraphics2D().drawLine(posx, posy + i, posx + width - i, posy + width);
			getGraphics2D().drawLine(posx + i, posy, posx + width, posy + width - i);
			getGraphics2D().drawLine(posx + width - i, posy, posx, posy + width - i);
			getGraphics2D().drawLine(posx + width, posy + i, posx + i, posy + width);
		}		
	}

	public void setVertical(){
		if(getStone() != null){
			if(getStone().isDoubles())
				setIsVertical(true);
			else 
				setIsVertical(false);
		} else setIsVertical(false);
	}
	
	private Graphics2D getGraphics2D(){
		return this.g;
	}
	
	private void setGraphics2D(Graphics2D g){
		this.g = g;
	}
	
	public void setIsOpen(boolean isOpen){
		this.isOpen = isOpen;
	}
	
	private void drawPoints(int posx, int posy, int countPoints){
		getGraphics2D().setColor(Color.WHITE);
		getGraphics2D().fillRoundRect(posx, posy, width, width, 10, 10);
		getGraphics2D().setColor(getColor()); 
		getGraphics2D().drawRoundRect(posx, posy, width, width, 10, 10);
		int shift = width / 5;
		int w = width / 8;
		getGraphics2D().setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		int height = width;
		switch (countPoints){
		case 1: 
			getGraphics2D().fillOval(posx + width / 2 - shift / 2, posy + width / 2 - shift / 2, shift, shift);
			break;
		case 2: 
			getGraphics2D().fillOval(posx + w, posy + height - 3 * w, shift, shift);
			getGraphics2D().fillOval(posx + width - 3 * w, posy + w, shift, shift);
			break;
		case 3: 
			getGraphics2D().fillOval(posx + w, posy + height - 3 * w, shift, shift);
			getGraphics2D().fillOval(posx +  width / 2 - shift / 2, posy +  width / 2 - shift / 2, shift, shift);
			getGraphics2D().fillOval(posx + width - 3 * w, posy + w, shift, shift);
			break;
		case 4: 
			getGraphics2D().fillOval(posx + w, posy + height - 3 * w, shift, shift);
			getGraphics2D().fillOval(posx + width - 3 * w, posy + height - 3 * w, shift, shift);
			getGraphics2D().fillOval(posx + width - 3 * w, posy + w, shift, shift);
			getGraphics2D().fillOval(posx + w, posy + w, shift, shift);
			break;
		case 5: 
			getGraphics2D().fillOval(posx + w, posy + height - 3 * w, shift, shift);
			getGraphics2D().fillOval(posx + width - 3 * w, posy + height - 3 * w, shift, shift);
			getGraphics2D().fillOval(posx + width - 3 * w, posy + w, shift, shift);
			getGraphics2D().fillOval(posx + w, posy + w, shift, shift);
			getGraphics2D().fillOval(posx +  width / 2 - shift / 2, posy +  width / 2 - shift / 2, shift, shift);
			break;
		case 6:
			getGraphics2D().fillOval(posx + w, posy + height - 3 * w, shift, shift);
			getGraphics2D().fillOval(posx + width - 3 * w, posy + height - 3 * w, shift, shift);
			getGraphics2D().fillOval(posx + width - 3 * w, posy + w, shift, shift);
			getGraphics2D().fillOval(posx + w, posy + w, shift, shift);
			getGraphics2D().fillOval(posx + w, posy + + width / 2 - shift / 2, shift, shift);
			getGraphics2D().fillOval(posx + width - 3 * w, posy + + width / 2 - shift / 2, shift, shift);
			break;
	}	
		getGraphics2D().setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF );
		getGraphics2D().setColor(Color.BLACK);
	}
	
	private void drawOpenStone(){
		drawPoints(0, 0, getStone().getStart());
		drawPoints(0, getStoneWidth(), getStone().getFinish());
	}
}