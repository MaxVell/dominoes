package domino;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Bazar {
	private ArrayList<Stone> market;
	
	public Bazar(){
		market = new ArrayList<Stone>();
		fullBazar();
	}
	
	public Bazar(int countStones){
		market = new ArrayList<Stone>();
		for(int i = 0; i < countStones; i++){
			market.add(new Stone());
		}
	}
	
	public Stone getStone(int index){
		return market.get(index);
	}
	
	
	private void fullBazar(){
		for(int i = 0; i <= 6; i++)
			for(int j = i; j <=6; j++){
				market.add(new Stone(i, j));
			}
	}
	
	public Stone removeStone(int index){
		return market.remove(index);
	}
	
	public void removeStone(Stone stone){
		market.remove(stone);
	}
	
	public int getCountStones(){
		return market.size();
	}
	
	@SuppressWarnings("unchecked")
	public HashSet<Stone>[] dispence(int countGamers, int countStones){
		HashSet<Stone>[] gamerStones = new HashSet[countGamers];
		for(int i = 0; i < countGamers; i++)
			gamerStones[i] = new HashSet<Stone>();
		Random rand = new Random();
		for(int i = 0; i < countGamers; i++)
			for(int j = 0; j < countStones; j++){				
				int randNumber = rand.nextInt(this.getCountStones());
				gamerStones[i].add(removeStone(randNumber));
			}
		return gamerStones;
	}
	
	public Stone getRandom(){
		Random rand = new Random();
		int randNumber = rand.nextInt(this.getCountStones());
		return getStone(randNumber);
	}
	
	public Stone removeRandom(){
		Random rand = new Random();
		int randNumber = rand.nextInt(this.getCountStones());
		return removeStone(randNumber);
	}
	
	public void addStone(Stone stone){
		market.add(stone);
	}
	
	public boolean haveNumber(int number){
		for(int i = 0; i < this.getCountStones(); i++){
			if((this.getStone(i).hasNumber(number)))
				return true;
		}
		return false;
	}
}
