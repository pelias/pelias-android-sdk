package com.mapzen.pelias.gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a {@link Feature}'s geometric information.
 */
public class Geometry {

  /**
   * The geometry type (ie "Point").
   */
  public String type;

  /**
   * The geometry lat and lon.
   */
  public List<Double> coordinates = new ArrayList<>();
}
