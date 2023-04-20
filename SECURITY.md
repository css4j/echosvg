# Security and Privacy Considerations

EchoSVG handles images in the Scalable Vector Graphics (SVG) format for various
purposes, however it is important to understand its limitations related to security
and privacy.

<br/>

## Security

SVG documents can be complex and can drive any rendering software to its limits.
In general, if a SVG document can cause issues to a web browser that attempts to
render it, that image will also cause problems to Batik.

Unfortunately, this library can also be less secure than web browsers, especially
in scripting security. To execute scripts, EchoSVG relies on the Mozilla Rhino
javascript library, which is embedded via a feature called LiveConnect. [It is well
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

## Using EchoSVG safely

If you are concerned about security and privacy, be sure to learn about the features
of this library that have security implications, like the `KEY_ALLOW_EXTERNAL_RESOURCES`,
`KEY_CONSTRAIN_SCRIPT_ORIGIN` or `KEY_ALLOWED_SCRIPT_TYPES` transcoding hints.
