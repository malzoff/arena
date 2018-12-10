package testapp.pages;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.time.Duration;
import testapp.WebSession;
import testapp.game.PlayerState;
import testapp.game.QueueScheduler;

public class QueuePage extends BasePage {

    public QueuePage(PageParameters parameters) {
        super(parameters);
        add(new Label("qSize", getModel(QueueScheduler.getQueueSize()))
                .add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(500)))
        );
        add(new Label("rating", getModel(getPlayer().getRating())));
        add(new StatelessLink<MarkupContainer>("enterQueue") {

            @Override
            public void onClick() {
                if (WebSession.get().isLoggedIn()) {
                    if (QueueScheduler.addPlayer(getPlayer())) {
                        getPlayer().setState(PlayerState.IN_QUEUE);
                    }
                    setEnabled(false).setVisible(false);
                    setResponsePage(QueuePage.class, new PageParameters());
                }
            }
        });
        add(new WebMarkupContainer("processing"));
        add(new AbstractAjaxTimerBehavior(Duration.milliseconds(100)) {
            @Override
            protected void onTimer(AjaxRequestTarget target) {
                if (WebSession.get().isLoggedIn()) {
                    if (getPlayer().getState() == PlayerState.READY && getPlayer().getEnemy() != null) {
                        setResponsePage(WarmUpPage.class);
                    }
                }
            }
        });
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        get("enterQueue").setEnabled(getPlayerState() == PlayerState.IDLE).setVisible(getPlayerState() == PlayerState.IDLE);
        get("processing").setVisible(getPlayer().getState() == PlayerState.IN_QUEUE);
    }
}
