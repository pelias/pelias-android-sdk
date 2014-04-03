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

TODO add stategies for mocking retrofit interface to get a callback value one can call success and failur on


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
