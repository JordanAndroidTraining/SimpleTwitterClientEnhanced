package com.codepath.apps.SimpleTwitterClient.Fragments;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.SimpleTwitterClient.Activities.FullScreenImageViewActivity;
import com.codepath.apps.SimpleTwitterClient.Adapters.HomeTimelineAdapter;
import com.codepath.apps.SimpleTwitterClient.R;
import com.codepath.apps.SimpleTwitterClient.models.TweetModel;

import java.util.ArrayList;

/**
 * Created by jordanhsu on 8/16/15.
 */
public class TweetListFragment extends Fragment {
    public static final String TWEET_LIST_FRAGMENT_DEV_TAG = "TweetListFragmentDevTag";
    private HomeTimelineAdapter mAdapter;
    private ArrayList<TweetModel> mTweetList;
    public ListView mTweetListLv;
    public SwipeRefreshLayout mSwipeRefreshContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweet_listview,container,false);
        mTweetListLv = (ListView) v.findViewById(R.id.tweetListLv);
        mTweetListLv.setAdapter(mAdapter);
        mSwipeRefreshContainer = (SwipeRefreshLayout) v.findViewById(R.id.tweetRefreshContainer);
        mSwipeRefreshContainer.setColorSchemeResources(R.color.twitter_blue);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTweetList = new ArrayList<>();
        mAdapter = new HomeTimelineAdapter(getActivity(),0,mTweetList);
    }

    public void addAllToTweetList(ArrayList<TweetModel> tweetList){
        mTweetList.addAll(tweetList);
        mAdapter.notifyDataSetChanged();
    }
    public void clearAllTweetList(){
        mTweetList.clear();
    }
}
