package testapp.pages;

import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.time.Duration;
import testapp.WebSession;
import testapp.game.PlayerState;
import testapp.game.QueueScheduler;

public class QueuePage extends BasePage {

    public QueuePage(PageParameters parameters) {
        super(parameters);
        System.err.println("stateful=" + !isStateless());
        Label label = new Label("qSize", Integer.toString(QueueScheduler.getQueueSize()));
        add(label);
        label.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(1)));

        add(new AbstractAjaxTimerBehavior(Duration.milliseconds(100)) {
            @Override
            protected void onTimer(AjaxRequestTarget target) {
                if (WebSession.get().isLoggedIn()) {
                    if (getPlayer().getState() == PlayerState.IDLE) {
                        if (QueueScheduler.addPlayer(getPlayer())) {
                            getPlayer().setState(PlayerState.IN_QUEUE);
                        }
                    }
                    if (getPlayer().getState() == PlayerState.READY && getPlayer().getEnemy() != null) {
                        setResponsePage(WarmUpPage.class);
                    }
                }
            }
        });
    }
}
