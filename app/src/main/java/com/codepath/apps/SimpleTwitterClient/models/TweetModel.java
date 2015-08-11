package com.codepath.apps.SimpleTwitterClient.models;

import android.util.Log;

import com.codepath.apps.SimpleTwitterClient.Utils.GeneralUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordanhsu on 8/11/15.
 */
public class TweetModel {

    public static final String TWEET_MODEL_DEV_TAG = "TweetModelDevTag";
    private String caption = "";
    private String relativeTimestamp = "";
    private String tweetImgUrl = "";
    private int retweetCount = 0;
    private int favouritesCount = 0;
    private List<HashTagModel> hashTagList;
    private List<MentionUserModel> mentonUsersList;
    private UserModel user;

    public static TweetModel parseFromJSONObject(JSONObject json){
        TweetModel returnModel = new TweetModel();
        if(GeneralUtils.checkJSONObjectCol("text",json)){
            returnModel.setCaption(json.optString("text"));
        }
        if(GeneralUtils.checkJSONObjectCol("retweet_count",json)){
            returnModel.setRetweetCount(json.optInt("retweet_count"));
        }
        if(GeneralUtils.checkJSONObjectCol("favorite_count",json)){
           returnModel.setFavouritesCount(json.optInt("favorite_count"));
        }
        if(GeneralUtils.checkJSONObjectCol("created_at",json)){
            returnModel.setRelativeTimestamp(GeneralUtils.getRelativeTimeAgo(json.optString("created_at")));
        }
        if(GeneralUtils.checkJSONObjectCol("user",json)){
            try {
                returnModel.setUser(UserModel.parseFromJSONObject(json.getJSONObject("user")));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        // TODO: memtion user, hash tag!!

        return  returnModel;
    }

    public static ArrayList<TweetModel> parseFromJSONArray(JSONArray json){
        ArrayList<TweetModel> returnModel = new ArrayList<>();
        for(int i = 0 ; i < json.length() ; i++){
            try {
                TweetModel tweet = TweetModel.parseFromJSONObject(json.getJSONObject(i));
                if(tweet != null){
                    returnModel.add(tweet);
                }
            }catch (JSONException e){
                Log.d(TWEET_MODEL_DEV_TAG, "parseFromJSONArray| Exception" + e.toString());
                e.printStackTrace();
                continue;
            }
        }
        return returnModel;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getRelativeTimestamp() {
        return relativeTimestamp;
    }

    public void setRelativeTimestamp(String relativeTimestamp) {
        this.relativeTimestamp = relativeTimestamp;
    }

    public List<HashTagModel> getHashTagList() {
        return hashTagList;
    }

    public void setHashTagList(List<HashTagModel> hashTagList) {
        this.hashTagList = hashTagList;
    }

    public List<MentionUserModel> getMentonUsersList() {
        return mentonUsersList;
    }

    public void setMentonUsersList(List<MentionUserModel> mentonUsersList) {
        this.mentonUsersList = mentonUsersList;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public void setFavouritesCount(int favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    public String getTweetImgUrl() {
        return tweetImgUrl;
    }

    public void setTweetImgUrl(String tweetImgUrl) {
        this.tweetImgUrl = tweetImgUrl;
    }

}
