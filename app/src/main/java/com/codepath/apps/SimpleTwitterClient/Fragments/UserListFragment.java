package com.codepath.apps.SimpleTwitterClient.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.apps.SimpleTwitterClient.Adapters.UserListAdapter;
import com.codepath.apps.SimpleTwitterClient.R;
import com.codepath.apps.SimpleTwitterClient.SimpleTwitterApplication;
import com.codepath.apps.SimpleTwitterClient.SimpleTwitterClient;
import com.codepath.apps.SimpleTwitterClient.Utils.GeneralUtils;
import com.codepath.apps.SimpleTwitterClient.models.UserModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jordanhsu on 8/19/15.
 */
public class UserListFragment extends Fragment {

    public static final String USER_LIST_FRAGMENT = "UserListFragment";
    private String mScreenName;
    private String mListType;
    private SimpleTwitterClient mClient;
    private UserListAdapter mAdapter;
    private ArrayList<UserModel> mUserList;
    private ListView mUserListLv;
    private TextView mTitleTv;

    // listType : follower| following
    public static UserListFragment newInstance(String screenName,String listType){
        UserListFragment fg = new UserListFragment();
        Bundle args = new Bundle();
        args.putString("screen_name",screenName);
        args.putString("list_type",listType);
        fg.setArguments(args);
        return  fg;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_listview,container,false);
        mUserListLv = (ListView) v.findViewById(R.id.userListLv);
        //mUserListLv.setAdapter(mAdapter);
        mTitleTv = (TextView) v.findViewById(R.id.userListTyleTitleTv);
        renderUserList();
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null){
            if(arguments.containsKey("screen_name"))
                mScreenName = arguments.getString("screen_name");
            if(arguments.containsKey("list_type"))
                mListType = arguments.getString("list_type");
        }
        mClient = SimpleTwitterApplication.getRestClient();
        mUserList = new ArrayList<>();
        //mAdapter = new UserListAdapter(getActivity(),0,mUserList);
        Log.d("JordanXXXX", "HAHAHA|mScreenName: " + mScreenName + "|mListType: " + mListType);
    }

    public void renderUserList(){
        if (mListType.equals("follower")){
            mTitleTv.setText("FOLLOWER");
            Log.d("JordanXXXX","renderUserList|follower");
            mClient.getFollowerList(mScreenName,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("JordanXXXX","getFollowerList|onSuccess");
                    if(GeneralUtils.checkJSONObjectCol("users",response)){
                        JSONArray userArray = response.optJSONArray("users");
                        mUserList = UserModel.parseFromJsonArray(userArray);
                        mAdapter = new UserListAdapter(getActivity(),0,mUserList);
                        mUserListLv.setAdapter(mAdapter);
                        Log.d("JordanXXXX","getFollowerList|onSuccess|mUserList: " + mUserList);
//                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d(USER_LIST_FRAGMENT,"getFollowerList()|FAIL|responseString: " + responseString);
                }
            });
        }
        if (mListType.equals("following")){
            mTitleTv.setText("FOLLOWING");
            Log.d("JordanXXXX","renderUserList|following");
            mClient.getFollowingList(mScreenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if(GeneralUtils.checkJSONObjectCol("users",response)){
                        JSONArray userArray = response.optJSONArray("users");
                        mUserList = UserModel.parseFromJsonArray(userArray);
                        mAdapter = new UserListAdapter(getActivity(),0,mUserList);
                        mUserListLv.setAdapter(mAdapter);
                        Log.d("JordanXXXX", "getFollowingList|onSuccess|mUserList: " + String.valueOf(mUserList));
                        //mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d(USER_LIST_FRAGMENT, "getFollowerList()|FAIL|responseString: " + responseString);
                }
            });
        }
    }
}
