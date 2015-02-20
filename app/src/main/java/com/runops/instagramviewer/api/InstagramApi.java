package com.runops.instagramviewer.api;

import com.runops.instagramviewer.model.Popular;

import retrofit.RestAdapter;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public class InstagramApi {
    private static InstagramApiInterface instagramService;

    public static InstagramApiInterface getInstagramApiClient() {
        if (instagramService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("https://api.instagram.com/v1")
                    .build();

            instagramService = restAdapter.create(InstagramApiInterface.class);
        }

        return instagramService;
    }


    public interface InstagramApiInterface {
        @GET("/media/popular")
        void getPopularMedia(
                @Query("client_id") String clientId, Callback<Popular> callback);
    }
}
