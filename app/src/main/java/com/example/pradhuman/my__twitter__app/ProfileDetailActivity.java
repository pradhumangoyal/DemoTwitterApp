package com.example.pradhuman.my__twitter__app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pradhuman.my__twitter__app.Data.Profile;
import com.example.pradhuman.my__twitter__app.Fragments.FollowersFragment;
import com.example.pradhuman.my__twitter__app.Fragments.FollowingFragment;
import com.example.pradhuman.my__twitter__app.networking.ProfileResponse;
import com.google.android.exoplayer.C;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileDetailActivity extends AppCompatActivity implements FollowingFragment.OnCompleteListener{

    ImageView mBackGroundImageView;
    ProfileResponse profileResponse;
    static int y = 0;
    @BindView(R.id.full_name)
    TextView fullTv;
    @BindView(R.id.user_name)
    TextView userNameTv;
    @BindView(R.id.followers)
    TextView followersTv;
    @BindView(R.id.following)
    TextView followingTv;
    @BindView(R.id.profile_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.profile_detail_tab)
    TabLayout mTabLayout;
    private ProfileDetailSectionsPagerAdapter mSectionsPagerAdapter;
    Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        setContentView(R.layout.activity_profile_detail);
        ButterKnife.bind(this);
        mSectionsPagerAdapter = new ProfileDetailSectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);
        Intent i = getIntent();
        b = i.getExtras();
        profileResponse = (ProfileResponse) i.getSerializableExtra(Constants.INTENT_PROFILE_RESPONSE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (profileResponse != null) {
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                            .getActiveSession();
                    final Intent intent = new ComposerActivity.Builder(ProfileDetailActivity.this)
                            .session(session)
                            .text("")
                            .hashtags("")
                            .createIntent();
                    startActivity(intent);
                }
            });

            mBackGroundImageView = (ImageView) findViewById(R.id.backgroound_image_view);

            Picasso.with(ProfileDetailActivity.this).load(profileResponse.getBackgroundImage()+"/mobile").into(mBackGroundImageView);
            ImageView profileImageView = (ImageView) findViewById(R.id.profile_detail_profile_image_view);
            Log.i("ProfileBackground",profileResponse.getProfileImage().substring(0,profileResponse.getProfileImage().length()-11)+".jpg");
            Log.i("ProfileBackground",profileResponse.getProfileImage());
            Picasso.with(ProfileDetailActivity.this).load(profileResponse.getProfileImage().substring(0,profileResponse.getProfileImage().length()-11)+".jpg").resize(200,200).centerCrop().into(profileImageView);
            fullTv.setText(profileResponse.getName());
            String habitnumber =  "<b> "+profileResponse.getFollowersCount()+ "</b>" + " Followers "   ;
            String folloein = "<b>"+(profileResponse.getFollowingCount()-y)+"</b>"+" Following";
            userNameTv.setText("@"+profileResponse.getScreenName());
            followersTv.setText(Html.fromHtml(habitnumber));
            followingTv.setText(Html.fromHtml(folloein));
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onComplete() {
        y++;
        String folloein = "<b>"+(profileResponse.getFollowingCount()-y)+"</b>"+" Following";
        followingTv.setText(Html.fromHtml(folloein));
    }

    public class ProfileDetailSectionsPagerAdapter extends FragmentPagerAdapter{

        public ProfileDetailSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    FollowingFragment followingFragment = new FollowingFragment();
                    followingFragment.setArguments(b);
                    return followingFragment;
                default:
                    FollowersFragment followersFragment = new FollowersFragment();
                    followersFragment.setArguments(b);
                    return followersFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Following";
                case 1:
                    return "Followers";
            }
            return null;
        }
    }
}
