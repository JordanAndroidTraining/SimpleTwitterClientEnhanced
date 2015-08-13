package com.codepath.apps.SimpleTwitterClient.models;

import android.os.Parcelable;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.codepath.apps.SimpleTwitterClient.Utils.GeneralUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordanhsu on 8/11/15.
 */
@Table(name = "tweets")
public class TweetModel extends Model{

    public static final String TWEET_MODEL_DEV_TAG = "TweetModelDevTag";

    @Column(name = "caption")
    private String caption = "";

    @Column(name = "relative_time_stamp")
    private String relativeTimestamp = "";

    @Column(name = "tweet_img_url")
    private String tweetImgUrl = "";

    @Column(name = "retweet_count")
    private int retweetCount = 0;

    @Column(name = "favorite_count")
    private int favouritesCount = 0;

    @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private UserModel user = new UserModel();

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
            Log.d(TWEET_MODEL_DEV_TAG,"in created_at");
            returnModel.setRelativeTimestamp(GeneralUtils.getRelativeTimeAgo(json.optString("created_at")));
        }
        if(GeneralUtils.checkJSONObjectCol("entities",json)){
            JSONObject entityJson = json.optJSONObject("entities");
            if(GeneralUtils.checkJSONObjectCol("media",entityJson)){
                JSONArray mediaList = entityJson.optJSONArray("media");
                if(mediaList.length() > 0){
                    JSONObject firstMedia = mediaList.optJSONObject(0);
                    if(GeneralUtils.checkJSONObjectCol("media_url",firstMedia)){
                        returnModel.setTweetImgUrl(firstMedia.optString("media_url"));
                    }
                }
            }
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
