package org.mix3.bot.service;

import java.util.List;

import org.mix3.bot.utils.Utils;
import org.mix3.datastore_map.CachedDatastoreMap;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;

import com.google.inject.Singleton;

@Singleton
public class ServiceImpl implements Service{
	CachedDatastoreMap<String, String> _token = new CachedDatastoreMap<String, String>("Token");
	
	private String getProperty(String key) {
		return Utils.getTwitterProperties().getProperty(key);
	}
	
	@Override
	public Twitter getTwitter() {
		Twitter twitter =  new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(getProperty("consumer.key"), getProperty("consumer.secret"));
		return twitter;
	}
	
	@Override
	public Twitter getTwitter(RequestToken requestToken) throws TwitterException {
		Twitter twitter = getTwitter();
		AccessToken accessToken = twitter.getOAuthAccessToken(requestToken);
		setToken(accessToken.getToken(), accessToken.getTokenSecret());
		twitter.verifyCredentials();
		return twitter;
	}
	
	@Override
	public Twitter getTwitter(AccessToken accessToken) throws TwitterException {
		Twitter twitter = getTwitter();
		twitter.setOAuthAccessToken(accessToken);
		twitter.verifyCredentials();
		return twitter;
	}
	
	@Override
	public RequestToken getRequestToken() {
		if(_token.get("token") == null || _token.get("secret") == null) {
			return new RequestToken("", "");
		}
		return new RequestToken(_token.get("token"), _token.get("secret"));
	}
	
	@Override
	public void setAccessToken(String pin) throws TwitterException {
		Twitter twitter = getTwitter();
		AccessToken token = twitter.getOAuthAccessToken(getRequestToken(), pin);
		setToken(token.getToken(), token.getTokenSecret());
	}
	
	@Override
	public AccessToken getAccessToken() {
		if(_token.get("token") == null || _token.get("secret") == null) {
			return new AccessToken("", "");
		}
		return new AccessToken(_token.get("token"), _token.get("secret"));
	}
	
	@Override
	public void setToken(String token, String secret) {
		_token.put("token", token);
		_token.put("secret", secret);
	}
	
	@Override
	public void clearToken() {
		_token.clear();
	}

	@Override
	public void delAllPost() throws TwitterException {
		Twitter twitter = getTwitter(getAccessToken());
		List<Status> list = statusList(twitter, twitter.getUserTimeline());
		for(Status s : list){
			try {
				twitter.destroyStatus(s.getId());
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
	}
	
	private List<Status> statusList(Twitter twitter, List<Status> list) throws TwitterException {
		if(list.size() > 0){
			Paging paging = new Paging(list.get(list.size() - 1).getId());
			List<Status> _list = statusList(twitter, twitter.getUserTimeline(paging));
			list.addAll(statusList(twitter, _list));
		}
		return list;
	}
	
	@Override
	public void runPost(String post) throws TwitterException {
		Twitter twitter = getTwitter(getAccessToken());
		twitter.updateStatus(post);
	}
}
