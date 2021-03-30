# Contributing to EchoSVG

 You can contribute to the EchoSVG project by raising issues and/or sending
`git` pull requests.

<br/>

## Report issues

 If you find any issue with the software or want to ask for an enhancement, use
the Github's [issue tracker](https://github.com/css4j/echosvg/issues).

<br/>

## Pull requests

 To contribute code to this project it is recommended to open an issue first,
explaining the rationale for the changes that you want to implement. Then, in
the title of the pull request (PR) you can include a reference like "fixes #NN"
where NN is the issue number. And it is generally a good idea to base your PR on
a branch that was named after the issue; for example your branch could be named
`issue-4`.

 A PR should only try to fix a single issue, unless it fixes two or more issues
that are very related or effectively the same. And if a commit has two or more
different purposes, it is often better to split it in multiple commits. Tools
like the _Git GUI_ are particularly useful for that.

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

 All PRs should come with one or more JUnit tests unless the change is a small,
obviously correct fix. Ideally, tests should provide a full coverage of the new
code, except for cases like multiple equivalent comparisons, exceptions that are
very hard to trigger or never thrown (or put as a theoretical safeguard), etc.
However, even one test is better than nothing.

<br/>

### Code style

 The code style could be summarized by the following points:

- Indent by tabs, not spaces. The automated formatting provided by the Eclipse
IDE is often used.
- `if`-`else` blocks should always use curly braces, even if a single line of
code is involved.
- Long, descriptive variable names are preferred.
- Add comments to explain what the code is trying to do, but avoiding useless
prose that just mimics the code, like _"check if foo is larger than 1"_ as a
comment to `if (foo > 1)`.
- Public and protected methods must have documentation comments.
- Avoid trailing whitespace except for empty lines in Javadoc comments.
- Classes and methods should have the minimum visibility that they require.
A method should not have `protected` visibility when being package-visible could
be enough, unless subclasses in other packages would naturally extend it. For
complex package-level or inner classes, it is acceptable to have `protected`
methods as a mean to document which ones are intended to be overridden by other
classes. (In that case, protected methods do not appear in the Javadocs and
therefore are not part of the API)

<br/>

### Attribution

 If you modify a source file that was created by the current maintainer and it
happens to have an `@author` tag with his name, feel free to remove that field
and let `git blame` handle the attribution.

 It is intended that if you put an `@author` field with your name in a new file,
the same criteria would apply.

<br/>

### Licensing

 All your contributions are submitted according to the license of this project,
see the LICENSE file for more information.
