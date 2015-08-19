package com.codepath.apps.SimpleTwitterClient.Fragments;



import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.codepath.apps.SimpleTwitterClient.Adapters.TimelineAdapter;
import com.codepath.apps.SimpleTwitterClient.R;
import com.codepath.apps.SimpleTwitterClient.SimpleTwitterApplication;
import com.codepath.apps.SimpleTwitterClient.SimpleTwitterClient;
import com.codepath.apps.SimpleTwitterClient.models.TweetModel;
import com.codepath.apps.SimpleTwitterClient.models.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordanhsu on 8/16/15.
 */
public abstract class TweetListBaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    public static final String TWEET_LIST_FRAGMENT_DEV_TAG = "TweetListFragmentDevTag";
    public TimelineAdapter mAdapter;
    public ArrayList<TweetModel> mTweetList;
    public ListView mTweetListLv;
    public SwipeRefreshLayout mSwipeRefreshContainer;
    public SimpleTwitterClient mClient;
    public int mLoadedPage = 0;
    public boolean mIsLoading = false;
    public boolean mNeedLoadMore = true;
    public Activity mSelfActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_tweet_listview,container,false);
        mTweetListLv = (ListView) v.findViewById(R.id.tweetListLv);
        mTweetListLv.setAdapter(mAdapter);
        mSwipeRefreshContainer = (SwipeRefreshLayout) v.findViewById(R.id.tweetRefreshContainer);
        mSwipeRefreshContainer.setColorSchemeResources(R.color.twitter_blue);
        mSwipeRefreshContainer.setOnRefreshListener(this);
        mTweetListLv.setOnScrollListener(this);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTweetList = new ArrayList<>();
        mAdapter = new TimelineAdapter(getActivity(),0,mTweetList);
        mClient = SimpleTwitterApplication.getRestClient();
        mSelfActivity = getActivity();

    }

    public void addAllToTweetList(ArrayList<TweetModel> tweetList){
        mTweetList.addAll(tweetList);
        mAdapter.notifyDataSetChanged();
    }
    public void clearAllTweetList(){
        mTweetList.clear();
    }


    public static void saveTweetData(ArrayList<TweetModel> tweetList){
        Log.d(TWEET_LIST_FRAGMENT_DEV_TAG, "saveTweetData");
        //Clear old data
        new Delete().from(UserModel.class).execute();
        new Delete().from(TweetModel.class).execute();

        //Add new data
        for(TweetModel tweet: tweetList){
            Log.d(TWEET_LIST_FRAGMENT_DEV_TAG,"saveTweetData|catption: "+ tweet.getCaption());

            //Important! must save reference object first
            tweet.getUser().save();
            // save tweet
            tweet.save();
        }
    }

    public static ArrayList<TweetModel> loadTweetData(){
        Log.d(TWEET_LIST_FRAGMENT_DEV_TAG,"loadTweetData called");
        List<TweetModel> cacheTweetDataList;
        ArrayList<TweetModel> returnModel = new ArrayList<>();
        cacheTweetDataList = new Select().from(TweetModel.class).execute();
        Log.d(TWEET_LIST_FRAGMENT_DEV_TAG, "loadTweetData|cacheTweetDataList: " + cacheTweetDataList);

        for (TweetModel tweet : cacheTweetDataList){
            Log.d(TWEET_LIST_FRAGMENT_DEV_TAG,"loadTweetData|tweet User: " + tweet.getUser());
            Log.d(TWEET_LIST_FRAGMENT_DEV_TAG,"loadTweetData|imageURL: "+tweet.getTweetImgUrl());
            Log.d(TWEET_LIST_FRAGMENT_DEV_TAG,"loadTweetData|tweet User name: " + tweet.getUser().getUserName());
            Log.d(TWEET_LIST_FRAGMENT_DEV_TAG,"loadTweetData|tweet User id: " + tweet.getUser().getUserScreenName());
            Log.d(TWEET_LIST_FRAGMENT_DEV_TAG, "loadTweetData|tweet Caption: " + tweet.getCaption());
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
            Log.d(TWEET_LIST_FRAGMENT_DEV_TAG,"onScroll to loadmore|page: " + mLoadedPage);
            renderTimeline(false);
        }
    }

    public abstract void renderTimeline(final boolean clearAdapter);
}
