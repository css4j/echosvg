# How to produce an EchoSVG release

Please follow these steps to produce a new release of EchoSVG.

## Requirements

- The [Git version control system](https://git-scm.com/downloads) is required to
obtain the sources. Any recent version should suffice.

- Java 11 to 24, which you can install from your favourite package manager or
by downloading from [Adoptium](https://adoptium.net/). Generally, Java 21 is
used for compiling releases.

- The [`generate_directory_index_caddystyle.py`](https://gist.github.com/carlosame/bd5b68c4eb8e0817d9beb1dcfb4de43d)
script and a recent version of [Python](https://www.python.org/) (required to
run it). The script is necessary to create the index files in the bare-bones
Maven repository currently used by EchoSVG.

- An archiver like [7-Zip](https://7-zip.org/).

<br/>

## Steps

1) In the `master` branch of your local copy of the EchoSVG Git repository, bump
the `version` in the [`echosvg.java-conventions.gradle`](buildSrc/src/main/groovy/echosvg.java-conventions.gradle)
file or remove the `-SNAPSHOT` suffix as necessary. Bump also the latest version
in the [`README`](README.md). Commit the changes to the Git repository.

2) If there is an issue tracking the release, close it (could be done adding a
'closes...' to the message in the previously described commit). Same if there is
a milestone.

3) If your local copy of the EchoSVG Git repository exactly matches the current
`master` HEAD, use that copy to execute the `./gradlew` commands shown later,
otherwise create a new clone of the `git@github.com:css4j/echosvg.git`
repository with `git clone` and use it.

For reference, let your copy of the EchoSVG release code be at `/path/to/echosvg`.

4) To check that everything is fine, build the code. If you are not using a Windows
system do not execute the tests, as several would fail. That is:

```shell
cd /path/to/echosvg
./gradlew build -x test
```

5) For convenience, now copy all the produced _jar_ files into a new `jar`
directory and create a Zip archive of them. For example if you are releasing
`2.3.1`:

```shell
./gradlew copyJars
mv jar echosvg-2.3.1-bin
7z a -mx9 echosvg-2.3.1-binaries.zip echosvg-2.3.1-bin
```

6) Use `changes.sh <new-version>` to create a `CHANGES.txt` file for the new
version, with the changes from the latest tag:

```shell
./changes.sh 2.3.1
```

Edit the resulting `CHANGES.txt` as convenient, to use it as the basis for the
detailed list of changes when you create the new release in Github.

7) Clone the `git@github.com:css4j/css4j.github.io.git` repository (which
contains a bare-bones Maven repository) and let `/path/to/css4j.github.io` be
its location.

8) From your copy of the EchoSVG release code, write the new artifacts into the
local copy of the bare-bones Maven repository with:

```shell
cd /path/to/echosvg
./gradlew publish -PmavenReleaseRepoUrl="file:///path/to/css4j.github.io/maven"
```

9) Produce the necessary directory indexes in the local copy of the bare-bones
Maven repository using [`generate_directory_index_caddystyle.py`](https://gist.github.com/carlosame/bd5b68c4eb8e0817d9beb1dcfb4de43d):

```shell
cd /path/to/css4j.github.io/maven/io/sf
generate_directory_index_caddystyle.py -r carte
```

If the changes to the `css4j.github.io` repository look correct, commit them.

Check whether the ["Examples" CI](https://github.com/css4j/css4j.github.io/actions/workflows/examples.yml)
triggered by that commit to the `css4j.github.io` repository completed
successfully. A failure could mean that you need to document something, or that
the artifacts are not usable with Java 8, for example.

10) Generate two archives with the modular Javadocs (for example with the 7-Zip
archiver):

```shell
cd /path/to/echosvg
./gradlew modularJavadoc
cd echosvg-all/build/docs
mv modular echosvg-2.3.1-modular-javadocs
7z a echosvg-2.3.1-modular-javadocs.7z echosvg-2.3.1-modular-javadocs
7z a -mx9 echosvg-2.3.1-modular-javadocs.zip echosvg-2.3.1-modular-javadocs
```

The compressed archives will be part of the published release. Notice that the
`.7z` archive is much smaller than the `.zip`.

_Currently, the javadocs are online at the https://carte.sourceforge.io/api/echosvg/ website.
Provided that you have the required credentials, you could update it via_ `rsync`.

11) Create a `v<version>` tag in the EchoSVG Git repository. For example:

```shell
cd /path/to/echosvg
git tag -s v2.3.1 -m "Release 2.3.1"
git push origin v2.3.1
```

or `git tag -a` instead of `-s` if you do not plan to sign the tag. But it is
generally a good idea to sign a release tag.

Alternatively, you could create the new tag when drafting the Github release
(next step).

12) Draft a new Github release at https://github.com/css4j/echosvg/releases

Summarize the most important changes in the release description, then create a
`## Detail of changes` section and paste the contents of the `CHANGES.txt` file
under it.

Add to the Github release the `echosvg-2.3.1-bin.zip` archive that you created,
the modular javadoc archives (`echosvg-2.3.1-modular-javadocs.7z` and
`echosvg-2.3.1-modular-javadocs.zip`), and the result of executing:

```shell
./gradlew uberjar
```
to be found at the `echosvg-all/build/libs/echosvg-all-2.3.1-alldeps.jar`. Then execute:

```shell
./gradlew echosvg-codec-jar-with-deps
./gradlew echosvg-svggen-jar-with-deps
./gradlew echosvg-transcoder-jar-with-deps
./gradlew echosvg-transcoder-svg-jar-with-deps
./gradlew echosvg-transcoder-tosvg-jar-with-deps
./gradlew echosvg-transcoder-svg2svg-jar-with-deps
```

and add to the release the archives at

```
echosvg-codec/build/libs/echosvg-codec-2.3.1-with-deps.jar
echosvg-svggen/build/libs/echosvg-svggen-2.3.1-with-deps.jar
echosvg-transcoder/build/libs/echosvg-transcoder-2.3.1-with-deps.jar
echosvg-transcoder-svg/build/libs/echosvg-transcoder-svg-2.3.1-with-deps.jar
echosvg-transcoder-tosvg/build/libs/echosvg-transcoder-tosvg-2.3.1-with-deps.jar
echosvg-transcoder-svg2svg/build/libs/echosvg-transcoder-svg2svg-2.3.1-with-deps.jar
```

13) Verify that the new [Github packages](https://github.com/orgs/css4j/packages?repo_name=echosvg)
were created successfully by the [Gradle Package](https://github.com/css4j/echosvg/actions/workflows/gradle-publish.yml)
task.

14) Describe new features in the Wiki.
