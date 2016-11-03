# Contributing to Pelias for Android

Contributor guidelines for the Pelias Android SDK.

## Getting Started

Fork and clone the repo:

    git clone git@github.com:username/pelias-android-sdk.git

Create a feature branch to make your changes:

    git checkout -b my-feature-name

## Writing Tests

Good test coverage is an important guard against defects and regressions in the Pelias for Android library. All classes should have unit test classes. All public methods should have unit tests. Those classes and methods should have all their possible states well tested. Pull requests without tests will be sent back to the submitter.

## Building and Testing

Before submitting a pull request you can build the library and run all tests and code quality checks locally using:

    ./gradlew clean assembleDebug checkstyle copyTask testDebug install

## Code Style

Essentially the IntelliJ default Java style, but with two-space indents.

1. Spaces, not tabs.
2. Two space indent.
3. Curly braces for everything: if, else, etc.
4. One line of white space between methods.
5. Single imports only.

The full Mapzen Android code style settings can be installed from https://github.com/mapzen/java-code-styles.
