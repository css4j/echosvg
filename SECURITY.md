# Security and Privacy Considerations

EchoSVG handles images in the Scalable Vector Graphics (SVG) format for various
purposes, however it is important to understand its limitations related to security
and privacy.

<br/>

## Security

SVG documents can be complex and can drive any rendering software to its limits.
In general, if an SVG document can cause issues to a web browser that attempts to
render it, that image will also cause problems to EchoSVG or Batik.

Unfortunately, this library can also be less secure than web browsers, especially
in scripting security. To execute scripts, EchoSVG relies on the Mozilla Rhino
javascript library, which is embedded with a feature called LiveConnect. [It is well
known that it is almost impossible to secure a Rhino environment that uses
LiveConnect](https://github.com/mozilla/rhino/discussions/1045), so users are
advised against running untrusted scripts, or any trusted script that could somehow
be exploited by a malicious actor.

If you have any concern about script security, please do not enable scripting at
all in EchoSVG.

<br/>

## Privacy

In addition to potential issues with javascript, even a static SVG document could
leak information if a malicious CSS style sheet is used.

In short, this library should not be used to process any sort of untrusted content,
and you are advised to set up strict security measures (like a strongly secured
operating system container) if you need to do that.

<br/>

## Security scanners

If you run a security scanner on the EchoSVG codebase, it may report a false XXE
vulnerability in `SAXDocumentFactory.java`. It is a false positive, and the reason
why this project does not use `load-external-dtd` nor `disallow-doctype-decl`,
which are the configurations that the security scanner probably expects, is that
they lead to [data loss](https://css4j.github.io/resolver.html) when XML entities
are used.

<br/>

## Reporting a vulnerability

Any software that renders SVG and is compliant with the specification can be abused
to produce a DoS attack, and little can be done except maybe limiting the compliance.
However, sometimes there are security issues that are actionable.

Only the latest version is supported, and it is recommended that you check
whether the latest `master` branch is vulnerable before reporting any issue.

To report a security vulnerability, please read
[Privately reporting a security vulnerability](https://docs.github.com/en/code-security/security-advisories/guidance-on-reporting-and-writing/privately-reporting-a-security-vulnerability#privately-reporting-a-security-vulnerability).
Take into account what is written in this document about the inherent difficulties in
securing this software, and keep in mind that nobody is being paid to work on EchoSVG.
If the fix requires a large amount of work, it may never be produced.

<br/>

## Using EchoSVG safely

If you are concerned about security and privacy, be sure to learn about the features
of this library that have security implications, like the `KEY_ALLOW_EXTERNAL_RESOURCES`,
`KEY_CONSTRAIN_SCRIPT_ORIGIN` or `KEY_ALLOWED_SCRIPT_TYPES` transcoding hints.

But keep in mind that there is no way to be safe when processing untrusted content
(and hence the recommendation given previously to use operating system containers).
