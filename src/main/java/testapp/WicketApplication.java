package testapp;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import testapp.db.HibernateUtil;
import testapp.pages.HomePage;

import java.util.regex.Pattern;

public class WicketApplication extends WebApplication
{
	public static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$", Pattern.UNICODE_CASE);
	public static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]+", Pattern.CASE_INSENSITIVE);

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

	}

	@Override
	public Session newSession(Request request, Response response) {
		return new WebSession(request);
	}


}
