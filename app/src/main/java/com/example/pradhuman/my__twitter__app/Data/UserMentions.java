package com.example.pradhuman.my__twitter__app.Data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Pradhuman on 19-07-2017.
 */

public class UserMentions {
    @SerializedName("screen_name")
    private String screenName;
    private long id;
    @SerializedName("indices")
    private ArrayList<Integer> indexes;

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<Integer> getIndexes() {
        return indexes;
    }

    public void setIndexes(ArrayList<Integer> indexes) {
        this.indexes = indexes;
    }
}
