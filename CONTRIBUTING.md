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

2) It is acceptable to mix tests and main source code in the same commit, as it
is immediately obvious to the reviewers which are which, and the tests offer an
insight of what the commit is trying to achieve. You do not need to mention the
tests in the commit message.

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

### Benchmarks

 EchoSVG uses [JMH](https://github.com/openjdk/jmh) for benchmarks, with the
benchmarking files being located at `echosvg-test/src/jmh`. The names of all the
classes containing benchmarks must end with `Mark` for ease of identification (a
similar convention to tests ending with `Test`).

<br/>

### Code style

 The code style could be summarized by the following points:

- Indent by tabs, not spaces. The automated formatting provided by the Eclipse
IDE is often used.

- Avoid trailing whitespace except for empty lines in Javadoc comments.

- `if`-`else` blocks should always use curly braces, even if a single line of
code is involved.

- Long, descriptive variable names are preferred.

- Add comments to explain what the code is trying to do, but avoiding useless
prose that just mimics the code, like _"check if foo is larger than 1"_ as a
comment to `if (foo > 1)`.

- Public and protected methods must have documentation comments.

- Code readability should not be sacrificed for compactness, unless there are
obvious gains and the trade-off can be justified. For example, `i++; foo(i);` is
preferable to `foo(++i);` except in conditional expressions.

- Classes and methods should have the minimum visibility that they require.
A method should not have `protected` visibility when being package-visible could
be enough, unless subclasses in other packages would naturally extend it. For
complex package-level or inner classes, it is acceptable to have `protected`
methods as a mean to document which ones are intended to be overridden by other
classes. (In that case, protected methods do not appear in the Javadocs and
therefore are not part of the API)

- Local variables and the `final` keyword: in Java, final local variables are
automatically detected by the compiler, so the `final` keyword is unnecessary
and only adds clutter. However, in some cases it is good to explicitly mark a
local variable as `final`: when it may be confused with a non-final one, when
setting a literal value, or when used in a final-only context (_e.g._ lambdas).

<br/>

### Commented code

 The old codebase contains several chunks of commented code; those comments are
often an indication that either that area or a related part of the codebase
could be buggy. Please do not remove them unless you have analysed why the
commented code is there, and determined that there is no such problem.

 At this project's initial code clean-up, in at least one case where commented
code was removed, the surrounding non-commented part had to be modified as well,
because there was a bug there.

 It is acceptable, however, to replace commented code with an informational task
comment, if that does not lead to the loss of information. For example:
```java
// FIXME: parameter 'foo' may not be correctly initialised here
```
 Pull requests should not contain newly commented code.

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

## Distribution

 This project [is not being submitted to the Maven Central repository](https://groups.google.com/g/css4j/c/op5jIoINb3M/m/IiiN-LfkDAAJ)
and this is something known to deter some contributors, thus being a relevant
information to cover here.

 However, the project distributes its artifacts through the css4j Maven
repository, as explained in the [README](README.md) (see 'Usage from a Gradle
project'). Please use that repository **only** for the artifact groups that it
supplies.
