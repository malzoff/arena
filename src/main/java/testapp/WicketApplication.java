package testapp;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import testapp.db.HibernateUtil;
import testapp.game.QueueScheduler;
import testapp.pages.HomePage;
import testapp.pages.MenuPage;
import testapp.pages.QueuePage;

import java.util.regex.Pattern;

public class WicketApplication extends WebApplication
{
	public static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9]{6,16}$", Pattern.UNICODE_CASE);
	public static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]{5,15}$", Pattern.CASE_INSENSITIVE);
	private QueueScheduler queueScheduler;

	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return HomePage.class;
	}

	@Override
	public void init()
	{
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
	}

	@Override
	protected void onDestroy() {
		HibernateUtil.getSession().close();
		HibernateUtil.getSessionFactory().close();
		queueScheduler.stop();
	}
}
