package com.example.pradhuman.my__twitter__app.Data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pradhuman on 19-07-2017.
 */

public class Entities {
   /* @SerializedName("hashtags")
    private ArrayList<HashTags> hashTagsArrayList;
    @SerializedName("user_mentions")
    private ArrayList<UserMentions> userMentionsArrayList;*/
    @SerializedName("urls")
    private List<Urls> urlsArrayList;
    public List<Urls> getUrlsArrayList() {
        return urlsArrayList;
    }

    public void setUrlsArrayList(List<Urls> urlsArrayList) {
        this.urlsArrayList = urlsArrayList;
    }
   /* public ArrayList<HashTags> getHashTagsArrayList() {
        return hashTagsArrayList;
    }

    public void setHashTagsArrayList(ArrayList<HashTags> hashTagsArrayList) {
        this.hashTagsArrayList = hashTagsArrayList;
    }

    public ArrayList<UserMentions> getUserMentionsArrayList() {
        return userMentionsArrayList;
    }

    public void setUserMentionsArrayList(ArrayList<UserMentions> userMentionsArrayList) {
        this.userMentionsArrayList = userMentionsArrayList;
    }
*/

}
