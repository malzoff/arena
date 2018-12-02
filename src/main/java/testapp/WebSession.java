package testapp;

import org.apache.wicket.request.Request;
import testapp.db.beans.User;
import testapp.game.PlayerState;

public class WebSession extends org.apache.wicket.protocol.http.WebSession {

    private int userId;

    private PlayerState playerState = PlayerState.IDLE;
    private int currentHp;

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

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public int getCurrentHp() {
        return Math.max(0, currentHp);
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = Math.max(0, currentHp);
    }

    public void logout() {
        userId = 0;
        WebSession.get().invalidate();
    }
}
