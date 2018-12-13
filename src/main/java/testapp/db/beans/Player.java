package testapp.db.beans;

import testapp.db.DAO;
import testapp.game.PlayerState;

public class Player {

    private int id;
    private int level;
    private int rating;
    private int state;
    private String name;

    public Player() {
    }

    public Player(int id) {
        this.id = id;
        level = 1;
        rating = 1500;
        state = PlayerState.IDLE;
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

    public String getName() {
        if (name == null) {
            name = DAO.getUser(id).getLogin();
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addLevel() {
        level += 1;
    }

    public void addRating(int i) {
        if (rating > 0) {
            rating += i;
        }
    }
}
