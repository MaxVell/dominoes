package domino;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MessagePanel extends JPanel implements MouseListener {
    private String message;
    private JLabel label;
    private JLabel clickToHide;
    private BufferedImage image;
    private int width;
    private int height;
    /**
	 * 
	 */
    private static final long serialVersionUID = -485570186785402913L;

    public MessagePanel() {
        message = "1234567890";
        label = new JLabel(message);
        clickToHide = new JLabel("Click to hide text");
        try {
            image = ImageIO.read(new File("winRound.png"));
        } catch (IOException e) {
            // new HelpDialog(this, "Error", "");
        }
        setSize(1, 1);
        width = 0;
        height = 0;
        add(clickToHide);
        add(label);
        addMouseListener(this);
        setOpaque(false);
        setVisible(true);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPanel((Graphics2D) g);
        drawImage((Graphics2D) g);
        drawText((Graphics2D) g);

    }

    private void drawText(Graphics2D g) {
        FontMetrics fm = g.getFontMetrics();
        label.setLocation((getWidth() - fm.stringWidth(getMessage())) / 2, getImage().getHeight() + fm.getHeight());
        clickToHide.setLocation((getWidth() - fm.stringWidth(clickToHide.getText())) / 2, getImage().getHeight() + 2 * fm.getHeight());
    }

    private void drawPanel(Graphics2D g) {
        Composite originalComposite = g.getComposite();
        g.setColor(Color.WHITE);
        g.setComposite(makeComposite(0.5f));
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics fm = g.getFontMetrics();
        width = (fm.stringWidth(getMessage()) < getImage().getWidth()) ? getImage().getWidth() + 2 * fm.getHeight() : fm.stringWidth(getMessage()) + 2 * fm.getHeight();
        height = width + +2 * fm.getHeight();
        setSize(width, height);
        g.fillRoundRect(0, 0, width, height, 20, 20);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setComposite(originalComposite);
    }

    public int getPanelWidth() {
        return width;
    }

    private AlphaComposite makeComposite(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return (AlphaComposite.getInstance(type, alpha));
    }

    private BufferedImage getImage() {
        return image;
    }

    private void drawImage(Graphics2D g) {
        FontMetrics fm = g.getFontMetrics();
        g.drawImage(getImage(), (getWidth() - getImage().getWidth()) / 2, (getHeight() - getImage().getHeight() - 2 * fm.getHeight()) / 2, this);
    }

    private String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        label.setText(message);

        this.message = message;
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
        setVisible(false);
        // TODO Auto-generated method stub
    }

    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }
}
