package testapp.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import testapp.db.beans.ArenaParticipant;

public class ResultsPage extends BasePage {

    ResultsPage(PageParameters parameters) {
        super(parameters);

        boolean isWinner = parameters.get("res").toInt() == CombatPage.WIN;
        add(new Label("result", (IModel<String>) () -> isWinner ? "Победа!" : "Поражение..."));
        add(new Label("rating", (IModel<String>) () -> String.valueOf(getPlayer().getRating())));
        add(new ArenaParticipantPanel("stats", (IModel<ArenaParticipant>) () -> getArenaParticipant()));
        add(new BookmarkablePageLink("back", MenuPage.class));
    }
}
