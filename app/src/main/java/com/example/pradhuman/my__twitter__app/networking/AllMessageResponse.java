package com.example.pradhuman.my__twitter__app.networking;

import android.provider.ContactsContract;

import com.example.pradhuman.my__twitter__app.Data.Profile;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Pradhuman on 21-07-2017.
 */

public class AllMessageResponse {
    @SerializedName("id_str")
    String idStr;

    String text;
    @SerializedName("sender")
    ProfileResponse senderProfile;

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ProfileResponse getSenderProfile() {
        return senderProfile;
    }

    public void setSenderProfile(ProfileResponse senderProfile) {
        this.senderProfile = senderProfile;
    }
}
