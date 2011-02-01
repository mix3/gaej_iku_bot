package org.mix3.bot.page;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mix3.bot.service.Service;
import org.mix3.bot.service.ServiceImpl;

import twitter4j.http.RequestToken;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

public class ServiceTest {
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	
	private Service service;
	@Before
	public void setUp() {
		helper.setUp();
		
		Injector injector = Guice.createInjector(new AbstractModule(){
			@Override
			protected void configure() {
				bind(Service.class).to(ServiceImpl.class).in(Singleton.class);
			}
		});
		service = injector.getInstance(Service.class);
	}
	
	@After
	public void tearDown(){
		helper.tearDown();
	}
	
	@Test
	public void token() {
		service.setToken("token", "secret");
		RequestToken requestToken = service.getRequestToken();
		Assert.assertTrue(requestToken.getToken().equals("token"));
		Assert.assertTrue(requestToken.getTokenSecret().equals("secret"));
		
		service.clearToken();
		requestToken = service.getRequestToken();
		Assert.assertTrue(requestToken.getToken().equals(""));
		Assert.assertTrue(requestToken.getTokenSecret().equals(""));
	}
}
