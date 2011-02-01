package org.mix3.bot.auth;

import org.apache.wicket.Request;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class AuthSession extends AuthenticatedWebSession {
	public AuthSession(Request request) {
		super(request);
	}

	@Override
	public boolean authenticate(String arg0, String arg1) {
		return UserServiceFactory.getUserService().isUserLoggedIn();
	}

	@Override
	public Roles getRoles() {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if(user == null){
			return null;
		}else if(UserServiceFactory.getUserService().isUserAdmin()){
			return new Roles(Roles.ADMIN);
		}else{
			return new Roles(Roles.USER);
		}
	}
	
	protected String token;
	protected String secret;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
}
