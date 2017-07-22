package com.example.pradhuman.my__twitter__app.Fragments;

import android.content.Context;
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

import com.example.pradhuman.my__twitter__app.MessageRecyclerAdapter;
import com.example.pradhuman.my__twitter__app.R;
import com.example.pradhuman.my__twitter__app.networking.AllMessageResponse;
import com.example.pradhuman.my__twitter__app.networking.ApiInterface;
import com.example.pradhuman.my__twitter__app.networking.GetRetrofit;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageSentFragment extends Fragment {

    RecyclerView mRecyclerView;
    ApiInterface apiInterface;

    View rootView;
    public MessageSentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.fragment_message_sent, container, false);
        mRecyclerView = rootView.findViewById(R.id.fragment_message_sent_recycler_view);
        apiInterface = GetRetrofit.getApiInterface();
        Call<ArrayList<AllMessageResponse>> call = apiInterface.getSentMessage(20);
        call.enqueue(new Callback<ArrayList<AllMessageResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AllMessageResponse>> call, Response<ArrayList<AllMessageResponse>> response) {
                try {
                    Log.i("Sent Message Response", "Pass");
                    ArrayList<AllMessageResponse> allMessageResponse = response.body();
                    onDownloadComplete(allMessageResponse);
                } catch (Exception e) {
                    Snackbar.make(rootView,"Can't load now! Try Again Later",Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AllMessageResponse>> call, Throwable t) {
                Log.e("Sent Message Response", "Fail");
            }
        });
        return rootView;
    }
    private void onDownloadComplete(ArrayList<AllMessageResponse> allMessageResponse) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setAdapter(new MessageRecyclerAdapter(getContext(),allMessageResponse));
    }
}
