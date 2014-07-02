package com.codepath.apps.basictwitter.ui.fragments;

import android.os.Bundle;

import com.loopj.android.http.RequestParams;

public class UserTimelineFragment extends TweetsListFragment {
	private static final String LOG_TAG = UserTimelineFragment.class.getSimpleName();

	private String mScreenName;
	public void setScreenName(String name) {
		this.mScreenName = name;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RequestParams params = new RequestParams();
		params.put("screen_name", String.valueOf(mScreenName));
		mTweetFetcher.loadUserTweets(mScreenName);
	}
    
}
