# resvg Test Suite

The [resvg test suite](https://github.com/linebender/resvg-test-suite) was
imported into EchoSVG's tests. The SVG files and referenced resources are under
this directory while the reference images are under the corresponding directory
in `test-references`.

The resvg website gives a table with the test results:

https://linebender.org/resvg-test-suite/svg-support-table.html

The table was used as a guide to interpreting the results, although in a couple
of places it looks like the pass/fail marks for Batik were off by one, marking
as passing a test for a feature that Batik does not implement, or claiming a
failure where it succeeds, but those cases are minimal.

Most of the reference images were left as in the original test suite, using a
variation mask for passing it. In the passing tests that involve text, the
reference images were replaced by the ones produced by EchoSVG, as the
differences in text rendering are too big.

When EchoSVG does not implement a feature, the tests are considered to fail even
if the reference images aren't very different. A few tests are failed by most
user agents (_e.g._ `textPath/spacing=auto`) but are run anyway due to the lack
of an appropriate reference.

The tests that this software does not pass were disabled and left for future
investigation. As expected, EchoSVG passes more tests than Apache Batik.

</p>

## Undefined behaviour

Quite a few tests are marked as "undefined behaviour". In the cases where the
EchoSVG output looked consistent, it was put as reference. The rest of those
tests were disabled.

<br/>

## Deprecated features

A few tests involving the SVG 1.1 `icc-color()` function were removed, as that
value was deprecated from SVG and is not supported by current implementations of
SVG2. Web browsers and EchoSVG implement the modern replacements from the CSS
Color Level 4 and later specifications.

<br/>

## Text baseline properties

All of the tests for the `alignment-baseline` and `dominant-baseline` properties
fail, according to the results table and the reference images, with only Safari
and Chrome matching the resvg output. However it is unclear that this
interpretation of the SVG specification is what its original author had in mind.

The Apache Batik developers wrote specific code so the behaviour is what it is,
and there are several Batik (and EchoSVG) tests checking it. At that time they
were in close contact with the author of the spec.

At some point I even made changes to the code so it mimicked Chrome, but was
unconvinced that such interpretation matched the original intent so I dropped
them.

<br/>

## Geometry properties

The Structure tests contain two non-presentational attribute tests in the style
and style-attribute subdirectories. User agents supporting geometry properties,
like EchoSVG 3.x and the web browsers, render it as intended by the SVG2
specification. Although the resvg website gives it as passed by resvg, Inkscape
and QtSvg, and failed by the browsers, the reference images were replaced by the
EchoSVG output.

Note that the SVG file has the `version` attribute of the `svg` element set to
`1.1`, but that attribute was removed from the SVG specification and must be
ignored by SVG2-compliant user agents.
