package com.example.pradhuman.my__twitter__app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

/**
 * Created by Pradhuman on 22-07-2017.
 */

public class CustomAdapter extends TweetTimelineListAdapter {
    /**
     * Constructs a TweetTimelineListAdapter for the given Tweet Timeline.
     *
     * @param context  the context for row views.
     * @param timeline a Timeline&lt;Tweet&gt; providing access to Tweet data items.
     * @throws IllegalArgumentException if timeline is null
     */

    Context mContext;
    OnTweetClickListener mListener;

    public CustomAdapter(Context context, Timeline<Tweet> timeline, OnTweetClickListener listener) {
        super(context, timeline);

        mContext = context;
        mListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        View view = super.getView(position, convertView, parent);

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mListener.onTweetClicked(pos, getItem(pos));
                return true;
            }
        });

        return view;
    }


    public interface OnTweetClickListener {
        void onTweetClicked(int position, Tweet tweet);
    }
}

