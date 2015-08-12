package com.codepath.apps.SimpleTwitterClient.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.SimpleTwitterClient.Adapters.HomeTimelineAdapter;
import com.codepath.apps.SimpleTwitterClient.R;
import com.codepath.apps.SimpleTwitterClient.SimpleTwitterApplication;
import com.codepath.apps.SimpleTwitterClient.SimpleTwitterClient;
import com.codepath.apps.SimpleTwitterClient.Utils.GeneralUtils;
import com.codepath.apps.SimpleTwitterClient.models.TweetModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;

public class HomeTimelineActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {

    public static final String HOME_TIMELINE_ACTIVITY_DEV_TAG = "HomeTimelineActivityDevTag";
    public static final int COMPOSE_REQUEST_CODE = 999;
    private SimpleTwitterClient mClient;
    private HomeTimelineAdapter mAdapter;
    private ArrayList<TweetModel> mTweetList;
    private ListView mHomeTimelineContainerLv;
    private SwipeRefreshLayout mSwipeRefreshContainer;
    private Context mSelfContext;
    private int mLoadedPage = 0;
    private boolean mIsLoading = false;
    private boolean mNeedLoadMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_timeline);
        mSelfContext = this;
        mSwipeRefreshContainer = (SwipeRefreshLayout) findViewById(R.id.homeTimelineSwipeRefreshContainer);
        mSwipeRefreshContainer.setOnRefreshListener(this);
        mSwipeRefreshContainer.setColorSchemeResources(R.color.twitter_blue);
        mHomeTimelineContainerLv = (ListView) findViewById(R.id.homeTimelineContainerLv);
        mHomeTimelineContainerLv.setOnScrollListener(this);
        mTweetList = new ArrayList<>();

        mClient = SimpleTwitterApplication.getRestClient();
        renderTimeline(true);
    }


    public void renderTimeline(final boolean clearAdapter){
        if(!GeneralUtils.isNetworkAvailable(mSelfContext)){
            return;
        }
        mIsLoading = true;
        mClient.getHomeTimelinePosts(mLoadedPage, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d(HOME_TIMELINE_ACTIVITY_DEV_TAG, "renderTimeline|onSuccess()");
                Log.d(HOME_TIMELINE_ACTIVITY_DEV_TAG, "renderTimeline|response: " + response.toString());
                if(clearAdapter){
                    mTweetList.clear();
                    mTweetList.addAll(TweetModel.parseFromJSONArray(response));
                    mAdapter = new HomeTimelineAdapter(mSelfContext, 0, mTweetList);
                    mHomeTimelineContainerLv.setAdapter(mAdapter);
                    mSwipeRefreshContainer.setRefreshing(false);
                }
                else {
                    mTweetList.addAll(TweetModel.parseFromJSONArray(response));
                    mAdapter.notifyDataSetChanged();
                    mIsLoading = false;
                    if (response.length() == 0){
                        mNeedLoadMore = false;
                    }
                }
                mIsLoading = false;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(HOME_TIMELINE_ACTIVITY_DEV_TAG, "renderTimeline()|FAILED|responseString: " + responseString);
                mSwipeRefreshContainer.setRefreshing(false);
                Toast.makeText(mSelfContext, "Refresh Timeline Failed!", Toast.LENGTH_LONG).show();
                mIsLoading = false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_timeline, menu);
        //MenuItem composeBtn = menu.findItem(R.id.action_compose);
        //composeBtn.setOnMenuItemClickListener();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_compose) {
            Intent i = new Intent(this, ComposeTweetActivity.class);
            //startActivity(i);
            startActivityForResult(i, COMPOSE_REQUEST_CODE);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COMPOSE_REQUEST_CODE) {
            Log.d(HOME_TIMELINE_ACTIVITY_DEV_TAG,"onActivityResult()|resultCode: " + resultCode + "data" + data.toString());
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Log.d(HOME_TIMELINE_ACTIVITY_DEV_TAG,"onActivityResult()|RESULT_OK");
                renderTimeline(true);
            }
        }
    }

    @Override
    public void onRefresh() {
        mLoadedPage = 0;
        renderTimeline(true);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        // do nothing!
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(firstVisibleItem + visibleItemCount >= totalItemCount && !mIsLoading && mNeedLoadMore && totalItemCount != 0) {
            mLoadedPage++;
            Log.d(HOME_TIMELINE_ACTIVITY_DEV_TAG,"onScroll to loadmore|page: " + mLoadedPage);
            renderTimeline(false);
        }
    }
}
