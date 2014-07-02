package com.codepath.apps.basictwitter.ui.fragments;

import android.os.Bundle;

public class CurrentUserTimelineFragment extends TweetsListFragment {
	private static final String LOG_TAG = CurrentUserTimelineFragment.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTweetFetcher.loadUserTweets();
	}

}
