package com.mapzen.android;

public class Pelias {
    public static Object search(String query, Callback callback) {
        if (callback != null) {
            callback.onSuccess();
            callback.onError();
        }
        return new Object();
    }

    public static Object suggest(String query, Callback callback) {
        if (callback != null) {
            callback.onSuccess();
            callback.onError();
        }
        return new Object();
    }


}