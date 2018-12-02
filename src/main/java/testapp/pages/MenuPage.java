package testapp.pages;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import static testapp.game.PlayerState.IDLE;

public class MenuPage extends BasePage {

    private static final long serialVersionUID = 1L;

    public MenuPage(PageParameters parameters) {
        super(parameters);

        add(new StatelessLink<MarkupContainer>("enterQueue") {
            @Override
            public void onClick() {
                if (getPlayerState() == IDLE) {
                    setResponsePage(QueuePage.class);
                } else {
                    setPlayerState(IDLE);
                }
            }
        });

        add(new StatelessLink<MarkupContainer>("Exit") {
            @Override
            public void onClick() {

            }
        });
    }



}
