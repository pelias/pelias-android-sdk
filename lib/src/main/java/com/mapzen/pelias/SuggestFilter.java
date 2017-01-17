package com.mapzen.pelias;

/**
 * Interface to optionally limit autocomplete results on a
 * {@link com.mapzen.pelias.widget.PeliasSearchView}.
 */
public interface SuggestFilter {

  /**
   * Return a string in the alpha-2 or alpha-3 ISO-3166 country code format.
   * @return
   */
  String getCountryFilter();

  /**
   * Return a comma-delimited string. For a list of valid layers, see:
   * https://mapzen.com/documentation/search/autocomplete/
   * @return
   */
  String getLayersFilter();

  /**
   * Return a comma-delimited string. For a list of valid sources, see:
   * https://mapzen.com/documentation/search/reverse/#filter-by-data-source
   * @return
   */
  String getSources();
}
