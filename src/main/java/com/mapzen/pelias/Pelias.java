package com.mapzen.pelias;

import com.mapzen.pelias.gson.Result;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class Pelias {
    private PeliasService service;
    public static final String DEFAULT_SERVICE_ENDPOINT = "https://search.mapzen.com/v1/";
    private String apiKey = "";
    static Pelias instance = null;
    private PeliasLocationProvider locationProvider;

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
        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new LoggingInterceptor());

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(serviceEndpoint)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient((client)))
                .build();
        this.service = restAdapter.create(PeliasService.class);
    }

    public static Pelias getPeliasWithEndpoint(String endpoint) {
        if (instance == null) {
            instance = new Pelias(endpoint);
        }
        return instance;
    }

    public static Pelias getPelias() {
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

    public void setLocationProvider(PeliasLocationProvider locationProvider) {
        this.locationProvider = locationProvider;
    }

    public void setApiKey(String key) {
        apiKey = key;
    }

    class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            android.util.Log.i("Pelias", String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            android.util.Log.i("Pelias", String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }
}
