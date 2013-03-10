package domino;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonCreateListener implements ActionListener{

	private MainFrame mainFrame;
	
	public ButtonCreateListener(MainFrame mainFrame){
		this.mainFrame = mainFrame;
	}
	
	private MainFrame getMainFrame(){
		return this.mainFrame;
	}
	
	public void actionPerformed(ActionEvent e) {
		getMainFrame().createGame(2);
		getMainFrame().createServer();
	 }

}
