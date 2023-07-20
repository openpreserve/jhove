---
layout: page
title: Logging
---

A Guide to Logging in JHOVE
===========================

Controlling JHOVE Application Logging Levels
--------------------------------------------

To change the logging level when you run the code, you can either add something like this to the config file:

```xml
<jhoveConfig version="1.0"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xmlns="http://hul.harvard.edu/ois/xml/ns/jhove/jhoveConfig"
 xsi:schemaLocation="http://hul.harvard.edu/ois/xml/ns/jhove/jhoveConfig
                     http://hul.harvard.edu/ois/xml/xsd/jhove/1.6/jhoveConfig.xsd">
 <jhoveHome>/home/cfw/jhove-dev</jhoveHome>
 <defaultEncoding>utf-8</defaultEncoding>
 <bufferSize>131072</bufferSize>
 <module>
  ...
 </module>
  <logLevel>ALL</logLevel>
</jhoveConfig>
```
Where the log level honours the following values:

```xml
<xs:enumeration value="OFF"/>
<xs:enumeration value="SEVERE"/>
<xs:enumeration value="WARNING"/>
<xs:enumeration value="INFO"/>
<xs:enumeration value="CONFIG"/>
<xs:enumeration value="FINE"/>
<xs:enumeration value="FINER"/>
<xs:enumeration value="FINEST"/>
<xs:enumeration value="ALL"/>
```
So setting `<logLevel>ALL</logLevel>` shows all message, where `<logLevel>WARNING</logLevel>` would display `WARNING` and `SEVERE` log messages only. JHOVE's default settings show only `SEVERE` messages.

You can also change the logging level by passing a `-l` param with the same level values, e.g. `jhove --version -l ALL` would set the log level to `ALL` and overrides the config setting.

Logging from Java Code
----------------------
If you're new to Java or Java logging please read the official guide to [Java logging](https://docs.oracle.com/javase/8/docs/technotes/guides/logging/overview.html).
Please use native Java logging rather than introduce a dependency.
The correct import and logger initialization look like this:

```java
import java.util.logging.Level;
import java.util.logging.Logger;

public class SomeClass {
    /** Logger for this class. */
    private static final Logger logger = Logger.getLogger(SomeClass.class.getCanonicalName());
}
```

The logger can then be used to log messages at a defined level. These levels range from `Level.FINEST` (detailed debug) to `Level.SEVERE` for unrecoverable errors. You can log messages at your chosen level using:

```java
logger.log(Level.FINE, "message")
logger.log(Level.FINE, "message", exception)
```

The second example will also log the results of the accompanying exception.
