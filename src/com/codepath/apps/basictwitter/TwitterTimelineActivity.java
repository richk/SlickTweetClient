package com.codepath.apps.basictwitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.util.Log;

import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.codepath.apps.basictwitter.utils.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import eu.erikw.PullToRefreshListView;

public class TwitterTimelineActivity extends Activity {
	private static final String LOG_TAG = TwitterTimelineActivity.class.getSimpleName();

	private TweetFetcher mTweetFetcher;
	private TweetArrayAdapter tweetAdapter;
	private PullToRefreshListView lvTweets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "onCreate");
		setContentView(R.layout.activity_twitter_timeline);
		lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
		tweetAdapter = new TweetArrayAdapter(this, new ArrayList<Tweet>());
		lvTweets.setAdapter(tweetAdapter);
		mTweetFetcher = new TweetFetcher(this, tweetAdapter);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				Log.d(LOG_TAG, "onLoadMore.Page:" + page + " | TotalItemsCount:" + totalItemsCount);
				customLoadMoreDataFromApi(totalItemsCount);
			}
		});
		lvTweets.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (isNetworkAvailable()) {
					mTweetFetcher.loadNewTweets();
				}
				lvTweets.onRefreshComplete();
			}
		});
		lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.d(LOG_TAG, "onItemClicked");
				Intent detailIntent = new Intent(getApplicationContext(), TweetDetailsActivity.class);
				detailIntent.putExtra("tweet", tweetAdapter.getItem(position));
				startActivity(detailIntent);
			}
		});
		if (!isNetworkAvailable()) {
			mTweetFetcher.loadSavedTweets();
		} 
	}

	public void customLoadMoreDataFromApi(int itemsCount) {
		if (isNetworkAvailable()) {
			mTweetFetcher.loadMoreTweets();
		}
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mTweetFetcher.saveTweets(tweetAdapter);
	}

	private Boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
	
	
}
