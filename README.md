# EchoSVG

 EchoSVG is a fork of Apache Batik, a Java based toolkit for applications which
handle images in the Scalable Vector Graphics (SVG) format for various purposes,
such as viewing, generation or manipulation.

 Thanks to its usage of the CSS4J style parser, supports level 4 CSS selectors
and RGBA color values (colors with alpha channel).

If you are using Apache Batik, you may want to read [MIGRATING_FROM_BATIK](https://github.com/css4j/echosvg/blob/master/MIGRATING_FROM_BATIK.md).

<br/>

## Building from source

### Requirements

To build EchoSVG you need the following software installed:

- The [Git version control system](https://git-scm.com/downloads) is required to
obtain the sources. Any recent version should suffice.
- A recent modular Java JDK (version 16 is being used to build). You can install it
from your favourite package manager or by downloading from
[AdoptOpenJDK](https://adoptopenjdk.net/).
- Version 7 of [Gradle](https://gradle.org/). If you do not have Gradle
installed, it is easy to do so using a package manager (for example
[`scoop`](https://scoop.sh/) in Windows).

It is a good idea to create a Gradle wrapper, especially if you want to use a
[IDE](https://en.wikipedia.org/wiki/Integrated_development_environment)
(otherwise it is not really necessary). To create it, run
`gradle wrapper --gradle-version 7.0.2` (or any Gradle version later than 7.0)
at the EchoSVG sources directory.

<br/>

### Building with Gradle

Run `gradle build` to build. For example:

```shell
git clone https://github.com/css4j/echosvg.git
cd echosvg
gradle build
```
or
```shell
git clone https://github.com/css4j/echosvg.git
cd echosvg
gradle wrapper --gradle-version 7.0.2
gradle build
```
if you want to create a wrapper (only need to do that once!).

<br/>

### Tests

Most of the (JUnit-based) test suite is executed during build (except some tests
that are currently incompatible with Gradle), but **beware that many tests are
platform-dependent and could fail**. If you encounter test failures, please open
an issue with the details so the necessary tweaks can be made.

To build without running the tests:
```shell
gradle build -x test
```
The full test suite can be executed from the Eclipse IDE; please open an issue
if you are interested in executing the tests from other IDEs.

<br/>

### Benchmarks

This project uses [JMH](https://github.com/openjdk/jmh) for benchmarking. To run
all the benchmarks (currently only one):
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

### Deploying to a Maven repository

Use:
- `gradle build publishToMavenLocal` to install in your local Maven repository.
- `gradle publish` to deploy to a (generally remote) Maven repository.

Before deploying to a remote Maven repository, please read the
`publishing.repositories.maven` block of
[echosvg.java-conventions.gradle](https://github.com/css4j/echosvg/blob/master/buildSrc/src/main/groovy/echosvg.java-conventions.gradle)
to learn which properties you need to set (like `mavenReleaseRepoUrl`or
`mavenRepoUsername`), either at the [command line](https://docs.gradle.org/current/userguide/build_environment.html#sec:project_properties)
(`-P` option) or your `GRADLE_USER_HOME/gradle.properties` file.

<br/>

### Creating a _uber Jar_ or _fat Jar_

Sometimes, in non-modular projects it is useful to have a single Jar file
bundled with all the dependencies, often called a _uber Jar_ or _fat Jar_.
Execute the `uberjar` task to create it:
```shell
gradle uberjar
```
The file is to be found at
`echosvg-all/build/libs/echosvg-all-<version>-alldeps.jar`.

<br/>

## Open the project in your IDE

Modern IDEs are able to import Gradle projects and let it manage the
dependencies. For example, in the [Eclipse IDE](https://www.eclipse.org/):
```
File > Import... > Gradle > Existing Gradle Project
```
Eclipse shall ask you if you want to use a wrapper or its own instance of
Gradle. If you created a wrapper after getting the sources, select the
"wrapper" choice.

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
