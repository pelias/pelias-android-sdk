pelias-android-sdk
==================

Android sdk for pelias

[![Build Status](https://travis-ci.org/mapzen/pelias-android-sdk.svg?branch=master)](https://travis-ci.org/mapzen/pelias-android-sdk)

## Usage

Pelias Android Sdk is a wrapper for Pelias

#### Initialization

Pelias provides a simple singleton which can be statically included in your application.

```java
import static com.mapzen.android.Pelias.getPelias;

getPelias().<method> ...

```

#### Suggest

```java
import static com.mapzen.android.Pelias.getPelias;

getPelias().suggest("term to search", Callback<Result>);
```

#### Search

```java
import static com.mapzen.android.Pelias.getPelias;

getPelias().search("term to search", "<viewbox>", Callback<Result>);
````

#### Testing

The current stratedgy for testing involves mocking the service instance which is an retrofit interface which describes the paths to the API.

You can extend Pelias where you can inject you mock object ... we use Mockito which is awesome

```java
package com.mapzen.android;

import org.mockito.Mockito;

public final class TestPelias extends Pelias {
    protected TestPelias(PeliasService service) {
        super(service);
    }

    public static PeliasService getPeliasMock() {
        PeliasService service = Mockito.mock(PeliasService.class);
        instance = new TestPelias(service);
        return service;
    }
}
```

Then when you want to interact with Pelias in stests you can just feed it a mock

```java
@Captor
@SuppressWarnings("unused")
ArgumentCaptor<Callback<Result>> peliasCallback;
    

MockitoAnnotations.initMocks(this);
PeliasService peliasServiceMock = TestPelias.getPeliasMock();
    
Mockito.verify(peliasServiceMock)
  .getSearch(Mockito.eq("Empire State Building"), Mockito.anyString(), peliasCallback.capture());

```
With this you can verify which arguments got sent to the service ... and then to test success or failure path you can can capture the callback and retrive the value and call respective callbacks with your own object mocks you want to have returned in your test.

success

```java
peliasCallback.getValue().success(results, response);
```

failure
```java
peliasCallback.getValue().failure(RetrofitError.unexpectedError("", null));
```


## Install

TODO submit to maven central and verify these links work

#### Download Jar

Download the [latest JAR][1].

#### Maven

Include dependency using Maven.

```xml
<dependency>
  <groupId>com.mapzen.android</groupId>
  <artifactId>pelias-android-sdk</artifactId>
  <version>1.0.0</version>
</dependency>
```

#### Gradle

Include dependency using Gradle.

```groovy
compile 'com.mapzen.android:pelias-android-sdk:1.0.0'
```

[1]: #
