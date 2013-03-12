package domino;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GamerMouseListener implements MouseListener, MouseMotionListener {

	private StoneView stone;
	private int x;
	private int y;
	private DrawGame dGame;
	private DrawClient dClient;
	private boolean canChange;
	
	public GamerMouseListener(StoneView stone, DrawGame dGame){
		this.stone = stone;
		this.dGame = dGame;
	}
	
	public GamerMouseListener(StoneView stone, DrawClient dClient){
		this.stone = stone;
		this.dClient = dClient;
	}

	private void setStoneRectVisible(boolean visible){
		if(getGame().getGameLine().canAttachStart(getStone()) || !visible)
			getStoneRect(0).setVisible(visible);
		if(getGame().getGameLine().canAttachEnd(getStone()) || !visible)
			getStoneRect(1).setVisible(visible);
	}
	
	private void setStoneRect(Stone stone){
		getStoneRect(0).setStone(stone);
		getStoneRect(0).setIsOpen(true);
		getStoneRect(0).setColor(Color.GRAY);
		getStoneRect(0).setVertical();
		getStoneRect(1).setStone(stone);
		getStoneRect(1).setIsOpen(true);
		getStoneRect(1).setColor(Color.GRAY);
		getStoneRect(1).setVertical();
	}
	
	private StoneView getStoneRect(int number){
		if(dGame != null)
			return dGame.getStoneRect()[number];//this.stoneRect[number];
		else 
			return dClient.getStoneRect()[number];
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	private boolean canChange(){
		return canChange;
	}
	
	private void setCanChange(boolean isPress){
		if(dGame != null){
			canChange = dGame.canChange()&& dGame.getGame().getGamer(0).haveStone(getStone()) && isPress;
		} else { 
			canChange = dClient.canChange() && dClient.getGame().getGamer(0).haveStone(getStone())&& isPress;
		}
	}
	
	@Override
	public void mousePressed(MouseEvent me) {
		setCanChange(true);
		if(canChange() ){
			// TODO Auto-generated method stub
			stone.setIsPress(true);
			x = me.getX();
			y = me.getX();
			setStoneRect(stone.getStone());
			setStoneRectVisible(true);
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {
	//	
		if(canChange()){
		// TODO Auto-generated method stub
			stone.setIsPress(false);
			stepGamer(me);
			setStoneRectVisible(false);
			stone.repaint();
		}
		setCanChange(false);
	}

	private Game getGame(){
		if(dGame != null){
			return dGame.getGame();
		} else return dClient.getGame();
	}
	
	private Stone getStone(){
		return stone.getStone();
	}
	
	private boolean stepGamer(MouseEvent me){
		if(inRect(me, 1)) {
			if(getGame().getGameLine().canAttachEnd(getStone())){
				if(dGame != null){
					dGame.addEndGameLineStone(0, stone, true);
				//	dGame.sendGame();
				} else{
					dClient.addEndGameLineStone(stone);
				}
			}
		}
		if(inRect(me, 0)){
			if(getGame().getGameLine().canAttachStart(getStone())){
				if(dGame != null){
					dGame.addStartGameLineStone(0,stone, true);
				//	dGame.sendGame();
				} else{
					dClient.addStartGameLineStone(stone);
				}
			}
		}
		
		return false;
	}
	
	private boolean inRect(MouseEvent me, int number){
		int x1 = getStoneRect(number).getLocation().x - stone.getWidth();
		int y1 = getStoneRect(number).getLocation().y - stone.getHeight();
		int x2 = getStoneRect(number).getLocation().x + getStoneRect(number).getWidth(); 
		int y2 = getStoneRect(number).getLocation().y + getStoneRect(number).getHeight();
		int x = getX() + me.getX() - this.x;
		int y = getY() + me.getY() - this.y;
		if((x > x1) && (x < x2) && (y > y1) && (y < y2))
			return true;
		else
			return false;
	}
	
	private int getX(){
		return stone.getLocation().x;
	}
	
	private int getY(){
		return stone.getLocation().y;
	}
	
	@Override
	public void mouseDragged(MouseEvent me) {
		// TODO Auto-generated method stub
		if(canChange())
		stone.setLocation(getX() + me.getX() - x, getY() + me.getY() - y);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
	}

}
