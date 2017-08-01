package com.example.pradhuman.my__twitter__app.SuggestedUsers;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pradhuman.my__twitter__app.R;
import com.example.pradhuman.my__twitter__app.networking.ApiInterface;
import com.example.pradhuman.my__twitter__app.networking.GetRetrofit;
import com.example.pradhuman.my__twitter__app.networking.NewApiInterface;
import com.example.pradhuman.my__twitter__app.networking.NewRetrofit;
import com.example.pradhuman.my__twitter__app.networking.ProfileResponse;
import com.example.pradhuman.my__twitter__app.networking.SuggestionResponse;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuggestedUsers extends AppCompatActivity {
    ApiInterface mApiInterface;
    NewApiInterface mNewAPiInterface;
    ExpandableListViewAdapter mExpandableListViewAdapter;
    ExpandableListView mExpandableListView;
    ArrayList<SuggestionResponse> mListDataHeader;
    HashMap<SuggestionResponse, ArrayList<ProfileResponse>> mListDataChild;
    final String TAG = "SuggestedUsers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Suggestions");
        mListDataChild = new HashMap<>();
        mListDataHeader = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mApiInterface = GetRetrofit.getApiInterface();
        hitApi();

        mExpandableListView = (ExpandableListView) findViewById(R.id.lvExp);
        mExpandableListViewAdapter = new ExpandableListViewAdapter(this, mListDataHeader, mListDataChild);
        mExpandableListView.setAdapter(mExpandableListViewAdapter);
    }

    private void hitApi() {
        mNewAPiInterface = NewRetrofit.getApiInterface();
        Call<ArrayList<SuggestionResponse>> call = mNewAPiInterface.getSuggestionResponse();
        call.enqueue(new Callback<ArrayList<SuggestionResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SuggestionResponse>> call, Response<ArrayList<SuggestionResponse>> response) {

                Log.d(TAG, "onResponse: hitApi " + response.code());
                if (response.code() == 200) {
                    ArrayList<SuggestionResponse> list = response.body();
                    mListDataHeader.clear();
                    mListDataHeader.addAll(list);
                    getChild();
                } else {
                    Toast.makeText(SuggestedUsers.this, "Cannot Fetch Suggestions", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<SuggestionResponse>> call, Throwable t) {
                Toast.makeText(SuggestedUsers.this, "Cannot Fetch Suggestions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getChild() {
        int count = 0;
        for (SuggestionResponse s : mListDataHeader) {
            count++;
            hitChildApi(s, s.getSlug());
            if (count == 5)
                break;
        }
    }

    private void hitChildApi(final SuggestionResponse s, String slug) {
        Call<ArrayList<ProfileResponse>> call = mNewAPiInterface.getSlugMembers(slug);
        call.enqueue(new Callback<ArrayList<ProfileResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ProfileResponse>> call, Response<ArrayList<ProfileResponse>> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.code() == 200) {
                    mListDataChild.put(s, response.body());
                    mExpandableListViewAdapter.notifyDataSetChanged();
                }else if(response.code() == 409||response.code() == 209){
                    Snackbar.make(getCurrentFocus(),"Hit Finish",Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProfileResponse>> call, Throwable t) {
                Log.d(TAG, "onFailure: hitChildApi");
            }
        });
    }


}
