package org.mix3.bot.cron;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.mix3.bot.utils.Queue;
import org.mix3.datastore_map.CachedDatastoreMap;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class RepliesCron extends AbstractCron{
	@Override
	protected void run() {
		CachedDatastoreMap<String, Long> sinceId = new CachedDatastoreMap<String, Long>("SinceID");
		try{
			Twitter twitter = service.getTwitter(service.getAccessToken());
			List<Status> statusList = new ArrayList<Status>();
			if(sinceId.get("replies") != null){
				statusList = twitter.getMentions(new Paging(sinceId.get("replies")));
			}else{
				statusList = twitter.getMentions();
				if(statusList.size() > 0){
					sinceId.put("replies", statusList.get(0).getId());
					statusList = new ArrayList<Status>();
				}
			}
			for(Status s : statusList){
				Replies replies = resolve(twitter, s.getUser().getScreenName(), s.getText(), s.getId());
				if(replies != null){
					System.out.println(replies.execute());
				}
			}
			if(statusList.size() > 0){
				sinceId.put("replies", statusList.get(0).getId());
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
	
	private Replies resolve(final Twitter twitter, final String screenName, String text, final long statusId){
		if(text.matches("^.*(RT|QT) @[a-zA-Z0-9_]*:.*$")){
			// RT、QTはスルー
			System.out.println("RT.");
			return null;
		}
		
		if(Queue.getInstance().contains(screenName)){
			// 連続でﾘﾌﾟﾗｲした相手もスルー
			System.out.println("Chain Replies.");
			return null;
		}
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
        cal.setTime(new Date());
        int h = cal.get(Calendar.HOUR_OF_DAY);
        System.out.println(h + "時");
		if(3 <= h && h < 6){
			// 夜は寝てる
			return null;
		}
		
		if(text.contains("followして") || 
					text.contains("Followして") ||
					text.contains("フォローして")){
			String post = "@"+screenName+" フォローですか。承知致しました。";
			return new Replies(twitter, post, statusId, screenName, CREATE_FRIENDSHIP);
		}else if(text.contains("removeして") || 
					text.contains("Removeして") ||
					text.contains("リムーブして")){
			String post = "@"+screenName+" リムーブですね。承知致しました。　…少々寂しいですね。";
			return new Replies(twitter, post, statusId, screenName, DESTROY_FRIENDSHIP);
		}else if(text.contains("こんにちは") ||
					text.contains("こんにちわ") ||
					text.contains("コンニチワ") ||
					text.contains("コンニチハ") ||
					text.contains("こんばんは") ||
					text.contains("こんばんわ") ||
					text.contains("コンバンワ") ||
					text.contains("コンバンハ") ||
					text.contains("おはよう")   ||
					text.contains("オハヨウ")
				){
			String post = "@"+screenName+" どうも、こんにちは。";
	        if(6 <= h && h < 11){
	        	// 朝
	        	post = "@"+screenName+" おはようございます。";
	        }else if(11 <= h && h < 6){
	        	// 昼
	        	post = "@"+screenName+" どうも、こんにちは。";
	        }else if(6 <= h && h < 12){
	        	// 夜
	        	post = "@"+screenName+" こんばんわー。";
	        }else if(0 <= h && h < 3){
	        	// 深夜
	        	post = "@"+screenName+" こんばんわー。そろそろオヤスミの時間ですね。あわわ…";
	        }
			return new Replies(twitter, post, statusId, screenName);
		}else if(text.contains("可愛い") || 
				text.contains("かわいい") ||
				text.contains("カワイイ") ||
				text.contains("好きだよ") ||
				text.contains("スキだよ") ||
				text.contains("スキダヨ") ||
				text.contains("愛してる") ||
				text.contains("アイしてる") ||
				text.contains("アイシテル")){
			String post = "@"+screenName+" あ、えっと…　なにがなんだか、そのー、えっと…";
			return new Replies(twitter, post, statusId, screenName);
		}else if(text.contains("ｷｬｰｲｸｻｰﾝ")){
			String post = "@"+screenName+" ｷｭﾋﾟｰﾝ!!";
			return new Replies(twitter, post, statusId, screenName);
		}else{
			String post;
			switch((int)(Math.random() * 10)){
				case 4 :
					post = "@"+screenName+" はて、何か御用ですか？";
					return new Replies(twitter, post, statusId, screenName);
				case 5 :
					post = "@"+screenName+" 私を呼びましたか？";
					return new Replies(twitter, post, statusId, screenName);
				case 6 :
					post = "@"+screenName+" ？";
					return new Replies(twitter, post, statusId, screenName);
				default :
					return null;
			}
		}
	}
	
	private static final int REPLIES = 0;
	private static final int CREATE_FRIENDSHIP = 1;
	private static final int DESTROY_FRIENDSHIP = 2;
	
	private class Replies {
		private Twitter twitter;
		private String post;
		private long id;
		private int kind;
		private String screenName;
		
		public Replies(Twitter twitter, String post, long id, String screenName){
			this.twitter = twitter;
			this.post = post;
			this.id = id;
			this.kind = REPLIES;
			this.screenName = screenName;
		}
		
		public Replies(Twitter twitter, String post, long id, String screenName, int kind){
			this.twitter = twitter;
			this.post = post;
			this.id = id;
			this.kind = kind;
			this.screenName = screenName;
		}
		
		protected String execute() throws TwitterException {
			if(!isLocalMode){
				twitter.updateStatus(post, id);
			}
			switch(kind){
				case CREATE_FRIENDSHIP :
					if(!isLocalMode){
						twitter.createFriendship(screenName);
					}
					break;
				case DESTROY_FRIENDSHIP :
					if(!isLocalMode){
						twitter.destroyFriendship(screenName);
					}
					break;
				default :
					break;
			}
			Queue.getInstance().add(screenName);
			return post;
		}
	}
}
