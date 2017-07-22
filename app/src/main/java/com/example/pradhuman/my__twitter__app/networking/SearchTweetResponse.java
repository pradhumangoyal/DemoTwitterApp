package com.example.pradhuman.my__twitter__app.networking;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Pradhuman on 20-07-2017.
 */

public class SearchTweetResponse {
    @SerializedName("statuses")
    ArrayList<TweetResponse> tweetResponseArrayList;

    public ArrayList<TweetResponse> getTweetResponseArrayList() {
        return tweetResponseArrayList;
    }

    public void setTweetResponseArrayList(ArrayList<TweetResponse> tweetResponseArrayList) {
        this.tweetResponseArrayList = tweetResponseArrayList;
    }
}
