# EchoSVG Frequently Asked Questions

## Modulepath and boot

### [0.1.x] `java.lang.module.FindException: Module xalan not found, required by io.sf.carte.echosvg.dom`

The module system is refusing to create a filename-based module name from the
Xalan jar file because [it is finding a declaration of a non-existent service provider](https://issues.apache.org/jira/browse/XALANJ-2632).

The solution is to upgrade to EchoSVG 0.2 or later, but if you cannot do that
the workaround is to use a plugin to force the declaration of a module name.

With [Gradle](https://gradle.org/) you can use either the [`extra-java-module-info`](https://plugins.gradle.org/plugin/de.jjohannes.extra-java-module-info)
plugin:

```groovy
plugins {
  id 'de.jjohannes.extra-java-module-info' version '0.11'
}

extraJavaModuleInfo {
  failOnMissingModuleInfo.set(false)
  automaticModule("xalan-${xalanVersion}.jar", 'xalan')
}
```

or the [`moditect-gradle-plugin`](https://github.com/moditect/moditect-gradle-plugin).

With [Maven](https://maven.apache.org/), you could use the [Moditect Maven plugin](https://github.com/moditect/moditect).
