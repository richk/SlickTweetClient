package com.codepath.apps.basictwitter.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class User implements Serializable {
	private static final String LOG_TAG = User.class.getSimpleName();
	
	private String name;
	private long uid;
	private String screenName;
	private String profileImageUrl;
	
	public static User fromJSON(JSONObject userObject) {
		User user = new User();
		try {
		user.name = userObject.getString("name");
		user.uid = userObject.getLong("id");
		user.screenName = userObject.getString("screen_name");
		user.profileImageUrl = userObject.getString("profile_image_url");
		} catch (JSONException je) {
			Log.e(LOG_TAG, "JSONException", je);
		}
		return user;
	}

	public String getName() {
		return name;
	}

	public long getUid() {
		return uid;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}
}
