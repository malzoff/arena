package testapp.game;

import testapp.db.beans.Player;

public class ArenaPlayer {

    private int id;
    private int maxHp;
    private float currentHp;
    private int damage;

    public ArenaPlayer(Player player) {
        id = player.getId();
        maxHp = player.getHp();
        currentHp = player.getHp();
        damage = player.getDamage();
    }
}
