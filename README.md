# audiveris-eg
This is version 4 of Audiveris OMR application, previously hosted on Kenai, now available on GitHub. 
It has been renamed as audiveris-eg (for Earlier Generation). 

Featurewise, audiveris-eg is a very fast OMR, but with limited recognition capabilities and requiring good quality scores.

Distributionwise, there are significant differences:
* V4.3 relied on various Ant tasks to build OS, architecture and Java dependent binaries, and on JNLP to transparently download the various software pieces on demand.
Unfortunately, JNLP got more and more impeded by increasing restrictions of Java security policies.
* V4.4 relies on a Gradle script to generate an OS/architecture dependent distribution downloaded as a whole.
