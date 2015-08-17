package com.codepath.apps.SimpleTwitterClient.Fragments;

import android.app.Activity;
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
 * Created by jordanhsu on 8/17/15.
 */
public class MentionTimeLineFragment extends TweetListFragment{
    private ArrayList<TweetModel> mTweetList;
    public static final String HOME_TIMELINE_FRAGMENT_DEV_TAG = "HomeTimeLineFragment";
    public static final int COMPOSE_REQUEST_CODE = 999;

    public static MentionTimeLineFragment newInstance(){
        MentionTimeLineFragment fg = new MentionTimeLineFragment();
        return fg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderTimeline(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
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
            mClient.getMentionTimelinePosts(mLoadedPage, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d(HOME_TIMELINE_FRAGMENT_DEV_TAG, "renderTimeline|onSuccess()");
                    Log.d(HOME_TIMELINE_FRAGMENT_DEV_TAG, "renderTimeline|response: " + response.toString());
                    mTweetList = TweetModel.parseFromJSONArray(response);
                    if (clearAdapter) {
                        clearAllTweetList();
                        addAllToTweetList(mTweetList);
                    } else {
                        addAllToTweetList(mTweetList);
                        mIsLoading = false;
                        if (response.length() == 0) {
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
}
