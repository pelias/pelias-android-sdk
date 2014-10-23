package com.mapzen.android;

import com.mapzen.android.gson.Result;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface PeliasService {
    @GET("/suggest")
    void getSuggest(@Query("input") String query, @Query("lat") String lat, @Query("lon") String lon, Callback<Result> callback);

    @GET("/search")
    void getSearch(@Query("input") String query, @Query("lat") String lat, @Query("lon") String lon, Callback<Result> callback);

    @GET("/doc")
    void getDoc(@Query("id") String typeAndId, Callback<Result> callback);
}
