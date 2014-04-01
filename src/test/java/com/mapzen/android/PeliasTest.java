package com.mapzen.android;

import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;

public class PeliasTest {
    @Test
    public void search_shouldNotBeNull() throws Exception {
        assertThat(Pelias.search(null, null, null)).isNotNull();
    }

    @Test
    public void suggest_shouldNotBeNull() throws Exception {
        assertThat(Pelias.suggest(null, null, null)).isNotNull();
    }
}
