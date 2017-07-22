package com.example.pradhuman.my__twitter__app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class LogInActivity extends AppCompatActivity {
    TwitterLoginButton loginButton;
    SharedPreferences mSharedPreferences;
    String mLoginCredentials;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        setContentView(R.layout.activity_log_in);
        mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_APP_NAME,MODE_PRIVATE);
        mLoginCredentials = mSharedPreferences.getString(Constants.SHARED_PREFERENCES_STRING_SET,null);
        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                TwitterSession  session = result.data;
                TwitterAuthToken twitterAuthToken = session.getAuthToken();
                mLoginCredentials = twitterAuthToken.token;
                mLoginCredentials = mLoginCredentials+"`"+twitterAuthToken.secret;
                mLoginCredentials = mLoginCredentials+"`"+session.getUserName();
                mLoginCredentials = mLoginCredentials + "`" + session.getId();
                mLoginCredentials = mLoginCredentials+"`"+session.getUserId();

                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(Constants.SHARED_PREFERENCES_STRING_SET,mLoginCredentials);
                editor.commit();
                startActivity(new Intent(LogInActivity.this,HomeActivity.class));
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Toast.makeText(LogInActivity.this,"Couldnot Authenticate You",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LogInActivity.this,SplashActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
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

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }
}
