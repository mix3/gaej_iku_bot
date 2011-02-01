package org.mix3.bot.page;

import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.mix3.bot.utils.Switch;

import twitter4j.TwitterException;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@AuthorizeInstantiation({Roles.ADMIN})
public class IndexPage  extends AbstractWebPage {
	@SuppressWarnings("serial")
	public IndexPage() {
    	Switch s = Switch.getInstance();
    	String value;
    	if(s.isActive()){
    		value = "稼動中";
    	}else{
    		value = "停止中";
    	}
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		add(new Label("label", user.getNickname()));
		add(new ExternalLink("logout", userService.createLogoutURL("/home")));
		
		add(new BookmarkablePageLink<Void>("auth", AuthPage.class));
		add(new BookmarkablePageLink<Void>("pin", PinPage.class));
		
		add(new Form<Void>("switch"){
		}.add(new Button("switch"){
			@Override
			public void onSubmit() {
				Boolean result = Switch.getInstance().switchActive();
				System.out.println(result);
				setResponsePage(IndexPage.class);
			}
		}.add(new SimpleAttributeModifier("value", value))));
    	
		add(new Form<Void>("delform"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				try {
					service.delAllPost();
				} catch (TwitterException e) {
					error(e.getMessage());
				}
				setResponsePage(AuthPage.class);
			}
		});
		
		add(new Form<Void>("postcheck"){
			@Override
			public void onSubmit() {
				setResponsePage(PostCheckPage.class);
			}
		});
		
		add(new Form<Void>("runpost"){
			@Override
			public void onSubmit() {
				setResponsePage(RunPostPage.class);
			}
		});
	}
}
