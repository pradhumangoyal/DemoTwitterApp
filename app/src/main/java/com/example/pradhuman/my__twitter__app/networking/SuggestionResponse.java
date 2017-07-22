package com.example.pradhuman.my__twitter__app.networking;

/**
 * Created by Pradhuman on 23-07-2017.
 */

public class SuggestionResponse {
    int size;
    String slug;
    String name;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
