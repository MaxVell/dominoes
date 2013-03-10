package domino;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonConnectListener implements ActionListener{

private MainFrame mainFrame;
	
	public ButtonConnectListener(MainFrame mainFrame){
		this.mainFrame = mainFrame;
	}
	
	private MainFrame getMainFrame(){
		return this.mainFrame;
	}
	public void actionPerformed(ActionEvent e) {
		new ConnectDialog(mainFrame);
		getMainFrame().createClient();
	}
}
