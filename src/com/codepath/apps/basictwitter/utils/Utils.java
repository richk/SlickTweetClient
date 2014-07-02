package com.codepath.apps.basictwitter.utils;

import com.codepath.apps.basictwitter.TwitterClientApp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
	
    public static Boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) 
				TwitterClientApp.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
}
