package domino;

import java.util.ArrayList;

public class GameLineView {

    private GameLineGoat gameLine;
    private ArrayList<StoneView> stones;

    public GameLineView(GameLineGoat gameLine) {
        this.gameLine = gameLine;
        this.stones = new ArrayList<StoneView>();
    }

    public ArrayList<StoneView> getStones() {
        return stones;
    }

    public GameLineGoat getGameLine() {
        return gameLine;
    }

    public void setGameLine(GameLineGoat gameLine) {
        this.gameLine = gameLine;
    }

    public StoneView getStoneView(int index) {
        return stones.get(index);
    }

    public StoneView putStone() {
        return stones.remove(0);
    }

    public void addStart(StoneView stoneView) {
        getStones().add(0, stoneView);
        // getGameLine().addStartStone(stoneView.getStone());
    }

    public void addEnd(StoneView stoneView) {
        getStones().add(stoneView);
        // getGameLine().addEndStone(stoneView.getStone());
    }
}
