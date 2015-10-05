package com.mapzen.pelias;

import com.mapzen.pelias.gson.Result;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface PeliasService {
    @GET("/autocomplete")
    void getSuggest(@Query("text") String query,
                    @Query("focus.point.lat") String lat,
                    @Query("focus.point.lon") String lon,
                    @Query("api_key") String key, Callback<Result> callback);

    @GET("/search")
    void getSearch(@Query("text") String query,
                   @Query("focus.point.lat") String lat,
                   @Query("focus.point.lon") String lon,
                   @Query("boundary.rect.min_lon") String minLon,
                   @Query("boundary.rect.min_lat") String minLat,
                   @Query("boundary.rect.max_lon") String maxLon,
                   @Query("boundary.rect.max_lat") String maxLat,
                   @Query("api_key") String key, Callback<Result> callback);

    @GET("/reverse")
    void getReverse(@Query("point.lat") String lat,
                    @Query("point.lon") String lon,
                    @Query("api_key") String key, Callback<Result> callback);

    @GET("/place")
    void getDoc(@Query("id") String typeAndId,
                @Query("api_key") String key, Callback<Result> callback);
}
