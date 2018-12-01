package testapp.db.beans;

public class Player {

    private int id;
    private int level;
    private int rating;

    public Player() {
    }

    public Player(int id) {
        this.id = id;
        this.level = 1;
        this.rating = 1500;
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
}
