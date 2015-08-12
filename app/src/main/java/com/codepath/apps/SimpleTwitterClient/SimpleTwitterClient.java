package com.codepath.apps.SimpleTwitterClient;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class SimpleTwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "e6dBQqN6XTDPqV2C5AP80mRLB";       // Change this
	public static final String REST_CONSUMER_SECRET = "xz1T0Us7J8qQBprwy0L8Cme3KCWjDTmRRlh3ces6cqjTQQ3KCA"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://simpleTwitterClient"; // Change this (here and in manifest)
	public static final String SIMPLE_TWITTER_CLIENT_DEV_TAG = "simpleTwitterClientDevTag";

	public SimpleTwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimelinePosts(int page,AsyncHttpResponseHandler handler){
		Log.d(SIMPLE_TWITTER_CLIENT_DEV_TAG, "getHomeTimelinePosts|page: " + page);
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count",25);
		params.put("page",page);
		getClient().get(apiUrl, params, handler);
	}

	public void getCurrentUserInfo(AsyncHttpResponseHandler handler){
		Log.d(SIMPLE_TWITTER_CLIENT_DEV_TAG, "getCurrentUserInfo");
		String apiUrl = getApiUrl("account/verify_credentials.json");
		getClient().get(apiUrl,handler);
	}

	public void postNewTweet(String text,AsyncHttpResponseHandler handler){
		Log.d(SIMPLE_TWITTER_CLIENT_DEV_TAG, "postNewTweet");
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status",text);
		getClient().post(apiUrl,params,handler);
	}
}