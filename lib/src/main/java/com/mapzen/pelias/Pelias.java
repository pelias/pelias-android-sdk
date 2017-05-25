package com.mapzen.pelias;

import com.mapzen.pelias.gson.Result;
import com.mapzen.pelias.http.Tls12OkHttpClientFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Main class for interaction with Pelias.
 */
public class Pelias {
  public static final String DEFAULT_SEARCH_ENDPOINT = "https://search.mapzen.com/";

  private PeliasService service;
  private PeliasLocationProvider locationProvider;
  private PeliasRequestHandler requestHandler;
  private String endpoint = DEFAULT_SEARCH_ENDPOINT;
  private boolean debug = false;
  private Retrofit retrofit;
  private RequestInterceptor requestInterceptor;

  /**
   * Constructs a {@link Pelias} object configured to use the default search endpoint for requests.
   */
  public Pelias() {
    initService();
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
    endpoint = url;
    initService();
  }

  private void initService() {
    requestInterceptor = new RequestInterceptor();
    if (requestHandler != null) {
      requestInterceptor.setRequestHandler(requestHandler);
    }

    final OkHttpClient.Builder clientBuilder = Tls12OkHttpClientFactory.enableTls12OnPreLollipop(
        new OkHttpClient.Builder().addNetworkInterceptor(requestInterceptor));

    if (debug) {
      final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
      logging.setLevel(HttpLoggingInterceptor.Level.BODY);
      clientBuilder.addNetworkInterceptor(logging);
    }

    retrofit = new Retrofit.Builder()
        .baseUrl(endpoint)
        .client(clientBuilder.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    this.service = retrofit.create(PeliasService.class);
  }

  /**
   * Sets a request handler for the object. Set a custom handler to provide extra query params or
   * headers such as api keys.
   */
  public void setRequestHandler(PeliasRequestHandler handler) {
    requestHandler = handler;
    if (requestInterceptor != null) {
      requestInterceptor.setRequestHandler(handler);
    }
  }

  /**
   * Sets endpoint for all http requests.
   * @param endpoint
   */
  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
    initService();
  }

  /**
   * When debugging, http requests are logged.
   * @param debug
   */
  public void setDebug(boolean debug) {
    this.debug = debug;
    initService();
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
   * Returns autocomplete suggestions given a query string. The query will use the
   * {@link PeliasLocationProvider} to retrieve a lat/lon to use as a focus point for the request.
   * Results will be limited by the layers, country, and sources strings. The callback will be
   * notified upon success or failure of query.
   */
  public void suggest(String query, String layers, String country, String sources,
      Callback<Result> callback) {
    service.getSuggest(query, locationProvider.getLat(), locationProvider.getLon(), layers, country,
        sources).enqueue(callback);
  }

  /**
   * Requests autocomplete suggestions given a query and lat/lon. The lat/lon is used as a focus
   * point for results The callback will be notified upon success or failure of the query.
   */
  public void suggest(String query, double lat, double lon, Callback<Result> callback) {
    service.getSuggest(query, lat, lon).enqueue(callback);
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
    service.getSearch(query, box.getMinLat(), box.getMinLon(), box.getMaxLat(), box.getMaxLon())
        .enqueue(callback);
  }

  /**
   * Requests search results given a query. The lat/lon will be used as a focus point to
   * generate relevant results. The callback will be notified upon success or failure of
   * the query.
   */
  public void search(String query, double lat, double lon, Callback<Result> callback) {
    service.getSearch(query, lat, lon).enqueue(callback);
  }

  /**
   * Issues a reverse geocode request given the lat/lon. The callback will be notified upon success
   * or failure of the query.
   */
  public void reverse(double lat, double lon, Callback<Result> callback) {
    service.getReverse(lat, lon).enqueue(callback);
  }

  /**
   * Issues a reverse geocode request given the lat/lon and limited to the sources. The callback
   * will be notified upon success or failure of the query.
   */
  public void reverse(double lat, double lon, String sources, Callback<Result> callback) {
    service.getReverse(lat, lon, sources).enqueue(callback);
  }

  /**
   * Issues a place request for a given global identifier. The callback will be notified upon
   * success or failure of the query.
   */
  public void place(String gid, Callback<Result> callback) {
    service.getPlace(gid).enqueue(callback);
  }

  /**
   * Set a location provider to be used in search and suggest requests. This will be used to return
   * more relevant results for given positions and areas
   */
  public void setLocationProvider(PeliasLocationProvider locationProvider) {
    this.locationProvider = locationProvider;
  }

  /**
   * Returns the http endpoint.
   * @return
   */
  public String getEndpoint() {
    return endpoint;
  }

  /**
   * Returns whether or not debug is enabled.
   * @return
   */
  public boolean getDebug() {
    return debug;
  }
}
