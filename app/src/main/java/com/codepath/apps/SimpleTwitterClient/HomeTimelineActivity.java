package com.codepath.apps.SimpleTwitterClient;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class HomeTimelineActivity extends ActionBarActivity {

    public static final String HOME_TIMELINE_ACTIVITY_DEV_TAG = "HomeTimelineActivityDevTag";
    private SimpleTwitterClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_timeline);
        Log.d(HOME_TIMELINE_ACTIVITY_DEV_TAG, "HAHAHAGGG~~");
        mClient = SimpleTwitterApplication.getRestClient();
        renderTimeline();
    }


    public void renderTimeline(){
        mClient.getHomeTimelinePosts(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d(HOME_TIMELINE_ACTIVITY_DEV_TAG,"onSuccess()");
                //super.onSuccess(statusCode, headers, response);
                Log.d(HOME_TIMELINE_ACTIVITY_DEV_TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //super.onFailure(statusCode, headers, responseString, throwable);
                Log.d(HOME_TIMELINE_ACTIVITY_DEV_TAG, "renderTimeline()|FAILED");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
