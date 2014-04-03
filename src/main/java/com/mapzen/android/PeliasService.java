package com.mapzen.android;

import com.mapzen.android.gson.Result;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface PeliasService {
    @GET("/suggest")
    void getSuggest(@Query("query") String query, Callback<Result> callback);

    @GET("/search")
    void getSearch(@Query("query") String query, @Query("viewbox") String viewbox, Callback<Result> callback);
}
