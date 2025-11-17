# Contributing to EchoSVG

 You can contribute to the EchoSVG project by raising issues and/or sending
`git` pull requests.

<br/>

## Report issues

 To report any issue with the software or to ask for an enhancement, please use
Github's [issue tracker](https://github.com/css4j/echosvg/issues).

 If you believe that you found a bug in the software, please explain clearly
what he problem is: what result did you expect and what did you obtain instead.

 Describe step by step how to reproduce the issue with example document(s),
rendered images and the minimum amount of code that would reproduce it. Remember
including also Java and operating system version information.

 Please research and document your bug as much as possible, and provide a
minimal reproducer. If the reproducer involves using a downstream application
(one that depends on EchoSVG), it will be considered invalid and the issue will
likely not be fixed.

 If the issue is related to SVG rendering, you may want to look at the
[SamplesRenderingTest.java](https://github.com/css4j/echosvg/blob/master/echosvg-test/src/test/java/io/sf/carte/echosvg/test/svg/SamplesRenderingTest.java)
source file and to [IMAGE_COMPARISONS.md](https://github.com/css4j/echosvg/blob/master/IMAGE_COMPARISONS.md)
to prepare and run your own tests.

<br/>

## Pull requests

 To contribute code to this project it is recommended to open an issue first,
explaining the rationale for the changes that you want to implement. Then, in
the title of the pull request (PR) you can include a reference like "fixes #NN"
where NN is the issue number. And it is generally a good idea to base your PR on
a branch that was named after the issue; for example your branch could be named
`issue-14`.

 A PR should only try to fix a single issue, unless it fixes two or more issues
that are very related or effectively the same. And if a commit has two or more
different purposes, it is often better to split it in multiple commits; tools
like the _Git GUI_ are particularly useful for that.

<br/>

### Commit messages

 It is recommended that commit messages (or at least the message for the main
commit) start with a prefix related to the area being affected. For example:
```
Util: honor the 'http.agent' system property in ParsedURL. Fixes #2.
```
If the commit has a wider scope than a single area, you do not need to include
any prefix, for example:
```
Add .gitattributes file.
```
 When the commit is exclusively about tests, you could use the "Tests:" prefix.

 The commit should focus on a specific task, and its descriptive message should
tell accurately what the commit does. For example, do not mix bug fixes with
arbitrary clean-ups, unless the clean-up is part of the fix.

 Although it is acceptable to include a small, unrelated code formatting fix
inside a bug-fixing commit (like a small indentation fix in the same file), if
the commit contains several formatting changes they should be split to a
different commit. That eases the task of future code reviewers.

 It is acceptable to mix tests and main source code in the same commit, as it is
immediately obvious to the reviewers which are which, and the tests offer an
insight of what the commit is trying to achieve. You do not need to mention the
tests in the commit message.

<br/>

### Tests

 All PRs should come with one or more JUnit tests unless the change is a small,
obviously correct fix. Ideally, tests should provide a full coverage of the new
code, except for cases like multiple equivalent comparisons, exceptions that are
very hard to trigger or never thrown (or put as a theoretical safeguard), etc.
However, even one test is better than nothing.

1) For consistency, the names of classes that contain tests (i.e. with `@Test`
annotation(s)) must end with `Test`. This makes easier to tell the actual tests
apart from their helper classes.

2) If the test requires access to package-visible elements, it should go into
the `src/tests` source set of that module. Otherwise, it probably belongs to the
`echosvg-test` project. In general, if the test actually checks stuff that
belongs to several modules, it should go into `echosvg-test`.

3) Tests should not involve remote network connections, unless the subject of
that test is to check the connection or its security.

4) There are scripting tests which are executed via the various subclasses of
`SelfContainedSVGOnLoadTest`. Those tests (_e.g._ in `test-resources/io/sf/carte/echosvg/dom`
or `test-resources/io/sf/carte/echosvg/css/dom`) return their result through the
`testResult` element. The tests pass only if the `result` attribute contains the
`passed` value, while the `errorCode` attribute may contain additional
information. If your test requires storing additional information, a child
element with name `errorDescriptionEntry` may be added to `result`, with the
relevant data being in the `id` and `value` attributes.

<br/>

#### Test infrastructure

 If you want to contribute to the testing infrastructure please make sure to run
all of the tests including the Security Manager tests, which currently is only
possible from the Eclipse IDE ("Run as JUnit test"). Gradle does an undocumented
use of the security manager, so the SM tests are not run by Gradle.

 The modified tests should be runnable from any of the main Java IDEs, with the
aforementioned exception of the SM tests that so far only work on Eclipse. This
project shall keep supporting the SM as long as Java 11 or 17 LTS receive normal
updates.

<br/>

### Benchmarks

 EchoSVG uses [JMH](https://github.com/openjdk/jmh) for benchmarks, with the
benchmarking files being located at `echosvg-test/src/jmh`. The names of all the
classes containing benchmarks must end with `Mark` for ease of identification (a
similar convention to tests ending with `Test`).

<br/>

### Code style

 Please read the [CODE_STYLE](CODE_STYLE.md) document.

<br/>

### Copyright and attribution

 All contributions are submitted under a [Developer Certificate of Origin](DeveloperCertificateOfOrigin.txt).
Although every author maintains their copyright, in case that code from this
project —including your contribution(s)— is used in a way that is deemed as a
license infringement, you are —in principle— not opposed to be represented by
the maintainer of this project in any legal proceedings intended to protect the
license of this project.

 If you modify a source file that was created by the current maintainer and it
happens to have an `@author` tag with his name, feel free to remove that field
and let `git blame` handle the attribution.

 It is intended that if you put an `@author` field with your name in a new file,
the same criteria would apply.

<br/>

### Licensing

 All your contributions are submitted according to the license of this project,
see the LICENSE file for more information.

<br/>

## Distribution

 This project [is not being submitted to the Maven Central repository](https://groups.google.com/g/css4j/c/op5jIoINb3M/m/IiiN-LfkDAAJ)
and this is something known to deter some contributors, thus being a relevant
information to cover here.

 However, the project distributes its artifacts through the css4j Maven
repository, as explained in the [README](README.md) (see 'Usage from a Gradle
project'). Please use that repository **only** for the artifact groups that it
supplies.
