package testapp.db;


import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;
    private static ThreadLocal<Session> session = new ThreadLocal<>();

    public static void init() {
        try {
            Configuration configuration = new Configuration().configure();
            sessionFactory = configuration.buildSessionFactory(
                    new StandardServiceRegistryBuilder()
                            .applySettings(configuration.getProperties())
                            .build());
            sessionFactory.getStatistics().setStatisticsEnabled(true);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            init();
        }
        return sessionFactory;
    }

    public static Session getSession() {
        Session s = session.get();
        if (s == null) {
            s = getSessionFactory().openSession();
            s.setFlushMode(FlushMode.COMMIT);
            s.setCacheMode(CacheMode.NORMAL);
            session.set(s);
            s.beginTransaction();
        }
        return s;
    }

    public static void closeSession(boolean commit) {
        Session s = session.get();
        if (s != null) {
            try {
                if (s.getTransaction() != null && s.getTransaction().isActive()) {
                    if (commit) {
                        s.getTransaction().commit();
                    } else {
                        s.getTransaction().rollback();
                    }
                }
            } finally {
                try {
                    s.close();
                } finally {
                    session.set(null);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> clazz, Integer id) {
        return id != null && id > 0 ? (T) getSession().get(clazz, id) : null;
    }

    public static Object persist(Object o) {
        getSession().persist(o);
        return o;
    }
}
