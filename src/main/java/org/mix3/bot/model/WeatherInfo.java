package org.mix3.bot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class WeatherInfo implements Serializable{
	private String date = "";
	private List<String> weather = new ArrayList<String>();
	private boolean latest = false;
	
	public void update(String date, List<String> weather) {
		if(!this.getWeather().containsAll(weather) || !weather.containsAll(this.getWeather())){
			this.setDate(date);
			this.setWeather(weather);
			this.setLatest(true);
		}
	}
	
	public String getNew() {
		if(!this.isLatest()){
			return null;
		}
		
		String result = null;
		if(this.getWeather().isEmpty()){
			result = this.getDate() + " 雷注意報は出ていないようです。";
		}else{
			result = this.getDate() + " " + this.getWeather().toString() + " の地域で雷注意報が出ているようです。(via: http://tenki.jp/warn/)";
		}
		this.setLatest(false);
		
		return result;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<String> getWeather() {
		return weather;
	}
	public void setWeather(List<String> weather) {
		this.weather = weather;
	}
	public void setLatest(boolean latest) {
		this.latest = latest;
	}
	public boolean isLatest() {
		return latest;
	}
}
