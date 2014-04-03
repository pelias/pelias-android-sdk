package com.mapzen.android;

import com.mapzen.android.gson.Result;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PeliasTest {
    Pelias pelias;
    TestCallback callback;
    PeliasService mock;

    @Captor
    @SuppressWarnings("unused")
    private ArgumentCaptor<Callback<Result>> cb;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        callback = new TestCallback();
        mock = Mockito.mock(PeliasService.class);
        pelias = new Pelias(mock);
    }

    @Test
    public void search_getSearch() throws Exception {
        pelias.search("test", "1,2,3,4", callback);
        Mockito.verify(mock).getSearch(Mockito.eq("test"), Mockito.eq("1,2,3,4"), cb.capture());
    }

    @Test
    public void suggest_getSuggest() throws Exception {
        pelias.suggest("test", callback);
        Mockito.verify(mock).getSuggest(Mockito.eq("test"), cb.capture());
    }

    class TestCallback implements Callback<Result> {
        @Override
        public void success(Result o, Response response) {
        }

        @Override
        public void failure(RetrofitError retrofitError) {
        }
    }
}
