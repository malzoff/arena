package testapp.db;

import testapp.db.beans.ArenaParticipant;
import testapp.db.beans.Player;
import testapp.db.beans.User;

public class DAO {

    public static User createUser(String login, String password) {
        return (User) HibernateUtil.persist(new User(login, password));
    }

    public static Player createPlayer(User user) {
        return (Player) HibernateUtil.persist(new Player(user.getId()));
    }

    public static ArenaParticipant createArenaParticipant(Player player) {
        return (ArenaParticipant) HibernateUtil.persist(new ArenaParticipant(player));
    }

    public static User getUser(int id) {
        return HibernateUtil.get(User.class, id);
    }

    public static User getUser(String login) {
        Integer id = (Integer) HibernateUtil.getSession().createQuery("select id from User u where u.login=:login")
                .setString("login", login)
                .uniqueResult();
        return id == null ? null : HibernateUtil.get(User.class, id);
    }

    public static Player getPlayer(int id) {
        return HibernateUtil.get(Player.class, id);
    }

    public static ArenaParticipant getArenaParticipant(int id) {
        return HibernateUtil.get(ArenaParticipant.class, id);
    }
}
