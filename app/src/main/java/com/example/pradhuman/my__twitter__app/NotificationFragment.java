package com.example.pradhuman.my__twitter__app;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.pradhuman.my__twitter__app.networking.ApiInterface;
import com.example.pradhuman.my__twitter__app.networking.TweetResponse;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.TweetUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationFragment extends Fragment {

    private String mToken;
    private String mSecret;
    private String mUserId;
    private String mUserName;
    private String mId;
    ProgressBar mProgressBar;
    Retrofit mRetrofit;
    private OnFragmentInteractionListener mListener;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ScrollViewExt mScrollViewExt;
    private LinearLayout mLinearLayout;
    View rootView;
    public NotificationFragment() {
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
         rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_view_fragment_notification);
        mLinearLayout = rootView.findViewById(R.id.my_layout_fragment_notification);
        mProgressBar = rootView.findViewById(R.id.progressBarNotification);
        mProgressBar.setVisibility(View.VISIBLE);
        mScrollViewExt = rootView.findViewById(R.id.scroll_view_ext_fragment_notification);
        
        hitApi();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                hitApi();
            }
        });
        return rootView;
    }

   

    public void hitApi() {
        final ApiInterface apiInterface = GetRetrofit.getApiInterface();
        Call<List<TweetResponse>> call = apiInterface.getTweetsArrayList(mUserName, 20);
        call.enqueue(new Callback<List<TweetResponse>>() {
            @Override
            public void onResponse(Call<List<TweetResponse>> call, Response<List<TweetResponse>> response) {
                try {
                    Log.i("favourite List", "Pass");
                    List<TweetResponse> tweetResponseList = response.body();
                    onDownloadComplete(tweetResponseList);
                } catch (Exception e) {
                    Snackbar.make(rootView,"No Search Result",Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TweetResponse>> call, Throwable t) {
                Log.i("favourite List", "Fail");
            }
        });
    }

    public void onDownloadComplete(List<TweetResponse> tweetResponseList) {
        if (mLinearLayout.getChildCount() > 0)
            mLinearLayout.removeAllViews();
        final ArrayList<String> finalIds = new ArrayList<>();
        for (int i = 0; i < tweetResponseList.size(); i++) {
            finalIds.add(tweetResponseList.get(i).getId_str());
        }
        for (int i = 0; i < finalIds.size(); i++) {
            Log.e("Tweet List", finalIds.get(i));
            final int pos = i;
            TweetUtils.loadTweet(Long.parseLong(finalIds.get(i)), new com.twitter.sdk.android.core.Callback<Tweet>() {
                @Override
                public void success(Result<Tweet> result) {
                    Log.i("favourite tweet load", "Pass");
                    Tweet tweet = result.data;
                    CompactTweetView compactTweetView = new CompactTweetView(getContext(), tweet);
                    compactTweetView.setTweetActionsEnabled(true);
                    compactTweetView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                            alert.setTitle("");
                            WebView wv = new WebView(getContext());
                            wv.loadUrl("https://twitter.com/" + mUserName + "/status/" + finalIds.get(pos));
                            wv.setWebViewClient(new WebViewClient() {
                                @Override
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    view.loadUrl(url);
                                    return true;
                                }
                            });
                            alert.setView(wv);
                            alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                            alert.show();
                            Log.i("LoadUrl", "https://twitter.com/" + mUserName + "/status/" + finalIds.get(pos));
                        }
                    });
                    mLinearLayout.addView(compactTweetView);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void failure(TwitterException exception) {
                    Log.i("favourite tweet load", "Fail");
                }
            });
        }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
