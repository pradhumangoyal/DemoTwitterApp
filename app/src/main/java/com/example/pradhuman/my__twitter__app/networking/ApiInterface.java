package com.example.pradhuman.my__twitter__app.networking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Pradhuman on 19-07-2017.
 */

public interface ApiInterface {

    @GET("favorites/list.json")
    Call<List<TweetResponse> >getTweetsArrayList(@Query("screen_name") String screenName,@Query("count") int count);


    @GET("statuses/home_timeline.json")
    Call<List<TweetResponse> >getTweetListIdLong(@Query("screen_name") String screenName,@Query("count") int count);

    @GET("users/show.json")
    Call<ProfileResponse> getProfileImage(@Query("screen_name")String screenName);

    @GET("search/tweets.json")
    Call<SearchTweetResponse> getSearchTweet(@Query("q")String query, @Query("lang") String lang);

    @GET("followers/list.json")
    Call<FollowerResponse> getFollowerList(@Query("cursor") int cursor, @Query("screen_name") String screenName,
                                           @Query("include_user_entities") boolean userEntities, @Query("skip_status")boolean skipStatus);

    @GET("friends/list.json")
    Call<FollowerResponse> getFollowingList(@Query("cursor") int cursor, @Query("screen_name") String screenName,
                                           @Query("include_user_entities") boolean userEntities, @Query("skip_status")boolean skipStatus);

    @GET("direct_messages.json")
    Call<ArrayList<AllMessageResponse> > getAllMessage(@Query("count")int count);

    @GET("direct_messages/sent.json")
    Call<ArrayList<AllMessageResponse> > getSentMessage(@Query("count")int count);

    @POST("direct_messages/new.json")
    Call<PostResponse> getPostResponse(@Query("text") String text, @Query("screen_name") String screenName,@Query("user_id") String id);

    @POST("friendships/destroy.json")
    Call<PostResponse> getUnFollowResponse(@Query("screen_name") String text,@Query("user_id") String id);

    @POST("statuses/destroy/{id}.json")
    Call<PostResponse> getDestroyTweet(@Path("id") long id);

    @POST("direct_messages/destroy.json")
    Call<PostResponse> postDestroyMessage(@Query("id") String id);

    @GET("users/suggestions.json")
    Call<ArrayList<SuggestionResponse> > getSuggestionResponse(@Query("lang") String lang);

    @GET("users/suggestions/{slug}.json")
    Call<SlugResponse> getSuggestionSlug(@Query("lang") String lang, @Path("slug") String slug);
}
