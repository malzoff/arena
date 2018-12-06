package testapp.pages;

import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.time.Duration;
import testapp.WebSession;
import testapp.game.PlayerState;
import testapp.game.QueueScheduler;

public class QueuePage extends BasePage {

    private int qSize;

    public QueuePage(PageParameters parameters) {
        super(parameters);
        if (getPlayer().getState() ==  PlayerState.IDLE) {
            if(QueueScheduler.addPlayer(getPlayer())) {
                getPlayer().setState(PlayerState.IN_QUEUE);
            }
        }
        qSize = QueueScheduler.getQueueSize();
        add(new Label("qSize", Integer.toString(qSize)) {
        });

        add(new AbstractAjaxTimerBehavior(Duration.milliseconds(500)) {
            @Override
            protected void onTimer(AjaxRequestTarget target) {
                qSize = QueueScheduler.getQueueSize();
                if (WebSession.get().isLoggedIn()
                        && getPlayer().getState() == PlayerState.READY
                        && getPlayer().getEnemy() != null
                ) {
                    setResponsePage(CombatPage.class);
                }
            }
        });
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        System.err.println("onBeforeRender::qSize=" + qSize);
    }
}
