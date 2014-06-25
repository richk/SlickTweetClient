package com.codepath.apps.basictwitter;

import java.util.List;

import com.codepath.apps.basictwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {
	private static final String LOG_TAG = TweetArrayAdapter.class.getSimpleName();
	
	public TweetArrayAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}
	
	private static class ViewHolder {
		ImageView profileImage;
		TextView tweetBody;
		TextView name;
		TextView screenName;
		TextView retweetCounter;
		TextView starCounter;
		TextView time;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    Tweet tweet = getItem(position);
	    ViewHolder viewHolder = null;
	    if (convertView == null) {
	    	LayoutInflater inflater = LayoutInflater.from(getContext());
	    	convertView = inflater.inflate(R.layout.tweet_item, parent, false);
	    	viewHolder = new ViewHolder();
	    	viewHolder.profileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
	    	viewHolder.tweetBody = (TextView) convertView.findViewById(R.id.tvBody);
	    	viewHolder.name = (TextView) convertView.findViewById(R.id.tvFullName);
	    	viewHolder.screenName = (TextView) convertView.findViewById(R.id.tvScreenName);
	    	viewHolder.retweetCounter = (TextView) convertView.findViewById(R.id.tvRetweetCounter);
	    	viewHolder.starCounter = (TextView) convertView.findViewById(R.id.tvStarCounter);
	    	viewHolder.time = (TextView) convertView.findViewById(R.id.tvTime);
	    	convertView.setTag(viewHolder);
	    } else {
	    	viewHolder = (ViewHolder) convertView.getTag();
	    }
	    viewHolder.profileImage.setImageResource(android.R.color.transparent);	
	    ImageLoader imageLoader = ImageLoader.getInstance();
	    if (!imageLoader.isInited()) {
	    	Log.d(LOG_TAG, "Initializing Image Loader Again");
	    	imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
	    }
	    imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), viewHolder.profileImage);
	    viewHolder.tweetBody.setText(tweet.getBody());
	    viewHolder.screenName.setText("@" + tweet.getUser().getScreenName());
	    viewHolder.name.setText(tweet.getUser().getName());
	    viewHolder.time.setText(tweet.getRelativeTimeAgo());
	    viewHolder.retweetCounter.setText(String.valueOf(tweet.getRetweetCount()));
	    if (tweet.isFavorited()) {
	    	viewHolder.starCounter.setText(String.valueOf(tweet.getFavoriteCount()));
	    }
	    return convertView;
	}

}
