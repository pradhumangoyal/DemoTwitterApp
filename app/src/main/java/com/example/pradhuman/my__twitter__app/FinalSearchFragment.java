package com.example.pradhuman.my__twitter__app;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pradhuman.my__twitter__app.networking.ApiInterface;
import com.example.pradhuman.my__twitter__app.networking.SearchTweetResponse;
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

import static android.content.Context.SEARCH_SERVICE;

public class FinalSearchFragment extends Fragment {
    private String mToken;
    private String mSecret;
    private String mUserId;
    private String mUserName;
    private String mId;
    private SwipeRefreshLayout mSwipeView;
   TextView mTextView;
    LinearLayout mLinearLayout;
    ApiInterface apiInterface;
    ArrayList<String> finalIds;
    private ScrollViewExt mScrollViewExt;
    View rootView;
    String lastSearch;
    public FinalSearchFragment() {
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
         rootView = inflater.inflate(R.layout.fragment_final_search, container, false);
        setHasOptionsMenu(true);
        lastSearch = "";
        mLinearLayout = (LinearLayout) rootView.findViewById(R.id.final_search_linear_layout);
        mTextView = (TextView) rootView.findViewById(R.id.no_search_text_view);
       mTextView.setVisibility(View.VISIBLE);
        mSwipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.final_search_swipe_view);
        mSwipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeView.setRefreshing(true);
                finalIds.clear();

                if(lastSearch!=null&&!lastSearch.trim().isEmpty()&&lastSearch.trim().equals("#"))
                {
                    Call<SearchTweetResponse> call = apiInterface.getSearchTweet(lastSearch, "en");
                    call.enqueue(new Callback<SearchTweetResponse>() {
                        @Override
                        public void onResponse(Call<SearchTweetResponse> call, Response<SearchTweetResponse> response) {
                            try {
                                if (mLinearLayout.getChildCount() != 0)
                                    mLinearLayout.removeAllViews();
                                SearchTweetResponse searchTweetResponse = response.body();
                                finalIds.clear();
                                for (int i = 0; i < searchTweetResponse.getTweetResponseArrayList().size(); i++) {
                                    finalIds.add(searchTweetResponse.getTweetResponseArrayList().get(i).getId_str());
                                }
                                onDownloadComplete();
                            } catch (Exception e) {
                                lastSearch = "";
                                Snackbar.make(rootView,"No Search Result",Snackbar.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SearchTweetResponse> call, Throwable t) {
                            Log.i("onQueryTextChange","Fail");
                        }
                    });
                }
                else{
                    mTextView.setVisibility(View.VISIBLE);
                }
                mSwipeView.setRefreshing(false);

            }
        });
        mScrollViewExt = rootView.findViewById(R.id.final_search_scroll_view);
        finalIds = new ArrayList<String>();
        apiInterface = GetRetrofit.getApiInterface();
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home,menu);
        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                lastSearch = query;
                searchViewAndroidActionBar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               mTextView.setVisibility(View.GONE);
                if(newText!=null&&!newText.trim().isEmpty()&&!newText.trim().equals("#"))
                {
                    lastSearch = newText;
                    Call<SearchTweetResponse> call = apiInterface.getSearchTweet(newText, "en");
                    call.enqueue(new Callback<SearchTweetResponse>() {
                        @Override
                        public void onResponse(Call<SearchTweetResponse> call, Response<SearchTweetResponse> response) {
                            try {

                                SearchTweetResponse searchTweetResponse = response.body();
                                finalIds.clear();
                                for (int i = 0; i < searchTweetResponse.getTweetResponseArrayList().size(); i++) {
                                    finalIds.add(searchTweetResponse.getTweetResponseArrayList().get(i).getId_str());
                                }
                                onDownloadComplete();
                            } catch (Exception e) {
                                lastSearch = "";
                                Snackbar.make(rootView,"No Search Result",Snackbar.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SearchTweetResponse> call, Throwable t) {
                            Log.i("onQueryTextChange","Fail");
                        }
                    });
                }
                else {
                    lastSearch = "";
                    if (mLinearLayout.getChildCount() != 0)
                        mLinearLayout.removeAllViews();
                    mTextView.setVisibility(View.VISIBLE);
                }

                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void onDownloadComplete() {
        if (mLinearLayout.getChildCount() != 0)
            mLinearLayout.removeAllViews();
        for (int i = 0; i < finalIds.size() ; i++) {
            Log.e("Tweet List", finalIds.get(i));
            final int pos = i;
            TweetUtils.loadTweet(Long.parseLong(finalIds.get(i)), new com.twitter.sdk.android.core.Callback<Tweet>() {
                @Override
                public void success(Result<Tweet> result) {
                    Tweet tweet = result.data;
                    CompactTweetView compactTweetView = new CompactTweetView(getContext(), tweet);
                    compactTweetView.setTweetActionsEnabled(true);
                    /*compactTweetView.setOnClickListener(new View.OnClickListener() {
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
                    });*/
                    mLinearLayout.addView(compactTweetView);
                }

                @Override
                public void failure(TwitterException exception) {
                    Log.e("Loadtweet", "Failed");
                }
            });
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                ;
        }
        return true;
    }
}
