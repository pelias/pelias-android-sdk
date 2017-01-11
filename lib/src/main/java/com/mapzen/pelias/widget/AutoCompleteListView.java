package com.mapzen.pelias.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * List view used by {@link PeliasSearchView} to display autocomplete items. Adds ability to
 * show/hide an empty view for when the list has no results.
 */
public class AutoCompleteListView extends ListView {
  private View emptyView;

  /**
   * Constructs a new list given a context.
   */
  public AutoCompleteListView(Context context) {
    super(context);
  }

  /**
   * Constructs a new list given a context and attribute set.
   */
  public AutoCompleteListView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  /**
   * Constructs a new list given a context, attribute set, and style.
   */
  public AutoCompleteListView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override public void setEmptyView(View emptyView) {
    this.emptyView = emptyView;
    updateEmptyView();
  }

  @Override public View getEmptyView() {
    return emptyView;
  }

  @Override public void setVisibility(int visibility) {
    super.setVisibility(visibility);
    updateEmptyView();
  }

  @Override public void setAdapter(final ListAdapter adapter) {
    super.setAdapter(adapter);
    adapter.registerDataSetObserver(new DataSetObserver() {
      @Override public void onChanged() {
        updateEmptyView();
      }
    });
  }

  @Override public void setAnimation(Animation animation) {
    super.setAnimation(animation);
    if (emptyView != null && getAdapter() != null && getAdapter().isEmpty()) {
      emptyView.setAnimation(animation);
    }
  }

  private void updateEmptyView() {
    if (emptyView == null) {
      return;
    }

    if (getVisibility() != VISIBLE) {
      emptyView.setVisibility(View.GONE);
      return;
    }

    if (getAdapter() == null || getAdapter().isEmpty()) {
      emptyView.setVisibility(VISIBLE);
    } else {
      emptyView.setVisibility(GONE);
    }
  }
}
