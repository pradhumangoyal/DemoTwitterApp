package com.example.pradhuman.my__twitter__app.Data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pradhuman on 19-07-2017.
 */

public class UserTweet {
    private long id;
    private String name;
    @SerializedName("screen_name")
    private String screenName;
    @SerializedName("followers_count")
    private long followersCount;
    @SerializedName("friends_count")
    private long followingCount;
    @SerializedName("profile_background_image_url")
    private String profileBackgroundImage;
    @SerializedName("profile_image_url")
    private String profileImage;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public long getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(long followersCount) {
        this.followersCount = followersCount;
    }

    public long getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(long followingCount) {
        this.followingCount = followingCount;
    }

    public String getProfileBackgroundImage() {
        return profileBackgroundImage;
    }

    public void setProfileBackgroundImage(String profileBackgroundImage) {
        this.profileBackgroundImage = profileBackgroundImage;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
