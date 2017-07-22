package com.example.pradhuman.my__twitter__app;

import android.util.Log;

import com.github.scribejava.core.model.ParameterList;
import com.github.scribejava.core.services.HMACSha1SignatureService;
import com.github.scribejava.core.services.TimestampServiceImpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OAuthInterceptor implements Interceptor {

    private static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
    private static final String OAUTH_NONCE = "oauth_nonce";
    private static final String OAUTH_SIGNATURE = "oauth_signature";
    private static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
    private static final String OAUTH_SIGNATURE_METHOD_VALUE = "HMAC-SHA1";
    private static final String OAUTH_TIMESTAMP = "oauth_timestamp";
    private static final String OAUTH_VERSION = "oauth_version";
    private static final String OAUTH_VERSION_VALUE = "1.0";
    private static final String OAUTH_TOKEN = "oauth_token";

    private final String consumerKey;
    private final String consumerSecret;
    private final String userToken;
    private final String userSecret;

    private OAuthInterceptor(String consumerKey, String consumerSecret, String userToken,String userSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.userSecret = userSecret;
        this.userToken = userToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();

        Log.d("URL", original.url().toString());
        Log.d("URL", original.url().scheme());
        Log.d("encodedpath", original.url().encodedPath());
        Log.d("query", ""+original.url().query());
        Log.d("path", ""+original.url().host());
        Log.d("encodedQuery", ""+original.url().encodedQuery());
        ;
        Log.d("method", ""+original.method());

        ////////////////////////////////////////////////////////////

        final String nonce = new TimestampServiceImpl().getNonce();
        final String timestamp = new TimestampServiceImpl().getTimestampInSeconds();
        Log.d("nonce", nonce);
        Log.d("time", timestamp);

        String dynamicStructureUrl = original.url().scheme() + "://" + original.url().host() + original.url().encodedPath();

        Log.d("ENCODED PATH", ""+dynamicStructureUrl);
        String firstBaseString = original.method() + "&" + urlEncoded(dynamicStructureUrl);
        Log.d("firstBaseString", firstBaseString);
        String generatedBaseString = "";


        if(original.url().encodedQuery()!=null) {
            generatedBaseString = original.url().encodedQuery() + "&oauth_consumer_key=" + consumerKey +"&oauth_token="+userToken+ "&oauth_nonce=" + nonce + "&oauth_signature_method=HMAC-SHA1&oauth_timestamp=" + timestamp + "&oauth_version=1.0";
        }
        else
        {
            generatedBaseString = "oauth_consumer_key=" + consumerKey + "&oauth_nonce=" + nonce + "&oauth_signature_method=HMAC-SHA1&oauth_timestamp=" + timestamp + "&oauth_version=1.0";

        }

        ParameterList result = new ParameterList();
        result.addQuerystring(generatedBaseString);
        generatedBaseString=result.sort().asOauthBaseString();
        Log.d("Sorted","00--"+result.sort().asOauthBaseString());

        String secoundBaseString = "&" + generatedBaseString;

        if (firstBaseString.contains("%3F")) {
            Log.d("iff","yess iff");
            secoundBaseString = "%26" + urlEncoded(generatedBaseString);
        }

        String baseString = firstBaseString + secoundBaseString;

        String signature = new HMACSha1SignatureService().getSignature(baseString, consumerSecret, userSecret);
        Log.d("Signature", signature);

        HttpUrl url = originalHttpUrl.newBuilder()

                .addQueryParameter(OAUTH_SIGNATURE_METHOD, OAUTH_SIGNATURE_METHOD_VALUE)
                .addQueryParameter(OAUTH_CONSUMER_KEY, consumerKey)
                .addQueryParameter(OAUTH_VERSION, OAUTH_VERSION_VALUE)
                .addQueryParameter(OAUTH_TIMESTAMP, timestamp)
                .addQueryParameter(OAUTH_NONCE, nonce)
                .addQueryParameter(OAUTH_TOKEN, userToken)
                .addQueryParameter(OAUTH_SIGNATURE, signature)
                .build();

        // Request customization: add request headers
        Request.Builder requestBuilder = original.newBuilder()
                .url(url);

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }


    public static final class Builder {

        private String consumerKey;
        private String consumerSecret;
        private String userToken;
        private String userSecret;
        private int type;

        public Builder consumerKey(String consumerKey) {
            if (consumerKey == null) throw new NullPointerException("consumerKey = null");
            this.consumerKey = consumerKey;
            return this;
        }

        public Builder consumerSecret(String consumerSecret) {
            if (consumerSecret == null) throw new NullPointerException("consumerSecret = null");
            this.consumerSecret = consumerSecret;
            return this;
        }
        public Builder userSecret(String userSecret) {
            if (userSecret == null) throw new NullPointerException("userSecret = null");
            this.userSecret = userSecret;
            return this;
        }
        public Builder userToken(String userToken) {
            if (userToken == null) throw new NullPointerException("userToken = null");
            this.userToken = userToken;
            return this;
        }


        public OAuthInterceptor build() {

            if (consumerKey == null) throw new IllegalStateException("consumerKey not set");
            if (consumerSecret == null) throw new IllegalStateException("consumerSecret not set");
            if(userSecret == null) throw new IllegalStateException("userSecret not set");
            if(userToken == null) throw new IllegalStateException("userToken not set");
            return new OAuthInterceptor(consumerKey, consumerSecret,userToken,userSecret);
        }
    }

    public String urlEncoded(String url) {
        String encodedurl = "";
        try {

            encodedurl = URLEncoder.encode(url, "UTF-8");
            Log.d("TEST", encodedurl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedurl;
    }
}