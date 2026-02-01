# EchoSVG code style

 The code style is partly enforced by [`checkstyle` rules](config/checkstyle/checkstyle.xml)
and could be summarized by the following points:

- Indent by tabs, not spaces. The automated formatting provided by the Eclipse
IDE is often used.

- Lines should not be longer than 100-110 characters, although only a limit of
130 is enforced by `checkstyle`. In justified cases (_e.g._ `import` statements)
it is acceptable to configure exclusions in
[`suppressions.xml`](config/checkstyle/suppressions.xml).

- Avoid trailing whitespace except for empty lines in Javadoc comments.

- `if`-`else` blocks should always use curly braces, even if a single line of
code is involved.

- Long, descriptive variable names are preferred.

- Add comments to explain what the code is trying to do, but avoiding useless
prose that just mimics the code, like _"check if foo is larger than 1"_ as a
comment to `if (foo > 1)`.

- Public and protected methods should have documentation comments.

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

## Commented code

 The old codebase contains several chunks of commented code; those comments are
often an indication that either that area or a related part of the codebase
could be buggy. Please do not remove them unless you have understood why the
commented code is there, and determined that there is no issue in doing it.

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

## `@author` tags

Please see ['Copyright and attribution' in CONTRIBUTING](CONTRIBUTING.md#copyright-and-attribution).

<br/>

## Changing the code style

 For any comment or suggestion about the code style, please open a discussion.
