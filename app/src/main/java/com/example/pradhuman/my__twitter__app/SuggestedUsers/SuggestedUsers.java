package com.example.pradhuman.my__twitter__app.SuggestedUsers;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pradhuman.my__twitter__app.R;
import com.example.pradhuman.my__twitter__app.networking.ApiInterface;
import com.example.pradhuman.my__twitter__app.networking.GetRetrofit;
import com.example.pradhuman.my__twitter__app.networking.SuggestionResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuggestedUsers extends AppCompatActivity implements SuggestedUserRecyclerAdapter.onSuggestionListItemClickListener {
    @BindView(R.id.content_suggested_users_recycler_view)
    RecyclerView mRecyclerView;
    SuggestedUserRecyclerAdapter mAdapter;
    ArrayList<SuggestionResponse> mArrayList;
    ApiInterface mApiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_users);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Suggestions");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mApiInterface = GetRetrofit.getApiInterface();
        hitApi();
    }

    private void hitApi() {
        Call<ArrayList<SuggestionResponse> > call = mApiInterface.getSuggestionResponse("en");
        call.enqueue(new Callback<ArrayList<SuggestionResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SuggestionResponse>> call, Response<ArrayList<SuggestionResponse>> response) {
                if(response.code()==200){
                    mArrayList = response.body();
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(SuggestedUsers.this,LinearLayoutManager.VERTICAL,false));
                    mAdapter = new SuggestedUserRecyclerAdapter(SuggestedUsers.this,mArrayList);
                    mAdapter.setOnSuggestionListItemClickListener(SuggestedUsers.this);
                    mRecyclerView.setAdapter(mAdapter);
                }else{
                    Toast.makeText(SuggestedUsers.this,"Cannot Fetch Suggestions",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SuggestionResponse>> call, Throwable t) {
                Toast.makeText(SuggestedUsers.this,"Cannot Fetch Suggestions",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onSuggestionItemClickListener(int count, String slug, String name,View itemView, int position) {
        /*ImageView changeImageView = itemView.findViewById(R.id.arrow_change_image_view);
        if(changeImageView == null)
            return;
        Animation animation = AnimationUtils.loadAnimation(SuggestedUsers.this,R.anim.rotate);
        changeImageView.startAnimation(animation);
        if(changeImageView.getTag().equals("R.drawable.ic_keyboard_arrow_down_black_24dp")){
            changeImageView.setTag("R.drawable.ic_keyboard_arrow_up_black_24dp");


        }else {
            changeImageView.setTag("R.drawable.ic_keyboard_arrow_down_black_24dp");

        }*/
    }
}
