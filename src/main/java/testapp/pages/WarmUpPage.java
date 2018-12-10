package testapp.pages;

import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.time.Duration;
import testapp.db.HibernateUtil;
import testapp.db.beans.User;
import testapp.game.PlayerState;
import testapp.game.TimeUtil;

public class WarmUpPage extends BasePage {

    public WarmUpPage(PageParameters parameters) {
        super(parameters);

        final long startTime = TimeUtil.now() + TimeUtil.SECOND * 30;
        Label label = new Label("timer"
                , (IModel<Integer>) () -> (int) Math.max((startTime - TimeUtil.now()) / TimeUtil.SECOND, 0)
        );
        label.add(new AjaxSelfUpdatingTimerBehavior(Duration.milliseconds(100)));
        add(label);
        add(new Label("enemy", (IModel<String>) () -> getEmemyName()));
        add(new AbstractAjaxTimerBehavior(Duration.milliseconds(100)) {
            @Override
            protected void onTimer(AjaxRequestTarget target) {
                if (startTime - TimeUtil.now() <= 0) {
                    setPlayerState(PlayerState.IN_COMBAT);
                    setResponsePage(CombatPage.class, new PageParameters());
                }
            }
        });
    }

    private String getEmemyName() {
        User user = HibernateUtil.get(User.class, getPlayer().getEnemy().getId());
        return user == null ? "отсутствует" : user.getLogin();
    }
}
