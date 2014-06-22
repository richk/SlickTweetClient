package com.codepath.apps.basictwitter.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateUtils;
import android.util.Log;

public class Tweet implements Serializable {
	private static final String LOG_TAG = Tweet.class.getSimpleName();
	private String body;
	private long uid;
	private String createdAt;
	private int retweetCount;
	private boolean retweeted;
	private boolean favorited;
	private int favoriteCount;
	private User user;
	
	public static Tweet fromJSON(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		// Extract json to polupate member variables
		try {
		    tweet.body = jsonObject.getString("text");
		    tweet.uid = jsonObject.getLong("id");
		    tweet.createdAt = jsonObject.getString("created_at");
		    tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
		    tweet.favorited = jsonObject.getBoolean("favorited");
		    if (tweet.favorited) {
		    	tweet.favoriteCount = jsonObject.getInt("favorite_count");
		    }
		    tweet.retweetCount = jsonObject.getInt("retweet_count");
		    tweet.retweeted = jsonObject.getBoolean("retweeted");
		} catch (JSONException je) {
			Log.e("Error", "JSONException", je);
		}
		return tweet;
	}
	
	public static ArrayList<Tweet> fromJSONArray(JSONArray tweetArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		for (int i=0;i<tweetArray.length();++i) {
	    	JSONObject tweetObject = null;
			try {
				tweetObject = (JSONObject) tweetArray.get(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	Tweet tweet = Tweet.fromJSON(tweetObject);
	    	if (tweet != null) {
	    	    tweets.add(tweet);
	    	}
	    }
		return tweets;
	}

	public String getBody() {
		return body;
	}

	public long getUid() {
		return uid;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public User getUser() {
		return user;
	}
	
	public int getRetweetCount() {
		return retweetCount;
	}

	public boolean isRetweeted() {
		return retweeted;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}

	
	public String getRelativeTimeAgo() {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);
	 
		String relativeDate = "";
		try {
			long dateMillis = sf.parse(createdAt).getTime();
			relativeDate = getTimeAbbrev(DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	 
		return relativeDate;
	}
	
	public String getTimeAbbrev(String relativeTimeSpan) {
		String[] timeComps = relativeTimeSpan.split("\\s");
		if (timeComps == null || timeComps.length < 3) {
			return relativeTimeSpan;
		} else {
			if ("seconds".equals(timeComps[1]) || "second".equals(timeComps[1])) {
				return (timeComps[0] + "s"); 
			} else if ("minutes".equals(timeComps[1]) || "minute".equals(timeComps[1])) {
				return (timeComps[0] + "m");
			} else if ("hours".equals(timeComps[1]) || "hour".equals(timeComps[1])) {
				return (timeComps[0] + "h");
			} else if ("days".equals(timeComps[1]) || "day".equals(timeComps[1])) {
				return (timeComps[0] + "d");
			} else if ("weeks".equals(timeComps[1]) || "week".equals(timeComps[1])) {
				return (timeComps[0] + "w");
			} else if ("months".equals(timeComps[1]) || "month".equals(timeComps[1])) {
				return (timeComps[0] + "months");
			} else if ("years".equals(timeComps[1]) || "year".equals(timeComps[1])) {
				return (timeComps[0] + "y");
			} else {
				return relativeTimeSpan;
			}
		}
		
	}
	
	@Override
	public String toString() {
		return getBody() + "-" + getUser().getScreenName();
	}
}
