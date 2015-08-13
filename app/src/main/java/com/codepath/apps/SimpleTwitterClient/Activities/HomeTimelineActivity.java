package com.codepath.apps.SimpleTwitterClient.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.Model;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
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
import java.util.List;

public class HomeTimelineActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, View.OnClickListener, AdapterView.OnItemClickListener {

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
        mHomeTimelineContainerLv.setOnItemClickListener(this);
        mTweetList = new ArrayList<>();

        //set icon
        getSupportActionBar().setLogo(R.mipmap.ic_twitter_log_white);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        mClient = SimpleTwitterApplication.getRestClient();
        renderTimeline(true);

        List<Model> testLoadDataList;
        testLoadDataList = new Select().from(TweetModel.class).execute();
        Log.d(HOME_TIMELINE_ACTIVITY_DEV_TAG, "testLoadDataList: " + testLoadDataList.toString());

    }


    public void renderTimeline(final boolean clearAdapter){
        if(!GeneralUtils.isNetworkAvailable(mSelfContext)){
            //loadTweetData();
//            for (TweetModel tweet : mTweetList){
//                Log.d(HOME_TIMELINE_ACTIVITY_DEV_TAG,"loadTweetData|tweet User: " + tweet.getUser());
//                Log.d(HOME_TIMELINE_ACTIVITY_DEV_TAG,"loadTweetData|tweet User: " + tweet.getCaption());
//            }
//            mAdapter = new HomeTimelineAdapter(mSelfContext, 0, mTweetList);
//            mHomeTimelineContainerLv.setAdapter(mAdapter);
//            mSwipeRefreshContainer.setRefreshing(false);
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


                    //saveTweetData();
                    //loadTweetData();


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

    public void saveTweetData(){
        //Clear old data
        new Delete().from(TweetModel.class).execute();
        //Add new data
        for(TweetModel tweet: mTweetList){
            tweet.save();
        }
    }

    public void loadTweetData(){
        List<TweetModel> cacheTweetDataList;
        cacheTweetDataList = new Select().from(TweetModel.class).execute();
        for (TweetModel tweet : cacheTweetDataList){
            Log.d(HOME_TIMELINE_ACTIVITY_DEV_TAG,"loadTweetData|tweet User: " + tweet.getUser());
            Log.d(HOME_TIMELINE_ACTIVITY_DEV_TAG,"loadTweetData|tweet Caption: " + tweet.getCaption());
        }

//        mTweetList.clear();
//        mTweetList.addAll(cacheTweetDataList);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveTweetData();
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
            switch (resultCode){
                case RESULT_OK:
                    Log.d(HOME_TIMELINE_ACTIVITY_DEV_TAG,"onActivityResult()|RESULT_OK");
                    renderTimeline(true);
                    break;
                case RESULT_CANCELED:
                    break;
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//        Intent i = new Intent(this,DetailViewActivity.class);
//        startActivity(i);
        Log.d(HOME_TIMELINE_ACTIVITY_DEV_TAG,mTweetList.get(position).getClass().toString());
    }

    @Override
    public void onClick(View view) {

        int position = mHomeTimelineContainerLv.getPositionForView(view);
        Log.d(HOME_TIMELINE_ACTIVITY_DEV_TAG, "IMAGE view clicked!| position: " + position);
        Intent i = new Intent(this,FullScreenImageViewActivity.class);
        i.putExtra("imgUrl",mTweetList.get(position).getTweetImgUrl());
        i.putExtra("retweetCount", String.valueOf(mTweetList.get(position).getRetweetCount()));
        i.putExtra("favoriteCount",String.valueOf(mTweetList.get(position).getFavouritesCount()));
        startActivity(i);
    }
}
