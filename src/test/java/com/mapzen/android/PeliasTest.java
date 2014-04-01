package com.mapzen.android;

import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;

public class PeliasTest {

    @Test
    public void search_shouldNotBeNull() throws Exception {
        assertThat(Pelias.search(null, null)).isNotNull();
    }

    @Test
    public void search_shouldBeSuccessful() throws Exception {
        TestCallback callback = new TestCallback();
        Pelias.search(null, callback);
        assertThat(callback.wasSuccessful()).isTrue();
    }

    @Test
    public void search_shouldExecuteErrorCallback() throws Exception {
        TestCallback callback = new TestCallback();
        Pelias.search(null, callback);
        assertThat(callback.wasUnSuccessful()).isTrue();
    }

    @Test
    public void suggest_shouldNotBeNull() throws Exception {
        assertThat(Pelias.suggest(null, null)).isNotNull();
    }

    @Test
    public void suggest_shouldExecuteSuccessCallback() throws Exception {
        TestCallback callback = new TestCallback();
        Pelias.suggest(null, callback);
        assertThat(callback.wasSuccessful()).isTrue();
    }

    @Test
    public void suggest_shouldExecuteErrorCallback() throws Exception {
        TestCallback callback = new TestCallback();
        Pelias.suggest(null, callback);
        assertThat(callback.wasUnSuccessful()).isTrue();
    }

    class TestCallback implements Callback {
        boolean success = false;
        boolean error = false;

        @Override
        public void onSuccess() {
            success = true;
        }

        @Override
        public void onError() {
            error = true;
        }

        public boolean wasSuccessful() {
            return success;
        }

        public boolean wasUnSuccessful() {
            return error;
        }
    }
}
