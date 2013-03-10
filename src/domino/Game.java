package domino;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;


public class Game {
	private Bazar market;
	private Gamer[] gamers;
	private GameLineGoat gameLine;
	private int activeGamer;
	private int countGamers;
	private boolean Team = false;
	private int lastWinner;
	private int[] score;
	private String[] gamersName;
	private Properties properties;
	private ArrayList<Stone> stoneWeight;
	
	
	public Game(int countGamers){
		this.countGamers = countGamers;
		this.score = new int[countGamers];
		this.gamersName = new String[countGamers];
		for(int i = 0; i < countGamers; i++)
			this.score[i] = 0;
		this.gamers = new Gamer[countGamers];
		this.properties = new Properties();
		try{
			this.properties.load(new FileInputStream("dominoes.properties"));
		}catch(IOException e){
		//	new HelpDialog(this, "Error", "");
		}
		this.market = new Bazar();
		int count = getCountGamers();
		for(int i = 0; i < count; i++)
			this.gamers[i] = new Gamer();
		this.gameLine = new GameLineGoat();
	}

	public Game(int activeGamer, Gamer[] gamers, GameLineGoat gameLine, Bazar market, int[] scores){
		this.activeGamer = activeGamer;
		this.gamers = gamers;
		this.gameLine = gameLine;
		this.market = market;
		this.score = scores;
	}
	
	public Game(HashSet<Stone> gamer, int countGamers){
		this.countGamers = countGamers;
		this.score = new int[countGamers];
		this.gamersName = new String[countGamers];
		for(int i = 0; i < countGamers; i++)
			this.score[i] = 0;
		this.properties = new Properties();
		try{
			this.properties.load(new FileInputStream("dominoes.properties"));
		}catch(IOException e){
		//	new HelpDialog(this, "Error", "");
		}
		this.gameLine = new GameLineGoat();
		gamers = new Gamer[1];
		gamers[0] = new Gamer(gamer);
		this.market = new Bazar();
	}
	
/*	public void newGame(int countGamers, int activeGamer, Gamer gamer, int[] countStonesGamers, GameLineGoat gameLine ){
		
	}*/
	
	public void newGame(){
//		this.score = new int[countGamers];
//		this.gamersName = new String[countGamers];
//		for(int i = 0; i < countGamers; i++)
//			this.score[i] = 0;
		this.gamers = new Gamer[countGamers];
		this.properties = new Properties();
		try{
			this.properties.load(new FileInputStream("dominoes.properties"));
		}catch(IOException e){
		//	new HelpDialog(this, "Error", "");
		}
		this.market = new Bazar();
		int count = getCountGamers();
		for(int i = 0; i < count; i++)
			this.gamers[i] = new Gamer();
		this.gameLine = new GameLineGoat();
	}
	
	
	public void dispence(){
//		this.gameLine = new GameLineGoat();
		this.gameLine.clear();
		this.market = new Bazar();
		int countGamers = getCountGamers();
		for(int i = 0; i < countGamers; i++)
//			this.gamers[i]. = new Gamer();
			this.gamers[i].removeStones();
	//	setActiveGamer(whoIsFirst());
	}
	
	public boolean stepStart(int numberGamer, Stone stone){
		if(getGameLine().canStep(getGamer(numberGamer))){
			if(getGameLine().addStartStone(stone)){
				getGamer(numberGamer).removeStone(stone);
				nextGamer();
				return true;
			}
		};
		return false;
	}
	
	public boolean stepEnd(int indexGamer, Stone stone){
		if(getGameLine().canStep(getGamer(indexGamer))){
			if(getGameLine().addEndStone(stone)){
				getGamer(indexGamer).removeStone(stone);
				nextGamer();
				return true;
			}
		};
		return false;
	}
	
	public void setGamerName(int numberGamer, String name){
		this.gamersName[numberGamer] = name;
	}
	
	public String getGamerName(int numberGamer){
		return this.gamersName[numberGamer];
	}
	
	public String[] getGamersName(){
		return this.gamersName;
	}
	
	public Gamer getGamer(int indexGamer){
		return this.gamers[indexGamer];
	}
	
	private int getCountGamers(){
		return this.gamers.length;
	}
	
	public Bazar getBazar(){
		return this.market;
	}

	public Gamer[] getGamers(){
		return this.gamers;
	}

	public GameLineGoat getGameLine(){
		return this.gameLine;
	}
	
	public int[] getScore(){
		return this.score;
	}
	
	public int getScore(int index){
		return this.score[index];
	}
	
	private void setScore(int index, int scores){
		this.score[index] = scores;
	}
	
	public boolean canStep(){
		return getGameLine().canStep(getGamer(0));
	}
	
/*	private boolean isRecord(){
		boolean record = true;
		int countGamers = getCountGamers();
		for(int i = 0; i < countGamers;i++){
			record = record && (getScore(i) != 0);
		}
		return record;
	}*/
	
	public int getLastWinner(){
		return this.lastWinner;
	}
	
	public boolean isTeam(){
		return this.Team;
	}
	
	public void setIsTeam(boolean team){
		this.Team = team;
	}
	
	private void setStoneWeight(){
		this.stoneWeight = new ArrayList<Stone>();
		for(int i = 0; i <= 6; i++) 
			this.stoneWeight.add(new Stone((i + 1) % 7, (i + 1) % 7));
		for(int i = 0; i < 6; i++)
			for(int j = i + 1; j <= 6; j++)
				this.stoneWeight.add(new Stone(i, j));
	}
	
	public int whoIsFirst(){
		setStoneWeight();
/*		if(isRecord() && isTeam())
			return (getLastWinner() + 2) % getCountGamers();
		if(isRecord() && !isTeam())	
				return getLastWinner();*/
		int countGamers = getGamers().length;
		for(int i = 0; i < 27; i++){
			for(int j = 0; j < countGamers; j++)
				if(getGamers()[j].haveStone(this.stoneWeight.get(i))){
					return j;
				}
		}
		return 0;
	}	
	
	public int getNumberWinner(){
			int numberWinner = 0;
			int countGamers = getGamers().length;
			for(int i = 0; i < countGamers - 1; i++)
				if(getScore(i) > getScore(i + 1))
					numberWinner = i + 1;
			return numberWinner;
	}
	
	public boolean isEndGame(){
		int countGamers = getCountGamers();
		for(int i = 0; i < countGamers; i++)
			if(getScore()[i] > getIntProperty("maxScore"))
				return true;
		return false;
	}
	
	public void scoreCount(){
		int countGamers = getGamers().length;	
			for(int i = 0; i < countGamers; i++){
				int scores = 0;
				int countGamerStone = getGamers()[i].getCountStones();
				for(int j = 0; j < countGamerStone; j++){
					scores += getGamers()[i].getStone(j).getScore();
				}
				if((getScore(i) > 0) || (scores >= getIntProperty("startScore")))
					setScore(i, scores + getScore(i));
			}
	}
	
	public void newScores(){
		int countGamers = getGamers().length;
		for(int i = 0; i < countGamers; i++){
			setScore(i, 0);
		}
	}
	
	private Properties getProperties(){
		return this.properties;
	}

	private int getIntProperty(String param){
		return Integer.valueOf(getProperties().getProperty(param));		
	}
	
	public void setLastWinner(int lastWinner){
		this.lastWinner = lastWinner;
	}
	
	public boolean isEndRound(){
		return this.gameLine.isEnd(getGamers(), getBazar());
	}
	
	public void setActiveGamer(int indexOfGamer){
		this.activeGamer = indexOfGamer;
	}
	
	public int getActiveGamer(){
		return this.activeGamer;
	}
	
	public void nextGamer(){
		this.activeGamer = (this.activeGamer + 1) % this.getCountGamers();
//		System.out.println("next gamer: " + this.activeGamer);
	}
	
	public void setScores(int[] scores){
		this.score = scores;
	}
	
	public void setNames(String[] names){
		this.gamersName = names;
	}
	
}