package domino;

import java.util.ArrayList;

public class GamerView {

	private Gamer gamer;
	private DrawGame drawGame;
	private ArrayList<StoneView> stones;
	private DrawClient drawClient;
	
	public GamerView(Gamer gamer, DrawGame drawGame){
		this.gamer = gamer;
		this.drawGame = drawGame;
		this.stones = new ArrayList<StoneView>();
	}
	
	public GamerView(Gamer gamer, DrawClient drawClient){
		this.gamer = gamer;
		this.drawClient = drawClient;
		this.stones = new ArrayList<StoneView>();
	}
	
	public GamerView(DrawClient drawClient){
		this.drawClient = drawClient;
		this.stones = new ArrayList<StoneView>();
	}

	public StoneView putLast(){
		StoneView stone = getStones().remove(size() - 1);
		stone.removeMouseListener(stone.getMouseListeners()[0]);
		stone.removeMouseMotionListener(stone.getMouseMotionListeners()[0]);
		return stone;
	}
	
	public void setGamer(Gamer gamer){
		this.gamer = gamer;		
	}
	
	public int size(){
		return stones.size();
	}
	
	private DrawGame getDrawGame(){
		return drawGame;
	}
	
	private DrawClient getDrawClient(){
		return drawClient;
	}
	
	public void addStone(StoneView stoneView){
		GamerMouseListener MouseListener;
		if(getDrawGame() != null){
			getGamer().addStone(stoneView.getStone());
			MouseListener = new GamerMouseListener(stoneView, getDrawGame());
		} else
			MouseListener = new GamerMouseListener(stoneView, getDrawClient());
		stoneView.addMouseListener(MouseListener);
		stoneView.addMouseMotionListener(MouseListener);
		stoneView.setIsOpen(true);
		getStones().add(stoneView);
	}
	
	private Gamer getGamer(){
		return gamer;
	}
	
	public ArrayList<StoneView> getStones(){
		return stones;
	}
	
	public StoneView getStoneView(int index){
		return getStones().get(index);
	}
	
	public StoneView getStoneView(Stone stone){
		int countStones = getStones().size();
		for(int i = 0; i < countStones; i++){
			if(getStones().get(i).getStone().equals(stone))
				return getStones().get(i); 
		}
		return null;
	}
	
	public void removeStoneView(StoneView stoneView){
		stoneView.removeMouseListener(stoneView.getMouseListeners()[0]);
		stoneView.removeMouseMotionListener(stoneView.getMouseMotionListeners()[0]);
		getStones().remove(stoneView);
	}
	
}
