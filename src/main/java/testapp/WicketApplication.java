package testapp;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.cycle.RequestCycleListenerCollection;
import testapp.db.HibernateUtil;
import testapp.game.QueueScheduler;
import testapp.pages.*;

import java.util.regex.Pattern;

public class WicketApplication extends WebApplication {
    public static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9]{6,16}$", Pattern.UNICODE_CASE);
    public static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]{5,15}$", Pattern.CASE_INSENSITIVE);
    private QueueScheduler queueScheduler;

    @Override
    public Class<? extends WebPage> getHomePage() {
        return HomePage.class;
    }

    @Override
    public void init() {
        super.init();
        HibernateUtil.init();
        mountPages();
        queueScheduler = new QueueScheduler();
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new WebSession(request);
    }

    private void mountPages() {
        mountPage("/welcome", HomePage.class);
        mountPage("/menu", MenuPage.class);
        mountPage("/queue", QueuePage.class);
        mountPage("/queue/warmup", WarmUpPage.class);
        mountPage("/combat", CombatPage.class);
    }

    @Override
    protected void onDestroy() {
        HibernateUtil.getSession().close();
        HibernateUtil.getSessionFactory().close();
        queueScheduler.stop();
    }

    @Override
    public RequestCycleListenerCollection getRequestCycleListeners() {
        RequestCycleListenerCollection RCLCollection = super.getRequestCycleListeners();
        RCLCollection.add(new IRequestCycleListener() {

            @Override
            public void onEndRequest(RequestCycle cycle) {
                HibernateUtil.closeSession(true);
            }

            @Override
            public IRequestHandler onException(RequestCycle cycle, Exception ex) {
                HibernateUtil.closeSession(false);
                ex.printStackTrace();
                return null;
            }
        });
        return RCLCollection;
    }
}
