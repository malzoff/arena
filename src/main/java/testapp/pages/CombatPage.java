package testapp.pages;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.time.Duration;
import testapp.WebSession;
import testapp.db.beans.ArenaParticipant;

import java.util.List;

public class CombatPage extends BasePage {

    public static int WIN = 1;
    public static int DEFEAT = 2;

    public CombatPage(PageParameters parameters) {
        super(parameters);

        add(new ArenaParticipantPanel("myPanel", (IModel<ArenaParticipant>) () -> getArenaParticipant()));
        add(new ArenaParticipantPanel("enemyPanel", (IModel<ArenaParticipant>) () -> getArenaParticipantEnemy()));
        StatelessLink attackLink;
        add(attackLink = new StatelessLink<MarkupContainer>("attack") {

                    @Override
                    public void onClick() {
                        ArenaParticipant myAP = getArenaParticipant();
                        ArenaParticipant enemyAP = getArenaParticipantEnemy();
                        if (isLoggedIn() && myAP.canHit() && enemyAP.getCurrentHp() > 0) {
                            int damageAmount = myAP.doDamage(enemyAP);
                            getCombatLog().add(0, "Вы ударили " + enemyAP.getName()
                                    + " на " + damageAmount + " урона."
                            );
                            if (enemyAP.getCurrentHp() <= 0) {
                                getCombatLog().add(0, "Вы убили " + enemyAP.getName() + ".");
                            }
                            setEnabled(false);
                        }
                        setResponsePage(CombatPage.class);
                    }
                }
        );
        attackLink.setOutputMarkupId(true);

        StatelessLink resultLink;
        add(resultLink = new StatelessLink<MarkupContainer>("resultLink") {
            @Override
            public void onClick() {
                int result = getCombatResult();
                if (result != 0) {
                    getPlayer().addLevel();
                    getPlayer().addRating(result == WIN ? +1 : -1);
                    getArenaParticipant().updateStats(getPlayer().getLevel());
                    setResponsePage(ResultsPage.class, getPageParameters().add("r", result));
                }
            }
        });
        resultLink.setOutputMarkupId(true);
        resultLink.setEnabled(false).setVisible(false);

        WebMarkupContainer container;
        add(container = new WebMarkupContainer("container"));
        container.setOutputMarkupId(true);
        container.add(new ListView<String>("combatLog", (IModel<List<String>>) () -> getCombatLog()) {

            @Override
            protected void populateItem(ListItem<String> item) {
                item.add(new Label("message", item.getModelObject()));
            }
        });

        add(new AbstractAjaxTimerBehavior(Duration.milliseconds(200)) {
            @Override
            protected void onTimer(AjaxRequestTarget target) {
                int hpLoss = getArenaParticipant().getReceivedDamage();
                if (hpLoss > 0) {
                    getCombatLog().add(0, getArenaParticipantEnemy().getName() + " ударил Вас на " + hpLoss + " урона.");
                    if (WebSession.get().getCurrentHp() <= 0) {
                        getCombatLog().add(0, getArenaParticipantEnemy().getName() + " убил Вас.");
                    }
                }
                getArenaParticipant().setReceivedDamage(0);
                if (!attackLink.isEnabled() && getArenaParticipant().canHit() && getArenaParticipantEnemy().getCurrentHp() > 0
                        && getCombatResult() == 0
                ) {
                    attackLink.setEnabled(true);
                    target.add(attackLink);
                    setResponsePage(CombatPage.class);
                }
                if (getCombatResult() != 0 && !resultLink.isEnabled()) {
                    resultLink.setEnabled(true).setVisible(true);
                    target.add(resultLink);
                    setResponsePage(CombatPage.class);
                }
                target.add(container, get("myPanel"));
            }

            @Override
            public void beforeRender(Component component) {
                super.beforeRender(component);

            }
        });
    }

    private int getCombatResult() {
        if (getArenaParticipant().getCurrentHp() > 0) {
            if (getArenaParticipantEnemy().getCurrentHp() <= 0) {
                return WIN;
            } else {
                return 0;
            }
        } else return DEFEAT;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        get("attack").setEnabled(getArenaParticipant().canHit() && getArenaParticipantEnemy().getCurrentHp() > 0);
        get("resultLink").setEnabled(getCombatResult() != 0).setVisible(getCombatResult() != 0);
    }
}
