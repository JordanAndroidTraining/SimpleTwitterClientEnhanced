package com.codepath.apps.SimpleTwitterClient.Fragments;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.SimpleTwitterClient.R;
import com.codepath.apps.SimpleTwitterClient.SimpleTwitterApplication;
import com.codepath.apps.SimpleTwitterClient.SimpleTwitterClient;
import com.codepath.apps.SimpleTwitterClient.models.UserModel;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by jordanhsu on 8/17/15.
 */
public class ProfileViewFragment extends Fragment {
    public static final String PROFILE_VIEW_FRAGMENT = "ProfileViewFragment";
    private ImageView mProfileImgIv;
    private TextView mUserNameTv;
    private TextView mUserIdTv;
    private TextView mFollowingCountTv;
    private TextView mFollowerCountTv;
    private String mScreenName;
    private String mUserId;
    private SimpleTwitterClient mClient;
    private UserModel mUser;
    public Context mSelfContext;


    // get self profile view
    public static ProfileViewFragment newInstance(){
        ProfileViewFragment fg = new ProfileViewFragment();
        return  fg;
    }

    // get specific user's profile view
    public static ProfileViewFragment newInstance(String screenName, String userId ){
        ProfileViewFragment fg = new ProfileViewFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        args.putString("user_id", userId);
        fg.setArguments(args);
        return fg;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_page, container, false);
        mProfileImgIv = (ImageView) v.findViewById(R.id.profilePageProfileImgIv);
        mUserNameTv = (TextView) v.findViewById(R.id.profilePageUserNameTv);
        mUserIdTv = (TextView) v.findViewById(R.id.profilePageUserIdTv);
        mFollowingCountTv = (TextView) v.findViewById(R.id.profilePageFollowingValTv);
        mFollowerCountTv = (TextView) v.findViewById(R.id.profilePageFollowerValTv);
        renderProfileTimeline();
        renderProfileHeader();
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScreenName = null;
        mUserId = null;
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("screen_name")){
            if(arguments.containsKey("screen_name"))
                mScreenName = arguments.getString("screen_name");
            if(arguments.containsKey("user_id"))
                mUserId = arguments.getString("user_id");
        }
        mSelfContext = getActivity();
        mClient = SimpleTwitterApplication.getRestClient();
    }

    public void renderProfileHeader(){
        // for self user timeline
        if(mScreenName == null && mUserId == null){
            mClient.getCurrentUserInfo(new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    mUser = UserModel.parseFromJSONObject(response);
                    renderProfileHeaderOnSuccessHandler();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d(PROFILE_VIEW_FRAGMENT, "renderProfileHeader|onFailure()|responseString: " + responseString);

                }
            });
        }
        // for specific user timeline
        else{
            mClient.getUserInfo(mScreenName,mUserId, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("JordanTestXXX", "onSuccess()");
                    mUser = UserModel.parseFromJSONObject(response);

                    renderProfileHeaderOnSuccessHandler();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d(PROFILE_VIEW_FRAGMENT, "renderProfileHeader|onFailure()|responseString: " + responseString);

                }
            });
        }
    }

    public void renderProfileHeaderOnSuccessHandler(){
        Picasso.with(mSelfContext).load(mUser.getProfilePhotoUrl()).into(mProfileImgIv);
        mUserIdTv.setText(mUser.getUserScreenName());
        mUserNameTv.setText(mUser.getUserName());
        mFollowerCountTv.setText(mUser.getFollowerCount());
        mFollowingCountTv.setText(mUser.getFollowingCount());
    }

    public void renderProfileTimeline(){

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        TimeLineProfileViewFragment timelineFg;
        if(mScreenName != null){
            timelineFg = TimeLineProfileViewFragment.newInstance(mScreenName);
        }
        else {
            timelineFg = TimeLineProfileViewFragment.newInstance();
        }
        ft.replace(R.id.profileTweetListContainer, timelineFg);
        ft.commit();
    }

}
