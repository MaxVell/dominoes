package domino;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class DrawComponents {
	private int stoneWidth;
	private Properties properties;
	private BufferedImage backgroundImage;
	private JPanel jpanel;
	private int rowUp;
	private int rowDown;
	private static String gamerScores = "Game scores:";
	private String fontType = "SansSerif";
	
	
	public DrawComponents(JPanel jpanel){
		this.jpanel = jpanel;
		this.properties = new Properties();
		try{
			this.properties.load(new FileInputStream("dominoes.properties"));
			this.backgroundImage = ImageIO.read(new File("fon.jpeg"));
		}catch(IOException e){
		//	new HelpDialog(this, "Error", "");
		}
	}
	
	private Properties getProperties(){
		return this.properties;
	}
	
	private int getIntProperty(String param){
		return Integer.valueOf(getProperties().getProperty(param));		
	}
	
	public void drawGame(Graphics2D g, GamerView[] gamers, MarketView market, GameLineView gameLine, StoneView[] stoneRect, String[] names, int[] scores, String message){
		setStoneWidth();
		drawBackground(g);
		drawGamersPanel(g, gamers);
		drawMarket(g, market.getStones());
		drawGameLine(gameLine);
		drawStoneRect(stoneRect, gameLine.getStones());
		drawMessage(g, message, gamers.length);
		drawScore(g, names, scores, gamers.length);
	}
	
	private int getRowUp(){
		return this.rowUp;
	}
	
	private int getRowDown(){
		return this.rowDown;
	}
	
	private void addRowUp(){
		this.rowUp++;
	}
	
	private void addRowDown(){
		this.rowDown++;
	}
	
	private void setRowUp(int rowUp){
		this.rowUp = rowUp;
	}
	
	private void setRowDown(int rowDown){
		this.rowDown = rowDown;
	}
	
	private int getGameLinePanelX(){
		return getGamerPanelHeight();
	}
	
	private void drawGameLine(GameLineView gameLineView){
		boolean isLeft = true;
		setRowUp(0);
		setRowDown(0);
		ArrayList<StoneView> gameLine = gameLineView.getStones();
		int countStones = gameLine.size();
		int posx = (getPanel().getWidth() + getStoneWidth()) / 2;
		int posy = (getPanel().getHeight() - 2 * getStoneWidth()) / 2;
		int numberStart = gameLineView.getGameLine().getStartCount();
		if((countStones != 0) && (numberStart != -1)){
			gameLine.get(numberStart).setWidth(getStoneWidth());
			gameLine.get(numberStart).setIsOpen(true);
			if(gameLine.get(numberStart).getStone().isDoubles()){
				gameLine.get(numberStart).setIsVertical(true);
			} else{
				gameLine.get(numberStart).setIsVertical(false);
			}
			gameLine.get(numberStart).setLocation(posx - gameLine.get(numberStart).getWidth() / 2, posy - gameLine.get(numberStart).getHeight() / 2);
			for(int i = numberStart - 1; i >= 0; i--){
				gameLine.get(i).setWidth(getStoneWidth());
				gameLine.get(i).setIsOpen(true);
				if( isLeft != nextStoneGameLineStart(gameLine.get(i + 1), gameLine.get(i), isLeft)){
					isLeft = !isLeft;
					addRowUp();
				}
			}
			isLeft = false;
			for(int i = numberStart + 1; i < countStones; i++){
				gameLine.get(i).setIsOpen(true);
				gameLine.get(i).setWidth(getStoneWidth());
				if(isLeft != nextStoneGameLineEnd(gameLine.get(i - 1), gameLine.get(i), isLeft)){
					isLeft = !isLeft;
					addRowDown();
				}
			}
		}
	}
	
	private boolean nextStoneGameLineStart(StoneView preview, StoneView next, boolean isLeft){
		boolean up = getRowUp() % 2 != 0;
		boolean isTranfered = (!up)?(next.getStone().getFinish() != preview.getStartNumber(isLeft)):(next.getStone().getStart() != preview.getStartNumber(isLeft));
		int delta = 0;
		if(isLeft){
			if(preview.getLocation().x - 2 * next.getStoneWidth() > getGameLinePanelX()){
				if((preview.getLocation().x + preview.getWidth() + 2 * next.getStoneWidth() > getPanel().getWidth() - getGameLinePanelX()) && (preview.getIsVertical())){
					next.setIsVertical(false);
					delta = preview.getStoneWidth() / 2;
				} else{
					next.setVertical();
				}
				if(isTranfered)
					next.setTheta(next.getTheta() + Math.PI);
				next.setLocation(preview.getLocation().x - next.getWidth(), preview.getLocation().y + preview.getHeight() / 2 - next.getHeight() / 2 - delta);
				return true;
			} else{
				next.setIsVertical(true);
				//if(isTranfered)
				if(next.getStone().getFinish() != preview.getStartNumber(isLeft))
					next.setTheta(next.getTheta() + Math.PI);
				next.setLocation(preview.getLocation().x, preview.getLocation().y - next.getHeight());
				return false;
			}
		} else{
			if(preview.getLocation().x + preview.getWidth() + 2 * next.getStoneWidth() < getPanel().getWidth() - getGameLinePanelX()){
				if((preview.getLocation().x - 2 * next.getStoneWidth() < getGameLinePanelX()) && preview.getIsVertical()){
					next.setIsVertical(false);
					delta = preview.getStoneWidth() / 2; 
				} else{
					next.setVertical();
				}
				if(isTranfered)
					next.setTheta(next.getTheta() + Math.PI);
				next.setLocation(preview.getLocation().x + preview.getWidth(), preview.getLocation().y + preview.getHeight() / 2 - next.getHeight() / 2 - delta);
				return false;
			} else{
				next.setIsVertical(true);
				//if(isTranfered)
				if(next.getStone().getFinish() != preview.getStartNumber(isLeft))
					next.setTheta(next.getTheta() + Math.PI);
				next.setLocation(preview.getLocation().x + preview.getWidth() - next.getWidth(), preview.getLocation().y - next.getHeight());
				return true;
			}
		}
	}
	
	private boolean nextStoneGameLineEnd(StoneView preview, StoneView next, boolean isLeft){
		boolean down = getRowDown() % 2 != 0;
	//	boolean isTranfered = (down)?(next.getStone().getFinish() != preview.getEndNumber(isLeft)):(next.getStone().getStart() != preview.getEndNumber(isLeft));
		boolean isTranfered = (down)?(next.getStone().getFinish() != preview.getEndNumber(isLeft)):(next.getStone().getStart() != preview.getEndNumber(isLeft));
		int delta = 0;
		if(isLeft){
			if(preview.getLocation().x - 2 * next.getStoneWidth() > getGameLinePanelX()){
				if((preview.getLocation().x + preview.getWidth() + 2 * next.getStoneWidth() > getPanel().getWidth() - getGameLinePanelX()) && (preview.getIsVertical())){
					next.setIsVertical(false);
					delta = preview.getStoneWidth() / 2;
				} else{
					next.setVertical();
				}
				if(isTranfered)
					next.setTheta(next.getTheta() + Math.PI);
				next.setLocation(preview.getLocation().x - next.getWidth(), preview.getLocation().y + preview.getHeight() / 2 - next.getHeight() / 2 + delta);
				return true;
			} else{
				next.setIsVertical(true);
			//	if(!isTranfered)
				if(next.getStone().getStart() != preview.getEndNumber(isLeft))
					next.setTheta(next.getTheta() + Math.PI);
				next.setLocation(preview.getLocation().x, preview.getLocation().y + preview.getHeight());
				return false;
			}
		} else{
			if(preview.getLocation().x + preview.getWidth() + 2 * next.getStoneWidth() < getPanel().getWidth() - getGameLinePanelX()){
				if((preview.getLocation().x - 2 * next.getStoneWidth() < getGameLinePanelX()) && preview.getIsVertical()){
					next.setIsVertical(false);
					delta = preview.getStoneWidth() / 2; 
				} else{
					next.setVertical();
				}
				if(isTranfered)
					next.setTheta(next.getTheta() + Math.PI);
				next.setLocation(preview.getLocation().x + preview.getWidth(), preview.getLocation().y + preview.getHeight() / 2 - next.getHeight() / 2 + delta);
				return false;
			} else{
				next.setIsVertical(true);
				//if(!isTranfered)
				if(next.getStone().getStart() != preview.getEndNumber(isLeft))
					next.setTheta(next.getTheta() + Math.PI);
				next.setLocation(preview.getLocation().x + preview.getWidth() - next.getWidth(), preview.getLocation().y + preview.getHeight());
				return true;
			}
		}
	}
	
	private void drawStoneRect(StoneView[] stoneRect, ArrayList<StoneView> gameLine){
		int posx = (getPanel().getWidth() + getStoneWidth()) / 2;
		int posy = (getPanel().getHeight() - 2 * getStoneWidth()) / 2;
		stoneRect[0].setWidth(getStoneWidth());
		stoneRect[1].setWidth(getStoneWidth());
		if(gameLine.isEmpty()){
			stoneRect[0].setIsVertical(true);
			stoneRect[0].setLocation(posx - stoneRect[0].getWidth() / 2, posy - stoneRect[0].getHeight() / 2);
		} else{
			nextStoneGameLineStart(gameLine.get(0), stoneRect[0], getRowUp() % 2 == 0);
			nextStoneGameLineEnd(gameLine.get(gameLine.size() - 1), stoneRect[1], getRowDown() % 2 != 0);
		}
	}
	
	public int getStoneWidth(){
		return this.stoneWidth;
	}
	
	public void setStoneWidth(int stoneWidth){
		this.stoneWidth = stoneWidth;
	}
	
	private void drawBackground(Graphics2D g){
		int frameWidth = getPanel().getWidth();
		int frameHeight = getPanel().getHeight();
		int imageWidth = backgroundImage.getWidth();
		int imageHeight = backgroundImage.getHeight();
		for(int i = 0; i < frameWidth; i += imageWidth){
			for(int j = 0; j < frameHeight; j += imageHeight)
				g.drawImage(backgroundImage, i, j, getPanel());
		}
	}
	
	private JPanel getPanel(){
		return this.jpanel;
	}
	
	private void setStoneWidth(){
		int horWidth = (getPanel().getWidth() - getGamerImageWidth() - 6 * getIntProperty("insetsGamer") - 2 * getIntProperty("insetsStone")) / 19;
		int verWidth = (getPanel().getHeight() - getGamerImageWidth() - 6 * getIntProperty("insetsGamer") - 2 * getIntProperty("insetsStone")) / 19;
		this.stoneWidth = (horWidth < verWidth)?horWidth:verWidth;
		setStoneWidth(this.stoneWidth);
	}
	
	private int getGamerPanelHeight(){
		return 4 * getStoneWidth() + getIntProperty("insetsGamer") + getIntProperty("insetsStone");
	}
	
	private int getGamerPanelWidth(){
		return getStonePanelWidth() + getGamerImageWidth(); 
	}
	
	private int getStonePanelWidth(){
		return getStoneWidth() * getIntProperty("gamerMaxStoneRow") + getIntProperty("insetsGamer");  
	}
	
	private int getGamerImageWidth(){
		return 0;
	}
	
	private void drawMarket(Graphics2D g, ArrayList<StoneView> market){
		int countStones = market.size();
		int posx = (getPanel().getWidth() - getGamerPanelHeight());
		int posy = (getPanel().getHeight() - (countStones / 2) * getStoneWidth()) / 2;
		for(int i = 0; i < countStones; i++){
			market.get(i).setIsVertical(false);
			market.get(i).setIsOpen(false);
			market.get(i).setTheta(Math.PI / 2);
			market.get(i).setWidth(getStoneWidth());
			if(i < countStones / 2){
				market.get(i).setLocation(posx, posy  + i * getStoneWidth());
			} else{
				market.get(i).setLocation(posx + 2 * getStoneWidth() + getIntProperty("insetsStone"), posy  + (i - countStones / 2) * getStoneWidth());
			}
		}
	}
	
	private void drawGamersPanel(Graphics2D g, GamerView[] gamers){
		int countGamers = gamers.length;
		for(int i = 0; i < countGamers; i++){
			drawGamerPanel(g, i, gamers[i].getStones());
		}
	}
	
	private void drawGamerPanel(Graphics2D g, int numberGamer, ArrayList<StoneView> stones){
		int posx;
		int posy;
		switch(numberGamer){
		case 0:
			posx = (getPanel().getWidth() - getGamerPanelWidth()) / 2;
			posy = getPanel().getHeight() - getGamerPanelHeight();
			drawGamerField(g, posx, posy, getGamerPanelWidth(), getGamerPanelHeight());
			drawGamerStone(g, posx + getIntProperty("insetsGamer"), posy + getIntProperty("insetsGamer"), stones, true, true);
			break;
		case 1:
			posx = 0;//getGamerPanelHeight();
			posy = (getPanel().getHeight() - getGamerPanelWidth()) / 2;
			drawGamerField(g, posx, posy, getGamerPanelHeight(), getGamerPanelWidth());
			drawGamerStone(g, posx + getIntProperty("insetsGamer"), posy + getIntProperty("insetsGamer"), stones, false, false);
			break;
		case 2:
			posx = (getPanel().getWidth() - getGamerPanelWidth()) / 2;
			posy = 0;			
			drawGamerField(g, posx, posy, getGamerPanelWidth(), getGamerPanelHeight());
			drawGamerStone(g, posx + getIntProperty("insetsGamer"), posy + getIntProperty("insetsGamer"), stones, true, false);
			break;
		case 3:
			posx = (getPanel().getWidth() - getGamerPanelHeight());
			posy = (getPanel().getHeight() - getGamerPanelWidth()) / 2;
			drawGamerField(g, posx, posy, getGamerPanelHeight(), getGamerPanelWidth());
			drawGamerStone(g, posx + getIntProperty("insetsGamer"), posy + getIntProperty("insetsGamer"), stones, false, false);
			break;
		}
	}
	
	private void drawGamerField(Graphics2D g, int x, int y, int width, int height){
		Composite originalComposite = g.getComposite();
		g.setColor(Color.WHITE);
		g.setComposite(makeComposite(0.5f));
		g.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		g.fillRoundRect(x, y, width, height, 20, 20);
		g.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF );
		g.setComposite(originalComposite);
	}
	
	private AlphaComposite makeComposite(float alpha) {
		  int type = AlphaComposite.SRC_OVER;
		  return(AlphaComposite.getInstance(type, alpha));
		 }
	
	private void drawGamerStone(Graphics2D g, int posx, int posy, ArrayList<StoneView> stones, boolean isVertical, boolean isOpen){
		if(!isVertical){
			int countStones = stones.size();
			for(int i = 0; i < countStones; i++){
				if(!stones.get(i).isPress()){
					stones.get(i).setIsOpen(isOpen);
					stones.get(i).setWidth(getStoneWidth());
					stones.get(i).setIsVertical(isVertical);
					if(i < getIntProperty("gamerMaxStoneRow")){
						stones.get(i).setLocation(posx, posy  + i * getStoneWidth());
					} else{
						stones.get(i).setLocation(posx + 2 * getStoneWidth() + getIntProperty("insetsStone"), posy  + (i - getIntProperty("gamerMaxStoneRow")) * getStoneWidth());
					}
				}
			}
		} else{
			int countStones = stones.size();
			for(int i = 0; i < countStones; i++){
				if(!stones.get(i).isPress()){
					stones.get(i).setIsOpen(isOpen);
					stones.get(i).setWidth(getStoneWidth());
					stones.get(i).setIsVertical(isVertical);
					if(i < getIntProperty("gamerMaxStoneRow")){
						stones.get(i).setLocation(posx + i * getStoneWidth(), posy);
					} else{
						stones.get(i).setLocation(posx + (i - getIntProperty("gamerMaxStoneRow")) * getStoneWidth(), posy + 2 * getStoneWidth() + getIntProperty("insetsStone"));
					}
				}
			}
		}
	}
	
	private void drawMessage(Graphics2D g, String message, int countGamers){
		Composite originalComposite = g.getComposite();
		FontMetrics fm = g.getFontMetrics();
		int width =  4 * getStoneWidth() + 2 * getIntProperty("insetsGamer") + getIntProperty("insetsStone");
		int fontSize = ((width - fm.getLeading()) / (8));
		g.setFont(new Font(fontType, Font.BOLD, fontSize));
		g.setComposite(makeComposite(0.2f));
		g.setColor(Color.WHITE);
		g.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		int posx = 0;
		int posy = getPanel().getHeight() - getGamerPanelHeight();
		g.fillRoundRect(posx, posy, getGamerPanelHeight(), getGamerPanelHeight(), getIntProperty("insetsString"), getIntProperty("insetsString"));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF );
		g.setComposite(originalComposite);
		g.setColor(Color.BLACK);
		fm = g.getFontMetrics();
		String[] words = message.split(" ");
		ArrayList<String> rows = new ArrayList<String>();
		String row = "";
		int countWords = words.length;
		for(int i = 0; i < countWords; i++)
		if(fm.stringWidth(row + " " + words[i]) > getGamerPanelHeight()){
			rows.add(row);
			row = words[i];
		} else{
			row += " " + words[i]; 
		}
		rows.add(row);
		int countRows = rows.size();
		int deltaY = getGamerPanelHeight() / (countRows + 1);
		posy += deltaY;
		for(int i = 0; i < countRows; i++){
			posx = (getGamerPanelHeight() - fm.stringWidth(rows.get(i))) / 2;
			g.drawString(rows.get(i) , posx, posy + i * deltaY);
		}
	}
		
	private void drawScore(Graphics2D g, String[] names, int[] scores, int countGamers){
		Composite originalComposite = g.getComposite();
		FontMetrics fm = g.getFontMetrics();
		int width =  4 * getStoneWidth() + 2 * getIntProperty("insetsGamer") + getIntProperty("insetsStone");
		int fontSize = ((width - fm.getLeading()) / (countGamers + 1));
		do{
			g.setFont(new Font(fontType, Font.BOLD, fontSize--));
			fm = g.getFontMetrics();
		}while(fm.stringWidth(gamerScores) > (width - 2 * getIntProperty("insetsString")));
		
		g.setComposite(makeComposite(0.2f));
		g.setColor(Color.WHITE);
		g.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

		int posx = getPanel().getWidth() - getGamerPanelHeight();
		int posy = 0;
		
		g.fillRoundRect(posx, posy, 4 * getStoneWidth() + 2 * getIntProperty("insetsGamer") + getIntProperty("insetsStone"), 4 * getStoneWidth() + 2 * getIntProperty("insetsGamer") + getIntProperty("insetsStone"), getIntProperty("insetsString"), getIntProperty("insetsString"));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF );
		g.setComposite(originalComposite);
		g.setColor(Color.BLACK);
		
		fm = g.getFontMetrics();
		posx += getIntProperty("insetsString");
		posy += getIntProperty("insetsString") + fm.getAscent();
		g.drawString(gamerScores, posx, posy);
		for(int i = 0; i < countGamers; i++){
			posy += fm.getHeight();
			g.drawString(changeNameGamer(g, names[i], 3 * getStoneWidth() + getIntProperty("insetsGamer") + getIntProperty("insetsStone")) , posx, posy);
			g.drawString(": " + scores[i], getPanel().getWidth() - getStoneWidth() - getIntProperty("insetsGamer") , posy);
		}
		
	}
	
	private String changeNameGamer(Graphics2D g, String nameGamer, int length){
		FontMetrics fm = g.getFontMetrics();
		int count = 0;
		while(fm.stringWidth(nameGamer) > length){
			nameGamer = nameGamer.substring(0, nameGamer.length() - 1);
			count++;
		}
		if(count > 0)
			nameGamer = nameGamer.substring(0, nameGamer.length() - 3) + "...";
		return nameGamer;
	}
}
