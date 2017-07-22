package com.example.pradhuman.my__twitter__app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.pradhuman.my__twitter__app.Data.FinalId;
import com.example.pradhuman.my__twitter__app.Data.Tweets;
import com.example.pradhuman.my__twitter__app.networking.ApiInterface;
import com.example.pradhuman.my__twitter__app.networking.TweetResponse;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CollectionTimeline;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;


public class HomeFragment extends Fragment {
    private String mToken;
    private String mSecret;
    private String mUserId;
    private String mUserName;
    private String mId;
    private SwipeRefreshLayout mSwipeView;
    private LinearLayout mLinearLayout;
    private ScrollViewExt mScrollViewExt;
    ListView mListView;
    ProgressBar mProgressBar;
    private OnFragmentInteractionListener mListener;
    private int count, initCount, max;
    ArrayList<String> idListFinal, finalUrl;
    View rootView;

    public HomeFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        final View emptyView = inflater.inflate(R.layout.list_empty_view, container, false);
        OAuthInterceptor oauth1Woocommerce = new OAuthInterceptor.Builder()
                .consumerKey(Constants.CONSUMER_KEY)
                .consumerSecret(Constants.CONSUMER_SECRET)
                .userToken(mToken)
                .userSecret(mSecret)
                .build();
        count = 20;
        initCount = 0;
        max = 20;
        mProgressBar = rootView.findViewById(R.id.progressBarHome);
        mProgressBar.setVisibility(View.VISIBLE);
        mScrollViewExt = rootView.findViewById(R.id.scroll_view_ext);
        mScrollViewExt.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(ScrollViewExt scrollView, int x, int y, int oldx, int oldy) {
                View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

                // if diff is zero, then the bottom has been reached
                if (diff == 0) {
                    // do stuff
                    onDownloadComplete(idListFinal);
                }
            }
        });
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(oauth1Woocommerce)// Interceptor oauth1Woocommerce added
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.twitter.com/1.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        mSwipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_view);
        mLinearLayout = (LinearLayout) rootView.findViewById(R.id.my_layout);
        final ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        idListFinal = new ArrayList<String>();
        finalUrl = new ArrayList<>();
        mSwipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeView.setRefreshing(true);
                idListFinal.clear();
                count = 20;
                initCount = 0;

                Call<List<TweetResponse>> call = apiInterface.getTweetListIdLong(mUserName, 200);
                call.enqueue(new Callback<List<TweetResponse>>() {
                    @Override
                    public void onResponse(Call<List<TweetResponse>> call, Response<List<TweetResponse>> response) {
                        idListFinal.clear();
                        List<TweetResponse> tweetResponseList = response.body();
                        if (tweetResponseList.size() == 0) {
                            Toast.makeText(getContext(), "No Tweets", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (int i = 0; i < tweetResponseList.size(); i++) {
                            idListFinal.add(tweetResponseList.get(i).getId_str());
                            //   finalUrl.add(tweetResponseList.get(i).getEntities().getUrlsArrayList().get(0).getExpandedUrl());
                        }
                        if (mLinearLayout.getChildCount() > 0)
                            mLinearLayout.removeAllViews();
                        onDownloadComplete(idListFinal);
                    }

                    @Override
                    public void onFailure(Call<List<TweetResponse>> call, Throwable t) {
                        Log.e("onResponse", "fail");
                    }
                });

            }
        });
        Call<List<TweetResponse>> call = apiInterface.getTweetListIdLong(mUserName, 200);
        Log.e("After retrofit", "Yes");
        call.enqueue(new Callback<List<TweetResponse>>() {
            @Override
            public void onResponse(Call<List<TweetResponse>> call, Response<List<TweetResponse>> response) {
                try {
                    List<TweetResponse> tweetResponseList = response.body();
                    if (tweetResponseList.size() == 0) {
                        Toast.makeText(getContext(), "No Tweets", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (int i = 0; i < tweetResponseList.size(); i++) {
                        idListFinal.add(tweetResponseList.get(i).getId_str());
                        //   finalUrl.add(tweetResponseList.get(i).getEntities().getUrlsArrayList().get(0).getExpandedUrl());
                    }
                    onDownloadComplete(idListFinal);
                } catch (Exception e) {
                    Snackbar.make(rootView,"No Search Result",Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TweetResponse>> call, Throwable t) {
                Log.e("onResponse", "fail");
            }
        });
        Log.e("After Call", "Yes");
        return rootView;
    }

    private void onDownloadComplete(final ArrayList<String> finalIds) {

        Log.e("onDownloadComplete", "Yes");
        for (int i = initCount; i < finalIds.size() && i < count; i++) {
            Log.e("Tweet List", finalIds.get(i));
            final int pos = i;
            TweetUtils.loadTweet(Long.parseLong(finalIds.get(i)), new com.twitter.sdk.android.core.Callback<Tweet>() {
                @Override
                public void success(Result<Tweet> result) {
                    Tweet tweet = result.data;
                    CompactTweetView compactTweetView = new CompactTweetView(getContext(), tweet);
                    compactTweetView.setTweetActionsEnabled(true);
                    compactTweetView.animate();
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
                    count++;
                    initCount++;
                    mSwipeView.setRefreshing(false);
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void failure(TwitterException exception) {
                    Log.e("Loadtweet", "Failed");
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
