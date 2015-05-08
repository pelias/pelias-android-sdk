package com.mapzen.pelias;

import com.mapzen.pelias.gson.Result;

import retrofit.Callback;
import retrofit.RestAdapter;

public class Pelias {
    private PeliasService service;
    public static final String DEFAULT_SERVICE_ENDPOINT = "https://pelias.mapzen.com/";
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
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(serviceEndpoint)
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

    public void suggest(String query, String lat, String lon, Callback<Result> callback) {
        service.getSuggest(query, lat, lon, callback);
    }

    public void suggest(String query, Callback<Result> callback) {
        service.getSuggest(query, locationProvider.getLat(), locationProvider.getLon(), callback);
    }

    public void search(String query, String lat, String lon, Callback<Result> callback) {
        service.getSearch(query, lat, lon, callback);
    }

    public void search(String query, Callback<Result> callback) {
        service.getSearch(query, locationProvider.getLat(), locationProvider.getLon(), callback);
    }

    public void doc(String type, String id, Callback<Result> callback) {
        service.getDoc(type + ":" + id, callback);
    }

    public void setLocationProvider(PeliasLocationProvider locationProvider) {
        this.locationProvider = locationProvider;
    }
}
