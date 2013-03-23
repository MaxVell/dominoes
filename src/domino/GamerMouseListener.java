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
	private FrontPanel frontPanel;
	
	public GamerMouseListener(StoneView stone, DrawGame dGame, FrontPanel frontPanel){
		this.stone = stone;
		this.dGame = dGame;
		this.frontPanel = frontPanel;
	}
	
	public GamerMouseListener(StoneView stone, DrawClient dClient, FrontPanel frontPanel){
		this.stone = stone;
		this.dClient = dClient;
		this.frontPanel = frontPanel;
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
	
	private FrontPanel getFrontPanel(){
		return frontPanel;
	}
	
	private DrawGame getDrawGame(){
		return dGame;
	}
	
	private DrawClient getDrawClient(){
		return dClient;
	}
	
	private StoneView getStoneRect(int number){
		if(getDrawGame() != null)
			return getDrawGame().getStoneRect()[number];//this.stoneRect[number];
		else 
			return getDrawClient().getStoneRect()[number];
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
		if(getDrawGame() != null){
			canChange = getDrawGame().canChange()&& getDrawGame().getGame().getGamer(0).haveStone(getStone()) && isPress;
		} else { 
			canChange = getDrawClient().canChange() && getDrawClient().getGame().getGamer(0).haveStone(getStone())&& isPress;
		}
	}
	
	@Override
	public void mousePressed(MouseEvent me) {
		setCanChange(true);
		if(canChange() ){
			// TODO Auto-generated method stub
			getFrontPanel().add(getStoneView());
			getStoneView().setIsPress(true);
			x = me.getX();
			y = me.getX();
			setStoneRect(getStoneView().getStone());
			setStoneRectVisible(true);
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {
	//	
		if(canChange()){
		// TODO Auto-generated method stub
			getStoneView().setIsPress(false);
			stepGamer(me);
			setStoneRectVisible(false);
			if(getDrawGame() != null){
				getDrawGame().add(getStoneView());
			} else{
				getDrawClient().add(getStoneView());
			}
			getStoneView().repaint();
		}
		setCanChange(false);
	}

	private Game getGame(){
		if(getDrawGame() != null){
			return getDrawGame().getGame();
		} else return getDrawClient().getGame();
	}
	
	private StoneView getStoneView(){
		return stone;
	}
	
	private Stone getStone(){
		return stone.getStone();
	}
	
	private boolean stepGamer(MouseEvent me){
		if(inRect(me, 1)) {
			if(getGame().getGameLine().canAttachEnd(getStone())){
				if(getDrawGame() != null){
					getDrawGame().addEndGameLineStone(0, getStoneView(), true);
				//	dGame.sendGame();
				} else{
					getDrawClient().addEndGameLineStone(getStoneView());
				}
			}
		}
		if(inRect(me, 0)){
			if(getGame().getGameLine().canAttachStart(getStone())){
				if(getDrawGame() != null){
					getDrawGame().addStartGameLineStone(0,getStoneView(), true);
				//	dGame.sendGame();
				} else{
					getDrawClient().addStartGameLineStone(getStoneView());
				}
			}
		}
		
		return false;
	}
	
	private boolean inRect(MouseEvent me, int number){
		int x1 = getStoneRect(number).getLocation().x - getStoneView().getWidth();
		int y1 = getStoneRect(number).getLocation().y - getStoneView().getHeight();
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
			getStoneView().setLocation(getX() + me.getX() - x, getY() + me.getY() - y);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
	}

}
