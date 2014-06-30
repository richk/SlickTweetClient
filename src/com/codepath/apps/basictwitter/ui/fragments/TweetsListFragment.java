package com.codepath.apps.basictwitter.ui.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TweetArrayAdapter;
import com.codepath.apps.basictwitter.TweetFetcher;
import com.codepath.apps.basictwitter.models.Tweet;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TweetsListFragment extends Fragment {
	private static final String LOG_TAG = TweetsListFragment.class.getSimpleName();
	
	private PullToRefreshListView lvTweetsList;
	protected TweetArrayAdapter mTweetAdapter;
	
	private OnRefreshListener mRefreshListener;
	protected TweetFetcher mTweetFetcher;
	
	public interface OnEndlessScrollListener {
		public void onLoadMore(int page, int totalItemsCount);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mRefreshListener = (OnRefreshListener) activity;
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
		lvTweetsList.setOnRefreshListener(mRefreshListener);
		// Return the layout view
		return v;
	}
	
	public void insert(Tweet tweet, int index) {
		mTweetAdapter.insert(tweet, index);
		mTweetAdapter.notifyDataSetChanged();
	}

	
	public void refreshComplete() {
		lvTweetsList.onRefreshComplete();
	}
}
