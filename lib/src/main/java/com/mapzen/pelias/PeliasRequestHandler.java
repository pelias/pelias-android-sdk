package com.mapzen.pelias;

import java.util.Map;

/**
 * Interface to be used when configuring {@link Pelias}. Use this to provide extra headers or query
 * params in requests (ie. to add api keys).
 */
public interface PeliasRequestHandler {

  /**
   * Return headers to be added to every request.
   */
  Map<String, String> headersForRequest();

  /**
   * Return params to be added to every request.
   */
  Map<String, String> queryParamsForRequest();
}
