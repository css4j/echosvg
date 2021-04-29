# EchoSVG

 EchoSVG is a fork of Apache Batik, a Java based toolkit for applications which
handle images in the Scalable Vector Graphics (SVG) format for various purposes,
such as viewing, generation or manipulation.

 Thanks to its usage of the CSS4J style parser, supports level 4 CSS selectors
and RGBA color values (colors with alpha channel).

If you are using Apache Batik, you may want to read [MIGRATING_FROM_BATIK](https://github.com/css4j/echosvg/blob/master/MIGRATING_FROM_BATIK.md).

<br/>

## Building from source

 To build from source, you need version 7 of Gradle. If you want to create a
Gradle wrapper, run `gradle wrapper --gradle-version 7.0`.

 Just run `gradle build` to build, and `gradle build uberjar` to produce a
_uberJar_ bundle containing all the packages with their dependencies (in the
`echosvg-all` module).

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
