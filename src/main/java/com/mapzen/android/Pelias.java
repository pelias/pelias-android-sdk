package com.mapzen.android;

import com.mapzen.android.gson.Result;

import retrofit.Callback;
import retrofit.RestAdapter;

public class Pelias {
    PeliasService service;
    String endpoint = "http://pelias.test.mapzen.com/";

    protected Pelias(PeliasService service) {
        this.service = service;
    }

    public Pelias() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .build();
        this.service = restAdapter.create(PeliasService.class);
    }

    public void suggest(String query, Callback<Result> callback) {
        service.getSuggest(query, callback);
    }

    public void search(String query, String viewbox, Callback<Result> callback) {
        service.getSearch(query, viewbox, callback);
    }
}