package com.mapzen.pelias.gson;

/**
 * Represents a feature returned from {@link Pelias}.
 */
public class Feature {

  /**
   * The feature's properties (ie. gid, name, region, country).
   */
  public Properties properties;

  /**
   * The feature's geometry (ie. lat, lon).
   */
  public Geometry geometry;
}
