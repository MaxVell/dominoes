package domino;

import java.util.ArrayList;
import java.util.Random;

public class MarketView {
	
	private Bazar market;
	private ArrayList<StoneView> stones;
	private DrawGame dGame;
	private DrawClient dClient;
	
	public MarketView(Bazar market){
		this.market = market;
		this.stones = new ArrayList<StoneView>();
	}
	
	public MarketView(DrawClient dClient){
		this.market = new Bazar();
		this.stones = new ArrayList<StoneView>();
		this.dClient = dClient;
	}
	
	public MarketView(Bazar market, ArrayList<StoneView> stones, DrawGame dGame){
		this.market = market;
		this.stones = stones;
		this.dGame = dGame;
		for(int i = 0; i < 28; i++){
			stones.get(i).setStone(market.getStone(i));
		}
		setMouseListener();
	}
	
	public MarketView(Bazar market, ArrayList<StoneView> stones, DrawClient dClient){
		this.market = market;
		this.stones = stones;
		this.dClient = dClient;
		setMouseListener();
	}
	
	private void setMouseListener(){
		int countStones = stones.size();
		for(int i = 0; i < countStones; i++){
			if(dGame != null)
				stones.get(i).addMouseListener(new MarketMouseListener(stones.get(i), dGame ));
			else
				stones.get(i).addMouseListener(new MarketMouseListener(stones.get(i), dClient ));
		}
	}
	
	public StoneView putStoneView(StoneView stoneView){
		StoneView stone = stoneView;
		stone.removeMouseListener(stone.getMouseListeners()[0]);
		this.stones.remove(stoneView);
		return stone;
	}
	
	public StoneView putStoneView(Stone stone){
		StoneView stoneView = getStone(stone);
		stoneView.removeMouseListener(stoneView.getMouseListeners()[0]);
		this.stones.remove(stoneView);
		return stoneView;
	}
	
	public StoneView putStone(int index){
		StoneView stoneView = this.stones.remove(index);
		stoneView.removeMouseListener(stoneView.getMouseListeners()[0]);
		return stoneView; 
	}
	
	public StoneView putRandomStoneView(){
		Random rand = new Random();
		int numberStone = rand.nextInt(getStones().size());
		StoneView stoneView = this.getStones().remove(numberStone);
		stoneView.removeMouseListener(stoneView.getMouseListeners()[0]);
		return stoneView;
	}
	
	private Bazar getMarket(){
		return this.market;
	}
	
	public ArrayList<StoneView> getStones(){
		return this.stones;
	}
	
	public void addStone(StoneView stoneView){
		getStones().add(stoneView);
		if(dGame != null){
			stoneView.addMouseListener(new MarketMouseListener(stoneView, dGame ));
			getMarket().addStone(stoneView.getStone());
		} else{
			stoneView.setStone(new Stone(stones.size(), stones.size()));
			getMarket().addStone(stoneView.getStone());
			stoneView.addMouseListener(new MarketMouseListener(stoneView, dClient ));
		}
	}
	
	public StoneView getStone(Stone stone){
		int countStones = getStones().size();
		for(int i = 0; i < countStones; i++){
			if(getStones().get(i).getStone().equals(stone))
				return getStones().get(i); 
		}
		return null;
	}
	
	public void removeStone(StoneView stoneView){
		getStones().remove(stoneView);
		getMarket().removeStone(stoneView.getStone());
	}
}
