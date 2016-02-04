package com.kluehspies.marian.example;

import com.kluehspies.marian.unlockmanager.persistence.Achievement;

/**
 * Created by Andreas Schattney on 16.10.2015.
 */
public class UserAchievement extends Achievement {

    private String user;

    public static UserAchievement with(String key){
        return new UserAchievement(key);
    }

    public UserAchievement() {}

    public UserAchievement(String key) {
        super(key);
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

}
