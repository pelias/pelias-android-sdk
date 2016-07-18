package com.mapzen.pelias;

import java.util.Map;

public interface PeliasRequestHandler {

  Map<String, String> headersForRequest();
  Map<String, String> queryParamsForRequest();
}
