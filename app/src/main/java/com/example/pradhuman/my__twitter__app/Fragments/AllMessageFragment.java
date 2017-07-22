package com.example.pradhuman.my__twitter__app.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pradhuman.my__twitter__app.MessageRecyclerAdapter;
import com.example.pradhuman.my__twitter__app.OnMessageClickListener;
import com.example.pradhuman.my__twitter__app.R;
import com.example.pradhuman.my__twitter__app.networking.AllMessageResponse;
import com.example.pradhuman.my__twitter__app.networking.ApiInterface;
import com.example.pradhuman.my__twitter__app.networking.GetRetrofit;
import com.example.pradhuman.my__twitter__app.networking.PostResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllMessageFragment extends Fragment implements OnMessageClickListener {

    RecyclerView mRecyclerView;
    ApiInterface apiInterface;
    MessageRecyclerAdapter mAdapter;
    ArrayList<AllMessageResponse> mAllMessageResponse;
    public AllMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_all_message, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_all_message_recycler_view);
        apiInterface = GetRetrofit.getApiInterface();
        Call<ArrayList<AllMessageResponse>> call = apiInterface.getAllMessage(20);
        call.enqueue(new Callback<ArrayList<AllMessageResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AllMessageResponse>> call, Response<ArrayList<AllMessageResponse>> response) {
                try {


                    Log.i("All Message Response", "Pass");
                    mAllMessageResponse = response.body();
                    onDownloadComplete(mAllMessageResponse);
                }catch (Exception e){
                    Snackbar.make(rootView,"Can't load now! Try Again Later",Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AllMessageResponse>> call, Throwable t) {
                Log.e("All Message Response", "Fail");
            }
        });
        return rootView;
    }

    private void onDownloadComplete(ArrayList<AllMessageResponse> allMessageResponse) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
         mAdapter =new MessageRecyclerAdapter(getContext(),allMessageResponse);
        mAdapter.setOnMessageClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onMessageClickListener(final int position, String id) {
        final String idStr = id;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Message");
        builder.setMessage("Do you really want to delete this message?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Call<PostResponse> postResponseCall = apiInterface.postDestroyMessage(idStr);
                postResponseCall.enqueue(new Callback<PostResponse>() {
                    @Override
                    public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                        if(response.code()!=200){
                            Log.i("Delete Message", "Fail");
                            Toast.makeText(getContext(),"Can't Delete the message!",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            onRefresh();
                            Log.i("Delete Message", "Pass");
                        }
                    }

                    @Override
                    public void onFailure(Call<PostResponse> call, Throwable t) {
                        Log.i("Delete Message", "Fail");
                        Toast.makeText(getContext(),"Can't Delete the message!",Toast.LENGTH_SHORT).show();
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
        builder.create().show();
    }

    private void onRefresh() {
        Call<ArrayList<AllMessageResponse>> call = apiInterface.getAllMessage(20);
        call.enqueue(new Callback<ArrayList<AllMessageResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AllMessageResponse>> call, Response<ArrayList<AllMessageResponse>> response) {
                try {
                    Log.i("All Message Response", "Pass");
                    mAllMessageResponse.clear();
                    mAdapter.notifyDataSetChanged();
                    mAllMessageResponse = response.body();
                    mAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    ;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AllMessageResponse>> call, Throwable t) {
                Log.e("All Message Response", "Fail");
            }
        });
    }
}
