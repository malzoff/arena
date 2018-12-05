package testapp.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import testapp.game.PlayerState;
import testapp.game.QueueScheduler;

public class QueuePage extends BasePage {
    public QueuePage(PageParameters parameters) {
        super(parameters);
        if (getPlayer().getState() ==  PlayerState.IDLE) {
            QueueScheduler.addPlayer(getPlayer());
        }
    }
}
