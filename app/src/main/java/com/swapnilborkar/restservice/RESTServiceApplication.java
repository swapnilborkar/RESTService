package com.swapnilborkar.restservice;

import android.app.Application;

import com.swapnilborkar.restservice.data.User;

/**
 * Created by SWAPNIL on 30-07-2016.
 */
public class RESTServiceApplication extends Application {

    private static RESTServiceApplication instance;
    private User user;
    private String accessToken;

    public static RESTServiceApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        user = new User();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
