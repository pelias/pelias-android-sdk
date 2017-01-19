package com.mapzen.pelias.widget;

/**
 * Interface to override what happens when the search key is pressed.
 */
public interface SearchSubmitListener {
  /**
   * Return true to have the {@link PeliasSearchView} execute a search when the search key is
   * pressed, false to do no search.
   * @return
   */
  boolean searchOnSearchKeySubmit();

  /**
   * Return true to have the {@link PeliasSearchView} hide the autocomplete list view when the
   * search key is pressed, false to have it remain visible.
   * @return
   */
  boolean hideAutocompleteOnSearchSubmit();

}
