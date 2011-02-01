package org.mix3.bot.page;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class WeatherInfoTest {
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	
	@Before
	public void setUp() {
		helper.setUp();
	}
	
	@After
	public void tearDown(){
		helper.tearDown();
	}
	
	@Test
	public void test() {
		List<String> list1 = Arrays.asList("テスト１", "テスト２", "テスト３");
		List<String> list2 = Arrays.asList("テスト３", "テスト２", "テスト１");
		List<String> list3 = Arrays.asList("テスト１", "テスト２");
		List<String> list4 = Arrays.asList("テスト１", "テスト２", "テスト３", "テスト３");
		
		Assert.assertTrue(list1.containsAll(list2));
		Assert.assertTrue(list1.containsAll(list3));
		Assert.assertFalse(list3.containsAll(list1));
		Assert.assertTrue(list1.containsAll(list4));
	}
}
