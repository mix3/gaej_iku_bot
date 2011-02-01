package org.mix3.bot.utils;

import org.mix3.datastore_map.CachedDatastoreMap;
import org.mix3.datastore_map.Updater;

public class Switch {
	private static CachedDatastoreMap<String, Boolean> switcher =
		new CachedDatastoreMap<String, Boolean>("Activity");
	private static Switch instance = new Switch();
	
	private Switch(){}
	
	public static Switch getInstance(){
		if(switcher.get("state") == null){
			switcher.put("state", false);
		}
		return instance;
	}
	
	public boolean isActive(){
		return switcher.get("state");
	}
	
	public boolean switchActive(){
		switcher.update("state", new Updater<Boolean>() {
			@Override
			protected Boolean update(Boolean object) {
				return !object;
			}
		});
		return switcher.get("state");
	}
}
