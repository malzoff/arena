package testapp;

import org.apache.wicket.request.Request;
import testapp.db.beans.User;

public class WebSession extends org.apache.wicket.protocol.http.WebSession {

    private int userId;
    private int playerState;
    private int currentHp;

    public WebSession(Request request) {
        super(request);
    }

    public int getUserId() {
        return userId;
    }

    public void setUser(User user) {
        userId = user.getId();
        WebSession.get().bind();
    }

    public int getPlayerState() {
        return playerState;
    }

    public void setPlayerState(int playerState) {
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
