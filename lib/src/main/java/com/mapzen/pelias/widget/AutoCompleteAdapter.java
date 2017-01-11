package com.mapzen.pelias.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adapter to be used with {@link AutoCompleteListView}.
 */
public class AutoCompleteAdapter extends ArrayAdapter<AutoCompleteItem> {
  private int iconId;

  /**
   * Constructs a new adapter given a context and layout id.
   */
  public AutoCompleteAdapter(Context context, int resource) {
    super(context, resource);
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    final TextView textView = (TextView) super.getView(position, convertView, parent);
    final AutoCompleteItem item = getItem(position);
    textView.setCompoundDrawablesWithIntrinsicBounds(iconId, 0, 0, 0);
    return textView;
  }

  public void setIcon(int resId) {
    iconId = resId;
  }
}
