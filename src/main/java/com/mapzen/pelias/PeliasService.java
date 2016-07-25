package com.mapzen.pelias;

import com.mapzen.pelias.gson.Result;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * HTTP interface for {@link Pelias}.
 */
public interface PeliasService {

  /**
   * Asynchronously request autocomplete results given a query and focus point.
   */
  @GET("/autocomplete") void getSuggest(@Query("text") String query,
      @Query("focus.point.lat") double lat, @Query("focus.point.lon") double lon,
      Callback<Result> callback);

  /**
   * Asynchronously request search results given a query and bounding box.
   */
  @GET("/search") void getSearch(@Query("text") String query,
      @Query("focus.viewport.min_lat") double minLat,
      @Query("focus.viewport.min_lon") double minLon,
      @Query("focus.viewport.max_lat") double maxLat,
      @Query("focus.viewport.max_lon") double maxLon, Callback<Result> callback);

  /**
   * Asynchronously request search results given a query and focus point.
   */
  @GET("/search") void getSearch(@Query("text") String query, @Query("focus.point.lat") double lat,
      @Query("focus.point.lon") double lon, Callback<Result> callback);

  /**
   * Asynchronously issue reverse geocode request.
   */
  @GET("/reverse") void getReverse(@Query("point.lat") double lat, @Query("point.lon") double lon,
      Callback<Result> callback);

  /**
   * Asynchronously request more information about places given their global unique identifiers.
   */
  @GET("/place") void getPlace(@Query("ids") String ids, Callback<Result> callback);
}
