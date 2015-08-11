package com.codepath.apps.SimpleTwitterClient.models;

import com.codepath.apps.SimpleTwitterClient.Utils.GeneralUtils;

import org.json.JSONObject;

/**
 * Created by jordanhsu on 8/12/15.
 */
public class UserModel {
    private String userName;
    private String userID;
    private String profilePhotoUrl;

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
        if(GeneralUtils.checkJSONObjectCol("id_str",json)){
            user.setUserID(json.optString("id_str"));
        }
        return user;
    }
}
