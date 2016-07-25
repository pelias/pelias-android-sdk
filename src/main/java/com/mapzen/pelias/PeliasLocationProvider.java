package com.mapzen.pelias;

/**
 * Used by {@link Pelias} to provide more accurate autocomplete and search results.
 */
public interface PeliasLocationProvider {

  /**
   * Returns a lat to be used in {@link Pelias#suggest(String, Callback)}.
   */
  double getLat();

  /**
   * Returns a lon to be used in {@link Pelias#suggest(String, Callback)}.
   */
  double getLon();

  /**
   * Returns a bounding box to be used in {@link Pelias#search(String, Callback)}.
   */
  BoundingBox getBoundingBox();
}
