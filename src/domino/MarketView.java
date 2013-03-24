package domino;

import java.util.ArrayList;
import java.util.Random;

public class MarketView {

    private Bazar market;
    private ArrayList<StoneView> stones;
    private DrawGame dGame;
    private DrawClient dClient;
    private Random rand;

    public MarketView(Bazar market) {
        this.market = market;
        this.stones = new ArrayList<StoneView>();
        rand = new Random(System.currentTimeMillis());
    }

    public MarketView(DrawClient dClient) {
        this.market = new Bazar();
        this.stones = new ArrayList<StoneView>();
        this.dClient = dClient;
        rand = new Random(System.currentTimeMillis());
    }

    public MarketView(Bazar market, ArrayList<StoneView> stones, DrawGame dGame) {
        rand = new Random(System.currentTimeMillis());
        this.market = market;
        this.stones = stones;
        this.dGame = dGame;
        for (int i = 0; i < 28; i++) {
            stones.get(i).setStone(market.getStone(i));
        }
        setMouseListener();

    }

    public MarketView(Bazar market, ArrayList<StoneView> stones, DrawClient dClient) {
        rand = new Random(System.currentTimeMillis());
        this.market = market;
        this.stones = stones;
        this.dClient = dClient;
        setMouseListener();
    }

    private DrawGame getDrawGame() {
        return dGame;
    }

    private DrawClient getDrawClient() {
        return dClient;
    }

    private void setMouseListener() {
        int countStones = getStones().size();
        for (int i = 0; i < countStones; i++) {
            if (getDrawGame() != null)
                getStones().get(i).addMouseListener(new MarketMouseListener(getStones().get(i), getDrawGame()));
            else
                getStones().get(i).addMouseListener(new MarketMouseListener(getStones().get(i), getDrawClient()));
        }
    }

    public StoneView putStoneView(StoneView stoneView) {
        StoneView stone = stoneView;
        stone.removeMouseListener(stone.getMouseListeners()[0]);
        getStones().remove(stoneView);
        return stone;
    }

    public StoneView putStoneView(Stone stone) {
        StoneView stoneView = getStone(stone);
        stoneView.removeMouseListener(stoneView.getMouseListeners()[0]);
        getStones().remove(stoneView);
        return stoneView;
    }

    public StoneView putStone(int index) {
        StoneView stoneView = getStones().remove(index);
        stoneView.removeMouseListener(stoneView.getMouseListeners()[0]);
        return stoneView;
    }

    public StoneView putRandomStoneView() {
        int numberStone = getRandInt(getStones().size());
        StoneView stoneView = getStones().remove(numberStone);
        stoneView.removeMouseListener(stoneView.getMouseListeners()[0]);
        return stoneView;
    }

    private int getRandInt(int maxInt) {
        return rand.nextInt(maxInt);
    }

    private Bazar getMarket() {
        return market;
    }

    public ArrayList<StoneView> getStones() {
        return stones;
    }

    public void addStone(StoneView stoneView) {
        getStones().add(stoneView);
        if (getDrawGame() != null) {
            stoneView.addMouseListener(new MarketMouseListener(stoneView, getDrawGame()));
            getMarket().addStone(stoneView.getStone());
        } else {
            stoneView.setStone(new Stone(getStones().size(), getStones().size()));
            getMarket().addStone(stoneView.getStone());
            stoneView.addMouseListener(new MarketMouseListener(stoneView, getDrawClient()));
        }
    }

    public StoneView getStone(Stone stone) {
        int countStones = getStones().size();
        for (int i = 0; i < countStones; i++) {
            if (getStones().get(i).getStone().equals(stone)) return getStones().get(i);
        }
        return null;
    }

    public void removeStone(StoneView stoneView) {
        getStones().remove(stoneView);
        getMarket().removeStone(stoneView.getStone());
    }
}
