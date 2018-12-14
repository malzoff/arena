package testapp;

import org.apache.wicket.request.Request;
import testapp.db.DAO;
import testapp.db.HibernateUtil;
import testapp.db.beans.ArenaParticipant;
import testapp.db.beans.Player;
import testapp.db.beans.User;
import testapp.game.QueueScheduler;

import java.util.ArrayList;
import java.util.List;

public class WebSession extends org.apache.wicket.protocol.http.WebSession {

    private int userId;
    private int currentHp;
    private List<String> combatLog = new ArrayList<>();
    private String result;

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

    public ArenaParticipant getArenaParticipant() {
        return DAO.getArenaParticipant(userId);
    }

    public ArenaParticipant getArenaParticipantEnemy() {
        return DAO.getArenaParticipant(getArenaParticipant().getEnemyId());
    }

    public List<String> getCombatLog() {
        return combatLog;
    }

    public User getUser() {
        return DAO.getUser(userId);
    }

    public void setCombatResult(String result) {
        this.result = result;
    }

    public String geCombatResult() {
        return result;
    }
}
