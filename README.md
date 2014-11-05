pelias-android-sdk
==================

Android sdk for pelias

[![Circle CI Build Status](https://circleci.com/gh/mapzen/pelias-android-sdk.png?circle-token=6e6203a065375a8fb6fabb5a689c11dcd8fa78cf)](https://circleci.com/gh/mapzen/pelias-android-sdk)

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

#### Custom Endpoint

If you have deployed your own instance of pelias described [here][2] you can set it on the class before initializing

```java
getPeliasWithEndpoint("http://your-pelias-domain.com").search("term to search", "<viewbox>", Callback<Result>);
```


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

#### Download Jar

Download the [latest JAR][1].

#### Maven

Include dependency using Maven.

```xml
<dependency>
  <groupId>com.mapzen.android</groupId>
  <artifactId>pelias-android-sdk</artifactId>
  <version>0.3.1</version>
</dependency>
```

#### Gradle

Include dependency using Gradle.

```groovy
compile 'com.mapzen.android:pelias-android-sdk:0.3.1'
```

[1]: http://search.maven.org/remotecontent?filepath=com/mapzen/android/pelias-android-sdk/0.3.1/pelias-android-sdk-0.3.1.jar
[2]: https://github.com/mapzen/pelias#setup-performance-information
