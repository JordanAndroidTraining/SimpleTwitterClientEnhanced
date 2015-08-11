package com.codepath.apps.SimpleTwitterClient.models;

/**
 * Created by jordanhsu on 8/12/15.
 */
public class MentionUserModel {
    private String userName;
    private String userID;
    private String screenName;

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

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
}
