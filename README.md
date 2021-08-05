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
[Adoptium](https://adoptium.net/).

<br/>

### Building with Gradle

Execute the build script with `gradlew build` to build. For example:

```shell
git clone https://github.com/css4j/echosvg.git
cd echosvg
./gradlew build
```
or just `gradlew build` (without the `./`) on a Windows command prompt.

<br/>

### Tests

Most of the (JUnit-based) test suite is executed during build (except some tests
that are currently incompatible with Gradle), but **beware that some tests are
platform-dependent and could fail**. If you encounter test failures, please open
an issue with the details so the necessary tweaks can be made.

To build without running the tests:
```shell
gradlew build -x test
```
The full test suite can be executed from the Eclipse IDE, and most of it runs
satisfactorily from _IntelliJ IDEA_; please open an issue if you find issues
executing the tests from other IDEs.

_Note_: when running tests from the Eclipse IDE, it is recommended to run them
as a "JUnit Test" in the "Run As" menu option. If you run them as "Gradle Test"
you may encounter well-known security-related issues (issue #19).

#### Interactive tests

Several interactive tests can be executed via:
```shell
gradlew iTest
```
Note that several of those tests are failing (a few being unfinished).

<br/>

### Benchmarks

This project uses [JMH](https://github.com/openjdk/jmh) for benchmarking. To run
all the benchmarks (currently only one):
```shell
gradlew runJmh
```
To run specific benchmark(s) matched by a regular expression:
```shell
gradlew runJmh <regexp>
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
- `gradlew build publishToMavenLocal` to install in your local Maven repository.
- `gradlew publish` to deploy to a (generally remote) Maven repository.

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
gradlew uberjar
```
The file is to be found at
`echosvg-all/build/libs/echosvg-all-<version>-alldeps.jar`.

<br/>

### Produce a merged Javadoc

A merged Javadoc allows to have the Javadocs of all the packages merged in a
single directory, and the `mergedJavadoc` task can do it:
```shell
gradlew mergedJavadoc
```
The Javadocs are created at `echosvg-all/build/docs/javadoc`.

Unfortunately —and due to JDK limitations— the generated Javadocs are not
modular.

<br/>

## Open the project in your IDE

Modern IDEs are able to import Gradle projects and let it manage the
dependencies. In _IntelliJ IDEA_ you can just open the root directory and the
Gradle project is opened, while in the [Eclipse IDE](https://www.eclipse.org/)
you need to import it explicitly:
```
File > Import... > Gradle > Existing Gradle Project
```
Eclipse shall ask you if you want to use a wrapper or its own instance of
Gradle, select the "wrapper" choice.

If you use Eclipse 2021-06, it is advisable to run a build with `./gradlew build`
before importing the project. Apparently Eclipse requires some files produced by
a build but is unable to do that by itself.

Note: it is normal to experience build issues with the `echosvg-test-scripts`
subproject in Eclipse, you may prefer to keep that project closed.

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
version is `0.1.1`).

<br/>

##  Licensing

 For licensing issues, please read the LICENSE and NOTICE files. The tests use
files which may have their own additional licenses, under the `samples`
directory. Please look for the appropriate license files there.
