package testapp.game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Arena {

    private static ConcurrentMap<Integer, Arena> currentArenas = new ConcurrentHashMap<>();

    private int id;
    private ArenaParticipant ap1;
    private ArenaParticipant ap2;
    private int winnerId = 0;
    private boolean isFinished = false;
    private List<String> battleLog = new ArrayList<>();

    public Arena(ArenaParticipant ap1, ArenaParticipant ap2) {
        this.ap1 = ap1;
        this.ap2 = ap2;
        id = generateId();
    }

    public int getId() {
        return id;
    }

    public ArenaParticipant getAp(int APId) {
        return ap1.getId() == APId ? ap1 : ap2.getId() == APId ? ap2 : null;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    private int generateId() {
        return Integer.valueOf(ap1.getId() + "" + ap2.getId());
    }

    public static Arena get(int arenaId) {
        return currentArenas.get(arenaId);
    }

    public static void add(Arena arena) {
        currentArenas.put(arena.id, arena);
    }

    public static void remove(Arena arena) {
        currentArenas.remove(arena.getId());
    }

    public ArenaParticipant getEnemy(int APId) {
        return ap1.getId() != APId ? ap1 : ap2;
    }

    public List<String> getBattleLog() {
        return battleLog;
    }
}
