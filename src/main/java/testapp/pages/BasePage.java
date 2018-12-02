package testapp.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import testapp.WebSession;
import testapp.game.PlayerState;

public abstract class BasePage extends WebPage {

    public BasePage(PageParameters parameters) {

    }

    protected PlayerState getPlayerState() {
        return WebSession.get().getPlayerState();
    }

    protected void setPlayerState(PlayerState state) {
        WebSession.get().setPlayerState(state);
    }
}
