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

public class StartPanel extends JPanel implements MouseListener, Constants {

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

    public StartPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        properties = new Properties();
        try {
            properties.load(new FileInputStream("dominoes.properties"));
            backgroundImage = ImageIO.read(new File("fon.jpeg"));
            mainImage = ImageIO.read(new File("domino.png"));
        } catch (IOException e) {
            // new HelpDialog(this, "Error", "");
        }

        add(buttonCreate = new JButton("Create"));
        buttonCreate.addActionListener(new ButtonCreateListener(mainFrame));
        add(buttonConnect = new JButton("Connect"));
        buttonConnect.addActionListener(new ButtonConnectListener(mainFrame));
        addMouseListener(this);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setGraphics2D((Graphics2D) g);
        drawBackground();
        drawMainImage();
        drawButtons();
    }

    private BufferedImage getMainImage() {
        return mainImage;
    }

    private void drawMainImage() {
        getGraphics2D().drawImage(getMainImage(), (getPanelWidth() - getMainImage().getWidth()) / 2, (getPanelHeight() - getMainImage().getHeight()) / 2, this);
    }

    private void drawBackground() {
        int frameWidth = getMainFrame().getWidth();
        int frameHeight = getMainFrame().getHeight();
        int imageWidth = getBackgroundImage().getWidth();
        int imageHeight = getBackgroundImage().getHeight();
        for (int i = 0; i < frameWidth; i += imageWidth) {
            for (int j = 0; j < frameHeight; j += imageHeight)
                getGraphics2D().drawImage(getBackgroundImage(), i, j, this);
        }
    }

    private void drawButtons() {
        buttonCreate.setLocation(getPanelWidth() / 2 - buttonCreate.getWidth() - 2, (getPanelHeight() + getMainImage().getHeight()) / 2 + buttonCreate.getHeight());
        buttonConnect.setLocation(getPanelWidth() / 2 + 2, (getPanelHeight() + getMainImage().getHeight()) / 2 + buttonConnect.getHeight());
    }

    private int getPanelWidth() {
        return getMainFrame().getWidth() - getMainFrame().getInsets().left - getMainFrame().getInsets().right;
    }

    private int getPanelHeight() {
        return getMainFrame().getHeight() - getMainFrame().getInsets().top - getMainFrame().getInsets().bottom - 24;// -
                                                                                                                    // getMenuHeight();
    }

    private BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    private JFrame getMainFrame() {
        return mainFrame.getFrame();
    }

    private void setGraphics2D(Graphics2D g) {
        this.g = g;
    }

    private Graphics2D getGraphics2D() {
        return g;
    }

    public void mouseClicked(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

}
