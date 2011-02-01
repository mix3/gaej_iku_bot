package org.mix3.bot.page;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class FetchTest {
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper();
	
	@Before
	public void setUp() {
		helper.setUp();
	}
	
	@After
	public void tearDown(){
		helper.tearDown();
	}
	
	@Test
	public void fetchTest() {
		URLFetchService ufs = URLFetchServiceFactory.getURLFetchService();
		try {
			HTTPResponse res = ufs.fetch(new URL("http://tenki.jp/warn/"));
			String html = new String(res.getContent());
			Source source = new Source(html);
			Element wrap = source.getElementById("wrap_announcingTable");
			int flag = 0;
			List<String> result = new ArrayList<String>();
			for(Element element : wrap.getAllElements()){
				if(flag == 1){
					for(Element a : element.getAllElements("a")){
						result.add(a.getContent().toString());
					}
					flag = 0;
				}
				if(element.getAttributeValue("abbr") != null && element.getAttributeValue("abbr").equals("雷")){
					flag = 1;
				}
			}
			
			System.out.println(source.getAllElementsByClass("dateRight").get(0).getContent().toString());
			System.out.println(result.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
