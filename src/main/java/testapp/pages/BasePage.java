package testapp.pages;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import testapp.WebSession;
import testapp.db.beans.Player;
import testapp.game.TimeUtil;

abstract class BasePage extends WebPage {

    BasePage(PageParameters parameters) {
        checkForRedirectToHome();
        add(new Label("workTime", getPageGenerationTime()));
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

    protected long getPageGenerationTime() {
        return TimeUtil.now() - RequestCycle.get().getStartTime();
    }

    protected void checkForRedirectToHome() {
        if (!WebSession.get().isLoggedIn() && getPageClass() != HomePage.class) {
            throw new RestartResponseException(HomePage.class, new PageParameters());
        }
    }

    protected int getUserId() {
        return WebSession.get().getUserId();
    }

    public <T> IModel<T> getModel(T o) {
        return (IModel<T>) () -> o;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        checkForRedirectToHome();
    }
}
