# How to produce an EchoSVG release

Please follow these steps to produce a new release of EchoSVG.

## Requirements

- The [Git version control system](https://git-scm.com/downloads) is required to
obtain the sources. Any recent version should suffice.

- Java 11 or later. You can install it from your favourite package manager or by
downloading from [Adoptium](https://adoptium.net/).

- The [`generate_directory_index_caddystyle.py`](https://gist.github.com/carlosame/bd5b68c4eb8e0817d9beb1dcfb4de43d)
script and a recent version of [Python](https://www.python.org/) (required to
run it). The script is necessary to create the index files in the bare-bones
Maven repository currently used by EchoSVG.

## Steps

1) In the `master` branch of your local copy of the EchoSVG Git repository, bump
the `version` in the [`echosvg.java-conventions.gradle`](buildSrc/src/main/groovy/echosvg.java-conventions.gradle)
file or remove the `-SNAPSHOT` suffix as necessary. Bump also the latest version
in the [`README`](README.md). Commit the changes to the Git repository.

2) If there is an issue tracking the release, close it (could be done adding a
'closes...' to the message in the previously described commit).

3) If your local copy of the EchoSVG Git repository exactly matches the current
`master` HEAD, use that copy to execute the `gradlew` commands shown later,
otherwise create a new clone of the `git@github.com:css4j/echosvg.git`
repository with `git clone` and use it.

For reference, let your copy of the EchoSVG release code be at `/path/to/echosvg`.

4) To check that everything is fine, build the code. If you are not using a Windows
system do not execute the tests, as several would fail. That is:

```shell
cd /path/to/echosvg
gradlew build -x test
```

5) For convenience, now copy all the produced _jar_ files into a new `jar`
directory:

```shell
gradlew copyJars
```

6) Use `changes.sh <new-version>` to create a `CHANGES.txt` file with the
changes from the latest tag. For example if you are releasing `0.3.1`:

```shell
changes.sh 0.3.1
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
gradlew publish -PmavenReleaseRepoUrl="file:///path/to/css4j.github.io/maven"
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

10) Create a `v<version>` tag in the EchoSVG Git repository. For example:

```shell
cd /path/to/echosvg
git tag -s v0.3.1 -m "Milestone Release 0.3.1"
git push origin v0.3.1
```

or `git tag -a` instead of `-s` if you do not plan to sign the tag. But it is
generally a good idea to sign a release tag.

Alternatively, you could create the new tag when drafting the Github release
(next step).

11) Draft a new Github release at https://github.com/css4j/echosvg/releases

Summarize the most important changes in the release description, then create a
`## Detail of changes` section and paste the contents of the `CHANGES.txt` file
under it.

Add to the Github release the _jar_ files from the `jar` directory in your copy
of the EchoSVG release code.

12) Verify that the new [Github packages](https://github.com/orgs/css4j/packages?repo_name=echosvg)
were created successfully by the [Gradle Package](https://github.com/css4j/echosvg/actions/workflows/gradle-publish.yml)
task.