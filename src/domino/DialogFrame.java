package domino;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;

public class DialogFrame extends Dialog implements ActionListener, WindowListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private JButton buttonOk;
    private Properties properties;

    DialogFrame(JFrame mainFrame, String text) {
        super(mainFrame, text, true);
        properties = new Properties();
        try {
            properties.load(new FileInputStream("dominoes.properties"));
        } catch (IOException e) {
            // new HelpDialog(mainFrame, "Error", errorOpenConfig);
        }
        setLayout(new GridBagLayout());
        setMinimumSize(new Dimension(getIntProperty("DialogFrameMinimumSizeX"), getIntProperty("DialogFrameMinimumSizeY")));
        setSize(getIntProperty("DialogFrameStartSizeX"), getIntProperty("DialogFrameStartSizeY"));
        setLocation(mainFrame.getLocation().x + mainFrame.getWidth() / 2 - getWidth() / 2, mainFrame.getLocation().y + mainFrame.getHeight() / 2 - getHeight() / 2);
        add(new Label(text));
        add(buttonOk = new JButton("Ok"));
        buttonOk.addActionListener(this);
        addWindowListener(this);
        setVisible(true);

    }

    private Properties getProperties() {
        return properties;
    }

    private int getIntProperty(String param) {
        return Integer.valueOf(getProperties().getProperty(param));
    }

    public void windowActivated(WindowEvent arg0) {
        // TODO Auto-generated method stub
    }

    public void windowClosed(WindowEvent arg0) {
        // TODO Auto-generated method stub
        dispose();
    }

    public void windowClosing(WindowEvent arg0) {
        // TODO Auto-generated method stub
        dispose();
    }

    public void windowDeactivated(WindowEvent arg0) {
        // TODO Auto-generated method stub
    }

    public void windowDeiconified(WindowEvent arg0) {
        // TODO Auto-generated method stub
    }

    public void windowIconified(WindowEvent arg0) {
        // TODO Auto-generated method stub
    }

    public void windowOpened(WindowEvent arg0) {
        // TODO Auto-generated method stub
    }

    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        dispose();
    }

}
