package testapp.game;

import testapp.db.beans.Player;

import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueScheduler {

    private boolean stopped;
    private static final ConcurrentLinkedQueue<Integer> arenaQueue = new ConcurrentLinkedQueue<>();

    public QueueScheduler() {
        stopped = false;
        new Thread(this::run);
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public static boolean addPlayer(Player player) {
        synchronized (arenaQueue) {
            if (arenaQueue.contains(player.getId()) || arenaQueue.add(player.getId())) {
                    player.setState(PlayerState.IN_QUEUE);
                    return true;
                } else {
                    return false;
                }
        }
    }

    private static boolean removePlayer(Player player) {
        synchronized (arenaQueue) {
            player.setState(PlayerState.IDLE);
            return arenaQueue.remove(player.getId());
        }
    }

    private void run() {
        while (!stopped) {
            long startTime = System.currentTimeMillis();
            synchronized (arenaQueue) {
                while (System.currentTimeMillis() - startTime < TimeUtil.SECOND / 10 && arenaQueue.size() >= 2) {
                    int player1Id = arenaQueue.poll();
                    int player2Id = arenaQueue.poll();
                }
                try {
                    wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
