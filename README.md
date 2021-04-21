# EchoSVG

 EchoSVG is a fork of Apache Batik, a Java based toolkit for applications which
handle images in the Scalable Vector Graphics (SVG) format for various purposes,
such as viewing, generation or manipulation.

 Thanks to its usage of the CSS4J style parser, supports level 4 selectors and
RGBA color values.

 No formal release has been produced yet, but you are encouraged to use it by
building from source.


## Building

 To build from source, you need version 7 of Gradle. If you want to create a
Gradle wrapper, run `gradle wrapper --gradle-version 7.0`.

 Just run `gradle build` to build, and `gradle build uberjar` to produce a
_uberJar_ bundle containing all the packages with their dependencies (in the
`echosvg-all` module).


##  Licensing

 For licensing issues, please read the LICENSE and NOTICE files.
