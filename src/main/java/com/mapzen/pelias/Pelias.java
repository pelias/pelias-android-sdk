package com.mapzen.pelias;

import com.mapzen.pelias.gson.Result;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class Pelias {
    private PeliasService service;
    public static final String DEFAULT_SERVICE_ENDPOINT = "https://search.mapzen.com/v1/";
    private String apiKey = "";
    private static Pelias instance = null;
    private PeliasLocationProvider locationProvider;

    private static RestAdapter.LogLevel logLevel;

    public static final String HEADER_DNT = "DNT";
    public static final String VALUE_DNT = "1";
    private boolean dntEnabled = true;

    protected Pelias(PeliasService service) {
        this.service = service;
    }

    private Pelias(String endpoint) {
        initService(endpoint);
    }

    private Pelias() {
        initService(DEFAULT_SERVICE_ENDPOINT);
    }

    private void initService(String serviceEndpoint) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(serviceEndpoint)
                .setLogLevel(logLevel)
                .setRequestInterceptor(new AddHeadersInterceptor())
                .build();
        this.service = restAdapter.create(PeliasService.class);
    }

    public static Pelias getPeliasWithEndpoint(String endpoint) {
        return getPeliasWithEndpoint(endpoint, RestAdapter.LogLevel.NONE);
    }

    public static Pelias getPelias() {
        return getPelias(RestAdapter.LogLevel.NONE);
    }

    public static Pelias getPeliasWithEndpoint(String endpoint, RestAdapter.LogLevel logLevel) {
        Pelias.logLevel = logLevel;

        if (instance == null) {
            instance = new Pelias(endpoint);
        }
        return instance;
    }

    public static Pelias getPelias(RestAdapter.LogLevel logLevel) {
        Pelias.logLevel = logLevel;

        if (instance == null) {
            instance = new Pelias();
        }
        return instance;
    }

    public void suggest(String query, Callback<Result> callback) {
        suggest(query, locationProvider.getLat(), locationProvider.getLon(), callback);
    }

    public void suggest(String query, double lat, double lon, Callback<Result> callback) {
        service.getSuggest(query, lat, lon, apiKey, callback);
    }

    public void search(String query, Callback<Result> callback) {
        search(query, locationProvider.getBoundingBox(), callback);
    }

    public void search(String query, BoundingBox box, Callback<Result> callback) {
        service.getSearch(query, box.minLat, box.minLon, box.maxLat, box.maxLon, apiKey, callback);
    }

    public void search(String query, double lat, double lon, Callback<Result> callback) {
        service.getSearch(query, lat, lon, apiKey, callback);
    }

    public void reverse(double lat, double lon, Callback<Result> callback) {
        service.getReverse(lat, lon, apiKey, callback);
    }

    public void place(String gid, Callback<Result> callback) {
        service.getPlace(gid, apiKey, callback);
    }

    public void setLocationProvider(PeliasLocationProvider locationProvider) {
        this.locationProvider = locationProvider;
    }

    public void setApiKey(String key) {
        apiKey = key;
    }

    /**
     * When header "DNT=1" is present in requests, logs are dropped on server
     *
     * Default is enabled
     */
    public void setDntEnabled(boolean enabled) {
        this.dntEnabled = enabled;
    }

    public boolean isDntEnabled() {
        return dntEnabled;
    }

    private class AddHeadersInterceptor implements RequestInterceptor {

        @Override public void intercept(RequestFacade request) {
            if (dntEnabled) {
                request.addHeader(HEADER_DNT, VALUE_DNT);
            }
        }
    }
}
