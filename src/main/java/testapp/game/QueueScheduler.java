package testapp.game;

import testapp.db.DAO;
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
        queueSize = 0;
        new Thread(this::run).start();
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public static boolean addPlayer(Player player) {
        boolean inQueue = arenaQueue.contains(player.getId());
        if (inQueue) {
            return true;
        } else if (arenaQueue.add(player.getId())) {
            queueSize++;
            return true;
        } else {
            return false;
        }
    }

    public static int getQueueSize() {
        return queueSize;
    }

    public static boolean removePlayer(int playerId) {

        boolean isRemoved = arenaQueue.remove(playerId);
        if (isRemoved & queueSize > 0) {
            queueSize--;
        }
        return isRemoved;
    }

    private void run() {
        while (!stopped) {
            try {
                processQueue();
                HibernateUtil.closeSession(true);
            } catch (Throwable t) {
                t.printStackTrace();
                HibernateUtil.closeSession(false);
            }
            try {
                Thread.sleep(500);
            } catch (Throwable tt) {
                /*do nothing*/
            }
        }
        synchronized (this) {
            notifyAll();
        }
    }

    private void processQueue() {
        synchronized (arenaQueue) {
            while (arenaQueue.size() >= 2) {
                Player player1 = DAO.getPlayer(arenaQueue.poll());
                Player player2 = DAO.getPlayer(arenaQueue.poll());
                if (player1 != null && player2 != null) {
                    queueSize = Math.min(queueSize - 2, 0);
                    Arena arena = new Arena(new ArenaParticipant(player1), new ArenaParticipant(player2));
                    Arena.add(arena);
                    player1.setState(READY);
                    player1.setCurrentArenaId(arena.getId());
                    player2.setState(READY);
                    player2.setCurrentArenaId(arena.getId());
                }
            }
            HibernateUtil.closeSession(true);
            arenaQueue.notifyAll();
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
