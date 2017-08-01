package com.example.pradhuman.my__twitter__app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pradhuman.my__twitter__app.SuggestedUsers.SuggestedUsers;
import com.example.pradhuman.my__twitter__app.networking.ApiInterface;
import com.example.pradhuman.my__twitter__app.networking.GetRetrofit;
import com.example.pradhuman.my__twitter__app.networking.ProfileResponse;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    SharedPreferences mSharedPreferences;
    String mLoginCredentials;
    String[] mLoginAllCredentials;
    Retrofit mRetrofit;
    NavigationView navigationView;
    ProfileResponse profileResponse;
    Bundle b;
    boolean doubleBackToExitPressedOnce = false;
    OkHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        setContentView(R.layout.drawer_layout);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_APP_NAME, MODE_PRIVATE);
        mLoginCredentials = mSharedPreferences.getString(Constants.SHARED_PREFERENCES_STRING_SET, null);
        mLoginAllCredentials = mLoginCredentials.split("`");
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        toolbar.setTitle("Home");
                        break;
                    case 1:
                        toolbar.setTitle("My Tweets");
                        break;
                    case 2:
                        toolbar.setTitle("Favourites");
                        break;
                    default:
                        toolbar.setTitle("Search");
                        break;
                }
                setSupportActionBar(toolbar);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        TabLayout tabLayout = (TabLayout) findViewById(R.id.profile_detail_tab);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_event_note_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_favorite_black_24dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_search_black_24dp);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                        .getActiveSession();
                final Intent intent = new ComposerActivity.Builder(HomeActivity.this)
                        .session(session)
                        .text("")
                        .hashtags("")
                        .createIntent();
                startActivity(intent);
            }
        });
        GetRetrofit.initialize(mLoginAllCredentials[0],mLoginAllCredentials[1]);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        b = new Bundle();
        b.putString(Constants.AUHENTICATED_TOKEN, mLoginAllCredentials[0]);
        b.putString(Constants.AUTHENTICATED_SECRET, mLoginAllCredentials[1]);
        b.putString(Constants.AUTHENTICATED_USERNAME, mLoginAllCredentials[2]);
        b.putString(Constants.AUTHENTICATED_ID, mLoginAllCredentials[3]);
        b.putString(Constants.AUTHENTICATED_USER_ID, mLoginAllCredentials[4]);
        hitApi();
    }


    public void hitApi() {
        final ApiInterface apiInterface = GetRetrofit.getApiInterface();
        Call<ProfileResponse> call = apiInterface.getProfileImage(mLoginAllCredentials[2]);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                try {
                    Log.i("Profile Image", "Pass");
                    profileResponse = response.body();
                    String url;
                    final View headerLayout = navigationView.getHeaderView(0);
                    de.hdodenhof.circleimageview.CircleImageView imageView = headerLayout.findViewById(R.id.imageViewProfile);
                    if (profileResponse != null) {
                        url = profileResponse.getProfileImage().substring(0,profileResponse.getProfileImage().length()-11)+".jpg";
                        Picasso.with(HomeActivity.this).load(url).centerCrop().resize(200, 200).into(imageView);
                    } else {
                        Picasso.with(HomeActivity.this).load(R.drawable.egg).into(imageView);
                    }
                    TextView nameTextView = headerLayout.findViewById(R.id.full_name);
                    TextView userTextView = headerLayout.findViewById(R.id.screen_name);
                    nameTextView.setText(profileResponse.getName());
                    nameTextView.setTextColor(Color.BLACK);
                    userTextView.setText("@" + mLoginAllCredentials[2]);
                    userTextView.setTextColor(Color.GRAY);
                } catch (Exception e) {
                    Toast.makeText(HomeActivity.this,"Internet Not Working",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Log.e("Profile Image", "Fail");
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_help) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(Constants.SUPPORT_HELP));
            startActivity(i);
        } else if (id == R.id.nav_lists) {
            Intent intent = new Intent(HomeActivity.this,MessageActivity.class);
            intent.putExtra(Constants.INTENT_USER_ID,mLoginAllCredentials[4]);
            startActivity(intent);

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(HomeActivity.this, ProfileDetailActivity.class);
            intent.putExtra(Constants.INTENT_PROFILE_RESPONSE, profileResponse);
            startActivity(intent);

        } else if (id == R.id.nav_trending) {
            startActivity(new Intent(HomeActivity.this, TrendingActivity.class));

        } else {
            if (id == R.id.logout) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Logout");
                builder.setCancelable(false) ;
                builder.setMessage("Do you really want to logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences  mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_APP_NAME,MODE_PRIVATE);
                        mSharedPreferences.edit().clear().commit();
                        TwitterCore.getInstance().getSessionManager().clearActiveSession();
                        startActivity(new Intent(HomeActivity.this,LogInActivity.class));
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else if(id == R.id.suggestions_nav){
                startActivity(new Intent(HomeActivity.this, SuggestedUsers.class));
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else if (!doubleBackToExitPressedOnce) {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this,"Please click BACK again to exit.", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {
                super.onBackPressed();
                return;
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            if (position == 0) {
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(b);
                return homeFragment;
            } else if (position == 1) {

                SearchFragment searchFragment = new SearchFragment();
                searchFragment.setArguments(b);
                return searchFragment;
            } else if (position == 2) {
                NotificationFragment notificationFragment = new NotificationFragment();
                notificationFragment.setArguments(b);
                return notificationFragment;
            } else {
                FinalSearchFragment finalSearchFragment = new FinalSearchFragment();
                finalSearchFragment.setArguments(b);
                return finalSearchFragment;
            }


        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "";
                case 1:
                    return "";
                case 2:
                    return "";
                case 3:
                    return "";
            }
            return null;
        }
    }


}
