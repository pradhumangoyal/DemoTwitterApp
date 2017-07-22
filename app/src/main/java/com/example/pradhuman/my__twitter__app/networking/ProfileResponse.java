package com.example.pradhuman.my__twitter__app.networking;

import com.example.pradhuman.my__twitter__app.Data.Profile;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Pradhuman on 20-07-2017.
 */

public class ProfileResponse implements Serializable {
    String name;
    @SerializedName("screen_name")
    String screenName;
    @SerializedName("id_str")
    String idStr;
    @SerializedName("profile_use_background_image")
    boolean isbackgroundimage;
    @SerializedName("followers_count")
    long followersCount;
    @SerializedName("profile_banner_url")
    String backgroundImage;
    @SerializedName("profile_image_url")
    String profileImage;
    @SerializedName("friends_count")
    long followingCount;
    @SerializedName("statuses_count")
    long statusCount;
    @SerializedName("created_at")
    String createdAt;
    @SerializedName("location")
    String location;
    @SerializedName("favourites_count")
    long favCount;
    @SerializedName("url")
    String url;
    @SerializedName("lang")
    String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public boolean isbackgroundimage() {
        return isbackgroundimage;
    }

    public void setIsbackgroundimage(boolean isbackgroundimage) {
        this.isbackgroundimage = isbackgroundimage;
    }

    public long getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(long followersCount) {
        this.followersCount = followersCount;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public long getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(long followingCount) {
        this.followingCount = followingCount;
    }

    public long getStatusCount() {
        return statusCount;
    }

    public void setStatusCount(long statusCount) {
        this.statusCount = statusCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getFavCount() {
        return favCount;
    }

    public void setFavCount(long favCount) {
        this.favCount = favCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
