package com.codepath.apps.basictwitter.ui.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TweetArrayAdapter;
import com.codepath.apps.basictwitter.TweetFetcher;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.utils.EndlessScrollListener;
import com.codepath.apps.basictwitter.utils.Utils;

import eu.erikw.PullToRefreshListView;

public class TweetsListFragment extends Fragment {
	private static final String LOG_TAG = TweetsListFragment.class.getSimpleName();
	
	protected PullToRefreshListView lvTweetsList;
	protected TweetArrayAdapter mTweetAdapter;
	
	protected TweetFetcher mTweetFetcher;
	protected ConnectivityManager mConnectivityManager;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTweetAdapter = new TweetArrayAdapter(getActivity(), new ArrayList<Tweet>());
		mTweetFetcher = new TweetFetcher(getActivity(), mTweetAdapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout
		View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		// Assign our view references
		lvTweetsList = (PullToRefreshListView) v.findViewById(R.id.lvTweets);
		lvTweetsList.setAdapter(mTweetAdapter);
		lvTweetsList.setOnScrollListener(new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
			    customLoadMore(page, totalItemsCount);	
			}
		});
		// Return the layout view
		return v;
	}
	
	public void customLoadMore(int page, int totalItemsCount) {
		Log.d(LOG_TAG, "customLoadMore::doNothing");
	}
	
	public void insert(Tweet tweet, int index) {
		mTweetAdapter.insert(tweet, index);
		mTweetAdapter.notifyDataSetChanged();
	}

	
	public void refreshComplete() {
		lvTweetsList.onRefreshComplete();
	}
	
	protected boolean isNetworkAvailable() {
		return Utils.isNetworkAvailable();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mTweetFetcher.saveTweets(mTweetAdapter);
	}
	
}
