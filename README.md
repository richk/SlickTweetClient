SlickTwitterClient
===============

Code path assignment - Twitter client for Android

The app is a client to use Twitter API to grab user's timeline and display it as a list.
The user can go as far into their timeline history as they want or pull to refresh their 
timeline with latest updates from others they are following.
Users can compose tweets and see them posted to their timeline.
In offline mode, users can see the tweets they loaded last in online mode.

Time Spent : 18 hours

* [x] User can sign in to Twitter using OAuth login
* [x] User can view the tweets from their home timeline
          ** User should be able to see the username, name, body and timestamp for each tweet
          ** User should be displayed the relative timestamp for a tweet "8m", "7h"
          ** User can view more tweets as they scroll with infinite pagination
          ** Links in tweets are clickable and will launch the web browser (see autolink)
* [x] User can compose a new tweet
          ** User can click a “Compose” icon in the Action Bar on the top right
          ** User can then enter a new tweet and post this to twitter
          ** User is taken back to home timeline with new tweet visible in timeline
          ** User can see a counter with total number of characters left for tweet
* [x] User can refresh tweets timeline by pulling down to refresh (i.e pull-to-refresh)
* [x] User can open the twitter app offline and see last loaded tweets
          ** Tweets are persisted into sqlite and can be displayed from the local DB 
* [x] The user interface and theme in the the app has "twitter branded" feel.

Walkthrough of all user stories:

![Video Walkthrough](anim_twitter.gif)
![Video Walkthrough](anim_twitter_1.gif)