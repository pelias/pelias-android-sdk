package com.mapzen.android;

import com.mapzen.android.gson.Geojson;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static org.fest.assertions.api.Assertions.assertThat;

public class PeliasTest {
    MockWebServer server;
    String body = "{\n"
            + "    \"features\": [\n"
            + "        {\n"
            + "            \"geometry\": {\n"
            + "                \"coordinates\": [\n"
            + "                    -112.508508912214,\n"
            + "                    56.2007069527829\n"
            + "                ],\n"
            + "                \"type\": \"Point\"\n"
            + "            },\n"
            + "            \"properties\": {\n"
            + "                \"address_name\": null,\n"
            + "                \"admin0_abbr\": \"CA\",\n"
            + "                \"admin0_name\": \"Canada\",\n"
            + "                \"admin1_abbr\": null,\n"
            + "                \"admin1_name\": \"Alberta\",\n"
            + "                \"admin2_name\": null,\n"
            + "                \"hint\": \"HOUSE RIVER INDIAN CEMETERY 178, Alberta\",\n"
            + "                \"local_admin_name\": null,\n"
            + "                \"locality_name\": \"HOUSE RIVER INDIAN CEMETERY 178\",\n"
            + "                \"name\": \"HOUSE RIVER INDIAN CEMETERY 178\",\n"
            + "                \"neighborhood_name\": null,\n"
            + "                \"poi_name\": null,\n"
            + "                \"street_name\": null,\n"
            + "                \"type\": \"locality\"\n"
            + "            },\n"
            + "            \"type\": \"Feature\"\n"
            + "        }\n"
            + "    ],\n"
            + "    \"type\": \"FeatureCollection\"\n"
            + "}";

    @Before
    public void setup() throws Exception {
        server = new MockWebServer();
        server.play();
        Pelias.setEndpoint(server.getUrl("/").toString());
    }

    @After
    public void teardown() throws Exception {
        server.shutdown();
    }

    @Test(timeout = 1000)
    public void search_shouldBeSuccessful() throws Exception {
        TestCallback callback = new TestCallback();
        MockResponse response = new MockResponse().setBody(body).setResponseCode(200);
        server.enqueue(response);
        Pelias.search("test", callback);
        Thread.sleep(200);
        assertThat(callback.wasSuccessful()).isTrue();
    }

    @Test(timeout = 1000)
    public void search_shouldExecuteErrorCallback() throws Exception {
        TestCallback callback = new TestCallback();
        MockResponse response = new MockResponse().setResponseCode(500);
        server.enqueue(response);
        Pelias.search("test", callback);
        Thread.sleep(200);
        assertThat(callback.wasUnSuccessful()).isTrue();
    }

    @Test
    public void search_shouldHitEndpoint() throws Exception {
        TestCallback callback = new TestCallback();
        Pelias.search("hello search", callback);
        RecordedRequest request = server.takeRequest();
        assertThat(request.getRequestLine()).contains("query=hello+search");
    }

    @Test(timeout = 1000)
    public void suggest_shouldExecuteSuccessCallback() throws Exception {
        TestCallback callback = new TestCallback();
        MockResponse response = new MockResponse().setBody(body).setResponseCode(200);
        server.enqueue(response);
        Pelias.suggest("foo", callback);
        Thread.sleep(200);
        assertThat(callback.wasSuccessful()).isTrue();
    }

    @Test(timeout = 1000)
    public void suggest_shouldExecuteErrorCallback() throws Exception {
        MockResponse response = new MockResponse().setBody(body).setResponseCode(200);
        server.enqueue(response);
        TestCallback callback = new TestCallback();
        Pelias.search("hello search", callback);
        Thread.sleep(200);
        assertThat(callback.wasSuccessful()).isTrue();
    }

    class TestCallback implements Callback<Geojson> {
        boolean success = false;
        boolean error = false;

        @Override
        public void success(Geojson o, Response response) {
            success = true;
        }

        @Override
        public void failure(RetrofitError retrofitError) {
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
