Pelias Android SDK
==================

Android SDK for Pelias

[![Circle CI](https://circleci.com/gh/pelias/pelias-android-sdk.svg?style=svg&circle-token=6e6203a065375a8fb6fabb5a689c11dcd8fa78cf)](https://circleci.com/gh/pelias/pelias-android-sdk)

## Usage

Pelias Android SDK is a client-side Java wrapper for Pelias plus Android specific integrations.

### Initialization

The `Pelias` class provides a simple interface to the [Pelias geocoder](https://github.com/pelias/pelias) which can be included in your application.

```java
Pelias pelias = new Pelias();
```

### Suggest

The suggest endpoint provides fast type-ahead autocomplete results.

```java
pelias.suggest("term to search", lat, lon, Callback<Result>);
```

### Search

The search endpoint provides locally and globally relevant full-text search results for addresses and POIs.

```java
pelias.search("term to search", lat, lon, Callback<Result>);
```

### Custom Endpoint

If you have [deployed your own instance of Pelias][2] you can set it on the class before initializing.

```java

Pelias pelias = new Pelias("https://your-pelias-domain.com");
```

### Testing

The current strategy for testing involves mocking the service instance using a [Retrofit](https://github.com/square/retrofit) interface which describes the paths to the API.

```java
package com.mapzen.android;

import org.mockito.Mockito;

public class TestPelias extends Pelias {
  public TestPelias(PeliasService service) {
    super(service);
  }
  
  private class TestPeliasService implements PeliasService {
    @Override public Call<Result> getSuggest(@Query("text") String query,
        @Query("focus.point.lat") double lat, @Query("focus.point.lon") double lon) {
      return new TestCall();
    }
    ..
  }
  
  private class TestCall implements Call<Result> {
    @Override public Response<Result> execute() throws IOException {
      return Response.success(new Result());
    }

    @Override public void enqueue(Callback<Result> callback) {
      callback.onResponse(null, Response.success(new Result()));
    }
  }
}
```

## Install

#### Download Jar

Download the [latest AAR][1].

#### Maven

Include dependency using Maven.

```xml
<dependency>
  <groupId>com.mapzen.android</groupId>
  <artifactId>pelias-android-sdk</artifactId>
  <version>1.1.0</version>
</dependency>
```

#### Gradle

Include dependency using Gradle.

```groovy
compile 'com.mapzen.android:pelias-android-sdk:1.1.0'
```

[1]: http://search.maven.org/remotecontent?filepath=com/mapzen/android/pelias-android-sdk/1.1.0/pelias-android-sdk-1.1.0.aar
[2]: https://github.com/pelias/pelias#how-can-i-install-my-own-instance-of-pelias
