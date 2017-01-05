package com.mapzen.pelias.widget;

import com.mapzen.pelias.SimpleFeature;

import android.os.Parcel;

/**
 * Represents an autocomplete query in the {@link PeliasSearchView}.
 */
public class AutoCompleteItem {
  private final SimpleFeature simpleFeature;
  private final String text;

  /**
   * Constructs a new item given a query string.
   */
  public AutoCompleteItem(String text) {
    this.simpleFeature = null;
    this.text = text;
  }

  /**
   * Constructs a new item given a simple feature.
   */
  public AutoCompleteItem(SimpleFeature simpleFeature) {
    this.simpleFeature = simpleFeature;
    this.text = simpleFeature.label();
  }

  /**
   * Constructs a new item given a parcel.
   */
  public AutoCompleteItem(Parcel in) {
    in.setDataPosition(0);
    this.simpleFeature = SimpleFeature.readFromParcel(in);
    this.text = simpleFeature.label();
  }

  public String getText() {
    return text;
  }

  public SimpleFeature getSimpleFeature() {
    return simpleFeature;
  }

  @Override public String toString() {
    return getText();
  }
}
