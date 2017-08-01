package com.example.pradhuman.my__twitter__app.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pradhuman.my__twitter__app.Constants;
import com.example.pradhuman.my__twitter__app.ProfileDetailActivity;
import com.example.pradhuman.my__twitter__app.R;
import com.example.pradhuman.my__twitter__app.RecyclerAdapter;
import com.example.pradhuman.my__twitter__app.networking.ApiInterface;
import com.example.pradhuman.my__twitter__app.networking.FollowerResponse;
import com.example.pradhuman.my__twitter__app.networking.GetRetrofit;
import com.example.pradhuman.my__twitter__app.networking.ProfileResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class FollowersFragment extends Fragment implements RecyclerAdapter.OnItemClickListener {

    ProfileResponse mProfileResponse;
    View rootView;
    ApiInterface mApiInterface;
    RecyclerView mRecyclerView;
    ArrayList<ProfileResponse> mProfileArrayList;
    public FollowersFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProfileResponse = (ProfileResponse)getArguments().getSerializable(Constants.INTENT_PROFILE_RESPONSE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_followers, container, false);
        mRecyclerView = rootView.findViewById(R.id.fragment_follower_recycler_view);
        mApiInterface = GetRetrofit.getApiInterface();
        mProfileArrayList = new ArrayList<>();
        Call<FollowerResponse> followerResponseCall = mApiInterface.getFollowerList(-1,mProfileResponse.getScreenName(),false,true);
        followerResponseCall.enqueue(new Callback<FollowerResponse>() {
            @Override
            public void onResponse(Call<FollowerResponse> call, Response<FollowerResponse> response) {
                try {
                    FollowerResponse followerResponse = response.body();
                    ArrayList<ProfileResponse> profileResponseArrayList = followerResponse.getProfileResponses();
                    mProfileArrayList = profileResponseArrayList;
                    onDownloadComplete(profileResponseArrayList);
                }catch (Exception e){
                    Snackbar.make(rootView,"Can't load now! Try Again Later",Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FollowerResponse> call, Throwable t) {
                Log.i("Follower Response ", "Fail");
            }
        });
        return rootView;
    }

    private void onDownloadComplete(ArrayList<ProfileResponse> profileResponseArrayList) {
        RecyclerAdapter adapter = new RecyclerAdapter(getContext(),profileResponseArrayList);
        adapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClickListener(int pos, View view) {
        ProfileResponse profileResponse = mProfileArrayList.get(pos);
        Intent intent = new Intent(getContext(), ProfileDetailActivity.class);
        intent.putExtra(Constants.INTENT_PROFILE_RESPONSE,profileResponse);
        startActivity(intent);
    }
}
