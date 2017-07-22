package com.example.pradhuman.my__twitter__app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.twitter.sdk.android.tweetcomposer.TweetUploadService;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (TweetUploadService.UPLOAD_SUCCESS.equals(intent.getAction())) {
            // success
            final Long tweetId = intent.getExtras().getLong(TweetUploadService.EXTRA_TWEET_ID);
        } else {
            // failure
            final Intent retryIntent = intent.getExtras().getParcelable(TweetUploadService.EXTRA_RETRY_INTENT);
        }
    }
}
