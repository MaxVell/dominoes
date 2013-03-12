package domino;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MarketMouseListener implements MouseListener, MouseMotionListener, Constants{

	private StoneView stoneView;
	private DrawGame dGame;
	private DrawClient dClient;
	
	public MarketMouseListener(StoneView stoneView,DrawGame dGame){
		this.stoneView = stoneView;
		this.dGame = dGame;
	}
	
	public MarketMouseListener(StoneView stoneView, DrawClient dClient){
		this.stoneView = stoneView;
		this.dClient = dClient;
	}
	
	private Game getGame(){
		if(dGame != null){
			return dGame.getGame();
		} else 
			return dClient.getGame();
	}
	
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
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

	private DrawGame getDrawGame(){
		return dGame;
	}
	
	private DrawClient getDrawClient(){
		return dClient;
	}
	
	private StoneView getStoneView(){
		return stoneView;
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(!getGame().canStep()){
			getStoneView().setIsOpen(true);
			if(getDrawGame() != null)
				getDrawGame().putFromMarket(getStoneView());
			else{
				getDrawClient().putFromMarket(getStoneView());
			}
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
