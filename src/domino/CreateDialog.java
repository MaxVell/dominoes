package domino;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class CreateDialog extends Dialog implements ActionListener, WindowListener, Constants{
	
	private List gamers;
	private JLabel text;
	private JButton buttonStart;
	private Server server;
	private MainFrame mainFrame;
	private JLabel labelServerName;
	private JLabel errorMessage;
	private JTextField textServerName;
	private Properties properties;
	/**
	 * 
	 */
	private static final long serialVersionUID = 7326400400932610381L;

	public CreateDialog(MainFrame mainFrame, Server server){
		super(mainFrame.getFrame(), "Create game.", false);
		properties = new Properties();
		try{
			properties.load(new FileInputStream("dominoes.properties"));
		}catch(IOException e){
		//	new HelpDialog(this, "Error", "");
		}
		this.mainFrame = mainFrame;
		this.server = server;
		GridBagLayout gbLayout = new GridBagLayout();
		setLayout(gbLayout);
		setMinimumSize(new Dimension(getIntProperty("CreateFrameMinimumSizeX"), getIntProperty("CreateFrameMinimumSizeY")));
		setSize(getIntProperty("CreateFrameStartSizeX"), getIntProperty("CreateFrameStartSizeY"));
		gamers = new List();
		text = new JLabel("Connected Computers:");
		buttonStart = new JButton("Start");
		buttonStart.addActionListener(this);
		labelServerName = new JLabel("Enter server name:");
		textServerName = new JTextField("Gamer");
		errorMessage = new JLabel("");
		gbLayout.setConstraints(labelServerName, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(2, 2, 2, 2), 0, 0));
		gbLayout.setConstraints(textServerName, new GridBagConstraints(1, 0, 2, 1, 8, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		gbLayout.setConstraints(text, new GridBagConstraints(0, 1, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(2, 2, 2, 2), 0, 0));
		gbLayout.setConstraints(gamers, new GridBagConstraints(0, 2, 3, 1, 1, 3, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		gbLayout.setConstraints(buttonStart, new GridBagConstraints(2, 3, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		gbLayout.setConstraints(errorMessage, new GridBagConstraints(0, 3, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		add(labelServerName);
		add(textServerName);
		add(text);
		add(gamers);
		add(buttonStart);
		add(errorMessage);
		addWindowListener(this);
		setLocation(mainFrame.getFrame().getLocation().x + mainFrame.getFrame().getWidth() / 2 - this.getWidth() / 2, mainFrame.getFrame().getLocation().y + mainFrame.getFrame().getHeight() / 2 - this.getHeight() / 2);
		setVisible(true);
	}
	
	private Properties getProperties(){
		return properties;
	}

	private int getIntProperty(String param){
		return Integer.valueOf(getProperties().getProperty(param));		
	}
	
	public void addConnection(String param){
		gamers.add(param);
	}
	
	private Server getServer(){
		return server;
	}
	
	
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		getServer().closeServerSocket();
		dispose();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void setStartGame(boolean startGame){
		server.startGame(startGame);
	}
	
	private boolean isCorrectName(){
		if(textServerName.getText().equals("")){
		return false;
		} else{
			return true;			
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(isCorrectName()){
			if(gamers.getItemCount() != 0){
				mainFrame.drawGame();
				mainFrame.getGame().setGamerName(0, textServerName.getText());
				mainFrame.getServer().notifyServer();
				setStartGame(true);
			} else{
				getServer().closeServerSocket();
			}
			dispose();
		} else {
			errorMessage.setText(errorEnterName);
		}
	}

}
