package org.mix3.bot.page;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.mix3.bot.service.Service;

import com.google.inject.Inject;

public abstract class AbstractWebPage extends WebPage{
	@Inject
	protected Service service;
	
	public AbstractWebPage(){}
	public AbstractWebPage(PageParameters parameters){
		super(parameters);
	}
}
