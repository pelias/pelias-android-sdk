package com.mapzen.pelias.widget;

import com.mapzen.pelias.R;
import com.mapzen.pelias.SavedSearch;

import android.content.Context;
import android.os.ResultReceiver;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.view.animation.AnimationUtils.loadAnimation;

public class PeliasSearchView extends SearchView implements SearchView.OnQueryTextListener {
    public static final String TAG = PeliasSearchView.class.getSimpleName();

    private static final AutoCompleteTextViewReflector HIDDEN_METHOD_INVOKER =
            new AutoCompleteTextViewReflector();

    private Runnable showImeRunnable = new Runnable() {
        public void run() {
            final InputMethodManager imm = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                HIDDEN_METHOD_INVOKER.showSoftInputUnchecked(imm, PeliasSearchView.this, 0);
            }
        }
    };

    private Runnable hideImeRunnable = new Runnable() {
        public void run() {
            final InputMethodManager imm = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
    };

    private ListView autoCompleteListView;
    private SavedSearch savedSearch;

    public PeliasSearchView(Context context) {
        super(context);
        disableDefaultSoftKeyboardBehaviour();
        setOnQueryTextListener(this);
    }

    public void setAutoCompleteListView(final ListView listView) {
        autoCompleteListView = listView;
        setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    final Animation slideIn = loadAnimation(getContext(), R.anim.slide_in);
                    loadSavedSearches();
                    listView.setVisibility(VISIBLE);
                    listView.setAnimation(slideIn);
                    postDelayed(showImeRunnable, 300);
                } else {
                    final Animation slideOut = loadAnimation(getContext(), R.anim.slide_out);
                    listView.setVisibility(GONE);
                    listView.setAnimation(slideOut);
                    postDelayed(hideImeRunnable, 300);
                }
            }
        });
    }

    /**
     * Overrides default behavior for showing soft keyboard. Enables manual control by this class.
     */
    private void disableDefaultSoftKeyboardBehaviour() {
        try {
            Field showImeRunnable = SearchView.class.getDeclaredField("mShowImeRunnable");
            showImeRunnable.setAccessible(true);
            showImeRunnable.set(this, new Runnable() {
                @Override
                public void run() {
                    // Do nothing.
                }
            });
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Log.e(TAG, "Unable to override default soft keyboard behavior", e);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (savedSearch != null) {
            savedSearch.store(query);
        }

        clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void setSavedSearch(SavedSearch savedSearch) {
        this.savedSearch = savedSearch;
    }

    public void loadSavedSearches() {
        if (autoCompleteListView == null || autoCompleteListView.getAdapter() == null) {
            return;
        }

        final HeaderViewListAdapter headerViewListAdapter =
                (HeaderViewListAdapter) autoCompleteListView.getAdapter();
        final AutoCompleteAdapter adapter =
                (AutoCompleteAdapter) headerViewListAdapter.getWrappedAdapter();
        adapter.clear();
        adapter.addAll(savedSearch.getTerms());
        adapter.notifyDataSetChanged();
    }

    /**
     * Copied from {@link android.support.v7.widget.SearchView.AutoCompleteTextViewReflector}.
     */
    private static class AutoCompleteTextViewReflector {
        private Method showSoftInputUnchecked;

        AutoCompleteTextViewReflector() {
            try {
                showSoftInputUnchecked = InputMethodManager.class.getMethod(
                        "showSoftInputUnchecked", int.class, ResultReceiver.class);
                showSoftInputUnchecked.setAccessible(true);
            } catch (NoSuchMethodException e) {
                // Ah well.
            }
        }

        void showSoftInputUnchecked(InputMethodManager imm, View view, int flags) {
            if (showSoftInputUnchecked != null) {
                try {
                    showSoftInputUnchecked.invoke(imm, flags, null);
                    return;
                } catch (Exception e) {
                }
            }

            // Hidden method failed, call public version instead
            imm.showSoftInput(view, flags);
        }
    }
}
