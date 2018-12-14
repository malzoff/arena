package testapp.pages;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import testapp.WebSession;
import testapp.db.beans.ArenaParticipant;
import testapp.db.beans.Player;
import testapp.db.beans.User;
import testapp.game.TimeUtil;

import java.util.List;

public abstract class BasePage extends WebPage {

    public BasePage(PageParameters parameters) {
        super(parameters);
        checkForRedirect();
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

    protected void checkForRedirect() {
        redirectToHomePage();
    }
    private void redirectToHomePage() {
        if (!WebSession.get().isLoggedIn() && getPageClass() != HomePage.class) {
            throw new RestartResponseException(HomePage.class, new PageParameters());
        }
    }

    protected int getUserId() {
        return WebSession.get().getUserId();
    }

    protected User getUser() {
        return WebSession.get().getUser();
    }

    protected ArenaParticipant getArenaParticipant() {
        return WebSession.get().getArenaParticipant();
    }

    protected ArenaParticipant getArenaParticipantEnemy() {
        return WebSession.get().getArenaParticipantEnemy();
    }

    protected List<String> getCombatLog() {
        return WebSession.get().getCombatLog();
    }

    public boolean isLoggedIn() {
        return WebSession.get().isLoggedIn();
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        checkForRedirect();
    }
}
