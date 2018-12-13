package testapp.db.beans;

import testapp.WicketApplication;
import testapp.db.DAO;
import testapp.game.TimeUtil;

public class ArenaParticipant {

    public static long HIT_COOLDOWN = TimeUtil.SECOND;

    private int id;
    private int maxHp;
    private int currentHp;
    private int damage;
    private int receivedDamage;
    private long nextHitTime;
    private int enemyId;

    public ArenaParticipant() {
    }

    public ArenaParticipant(Player player) {
        id = player.getId();
        maxHp = getMaxHp(player.getLevel());
        currentHp = maxHp;
        damage = player.getLevel() - 1 + 10;
    }

    public void updateStats(int playerLevel) {
        maxHp = getMaxHp(playerLevel);
        currentHp = maxHp;
        damage = getDamage(playerLevel);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaxHp() {
        return maxHp;
    }

    private int getMaxHp(int playerLevel) {
        return playerLevel - 1 + 100;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }

    public int getDamage() {
        return damage;
    }

    private int getDamage(int playerLevel) {
        return playerLevel - 1 + 10;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getReceivedDamage() {
        return receivedDamage;
    }

    public void setReceivedDamage(int receivedDamage) {
        this.receivedDamage = receivedDamage;
    }

    public long getNextHitTime() {
        return nextHitTime;
    }

    public void setNextHitTime(long nextHitTime) {
        this.nextHitTime = nextHitTime;
    }

    public int getEnemyId() {
        return enemyId;
    }

    public void setEnemyId(int enemyId) {
        this.enemyId = enemyId;
    }

    public int getMinDamage() {
        return (int) (damage * 0.75f);
    }

    public int getMaxDamage() {
        return (int) (damage * 1.25f);
    }

    public boolean canHit() {
        return TimeUtil.now() > nextHitTime && currentHp > 0;
    }

    public int doDamage(ArenaParticipant target) {
        int damageAmount = getActualDamage();
        nextHitTime = TimeUtil.now() + HIT_COOLDOWN;
        target.receiveDamage(damageAmount);
        return damageAmount;
    }

    private int getActualDamage() {
        return (int) (getMinDamage() + WicketApplication.RANDOM.nextFloat() * (getMaxDamage() - getMinDamage()));
    }

    private void receiveDamage(int damageAmount) {
        currentHp -= damageAmount;
        setReceivedDamage(damageAmount);
    }

    public String getName() {
        return DAO.getPlayer(id).getName();
    }
}
