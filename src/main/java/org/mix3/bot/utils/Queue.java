package org.mix3.bot.utils;

import java.util.ArrayList;
import java.util.List;

import org.mix3.datastore_map.CachedDatastoreMap;
import org.mix3.datastore_map.Updater;

public class Queue {
	private static CachedDatastoreMap<String, List<String>> queue =
		new CachedDatastoreMap<String, List<String>>("Queue");
	private static Queue instance = new Queue();
	
	private Queue(){
	}
	
	public static Queue getInstance(){
		if(queue.get("queue") == null){
			queue.put("queue", new ArrayList<String>());
		}
		return instance;
	}
	
	public void add(final String screenName){
		queue.update("queue", new Updater<List<String>>(){
			public List<String> update(List<String> object) {
				object.add(0, screenName);
				if(object.size() > 3){
					object.remove(3);
				}
				return object;
			}
		});
	}
	
	public boolean contains(String screenName){
		List<String> list = queue.get("queue");
		int count = 0;
		for(String s : list){
			if(s.equals(screenName)){
				count++;
			}
		}
		return count >= 2;
	}
}
