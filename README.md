# EchoSVG

 EchoSVG is a fork of Apache Batik, a Java based toolkit for applications which
handle images in the Scalable Vector Graphics (SVG) format for various purposes,
such as viewing, generation or manipulation.

 Thanks to its usage of the CSS4J style parser, supports level 4 CSS selectors
and RGBA color values (colors with alpha channel).

If you are using Apache Batik, you may want to read [MIGRATING_FROM_BATIK](https://github.com/css4j/echosvg/blob/master/MIGRATING_FROM_BATIK.md).

<br/>

## Building from source

To build from source, you need version 7 of Gradle. If you do not have Gradle
installed, it is easy to do so using a package manager (for example
[`scoop`](https://scoop.sh/) in Windows).

It is a good idea to create a Gradle wrapper, especially if you want to use a
IDE (otherwise it is not really necessary). To create it, run
`gradle wrapper --gradle-version 7.0.2` (or any Gradle version later than 7.0).

Run `gradle build` to build, and `gradle uberjar` to produce a _uberJar_
bundle containing all the packages with their dependencies (to be found at the
`build/libs` directory of the `echosvg-all` module).

To install in your local Maven repository: `gradle build publishToMavenLocal`.

And to deploy to a Maven repository: `gradle publish`, but before doing that you
should read the `publishing.repositories.maven` block of
[echosvg.java-conventions.gradle](https://github.com/css4j/echosvg/blob/master/buildSrc/src/main/groovy/echosvg.java-conventions.gradle)
to learn which properties you need to set (like `mavenReleaseRepoUrl`or
`mavenRepoUsername`), either at the command line or your
`GRADLE_USER_HOME/gradle.properties` file.

### Tests

Most of the (JUnit-based) test suite is executed during build (except some tests
that are currently incompatible with Gradle), but beware that many tests are
platform-dependent and could fail. If you encounter test failures, please open
an issue with the details so the necessary tweaks can be made.

The full test suite can be executed from the Eclipse IDE; please open an issue
if you are interested in executing the tests from other IDEs.

### Benchmarks

To run all the benchmarks (currently only one):
```shell
gradle runJmh
```
To run specific benchmark(s) matched by a regular expression:
```shell
gradle runJmh <regexp>
```
A jmh-ready _fat Jar_ with all the dependencies is available at
`echosvg-test/build/libs/echosvg-<version>-jmh.jar`, and is the recommended way to run
benchmarks:
```shell
java -jar echosvg-test/build/libs/echosvg-<version>-jmh.jar <regexp>
```

<br/>

## Usage from a Gradle project
If your Gradle project depends on echosvg, you can use this project's own Maven
repository in a `repositories` section of your build file:
```groovy
repositories {
    maven {
        url "https://css4j.github.io/maven/"
        mavenContent {
            releasesOnly()
        }
        content {
            includeGroup 'io.sf.carte'
            includeGroup 'io.sf.jclf'
        }
    }
}
```
please use that repository **only** for the artifact groups that it supplies
(basically those listed in the above `includeGroup` statements).

Then, in your `build.gradle` file you can list the dependencies, for example:
```groovy
dependencies {
    api "io.sf.carte:echosvg-transcoder:${echosvgVersion}"
}
```
or, if you want all of the main modules:
```groovy
dependencies {
    api "io.sf.carte:echosvg-all:${echosvgVersion}"
}
```
where `echosvgVersion` would be defined in a `gradle.properties` file (current
version is `0.1`).

<br/>

##  Licensing

 For licensing issues, please read the LICENSE and NOTICE files.
