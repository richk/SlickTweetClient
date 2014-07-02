package com.codepath.apps.basictwitter.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.basictwitter.TweetFetcher;
import com.codepath.apps.basictwitter.models.Tweet;

import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class HomeTimelineFragment extends TweetsListFragment {
	private static final String LOG_TAG = HomeTimelineFragment.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		mTweetFetcher.loadNewTweets(false);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		lvTweetsList.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				if (isNetworkAvailable()) {
					mTweetFetcher.loadNewTweets(true);
				}
				lvTweetsList.onRefreshComplete();
			}
		});	
//		if (!isNetworkAvailable()) {
//			mTweetFetcher.loadSavedTweets(Tweet.TWEET_TYPE.HOME.ordinal());
//		}
		
		return v;
	}
	
	public void customLoadMore(int page, int totalItemsCount) {
		Log.d(LOG_TAG, "customLoadMore::HomeTimelineFragment");
		mTweetFetcher.loadMoreTweets();
	}
}
