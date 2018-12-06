package testapp.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import testapp.WebSession;
import testapp.db.beans.Player;

public abstract class BasePage extends WebPage {

    public BasePage(PageParameters parameters) {

    }

    protected Player getPlayer() {
        return WebSession.get().getPlayer();
    }

    protected void setPlayerState(int state) {
        getPlayer().setState(state);
    }

    protected int getPlayerState() {
        return getPlayer().getState();
    }
}
