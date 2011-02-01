package org.mix3.bot.page;

import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

import twitter4j.TwitterException;

@AuthorizeInstantiation({Roles.ADMIN})
public class PinPage extends AbstractWebPage{
	public PinPage(){
		add(new PinForm("pin"));
	}
	
	public class PinForm extends Form<String>
	{
		private static final long serialVersionUID = 1L;
		
		TextField<String> pin = new TextField<String>("pin", new Model<String>(new String()));
		
		public PinForm(String id) {
			super(id);
			add(pin);
		}
		
		@Override
		protected void onSubmit() {
			try {
				service.setAccessToken(pin.getModelObject());
			} catch (TwitterException e) {
				error(e.getMessage());
			}
			setResponsePage(AuthPage.class);
		}
	}
}
