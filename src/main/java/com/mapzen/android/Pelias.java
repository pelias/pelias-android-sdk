package com.mapzen.android;

import com.mapzen.android.gson.Geojson;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public class Pelias {
    private static String endpoint;

    public static void setEndpoint(String endpoint) {
        Pelias.endpoint = endpoint;
    }

    public static void generic(String type, String query, Callback<Geojson> callback) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .build();

        PeliasService service = restAdapter.create(PeliasService.class);

        service.getResults(type, query, callback);
    }

    public static void search(String query, Callback<Geojson> callback) {
        generic("search", query, callback);
    }

    public static void suggest(String query, Callback<Geojson> callback) {
        generic("suggest", query, callback);
    }

    public interface PeliasService {
        @GET("/{type}")
        void getResults(@Path("type") String type, @Query("query") String query, Callback<Geojson> callback);
    }
}