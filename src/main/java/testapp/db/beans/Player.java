package testapp.db.beans;

public class Player {

    private int id;
    private int level;

    private int currHp;

    public Player() {
    }

    public Player(int id, int level) {
        this.id = id;
        this.level = level;
        this.currHp = getHp();
    }

    public int getDamage() {
        return 10 + getLevel();
    }

    public int getHp() {
        return 99 + getLevel();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCurrHp() {
        return currHp;
    }

    public void setCurrHp(int currHp) {
        this.currHp = currHp;
    }
}
