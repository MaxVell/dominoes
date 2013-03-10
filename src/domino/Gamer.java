package domino;

import java.util.ArrayList;
import java.util.HashSet;

public class Gamer {
	private HashSet<Stone> gamerStones;
	
	public Gamer(){
		this.gamerStones = new HashSet<Stone>();
	}
	
	public Gamer(HashSet<Stone> gamerStone){
		this.gamerStones = gamerStone;
	}
	
	public Gamer(int countStones){
		this.gamerStones = new HashSet<Stone>();
		for(int i = 0; i < countStones; i++){
			this.gamerStones.add(new Stone(i, i));
		}
	}
	
	public void setGamer(HashSet<Stone> gamerStone){
		this.gamerStones = gamerStone;
	}
	
	public void removeStones(){
		this.gamerStones.clear();
	}
	
	public void addStone(Stone elem){
		this.gamerStones.add(elem);
	}
	
	public void addStone(){
		this.gamerStones.add(new Stone(getCountStones() - 1, getCountStones() - 1));
	}
	
	public void removeStone(){
		this.gamerStones.remove(this.gamerStones.add(new Stone(getCountStones() - 1, getCountStones() - 1)));
	}
	
	public void addStones(ArrayList<Stone> stones){
		this.gamerStones.addAll(stones);
	}
	
	public boolean removeStone(Stone stone){
		return this.gamerStones.remove(stone);
	}
	
/*	public Stone removeStone(int index){
		this.GamerStones.remove(arg0)
		return this.GamerStones.remove(index);
	}*/
	
	public Stone getStone(int index){
		Object[] stones = this.gamerStones.toArray();
		return (Stone)stones[index];
	}
	
	public int getCountStones(){
		return this.gamerStones.size();
	}
	
	public boolean haveStone(Stone elem){
	//	return this.gamerStones.contains(elem);
		Object[] stones = this.gamerStones.toArray();
		int countStone = getCountStones();
		Stone stone;
		for(int i = 0; i < countStone; i++){
			stone = (Stone)stones[i];
		if(stone.equals(elem))
			return true;
		}
		return false;

	}
	
	
	
	public boolean haveNumber(int number){
		Object[] stones = this.gamerStones.toArray();
		int countStone = getCountStones();
		Stone stone;
		for(int i = 0; i < countStone; i++){
			stone = (Stone)stones[i];
		if(stone.hasNumber(number))
			return true;
		}
		return false;
	}
}
