package com.mapzen.pelias.widget;

import com.mapzen.pelias.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import android.app.Activity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21)
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
        ListView listView = new ListView(ACTIVITY);
        listView.setVisibility(View.GONE);
        peliasSearchView.setAutoCompleteListView(listView);
        AutoCompleteTextView queryText = getQueryTextView();
        Shadows.shadowOf(queryText).setViewFocus(true);
        assertThat(listView.getVisibility()).isEqualTo(View.VISIBLE);
    }

    @Test
    public void setAutoCompleteListView_shouldHideListViewWhenQueryLosesFocus() throws Exception {
        ListView listView = new ListView(ACTIVITY);
        listView.setVisibility(View.VISIBLE);
        peliasSearchView.setAutoCompleteListView(listView);
        AutoCompleteTextView queryText = getQueryTextView();
        Shadows.shadowOf(queryText).setViewFocus(false);
        assertThat(listView.getVisibility()).isEqualTo(View.GONE);
    }

    private AutoCompleteTextView getQueryTextView() {
        LinearLayout linearLayout1 = (LinearLayout) peliasSearchView.getChildAt(0);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        return (AutoCompleteTextView) linearLayout3.getChildAt(0);
    }
}
