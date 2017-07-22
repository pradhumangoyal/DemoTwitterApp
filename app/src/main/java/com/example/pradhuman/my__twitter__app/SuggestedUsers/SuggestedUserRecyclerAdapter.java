package com.example.pradhuman.my__twitter__app.SuggestedUsers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pradhuman.my__twitter__app.R;
import com.example.pradhuman.my__twitter__app.networking.SuggestionResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pradhuman on 23-07-2017.
 */

public class SuggestedUserRecyclerAdapter extends RecyclerView.Adapter<SuggestedUserRecyclerAdapter.SuggestedUserListHolder> {
    Context mContext;
    ArrayList<SuggestionResponse> mSuggestionResponseArrayList;

    onSuggestionListItemClickListener mListener;
    public void setOnSuggestionListItemClickListener(onSuggestionListItemClickListener listItemClickListener){
        mListener = listItemClickListener;
    }

    public SuggestedUserRecyclerAdapter(Context mContext, ArrayList<SuggestionResponse> mSuggestionResponseArrayList) {
        this.mContext = mContext;
        this.mSuggestionResponseArrayList = mSuggestionResponseArrayList;
    }

    @Override
    public SuggestedUserListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.content_suggested_users_layout,null);
        return new SuggestedUserListHolder(v);
    }

    @Override
    public void onBindViewHolder(SuggestedUserListHolder holder, int position) {
        holder.countTextView.setText(mSuggestionResponseArrayList.get(position).getSize());
        holder.titleTextView.setText(mSuggestionResponseArrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mSuggestionResponseArrayList.size();
    }

    public class SuggestedUserListHolder extends  RecyclerView.ViewHolder{
        @BindView(R.id.list_suggested_users_title)
        TextView titleTextView;

        @BindView(R.id.count_suggested_users)
        TextView countTextView;


        public SuggestedUserListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
            final int position = getAdapterPosition();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null)
                        mListener.onSuggestionItemClickListener(mSuggestionResponseArrayList.get(position).getSize(),
                                mSuggestionResponseArrayList.get(position).getSlug(),
                                mSuggestionResponseArrayList.get(position).getName(),
                                view,
                                getAdapterPosition());
                }
            });
        }
    }
    interface onSuggestionListItemClickListener {
        void onSuggestionItemClickListener(int count, String slug, String name, View itemView, int position);
    }

}
