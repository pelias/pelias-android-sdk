package com.mapzen.pelias.gson;

/**
 * Represents a {@link Feature}'s property information.
 */
public class Properties {

  /**
   * The source dataset id.
   */
  public String id = "";

  /**
   * The global unique identifier.
   */
  public String gid = "";

  /**
   * The name.
   */
  public String name = "";

  /**
   * The country abbreviation.
   */
  public String country_a = "";

  /**
   * The country.
   */
  public String country = "";

  /**
   * The region.
   */
  public String region = "";

  /**
   * The region abbreviation.
   */
  public String region_a = "";

  /**
   * The county.
   */
  public String county = "";

  /**
   * The local admin.
   */
  public String localadmin = "";

  /**
   * The locality.
   */
  public String locality = "";

  /**
   * The neighborhood.
   */
  public String neighbourhood = "";

  /**
   * The confidence score.
   */
  public Double confidence = -1.0;

  /**
   * The label.
   */
  public String label = "";

  /**
   * The layer.
   */
  public String layer = "";
}
