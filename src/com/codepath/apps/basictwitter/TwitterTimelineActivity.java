package com.codepath.apps.basictwitter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.util.Log;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.utils.EndlessScrollListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Build;

public class TwitterTimelineActivity extends Activity {
	private static final String LOG_TAG = TwitterTimelineActivity.class.getSimpleName();
	private static final int MAX_COUNT = 25;
	
	private TwitterRestClient mTwitterClient;
//	private ArrayList<Tweet> tweets;
	private TweetArrayAdapter tweetAdapter;
	private ListView lvTweets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "onCreate");
		setContentView(R.layout.activity_twitter_timeline);
		mTwitterClient = TwitterClientApp.getRestClient();
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		tweetAdapter = new TweetArrayAdapter(this, new ArrayList<Tweet>());
		lvTweets.setAdapter(tweetAdapter);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				Toast.makeText(getApplicationContext(), "Page:" + String.valueOf(page) + "," + "TotalItemsCount:" + String.valueOf(totalItemsCount), 
						Toast.LENGTH_SHORT).show();
				customLoadMoreDataFromApi(totalItemsCount); 
			}
		});
		newTimeline();
	}
	
	// Append more data into the adapter
    public void customLoadMoreDataFromApi(int itemsCount) {
    	Toast.makeText(this, String.valueOf(itemsCount), Toast.LENGTH_SHORT).show();
        Tweet tweetItem = tweetAdapter.getItem(itemsCount - 1);
        populateTimeline(tweetItem.getUid());
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
	    	    Toast.makeText(getApplicationContext(), "Last ID:" + String.valueOf(lastID), Toast.LENGTH_SHORT).show();
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
