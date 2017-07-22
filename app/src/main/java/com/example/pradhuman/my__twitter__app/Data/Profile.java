package com.example.pradhuman.my__twitter__app.Data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pradhuman on 20-07-2017.
 */

public class Profile {
    @SerializedName("mobile")
    ImageClass imageClass;

    public ImageClass getImageClass() {
        return imageClass;
    }

    public void setImageClass(ImageClass imageClass) {
        this.imageClass = imageClass;
    }

    public static  class ImageClass {
        String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
