package com.codepath.apps.basictwitter.ui.fragments;

import android.os.Bundle;

public class MentionsTimelineFragment extends TweetsListFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTweetFetcher.loadNewMentions(false);
	}
	
//	
	public void saveTweets() {
		mTweetFetcher.saveTweets(mTweetAdapter);
	}

}
