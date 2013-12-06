hotpgen
=======

Simple implementation of a java HMAC One Time Password generator,  This program generates, prints, and places into the
clipboard a code that is used for multifactor authentication.


1. Build

2. Run with provided key. The key is encrypted and persisted to hotp.properties.  For extra protection, include a
password, which will be used for stronger encryption in the properties file.

Example: java -jar hotpgen.jar -k JRCVONLIM5GXK3LL -p PASSWORD


3. After the initial run, do not include the key when running the program.  If you included a password when you first
provided the key, the password must be provided so the key can be read from the file.

Example: java -jar hotpgen.jar -p PASSWORD
