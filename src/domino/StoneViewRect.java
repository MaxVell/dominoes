package domino;

import java.awt.Graphics;

public class StoneViewRect extends StoneView {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7731549707752922081L;
    private boolean isVertical;
    private int width = 30;

    public StoneViewRect() {
        setSize(30, 30);
        setVisible(true);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        getGraphics().fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
    }

    public void setWidth(int width) {
        this.width = width;
        if (getIsVertical()) {
            setSize(width, 2 * width);
        } else {
            setSize(2 * width, width);
        }
    }

    public int getWidth() {
        return width;
    }

    public void setVertical() {
        isVertical = false;
    }

    public void setIsVertical(boolean isVertical) {
        this.isVertical = isVertical;
    }

    public boolean getIsVertical() {
        return isVertical;
    }
}
