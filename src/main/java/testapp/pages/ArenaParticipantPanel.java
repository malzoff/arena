package testapp.pages;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import testapp.db.beans.ArenaParticipant;

public class ArenaParticipantPanel extends Panel {

    public ArenaParticipantPanel(String id, IModel<ArenaParticipant> arenaParticipantModel) {
        super(id, arenaParticipantModel);

        add(new Label("name", (IModel<String>) () -> arenaParticipantModel.getObject().getName()));
        add(new Label("damage", (IModel<String>) () ->
                arenaParticipantModel.getObject().getMinDamage()
                        + "~"
                        + arenaParticipantModel.getObject().getMaxDamage())
        );
        add(new WebComponent("hp")
                .add(new AttributeModifier("style"
                        , (IModel<String>) () -> {
                    ArenaParticipant ap = arenaParticipantModel.getObject();
                    int hpPercents = (int) Math.max((1f * ap.getCurrentHp() / ap.getMaxHp() * 100), 0);
                    return "width:" + hpPercents + "%";
                }))
        );
    }
}
