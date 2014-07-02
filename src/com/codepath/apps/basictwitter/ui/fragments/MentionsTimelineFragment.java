package com.codepath.apps.basictwitter.ui.fragments;

import com.codepath.apps.basictwitter.models.Tweet;

import eu.erikw.PullToRefreshListView.OnRefreshListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MentionsTimelineFragment extends TweetsListFragment {
	private static final String LOG_TAG = MentionsTimelineFragment.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		mTweetFetcher.loadNewMentions(false);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		lvTweetsList.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				if (isNetworkAvailable()) {
					mTweetFetcher.loadNewMentions(true);
				}
				lvTweetsList.onRefreshComplete();
			}
		});		
//		if (!isNetworkAvailable()) {
//			mTweetFetcher.loadSavedTweets(Tweet.TWEET_TYPE.MENTIONS.ordinal());
//		}
		return v;
	}
	
	public void customLoadMore(int page, int totalItemsCount) {
		Log.d(LOG_TAG, "customLoadMore::MentionsTimelineFragment");
		mTweetFetcher.loadMoreMentions();
	}
	
//	
	public void saveTweets() {
		mTweetFetcher.saveTweets(mTweetAdapter);
	}

}
