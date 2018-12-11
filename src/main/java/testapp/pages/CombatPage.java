package testapp.pages;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import testapp.game.Arena;
import testapp.game.ArenaParticipant;
import testapp.game.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class CombatPage extends BasePage {

    private IModel<ArenaParticipant> myModel;
    private IModel<ArenaParticipant> enemyModel;
    private List<String> log = new ArrayList<>();

    public CombatPage(PageParameters parameters) {
        super(parameters);
        Arena arena = getArena();
        if (arena == null) {
            throw new RestartResponseException(QueuePage.class);
        }
        myModel = (IModel<ArenaParticipant>) () -> arena.getAp(getUserId());
        enemyModel = (IModel<ArenaParticipant>) () -> arena.getEnemy(getUserId());
        add(new ArenaParticipantPanel("myPanel", myModel));
        add(new ArenaParticipantPanel("enemyPanel", enemyModel));
        ArenaParticipant myAP = myModel.getObject();
        ArenaParticipant enemyAP = enemyModel.getObject();
        add(new StatelessLink<MarkupContainer>("attack") {
                    {
                        setOutputMarkupId(true);
                    }

                    @Override
                    public void onClick() {
                        if (myAP.getCurrentHp() > 0 && enemyAP.getCurrentHp() > 0 && TimeUtil.now() >= myAP.getNextHitTime()) {
                            int damageAmount = myAP.doDamage(enemyAP);
                            arena.getBattleLog().add(0, myAP.getName() + " ударил " + enemyAP.getName()
                                    + " на" + damageAmount + " урона."
                            );
                            if (enemyAP.getCurrentHp() <= 0) {
                                arena.getBattleLog().add(0, myAP.getName() + " убил " + enemyAP.getName() + ".");
                            }
                        }
                        setResponsePage(CombatPage.class);
                    }
                }
        );

        add(new ListView<String>("log", (IModel<List<String>>) () -> log) {

            @Override
            protected void populateItem(ListItem<String> item) {
                item.add(new Label("message", item.getModelObject()));
            }
        });
    }

    private Arena getArena() {
        return Arena.get(getPlayer().getCurrentArenaId());
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        get("attack").setEnabled(myModel.getObject().getCurrentHp() > 0
                && enemyModel.getObject().getCurrentHp() > 0
                && TimeUtil.now() >= myModel.getObject().getNextHitTime()
        );
    }
}
