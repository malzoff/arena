package testapp;

import org.apache.wicket.request.Request;
import testapp.db.HibernateUtil;
import testapp.db.beans.Player;
import testapp.db.beans.User;
import testapp.game.QueueScheduler;

public class WebSession extends org.apache.wicket.protocol.http.WebSession {

    private int userId;
    private int currentHp;
    private int currentArenaId;

    public WebSession(Request request) {
        super(request);
    }

    public static WebSession get() {
        return (WebSession) org.apache.wicket.protocol.http.WebSession.get();
    }

    public int getUserId() {
        return userId;
    }

    public void setUser(User user) {
        userId = user.getId();
        WebSession.get().bind();
    }

    public int getCurrentHp() {
        return Math.max(0, currentHp);
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = Math.max(0, currentHp);
    }

    public void logout() {
        QueueScheduler.removePlayer(userId);
        userId = 0;
        WebSession.get().invalidate();
    }

    public boolean isLoggedIn() {
        return !WebSession.get().isTemporary() && !WebSession.get().isSessionInvalidated() && userId > 0;
    }

    public Player getPlayer() {
        return HibernateUtil.get(Player.class, userId);
    }

    public int getCurrentArenaId() {
        return currentArenaId;
    }

    public void setCurrentArenaId(int currentArenaId) {
        this.currentArenaId = currentArenaId;
    }
}
