package domino;

import java.util.ArrayList;

public class GameLineGoat {
	private ArrayList<Stone> lineGame;
	private ArrayList<Stone> stoneWeight;
	private int startConutStone;
	private int startNumber;
	private int endNumber;
	private int[] numbers;
	
	public GameLineGoat(){
		this.lineGame = new ArrayList<Stone>();
		startConutStone = -1;
		startNumber = -1;
		endNumber = -1;
		numbers = new int[7];
		for(int i = 0; i < 7; i++)
			numbers[i] = 0;
	}
	
	public GameLineGoat(ArrayList<Stone> gameLine){
		this.lineGame = gameLine;
		startConutStone = -1;
		startNumber = -1;
		endNumber = -1;
		numbers = new int[7];
		for(int i = 0; i < 7; i++)
			numbers[i] = 0;
	}
	
	public void clear(){
		this.lineGame.clear();
		this.startConutStone = - 1;
		this.startNumber = - 1;
		this.endNumber = -1;
	}
	
	public boolean addStartStone(Stone elem){
		if(length() == 0){
			setStartNumber(elem.getStart());
			setEndNumber(elem.getFinish());
			addStart(elem);
			addStartCount();
			return true; 
		}
		if(elem.getStart() == getStartNumber()){
			setStartNumber(elem.getFinish());
			addStart(elem);
			addStartCount();
			return true; 
		}
		if(elem.getFinish() == getStartNumber()){
			setStartNumber(elem.getStart());
			addStart(elem);
			addStartCount();
			return true;
		}
		return false;
	}
	
	public boolean addEndStone(Stone elem){
		if(length() == 0){
			setStartNumber(elem.getStart());
			setEndNumber(elem.getFinish());
			addStart(elem);
			addStartCount();
			return true; 
		}
		if(elem.getStart() == getEndNumber()){
			setEndNumber(elem.getFinish());
			addEnd(elem);
			return true; 
		}
		if(elem.getFinish() == getEndNumber()){
			setEndNumber(elem.getStart());
			addEnd(elem);
			return true;
		}
		return false;
	}
	
	private void addNumbers(Stone elem){
		this.numbers[elem.getStart()]++;
		this.numbers[elem.getFinish()]++;
	}
	
	private void addStart(Stone element){
		addNumbers(element);
		this.lineGame.add(0, element);
		
	}
	
	private void addEnd(Stone element){
		addNumbers(element);
		this.lineGame.add(element);
	}
		
	public boolean canStep(Gamer gamer){
		if(length() == 0){
			return true;
		}
		int countGamerStone = gamer.getCountStones(); 
		for(int i = 0; i < countGamerStone; i++)
			if(gamer.haveNumber(startNumber) || gamer.haveNumber(endNumber))
				return true;
		return false;
	}
	
	public boolean isEnd(Gamer[] gamers, Bazar market){
		if(isWin(gamers) || isFish()) return true;
			else return false;
	}
	
	public boolean isWin(Gamer[] gamers){
		int countGamers = gamers.length;
		for(int i = 0; i < countGamers; i++){
			if(gamers[i].getCountStones() == 0) return true;
		}
		return false;
	}
	
	public boolean isFish(){
		int[] numbers = new int[7];
		for(int i = 0; i < 7; i++)
			numbers[i] = 0;
		int countStones = this.lineGame.size();
		for(int i = 0; i < countStones; i++){
			addNumbers(this.lineGame.get(i));
		}
		for(int i = 0; i < 7; i++)
			if((numbers[i] == 8) && (i == getStartNumber()))
				return true;
		return false;
	}
	
	public void removeAll(){
		this.lineGame.clear();
	}
	
	public int getStartCount(){
		return this.startConutStone;
	}
	
	private void addStartCount(){
		this.startConutStone++;
	}
	
	public void setStartCount(int startCount){
		this.startConutStone = startCount;
	}
	
	public Stone priorityStone(int index){
		return this.stoneWeight.get(index);
	}
	
	public int getStartNumber(){
		return this.startNumber;
	}
	
	public void setStartNumber(int startNumber){
		this.startNumber = startNumber;
	}
	
	public int getEndNumber(){
		return this.endNumber;
	}
	
	public void setEndNumber(int endNumber){
		this.endNumber = endNumber;
	}
	
	public int length(){
		return this.lineGame.size();
	}
	
	public Stone getStone(int index){
		return this.lineGame.get(index);
	}
	
	public boolean canAttach(Stone stone){
		return stone.hasNumber(getStartNumber()) || stone.hasNumber(getEndNumber()) || length() == 0;
	}
	
	public boolean canAttachStart(Stone stone){
		return stone.hasNumber(getStartNumber()) || length() == 0;
	}
	
	public boolean canAttachEnd(Stone stone){
		return stone.hasNumber(getEndNumber()) && length() != 0;
	}
}
