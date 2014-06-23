package com.codepath.apps.basictwitter;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.util.Log;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.utils.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;

public class TwitterTimelineActivity extends Activity {
	private static final String LOG_TAG = TwitterTimelineActivity.class.getSimpleName();
	private static final int MAX_COUNT = 25;
	
	private TwitterRestClient mTwitterClient;
//	private ArrayList<Tweet> tweets;
	private TweetArrayAdapter tweetAdapter;
	private PullToRefreshListView	 lvTweets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "onCreate");
		setContentView(R.layout.activity_twitter_timeline);
		mTwitterClient = TwitterClientApp.getRestClient();
		lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
		tweetAdapter = new TweetArrayAdapter(this, new ArrayList<Tweet>());
		lvTweets.setAdapter(tweetAdapter);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				customLoadMoreDataFromApi(totalItemsCount); 
			}
		});
		lvTweets.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list contents
                // Make sure you call listView.onRefreshComplete()
                // once the loading is done. This can be done from here or any
                // place such as when the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
		newTimeline();
	}
	
	public void fetchTimelineAsync(int page) {
		Log.d(LOG_TAG, "Pull To Refresh");
		mTwitterClient.getHomeTimeline(MAX_COUNT, 0, new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray timelineArray) {
            	Log.d(LOG_TAG, "fetchTimelineAsync::Success:" + timelineArray.toString());
            	tweetAdapter.clear();
            	tweetAdapter.addAll(Tweet.fromJSONArray(timelineArray));
                lvTweets.onRefreshComplete();
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        });
    }
	
	// Append more data into the adapter
    public void customLoadMoreDataFromApi(int itemsCount) {
    	if (!tweetAdapter.isEmpty()) {
    		Tweet tweetItem = tweetAdapter.getItem(tweetAdapter.getCount() - 1);
    		populateTimeline(tweetItem.getUid());
    	}
    }	
    
    private void newTimeline() {
    	tweetAdapter.clear();
        populateTimeline(-1);	
    }
	
	private void populateTimeline(long l) {
	    mTwitterClient.getHomeTimeline(MAX_COUNT, l, new JsonHttpResponseHandler() {
	    	@Override
	    	public void onSuccess(JSONArray timelineArray) {
	    	    Log.d(LOG_TAG, "Success:" + timelineArray.toString());
	    	    tweetAdapter.addAll(Tweet.fromJSONArray(timelineArray));
	    	    long lastID = tweetAdapter.getItem(tweetAdapter.getCount() - 1).getUid();
	    	}
	    	
	    	@Override
	    	public void onFailure(Throwable e, String s) {
	    		Log.e(LOG_TAG, "Failure:" + s, e);
	    	}
	    	
	    	@Override
	    	public void handleFailureMessage(Throwable e, String responseBody) {
	    		Log.e(LOG_TAG, "Failure:" + responseBody, e);	
	    	}
	    });	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.twitter_timeline, menu);
		return true;
	}
	
	public void onComposeAction(MenuItem mv) {
		Intent intent = new Intent(this, TweetActivity.class);
		startActivityForResult(intent, 40);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 40 && resultCode == RESULT_OK) {
			Tweet newTweet = (Tweet) data.getSerializableExtra("tweet");
			tweetAdapter.insert(newTweet, 0);
			tweetAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
