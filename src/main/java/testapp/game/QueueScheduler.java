package testapp.game;

import testapp.db.HibernateUtil;
import testapp.db.beans.Player;

import java.util.concurrent.ConcurrentLinkedQueue;

import static testapp.game.PlayerState.READY;

public class QueueScheduler {

    private boolean stopped;
    private static final ConcurrentLinkedQueue<Integer> arenaQueue = new ConcurrentLinkedQueue<>();
    private static int queueSize;

    public QueueScheduler() {
        stopped = false;
        arenaQueue.clear();
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
                queueSize++;
                return true;
            } else {
                return false;
            }
        }
    }

    public static synchronized int getQueueSize() {
        return arenaQueue.size();
    }

    public static boolean removePlayer(Player player) {
        synchronized (arenaQueue) {
            boolean isRemoved = arenaQueue.remove(player.getId());
            if (isRemoved & queueSize > 0) {
                queueSize--;
            }
            return isRemoved;
        }
    }

    private void run() {
        while (!stopped) {
            long startTime = System.currentTimeMillis();
            synchronized (arenaQueue) {
                while (arenaQueue.size() >= 2 && System.currentTimeMillis() - startTime < 400) {
                    Player player1 = HibernateUtil.get(Player.class, arenaQueue.poll());
                    Player player2 = HibernateUtil.get(Player.class, arenaQueue.poll());
                    queueSize = Math.min(queueSize - 2, 0);
                    player1.setState(READY);
                    player1.setEnemy(player2);
                    player2.setState(READY);
                    player2.setEnemy(player1);
                }
                try {
                    wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        synchronized (this) {
            this.stopped = true;
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
