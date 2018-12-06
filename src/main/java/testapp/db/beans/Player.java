package testapp.db.beans;

import testapp.game.PlayerState;

public class Player {

    private int id;
    private int level;
    private int rating;
    private int state;
    private Player enemy;

    public Player() {
    }

    public Player(int id) {
        this.id = id;
        level = 1;
        rating = 1500;
        state = PlayerState.IDLE;
        enemy = null;
    }

    public int getDamage() {
        return 9 + getLevel();
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

    public int getRating() {
        if (rating < 0) {
            rating = 0;
        }
        return rating;
    }

    public void setRating(int rating) {
        this.rating = Math.max(0, rating);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Player getEnemy() {
        return enemy;
    }

    public void setEnemy(Player enemy) {
        this.enemy = enemy;
    }
}
