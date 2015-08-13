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

    @Column(name = "user_id")
    private String userID = "";

    @Column(name = "profile_photo_url")
    private String profilePhotoUrl = "";

    public UserModel(){
        super();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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
            user.setUserID("@" + json.optString("screen_name"));
        }
        return user;
    }
}
