package domino;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

public class ConnectDialog extends Dialog implements ActionListener, WindowListener, Constants{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1866491754710339991L;
	private JFormattedTextField ipTextField;
	private JLabel text;
	private JLabel errorMessage;
	private JButton ok;
	private MainFrame mainFrame;
	private InetAddress ipAddres;
	private JLabel labelServerName;
	private JTextField textGamerName;
	private Properties properties;

	public ConnectDialog(MainFrame mainFrame){
		super(mainFrame.getFrame(), "", true);
		properties = new Properties();
		try{
			properties.load(new FileInputStream("dominoes.properties"));
		}catch(IOException e){
		//	new HelpDialog(this, "Error", "");
		}
		setMainFrame(mainFrame);
		 try {
			MaskFormatter mf = new MaskFormatter("###.###.###.###");
			mf.setPlaceholderCharacter('0');
			ipTextField = new JFormattedTextField(mf);
			ipTextField.setText("127.000.000.001");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 GridBagLayout gbLayout = new GridBagLayout();
			setLayout(gbLayout);
			text = new JLabel("Server ip address:");
			errorMessage = new JLabel("");
			ok = new JButton("Ok");
			labelServerName = new JLabel("Enter your name:");
			textGamerName = new JTextField("Gamer");
			gbLayout.setConstraints(labelServerName, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(2, 2, 2, 2), 0, 0));
			gbLayout.setConstraints(textGamerName, new GridBagConstraints(1, 0, 2, 1, 8, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			gbLayout.setConstraints(text, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(2, 2, 2, 2), 0, 0));
			gbLayout.setConstraints(ipTextField, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
			gbLayout.setConstraints(errorMessage, new GridBagConstraints(0, 2, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			gbLayout.setConstraints(ok, new GridBagConstraints(1, 3, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
			ok.addActionListener(this);
			add(labelServerName);
			add(textGamerName);
			add(text);
			add(ipTextField);
			add(errorMessage);
			add(ok);
			
		 addWindowListener(this);
		 setMinimumSize(new Dimension(getIntProperty("ConnectFrameMinimumSizeX"), getIntProperty("ConnectFrameMinimumSizeY")));
		 setSize(getIntProperty("ConnectFrameStartSizeX"), getIntProperty("ConnectFrameStartSizeY"));
		 setLocation(mainFrame.getFrame().getLocation().x + mainFrame.getFrame().getWidth() / 2 - getWidth() / 2, mainFrame.getFrame().getLocation().y + mainFrame.getFrame().getHeight() / 2 - getHeight() / 2);
		 
		 setVisible(true);
	}
	
	private Properties getProperties(){
		return properties;
	}

	private int getIntProperty(String param){
		return Integer.valueOf(getProperties().getProperty(param));		
	}
	
	private void setIPAddress(InetAddress ipAddress){
		this.ipAddres = ipAddress;
	}
	
	private InetAddress getIPAddress(){
		return ipAddres;
	}
	
	private boolean isCorrectName(){
		if(textGamerName.getText().equals("")){
		return false;
		} else{
			return true;			
		}
	}
	
	private boolean isCorrectIP(String ipAddress){
		for(int i = 0; i < 4; i++){
		int num = Integer.valueOf(ipAddress.substring(0 + 4 * i, 3 + 4 * i));
		if((num < 0) || (num > 255))
			return false;
		}
		return true;
	}
	
	private void setMainFrame(MainFrame mainFrame){
		this.mainFrame = mainFrame;
	}
	
	private MainFrame getMainFrame(){
		return mainFrame;
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
	//	this.dispose();
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		getMainFrame().setCreateClient(false);
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

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(isCorrectName() && !isCorrectIP(ipTextField.getText())){
			errorMessage.setText(errorIP);
		}
		if(!isCorrectName() && isCorrectIP(ipTextField.getText())){
			errorMessage.setText(errorEnterName);
		}
		if(!isCorrectName() && !isCorrectIP(ipTextField.getText())){
			errorMessage.setText(errorIP + " and " + errorEnterName);
		}
		if(isCorrectIP(ipTextField.getText()) && isCorrectName()){
			try {
				setIPAddress(InetAddress.getByName(ipTextField.getText()));
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			getMainFrame().setIPAddress(getIPAddress());
			getMainFrame().setCreateClient(true);
			getMainFrame().setClientName(textGamerName.getText());
			dispose();
		}
	}
}
