package com.codepath.apps.basictwitter.ui.fragments;

import android.os.Bundle;

import com.codepath.apps.basictwitter.TweetFetcher;

public class HomeTimelineFragment extends TweetsListFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTweetFetcher.loadNewTweets(false);
	}
	
//	
	public void saveTweets() {
		mTweetFetcher.saveTweets(mTweetAdapter);
	}
}
