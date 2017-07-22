package com.example.pradhuman.my__twitter__app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pradhuman.my__twitter__app.networking.AllMessageResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Pradhuman on 21-07-2017.
 */

public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.MessageViewHolder> {
    private Context mContext;
    ArrayList<AllMessageResponse> mAllMessageResponses;
    OnMessageClickListener mMessageClickListener;

    public void setOnMessageClickListener(OnMessageClickListener listener){
        mMessageClickListener = listener;
    }

    public MessageRecyclerAdapter(Context mContext, ArrayList<AllMessageResponse> allMessageResponses) {
        this.mContext = mContext;
        mAllMessageResponses = allMessageResponses;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.message_item_layout, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Log.d("Message id", mAllMessageResponses.get(position).getIdStr());
        String mMessage = mAllMessageResponses.get(position).getText();
        String mSender = mAllMessageResponses.get(position).getSenderProfile().getName();
        String url = mAllMessageResponses.get(position).getSenderProfile().getProfileImage();
        String mProfileUrl = url.substring(0, url.length() - 11) + ".jpg";
        holder.fullTextView.setText(mMessage);
        holder.userTextView.setText(mSender);
        Log.i("mProfileUrl", mProfileUrl);
        Picasso.with(mContext).load(mProfileUrl).resize(50, 50).centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mAllMessageResponses.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView fullTextView;
        TextView userTextView;

        public MessageViewHolder(final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.message_item_image_view);
            fullTextView = itemView.findViewById(R.id.message_text_view);
            userTextView = itemView.findViewById(R.id.sender_text_view);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(mMessageClickListener!=null){
                        mMessageClickListener.onMessageClickListener(getAdapterPosition(),mAllMessageResponses.get(getAdapterPosition()).getIdStr());
                    }
                    return true;
                }
            });
        }
    }

}
