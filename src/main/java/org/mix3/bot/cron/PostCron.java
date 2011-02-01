package org.mix3.bot.cron;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.mix3.bot.model.WeatherInfo;
import org.mix3.datastore_map.CachedDatastoreMap;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public class PostCron extends AbstractCron {
	@Override
	protected void run() {
		CachedDatastoreMap<String, WeatherInfo> weatherInfo = new CachedDatastoreMap<String, WeatherInfo>("WeatherInfo");
		WeatherInfo wi = weatherInfo.get("weatherInfo");
		String result = null;
		if(wi != null && (result = wi.getNew()) != null){
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("JST"));
	        cal.setTime(new Date());
	        int h = cal.get(Calendar.HOUR_OF_DAY);
			if(3 <= h && h < 6){
				result = result + " ﾑﾆｬﾑﾆｬ…";
			}
			try {
				Twitter twitter = service.getTwitter(service.getAccessToken());
				if(!isLocalMode){
					twitter.updateStatus(result);
				}
				System.out.println(result);
				weatherInfo.put("weatherInfo", wi);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
	}
}
