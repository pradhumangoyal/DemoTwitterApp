package com.example.pradhuman.my__twitter__app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pradhuman.my__twitter__app.Fragments.FollowingFragment;
import com.example.pradhuman.my__twitter__app.networking.ProfileResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Pradhuman on 21-07-2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ListViewHolder>{

    private Context mContext;
    private ArrayList<ProfileResponse> mProfileResponse;
    OnLongItemClickListener mListener;

    public void setOnItemLongClickListener(OnLongItemClickListener listener){
        mListener = listener;
    }

    public RecyclerAdapter(Context mContext, ArrayList<ProfileResponse> mProfileResponse) {
        this.mContext = mContext;
        this.mProfileResponse = mProfileResponse;

    }



    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.follower_following_list_item_layout,parent,false);
        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        holder.userTextView.setText("@"+mProfileResponse.get(position).getScreenName());
        holder.fullTextView.setText(mProfileResponse.get(position).getName());
        Picasso.with(mContext).load(mProfileResponse.get(position).getProfileImage()).centerCrop().resize(50,50).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mProfileResponse.size();
    }

    public  class ListViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView fullTextView;
        TextView userTextView;
        public ListViewHolder(final View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.list_item_image_view);
            fullTextView = itemView.findViewById(R.id.list_item_full_name);
            userTextView = itemView.findViewById(R.id.list_item_user_name);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(mListener != null)
                    {
                        mListener.onLonggItemClickListener(getAdapterPosition(), itemView);
                    }
                    return true;
                }
            });
        }
    }
    public interface OnLongItemClickListener {
        void onLonggItemClickListener(int pos, View view);
    }

}

