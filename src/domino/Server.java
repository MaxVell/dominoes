package domino;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Properties;

public class Server implements Runnable, Constants{
	private Thread thread;
	private MainFrame jframe;
	private Game game;
	private boolean startGame;
	private CreateDialog crDialog;
	private boolean endGame;
	private DataOutputStream[] out; 
	private DataInputStream[] in;
	private ServerSocket ss;
	private Socket[] socket;
	private int numberServer = 0;
	private Properties properties;
	
	
	
   public Server(MainFrame jframe, Game game){
	   this.jframe = jframe;
	   this.game = game;
	   this.properties = new Properties();
		try{
			this.properties.load(new FileInputStream("dominoes.properties"));
		}catch(IOException e){
		//	new HelpDialog(this, "Error", "");
		}
	   thread = new Thread(this,"server");
	   thread.start();
	   
	      }

@Override
public void run() {
	// TODO Auto-generated method stub
    endGame = false;
      try {
    	if(createServerSocket()){
    		crDialog = new CreateDialog(jframe, this);
    		if(connectClient()){
    			crDialog.addConnection(getSocket().toString());
    			waitServer();
    			setDataInputStream();
    			setDataOutputStream();
    			
    			getDataOutputStream(0).writeBoolean(startGame);
    			readClientName();
    			sendServerName();
        	
    			sendGame(jframe.getGame(), 1);
    			while(!endGame){
    				if(jframe.getGame().getActiveGamer() != getNumberServer()){
    					try{
    						dialogClient();
    					} catch (SocketException se){
    						new DialogFrame(jframe.getFrame(), "Client disconnected");
    						getServerSocket().close();
    						endGame = true;
    						getMainFrame().getDrawGame().setVisible(false);
    					}
    				}
    			}
        }	}
     } catch(IOException x) {
    	 x.printStackTrace(); 
    	 }
}

private Properties getProperties(){
	return this.properties;
}

private int getIntProperty(String param){
	return Integer.valueOf(getProperties().getProperty(param));		
}

public synchronized void waitServer(){
	try {
		wait();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public synchronized void notifyServer(){
	notify();
}

private int getNumberServer(){
	return this.numberServer;
}

private void setDataInputStream(){
	int countSocket = getSocket().length;
	this.in = new DataInputStream[countSocket];
	for(int i = 0; i < countSocket; i++){
		try {
			this.in[i] = new DataInputStream(getSocket(i).getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

private void setDataOutputStream(){
	int countSocket = getSocket().length;
	this.out = new DataOutputStream[countSocket];
	for(int i = 0; i < countSocket; i++){
		try {
			this.out[i] = new DataOutputStream(getSocket(i).getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


private boolean connectClient(){
	try{
		setSocket(getServerSocket().accept()); 
	} catch(IOException e){
		return false;
	}
	return true;
}

private boolean createServerSocket(){
	try{
		try{
    		setServerSocket(new ServerSocket(getIntProperty("port"))); 
    	} catch(BindException be){
    		new DialogFrame(getMainFrame().getFrame(), "The another server is running!");
    		return false;
    	}
	}catch(IOException ioe){
		new DialogFrame(getMainFrame().getFrame(), "Create server error!");
		return false;
	}
	return true;
}

private Socket[] getSocket(){
	return this.socket;
}

private Socket getSocket(int number){
	return this.socket[number];
}

private void setSocket(Socket socket){
	if(getSocket() == null){
		this.socket = new Socket[1]; 
		this.socket[0] = socket;
	} else{
		Socket[] oldSocket = getSocket();
		int oldSocketLength = oldSocket.length;
		this.socket = new Socket[oldSocketLength + 1];
		
		for(int i = 0; i < oldSocketLength; i++)
			this.socket[i] = oldSocket[i];
			this.socket[oldSocketLength] = socket;
		}
	
}

private void setServerSocket(ServerSocket ss){
	this.ss = ss;
}

private ServerSocket getServerSocket(){
	return this.ss;
}

public void closeServerSocket(){
		try {
			this.ss.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}

private DataInputStream getDataInputStream(int number){
	return this.in[number];
}

private DataOutputStream getDataOutputStream(int number){
	return this.out[number];
}

private void setCanChangeServer(boolean canChange){
	getMainFrame().getDrawGame().setCanChange(canChange);
}

private MainFrame getMainFrame(){
	return this.jframe;
}

public void dialogClient() throws IOException{
	boolean isEndStep = false;
	int numStep;
	do{
		numStep = getDataInputStream(0).readInt();
		switch(numStep){
		case 0:
			Stone stone = readStone();
			boolean inStart = getDataInputStream(0).readBoolean();
			if(inStart){
				jframe.getDrawGame().addStartGameLineStone(1, jframe.getDrawGame().getStoneView(1, stone), false);
			}
			if(!inStart){
				jframe.getDrawGame().addEndGameLineStone(1, jframe.getDrawGame().getStoneView(1, stone), false);
			}
			isEndStep = true;
			break;
		case 1:
			int numbStone = getDataInputStream(0).readInt();
				Stone s = jframe.getDrawGame().putFromMarket(numbStone);
				sendStone(s);
				jframe.getDrawGame().repaint();
			break;
		case 2:
			jframe.getDrawGame().getGame().nextGamer();
			isEndStep = true;
			break;
		}
	}while(!isEndStep);
	setCanChangeServer(getGame().getActiveGamer() == getNumberServer());
	setMessage(jframe.getDrawGame().getGame());
	if(!jframe.getDrawGame().getGame().canStep() && (jframe.getDrawGame().getGame().getBazar().getCountStones() == 0)){
		jframe.getDrawGame().getGame().nextGamer();
		sendGame(jframe.getDrawGame().getGame(), 1);
	}
	jframe.getDrawGame().repaint();
	if(getGame().getActiveGamer() == getNumberServer()) 
		waitServer();
}

private Stone readStone() throws IOException{
	int hashCode = getDataInputStream(0).readInt();
	return new Stone(hashCode / 10, hashCode % 10);
}

public void sendWinner() throws IOException{
	getDataOutputStream(0).writeBoolean(true);
	getDataOutputStream(0).writeInt(getGame().getNumberWinner());
	getGame().newScores();
}

private void readClientName() throws IOException{
	getGame().setGamerName(1, getDataInputStream(0).readUTF());
}

private void sendServerName() throws IOException{
	getDataOutputStream(0).writeUTF(getGame().getGamersName()[0]);
}

private void setMessage(Game game){
	if(game.isEndRound() && (game.getGamers()[1].getCountStones() == 0)){
	//	getGame().scoreCount();
	//	new DialogFrame(getMainFrame().getFrame(), anotherPlayerWinRound);
	} else {
	if(game.getActiveGamer() == 0){
		if(game.canStep()) 
			getMainFrame().getDrawGame().setMessage(yourTurn);
		if(!game.canStep() && getSizeMarket() != 0)
			getMainFrame().getDrawGame().setMessage(takeFromMarket);
		if(!game.canStep() && getSizeMarket() == 0)
			getMainFrame().getDrawGame().setMessage(canNotTurn);
	}
	if(game.getActiveGamer() != 0){
		getMainFrame().getDrawGame().setMessage(anotherPlayerTurn);
	}
	}
}

private int getSizeMarket(){
	return this.jframe.getDrawGame().getSizeMarket();
}

private void sendStone(Stone stone) throws IOException{
	getDataOutputStream(0).writeInt(stone.hashCode());
}

private void sendGamer(Gamer gamer) throws IOException{
	int countGamerStone = gamer.getCountStones();
	getDataOutputStream(0).writeInt(countGamerStone);
	for(int i = 0; i < countGamerStone; i++){
		sendStone(gamer.getStone(i));
	}
}

private void sendGamers(Gamer[] gamers) throws IOException{
	//	count gamers
	getDataOutputStream(0).writeInt(gamers.length);
	//	count games stones
	int countGamers = gamers.length;
		for(int i = 0; i < countGamers; i++){
			getDataOutputStream(0).writeInt(gamers[i].getCountStones());
		}
}

private void sendGameLine(GameLineGoat gameLine) throws IOException{
	int countGameLineStone = gameLine.length(); 
	//length 
	getDataOutputStream(0).writeInt(countGameLineStone);
	for(int i = 0; i < countGameLineStone; i++){
		sendStone(gameLine.getStone(i));
	}
	//number start stone 
	getDataOutputStream(0).writeInt(gameLine.getStartCount());
	//start number
	getDataOutputStream(0).writeInt(gameLine.getStartNumber());
	//end number
	getDataOutputStream(0).writeInt(gameLine.getEndNumber());
	//game line stones
}

private void sendBazar(Bazar bazar) throws IOException{
	getDataOutputStream(0).writeInt(bazar.getCountStones());
}

private void sendScores() throws IOException{
	//send count gamers
	getDataOutputStream(0).writeInt(getGame().getGamers().length);
	//send scores
	int countGamers = getGame().getGamers().length;
	for(int i = 0; i < countGamers; i++){
		getDataOutputStream(0).writeInt(getGame().getScore(i));
	}
}

public void sendGame(Game game, int numberGamer) throws IOException{
	setMessage(game);
	//is End game
	getDataOutputStream(0).writeBoolean(false);
	//number winner
//	getDataOutputStream().writeInt(0);
	//number gamer
	getDataOutputStream(0).writeInt(numberGamer);
	//send active gamer
	getDataOutputStream(0).writeInt(game.getActiveGamer());
	//send gamers
	sendGamers(game.getGamers());
	//stones gamer
	sendGamer(game.getGamers()[numberGamer]);
	//game line
	sendGameLine(game.getGameLine());
	//bazar
	sendBazar(game.getBazar());
	//sendScores
	sendScores();
//	new DialogFrame(jframe.getFrame(), "Active gamer: "+ game.getActiveGamer() + " count gamers: " + game.getGamers().length + " count stones: "+ game.getGamer(1).getCountStones());
	setCanChangeServer(getGame().getActiveGamer() == 0);
	
//	
}

public boolean isStartGame(){
	return this.startGame;
}

public void startGame(boolean startGame){
	this.startGame = startGame;
}

private Game getGame(){
	return this.game;
	
}
}