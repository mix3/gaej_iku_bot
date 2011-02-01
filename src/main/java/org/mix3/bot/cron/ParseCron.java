package org.mix3.bot.cron;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.mix3.bot.model.WeatherInfo;
import org.mix3.datastore_map.CachedDatastoreMap;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class ParseCron extends AbstractCron {
	@Override
	protected void run() {
		CachedDatastoreMap<String, WeatherInfo> weatherInfo = new CachedDatastoreMap<String, WeatherInfo>("WeatherInfo");
		WeatherInfo wi = weatherInfo.get("weatherInfo");
		if(wi == null){
			wi = new WeatherInfo();
		}
		
		@SuppressWarnings("static-access")
		URLFetchService ufs = new URLFetchServiceFactory().getURLFetchService();
		try {
			HTTPResponse res = ufs.fetch(new URL("http://tenki.jp/warn/"));
			String html = new String(res.getContent(), "UTF8");
			Source source = new Source(html);
			String date = source.getAllElementsByClass("dateRight").get(0).getContent().toString();
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
			wi.update(date, result);
			weatherInfo.put("weatherInfo", wi);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
