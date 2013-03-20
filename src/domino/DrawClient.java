package domino;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import javax.swing.JPanel;

public class DrawClient extends JPanel implements Constants{

/**
	 * 
	 */
private static final long serialVersionUID = -3635322294163926941L;
private int numberGamer;
private String message = "Start game";
private Graphics2D g;
private MainFrame mainFrame;	
private Properties properties;
private boolean canChange = true;
private String[] gamersName;
private ArrayList<StoneView> stones;
private DrawComponents dComponents;
private StoneView[] stoneRect;
private GamerView[] gamerView;
private MarketView marketView;
private GameLineView gameLineView;
private Game game;
private MessagePanel messagePanel;

public DrawClient(MainFrame mainFrame, String gamerName, Game game, boolean canChange, String[] names){
	setLayout(null);
	this.game = game;
	dComponents = new DrawComponents(this);
	this.mainFrame = mainFrame;
	this.canChange = canChange;
	this.gamersName = names;
	properties = new Properties();
	try{
		properties.load(new FileInputStream("dominoes.properties"));
	}catch(IOException e){
	//	new HelpDialog(this, "Error", "");
	}
	messagePanel = new MessagePanel();
	messagePanel.setVisible(false);
	add(messagePanel);
	createStonesView();
	createComponents(game, stones);
	dispence(game);
//	mainFrame.hideWaitPanel();
}

private void dispence(Game game){
	int countGamers = game.getGamers().length;
	int countStones = getIntProperty("countStoneGamer");
	for(int i = 0; i < countGamers; i++)
		for(int j = 0; j < countStones; j++){
			getGamerView(i).addStone(marketView.putStone(0));
	}
	for(int j = 0; j < countStones; j++)
		getGamerView(0).getStoneView(j).setStone(game.getGamer(0).getStone(j));
}

private void createComponents(Game game, ArrayList<StoneView> stones){
	gameLineView = new GameLineView(game.getGameLine());
	int countGamers = game.getGamers().length;
	gamerView = new GamerView[countGamers];
	for(int i = 0; i < countGamers; i++){
		gamerView[i] = new GamerView(game.getGamer(i), this);
	}
	marketView = new MarketView(game.getBazar(), stones, this);
	stoneRect = new StoneView[2];
	stoneRect[0] = new StoneView(new Stone(0,0));
	stoneRect[1] = new StoneView(new Stone(0,0));
	add(stoneRect[0]);
	add(stoneRect[1]);
	stoneRect[0].setVisible(false);
	stoneRect[1].setVisible(false);
}

private void createStonesView(){
	stones = new ArrayList<StoneView>();
	for(int i = 0; i < 28; i++){
		stones.add(new StoneView());
		add(stones.get(i));
	}
}

public Game getGame(){
	return game;
}

public StoneView[] getStoneRect(){
	return stoneRect;
}

private GamerView[] getGamersView(){
	return gamerView;
}

public GamerView getGamerView(int index){
	return gamerView[index];
}

public void addStartGameLineStone(StoneView stoneView){
	getGamerView(0).removeStoneView(stoneView);
	getGameLineView().addStart(stoneView);
	try {
		notifyClient();
		getClient().sendStepGamer(stoneView.getStone(), true);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	getGame().stepStart(0, stoneView.getStone());
	getGameLineView().setGameLine(getGame().getGameLine());
	if(getGamerView(0).getStones().size() == 0){
		showMessage(youWinRound);
	}
	if(getGame().getGameLine().isFish())
		showMessage("Fish!");
}

public void addEndGameLineStone(StoneView stoneView){
	getGamerView(0).removeStoneView(stoneView);
	getGameLineView().addEnd(stoneView);
	try {
		notifyClient();
		getClient().sendStepGamer(stoneView.getStone(), false);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	getGame().stepEnd(0, stoneView.getStone());
	if(getGamerView(0).getStones().size() == 0){
		showMessage(youWinRound);
	}
	if(getGame().getGameLine().isFish())
		showMessage("Fish!");
}

public void putFromMarket(StoneView stoneView){
	StoneView s =  getMarketView().putStoneView(stoneView);
	Stone stone = new Stone(0,0);
	Random rand = new Random();
	int numb = rand.nextInt(getMarketView().getStones().size() + 1);
	try {
		stone = getClient().sendGetBazar(numb);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	s.setStone(stone);
	getGame().getGamer(0).addStone(stone);
	getGamerView(0).addStone(s);
	if(getGame().canStep()){
		setMessage(yourTurn);
	}
	if(!getGame().canStep() && getSizeMarket() == 0){
		notifyClient();
		setMessage(anotherPlayerTurn);
	}
	repaint();
}

public void dispenceStoneView(Game game){
	this.game = game;
	putStonesToMarket(game.getGamers(), game.getGameLine());
	setGamers(game.getGamers());
	setGameLine(game.getGameLine());
	repaint();
}

private void putStonesToMarket(Gamer[] gamers,GameLineGoat  gameLine){
	int countGamers = gamers.length;
	for(int i = 0; i < countGamers; i++)
	while((getGamerView(i).size() - gamers[i].getCountStones()) > 0){
		getMarketView().addStone(getGamerView(i).putLast());
	}
	while((gameLine.length() - getGameLineView().getStones().size()) < 0){
		getMarketView().addStone(getGameLineView().putStone());
	}
}

private void setGamers(Gamer[] gamers){
	int countGamers = gamers.length; 
	for(int i = 0; i < countGamers; i++){
		while((getGamerView(i).size() - gamers[i].getCountStones()) < 0){
			getGamerView(i).addStone(getMarketView().putRandomStoneView());
		}
	}
	int countStones = gamers[0].getCountStones();
	for(int i = 0; i < countStones; i++){
		getGamerView(0).getStoneView(i).setStone(gamers[0].getStone(i));
	}
}

private void setGameLine(GameLineGoat gameLine){
	while((gameLine.length() - getGameLineView().getStones().size()) > 0)
		getGameLineView().addStart(getMarketView().putRandomStoneView());
	int countStones = gameLine.length();
	for(int i = 0; i < countStones; i++){
		getGameLineView().getStoneView(i).setStone(gameLine.getStone(i));
	}
	getGameLineView().setGameLine(gameLine);
}

public void setGamerName(int numberGamer, String name){
	this.gamersName[numberGamer] = name;
}

public String getGamerName(int numberGamer){
	return gamersName[numberGamer];
}

private Properties getProperties(){
	return properties;
}

private int getIntProperty(String param){
	return Integer.valueOf(getProperties().getProperty(param));		
}

public void setCanChange(boolean canChange){
	this.canChange = canChange;
}

public boolean canChange(){
	return canChange;
}

public void setNumberGamer(int numberGamer){
	this.numberGamer = numberGamer;
}

public int getNumberGamer(){
	return numberGamer;
}


protected void paintComponent(Graphics g){
	super.paintComponent(g);
	setGraphics2D((Graphics2D)g);	
	dComponents.drawGame(getGraphics2D(), getGamersView(), getMarketView(), getGameLineView(), stoneRect, gamersName, getGame().getScore(), getMessage(), getMessagePanel());
}

private MessagePanel getMessagePanel(){
	return messagePanel;
}

private void setGraphics2D(Graphics2D g){
	this.g = g;
}

public void showMessage(String message){
	messagePanel.setMessage(message);
	messagePanel.setVisible(true);
}

private Client getClient(){
	return mainFrame.getClient();
}

private String getMessage(){
	return message;
}

public void setMessage(String message){
	//new DialogFrame(mainFrame.getFrame(), message);
	this.message = message;
	repaint();
}

public int getSizeMarket(){
	return /*getGame().getBazar().getCountStones();*/marketView.getStones().size();
}

private Graphics2D getGraphics2D(){
	return g;
}

/*private JFrame getMainFrame(){
	return mainFrame.getFrame();
}

/*protected int getStoneWidth(){
	return this.width;
}*/

public int getStoneWidth(){
	return dComponents.getStoneWidth();
}

private GameLineView getGameLineView(){
	return gameLineView;
}

private MarketView getMarketView(){
	return marketView;
}

private void notifyClient(){
	getClient().notifyClient();
}
}
