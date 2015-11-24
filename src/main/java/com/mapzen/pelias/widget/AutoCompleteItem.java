package com.mapzen.pelias.widget;

import com.mapzen.pelias.SimpleFeature;

import android.os.Parcel;

public class AutoCompleteItem {
    private final String text;
    private final SimpleFeature simpleFeature;

    public AutoCompleteItem(String text) {
        this.text = text;
        this.simpleFeature = null;
    }

    public AutoCompleteItem(SimpleFeature simpleFeature) {
        this.text = simpleFeature.getTitle();
        this.simpleFeature = simpleFeature;
    }

    public AutoCompleteItem(Parcel parcel) {
        this.simpleFeature = SimpleFeature.readFromParcel(parcel);
        this.text = simpleFeature.getTitle();
    }

    public String getText() {
        return text;
    }

    public SimpleFeature getSimpleFeature() {
        return simpleFeature;
    }

    @Override
    public String toString() {
        return getText();
    }
}
