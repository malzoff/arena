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
        new Thread(this::run).start();
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public static boolean addPlayer(Player player) {
        if (arenaQueue.contains(player.getId()) || arenaQueue.add(player.getId())) {
            queueSize++;
            System.err.println("player#" + player.getId() + " added to queue");
            System.err.println("Queue:" + arenaQueue.toString());
            return true;
        } else {
            return false;
        }

    }

    public static int getQueueSize() {
        return arenaQueue.size();
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
                System.err.println("start Queue processing...");
                processQueue();
                System.err.println("Queue:" + arenaQueue.toString());
                HibernateUtil.closeSession(true);
            } catch (Throwable t) {
                t.printStackTrace();
                HibernateUtil.closeSession(false);
            }
            System.err.println("End queue proessing.");
            try {
                Thread.sleep(500);
            } catch(Throwable tt) {
                /*do nothing*/
            }
        }
        synchronized(this) {
            notifyAll();
        }
    }

    private void processQueue() {
        synchronized (arenaQueue) {
            while (arenaQueue.size() >= 2) {
                Player player1 = HibernateUtil.get(Player.class, arenaQueue.poll());
                System.err.println("player1#" + player1.getId());
                Player player2 = HibernateUtil.get(Player.class, arenaQueue.poll());
                System.err.println("player2#" + player2.getId());
                queueSize = Math.min(queueSize - 2, 0);
                System.err.println("Queue size:" + queueSize);
                System.err.println("Actual queue size:" + arenaQueue.size());
                player1.setState(READY);
                player1.setEnemy(player2);
                player2.setState(READY);
                player2.setEnemy(player1);
            }
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
