package org.mix3.bot.page;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.http.RequestToken;

@AuthorizeInstantiation({Roles.ADMIN})
public class AuthPage extends AbstractWebPage{
	public AuthPage(){
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback.setOutputMarkupId(true));
		
		Twitter twitter;
		try{
			twitter = service.getTwitter(service.getAccessToken());
			System.out.println("service.getAccessToken()");
		}catch(TwitterException notAuth){
			System.out.println("TwitterException notAuth");
			try {
				twitter = service.getTwitter();
				RequestToken requestToken = twitter.getOAuthRequestToken();
				service.setToken(requestToken.getToken(), requestToken.getTokenSecret());
				throw new RestartResponseException(
						new RedirectPage(requestToken.getAuthorizationURL()));
			} catch (TwitterException e) {
				e.printStackTrace();
				error("Error...");
			}
		}
		
		add(new Form<Void>("form"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit() {
				service.clearToken();
				setResponsePage(AuthPage.class);
			}
		});
	}
}
