package testapp.game;

import testapp.db.DAO;
import testapp.db.HibernateUtil;
import testapp.db.beans.ArenaParticipant;
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
                Thread.sleep(500);
            } catch (Throwable tt) {
                /*do nothing*/
            }
        }
    }

    private void processQueue() {
        synchronized (arenaQueue) {
            boolean needCommit = arenaQueue.size() >= 2;
            while (arenaQueue.size() >= 2) {
                int player1Id = arenaQueue.poll();
                int player2Id = arenaQueue.poll();
                queueSize = Math.max(queueSize - 2, 0);

                prepareEntities(player1Id);
                DAO.getArenaParticipant(player1Id).setEnemyId(player2Id);
                prepareEntities(player2Id);
                DAO.getArenaParticipant(player2Id).setEnemyId(player1Id);
            }
            if (needCommit) {
                HibernateUtil.closeSession(true);
            }
            arenaQueue.notifyAll();
        }
    }

    private void prepareEntities(int player1Id) {
        Player player = DAO.getPlayer(player1Id);
        player.setState(READY);
        ArenaParticipant ap = DAO.getArenaParticipant(player1Id);
        ap.updateStats(player.getLevel());
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
