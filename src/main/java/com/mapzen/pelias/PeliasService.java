package com.mapzen.pelias;

import com.mapzen.pelias.gson.Result;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface PeliasService {
    @GET("/autocomplete")
    void getSuggest(
            @Query("text") String query,
            @Query("focus.point.lat") double lat,
            @Query("focus.point.lon") double lon,
            @Query("api_key") String key,
            Callback<Result> callback);

    @GET("/search")
    void getSearch(
            @Query("text") String query,
            @Query("focus.viewport.min_lat") double minLat,
            @Query("focus.viewport.min_lon") double minLon,
            @Query("focus.viewport.max_lat") double maxLat,
            @Query("focus.viewport.max_lon") double maxLon,
            @Query("api_key") String key,
            Callback<Result> callback);

    @GET("/search")
    void getSearch(
            @Query("text") String query,
            @Query("focus.point.lat") double lat,
            @Query("focus.point.lon") double lon,
            @Query("api_key") String key,
            Callback<Result> callback);

    @GET("/reverse")
    void getReverse(
            @Query("point.lat") double lat,
            @Query("point.lon") double lon,
            @Query("api_key") String key,
            Callback<Result> callback);

    @GET("/place")
    void getPlace(@Query("ids") String ids,
            @Query("api_key") String key,
            Callback<Result> callback);
}
