package com.example.pradhuman.my__twitter__app.networking;

import com.example.pradhuman.my__twitter__app.Data.Entities;
import com.example.pradhuman.my__twitter__app.Data.FinalId;
import com.example.pradhuman.my__twitter__app.Data.Tweets;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pradhuman on 19-07-2017.
 */

public class TweetResponse {
//    private List<Tweets> tweetsArrayList;
    String id_str;
   // Entities entities;

    /*public Entities getEntities() {
        return entities;
    }

    public void setEntities(Entities entities) {
        this.entities = entities;
    }*/

    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }

  /*public List<Tweets> getTweetsArrayList() {
        return tweetsArrayList;
    }



    public void setTweetsArrayList(List<Tweets> tweetsArrayList) {
        this.tweetsArrayList = tweetsArrayList;
    }*/
}
