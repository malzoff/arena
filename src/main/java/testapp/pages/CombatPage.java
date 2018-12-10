package testapp.pages;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import testapp.db.HibernateUtil;
import testapp.db.beans.Player;
import testapp.game.ArenaParticipant;

import java.util.ArrayList;
import java.util.List;

public class CombatPage extends BasePage {

    private IModel<ArenaParticipant> myModel;
    private IModel<ArenaParticipant> enemyModel;
    private List<String> log = new ArrayList<>();

    public CombatPage(PageParameters parameters) {
        super(parameters);
        int userId = parameters.get("id").toInt(0);
        if (userId == 0) throw new RestartResponseException(HomePage.class, new PageParameters());
        Player player = HibernateUtil.get(Player.class, userId);

        if (myModel == null) {
            myModel = (IModel<ArenaParticipant>) () -> new ArenaParticipant(player);
        }
        if (enemyModel == null) {
            enemyModel = (IModel<ArenaParticipant>) () -> new ArenaParticipant(player.getEnemy());
        }

        add(new ArenaParticipantPanel("myPanel", myModel));
        add(new ArenaParticipantPanel("enemyPanel", enemyModel));

        add(new StatelessLink<MarkupContainer>("attack") {
            @Override
            public void onClick() {

            }
        });

        add(new ListView<String>("log", (IModel<List<String>>) () -> log) {
            @Override
            protected void populateItem(ListItem<String> item) {
                item.add(new Label("message", item.getModelObject()));
            }
        });

    }
}
