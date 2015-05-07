package com.mapzen.pelias.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AutoCompleteAdapter extends ArrayAdapter<AutoCompleteItem> {
    private int recentSearchIconResourceId;
    private int autoCompleteIconResourceId;

    public AutoCompleteAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void setRecentSearchIconResourceId(int recentSearchIconResourceId) {
        this.recentSearchIconResourceId = recentSearchIconResourceId;
    }

    public void setAutoCompleteIconResourceId(int autoCompleteIconResourceId) {
        this.autoCompleteIconResourceId = autoCompleteIconResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TextView textView = (TextView) super.getView(position, convertView, parent);
        final AutoCompleteItem item = getItem(position);
        final int resId = item.getSimpleFeature() == null ? recentSearchIconResourceId :
                autoCompleteIconResourceId;
        textView.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        return textView;
    }
}
