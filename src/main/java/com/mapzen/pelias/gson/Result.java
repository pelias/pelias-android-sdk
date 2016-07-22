package com.mapzen.pelias.gson;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a result when querying the {@link Pelias} interface.
 */
public class Result {
  @Expose private String type;
  @Expose private List<Feature> features = new ArrayList<>();

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<Feature> getFeatures() {
    return features;
  }

  public void setFeatures(List<Feature> features) {
    this.features = features;
  }
}
