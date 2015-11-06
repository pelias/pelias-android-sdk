package com.mapzen.pelias.widget;

import com.mapzen.pelias.SimpleFeature;

public class AutoCompleteItem {
    private String text;
    private SimpleFeature simpleFeature;

    public AutoCompleteItem(String text) {
        this.text = text;
        this.simpleFeature = null;
    }

    public AutoCompleteItem(SimpleFeature simpleFeature) {
        this.text = simpleFeature.getTitle();
        this.simpleFeature = simpleFeature;
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
