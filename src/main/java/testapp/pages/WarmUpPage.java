package testapp.pages;

import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.time.Duration;
import testapp.WebSession;
import testapp.game.PlayerState;
import testapp.game.TimeUtil;

public class WarmUpPage extends BasePage {

    public WarmUpPage(PageParameters parameters) {
        super(parameters);

        getCombatLog().clear();
        WebSession.get().setCurrentHp(getArenaParticipant().getMaxHp());
        final long startTime = TimeUtil.now() + TimeUtil.SECOND * 5;
        Label label = new Label("timer"
                , (IModel<Integer>) () -> (int) Math.max((startTime - TimeUtil.now()) / TimeUtil.SECOND, 0)
        );
        label.add(new AjaxSelfUpdatingTimerBehavior(Duration.milliseconds(100)));
        add(label);
        add(new Label("enemy", (IModel<String>) () -> getEnemyName()));
        add(new AbstractAjaxTimerBehavior(Duration.milliseconds(100)) {
            @Override
            protected void onTimer(AjaxRequestTarget target) {
                if (startTime - TimeUtil.now() <= 0) {
                    setPlayerState(PlayerState.IN_COMBAT);
                    setResponsePage(CombatPage.class);
                }
            }
        });
    }

    private String getEnemyName() {
        return getArenaParticipantEnemy().getName();
    }
}
