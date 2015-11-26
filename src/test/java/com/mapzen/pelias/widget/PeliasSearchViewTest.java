package com.mapzen.pelias.widget;

import com.mapzen.pelias.BuildConfig;
import com.mapzen.pelias.Pelias;
import com.mapzen.pelias.PeliasService;
import com.mapzen.pelias.PeliasTest;
import com.mapzen.pelias.SavedSearch;
import com.mapzen.pelias.SimpleFeature;
import com.mapzen.pelias.SimpleFeatureTest;
import com.mapzen.pelias.gson.Result;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import android.app.Activity;
import android.os.Parcel;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Query;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21, shadows = {ShadowInputMethodManager.class})
public class PeliasSearchViewTest {
    private static final Activity ACTIVITY = Robolectric.setupActivity(Activity.class);

    private PeliasSearchView peliasSearchView;

    @Before
    public void setUp() throws Exception {
        peliasSearchView = new PeliasSearchView(ACTIVITY);
    }

    @Test
    public void shouldNotBeNull() throws Exception {
        assertThat(peliasSearchView).isNotNull();
    }

    @Test
    public void setAutoCompleteListView_shouldShowListViewWhenQueryGetsFocus() throws Exception {
        AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
        listView.setVisibility(View.GONE);
        peliasSearchView.setAutoCompleteListView(listView);
        AutoCompleteTextView queryText = getQueryTextView();
        shadowOf(queryText).setViewFocus(true);
        assertThat(listView.getVisibility()).isEqualTo(View.VISIBLE);
    }

    @Test
    public void setAutoCompleteListView_shouldHideListViewWhenQueryLosesFocus() throws Exception {
        AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
        listView.setVisibility(View.VISIBLE);
        peliasSearchView.setAutoCompleteListView(listView);
        AutoCompleteTextView queryText = getQueryTextView();
        shadowOf(queryText).setViewFocus(false);
        assertThat(listView.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void setAutoCompleteListView_shouldShowImeWhenQueryGetsFocus() throws Exception {
        AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
        listView.setVisibility(View.GONE);
        peliasSearchView.setAutoCompleteListView(listView);
        AutoCompleteTextView queryText = getQueryTextView();
        shadowOf(queryText).setViewFocus(false);
        shadowOf(queryText).setViewFocus(true);
        Robolectric.flushForegroundScheduler();
        assertThat(ShadowInputMethodManager.isSoftInputVisible()).isTrue();
    }

    @Test
    public void setAutoCompleteListView_shouldHideImeWhenQueryGetsFocus() throws Exception {
        AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
        listView.setVisibility(View.GONE);
        peliasSearchView.setAutoCompleteListView(listView);
        AutoCompleteTextView queryText = getQueryTextView();
        shadowOf(queryText).setViewFocus(true);
        shadowOf(queryText).setViewFocus(false);
        Robolectric.flushForegroundScheduler();
        assertThat(ShadowInputMethodManager.isSoftInputVisible()).isFalse();
    }

    @Test
    public void onQueryTextSubmit_shouldClearFocus() throws Exception {
        peliasSearchView.requestFocus();
        peliasSearchView.onQueryTextSubmit("query");
        assertThat(peliasSearchView.isFocused()).isFalse();
    }

    @Test
    public void onQueryTextSubmit_shouldSaveSearch() throws Exception {
        SavedSearch savedSearch = new SavedSearch();
        peliasSearchView.setSavedSearch(savedSearch);
        peliasSearchView.onQueryTextSubmit("query");
        assertThat(savedSearch.size()).isEqualTo(1);
        assertThat(savedSearch.get(0).getTerm()).isEqualTo("query");
    }

    @Test
    public void loadSavedSearches_shouldAddTermsToAutoCompleteListView() throws Exception {
        final AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
        final AutoCompleteAdapter adapter = new AutoCompleteAdapter(ACTIVITY,
                android.R.layout.simple_list_item_1);
        final SavedSearch savedSearch = new SavedSearch();

        savedSearch.store("query");
        listView.setAdapter(adapter);
        peliasSearchView.setAutoCompleteListView(listView);
        peliasSearchView.setSavedSearch(savedSearch);
        peliasSearchView.loadSavedSearches();
        assertThat(adapter.getCount()).isEqualTo(1);
        assertThat(((TextView) adapter.getView(0, null, null)).getText()).isEqualTo("query");
    }

    @Test
    public void loadSavedSearches_shouldAddPayloadToAutocompleteListView() throws Exception {
        final AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
        final AutoCompleteAdapter adapter = new AutoCompleteAdapter(ACTIVITY,
                android.R.layout.simple_list_item_1);
        final SavedSearch savedSearch = new SavedSearch();
        final Parcel payload = Parcel.obtain();
        final SimpleFeature simpleFeature = SimpleFeatureTest.getTestSimpleFeature();

        simpleFeature.writeToParcel(payload, 0);
        payload.setDataPosition(0);

        savedSearch.store("Test SimpleFeature", payload);
        listView.setAdapter(adapter);
        peliasSearchView.setAutoCompleteListView(listView);
        peliasSearchView.setSavedSearch(savedSearch);
        peliasSearchView.loadSavedSearches();
        assertThat(adapter.getCount()).isEqualTo(1);
        assertThat(adapter.getItem(0).getText()).isEqualTo(simpleFeature.label());
        assertThat(adapter.getItem(0).getSimpleFeature()).isEqualTo(simpleFeature);
    }

    @Test
    public void onQueryTextSubmit_shouldFetchSearchResults() throws Exception {
        final Pelias pelias = new TestPelias();
        final TestCallback callback = new TestCallback();
        pelias.setLocationProvider(new PeliasTest.TestLocationProvider());
        peliasSearchView.setPelias(pelias);
        peliasSearchView.setCallback(callback);
        peliasSearchView.setQuery("query", true);
        assertThat(callback.result).isNotNull();
        assertThat(callback.error).isNull();
    }

    @Test
    public void onItemClick_shouldSetQuery() throws Exception {
        final AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
        listView.setAdapter(new TestAdapter());
        peliasSearchView.setAutoCompleteListView(listView);
        listView.performItemClick(null, 1, 0);
        assertThat(peliasSearchView.getQuery().toString()).isEqualTo("query");
    }

    @Test
    public void onQueryTextSubmit_shouldNotifyOnSubmitListener() throws Exception {
        TestOnSubmitListener listener = new TestOnSubmitListener();
        peliasSearchView.setOnSubmitListener(listener);
        peliasSearchView.onQueryTextSubmit("query");
        assertThat(listener.submit).isTrue();
    }

    private AutoCompleteTextView getQueryTextView() {
        final LinearLayout linearLayout1 = (LinearLayout) peliasSearchView.getChildAt(0);
        final LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        final LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        return (AutoCompleteTextView) linearLayout3.getChildAt(0);
    }

    private class TestPelias extends Pelias {
        protected TestPelias() {
            super(new TestPeliasService());
        }
    }

    private class TestPeliasService implements PeliasService {
        @Override public void getSuggest(
                @Query("text") String query,
                @Query("focus.point.lat") double lat,
                @Query("focus.point.lon") double lon,
                @Query("api_key") String key, Callback<Result> callback) {
            callback.success(new Result(), null);
        }

        @Override public void getSearch(
                @Query("text") String query,
                @Query("focus.viewport.min_lon") double minLon,
                @Query("focus.viewport.min_lat") double minLat,
                @Query("focus.viewport.max_lon") double maxLon,
                @Query("focus.viewport.max_lat") double maxLat,
                @Query("api_key") String key, Callback<Result> callback) {
            callback.success(new Result(), null);
        }

        @Override public void getSearch(
                @Query("text") String query,
                @Query("focus.point.lat") double lat,
                @Query("focus.point.lon") double lon,
                @Query("api_key") String key, Callback<Result> callback) {
            callback.success(new Result(), null);
        }

        @Override public void getReverse(
                @Query("point.lat") double lat,
                @Query("point.lon") double lon,
                @Query("api_key") String key, Callback<Result> callback) {
            callback.success(new Result(), null);
        }

        @Override public void getDoc(
                @Query("id") String typeAndId,
                @Query("api_key") String key, Callback<Result> callback) {
            callback.success(new Result(), null);
        }
    }

    private class TestCallback implements Callback<Result> {
        private Result result;
        private RetrofitError error;

        @Override public void success(Result result, Response response) {
            this.result = result;
        }

        @Override public void failure(RetrofitError error) {
            this.error = error;
        }
    }

    private class TestAdapter extends ArrayAdapter<AutoCompleteItem> {
        public TestAdapter() {
            super(ACTIVITY, android.R.layout.simple_list_item_1);
        }

        @Override public AutoCompleteItem getItem(int position) {
            return new AutoCompleteItem("query");
        }

        @Override public int getCount() {
            return 1;
        }
    }

    private class TestOnSubmitListener implements PeliasSearchView.OnSubmitListener {
        private boolean submit;
        @Override public void onSubmit() {
            submit = true;
        }
    }
}
