package com.mapzen.android;

import com.mapzen.android.gson.Result;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static org.fest.assertions.api.Assertions.assertThat;

public class PeliasTest {
    Pelias peliasWithMock;
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
        peliasWithMock = new Pelias(mock);
    }

    @Test
    public void doc_getDocument() throws Exception {
        peliasWithMock.doc("osmnode", "15", callback);
        Mockito.verify(mock).getDoc(Mockito.eq("osmnode:15"), cb.capture());
    }

    @Test
    public void search_getSearch() throws Exception {
        peliasWithMock.search("test", "1", "2", callback);
        Mockito.verify(mock).getSearch(Mockito.eq("test"), Mockito.eq("1"), Mockito.eq("2"), cb.capture());
    }

    @Test
    public void suggest_getSuggest() throws Exception {
        peliasWithMock.suggest("test", "1", "2", callback);
        Mockito.verify(mock).getSuggest(Mockito.eq("test"), Mockito.eq("1"), Mockito.eq("2"), cb.capture());
    }

    @Test
    public void setEndpoint_shouldChangeServiceEndpoint() throws Exception {
        final MockWebServer server = new MockWebServer();
        MockResponse response = new MockResponse();
        server.enqueue(response);
        server.play();
        Pelias pelias = Pelias.getPeliasWithEndpoint(server.getUrl("/").toString());
        pelias.suggest("test", "1", "2", callback);
        RecordedRequest request = server.takeRequest();
        assertThat(request.getPath()).contains("/suggest");
        server.shutdown();
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
