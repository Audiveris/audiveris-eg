# audiveris-eg
This is version 4 of Audiveris OMR application, previously hosted on Kenai, now available on GitHub. 
It has been renamed as audiveris-eg (for Earlier Generation). 

Audiveris-eg is a very fast OMR, but with limited recognition capabilities
and requiring good quality scores. Its implementation is based on an iteration mechanism
like that: build glyph, assign shape, check validity. If the validity check fails,
we simply try another iteration. The main difficulty is of course that doing so
we have no way to know if we have actually reached the best interpretation.
For lower-quality input scores, a more sophisticated approach is therefore required.

Morevoer, previous Audiveris-eg release (V4.3) relies on various legacy tools
and technologies. Among them are [Ant](http://ant.apache.org) for building and
[Java Web Start](https://docs.oracle.com/javase/8/docs/technotes/guides/javaws/)
for cross-platform application deployment. Unfortunately, it has become increasigly
difficult to use these technologies in the past few years due to deprecations and
increasing restrictions of Java security policies.

We therefore decided to take the opportunity of Github migration to update audiveris-eg.
The upcoming version (V4.4) will introduce the following changes:

* modern dependency management (no more shipping of binary dependencies)

* replacement of proprietary dependencies with publicly available ones

* deprecation and removal of the Java Web Start in favour of binary distributions

* various modernizations to keep audiveris-eg running on the recent JRE.

## Building and running

First of all, you'll need the following dependencies installed and working from
the command line:

+ [Java Development Kit (JDK) version 7 or later (version 8 is recommended)][1].
+ [Git](https://git-scm.com) version control system.
+ [Gradle command line tool](https://gradle.org) for building Audiveris from source

Besides the above mentioned tools you'll need to have Tesseract language files for
[Tesseract OCR][2] to work properly. Please keep in mind that Tesseract is mandatory
for both building and running Audiveris. __It's currently not possible to use
Audiveris without Tesseract.__

You'll need at least the english language data. Other required languages can be
installed, too. Please check [this guide][3] for further details.

Moreover, opening PDFs containing vector graphics requires [Ghostscript][4] to be
available in your $PATH.

To build audiveris from source, run the following command from the source code
directory:

`gradle build`

To run audiveris as GUI tool, just issue

`gradle run`

## Contributions

__IMPORTANT: Please keep in mind that the main purpose of this repositoy is to preserve
audiveris-eg and to make this old software run on modern OSes. New features should
go into the [new audiveris][5]. For this repository, we'll only accept
bug fixes and compatibilty changes.__

[1]: http://www.oracle.com/technetwork/java/javase/downloads/index.html
[2]: https://github.com/tesseract-ocr/tesseract
[3]: https://github.com/tesseract-ocr/tesseract/wiki
[4]: https://www.ghostscript.com
[5]: https://github.com/Audiveris/audiveris
