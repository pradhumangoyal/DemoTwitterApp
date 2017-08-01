package com.example.pradhuman.my__twitter__app;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pradhuman.my__twitter__app.networking.ApiInterface;
import com.example.pradhuman.my__twitter__app.networking.GetRetrofit;
import com.example.pradhuman.my__twitter__app.networking.NewApiInterface;
import com.example.pradhuman.my__twitter__app.networking.NewRetrofit;
import com.example.pradhuman.my__twitter__app.networking.PostResponse;
import com.example.pradhuman.my__twitter__app.networking.ProfileResponse;
import com.twitter.sdk.android.core.OAuthSigning;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class SearchFragment extends Fragment implements CustomAdapter.OnTweetClickListener {
    private String mToken;
    private String mSecret;
    private String mUserId;
    private String mUserName;
    private String mId;
    private CustomAdapter customAdapter;
    private OnFragmentInteractionListener mListener;
    private Context mContext;
    ProgressBar mProgressBar;
    ApiInterface mApiInterface;
    View rootView;
    ListView listView;
    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle b = getArguments();
            mToken = b.getString(Constants.AUHENTICATED_TOKEN);
            mSecret = b.getString(Constants.AUTHENTICATED_SECRET);
            mUserId = b.getString(Constants.AUTHENTICATED_USER_ID);
            mUserName = b.getString(Constants.AUTHENTICATED_USERNAME);
            mId = b.getString(Constants.AUTHENTICATED_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        mProgressBar = rootView.findViewById(R.id.progressBarSearch);
        mProgressBar.setVisibility(View.VISIBLE);
        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(mUserName)
                .build();
        mProgressBar.setVisibility(View.GONE);
         listView = rootView.findViewById(R.id.user_list);
        customAdapter = new CustomAdapter(getContext(), userTimeline, this);
        listView.setAdapter(customAdapter);
        listView.setEmptyView(inflater.inflate(R.layout.list_empty_view, container, false));
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Log.e("NewRuntime", "Interface Not Implemented");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onTweetClicked(int position, final Tweet tweet) {
        mApiInterface = GetRetrofit.getApiInterface();
        final NewApiInterface apiInterface = NewRetrofit.getApiInterface();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setTitle("Delete Tweet");
        builder.setMessage("Do you really want to delete the tweet?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Call<PostResponse> call = apiInterface.getDestroyTweet(tweet.getId());
                Log.i("Final Url id", String.valueOf(tweet.getId()));
                call.enqueue(new Callback<PostResponse>() {
                    @Override
                    public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                        Log.i("response", response.code() + " " + response.errorBody());
                        if (response.code() != 200) {
                            Toast.makeText(getContext(), "Can't Delete This tweet now!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.VISIBLE);
                            final UserTimeline userTimeline = new UserTimeline.Builder()
                                    .screenName(mUserName)
                                    .build();
                            mProgressBar.setVisibility(View.GONE);
                            customAdapter = new CustomAdapter(getContext(), userTimeline, SearchFragment.this);
                            listView.setAdapter(customAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<PostResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "Can't Delete This tweet now!!", Toast.LENGTH_SHORT).show();
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        mProgressBar.setVisibility(View.VISIBLE);
        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(mUserName)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this.getContext())
                .setTimeline(userTimeline)
                .build();
        mProgressBar.setVisibility(View.GONE);
    }
}
