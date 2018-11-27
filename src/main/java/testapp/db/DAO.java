package testapp.db;

import testapp.db.beans.Player;
import testapp.db.beans.User;

public class DAO {

    public static User getUser(int id) {
        return (User) getBean(User.class, id);
    }

    private static User getUser(String login) {
        Integer id = (Integer) HibernateUtil.getSession().createQuery("select id from User u where u.login=:login")
                .setString("login", login)
                .uniqueResult();
        return id == null ? null : HibernateUtil.get(User.class, id);
    }

    public static Player getPlayer(int id) {
        return (Player) getBean(Player.class, id);
    }

    private static Object getBean(Class clazz, int id) {
        return HibernateUtil.getSession().get(clazz, id);
    }
}
