package testapp.pages;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import testapp.db.beans.ArenaParticipant;

import java.util.List;

public class CombatPage extends BasePage {

    public CombatPage(PageParameters parameters) {
        super(parameters);
        add(new ArenaParticipantPanel("myPanel", (IModel<ArenaParticipant>) () -> getArenaParticipant()));
        add(new ArenaParticipantPanel("enemyPanel", (IModel<ArenaParticipant>) () -> getArenaParticipantEnemy()));
        add(new StatelessLink<MarkupContainer>("attack") {

                    @Override
                    public void onClick() {
                        ArenaParticipant myAP = getArenaParticipant();
                        ArenaParticipant enemyAP = getArenaParticipantEnemy();
                        if (isLoggedIn() && myAP.canHit() && enemyAP.getCurrentHp() > 0) {
                            int damageAmount = myAP.doDamage(enemyAP);
                            getCombatLog().add(0, "Вы ударили " + enemyAP.getName()
                                    + " на" + damageAmount + " урона."
                            );
                            if (enemyAP.getCurrentHp() <= 0) {
                                getCombatLog().add(0, "Вы убили " + enemyAP.getName() + ".");
                            }
                        }
                        setResponsePage(CombatPage.class, new PageParameters());
                    }
                }
                .setEnabled(getArenaParticipant().canHit() && getArenaParticipantEnemy().getCurrentHp() > 0)
        );

        add(new ListView<String>("combatLog", (IModel<List<String>>) () -> getCombatLog()) {

            @Override
            protected void populateItem(ListItem<String> item) {
                item.add(new Label("message", item.getModelObject()));
            }
        });
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        get("attack").setEnabled(getArenaParticipant().canHit() && getArenaParticipantEnemy().getCurrentHp() > 0);
    }
}
