package com.example.pradhuman.my__twitter__app.networking;

import com.example.pradhuman.my__twitter__app.Constants;
import com.example.pradhuman.my__twitter__app.OAuthInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Pradhuman on 21-07-2017.
 */

public class GetRetrofit {
    static  OkHttpClient client;
    static  String mUserToken;
    static String mUserSecret;
    static Retrofit mRetrofit;
    static ApiInterface apiInterface;
    public static void initialize(String userToken, String userSecret){
       mUserSecret= userSecret;
        mUserToken = userToken;
        httpOk();
    }
    public static void httpOk(){
        OAuthInterceptor oauth1Woocommerce = new OAuthInterceptor.Builder()
                .consumerKey(Constants.CONSUMER_KEY)
                .consumerSecret(Constants.CONSUMER_SECRET)
                .userToken(mUserToken)
                .userSecret(mUserSecret)
                .build();
        client = new OkHttpClient.Builder()
                .addInterceptor(oauth1Woocommerce)// Interceptor oauth1Woocommerce added
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.twitter.com/1.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        apiInterface = mRetrofit.create(ApiInterface.class);
    }
    public static ApiInterface getApiInterface(){
        return apiInterface;
    }
}
