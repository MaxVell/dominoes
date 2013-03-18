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
	
	public void newGame(){
//		this.score = new int[countGamers];
//		this.gamersName = new String[countGamers];
//		for(int i = 0; i < countGamers; i++)
//			this.score[i] = 0;
		gamers = new Gamer[countGamers];
		properties = new Properties();
		try{
			properties.load(new FileInputStream("dominoes.properties"));
		}catch(IOException e){
		//	new HelpDialog(this, "Error", "");
		}
		market = new Bazar();
		int count = getCountGamers();
		for(int i = 0; i < count; i++)
			gamers[i] = new Gamer();
		gameLine = new GameLineGoat();
	}
	
	
	public void dispence(){
//		this.gameLine = new GameLineGoat();
		gameLine.clear();
		market = new Bazar();
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
		gamersName[numberGamer] = name;
	}
	
	public String getGamerName(int numberGamer){
		return gamersName[numberGamer];
	}
	
	public String[] getGamersName(){
		return gamersName;
	}
	
	public Gamer getGamer(int indexGamer){
		return gamers[indexGamer];
	}
	
	private int getCountGamers(){
		return gamers.length;
	}
	
	public Bazar getBazar(){
		return market;
	}

	public Gamer[] getGamers(){
		return gamers;
	}

	public GameLineGoat getGameLine(){
		return gameLine;
	}
	
	public int[] getScore(){
		return score;
	}
	
	public int getScore(int index){
		return score[index];
	}
	
	private void setScore(int index, int scores){
		score[index] = scores;
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
		return lastWinner;
	}
	
	public boolean isTeam(){
		return Team;
	}
	
	public void setIsTeam(boolean team){
		this.Team = team;
	}
	
	private void setStoneWeight(){
		stoneWeight = new ArrayList<Stone>();
		for(int i = 0; i <= 6; i++) 
			stoneWeight.add(new Stone((i + 1) % 7, (i + 1) % 7));
		for(int i = 0; i < 6; i++)
			for(int j = i + 1; j <= 6; j++)
				stoneWeight.add(new Stone(i, j));
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
		return properties;
	}

	private int getIntProperty(String param){
		return Integer.valueOf(getProperties().getProperty(param));		
	}
	
	public void setLastWinner(int lastWinner){
		this.lastWinner = lastWinner;
	}
	
	public boolean isEndRound(){
		return gameLine.isEnd(getGamers(), getBazar());
	}
	
	public void setActiveGamer(int indexOfGamer){
		this.activeGamer = indexOfGamer;
	}
	
	public int getActiveGamer(){
		return activeGamer;
	}
	
	public void nextGamer(){
		activeGamer = (activeGamer + 1) % this.getCountGamers();
	}
	
	public void setScores(int[] scores){
		this.score = scores;
	}
	
	public void setNames(String[] names){
		this.gamersName = names;
	}
	
}