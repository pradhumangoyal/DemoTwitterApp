package com.example.pradhuman.my__twitter__app.networking;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Pradhuman on 21-07-2017.
 */

public class FollowerResponse {


    @SerializedName("users")

    ArrayList<ProfileResponse> profileResponses;
    public ArrayList<ProfileResponse> getProfileResponses() {
        return profileResponses;
    }

    public void setProfileResponses(ArrayList<ProfileResponse> profileResponses) {
        this.profileResponses = profileResponses;
    }

}
