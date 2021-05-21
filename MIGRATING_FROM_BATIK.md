# Migrating from Apache Batik

 To migrate from Apache Batik, you generally have to deal with the following:

1) Package names are now prefixed by `io.sf.carte.echosvg` instead of
   `org.apache.batik`.
2) Class names that included the name "Batik" now have "EchoSVG".
3) The `codec` and `transcoder` modules were merged into one. See [issue #11](https://github.com/css4j/echosvg/issues/11).
4) Tcl and Python 2.x scripting was removed (see issues [#12](https://github.com/css4j/echosvg/issues/12)
   and [#13](https://github.com/css4j/echosvg/issues/13)).
5) The generic rasterizer module (and related files) was removed (svgrasterizer
   is still available but no longer supports PDF graphics). See [issue #10](https://github.com/css4j/echosvg/issues/10)
   for more details.
6) SAXDocumentFactory and SAXSVGDocumentFactory no longer have constructors with
   `String` arguments. Either remove the string argument or replace it by a
   `null` and you should be fine. Alternatively, you can now use your own
   `XMLReader` in place of the parser class name.
