package com.mapzen.pelias.widget;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import static org.fest.assertions.api.Assertions.assertThat;

public class AutoCompleteAdapterTest {
  private AutoCompleteAdapter adapter;

  @Before public void setUp() throws Exception {
    adapter = new AutoCompleteAdapter(RuntimeEnvironment.application,
        android.R.layout.simple_list_item_1);
  }

  @Test public void shouldNotBeNull() throws Exception {
    assertThat(adapter).isNotNull();
  }
}
