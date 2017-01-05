package com.mapzen.pelias;

import java.io.IOException;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * OkHttp3 style network interceptor to append client headers and query params to outgoing requests.
 */
class RequestInterceptor implements Interceptor {
  private PeliasRequestHandler requestHandler;

  /**
   * Intercepts outgoing requests and makes modifications if a {@link PeliasRequestHandler} is set.
   */
  @Override public Response intercept(Chain chain) throws IOException {
    if (requestHandler != null) {
      return modifyRequest(chain);
    }

    return chain.proceed(chain.request());
  }

  /**
   * Modifies headers and query params for an outgoing request.
   */
  private Response modifyRequest(Chain chain) throws IOException {
    final Request originalRequest = chain.request();
    final Request.Builder requestBuilder = originalRequest.newBuilder();
    addHeaders(requestBuilder);
    addQueryParams(requestBuilder, originalRequest);
    return chain.proceed(requestBuilder.build());
  }

  /**
   * Add custom headers to outgoing request.
   */
  private void addHeaders(Request.Builder requestBuilder) {
    final Map<String, String> headers = requestHandler.headersForRequest();
    if (headers != null) {
      for (String key : headers.keySet()) {
        requestBuilder.header(key, headers.get(key));
      }
    }
  }

  /**
   * Add custom query params to outgoing request.
   */
  private void addQueryParams(Request.Builder requestBuilder, Request originalRequest) {
    final Map<String, String> params = requestHandler.queryParamsForRequest();
    if (params != null) {
      for (String key : params.keySet()) {
        final HttpUrl url = originalRequest
            .url()
            .newBuilder()
            .addQueryParameter(key, params.get(key))
            .build();
        requestBuilder.url(url);
      }
    }
  }

  void setRequestHandler(PeliasRequestHandler requestHandler) {
    this.requestHandler = requestHandler;
  }
}
