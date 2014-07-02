package com.codepath.apps.basictwitter;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.basictwitter.listeners.FragmentTabListener;
import com.codepath.apps.basictwitter.ui.fragments.HomeTimelineFragment;
import com.codepath.apps.basictwitter.ui.fragments.MentionsTimelineFragment;

public class TwitterTimelineActivity extends Activity {
	private static final String LOG_TAG = TwitterTimelineActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "onCreate");
		setContentView(R.layout.activity_twitter_timeline);
		setupTabs();
//		fragmentTweetsList.getListView().setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				Log.d(LOG_TAG, "onItemClicked");
//				Intent detailIntent = new Intent(getActivity(), TweetDetailsActivity.class);
//				detailIntent.putExtra("tweet", mTweetAdapter.getItem(position));
//				startActivity(detailIntent);
//			}
//		});
	}
	
	private void setupTabs() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        Tab homeTab = actionBar
            .newTab()
            .setText("Home")
            .setIcon(R.drawable.ic_home)
            .setTag("HomeTimelineFragment")
            .setTabListener(
                new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, "home",
                		HomeTimelineFragment.class));

        actionBar.addTab(homeTab);
        actionBar.selectTab(homeTab);

        Tab mentionsTab = actionBar
            .newTab()
            .setText("Mentions")
            .setIcon(R.drawable.ic_mentions)
            .setTag("MentionsTimelineFragment")
            .setTabListener(
                new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, "mentions",
                		MentionsTimelineFragment.class));

        actionBar.addTab(mentionsTab);
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
//		if (requestCode == 40 && resultCode == RESULT_OK) {
//			Tweet newTweet = (Tweet) data.getSerializableExtra("tweet");
//			fragmentTweetsList.insert(newTweet, 0);
//		}
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
//		fragmentTweetsList.saveTweets();
	}
	
	public void onProfileView(MenuItem menuItem) {
		Log.d(LOG_TAG, "Profile View Selected");
		Intent i = new Intent(this, ProfileActivity.class);
		i.putExtra("profileType", 0);
		startActivity(i);
	}
}
