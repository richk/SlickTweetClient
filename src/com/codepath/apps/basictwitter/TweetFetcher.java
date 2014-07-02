package com.codepath.apps.basictwitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TweetFetcher {
	private static final String LOG_TAG = TweetFetcher.class.getSimpleName();
	public static final int MAX_COUNT = 25;

	private TwitterRestClient mTwitterClient;
	private Context mContext;
	private TweetArrayAdapter mTweetArrayAdapter;
	private boolean moreMentionsAvailable = true;

	public TweetFetcher(Context context, TweetArrayAdapter adapter) {
		mTwitterClient = TwitterClientApp.getRestClient();
		mContext = context;
		mTweetArrayAdapter = adapter;
	}

	public synchronized void loadSavedTweets(final int type) {
		Log.d(LOG_TAG, "Loading saved tweets");
		new AsyncTask<Void, Void, List<Tweet>>() {

			@Override
			protected List<Tweet> doInBackground(Void... params) {
				Log.d(LOG_TAG, "Retrieving saved tweets from db");
				return Tweet.getAll(type);	
			}

			@Override
			protected void onPostExecute(List<Tweet> result) {
				Log.d(LOG_TAG, "Adding tweets from db to adapter. Number of tweets loaded:" + result.size());
				if (!result.isEmpty()) {
					mTweetArrayAdapter.addAll(result);
				}
			}
		}.execute();
	}

	public synchronized void loadNewTweets(final boolean pullToRefresh) {
		RequestParams params = new RequestParams();
		Log.d(LOG_TAG, "Loading New Tweets from Twitter API");
		if (pullToRefresh && !mTweetArrayAdapter.isEmpty()) {
			params.put("since_id", String.valueOf(mTweetArrayAdapter.getItem(0).getUid() + 1));
		} else {
			params = null;
		}
		Log.d(LOG_TAG, "Number of API calls:" + mTwitterClient.getApiCount());
		mTwitterClient.getHomeTimeline(params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray timelineArray) {
				Log.d(LOG_TAG, "Success. Number of results:" + timelineArray.length());
				List<Tweet> results = Tweet.fromJSONArray(timelineArray, Tweet.TWEET_TYPE.HOME.ordinal());
				if (pullToRefresh) {
					for(int i=results.size()-1;i>=0;--i) {
						Log.d(LOG_TAG, "Inserting tweet:" + results.get(i).toString());
						mTweetArrayAdapter.insert(results.get(i), 0);    	
					}
				} else {
					mTweetArrayAdapter.addAll(results);
				}
				mTweetArrayAdapter.notifyDataSetChanged();
			} 
			
			@Override
			public void onFailure(Throwable t, JSONObject errorObject) {
				Log.e(LOG_TAG, "Failure:" + errorObject.toString(), t);
				try {
					JSONArray errorArray = errorObject.getJSONArray("errors");
					Toast.makeText(mContext, errorArray.getJSONObject(0).getString("message"), Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					Log.e(LOG_TAG, "JSONException", e);
				}
			}
		});
	}
	
	public synchronized void loadNewMentions(final boolean pullToRefresh) {
		RequestParams params = new RequestParams();
		Log.d(LOG_TAG, "Loading New Mentions from Twitter API");
		if (pullToRefresh && !mTweetArrayAdapter.isEmpty()) {
			params.put("since_id", String.valueOf(mTweetArrayAdapter.getItem(0).getUid() + 1));
		} else {
			params = null;
		}
		Log.d(LOG_TAG, "Number of API calls:" + mTwitterClient.getApiCount());
		mTwitterClient.getMentionsTimeline(params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray timelineArray) {
				Log.d(LOG_TAG, "Success. Number of results:" + timelineArray.length());
				List<Tweet> results = Tweet.fromJSONArray(timelineArray, Tweet.TWEET_TYPE.MENTIONS.ordinal());
				if (pullToRefresh) {
					for(int i=results.size()-1;i>=0;--i) {
						Log.d(LOG_TAG, "Inserting tweet:" + results.get(i).toString());
						mTweetArrayAdapter.insert(results.get(i), 0);    	
					}
				} else {
					mTweetArrayAdapter.addAll(results);
				}
				mTweetArrayAdapter.notifyDataSetChanged();
			} 
			
			@Override
			public void onFailure(Throwable t, JSONObject errorObject) {
				Log.e(LOG_TAG, "Failure:" + errorObject.toString(), t);
				try {
					JSONArray errorArray = errorObject.getJSONArray("errors");
					Toast.makeText(mContext, errorArray.getJSONObject(0).getString("message"), Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					Log.e(LOG_TAG, "JSONException", e);
				}
			}
		});
	}
	
	public synchronized void loadMoreMentions() {
		Toast.makeText(mContext, "Scrolling-Adapter Size:" + mTweetArrayAdapter.getCount(), Toast.LENGTH_SHORT).show();
		Log.d(LOG_TAG, "loadMoreMentions(). Number of items already in mentions adapter=" + mTweetArrayAdapter.getCount());
		Log.d(LOG_TAG, "Since_id:" + mTweetArrayAdapter.getItem(0).getUid() + 
				" | Max_id:" + mTweetArrayAdapter.getItem(mTweetArrayAdapter.getCount()-1).getUid());
		RequestParams params = new RequestParams();
		Log.d(LOG_TAG, "Loading Mentions from Twitter API");
		if (!mTweetArrayAdapter.isEmpty()) {
			long max_id = mTweetArrayAdapter.getItem(mTweetArrayAdapter.getCount()-1).getUid() + 1;
			params.put("max_id", String.valueOf(max_id) + 1);
		} else {
			params = null;
		}
		Log.d(LOG_TAG, "Number of API calls:" + mTwitterClient.getApiCount());
		mTwitterClient.getMentionsTimeline(params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray timelineArray) {
				Log.d(LOG_TAG, "Success. Result:" + timelineArray.toString());
				List<Tweet> results = Tweet.fromJSONArray(timelineArray, Tweet.TWEET_TYPE.MENTIONS.ordinal());
				Log.d(LOG_TAG, "Number of tweets retrieved from mentions API:" + results.size());
				if (!results.isEmpty()) {
					mTweetArrayAdapter.addAll(results);
				}
			} 
			
			@Override
			public void onFailure(Throwable t, JSONObject errorObject) {
				Log.e(LOG_TAG, "Failure:" + errorObject.toString(), t);
				try {
					JSONArray errorArray = errorObject.getJSONArray("errors");
					Toast.makeText(mContext, errorArray.getJSONObject(0).getString("message"), Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					Log.e(LOG_TAG, "JSONException", e);
				}
			}
		});	
	}
	
	public synchronized void loadMoreTweets() {
		Toast.makeText(mContext, "Scrolling-Adapter Size:" + mTweetArrayAdapter.getCount(), Toast.LENGTH_SHORT).show();
		RequestParams params = new RequestParams();
		Log.d(LOG_TAG, "Loading Tweets from Twitter API");
		if (!mTweetArrayAdapter.isEmpty()) {
			long max_id = mTweetArrayAdapter.getItem(mTweetArrayAdapter.getCount()-1).getUid();
			params.put("max_id", String.valueOf(max_id));
		} 
		params.put("count", String.valueOf(MAX_COUNT));
		Log.d(LOG_TAG, "Number of API calls:" + mTwitterClient.getApiCount());
		mTwitterClient.getHomeTimeline(params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray timelineArray) {
				Log.d(LOG_TAG, "Success. Number of results:" + timelineArray.length());
				List<Tweet> results = Tweet.fromJSONArray(timelineArray, Tweet.TWEET_TYPE.HOME.ordinal());
				if (!results.isEmpty()) {
					mTweetArrayAdapter.addAll(results);
				}
			} 
			
			@Override
			public void onFailure(Throwable t, JSONObject errorObject) {
				Log.e(LOG_TAG, "Failure:" + errorObject.toString(), t);
				try {
					JSONArray errorArray = errorObject.getJSONArray("errors");
					Toast.makeText(mContext, errorArray.getJSONObject(0).getString("message"), Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					Log.e(LOG_TAG, "JSONException", e);
				}
			}
		});	
	}
	
	public void saveTweets(final TweetArrayAdapter tweetAdapter) {
		Log.d(LOG_TAG, "Saving tweets to db. Number of tweets to save:" + tweetAdapter.getCount());
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				for (int i=0;i<tweetAdapter.getCount();++i) {
					Tweet tweet = tweetAdapter.getItem(i);
					User user = tweet.getUser();
					user.save();
					tweet.save();
				}
				return null;	
			}
		}.execute();
	}
	
	public void loadUserTweets() {
		Log.d(LOG_TAG, "loadUserTimeline");
		mTwitterClient.getUserTimeline(null, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONArray result) {
				mTweetArrayAdapter.addAll(Tweet.fromJSONArray(result, Tweet.TWEET_TYPE.USER.ordinal()));
			}
		});
	}
	
	public void loadUserTweets(String screenName) {
		Log.d(LOG_TAG, "loadUserTimeline. Screen Name:" + screenName);
		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		mTwitterClient.getUserTimeline(params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONArray result) {
				mTweetArrayAdapter.addAll(Tweet.fromJSONArray(result, Tweet.TWEET_TYPE.USER.ordinal()));
			}
		});
	}
}
