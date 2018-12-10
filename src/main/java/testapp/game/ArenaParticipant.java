package testapp.game;

import testapp.WicketApplication;
import testapp.db.beans.Player;

public class ArenaParticipant {

    public static long HIT_COOLDOWN = TimeUtil.SECOND;
    private int id;
    private int maxHp;
    private float currentHp;
    private int damage;
    private long nextHitTime;

    public ArenaParticipant(Player player) {
        id = player.getId();
        maxHp = player.getHp();
        currentHp = player.getHp();
        damage = player.getDamage();
    }

    public int getId() {
        return id;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public float getCurrentHp() {
        return currentHp;
    }

    public long getNextHitTime() {
        return nextHitTime;
    }

    public int getMinDamage() {
        return (int) (damage * 0.75f);
    }

    public int getMaxDamage() {
        return (int) (damage * 1.25f);
    }

    public int doDamage(ArenaParticipant target) {
        int damageAmount = (int) (0.75f * damage + WicketApplication.RANDOM.nextFloat() * 0.5f);
        target.receiveDamage(damageAmount);
        nextHitTime += nextHitTime == 0 ? TimeUtil.now() + HIT_COOLDOWN : HIT_COOLDOWN;
        return damageAmount;
    }

    private void receiveDamage(int damageAmount) {
        currentHp -= damageAmount;
    }




}
