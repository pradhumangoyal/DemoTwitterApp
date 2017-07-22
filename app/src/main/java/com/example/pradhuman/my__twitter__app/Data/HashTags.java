package com.example.pradhuman.my__twitter__app.Data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Pradhuman on 19-07-2017.
 */

public class HashTags {
    String text;
    @SerializedName("indices")
    ArrayList<Integer> indexes;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<Integer> getIndexes() {
        return indexes;
    }

    public void setIndexes(ArrayList<Integer> indexes) {
        this.indexes = indexes;
    }
}
