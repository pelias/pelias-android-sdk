package com.mapzen.pelias.widget;

import com.mapzen.pelias.BuildConfig;
import com.mapzen.pelias.Pelias;
import com.mapzen.pelias.PeliasService;
import com.mapzen.pelias.PeliasTest;
import com.mapzen.pelias.R;
import com.mapzen.pelias.SavedSearch;
import com.mapzen.pelias.SimpleFeature;
import com.mapzen.pelias.SimpleFeatureTest;
import com.mapzen.pelias.gson.Feature;
import com.mapzen.pelias.gson.Geometry;
import com.mapzen.pelias.gson.Properties;
import com.mapzen.pelias.gson.Result;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.app.Activity;
import android.os.Parcel;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import okhttp3.Request;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

@RunWith(RobolectricTestRunner.class) @Config(constants = BuildConfig.class, sdk = 21,
    shadows = { ShadowInputMethodManager.class })
public class PeliasSearchViewTest {
  private static final Activity ACTIVITY = Robolectric.setupActivity(Activity.class);

  private PeliasSearchView peliasSearchView;

  @Before public void setUp() throws Exception {
    ACTIVITY.setTheme(R.style.TestAppTheme);
    peliasSearchView = new PeliasSearchView(ACTIVITY);
  }

  @Test public void shouldNotBeNull() throws Exception {
    assertThat(peliasSearchView).isNotNull();
  }

  @Test public void setAutoCompleteListView_shouldShowListViewWhenQueryGetsFocus()
      throws Exception {
    AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
    listView.setVisibility(GONE);
    peliasSearchView.setAutoCompleteListView(listView);
    AutoCompleteTextView queryText = getQueryTextView();
    shadowOf(queryText).setViewFocus(true);
    assertThat(listView.getVisibility()).isEqualTo(VISIBLE);
  }

  @Test public void setAutoCompleteListView_shouldHideListViewWhenQueryLosesFocus()
      throws Exception {
    AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
    listView.setVisibility(VISIBLE);
    peliasSearchView.setAutoCompleteListView(listView);
    AutoCompleteTextView queryText = getQueryTextView();
    shadowOf(queryText).setViewFocus(false);
    assertThat(listView.getVisibility()).isEqualTo(GONE);
  }

  @Test public void setAutoCompleteListView_shouldShowImeWhenQueryGetsFocus() throws Exception {
    AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
    listView.setVisibility(GONE);
    peliasSearchView.setAutoCompleteListView(listView);
    AutoCompleteTextView queryText = getQueryTextView();
    shadowOf(queryText).setViewFocus(false);
    shadowOf(queryText).setViewFocus(true);
    Robolectric.flushForegroundThreadScheduler();
    assertThat(ShadowInputMethodManager.isSoftInputVisible()).isTrue();
  }

  @Test public void setAutoCompleteListView_shouldHideImeWhenQueryGetsFocus() throws Exception {
    AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
    listView.setVisibility(GONE);
    peliasSearchView.setAutoCompleteListView(listView);
    AutoCompleteTextView queryText = getQueryTextView();
    shadowOf(queryText).setViewFocus(true);
    shadowOf(queryText).setViewFocus(false);
    Robolectric.flushForegroundThreadScheduler();
    assertThat(ShadowInputMethodManager.isSoftInputVisible()).isFalse();
  }

  @Test public void disableAutoKeyboardShow_shouldNotShowImeWhenQueryGetsFocus() throws Exception {
    AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
    listView.setVisibility(GONE);
    peliasSearchView.disableAutoKeyboardShow();
    peliasSearchView.setAutoCompleteListView(listView);
    AutoCompleteTextView queryText = getQueryTextView();
    shadowOf(queryText).setViewFocus(true);
    Robolectric.flushForegroundThreadScheduler();
    assertThat(ShadowInputMethodManager.isSoftInputVisible()).isFalse();
  }

  @Test public void enableAutoKeyboardShow_shouldShowImeWhenQueryGetsFocus() throws Exception {
    AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
    listView.setVisibility(GONE);
    peliasSearchView.disableAutoKeyboardShow();
    peliasSearchView.enableAutoKeyboardShow();
    peliasSearchView.setAutoCompleteListView(listView);
    AutoCompleteTextView queryText = getQueryTextView();
    shadowOf(queryText).setViewFocus(true);
    Robolectric.flushForegroundThreadScheduler();
    assertThat(ShadowInputMethodManager.isSoftInputVisible()).isTrue();
  }

  @Test public void onQueryTextSubmit_shouldNotClearFocus() throws Exception {
    peliasSearchView.requestFocus();
    peliasSearchView.onQueryTextSubmit("query");
    assertThat(peliasSearchView.isFocused()).isTrue();
  }

  @Test public void onQueryTextSubmit_shouldHideListView() throws Exception {
    final AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
    peliasSearchView.setAutoCompleteListView(listView);
    peliasSearchView.onQueryTextSubmit("query");
    assertThat(listView.getVisibility()).isEqualTo(GONE);
  }

  @Test public void onQueryTextSubmit_submitListener_shouldNotHideListView() throws Exception {
    final AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
    peliasSearchView.setAutoCompleteListView(listView);
    peliasSearchView.setSearchSubmitListener(new TestSearchSubmitListener());
    peliasSearchView.onQueryTextSubmit("query");
    assertThat(listView.getVisibility()).isEqualTo(VISIBLE);
  }

  @Test public void onQueryTextSubmit_submitListener_shouldNotSearch() throws Exception {
    peliasSearchView.setSearchSubmitListener(new TestSearchSubmitListener());
    Pelias pelias = mock(Pelias.class);
    peliasSearchView.setPelias(pelias);
    peliasSearchView.onQueryTextSubmit("query");
    verify(pelias, times(0)).search(anyString(), any(Callback.class));
  }

  @Test public void onQueryTextSubmit_shouldSaveSearch() throws Exception {
    SavedSearch savedSearch = new SavedSearch();
    peliasSearchView.setSavedSearch(savedSearch);
    peliasSearchView.onQueryTextSubmit("query");
    assertThat(savedSearch.size()).isEqualTo(1);
    assertThat(savedSearch.get(0).getTerm()).isEqualTo("query");
  }

  @Test public void loadSavedSearches_shouldAddTermsToAutoCompleteListView() throws Exception {
    final AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
    final AutoCompleteAdapter adapter =
        new AutoCompleteAdapter(ACTIVITY, android.R.layout.simple_list_item_1);
    final SavedSearch savedSearch = new SavedSearch();

    savedSearch.store("query");
    listView.setAdapter(adapter);
    peliasSearchView.setAutoCompleteListView(listView);
    peliasSearchView.setSavedSearch(savedSearch);
    peliasSearchView.loadSavedSearches();
    assertThat(adapter.getCount()).isEqualTo(1);
    assertThat(((TextView) adapter.getView(0, null, null)).getText()).isEqualTo("query");
  }

  @Test public void loadSavedSearches_shouldAddPayloadToAutocompleteListView() throws Exception {
    final AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
    final AutoCompleteAdapter adapter =
        new AutoCompleteAdapter(ACTIVITY, android.R.layout.simple_list_item_1);
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

  @Test public void onQueryTextSubmit_shouldFetchSearchResults() throws Exception {
    final Pelias pelias = new TestPelias();
    final TestCallback callback = new TestCallback();
    pelias.setLocationProvider(new PeliasTest.TestLocationProvider());
    peliasSearchView.setPelias(pelias);
    peliasSearchView.setCallback(callback);
    peliasSearchView.setQuery("query", true);
    assertThat(callback.result).isNotNull();
    assertThat(callback.error).isNull();
  }

  @Test public void onQueryTextSubmit_shouldHandleNullBody() throws Exception {
    final AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
    final TestEmptyAdapter adapter = new TestEmptyAdapter();
    listView.setAdapter(adapter);
    peliasSearchView.setAutoCompleteListView(listView);
    final Pelias pelias = new TestEmptyPelias();
    final Callback<Result> callback = peliasSearchView.getSuggestCallback();
    pelias.setLocationProvider(new PeliasTest.TestLocationProvider());
    peliasSearchView.setPelias(pelias);
    peliasSearchView.setCallback(callback);
    peliasSearchView.setQuery("query", true);
    assertThat(adapter.getCount()).isEqualTo(0);
  }

  @Test public void onItemClick_shouldSetQuery() throws Exception {
    final AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
    listView.setAdapter(new TestAdapter());
    peliasSearchView.setAutoCompleteListView(listView);
    listView.performItemClick(null, 1, 0);
    assertThat(peliasSearchView.getQuery().toString()).isEqualTo("query");
  }

  @Test public void onItemClick_simpleFeature_shouldSetQuery() throws Exception {
    final AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
    listView.setAdapter(new TestSimpleFeatureAdapter());
    peliasSearchView.setAutoCompleteListView(listView);
    listView.performItemClick(null, 1, 0);
    assertThat(peliasSearchView.getQuery().toString()).isEqualTo("query");
  }

  @Test public void onItemClick_noFocus_shouldHideListView() throws Exception {
    final AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
    listView.setAdapter(new TestSimpleFeatureAdapter());
    peliasSearchView.setAutoCompleteListView(listView);
    peliasSearchView.clearFocus();
    listView.performItemClick(null, 1, 0);
    assertThat(listView.getVisibility()).isEqualTo(GONE);
  }

  @Test public void onQueryTextSubmit_shouldNotifyOnSubmitListener() throws Exception {
    TestOnSubmitListener listener = new TestOnSubmitListener();
    peliasSearchView.setOnSubmitListener(listener);
    peliasSearchView.onQueryTextSubmit("query");
    assertThat(listener.submit).isTrue();
  }

  @Test public void setOnPeliasFocusChangeListener_shouldNotifyListener() throws Exception {
    AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
    TestOnFocusChangeListener listener = new TestOnFocusChangeListener();
    peliasSearchView.setAutoCompleteListView(listView);
    peliasSearchView.setOnPeliasFocusChangeListener(listener);
    shadowOf(getQueryTextView()).setViewFocus(true);
    assertThat(listener.hasFocus).isTrue();
  }

  @Test public void onBackPressListener_shouldNotifyWhenKeyboardVisibleAndBackPressed()
      throws Exception {
    TestOnBackPressListener listener = new TestOnBackPressListener();
    peliasSearchView.setOnBackPressListener(listener);
    shadowOf(getQueryTextView()).setViewFocus(true);
    peliasSearchView.notifyOnBackPressListener();
    assertThat(listener.pressed).isTrue();
  }

  @Test public void onBackPressListener_shouldNotNotifyOnAutocompleteItemClicked() {
    TestOnBackPressListener listener = new TestOnBackPressListener();
    peliasSearchView.setOnBackPressListener(listener);
    shadowOf(getQueryTextView()).setViewFocus(true);
    AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
    peliasSearchView.setAutoCompleteListView(listView);
    final AutoCompleteAdapter adapter =
        new AutoCompleteAdapter(ACTIVITY, android.R.layout.simple_list_item_1);
    final SavedSearch savedSearch = new SavedSearch();
    final Parcel payload = Parcel.obtain();
    final SimpleFeature simpleFeature = SimpleFeatureTest.getTestSimpleFeature();
    simpleFeature.writeToParcel(payload, 0);
    payload.setDataPosition(0);
    savedSearch.store("Test", payload);
    listView.setAdapter(adapter);
    peliasSearchView.setAutoCompleteListView(listView);
    peliasSearchView.setSavedSearch(savedSearch);
    peliasSearchView.loadSavedSearches();
    shadowOf(listView).performItemClick(0);
    assertThat(listener.pressed).isFalse();
  }

  @Test public void onBackPressListener_shouldNotNotifyOnSearchSubmitted() {
    TestOnBackPressListener listener = new TestOnBackPressListener();
    peliasSearchView.setOnBackPressListener(listener);
    shadowOf(getQueryTextView()).setViewFocus(true);
    peliasSearchView.onQueryTextSubmit("Test");
    assertThat(listener.pressed).isFalse();
  }

  @Test public void onBackPressListener_shouldNotNotifyWhenSearchViewFocused() {
    TestOnBackPressListener listener = new TestOnBackPressListener();
    peliasSearchView.setOnBackPressListener(listener);
    shadowOf(getQueryTextView()).setViewFocus(true);
    assertThat(listener.pressed).isFalse();
  }

  @Test public void shouldCacheSearchResultsByDefault() {
    assertThat(peliasSearchView.cacheSearchResults()).isTrue();
  }

  @Test public void setCacheSearchResults_shouldSaveQueryOnSearch() {
    AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
    peliasSearchView.setAutoCompleteListView(listView);
    SavedSearch savedSearch = new SavedSearch();
    peliasSearchView.setSavedSearch(savedSearch);
    peliasSearchView.onQueryTextSubmit("test");
    assertThat(savedSearch.getItems().size()).isEqualTo(1);
  }

  @Test public void setCacheSearchResultsDisabled_shouldNotSaveQueryOnSearch() {
    AutoCompleteListView listView = new AutoCompleteListView(ACTIVITY);
    peliasSearchView.setAutoCompleteListView(listView);
    SavedSearch savedSearch = new SavedSearch();
    peliasSearchView.setSavedSearch(savedSearch);
    peliasSearchView.setCacheSearchResults(false);
    peliasSearchView.onQueryTextSubmit("test");
    assertThat(savedSearch.getItems().size()).isEqualTo(0);
  }

  @Test public void setSavedSearch_shouldClearSaveSearchesIfCacheSearchResultsDisabled() {
    peliasSearchView.setCacheSearchResults(false);
    SavedSearch savedSearch = new SavedSearch();
    savedSearch.store("test");
    peliasSearchView.setSavedSearch(savedSearch);
    assertThat(savedSearch.getItems().size()).isEqualTo(0);
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
    @Override public Call<Result> getSuggest(@Query("text") String query,
        @Query("focus.point.lat") double lat, @Query("focus.point.lon") double lon) {
      return new TestCall();
    }

    @Override public Call<Result> getSuggest(@Query("text") String query,
        @Query("focus.point.lat") double lat, @Query("focus.point.lon") double lon,
        @Query("layers") String layers, @Query("boundary.country") String country,
        @Query("sources") String source) {
      return new TestCall();
    }

    @Override public Call<Result> getSearch(@Query("text") String query,
        @Query("focus.viewport.min_lon") double minLon,
        @Query("focus.viewport.min_lat") double minLat,
        @Query("focus.viewport.max_lon") double maxLon,
        @Query("focus.viewport.max_lat") double maxLat) {
      return new TestCall();
    }

    @Override public Call<Result> getSearch(@Query("text") String query,
        @Query("focus.point.lat") double lat, @Query("focus.point.lon") double lon) {
      return new TestCall();
    }

    @Override public Call<Result> getReverse(@Query("point.lat") double lat,
        @Query("point.lon") double lon) {
      return new TestCall();
    }

    @Override
    public Call<Result> getReverse(@Query("point.lat") double lat, @Query("point.lon") double lon,
        @Query("sources") String sources) {
      return new TestCall();
    }

    @Override public Call<Result> getPlace(@Query("ids") String ids) {
      return new TestCall();
    }
  }

  private class TestCall implements Call<Result> {
    @Override public Response<Result> execute() throws IOException {
      return Response.success(new Result());
    }

    @Override public void enqueue(Callback<Result> callback) {
      callback.onResponse(null, Response.success(new Result()));
    }

    @Override public boolean isExecuted() {
      return false;
    }

    @Override public void cancel() {
    }

    @Override public boolean isCanceled() {
      return false;
    }

    @Override public Call<Result> clone() {
      return null;
    }

    @Override public Request request() {
      return null;
    }
  }

  private class TestEmptyPelias extends Pelias {
    protected TestEmptyPelias() {
      super(new TestEmptyPeliasService());
    }
  }

  private class TestEmptyPeliasService implements PeliasService {
    @Override public Call<Result> getSuggest(@Query("text") String query,
        @Query("focus.point.lat") double lat, @Query("focus.point.lon") double lon) {
      return new TestEmptyCall();
    }

    @Override public Call<Result> getSuggest(@Query("text") String query,
        @Query("focus.point.lat") double lat, @Query("focus.point.lon") double lon,
        @Query("layers") String layers, @Query("boundary.country") String country,
        @Query("sources") String source) {
      return new TestEmptyCall();
    }

    @Override public Call<Result> getSearch(@Query("text") String query,
        @Query("focus.viewport.min_lon") double minLon,
        @Query("focus.viewport.min_lat") double minLat,
        @Query("focus.viewport.max_lon") double maxLon,
        @Query("focus.viewport.max_lat") double maxLat) {
      return new TestEmptyCall();
    }

    @Override public Call<Result> getSearch(@Query("text") String query,
        @Query("focus.point.lat") double lat, @Query("focus.point.lon") double lon) {
      return new TestEmptyCall();
    }

    @Override public Call<Result> getReverse(@Query("point.lat") double lat,
        @Query("point.lon") double lon) {
      return new TestEmptyCall();
    }

    @Override
    public Call<Result> getReverse(@Query("point.lat") double lat, @Query("point.lon") double lon,
        @Query("sources") String sources) {
      return new TestEmptyCall();
    }

    @Override public Call<Result> getPlace(@Query("ids") String ids) {
      return new TestEmptyCall();
    }
  }

  private class TestEmptyCall implements Call<Result> {
    @Override public Response<Result> execute() throws IOException {
      return Response.success(null);
    }

    @Override public void enqueue(Callback<Result> callback) {
      callback.onResponse(null, null);
    }

    @Override public boolean isExecuted() {
      return false;
    }

    @Override public void cancel() {
    }

    @Override public boolean isCanceled() {
      return false;
    }

    @Override public Call<Result> clone() {
      return null;
    }

    @Override public Request request() {
      return null;
    }
  }

  private class TestEmptyAdapter extends AutoCompleteAdapter {
    Collection<? extends AutoCompleteItem> items = new HashSet<>();

    public TestEmptyAdapter() {
      super(ACTIVITY, android.R.layout.simple_list_item_1);
    }

    @Override public void addAll(Collection<? extends AutoCompleteItem> collection) {
      this.items = collection;
    }

    @Override public AutoCompleteItem getItem(int position) {
      return new AutoCompleteItem("query");
    }

    @Override public int getCount() {
      return items.size();
    }
  }

  private class TestCallback implements Callback<Result> {
    private Result result;
    private Throwable error;

    @Override public void onResponse(Call<Result> call, Response<Result> response) {
      result = response.body();
    }

    @Override public void onFailure(Call<Result> call, Throwable t) {
      error = t;
    }
  }

  private class TestAdapter extends AutoCompleteAdapter {
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

  private class TestSimpleFeatureAdapter extends AutoCompleteAdapter {
    public TestSimpleFeatureAdapter() {
      super(ACTIVITY, android.R.layout.simple_list_item_1);
    }

    @Override public AutoCompleteItem getItem(int position) {
      Feature feature = new Feature();
      feature.properties = new Properties();
      feature.properties.label = "query";
      feature.geometry = new Geometry();
      feature.geometry.coordinates = new ArrayList<>();
      feature.geometry.coordinates.add(0.0);
      feature.geometry.coordinates.add(1.0);
      return new AutoCompleteItem(SimpleFeature.fromFeature(feature));
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

  private class TestOnFocusChangeListener implements View.OnFocusChangeListener {
    private boolean hasFocus;

    @Override public void onFocusChange(View v, boolean hasFocus) {
      this.hasFocus = hasFocus;
    }
  }

  private class TestOnBackPressListener implements PeliasSearchView.OnBackPressListener {
    private boolean pressed;

    @Override public void onBackPressed() {
      pressed = true;
    }
  }

  private class TestSearchSubmitListener implements SearchSubmitListener {

    @Override public boolean searchOnSearchKeySubmit() {
      return false;
    }

    @Override public boolean hideAutocompleteOnSearchSubmit() {
      return false;
    }
  }
}
