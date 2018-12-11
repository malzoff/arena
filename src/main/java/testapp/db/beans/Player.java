package testapp.db.beans;

import testapp.db.DAO;
import testapp.game.Arena;
import testapp.game.PlayerState;

public class Player {

    private int id;
    private int level;
    private int rating;
    private int state;
    private int currentArenaId;
    private String name;

    public Player() {
    }

    public Player(int id) {
        this.id = id;
        level = 1;
        rating = 1500;
        state = PlayerState.IDLE;
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

    public int getCurrentArenaId() {
        return currentArenaId;
    }

    public void setCurrentArenaId(int currentArenaId) {
        this.currentArenaId = currentArenaId;
    }

    public String getName() {
        if (name == null) {
            name = DAO.getUser(id).getLogin();
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Arena getCurrentArena() {
        return Arena.get(currentArenaId);
    }
}
