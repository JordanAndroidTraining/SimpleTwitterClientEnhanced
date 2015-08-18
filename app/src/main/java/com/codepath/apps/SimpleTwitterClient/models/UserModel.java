package com.codepath.apps.SimpleTwitterClient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.codepath.apps.SimpleTwitterClient.Utils.GeneralUtils;

import org.json.JSONObject;

/**
 * Created by jordanhsu on 8/12/15.
 */
@Table(name = "users")
public class UserModel extends Model {
    @Column(name = "user_name")
    private String userName = "";

    @Column(name = "user_screen_name")
    private String userScreenName = "";

    @Column(name = "profile_photo_url")
    private String profilePhotoUrl = "";

    @Column(name = "following_count")
    private String followingCount = "";

    @Column(name = "follower_count")
    private String followerCount = "";

    @Column(name = "user_id")
    private String userId = "";

    public UserModel(){
        super();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(String followingCount) {
        this.followingCount = followingCount;
    }

    public String getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(String followerCount) {
        this.followerCount = followerCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserScreenName() {
        return userScreenName;
    }

    public void setUserScreenName(String userScreenName) {
        this.userScreenName = userScreenName;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }
    public static  UserModel parseFromJSONObject(JSONObject json){
        UserModel user = new UserModel();
        //JSONObject userObj = json.optJSONObject("user");
        if(GeneralUtils.checkJSONObjectCol("name", json)){
            user.setUserName(json.optString("name"));
        }
        if(GeneralUtils.checkJSONObjectCol("profile_image_url",json)){
            user.setProfilePhotoUrl(json.optString("profile_image_url"));
        }
        if(GeneralUtils.checkJSONObjectCol("screen_name",json)){
            user.setUserScreenName(json.optString("screen_name"));
        }
        if(GeneralUtils.checkJSONObjectCol("followers_count",json)){
            user.setFollowerCount(json.optString("followers_count"));
        }
        if(GeneralUtils.checkJSONObjectCol("friends_count",json)){
            user.setFollowingCount(json.optString("friends_count"));
        }
        if(GeneralUtils.checkJSONObjectCol("id_str",json)){
            user.setUserId(json.optString("id_str"));
        }
        return user;
    }
}
