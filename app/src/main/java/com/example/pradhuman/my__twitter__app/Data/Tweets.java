package com.example.pradhuman.my__twitter__app.Data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pradhuman on 19-07-2017.
 */

public class Tweets {
    @SerializedName("created_at")
    private String createdAt;
    private long id;
    private String text;
    @SerializedName("user")
    private UserTweet userTweet;
    @SerializedName("retweet_count")
    int retweetCount;
    @SerializedName("favorite_count")
    int favouriteCount;
    @SerializedName("retweeted")
    boolean isRetweet;
    @SerializedName("favorited")
    boolean isFavourite;
    private Entities entities;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserTweet getUserTweet() {
        return userTweet;
    }

    public void setUserTweet(UserTweet userTweet) {
        this.userTweet = userTweet;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public int getFavouriteCount() {
        return favouriteCount;
    }

    public void setFavouriteCount(int favouriteCount) {
        this.favouriteCount = favouriteCount;
    }

    public boolean isRetweet() {
        return isRetweet;
    }

    public void setRetweet(boolean retweet) {
        isRetweet = retweet;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public Entities getEntities() {
        return entities;
    }

    public void setEntities(Entities entities) {
        this.entities = entities;
    }
}
