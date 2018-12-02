package testapp.pages;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import testapp.WebSession;

import static testapp.game.PlayerState.IDLE;
import static testapp.game.PlayerState.IN_QUEUE;

public class MenuPage extends BasePage {

    private static final long serialVersionUID = 1L;

    public MenuPage(PageParameters parameters) {
        super(parameters);

        setPlayerState(IDLE);
        add(new StatelessLink<MarkupContainer>("enterQueue") {
            @Override
            public void onClick() {
                if (getPlayerState() == IDLE) {
                    setPlayerState(IN_QUEUE);
                    setResponsePage(QueuePage.class);
                }
            }
        });

        add(new StatelessLink<MarkupContainer>("Exit") {
            @Override
            public void onClick() {
                WebSession.get().logout();
                setResponsePage(HomePage.class, new PageParameters());
            }
        });
    }



}
