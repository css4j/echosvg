# Migrating from Apache Batik

 To migrate from Apache Batik, you generally have to deal with the following:

1) Package names are now prefixed by io.sf.carte.echosvg instead of
org.apache.batik.
2) Class names that included the name "Batik" now have "EchoSVG".
3) The 'codec' and 'transcoder' modules were merged into one.
4) The rasterizer application (and related files) were removed.
