package org.mix3.bot.task;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.Application;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebRequest;
import org.mix3.bot.WicketApplication;
import org.mix3.bot.page.IndexPage;
import org.mix3.bot.service.Service;
import org.mix3.bot.utils.Switch;

import com.google.inject.Inject;

public abstract class AbstractTask extends WebPage{
	@Inject
	protected Service service;
	
	protected boolean isLocalMode = ((WicketApplication)getApplication()).getConfigurationType().equals(Application.DEVELOPMENT);
	
	public AbstractTask(){
		this(new PageParameters());
	}
	
	public AbstractTask(PageParameters params){
		HttpServletRequest request = ((WebRequest) RequestCycle.get().getRequest()).getHttpServletRequest();
		if(!isLocalMode && request.getHeader("X-AppEngine-QueueName") == null){
			setRedirect(true);
			throw new RestartResponseException(IndexPage.class);
		}else{
			if(Switch.getInstance().isActive()){
				System.out.println("cron run.");
				run();
			}else{
				System.out.println("now is stop...");
			}
		}
	}
	
	protected abstract void run();
	
	protected abstract class Task {
		protected abstract String execute() throws Exception;
	}
}
