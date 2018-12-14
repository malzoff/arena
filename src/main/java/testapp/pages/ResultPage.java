package testapp.pages;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import testapp.WebSession;
import testapp.game.CombatResult;

public class ResultPage extends BasePage {


    public ResultPage(PageParameters parameters) {
        super(parameters);
        boolean isWinner = WebSession.get().geCombatResult().equals(CombatResult.WIN);
        add(new Label("result", isWinner ? "Победа!" : "Поражение..."));
        add(new Label("rating", String.valueOf(getPlayer().getRating())));
        add(new Label("damage", (IModel<String>) () -> getArenaParticipant().getMinDamage() + "~" + getArenaParticipant().getMaxDamage()));
        add(new Label("hp", (IModel<String>) () -> String.valueOf(getArenaParticipant().getMaxHp())));

        add(new StatelessLink<MarkupContainer>("back") {
            @Override
            public void onClick() {
                setResponsePage(MenuPage.class);
            }
        });
    }
}
