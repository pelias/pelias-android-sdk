package com.mapzen.pelias;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import static org.fest.assertions.api.Assertions.assertThat;

public class RequestInterceptorTest {
  private RequestInterceptor requestInterceptor = new RequestInterceptor();

  @Test public void shouldNotBeNull() throws Exception {
    assertThat(requestInterceptor).isNotNull();
  }

  @Test public void intercept_shouldReturnResponseFromChain() throws Exception {
    assertThat(requestInterceptor.intercept(new TestChain())).isInstanceOf(Response.class);
  }

  @Test public void intercept_shouldAppendHeadersToRequest() throws Exception {
    requestInterceptor.setRequestHandler(new TestRequestHandler());
    Response response = requestInterceptor.intercept(new TestChain());
    assertThat(response.request().header("header_key")).isEqualTo("header_value");
  }

  @Test public void intercept_shouldAppendQueryParamsToRequest() throws Exception {
    requestInterceptor.setRequestHandler(new TestRequestHandler());
    Response response = requestInterceptor.intercept(new TestChain());
    assertThat(response.request().url().toString()).contains("param_key=param_value");
  }

  private class TestChain implements Interceptor.Chain {
    @Override public Request request() {
      return new Request.Builder().url("http://example.com/").build();
    }

    @Override public Response proceed(Request request) throws IOException {
      return new Response.Builder().request(request).protocol(Protocol.HTTP_1_1).code(200).build();
    }

    @Override public Connection connection() {
      return null;
    }
  }

  private class TestRequestHandler implements PeliasRequestHandler {
    @Override public Map<String, String> headersForRequest() {
      HashMap<String, String> headers = new HashMap<>();
      headers.put("header_key", "header_value");
      return headers;
    }

    @Override public Map<String, String> queryParamsForRequest() {
      HashMap<String, String> params = new HashMap<>();
      params.put("param_key", "param_value");
      return params;
    }
  }
}
