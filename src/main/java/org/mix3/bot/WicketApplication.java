package org.mix3.bot;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.session.ISessionStore;
import org.mix3.bot.auth.AuthSession;
import org.mix3.bot.auth.Redirect;
import org.mix3.bot.auth.SignInPage;
import org.mix3.bot.cron.ParseCron;
import org.mix3.bot.cron.PostCron;
import org.mix3.bot.cron.RepliesCron;
import org.mix3.bot.page.AuthPage;
import org.mix3.bot.page.IndexPage;
import org.mix3.bot.page.PinPage;

import appengine.wicket.AppEngineWebApplicationHelper;

public class WicketApplication extends AuthenticatedWebApplication {
	AppEngineWebApplicationHelper helper = new AppEngineWebApplicationHelper(this);

	@Override
	public Class<? extends WebPage> getHomePage() {
		return IndexPage.class;
	}

	@Override
	protected void init() {
		super.init();
		helper.init();
		
		addComponentInstantiationListener(new GuiceComponentInjector(this));
		
		getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
		getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
		
		mountBookmarkablePage("/home", IndexPage.class);
		mountBookmarkablePage("/auth", AuthPage.class);
		mountBookmarkablePage("/auth/pin", PinPage.class);
		mountBookmarkablePage("/redirect", Redirect.class);
		mountBookmarkablePage("/cron/parse", ParseCron.class);
		mountBookmarkablePage("/cron/post", PostCron.class);
		mountBookmarkablePage("/cron/replies", RepliesCron.class);
	}

	@Override
	public String getConfigurationType() {
		return helper.getConfigurationType();
	}

	@Override
	protected ISessionStore newSessionStore() {
		return helper.newSessionStore();
	}

	@Override
	protected WebRequest newWebRequest(HttpServletRequest servletRequest) {
		return helper.newWebRequest(servletRequest);
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return SignInPage.class;
	}

	@Override
	protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
		return AuthSession.class;
	}
}
