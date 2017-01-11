package com.mapzen.sample.pelias;

import com.mapzen.pelias.BoundingBox;
import com.mapzen.pelias.Pelias;
import com.mapzen.pelias.PeliasLocationProvider;
import com.mapzen.pelias.gson.Result;
import com.mapzen.pelias.widget.AutoCompleteAdapter;
import com.mapzen.pelias.widget.AutoCompleteListView;
import com.mapzen.pelias.widget.PeliasSearchView;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Main entry point for Pelias sample. Displays a {@link PeliasSearchView} and allows user to view
 * autocomplete results.
 */
public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  AutoCompleteListView listView;
  Pelias pelias;

  //Hard code to NYC for simplicity
  PeliasLocationProvider peliasLocationProvider = new PeliasLocationProvider() {
    @Override public double getLat() {
      return 40.74433;
    }

    @Override public double getLon() {
      return -73.9903;
    }

    @Override public BoundingBox getBoundingBox() {
      return new BoundingBox(40.74433, -73.9903, 40.741050, -73.996142);
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setupPelias();
    setupListView();
    setupPeliasSearchView();
  }

  private void setupPelias() {
    pelias = new Pelias();
    pelias.setLocationProvider(peliasLocationProvider);
  }

  private void setupListView() {
    listView = (AutoCompleteListView) findViewById(R.id.list_view);
    AutoCompleteAdapter autocompleteAdapter = new AutoCompleteAdapter(this,
        android.R.layout.simple_list_item_1);
    listView.setAdapter(autocompleteAdapter);
  }

  private void setupPeliasSearchView() {
    PeliasSearchView searchView = new PeliasSearchView(this);
    ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
        ActionBar.LayoutParams.MATCH_PARENT);
    getSupportActionBar().setCustomView(searchView, lp);
    getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    searchView.setAutoCompleteListView(listView);
    searchView.setPelias(pelias);
    searchView.setCallback(new Callback<Result>() {
      @Override public void onResponse(Call<Result> call, Response<Result> response) {
        Log.d(TAG, "Feature Count:" + response.body().getFeatures().size());
      }

      @Override public void onFailure(Call<Result> call, Throwable t) {
        Log.d(TAG, "Failure:" + t.getMessage());
      }
    });
    searchView.setIconifiedByDefault(false);
    searchView.setQueryHint(this.getString(R.string.search_hint));
  }
}
