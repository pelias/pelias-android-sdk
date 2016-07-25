package com.mapzen.pelias;

import com.mapzen.pelias.gson.Result;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Main class for interaction with Pelias.
 */
public class Pelias {
  public static final String DEFAULT_SEARCH_ENDPOINT = "https://search.mapzen.com/v1/";

  private PeliasService service;
  private PeliasLocationProvider locationProvider;
  private PeliasRequestHandler requestHandler;

  /**
   * Constructs a {@link Pelias} object configured to use the default search endpoint for requests.
   */
  public Pelias() {
    initService(DEFAULT_SEARCH_ENDPOINT);
  }

  /**
   * Constructs a {@link Pelias} object configured to use the default provided
   * {@link PeliasService} for requests.
   */
  public Pelias(PeliasService service) {
    this.service = service;
  }

  /**
   * Constructs a {@link Pelias} object configured to use the provided url for requests.
   */
  public Pelias(String url) {
    initService(url);
  }

  private void initService(String endpoint) {
    RestAdapter adapter = new RestAdapter.Builder().setEndpoint(endpoint)
        .setRequestInterceptor(new RequestInterceptor() {
          @Override public void intercept(RequestFacade request) {
            if (requestHandler != null) {
              if (requestHandler.headersForRequest() != null) {
                for (String key : requestHandler.headersForRequest().keySet()) {
                  request.addHeader(key, requestHandler.headersForRequest().get(key));
                }
              }
              if (requestHandler.queryParamsForRequest() != null) {
                for (String key : requestHandler.queryParamsForRequest().keySet()) {
                  request.addQueryParam(key, requestHandler.queryParamsForRequest().get(key));
                }
              }
            }
          }
        })
        .build();
    this.service = adapter.create(PeliasService.class);
  }

  /**
   * Sets a request handler for the object. Set a custom handler to provide extra query params or
   * headers such as api keys.
   */
  public void setRequestHandler(PeliasRequestHandler handler) {
    requestHandler = handler;
  }

  /**
   * Sets the service to be used. Set a custom service to provide custom configurations for
   * search urls and logging levels.
   * @param service
   */
  public void setService(PeliasService service) {
    this.service = service;
  }

  /**
   * Returns autocomplete suggestions given a query string. The query will use the
   * {@link PeliasLocationProvider} to retrieve a lat/lon to use as a focus point for the request.
   * The callback will be notified upon success or failure of query.
   */
  public void suggest(String query, Callback<Result> callback) {
    suggest(query, locationProvider.getLat(), locationProvider.getLon(), callback);
  }

  /**
   * Requests autocomplete suggestions given a query and lat/lon. The lat/lon is used as a focus
   * point for results The callback will be notified upon success or failure of the query.
   */
  public void suggest(String query, double lat, double lon, Callback<Result> callback) {
    service.getSuggest(query, lat, lon, callback);
  }

  /**
   * Requests search results given a query. The {@link PeliasLocationProvider} will be used to
   * generate a bounding box for results. The callback will be notified upon success or failure of
   * the query.
   */
  public void search(String query, Callback<Result> callback) {
    search(query, locationProvider.getBoundingBox(), callback);
  }

  /**
   * Requests search results given a query. The {@link BoundingBox} will be used to
   * generate relevant results. The callback will be notified upon success or failure of
   * the query.
   */
  public void search(String query, BoundingBox box, Callback<Result> callback) {
    service.getSearch(query, box.getMinLat(), box.getMinLon(), box.getMaxLat(), box.getMaxLon(),
        callback);
  }

  /**
   * Requests search results given a query. The lat/lon will be used as a focus point to
   * generate relevant results. The callback will be notified upon success or failure of
   * the query.
   */
  public void search(String query, double lat, double lon, Callback<Result> callback) {
    service.getSearch(query, lat, lon, callback);
  }

  /**
   * Issues a reverse geocode request given the lat/lon. The callback will be notified upon success
   * or failure of the query.
   */
  public void reverse(double lat, double lon, Callback<Result> callback) {
    service.getReverse(lat, lon, callback);
  }

  /**
   * Issues a place request for a given global identifier. The callback will be notified upon
   * success or failure of the query.
   */
  public void place(String gid, Callback<Result> callback) {
    service.getPlace(gid, callback);
  }

  /**
   * Set a location provider to be used in search and suggest requests. This will be used to return
   * more relevant results for given positions and areas
   */
  public void setLocationProvider(PeliasLocationProvider locationProvider) {
    this.locationProvider = locationProvider;
  }
}
