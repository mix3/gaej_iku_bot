package org.mix3.bot.auth;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.WebPage;
import org.mix3.bot.page.IndexPage;

public class Redirect extends WebPage {
	public Redirect() {
		if(!continueToOriginalDestination()){
			throw new RestartResponseException(IndexPage.class);
		}
	}
}
