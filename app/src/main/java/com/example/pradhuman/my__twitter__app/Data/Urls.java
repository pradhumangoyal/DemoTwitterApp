package com.example.pradhuman.my__twitter__app.Data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Pradhuman on 19-07-2017.
 */

public class Urls {
    @SerializedName("expanded_url")
    private String expandedUrl;
   /* @SerializedName("indices")
    private ArrayList<Integer> indexes;*/

    public String getExpandedUrl() {
        return expandedUrl;
    }

    public void setExpandedUrl(String expandedUrl) {
        this.expandedUrl = expandedUrl;
    }

    /*public ArrayList<Integer> getIndexes() {
        return indexes;
    }

    public void setIndexes(ArrayList<Integer> indexes) {
        this.indexes = indexes;
    }*/
}
