package com.mapzen.pelias.widget;

import com.mapzen.pelias.SimpleFeature;

import android.os.Parcel;

public class AutoCompleteItem {
    private final SimpleFeature simpleFeature;
    private final String text;

    public AutoCompleteItem(String text) {
        this.simpleFeature = null;
        this.text = text;
    }

    public AutoCompleteItem(SimpleFeature simpleFeature) {
        this.simpleFeature = simpleFeature;
        this.text = simpleFeature.label();
    }

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

    @Override
    public String toString() {
        return getText();
    }
}
