package com.example.pradhuman.my__twitter__app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashSet;
import java.util.Set;

public class SplashActivity extends AppCompatActivity {
    SharedPreferences mSharedPreferences;
    String mLoginCredentials;
    @Override
    protected void onResume() {
        super.onResume();
        mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_APP_NAME,MODE_PRIVATE);
        mLoginCredentials = mSharedPreferences.getString(Constants.SHARED_PREFERENCES_STRING_SET,null);
        if(mLoginCredentials==null){
            startActivity(new Intent(SplashActivity.this,LogInActivity.class));
        }else{
            startActivity(new Intent(SplashActivity.this,HomeActivity.class));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }
}
