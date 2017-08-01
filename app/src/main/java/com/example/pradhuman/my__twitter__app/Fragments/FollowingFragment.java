package com.example.pradhuman.my__twitter__app.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pradhuman.my__twitter__app.Constants;
import com.example.pradhuman.my__twitter__app.ProfileDetailActivity;
import com.example.pradhuman.my__twitter__app.ProfileItemClickActivity;
import com.example.pradhuman.my__twitter__app.R;
import com.example.pradhuman.my__twitter__app.RecyclerAdapter;
import com.example.pradhuman.my__twitter__app.networking.ApiInterface;
import com.example.pradhuman.my__twitter__app.networking.FollowerResponse;
import com.example.pradhuman.my__twitter__app.networking.GetRetrofit;
import com.example.pradhuman.my__twitter__app.networking.PostResponse;
import com.example.pradhuman.my__twitter__app.networking.ProfileResponse;
import com.example.pradhuman.my__twitter__app.networking.TwitterUtils;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FollowingFragment extends Fragment implements RecyclerAdapter.OnItemClickListener {

    ProfileResponse mProfileResponse;
    View rootView;
    ApiInterface mApiInterface;
    RecyclerAdapter adapter;
    RecyclerView mRecyclerView;
    ArrayList<ProfileResponse> mProfileArrayList;
    public FollowingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProfileResponse = (ProfileResponse) getArguments().getSerializable(Constants.INTENT_PROFILE_RESPONSE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_following, container, false);
        mRecyclerView = rootView.findViewById(R.id.fragment_following_recycler_view);
        mApiInterface = GetRetrofit.getApiInterface();
        mProfileArrayList = new ArrayList<>();
        Call<FollowerResponse> followerResponseCall = mApiInterface.getFollowingList(-1, mProfileResponse.getScreenName(), false, true);
        followerResponseCall.enqueue(new Callback<FollowerResponse>() {
            @Override
            public void onResponse(Call<FollowerResponse> call, Response<FollowerResponse> response) {
                try {
                    FollowerResponse followerResponse = response.body();
                    ArrayList<ProfileResponse> profileResponseArrayList = followerResponse.getProfileResponses();
                    onDownloadComplete(profileResponseArrayList);
                } catch (Exception e) {
                    Snackbar.make(rootView, "Can't load now! Try Again Later", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FollowerResponse> call, Throwable t) {
                Log.i("Follower Response ", "Fail");
            }
        });
        return rootView;
    }

    private void onDownloadComplete(final ArrayList<ProfileResponse> profileResponseArrayList) {
         adapter = new RecyclerAdapter(getContext(), profileResponseArrayList);
         mProfileArrayList = profileResponseArrayList;
        adapter.setOnItemLongClickListener(new RecyclerAdapter.OnLongItemClickListener() {
            @Override
            public void onLonggItemClickListener(int pos,View view) {
                Log.i("Username", TwitterCore.getInstance().getSessionManager().getActiveSession().getUserName() + " My "+ profileResponseArrayList.get(pos).getScreenName());
                if(!mProfileResponse.getScreenName().equals(
                        TwitterCore.getInstance().getSessionManager().getActiveSession().getUserName())){
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final int position = pos;

                builder.setCancelable(false);
                builder.setTitle("UnFollow");
                builder.setMessage("Do you Want to unfollow " + profileResponseArrayList.get(pos).getName() + " ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mApiInterface = GetRetrofit.getApiInterface();
                        Log.i("callDestroy",profileResponseArrayList.get(position).getScreenName()+" "+ profileResponseArrayList.get(position).getIdStr());
                        Call<PostResponse> callDestroy = mApiInterface.getUnFollowResponse(profileResponseArrayList.get(position).getScreenName(),profileResponseArrayList.get(position).getIdStr());
                        callDestroy.enqueue(new Callback<PostResponse>() {
                            @Override
                            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                                if(response.code()!=200){
                                    Toast.makeText(getContext(),"Couldnot Unfollow Now",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    mListener.onComplete();
                                    mRecyclerView.removeViewAt(position);
                                    adapter.notifyItemRemoved(position);
                                }
                            }

                            @Override
                            public void onFailure(Call<PostResponse> call, Throwable t) {
                                Toast.makeText(getContext(),"Couldnot Unfollow Now",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        adapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClickListener(int pos, View view) {
        ProfileResponse profileResponse = mProfileArrayList.get(pos);
        Intent intent = new Intent(getContext(), ProfileDetailActivity.class);
        intent.putExtra(Constants.INTENT_PROFILE_RESPONSE,profileResponse);
        startActivity(intent);
    }

    public static interface OnCompleteListener {
        public abstract void onComplete();
    }

    private OnCompleteListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (OnCompleteListener)context;
        }
        catch (final ClassCastException e) {
            ;
        }
    }
}
