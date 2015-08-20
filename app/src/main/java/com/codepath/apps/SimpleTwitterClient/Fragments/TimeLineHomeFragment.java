package com.codepath.apps.SimpleTwitterClient.Fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.SimpleTwitterClient.Listeners.OnTweetComposeListener;
import com.codepath.apps.SimpleTwitterClient.Utils.GeneralUtils;
import com.codepath.apps.SimpleTwitterClient.models.TweetModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by jordanhsu on 8/16/15.
 */
public class TimeLineHomeFragment extends TweetListBaseFragment {
    private ArrayList<TweetModel> mTweetList;
    public static final String HOME_TIMELINE_FRAGMENT_DEV_TAG = "TimeLineHomeFragment";
    public static final int COMPOSE_REQUEST_CODE = 999;

    public static TimeLineHomeFragment newInstance(){
        TimeLineHomeFragment fg = new TimeLineHomeFragment();
        return fg;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().registerReceiver(new FragmentReceiver1(), new IntentFilter("fragmentupdater"));
        renderTimeline(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
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
//                    saveTweetData(mTweetList);
//                    loadTweetData();
                    mSwipeRefreshContainer.setRefreshing(false);
                    mIsLoading = false;
                    mLoadedPage++;
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d(HOME_TIMELINE_FRAGMENT_DEV_TAG, "renderTimeline()|FAILED|responseString: " + responseString);
                    mSwipeRefreshContainer.setRefreshing(false);
                    Toast.makeText(mSelfActivity, "Refresh Timeline Failed!", Toast.LENGTH_LONG).show();
                    mIsLoading = false;
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    public class FragmentReceiver1 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            renderTimeline(true);
        }
    }
}
