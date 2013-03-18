package domino;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;

import javax.swing.JFrame;

public class Client implements Runnable, Constants {
	private Thread thread;
	private MainFrame mainFrame;
	private boolean endGame;
	private DataInputStream in;
	private DataOutputStream out;
	private int activeGamer;
	private Socket socket;
	private InetAddress ipAddress;
	private String clientName;
	private String serverName;
	private int numberGamer;
	private Properties properties;
	private String[] names;
	
    public Client(JFrame jframe, MainFrame mainFrame, InetAddress ipAddress, String clientName) {
    	this.clientName = clientName;
    	this.ipAddress = ipAddress;
    	this.mainFrame = mainFrame;
    	this.properties = new Properties();
		try{
			this.properties.load(new FileInputStream("dominoes.properties"));
		}catch(IOException e){
		//	new HelpDialog(this, "Error", "");
		}
    	thread = new Thread(this, "client");
    	thread.start();
            }

	@Override
	public void run() {
		// TODO Auto-generated method stub
        endGame = true;
        try {
            setSocket(new Socket(getIPAddress(), getIntProperty("port")));       
            setDataInputStream(new DataInputStream(getSocket().getInputStream()));
            setDataOutputStream(new DataOutputStream(getSocket().getOutputStream()));
            getDataInputStream().readBoolean();
            setEndGame(false);
            names = new String[2];
            names[0] = getClientName();
            sendClientName();
            names[1] = readServerName();
        //    setGamersName();
            Game game = readGame();
            getMainFrame().drawClientGame(game, getActiveGamer() == getNumberGamer(), names);
            setMessage(game);
            while(!getEndGame()){
            	if((getActiveGamer() != getNumberGamer())){
            		try{
            			readGameStep();
            		} catch (SocketException se){
            			new DialogFrame(getMainFrame().getFrame(), "Server disconnected");
            			closeSocket();
            			setEndGame(true);
            			getMainFrame().getDrawClient().setVisible(false);
            			getMainFrame().showStartPanel();
            		}
            	} 
            }
        } catch (IOException x) {
            x.printStackTrace();
        }
	}
	
	private Properties getProperties(){
		return properties;
	}

	private int getIntProperty(String param){
		return Integer.valueOf(getProperties().getProperty(param));		
	}
	
	public synchronized void waitClient(){
		try {
			this.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void notifyClient(){
		notify();
	}
	
	private boolean isEndRound(Game game){
		if(game == null)
			return true;
		if(game.getGameLine().isFish()){
			getMainFrame().getDrawClient().showMessage("Fish!");
		//	new DialogFrame(getMainFrame().getFrame(), "Fish!");
			return true;
		}
		int countGamers = game.getGamers().length;
		for(int i = 0; i < countGamers; i++){
			if(game.getGamer(i).getCountStones() == 0){
				getMainFrame().getDrawClient().showMessage(names[i] + " win this round!");
			//	new DialogFrame(getMainFrame().getFrame(), names[i] + " win this round" + "gamer[" + i + "]:" + game.getGamer(i).getCountStones());
				return true;
			}
		}
		return false;
	}
	
	private void readGameStep() throws IOException{
			Game game = readGame();
			if(!isEndRound(game)){
				if(!game.canStep() && (getMainFrame().getDrawClient().getSizeMarket() == 0)){
					setMessage(anotherPlayerTurn + " in readGameStep");
				} else{
					getMainFrame().getDrawClient().dispenceStoneView(game);
					setCanChangeClient(getNumberGamer() == getActiveGamer());
					setMessage(game);
					if(getNumberGamer() == getActiveGamer())
						waitClient();
				}
			} else{
				readGameStep();
			}
	}
	
	private Socket getSocket(){
		return this.socket;
	}
	
	public String getServerName(){
		return this.serverName;
	}
	
	private String readServerName() throws IOException{
		return getDataInputStream().readUTF();
	}
	
	public void setIPAddress(InetAddress ipAddress){
		this.ipAddress = ipAddress;
	}
	
	private InetAddress getIPAddress(){
		return ipAddress;
	}
	
	private void setSocket(Socket socket){
		this.socket = socket;
	}
	
	public void closeSocket(){
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int getNumberGamer(){
		return numberGamer;
	}
	
	private DataOutputStream getDataOutputStream(){
		return out;
	}
	
	private void setDataOutputStream(DataOutputStream out){
		this.out = out;
	}
	
	private void sendClientName() throws IOException{
		getDataOutputStream().writeUTF(getClientName());
	}
	
	private String getClientName(){
		return clientName;
	}
	
	private DataInputStream getDataInputStream(){
		return in;
	}
	
	private void setDataInputStream(DataInputStream in){
		this.in = in;
	}
	
	private void setCanChangeClient(boolean canChange){
		mainFrame.getDrawClient().setCanChange(canChange);
	}
	
	private MainFrame getMainFrame() {
		return mainFrame;
	}

	public Game readGame() throws IOException{
		//read is win
		if(getDataInputStream().readBoolean()){
			int numberWinner = getDataInputStream().readInt();
			getMainFrame().getDrawClient().showMessage(getMainFrame().getDrawClient().getGamerName( ( getNumberGamer() + numberWinner) % names.length) + " winner!");
		//	new DialogFrame(getMainFrame().getFrame(), getMainFrame().getDrawClient().getGamerName( ( getNumberGamer() + numberWinner) % names.length) + " winner!");
			return null;
		} else {
		//number gamer
		setNumberGamer(readNumberGamer());
		//active gamer
		setActiveGamer(readActiveGamer());
		//send gamers
//		readCountStonesGamers();
		Gamer[] gamers = readGamers();
		//stones gamer
		gamers[1] = readGamer();
		gamers = clientsGamers(gamers, 1);
		//game line
		GameLineGoat gameLine = readGameLine();
		//bazar
		Bazar market = readBazar();
		//scores gamers
		int[] scores = readScores();
		scores =  clientsScores(scores, 1);
		return new Game(getNumberGamer(), gamers, gameLine, market, scores);
		}
	}
	
	private int[] clientsScores(int[] scores, int numberGamer){
		int countGamers = scores.length;
		int[] clientScores = new int[countGamers];
		for(int i = 0; i < countGamers; i++)
			clientScores[i] = scores[(numberGamer + i) % countGamers];
		return clientScores;
	}
	
	private Gamer[] clientsGamers(Gamer[] gamers, int numberGamer){
		int countGamers = gamers.length;
		Gamer[] clientGamers = new Gamer[countGamers];
		for(int i = 0; i < countGamers; i++)
			clientGamers[i] = gamers[(numberGamer + i) % countGamers];
		return clientGamers;
	}
	
	private int[] readScores() throws IOException{
		int countGamers = getDataInputStream().readInt();
		int[] scores = new int[countGamers];
		for(int i = 0; i < countGamers; i++)
			scores[i] = getDataInputStream().readInt();
		return scores;
	}
	
	public void sendStepGamer(Stone stone, boolean inStartGameLine) throws IOException{
		getDataOutputStream().writeInt(0);
		sendStone(stone);
		getDataOutputStream().writeBoolean(inStartGameLine);
		setActiveGamer(0);
		setCanChangeClient(false);
		getMainFrame().getDrawClient().setMessage(anotherPlayerTurn);
	}
	
	public void sendCanNotStep() throws IOException{
		getDataOutputStream().writeInt(2);
		setCanChangeClient(false);
		setMessage(canNotTurn);
	}
	
	private void sendStone(Stone stone) throws IOException{
		getDataOutputStream().writeInt(stone.hashCode());
	}
	
	public Stone sendGetBazar(int numberStone)throws IOException{
		getDataOutputStream().writeInt(1);
		getDataOutputStream().writeInt(numberStone);
		Stone stone = readStone();
		return stone;
	}
	
	private int readActiveGamer() throws IOException{
		return getDataInputStream().readInt();
	}
	
	private int readNumberGamer() throws IOException{
		return getDataInputStream().readInt();
	}
	
	private Gamer[] readGamers() throws IOException{
		int countGamers = getDataInputStream().readInt(); 
		Gamer[] gamers = new Gamer[countGamers];
		for(int i = 0; i < countGamers; i++){
			gamers[i] = new Gamer(getDataInputStream().readInt());
		}
		return gamers;
	}
	
	private void setMessage(String message){
		getMainFrame().getDrawClient().setMessage(anotherPlayerTurn);
	}
	
	private void setMessage(Game game){
		if(getNumberGamer() == getActiveGamer()){
			if(game.canStep())
				getMainFrame().getDrawClient().setMessage(yourTurn);
			if(!game.canStep() && (getSizeBazar() != 0))
				getMainFrame().getDrawClient().setMessage(takeFromMarket);
			if(!game.canStep() && (getSizeBazar() == 0))
				getMainFrame().getDrawClient().setMessage(anotherPlayerTurn);
		};
		if(getNumberGamer() != getActiveGamer()){
			getMainFrame().getDrawClient().setMessage(anotherPlayerTurn);
		}
	}
	
	private Stone readStone() throws IOException{
		int hashCode = getDataInputStream().readInt();
		return new Stone(hashCode / 10, hashCode % 10);
	}
	
	private Gamer readGamer() throws IOException{
		int countStone = getDataInputStream().readInt();
		HashSet<Stone> stones = new HashSet<Stone>();
		for(int i = 0; i < countStone; i++){
			stones.add(readStone());
		}
		Gamer gamer = new Gamer(stones);
		return gamer;
	}
	
	private GameLineGoat readGameLine() throws IOException{
		int countStones = getDataInputStream().readInt();
		ArrayList<Stone> gameLine = new ArrayList<Stone>();
		for(int i = 0; i < countStones; i++){
			gameLine.add(readStone());
		}
		GameLineGoat gameLineGoat = new GameLineGoat(gameLine);
		//read start stone number
		gameLineGoat.setStartCount(getDataInputStream().readInt());
		//read start number
		gameLineGoat.setStartNumber(getDataInputStream().readInt());
		//read end number
		gameLineGoat.setEndNumber(getDataInputStream().readInt());
		return gameLineGoat;
	}
	
	private Bazar readBazar() throws IOException{
		return new Bazar(getDataInputStream().readInt());
	}
	
	private void setActiveGamer(int activeGamer){
		this.activeGamer = activeGamer;
	}
	
	private int getActiveGamer(){
		return activeGamer;
	}
	
	private int getSizeBazar(){
		return mainFrame.getDrawClient().getSizeMarket();
	}
	
	public void setEndGame(boolean endGame){
		this.endGame = endGame;
	}
	
	public boolean getEndGame(){
		return endGame;
	}
	
	private void setNumberGamer(int numberGamer){
		this.numberGamer = numberGamer;
	}
}