package domino;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawGame extends JPanel implements Constants{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4917067303067920944L;
	private Graphics2D g;
	private Game game;
	private MainFrame mainFrame;	
	private Properties properties;
	private boolean canChange;
	private ArrayList<StoneView> stones;
	private DrawComponents dComponents;
	private StoneView[] stoneRect;
	private GamerView[] gamerView;
	private MarketView marketView;
	private GameLineView gameLineView;
	private String message;
	private MessagePanel messagePanel;
	private FrontPanel frontPanel;

	public DrawGame(Game game, MainFrame mainFrame, int menuHeight){
		setLayout(null);
		dComponents = new DrawComponents(this);
		this.game = game;
		this.mainFrame = mainFrame;
		message = "Start game";
		properties = new Properties();
		try{
			properties.load(new FileInputStream("dominoes.properties"));
		}catch(IOException e){
		//	new HelpDialog(this, "Error", "");
		}
		frontPanel = new FrontPanel(getFrame().getWidth(), getFrame().getHeight());
		addFrontPanelResizeListener();
		add(frontPanel);
		messagePanel = new MessagePanel();
		messagePanel.setVisible(false);
		add(messagePanel);
		setNames();
		createStonesView();
		createComponents(getGame(), stones);
		dispence();
		createStoneRect();
	}
	
	private void addFrontPanelResizeListener(){
		this.addComponentListener(new java.awt.event.ComponentAdapter(){
			public void componentResized(java.awt.event.ComponentEvent event){
				getFrontPanel().setSize(getFrame().getWidth(), getFrame().getHeight());
			}
		});
	}
	
	private void setNames(){
		int countGamers = getGame().getGamers().length;
		for(int i = 0; i < countGamers; i++)
			if(getGame().getGamerName(i) == null)
				getGame().setGamerName(i, "");
	}
	
	private void dispence(){
		int countGamers = getGame().getGamers().length;
		int countStones = getIntProperty("countStoneGamer");
		for(int i = 0; i < countGamers; i++)
			for(int j = 0; j < countStones; j++){
				getGamerView(i).addStone(getMarketView().putRandomStoneView());
		}
		game.setActiveGamer(this.game.whoIsFirst());
	}
	
	private void createStoneRect(){
		stoneRect = new StoneView[2];
		stoneRect[0] = new StoneView(new Stone(0,0));
		stoneRect[1] = new StoneView(new Stone(0,0));
		add(stoneRect[0]);
		add(stoneRect[1]);
		stoneRect[0].setVisible(false);
		stoneRect[1].setVisible(false);
	}
	
	private FrontPanel getFrontPanel(){
		return frontPanel;
	}
	
	private void createComponents(Game game, ArrayList<StoneView> stones){
		gameLineView = new GameLineView(game.getGameLine());
		int countGamers = game.getGamers().length;
		gamerView = new GamerView[countGamers];
		for(int i = 0; i < countGamers; i++){
			gamerView[i] = new GamerView(game.getGamer(i), this, getFrontPanel());
		}
		marketView = new MarketView(game.getBazar(), stones, this);
	}
	
	public void resetStoneView(){
		int countGamers = getGame().getGamers().length;
		for(int i = 0; i < countGamers; i++){
			int countStones = getGamerView(i).getStones().size();
			for(int j = 0; j < countStones; j++){
				getMarketView().addStone(getGamerView(i).putLast());
			}
		}
		int countStones = getGameLineView().getStones().size();
		for(int i = 0; i < countStones; i++){
			getMarketView().addStone(getGameLineView().putStone());
		}
	}
	
	private void createStonesView(){
		stones = new ArrayList<StoneView>();
		for(int i = 0; i < 28; i++){
			getStones().add(new StoneView());
			add(getStones().get(getStones().size() - 1));
		}
	}
	
	public StoneView[] getStoneRect(){
		return stoneRect;
	}

	private ArrayList<StoneView> getStones(){
		return stones;
	}

	public void putFromMarket(StoneView stoneView){
		getGamerView(getGame().getActiveGamer()).addStone(getMarketView().putStoneView(stoneView));
		if(getGame().canStep()){
			setMessage(yourTurn);
		}
		if(!getGame().canStep() && getSizeMarket() == 0){
			while(!getGame().getGameLine().canStep(getGame().getGamer(getGame().getActiveGamer()))){
				getGame().nextGamer();
				setCanChange(getGame().getActiveGamer() == 0);
				setMessage(canNotTurn);
				sendGame();
			}
		} else{
			try {
				getServer().sendGame(getGame(), 1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
	
/*	public void putFromMarket(Stone stone){
		getGamerView(getGame().getActiveGamer()).addStone(getMarketView().putStoneView(stone));
	}
*/	
	public Stone putFromMarket(int index){
		StoneView stone = getMarketView().putStone(index);
		getGamerView(1).addStone(stone);
		while(!getGame().getGameLine().canStep(getGame().getGamer(getGame().getActiveGamer())) && marketView.getStones().size() == 0){
			getGame().nextGamer();
			setCanChange(getGame().getActiveGamer() == 0);
			if(getGame().getActiveGamer() == 0)
				setMessage(yourTurn);
			else
				setMessage(anotherPlayerTurn);
		//	sendGame();
		}
		return stone.getStone();
	}
	
	public void addStartGameLineStone(int numberGamer, StoneView stoneView, boolean isServer){
		getGamerView(numberGamer).removeStoneView(stoneView);
		getGameLineView().addStart(stoneView);
		getGame().stepStart(numberGamer, stoneView.getStone());
		setCanChange(getGame().getActiveGamer() == 0);
		getGameLineView().setGameLine(getGame().getGameLine());
		check(isServer);
	}
	
	public void addEndGameLineStone(int numberGamer, StoneView stoneView, boolean isServer){
		getGamerView(numberGamer).removeStoneView(stoneView);
		getGameLineView().addEnd(stoneView);
		getGame().stepEnd(numberGamer, stoneView.getStone());
		setCanChange(getGame().getActiveGamer() == 0);
		getGameLineView().setGameLine(getGame().getGameLine());
		check(isServer);
	}

	private void check(boolean isServer){
		while(!getGame().getGameLine().canStep(getGame().getGamer(getGame().getActiveGamer())) && getSizeMarket() == 0 && !getGame().getGameLine().isFish()){
			getGame().nextGamer();
			setCanChange(getGame().getActiveGamer() == 0);
			if(getGame().getActiveGamer() != 0)
				setMessage(canNotTurn);
			sendGame();
		}
		if(isServer){
			sendGame();
		}
		if(isEndRound(isServer)){
			newRound();
		}
	}
	
	
	public boolean isEndRound(boolean isServer){
		if(getGame().getGameLine().isFish()){
			showMessage("Fish!");
			return true;
		}
		int countGamers = gamerView.length;
		for(int i = 0; i < countGamers; i++)
		if(getGamerView(i).size() == 0){
			if(isServer){
				showMessage(youWinRound);
			} else{
				showMessage(anotherPlayerWinRound);
			}
			return true;
		}
			return false;
	}
	
	public GamerView getGamerView(int index){
		return gamerView[index];
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
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		setGraphics2D((Graphics2D)g);
		dComponents.drawGame(getGraphics2D(), gamerView, getMarketView(), getGameLineView(), stoneRect,getGame().getGamersName(), getGame().getScore(),getMessage(), getMessagePanel());
	}
	
	private void setGraphics2D(Graphics2D g){
		this.g = g;
	}
	
	public StoneView getStoneView(int numberGamer, Stone stone){
		return gamerView[numberGamer].getStoneView(stone);
	}
	
	public int getSizeMarket(){
		return marketView.getStones().size();
	}
	
	public void setServerName(String serverName){
		getGame();
	}
	
	private Graphics2D getGraphics2D(){
		return g;
	}
	
	public Game getGame(){
		return game;
	}
	
	public void sendGame(){
		try{
			getServer().notifyServer();
			getServer().sendGame(getGame(), 1);
		}catch (IOException exception){
			
		}
	}
	
	private Server getServer(){
		return mainFrame.getServer();
	}
	
	public int getScoreGamer(int number){
		return getGame().getScore(number);
	}
	
	private void sendWinner(){
		try{
			mainFrame.getServer().sendWinner();
		}catch (IOException exception){
			
		}
	}

/*	private void setStonesInNewRound(Game game){
		int countGamers = getGame().getGamers().length;
		int countStones = getIntProperty("countStoneGamer");
		for(int i = 0; i < countGamers; i++)
			for(int j = 0; j < countStones; j++){
				gamerView[i].getStoneView(j).setStone(getGame().getGamer(i).getStone(j));
		}
		countStones = getGame().getBazar().getCountStones();
		for(int i = 0; i < countGamers; i++)
			marketView.getStones().get(i).setStone(getGame().getBazar().getStone(i));
	}*/
	
	private void newRound(){
		getGame().scoreCount();
		if(getGame().isEndGame()){
			sendWinner();
			showMessage(getGame().getGamerName(getGame().getNumberWinner()) + " win game!");
			//	new DialogFrame(getFrame(), getGame().getGamerName(getGame().getNumberWinner()) + " win game!");
			getGame().newScores();
		} 
		resetStoneView();
		getGame().dispence();
		dispence();
		sendGame();
		repaint();
		
		
		
		
/*		removeAll();
		getGame().scoreCount();
		if(getGame().isEndGame()){
			sendWinner();
			showMessage(getGame().getGamerName(getGame().getNumberWinner()) + " win game!");
			//	new DialogFrame(getFrame(), getGame().getGamerName(getGame().getNumberWinner()) + " win game!");
			getGame().newScores();
		}
		getGame().dispence();
		createStonesView();
		createComponents(getGame(), stones);
		dispence();
		createStoneRect();
		sendGame();
		repaint();*/
	}
	
	public void showMessage(String message){
		messagePanel.setVisible(true);
		messagePanel.setMessage(message);
	}
	
	private MessagePanel getMessagePanel(){
		return messagePanel;
	}
	
	public void setMessage(String message){
		this.message = message;
		repaint();
	}
	
	private JFrame getFrame(){
		return mainFrame.getFrame();
	}
	
	private String getMessage(){
		return message;
	}
	
	private GameLineView getGameLineView(){
		return gameLineView;
	}

	private MarketView getMarketView(){
		return marketView;
	}
/*	private GamerView getGamerView(int index){
		return this.gamerView[index];
	}*/
}
