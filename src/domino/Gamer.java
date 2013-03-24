package domino;

import java.util.ArrayList;
import java.util.HashSet;

public class Gamer {
    private HashSet<Stone> gamerStones;

    public Gamer() {
        this.gamerStones = new HashSet<Stone>();
    }

    public Gamer(HashSet<Stone> gamerStone) {
        this.gamerStones = gamerStone;
    }

    public Gamer(int countStones) {
        gamerStones = new HashSet<Stone>();
        for (int i = 0; i < countStones; i++) {
            this.gamerStones.add(new Stone(i, i));
        }
    }

    public void setGamer(HashSet<Stone> gamerStone) {
        this.gamerStones = gamerStone;
    }

    public void removeStones() {
        gamerStones.clear();
    }

    public void addStone(Stone elem) {
        gamerStones.add(elem);
    }

    public void addStone() {
        gamerStones.add(new Stone(getCountStones() - 1, getCountStones() - 1));
    }

    public void removeStone() {
        gamerStones.remove(gamerStones.add(new Stone(getCountStones() - 1, getCountStones() - 1)));
    }

    public void addStones(ArrayList<Stone> stones) {
        gamerStones.addAll(stones);
    }

    public boolean removeStone(Stone stone) {
        return gamerStones.remove(stone);
    }

    public Stone getStone(int index) {
        Object[] stones = gamerStones.toArray();
        return (Stone) stones[index];
    }

    public int getCountStones() {
        return gamerStones.size();
    }

    public boolean haveStone(Stone elem) {
        // return this.gamerStones.contains(elem);
        Object[] stones = gamerStones.toArray();
        int countStone = getCountStones();
        Stone stone;
        for (int i = 0; i < countStone; i++) {
            stone = (Stone) stones[i];
            if (stone.equals(elem)) return true;
        }
        return false;

    }

    public boolean haveNumber(int number) {
        Object[] stones = gamerStones.toArray();
        int countStone = getCountStones();
        Stone stone;
        for (int i = 0; i < countStone; i++) {
            stone = (Stone) stones[i];
            if (stone.hasNumber(number)) return true;
        }
        return false;
    }
}
