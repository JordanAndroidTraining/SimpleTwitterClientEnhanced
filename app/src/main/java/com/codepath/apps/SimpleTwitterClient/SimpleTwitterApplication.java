package com.codepath.apps.SimpleTwitterClient;

import android.content.Context;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     SimpleTwitterClient client = SimpleTwitterApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class SimpleTwitterApplication extends com.activeandroid.app.Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		SimpleTwitterApplication.context = this;
	}

	public static SimpleTwitterClient getRestClient() {
		return (SimpleTwitterClient) SimpleTwitterClient.getInstance(SimpleTwitterClient.class, SimpleTwitterApplication.context);
	}
}