package com.mapzen.pelias;

import com.mapzen.pelias.gson.Result;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class Pelias {
    public static final String DEFAULT_SEARCH_ENDPOINT = "https://search.mapzen.com/v1/";

    private PeliasService service;
    private PeliasLocationProvider locationProvider;

    public Pelias() {

    }

    public void setService(PeliasService service) {
        this.service = service;
    }

    public void suggest(String query, Callback<Result> callback) {
        suggest(query, locationProvider.getLat(), locationProvider.getLon(), callback);
    }

    public void suggest(String query, double lat, double lon, Callback<Result> callback) {
        service.getSuggest(query, lat, lon, callback);
    }

    public void search(String query, Callback<Result> callback) {
        search(query, locationProvider.getBoundingBox(), callback);
    }

    public void search(String query, BoundingBox box, Callback<Result> callback) {
        service.getSearch(query, box.minLat, box.minLon, box.maxLat, box.maxLon, callback);
    }

    public void search(String query, double lat, double lon, Callback<Result> callback) {
        service.getSearch(query, lat, lon, callback);
    }

    public void reverse(double lat, double lon, Callback<Result> callback) {
        service.getReverse(lat, lon, callback);
    }

    public void place(String gid, Callback<Result> callback) {
        service.getPlace(gid, callback);
    }

    public void setLocationProvider(PeliasLocationProvider locationProvider) {
        this.locationProvider = locationProvider;
    }

}
