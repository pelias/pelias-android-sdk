package com.mapzen.pelias.widget;

import android.os.Parcel;
import android.view.inputmethod.EditorInfo;
import com.mapzen.pelias.Pelias;
import com.mapzen.pelias.R;
import com.mapzen.pelias.SavedSearch;
import com.mapzen.pelias.SimpleFeature;
import com.mapzen.pelias.gson.Feature;
import com.mapzen.pelias.gson.Result;

import android.content.Context;
import android.os.ResultReceiver;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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

    private Runnable backPressedRunnable = new Runnable() {
        @Override public void run() {
            notifyOnBackPressListener();
        }
    };

    private ListView autoCompleteListView;
    private SavedSearch savedSearch;
    private Pelias pelias;
    private Callback<Result> callback;
    private OnSubmitListener onSubmitListener;
    private OnFocusChangeListener onPeliasFocusChangeListener;
    private int recentSearchIconResourceId;
    private int autoCompleteIconResourceId;
    private boolean disableAutoComplete;
    private boolean focusedViewHasFocus = false;
    private boolean listItemClicked = false;
    private boolean textSubmitted = false;
    private OnBackPressListener onBackPressListener;
    private boolean cacheSearchResults = true;
    private boolean autoKeyboardShow = true;

    public PeliasSearchView(Context context) {
        super(context);
        setup();
    }

    public PeliasSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
        disableAutoComplete = false;
        disableDefaultSoftKeyboardBehaviour();
        setOnQueryTextListener(this);
        setImeOptions(EditorInfo.IME_ACTION_SEARCH);
    }

    public void setAutoCompleteListView(final ListView listView) {
        autoCompleteListView = listView;
        setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    final Animation slideIn = loadAnimation(getContext(), R.anim.slide_in);
                    setAutoCompleteAdapterIcon(recentSearchIconResourceId);
                    loadSavedSearches();
                    listView.setVisibility(VISIBLE);
                    listView.setAnimation(slideIn);
                    if (autoKeyboardShow) {
                        postDelayed(showImeRunnable, 300);
                    }
                    setOnQueryTextListener(PeliasSearchView.this);
                } else {
                    final Animation slideOut = loadAnimation(getContext(), R.anim.slide_out);
                    listView.setVisibility(GONE);
                    listView.setAnimation(slideOut);
                    postDelayed(hideImeRunnable, 300);
                    setOnQueryTextListener(null);
                }

                // Notify secondary listener
                if (onPeliasFocusChangeListener != null) {
                    onPeliasFocusChangeListener.onFocusChange(view, hasFocus);
                }

                focusedViewHasFocus = hasFocus;
                postDelayed(backPressedRunnable, 300);
            }
        });

        autoCompleteListView.setOnItemClickListener(new OnItemClickHandler().invoke());
    }

    /**
     * Prevent the keyboard from showing for example when the autocomplete list is shown and
     * the view has focus
     */
    public void disableAutoKeyboardShow() {
        autoKeyboardShow = false;
    }

    public void enableAutoKeyboardShow() {
        autoKeyboardShow = true;
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
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Unable to override default soft keyboard behavior", e);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "Unable to override default soft keyboard behavior", e);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (pelias != null) {
            pelias.search(query, callback);
        }

        storeSavedSearch(query, null);

        if (onSubmitListener != null) {
            onSubmitListener.onSubmit();
        }
        textSubmitted = true;
        clearFocus();
        resetCursorPosition();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String text) {
        if (text.isEmpty() || disableAutoComplete) {
            setAutoCompleteAdapterIcon(autoCompleteIconResourceId);
            disableAutoComplete = false;
            return false;
        } else if (text.length() < 3) {
            setAutoCompleteAdapterIcon(recentSearchIconResourceId);
            loadSavedSearches();
        } else {
            setAutoCompleteAdapterIcon(autoCompleteIconResourceId);
            fetchAutoCompleteSuggestions(text);
        }

        return false;
    }

    public void disableAutoComplete() {
        disableAutoComplete = true;
    }

    private void setAutoCompleteAdapterIcon(int resId) {
        if (autoCompleteListView == null) {
            return;
        }

        final AutoCompleteAdapter adapter = (AutoCompleteAdapter) autoCompleteListView.getAdapter();
        if (adapter != null) {
            adapter.setIcon(resId);
        }
    }

    private void fetchAutoCompleteSuggestions(String text) {
        if (pelias == null) {
            return;
        }

        pelias.suggest(text, new Callback<Result>() {
            @Override public void success(Result result, Response response) {
                final ArrayList<AutoCompleteItem> items = new ArrayList<>();
                final List<Feature> features = result.getFeatures();
                for (Feature feature : features) {
                    items.add(new AutoCompleteItem(SimpleFeature.fromFeature(feature)));
                }

                if (autoCompleteListView == null) {
                    return;
                }

                if (autoCompleteListView instanceof AutoCompleteListView) {
                    ((AutoCompleteListView) autoCompleteListView).hideHeader();
                }

                //final HeaderViewListAdapter headerViewListAdapter =
                //        (HeaderViewListAdapter) autoCompleteListView.getAdapter();
                //final AutoCompleteAdapter adapter =
                //        (AutoCompleteAdapter) headerViewListAdapter.getWrappedAdapter();

                final AutoCompleteAdapter adapter = (AutoCompleteAdapter)
                        autoCompleteListView.getAdapter();
                adapter.clear();
                adapter.addAll(items);
                adapter.notifyDataSetChanged();
            }

            @Override public void failure(RetrofitError error) {
                Log.e(TAG, "Unable to fetch autocomplete results", error);
            }
        });
    }

    public void setSavedSearch(SavedSearch savedSearch) {
        this.savedSearch = savedSearch;
        updateSavedSearch();
    }

    public void loadSavedSearches() {
        if (autoCompleteListView == null || autoCompleteListView.getAdapter() == null) {
            return;
        }

        if (autoCompleteListView instanceof AutoCompleteListView) {
            ((AutoCompleteListView) autoCompleteListView).showHeader();
        }

        //final HeaderViewListAdapter headerViewListAdapter =
        //        (HeaderViewListAdapter) autoCompleteListView.getAdapter();
        //final AutoCompleteAdapter adapter =
        //        (AutoCompleteAdapter) headerViewListAdapter.getWrappedAdapter();

        final AutoCompleteAdapter adapter = (AutoCompleteAdapter) autoCompleteListView.getAdapter();
        adapter.clear();
        if (savedSearch != null) {
            adapter.addAll(savedSearch.getItems());
        }
        adapter.notifyDataSetChanged();
    }

    public void setPelias(Pelias pelias) {
        this.pelias = pelias;
    }

    public void setCallback(Callback<Result> callback) {
        this.callback = callback;
    }

    /**
     * This should be used over {@link #setOnQueryTextFocusChangeListener(OnFocusChangeListener)}
     * since {@link #setAutoCompleteListView(ListView)} relies on this method to control visibility
     * of the autocomplete suggestion list. Events will be forwarded by the built-in listener.
     *
     * @param onPeliasFocusChangeListener the listener to be invoked when the query text view focus
     * changes.
     */
    public void setOnPeliasFocusChangeListener(OnFocusChangeListener onPeliasFocusChangeListener) {
        this.onPeliasFocusChangeListener = onPeliasFocusChangeListener;
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

    public void setOnSubmitListener(OnSubmitListener onSubmitListener) {
        this.onSubmitListener = onSubmitListener;
    }

    public interface OnSubmitListener {
        public void onSubmit();
    }

    private void resetCursorPosition() {
        EditText editText = (EditText) findViewById(R.id.search_src_text);
        if (editText != null) {
            editText.setSelection(0);
        }
    }

    public void setRecentSearchIconResourceId(int recentSearchIconResourceId) {
        this.recentSearchIconResourceId = recentSearchIconResourceId;
    }

    public void setAutoCompleteIconResourceId(int autoCompleteIconResourceId) {
        this.autoCompleteIconResourceId = autoCompleteIconResourceId;
    }

    public class OnItemClickHandler {
        public AdapterView.OnItemClickListener invoke() {
            return new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final AutoCompleteItem item =
                            (AutoCompleteItem) autoCompleteListView.getAdapter().getItem(position);

                    if (item.getSimpleFeature() == null) {
                        setQuery(item.getText(), true);
                        resetCursorPosition();
                    } else {
                        final Result result = new Result();
                        final ArrayList<Feature> features = new ArrayList<>(1);
                        clearFocus();
                        setQuery(item.getText(), false);
                        resetCursorPosition();
                        features.add(item.getSimpleFeature().toFeature());
                        result.setFeatures(features);
                        if (callback != null) {
                            callback.success(result, null);
                        }
                        storeSavedSearch(item.getText(), item.getSimpleFeature().toParcel());
                    }
                    listItemClicked = true;
                }
            };
        }
    }

    /**
     * Listener to simulate when the SearchBar gains focus and then loses it when the user presses
     * back without executing a search or clicking on an item in the autocomplete list view
     *
     * {@link OnBackPressListener#onBackPressed()} is called after
     * {@link OnSubmitListener#onSubmit()},
     * {@link OnFocusChangeListener#onFocusChange(View, boolean)}, and
     * {@link android.widget.AdapterView.OnItemClickListener#onItemClick(
     * AdapterView, View, int, long)} are called
     *
     */
    public interface OnBackPressListener {
        public void onBackPressed();
    }

    public void setOnBackPressListener(OnBackPressListener onBackPressListener) {
        this.onBackPressListener = onBackPressListener;
    }

    protected void notifyOnBackPressListener() {
        if (onBackPressListener == null) {
            return;
        }
        if (!focusedViewHasFocus && !listItemClicked && !textSubmitted) {
            onBackPressListener.onBackPressed();
        }
        textSubmitted = false;
        listItemClicked = false;
    }

    private void storeSavedSearch(String query, Parcel parcel) {
        if (savedSearch == null || !cacheSearchResults) {
            return;
        }
        if (parcel == null) {
            savedSearch.store(query);
        } else {
            savedSearch.store(query, parcel);
        }
    }

    public void setCacheSearchResults(boolean cacheResults) {
        cacheSearchResults = cacheResults;
        updateSavedSearch();
    }

    public boolean cacheSearchResults() {
        return cacheSearchResults;
    }

    private void updateSavedSearch() {
        if (savedSearch != null && !cacheSearchResults) {
            savedSearch.clear();
            if (autoCompleteListView != null) {
                final AutoCompleteAdapter adapter =
                        (AutoCompleteAdapter) autoCompleteListView.getAdapter();
                if (adapter != null) {
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * When super is called, the {@link SearchView#getOnFocusChangeListener()} is invoked which
     * posts a delayed call to the {@link PeliasSearchView#backPressedRunnable}. We cancel this
     * post because it was not invoked from the back button being pressed
     */
    @Override public void onActionViewCollapsed() {
        super.onActionViewCollapsed();
        postDelayed(new Runnable() {
            @Override public void run() {
                removeCallbacks(backPressedRunnable);
            }
        }, 150);
    }
}
