package com.codepath.apps.SimpleTwitterClient.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.codepath.apps.SimpleTwitterClient.R;
import com.codepath.apps.SimpleTwitterClient.SimpleTwitterApplication;
import com.codepath.apps.SimpleTwitterClient.SimpleTwitterClient;
import com.codepath.apps.SimpleTwitterClient.Utils.GeneralUtils;
import com.codepath.apps.SimpleTwitterClient.models.TweetModel;
import com.codepath.apps.SimpleTwitterClient.models.UserModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordanhsu on 8/16/15.
 */
public class HomeTimeLineFragment extends TweetListFragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener  {
    private ArrayList<TweetModel> mTweetList;
    public static final String HOME_TIMELINE_FRAGMENT_DEV_TAG = "HomeTimeLineFragment";
    public static final int COMPOSE_REQUEST_CODE = 999;
    private SimpleTwitterClient mClient;
    private int mLoadedPage = 0;
    private boolean mIsLoading = false;
    private boolean mNeedLoadMore = true;
    private Activity mSelfActivity;
//    private SwipeRefreshLayout mSwipeRefreshContainer;

    public static HomeTimeLineFragment newInstance(){
        HomeTimeLineFragment fg = new HomeTimeLineFragment();
        return fg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClient = SimpleTwitterApplication.getRestClient();
        mSelfActivity = getActivity();

        renderTimeline(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        mSwipeRefreshContainer.setOnRefreshListener(this);
        mTweetListLv.setOnScrollListener(this);
        return v;
    }


    public void renderTimeline(final boolean clearAdapter){
        if(!GeneralUtils.isNetworkAvailable(mSelfActivity)){
            // load && render it!
            mTweetList = loadTweetData();
            addAllToTweetList(loadTweetData());
            mSwipeRefreshContainer.setRefreshing(false);
        }
        else {
            mIsLoading = true;
            mClient.getHomeTimelinePosts(mLoadedPage, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d(HOME_TIMELINE_FRAGMENT_DEV_TAG, "renderTimeline|onSuccess()");
                    Log.d(HOME_TIMELINE_FRAGMENT_DEV_TAG, "renderTimeline|response: " + response.toString());
                    mTweetList = TweetModel.parseFromJSONArray(response);
                    if(clearAdapter){
                        clearAllTweetList();
                        addAllToTweetList(mTweetList);
                    }
                    else {
                        addAllToTweetList(mTweetList);
                        mIsLoading = false;
                        if (response.length() == 0){
                            mNeedLoadMore = false;
                        }
                    }
                    saveTweetData(mTweetList);
                    loadTweetData();
                    mSwipeRefreshContainer.setRefreshing(false);
                    mIsLoading = false;
                    mLoadedPage++;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d(HOME_TIMELINE_FRAGMENT_DEV_TAG, "renderTimeline()|FAILED|responseString: " + responseString);
                    mSwipeRefreshContainer.setRefreshing(false);
                    Toast.makeText(mSelfActivity, "Refresh Timeline Failed!", Toast.LENGTH_LONG).show();
                    mIsLoading = false;
                }
            });
        }
    }

    public static void saveTweetData(ArrayList<TweetModel> tweetList){
        Log.d(HOME_TIMELINE_FRAGMENT_DEV_TAG, "saveTweetData");
        //Clear old data
        new Delete().from(UserModel.class).execute();
        new Delete().from(TweetModel.class).execute();

        //Add new data
        for(TweetModel tweet: tweetList){
            Log.d(HOME_TIMELINE_FRAGMENT_DEV_TAG,"saveTweetData|catption: "+ tweet.getCaption());

            //Important! must save reference object first
            tweet.getUser().save();
            // save tweet
            tweet.save();
        }
    }

    public static ArrayList<TweetModel> loadTweetData(){
        Log.d(HOME_TIMELINE_FRAGMENT_DEV_TAG,"loadTweetData called");
        List<TweetModel> cacheTweetDataList;
        ArrayList<TweetModel> returnModel = new ArrayList<>();
        cacheTweetDataList = new Select().from(TweetModel.class).execute();
        Log.d(HOME_TIMELINE_FRAGMENT_DEV_TAG, "loadTweetData|cacheTweetDataList: " + cacheTweetDataList);

        for (TweetModel tweet : cacheTweetDataList){
            Log.d(HOME_TIMELINE_FRAGMENT_DEV_TAG,"loadTweetData|tweet User: " + tweet.getUser());
            Log.d(HOME_TIMELINE_FRAGMENT_DEV_TAG,"loadTweetData|imageURL: "+tweet.getTweetImgUrl());
            Log.d(HOME_TIMELINE_FRAGMENT_DEV_TAG,"loadTweetData|tweet User name: " + tweet.getUser().getUserName());
            Log.d(HOME_TIMELINE_FRAGMENT_DEV_TAG,"loadTweetData|tweet User id: " + tweet.getUser().getUserID());
            Log.d(HOME_TIMELINE_FRAGMENT_DEV_TAG, "loadTweetData|tweet Caption: " + tweet.getCaption());
            returnModel.add(tweet);
        }
        return returnModel;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onRefresh() {
        mLoadedPage = 0;
        renderTimeline(true);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(firstVisibleItem + visibleItemCount >= totalItemCount && !mIsLoading && mNeedLoadMore && totalItemCount != 0) {
            Log.d(HOME_TIMELINE_FRAGMENT_DEV_TAG,"onScroll to loadmore|page: " + mLoadedPage);
            renderTimeline(false);
        }
    }

}
