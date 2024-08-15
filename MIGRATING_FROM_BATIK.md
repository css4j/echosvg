# Migrating from Apache Batik

 There are several reasons why EchoSVG is not a drop-in replacement for Apache
Batik:

- Avoid using Apache Software Foundation package names and trademarks.

- Improved conformance to the SVG and CSS specifications.

- Better user experience (_e.g._ the `codec` classes that got merged into
  `transcoder`, or the cleaner module interdependencies).

- A circularity with Apache FOP. To render PDF images, Batik uses FOP which in
  turn uses Batik, so depending on FOP would imply mixing EchoSVG and Batik. See
  issue [#10](https://github.com/css4j/echosvg/issues/10).

- Changes in the Java™ Platform.

<br/>

## Migration checklist

 To migrate from Apache Batik, you generally have to deal with the following:

1) Package names are now prefixed by `io.sf.carte.echosvg` instead of
   `org.apache.batik`.

2) Class names that included the name "Batik" now have "EchoSVG".

3) The `codec`-`transcoder` circular dependency was removed in the following way:
   a few classes from the `codec` module were moved to `transcoder`. Now
   `transcoder` explicitly depends on `codec` (but not the other way around).

4) Tcl and Python 2.x scripting was removed (see issues [#12](https://github.com/css4j/echosvg/issues/12)
   and [#13](https://github.com/css4j/echosvg/issues/13)). That scripting wasn't
   functional in Batik anyway.

5) The generic `rasterizer` module (and related files) was removed (`svgrasterizer`
   is still available but no longer supports PDF graphics). See [issue #10](https://github.com/css4j/echosvg/issues/10)
   for more details.

6) `SAXDocumentFactory` and `SAXSVGDocumentFactory` no longer have constructors
   with `String` arguments. Either remove the string argument or replace it by a
   `null` and you should be fine. Alternatively, you can now use your own
   `XMLReader` in place of the parser class name. (The API change was made for
   security reasons)

7) EchoSVG does not depend on `xml-apis-ext-1.3.04.jar` to provide the SVGOM and
   SMIL APIs (see [#15](https://github.com/css4j/echosvg/issues/15)).
   If `xml-apis-ext` happens to be in your modulepath, you could configure an
   exclusion for it (in Maven or Gradle), then use `svgom-api` and `smil-api`
   from the [Web APIs](https://github.com/css4j/web-apis) project instead. If
   your project requires the old SAC api which is provided by `xml-apis-ext` as
   well, you can use [`sac.jar`](https://mvnrepository.com/artifact/org.w3c.css/sac)
   as a replacement.

8) `EchoSVGSecurityManager` is deprecated for removal due to Security Manager
   API being deprecated for removal since Java 17. Similarly,
   `RhinoClassLoader.getAccessControlContext()` and other security-related
   methods in other classes are deprecated for removal as well. The constructor
   `ApplicationSecurityEnforcer(Class, String)` is no longer public, please use
   `ApplicationSecurityEnforcer.createSecurityEnforcer(Class, String)` instead.

9) `BridgeContext` (in the `bridge` module) gained a `close` method.

10) `SVGAnimationElementBridge.initializeAnimation()` gained a `BridgeContext`
   argument and now returns a `boolean`.

11) The old and deprecated `CSSValue` API was replaced by an API close to W3C's
   Typed OM.
