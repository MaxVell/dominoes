package domino;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class StartPanel extends JPanel implements MouseListener, Constants{
	
	private BufferedImage backgroundImage;
	private BufferedImage mainImage;
	private MainFrame mainFrame;
	private Graphics2D g;
	private Properties properties;
	private JButton buttonCreate;
	private JButton buttonConnect;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3353372636143207169L;

	public StartPanel(MainFrame mainFrame){
		this.mainFrame = mainFrame;
		this.properties = new Properties();
		try{
			this.properties.load(new FileInputStream("dominoes.properties"));
			this.backgroundImage = ImageIO.read(new File("fon.jpeg"));
			this.mainImage = ImageIO.read(new File("domino.png"));
		}catch(IOException e){
		//	new HelpDialog(this, "Error", "");
		}
		
		this.add(buttonCreate = new JButton("Create")); 
		buttonCreate.addActionListener(new ButtonCreateListener(mainFrame));
		this.add(buttonConnect = new JButton("Connect"));
		buttonConnect.addActionListener(new ButtonConnectListener(mainFrame));
		addMouseListener(this);
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		setGraphics2D((Graphics2D)g);		
		drawBackground();
		drawMainImage();
		drawButtons();
	}
	
	private BufferedImage getMainImage(){
		return this.mainImage;
	}
	
	private void drawMainImage(){
		getGraphics2D().drawImage(getMainImage(), (getPanelWidth() - getMainImage().getWidth()) / 2, (getPanelHeight() - getMainImage().getHeight()) / 2, this);
	}
	
	private void drawBackground(){
		int frameWidth = getMainFrame().getWidth();
		int frameHeight = getMainFrame().getHeight();
		int imageWidth = getBackgroundImage().getWidth();
		int imageHeight = getBackgroundImage().getHeight();
		for(int i = 0; i < frameWidth; i += imageWidth){
			for(int j = 0; j < frameHeight; j += imageHeight)
				getGraphics2D().drawImage(getBackgroundImage(), i, j, this);
		}
	}
	
	private void drawButtons(){
		this.buttonCreate.setLocation(getPanelWidth() / 2 - this.buttonCreate.getWidth() - 2, (getPanelHeight() + getMainImage().getHeight()) / 2 + this.buttonCreate.getHeight());
		this.buttonConnect.setLocation(getPanelWidth() / 2 + 2, (getPanelHeight() + getMainImage().getHeight()) / 2 + this.buttonConnect.getHeight());
	}
	
	private int getPanelWidth(){
		return getMainFrame().getWidth() - getMainFrame().getInsets().left - getMainFrame().getInsets().right; 
	}
	
	private int getPanelHeight(){
		return getMainFrame().getHeight() - getMainFrame().getInsets().top - getMainFrame().getInsets().bottom - 24;// - getMenuHeight();
	}
	
	private BufferedImage getBackgroundImage(){
		return this.backgroundImage;
	}
	
	private JFrame getMainFrame(){
		return this.mainFrame.getFrame();
	}
	
	private void setGraphics2D(Graphics2D g){
		this.g = g;
	}
	
	private Graphics2D getGraphics2D(){
		return this.g;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
