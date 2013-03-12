package domino;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainFrame extends JPanel{
 
	private static final long serialVersionUID = 9089671123674441332L;
	private JMenuBar menuBar;
	private Properties properties;
	private JFrame jfrm;
	private Game game;
	private DrawGame drawGame;
	private DrawClient drawClient;
	private MainFrame mainFrame;
	private boolean startGame;
	private Server server;
	private Client client;
	private boolean createClient;
	private InetAddress ipAddress;
	private String clientName;
	private StartPanel startPanel;
	private WaitPanel wPanel;
	
	public MainFrame(){
		jfrm = new JFrame("Dominoes");
		mainFrame = this;
		startGame = false;
		properties = new Properties();
		try{
			properties.load(new FileInputStream("dominoes.properties"));
		}catch(IOException e){
		}
		jfrm.setSize(getIntProperty("FrameStartSizeX"), getIntProperty("FrameStartSizeY"));
		jfrm.setMinimumSize(new Dimension(getIntProperty("FrameMinimumSizeX"), getIntProperty("FrameMinimumSizeY")));
		menuBar = loadMenu();
		
/*		createGame(2);
		this.drawGame = new DrawGame(getGame(), this.mainFrame, 24);
		this.jfrm.add(drawGame);*/
		
		jfrm.add(startPanel = new StartPanel(this));
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jfrm.setJMenuBar(menuBar);
//		wPanel = new WaitPanel();
	//	jfrm.add(wPanel);
		jfrm.setVisible(true);
	}
	
	public static void main(String args[]){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
					new MainFrame();
					// TODO Auto-generated catch block
			}
		});
	}
	
	public void showWaitPanel(){
		wPanel.setIsturn(true);
		wPanel.setVisible(true);
	}
	
	public void hideWaitPanel(){
		wPanel.setIsturn(false);
		wPanel.setVisible(false);
	}
	
	public void setIPAddress(InetAddress ipAddress){
		this.ipAddress = ipAddress;
	}
	
	private InetAddress getIPAddress(){
		return ipAddress;
	}
	
	private Properties getProperties(){
		return properties;
	}
	
	private int getIntProperty(String param){
		return Integer.valueOf(getProperties().getProperty(param));		
	}
	
	private JMenuBar loadMenu(){
		JMenuBar mbar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem create = new JMenuItem("Create...");
        create.addActionListener(new ButtonCreateListener(mainFrame));
        file.add(create);
        JMenuItem connect = new JMenuItem("Connect...");
        connect.addActionListener(new ButtonConnectListener(mainFrame));
        file.add(connect);
        file.addSeparator();
        JMenuItem quit = new JMenuItem("Quit...");
        quit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
					if(getServer() != null)
						getServer().closeServerSocket();
				
				mainFrame.getFrame().dispose();
			}
        	
        });
        file.add(quit);
        mbar.add(file);
        mbar.getHeight();
		 return mbar;
	}

	public void createClient(){
		if(getCreateClient()){
			//	this.showWaitPanel();
				setClient(new Client(jfrm, mainFrame, getIPAddress(), getClientName()));
		}
	}
	
	public void setClientName(String name){
		this.clientName = name;
	}
	
	public void hideStartPanel(){
		startPanel.setVisible(false);
	}
	
	private String getClientName(){
		return clientName;
	}
	
	private boolean getCreateClient(){
		return createClient;
	}
	
	public void setCreateClient(boolean createClient){
		this.createClient = createClient;
	}
	
	public void createServer(){
		setServer(new Server(mainFrame, getGame()));
	}
	
	private void setServer(Server server){
		this.server = server;
	}
	
	public Server getServer(){
		return server;
	}
	
	private void setClient(Client client){
		this.client = client;
	}
	
	public Client getClient(){
		return client;
	}
	
	public boolean startGame(){
		return startGame;
	}
	
	public JFrame getFrame(){
		return jfrm;
	}
	
	public void setStartGame(boolean startGame){
		this.startGame = startGame;
	}
	
	public void setGame(Game game){
		this.game = game;
	}
	
	public void createGame(int countGamers){
		this.game = new Game(countGamers);
	//	this.game.dispence();
	}
	
	public DrawGame getDrawGame(){
		return drawGame;
	}
	
	public DrawClient getDrawClient(){
		return drawClient;
	}
	
	public void drawClientGame(Game game, boolean canChange, String[] names){
		hideStartPanel();
		drawClient = new DrawClient(mainFrame, getClientName(), game, canChange, names);
		jfrm.add(drawClient);
		drawClient.revalidate();
	}
	
	public void drawGame(){
		drawGame = new DrawGame(getGame(), this.mainFrame, 24);
		hideStartPanel();
		this.jfrm.add(drawGame);
		drawGame.revalidate();
	}
	
	public Game getGame(){
		return game;
	}
	
	public int getMenuHeight(){
		return menuBar.getHeight();
	}
	
	public int getMenuWidth(){
		return menuBar.getWidth();
	}
}