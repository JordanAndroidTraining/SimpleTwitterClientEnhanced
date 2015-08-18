package com.codepath.apps.SimpleTwitterClient.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.SimpleTwitterClient.Utils.GeneralUtils;
import com.codepath.apps.SimpleTwitterClient.models.TweetModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by jordanhsu on 8/18/15.
 */
public class TimeLineProfileViewFragment extends TweetListFragment {
    public static final String TIME_LINE_PROFILE_VIEW_FRAGMENT = "TimeLineProfileViewFragment";
    private ArrayList<TweetModel> mTweetList;
    private String mScreenName;
    public static TimeLineProfileViewFragment newInstance(){
        TimeLineProfileViewFragment fg = new TimeLineProfileViewFragment();
        return  fg;
    }

    public static TimeLineProfileViewFragment newInstance(String screen_name){
        TimeLineProfileViewFragment fg = new TimeLineProfileViewFragment();
        Bundle args = new Bundle();
        args.putString("screen_name",screen_name);
        fg.setArguments(args);
        return  fg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScreenName = null;
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("screen_name")){
            mScreenName = arguments.getString("screen_name");
        }
        Log.d("JordanGGG","mScreenName: " + mScreenName);
        renderTimeline(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void renderTimeline(final boolean clearAdapter) {
        if(!GeneralUtils.isNetworkAvailable(mSelfActivity)){
            // load && render it!
            mTweetList = loadTweetData();
            addAllToTweetList(loadTweetData());
            mSwipeRefreshContainer.setRefreshing(false);
        }
        else {
            mIsLoading = true;
            mClient.getUserTimeline(mLoadedPage, mScreenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

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
//                    saveTweetData(mTweetList);
//                    loadTweetData();
                    mSwipeRefreshContainer.setRefreshing(false);
                    mIsLoading = false;
                    mLoadedPage++;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d(TIME_LINE_PROFILE_VIEW_FRAGMENT, "renderTimeline()|FAILED|responseString: " + responseString);
                    mSwipeRefreshContainer.setRefreshing(false);
                    Toast.makeText(mSelfActivity, "Refresh Timeline Failed!", Toast.LENGTH_LONG).show();
                    mIsLoading = false;
                }
            });
        }
    }
}
