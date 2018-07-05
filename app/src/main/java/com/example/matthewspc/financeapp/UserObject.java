package com.example.matthewspc.financeapp;

/**
 * Created by Christine on 12/3/2017.
 */

public class UserObject {
    private String userId;
    private String userEmail;
    public UserObject() {

    }

    public UserObject(String userId, String userEmail) {
        this.userId=userId;
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
