package com.mapzen.pelias;

/**
 * Represents a rectangular area on the map.
 */
public class BoundingBox {
  private final double minLat;
  private final double minLon;
  private final double maxLat;
  private final double maxLon;

  /**
   * Constructs a new {@link BoundingBox}.
   */
  public BoundingBox(double minLat, double minLon, double maxLat, double maxLon) {
    if (((maxLat - minLat) > 0.0) && ((maxLon - minLon) > 0.0)) {
      this.minLat = minLat;
      this.minLon = minLon;
      this.maxLat = maxLat;
      this.maxLon = maxLon;
    } else {
      this.minLat = maxLat;
      this.minLon = maxLon;
      this.maxLat = minLat;
      this.maxLon = minLon;
    }
  }

  /**
   * Returns min lat.
   */
  public double getMinLat() {
    return minLat;
  }

  /**
   * Returns min lon.
   */
  public double getMinLon() {
    return minLon;
  }

  /**
   * Returns max lat.
   */
  public double getMaxLat() {
    return maxLat;
  }

  /**
   * Returns max lon.
   */
  public double getMaxLon() {
    return maxLon;
  }
}
