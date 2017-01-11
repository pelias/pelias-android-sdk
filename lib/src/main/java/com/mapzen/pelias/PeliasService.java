package com.mapzen.pelias;

import com.mapzen.pelias.gson.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * HTTP interface for {@link Pelias}.
 */
public interface PeliasService {

  /**
   * Asynchronously request autocomplete results given a query and focus point.
   */
  @GET("/v1/autocomplete") Call<Result> getSuggest(@Query("text") String query,
      @Query("focus.point.lat") double lat, @Query("focus.point.lon") double lon);

  /**
   * Asynchronously request search results given a query and bounding box.
   */
  @GET("/v1/search") Call<Result> getSearch(@Query("text") String query,
      @Query("focus.viewport.min_lat") double minLat,
      @Query("focus.viewport.min_lon") double minLon,
      @Query("focus.viewport.max_lat") double maxLat,
      @Query("focus.viewport.max_lon") double maxLon);

  /**
   * Asynchronously request search results given a query and focus point.
   */
  @GET("/v1/search") Call<Result> getSearch(@Query("text") String query,
      @Query("focus.point.lat") double lat,
      @Query("focus.point.lon") double lon);

  /**
   * Asynchronously issue reverse geocode request.
   */
  @GET("/v1/reverse") Call<Result> getReverse(@Query("point.lat") double lat,
      @Query("point.lon") double lon);

  /**
   * Asynchronously request more information about places given their global unique identifiers.
   */
  @GET("/v1/place") Call<Result> getPlace(@Query("ids") String ids);
}
