package testapp.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import testapp.WebSession;
import testapp.db.beans.Player;

abstract class BasePage extends WebPage {

    BasePage(PageParameters parameters) {
        if (!WebSession.get().isLoggedIn() && getPageClass() != HomePage.class) {
            setResponsePage(HomePage.class, new PageParameters());
        }
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
