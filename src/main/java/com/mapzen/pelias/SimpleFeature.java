package com.mapzen.pelias;

import com.mapzen.pelias.gson.Feature;
import com.mapzen.pelias.gson.Geometry;
import com.mapzen.pelias.gson.Properties;

import android.os.Parcel;
import android.os.Parcelable;

import auto.parcel.AutoParcel;

/**
 * Represents a flattened autocomplete result from {@link Pelias}.
 */
@AutoParcel public abstract class SimpleFeature implements Parcelable {
  public static final String ADDRESS_LAYER = "address";

  /**
   * Creates a {@link SimpleFeature} from several properties.
   */
  public static SimpleFeature create(String id, String gid, String name, String country,
      String countryAbbr, String region, String regionAbbr, String county, String localAdmin,
      String locality, String neighborhood, Double confidence, String label, String layer,
      double lat, double lng) {

    return builder().id(id)
        .gid(gid)
        .name(name)
        .country(country)
        .countryAbbr(countryAbbr)
        .region(region)
        .regionAbbr(regionAbbr)
        .county(county)
        .localAdmin(localAdmin)
        .locality(locality)
        .neighborhood(neighborhood)
        .confidence(confidence)
        .label(label)
        .layer(layer)
        .lat(lat)
        .lng(lng)
        .build();
  }

  /**
   * Returns the feature's source dataset id.
   */
  public abstract String id();

  /**
   * Returns the feature's global unique identifier. This id should not be stored as it can change
   * across releases.
   */
  public abstract String gid();

  /**
   * Returns the feature's name.
   */
  public abstract String name();

  /**
   * Returns the feature's country. ie "United States"
   */
  public abstract String country();

  /**
   * Returns the feature's country abbreviation. ie "USA"
   */
  public abstract String countryAbbr();

  /**
   * Returns the feature's region. ie "Colorado"
   */
  public abstract String region();

  /**
   * Returns the feature's region. ie "CO"
   */
  public abstract String regionAbbr();

  /**
   * Returns the feature's county. ie "Boulder County"
   */
  public abstract String county();

  /**
   * Returns the feature's.
   */
  public abstract String localAdmin();

  /**
   * Returns the feature's locality. ie "Boulder"
   */
  public abstract String locality();

  /**
   * Returns the feature's neighborhood. ie "Central Boulder"
   */
  public abstract String neighborhood();

  /**
   * Returns the feature's confidence; how accurate the result matches the query.
   */
  public abstract Double confidence();

  /**
   * Returns the feature's label. ie "Galvanize, Boulder, CO, USA".
   */
  public abstract String label();

  /**
   * Returns the feature's layer. ie "venue", "address", "country"
   */
  public abstract String layer();

  /**
   * Returns the feature's latitude.
   */
  public abstract double lat();

  /**
   * Returns the feature's longitude.
   */
  public abstract double lng();

  /**
   * Builder for SimpleFeature.
   */
  @AutoParcel.Builder public abstract static class Builder {

    /**
     * Sets the SimpleFeature's id and returns the Builder object.
     */
    public abstract Builder id(String id);

    /**
     * Sets the SimpleFeature's global unique identifier and returns the Builder object.
     */
    public abstract Builder gid(String gid);

    /**
     * Sets the SimpleFeature's name and returns the Builder object.
     */
    public abstract Builder name(String name);

    /**
     * Sets the SimpleFeature's country and returns the Builder object.
     */
    public abstract Builder country(String country);

    /**
     * Sets the SimpleFeature's country abbreviation and returns the Builder object.
     */
    public abstract Builder countryAbbr(String countryAbbr);

    /**
     * Sets the SimpleFeature's region and returns the Builder object.
     */
    public abstract Builder region(String region);

    /**
     * Sets the SimpleFeature's region abbreviation and returns the Builder object.
     */
    public abstract Builder regionAbbr(String regionAbbr);

    /**
     * Sets the SimpleFeature's country and returns the Builder object.
     */
    public abstract Builder county(String county);

    /**
     * Sets the SimpleFeature's local admin and returns the Builder object.
     */
    public abstract Builder localAdmin(String localAdmin);

    /**
     * Sets the SimpleFeature's locality and returns the Builder object.
     */
    public abstract Builder locality(String locality);

    /**
     * Sets the SimpleFeature's neighborhood and returns the Builder object.
     */
    public abstract Builder neighborhood(String neighborhood);

    /**
     * Sets the SimpleFeature's confidence score and returns the Builder object.
     */
    public abstract Builder confidence(Double confidence);

    /**
     * Sets the SimpleFeature's label and returns the Builder object.
     */
    public abstract Builder label(String label);

    /**
     * Sets the SimpleFeature's layer and returns the Builder object.
     */
    public abstract Builder layer(String layer);

    /**
     * Sets the SimpleFeature's latitude and returns the Builder object.
     */
    public abstract Builder lat(double lat);

    /**
     * Sets the SimpleFeature's longitude and returns the Builder object.
     */
    public abstract Builder lng(double lng);

    /**
     * Builds and returns the newly created SimpleFeature.
     */
    public abstract SimpleFeature build();
  }

  /**
   * Returns the class' builder.
   */
  public static Builder builder() {
    return new AutoParcel_SimpleFeature.Builder();
  }

  /**
   * Returns a builder.
   */
  public abstract Builder toBuilder();

  /**
   * Converts and returns a SimpleFeature from a Feature.
   */
  public static SimpleFeature fromFeature(Feature feature) {
    return SimpleFeature.builder()
        .id(feature.properties.id)
        .gid(feature.properties.gid)
        .name(feature.properties.name)
        .country(feature.properties.country)
        .countryAbbr(feature.properties.country_a)
        .region(feature.properties.region)
        .regionAbbr(feature.properties.region_a)
        .county(feature.properties.county)
        .localAdmin(feature.properties.localadmin)
        .locality(feature.properties.locality)
        .neighborhood(feature.properties.neighbourhood)
        .confidence(feature.properties.confidence)
        .label(feature.properties.label)
        .layer(feature.properties.layer)
        .lat(feature.geometry.coordinates.get(1))
        .lng(feature.geometry.coordinates.get(0))
        .build();
  }

  /**
   * Returns the feature's address.
   */
  public String address() {
    int commaIndex = label().indexOf(", ");
    if (commaIndex != -1) {
      return label().substring(commaIndex + 2);
    }
    return label();
  }

  /**
   * Converts the SimpleFeature to a Feature.
   */
  public Feature toFeature() {
    final Feature feature = new Feature();
    final Properties properties = new Properties();
    final Geometry geometry = new Geometry();

    properties.id = id();
    properties.gid = gid();
    properties.name = name();
    properties.country = country();
    properties.country_a = countryAbbr();
    properties.region = region();
    properties.region_a = regionAbbr();
    properties.county = county();
    properties.localadmin = localAdmin();
    properties.locality = locality();
    properties.neighbourhood = neighborhood();
    properties.confidence = confidence();
    properties.label = label();
    properties.layer = layer();

    geometry.coordinates.add(lng());
    geometry.coordinates.add(lat());

    feature.properties = properties;
    feature.geometry = geometry;

    return feature;
  }

  /**
   * Converts the SimpleFeature to a Parcel.
   */
  public Parcel toParcel() {
    Parcel parcel = Parcel.obtain();
    writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    return parcel;
  }

  /**
   * Creates a SimpleFeature from a Parcel.
   */
  public static SimpleFeature readFromParcel(Parcel in) {
    return AutoParcel_SimpleFeature.CREATOR.createFromParcel(in);
  }

  /**
   * Returns whether the feature's layer is an "address".
   */
  public boolean isAddress() {
    return ADDRESS_LAYER.equals(layer());
  }
}
