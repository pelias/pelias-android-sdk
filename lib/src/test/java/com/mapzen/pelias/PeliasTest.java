package com.mapzen.pelias;

import com.mapzen.pelias.gson.Result;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeliasTest {
  Pelias peliasWithMock;
  TestCallback callback;
  PeliasService mock;

  @Captor @SuppressWarnings("unused") private ArgumentCaptor<Callback<Result>> cb;

  @Before public void setup() throws Exception {
    MockitoAnnotations.initMocks(this);
    callback = new TestCallback();
    mock = Mockito.mock(PeliasService.class);
    peliasWithMock = new Pelias(mock);
  }

  @Test public void search_getSearch() throws Exception {
    when(mock.getSearch(anyString(), anyDouble(), anyDouble(), anyDouble(), anyDouble()))
        .thenReturn(new TestCall());
    BoundingBox boundingBox = new BoundingBox(1.0, 2.0, 3.0, 4.0);
    peliasWithMock.search("test", boundingBox, callback);
    verify(mock).getSearch(eq("test"), eq(1.0), eq(2.0), eq(3.0), eq(4.0));
  }

  @Test public void search_getSearchWithLocationProvider() throws Exception {
    when(mock.getSearch(anyString(), anyDouble(), anyDouble(), anyDouble(), anyDouble()))
        .thenReturn(new TestCall());
    peliasWithMock.setLocationProvider(new TestLocationProvider());
    peliasWithMock.search("test", callback);
    verify(mock).getSearch(eq("test"), eq(3.0), eq(4.0), eq(5.0), eq(6.0));
  }

  @Test public void search_getSearchWithFocusPoint() throws Exception {
    when(mock.getSearch(anyString(), anyDouble(), anyDouble())).thenReturn(new TestCall());
    peliasWithMock.search("test", 1.0, 2.0, callback);
    verify(mock).getSearch(eq("test"), eq(1.0), eq(2.0));
  }

  @Test public void suggest_getSuggest() throws Exception {
    when(mock.getSuggest(anyString(), anyDouble(), anyDouble())).thenReturn(new TestCall());
    peliasWithMock.suggest("test", 1.0, 2.0, callback);
    verify(mock).getSuggest(eq("test"), eq(1.0), eq(2.0));
  }

  @Test public void suggest_getSuggestWithLocationProvider() throws Exception {
    when(mock.getSuggest(anyString(), anyDouble(), anyDouble())).thenReturn(new TestCall());
    peliasWithMock.setLocationProvider(new TestLocationProvider());
    peliasWithMock.suggest("test", callback);
    verify(mock).getSuggest(eq("test"), eq(1.0), eq(2.0));
  }

  @Test public void suggest_getSuggestWithLayersCountrySources() throws Exception {
    when(mock.getSuggest(anyString(), anyDouble(), anyDouble(), anyString(), anyString(),
        anyString())).thenReturn(new TestCall());
    peliasWithMock.setLocationProvider(new TestLocationProvider());
    peliasWithMock.suggest("test", "venue", "us", "wof", callback);
    verify(mock).getSuggest(eq("test"), eq(1.0), eq(2.0), eq("venue"), eq("us"), eq("wof"));
  }

  @Test public void reverse_getReverseGeocode() throws Exception {
    when(mock.getReverse(anyDouble(), anyDouble())).thenReturn(new TestCall());
    peliasWithMock.setLocationProvider(new TestLocationProvider());
    peliasWithMock.reverse(30.0, 40.0, callback);
    verify(mock).getReverse(eq(30.0), eq(40.0));
  }

  @Test public void reverse_getReverseGeocodeWithSources() throws Exception {
    when(mock.getReverse(anyDouble(), anyDouble(), anyString())).thenReturn(new TestCall());
    peliasWithMock.setLocationProvider(new TestLocationProvider());
    peliasWithMock.reverse(30.0, 40.0, "wof", callback);
    verify(mock).getReverse(eq(30.0), eq(40.0), eq("wof"));
  }

  @Test public void place_shouldSendSearchRequestToServer() throws Exception {
    when(mock.getPlace(anyString())).thenReturn(new TestCall());
    peliasWithMock.place("osm:venue:3669115471", callback);
    verify(mock).getPlace(eq("osm:venue:3669115471"));
  }

  @Test public void setEndpoint_shouldChangeServiceEndpoint() throws Exception {
    final MockWebServer server = new MockWebServer();
    MockResponse response = new MockResponse();
    server.enqueue(response);
    server.play();
    Pelias pelias = new Pelias(server.getUrl("/").toString());
    pelias.setRequestHandler(new PeliasRequestHandler() {
      @Override public Map<String, String> headersForRequest() {
        HashMap<String, String> headers = new HashMap();
        headers.put("TEST_HEADER", "TEST_HEADER_VALUE");
        return headers;
      }

      @Override public Map<String, String> queryParamsForRequest() {
        HashMap<String, String> params = new HashMap();
        params.put("TEST_PARAM", "TEST_PARAM_VALUE");
        return params;
      }
    });
    pelias.suggest("test", 1.0, 2.0, callback);
    RecordedRequest request = server.takeRequest();
    assertThat(request.getPath()).contains("/autocomplete");
    assertThat(request.getHeader("TEST_HEADER")).isEqualTo("TEST_HEADER_VALUE");
    assertThat(request.getPath()).contains("TEST_PARAM");
    server.shutdown();
  }

  @Test public void setDebug_shouldChangeLogLevel() {
    assertThat(peliasWithMock.getDebug()).isFalse();
    peliasWithMock.setDebug(true);
    assertThat(peliasWithMock.getDebug()).isTrue();
  }

  @Test public void setEndpoint_shouldChangeEndpoint() {
    peliasWithMock.setEndpoint("http://pelias.com/test/");
    assertThat(peliasWithMock.getEndpoint()).isEqualTo("http://pelias.com/test/");
  }

  private class TestCallback implements Callback<Result> {
    @Override public void onResponse(Call<Result> call, Response<Result> response) {
    }

    @Override public void onFailure(Call<Result> call, Throwable t) {
    }
  }

  public static class TestLocationProvider implements PeliasLocationProvider {
    @Override public double getLat() {
      return 1.0;
    }

    @Override public double getLon() {
      return 2.0;
    }

    @Override public BoundingBox getBoundingBox() {
      return new BoundingBox(3.0, 4.0, 5.0, 6.0);
    }
  }

  private class TestCall implements Call<Result> {
    @Override public Response<Result> execute() throws IOException {
      return Response.success(new Result());
    }

    @Override public void enqueue(Callback<Result> callback) {
      callback.onResponse(null, Response.success(new Result()));
    }

    @Override public boolean isExecuted() {
      return false;
    }

    @Override public void cancel() {
    }

    @Override public boolean isCanceled() {
      return false;
    }

    @Override public Call<Result> clone() {
      return null;
    }

    @Override public Request request() {
      return null;
    }
  }
}
