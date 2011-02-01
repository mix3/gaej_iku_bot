package org.mix3.bot.service;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;

import com.google.inject.ImplementedBy;

@ImplementedBy(ServiceImpl.class)
public interface Service {
	public RequestToken getRequestToken();
	public AccessToken getAccessToken();
	public void setToken(String token, String secret);
	public void setAccessToken(String pin) throws TwitterException;
	public void clearToken();
	
	public Twitter getTwitter();
	public Twitter getTwitter(RequestToken requestToken) throws TwitterException;
	public Twitter getTwitter(AccessToken accessToken) throws TwitterException;
	
	public void delAllPost() throws TwitterException;
	public void runPost(String post) throws TwitterException;
}
