package com.mapzen.android;

import com.mapzen.android.gson.Result;

import retrofit.Callback;
import retrofit.RestAdapter;

public class Pelias {
    PeliasService service;
    static String endpoint = "http://pelias.test.mapzen.com/";
    static Pelias instance = null;

    protected Pelias(PeliasService service) {
        this.service = service;
    }

    protected static void setInstance(PeliasService service) {
        Pelias.instance = new Pelias(service);
    }

    private Pelias() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .build();
        this.service = restAdapter.create(PeliasService.class);
    }

    public static Pelias getPelias() {
        if (instance == null) {
            instance = new Pelias();
        }
        return instance;
    }

    public void suggest(String query, Callback<Result> callback) {
        service.getSuggest(query, callback);
    }

    public void search(String query, String viewbox, Callback<Result> callback) {
        service.getSearch(query, viewbox, callback);
    }
}