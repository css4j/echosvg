# EchoSVG

 EchoSVG is a fork of Apache Batik, a Java based toolkit for applications which
handle images in the Scalable Vector Graphics (SVG) format for various purposes,
such as viewing, generation or manipulation.

 Thanks to its usage of the CSS4J style parser, supports level 4 CSS selectors
and color values (including colors with alpha channel) as well as the Level 5
`color-mix()` function.

This document mostly contains information of interest to developers, like how to
build from source or run tests. If you want general information (like how to use
the binaries) you are invited to read the [Wiki](https://github.com/css4j/echosvg/wiki),
especially the part about [why this fork was created](https://github.com/css4j/echosvg/wiki/Why-EchoSVG).

If you are using Apache Batik you may want to read [how Batik's testing
infrastructure was improved](https://github.com/css4j/echosvg/wiki/From-Batik-test-suite-to-EchoSVG-tests),
and if you decide to switch to EchoSVG, the [MIGRATING_FROM_BATIK](https://github.com/css4j/echosvg/blob/master/MIGRATING_FROM_BATIK.md)
document may be of help.

<br/>

## Building from source

### Requirements

To build EchoSVG you need the following software installed:

- The [Git version control system](https://git-scm.com/downloads) is required to
obtain the sources. Any recent version should suffice.

- Java version 11 to 25 (later versions may work but aren't tested by CI). You can install
it from your favourite package manager or by downloading from [Adoptium](https://adoptium.net/).

<br/>

### Building with Gradle

Execute the build script with `gradlew build` to build. For example:

```shell
git clone https://github.com/css4j/echosvg.git
cd echosvg
./gradlew build
```

<br/>

### Tests

Most of the (JUnit-based) test suite is executed during build (except some tests
that are currently incompatible with Gradle), but **beware that some tests are
platform-dependent and could fail**. If you encounter test failures, please open
an issue with the details so the necessary tweaks can be made.

If the failing test involves an image comparison, you may want to read
[IMAGE_COMPARISONS](IMAGE_COMPARISONS.md). For each failed image comparison,
side-by-side and diff images are saved into the project's `build/reports/tests/test/images`
directory.

To build without running the tests:
```shell
./gradlew build -x check
```
In the past, the full test suite could be executed from the Eclipse IDE, but that
is not the case anymore. Tests run satisfactorily from _IntelliJ IDEA_, except
for the security tests (that do not run on Gradle either, see  #19); please open
an issue if you find problems executing the tests from other IDEs.

_Note_: when running tests from the Eclipse IDE, it is recommended to run them
as a "JUnit Test" in the "Run As" menu option. If you run them as "Gradle Test"
you may encounter well-known security-related issues (issue #19).

#### Interactive tests

Several interactive tests can be executed via:
```shell
./gradlew iTest
```
Note that several of those tests are failing (a few being unfinished).

<br/>

### Benchmarks

This project uses [JMH](https://github.com/openjdk/jmh) for benchmarking. To run
all the benchmarks (currently only one):
```shell
./gradlew runJmh
```
To run specific benchmark(s) matched by a regular expression:
```shell
./gradlew runJmh <regexp>
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

- `./gradlew publishToMavenLocal` to install in your local Maven repository.

- `./gradlew publish` to deploy to a (generally remote) Maven repository.

If you plan to deploy to a repository, please configure the `mavenReleaseRepoUrl`
and/or `mavenSnapshotRepoUrl` properties (for example in
`GRADLE_USER_HOME/gradle.properties` or in the [command line](https://docs.gradle.org/current/userguide/build_environment.html#sec:project_properties)).
Otherwise, Gradle shall create a `build/repository` subdirectory and deploy there.

Properties `mavenRepoUsername` and `mavenRepoPassword` can also be set (generally
from the command line).

If you would rather look directly at the Gradle publish configurations, please
read the `publishing.repositories.maven` block of
[echosvg.java-conventions.gradle](https://github.com/css4j/echosvg/blob/master/buildSrc/src/main/groovy/echosvg.java-conventions.gradle).

<br/>

### Creating a _uber Jar_ or _fat Jar_

Sometimes, in non-modular projects it is useful to have a single Jar file
bundled with all the dependencies, often called a _uber Jar_ or _fat Jar_.
Execute the `uberjar` task to create one with _all_ the EchoSVG modules:
```shell
./gradlew uberjar
```
The file is to be found at
`echosvg-all/build/libs/echosvg-all-<version>-alldeps.jar`.

However, that archive is big and you may only want the classes that are needed
to run a specific module. In that case, run:
```shell
./gradlew <subproject-name>-jar-with-deps
```
and the archive shall be available at `<subproject-name>/build/libs/<subproject-name>-<version>-with-deps.jar`.

For example, to create an _all-deps_ jar for `echosvg-svggen`:
```shell
./gradlew echosvg-svggen-jar-with-deps
```
and the it will be located at `echosvg-svggen/build/libs/echosvg-svggen-<version>-with-deps.jar`.

Note that if you execute
```shell
./gradlew echosvg-all-jar-with-deps
```
the resulting jar will be identical to `echosvg-all-<version>-alldeps.jar`.

<br/>

### Produce a modular Javadoc

A Javadoc of all the modules is produced by the `modularJavadoc` task:
```shell
./gradlew modularJavadoc
```
The Javadocs are created at `echosvg-all/build/docs/modular`.

<br/>

### Produce a non-modular Javadoc

If you do not like modular javadocs, a merged non-modular Javadoc can be built
with the `mergedJavadoc` task:
```shell
./gradlew mergedJavadoc
```
The Javadocs are created at `echosvg-all/build/docs/javadoc`.

This task may be removed in the future; if the non-modular javadocs are useful
to you, please open an issue so it is preserved.

<br/>

### Other common tasks

#### OWASP Dependency Check

Check for known vulnerable dependencies that are used in the build.

```shell
./gradlew dependencyCheckAnalyze
```

#### Dependency Licensing

Generate a report about the licenses of the dependencies:

```shell
./gradlew generateLicenseReport
```

Check that the licenses of all the dependencies are allowed, fail otherwise:

```shell
./gradlew checkLicense
```
(the above task is executed on each build)

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

In Eclipse, it is advisable to run a build with `./gradlew build` before
importing the project. Apparently Eclipse requires some files produced by a
build but is unable to do that by itself.

Note: it is normal to experience build issues with the `echosvg-test-scripts`
subproject in Eclipse, you may prefer to keep that project closed.

<br/>

## Usage from a Gradle project

If your Gradle project depends on echosvg, you can use this project's own Maven
repository in a `repositories` section of your build file:
```groovy
repositories {
    maven {
        url = "https://css4j.github.io/maven/"
        mavenContent {
            releasesOnly()
        }
        content {
			includeGroupByRegex 'io\\.sf\\..*'
        }
    }
}
```
please use that repository only for the artifact groups that it supplies
(basically those listed in the above `includeGroupByRegex` statement).

Then, in your `build.gradle` file you can list the dependencies, for example:
```groovy
dependencies {
    implementation "io.sf.carte:echosvg-transcoder:${echosvgVersion}"
}
```
or, if you want all of the main modules:
```groovy
dependencies {
    implementation "io.sf.carte:echosvg-all:${echosvgVersion}"
}
```
where `echosvgVersion` would be defined in a `gradle.properties` file (current
version is `1.2.4`).

<br/>

##  Licensing

 For licensing issues, please read the LICENSE and NOTICE files. The tests use
files which may have their own additional licenses, under the `samples`
directory. Please look for the appropriate license files there.
