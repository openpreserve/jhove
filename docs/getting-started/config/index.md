---
title: Configuration
layout: page
---

# Configuring JHOVE

For proper operation, the `<jhoveHome>` element in the configuration file, `jhove/conf/jhove.conf`, must be edited to point to the absolute pathname of the JHOVE installation, or home, directory and the temporary directory (in which temporary files are created):

```xml
<jhoveHome><em>jhove-home-directory</em></jhoveHome>
<tempDirectory><em>temporary-directory</em></tempDirectory>
```

The JHOVE home directory is the top-most directory in the distribution TAR or ZIP file. On Unix systems, `/var/tmp` is an appropriate temporary directory; on Windows, `C:\Temp`. For example, if the distribution TAR file is disaggregated on a Unix system in the directory `/users/stephen/projects`, then the configuration file should read:

```xml
<jhoveHome>/users/stephen/projects/jhove</jhoveHome>
<tempDirectory>/var/tmp</tempDirectory>
```

In the JHOVE home directory, copy the JHOVE Bourne shell driver script template, `jhove.tmpl`, to `jhove` (or the equivalent Windows shell script, `jhove_bat.tmpl` to `jhove.bat`), and set the JHOVE home directory, Java home directory, and Java interpreter:

```bash
JHOVE_HOME=<em>jhove-home-directory</em>
JAVA_HOME=<em>java-home-directory</em>
JAVA=<em>java-interpreter</em>
```

where `JHOVE_HOME` is set to specify the absolute pathname of the JHOVE home directory; `JAVA_HOME` is set to specify the absolute pathname of the Java home directory; and `JAVA` is set to specify the absolute pathname of the Java interpreter. For example:

```bash
JHOVE_HOME=/users/[username]/projects/jhove
JAVA_HOME=/usr/local/java/jdk1.6.0_20-32
JAVA=$JAVA_HOME/bin/java
```

In the Windows shell driver script, `jhove.bat`, the equivalent three variables are:

```cmd
SET JHOVE_HOME=<em>jhove-home-directory</em>
SET JAVA_HOME=<em>java-home-directory</em>
SET JAVA=%JAVA_HOME%\bin\java
```

For example:

```cmd
SET JHOVE_HOME="C:\Program Files\jhove"
SET JAVA_HOME="C:\Program Files\java\jdk1.6.0_20-32"
SET JAVA=%JAVA_HOME%\bin\java
```

The quotation marks are necessary because of the embedded space characters. On Windows platforms, it may also be necessary to add the Java bin subdirectory to the System PATH environment variable:

```cmd
PATH=C:\Program Files\java\jdk1.6.0_20-32\bin;...
```

Specific instructions on installing JHOVE in a Windows XP environment are [available](/getting-started/windows/). For additional information on setting a Windows environment variable, consult your local documentation or system administrator.

Starting with version 1.8, it is no longer necessary to specify `JAVA_HOME` or `JAVA` in the Linux/Unix shell script, and starting with 1.9, it is no longer necessary to specify it in the Windows batch file.

At the time of its invocation, JHOVE performs dynamic configuration of its modules and output handlers based on an XML-formatted configuration file. The configuration file is specified by the first valid value defined as:

1. The `-c <config>` [command line](/getting-started#invocation) argument (only for the command-line interface);
2. The file `${user.home}/jhove/conf/jhove.conf`, where `${user.home}` is the standard Java `user.home` property; or
3. The `edu.harvard.hul.ois.jhove.config` property in the properties file `${user.home}/jhove/jhove.properties`.

Here are some typical `user.home` locations for various operating systems with the default Java configuration:

- **Windows XP:** `C:\Documents and Settings\{username}`
- **Windows Vista and 7:** `C:\Users\{username}` or perhaps `C:\{username}`
- **Macintosh OS X:** `/Users/{username}`
- **Unix:** `~/`

Note that the GUI interface only searches for the configuration file at the second and third locations listed above; it does not make use of the `-c <config>` option.

All format modules and output handlers must be specified in the XML-formatted configuration file, validatable against the XML Schema `<http://hul.harvard.edu/ois/xml/xsd/jhove/jhoveConfig.xsd>`. (In the following display, brackets [ and ] enclose optional configuration file elements.)

```xml
<?xml version="1.0"?>
<jhoveConfig version="1.0"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xmlns="http://hul.harvard.edu/ois/xml/ns/jhove/jhoveConfig"
xsi:schemaLocation="http://hul.harvard.edu/ois/xml/ns/jhove/jhoveConfig
                     http://hul.harvard.edu/ois/xml/xsd/jhove/jhoveConfig.xsd">
<jhoveHome><em>jhove-home-directory</em></jhoveHome>
[ <defaultEncoding><em>encoding</em></defaultEncoding> ]
[ <tempDirectory><em>directory</em></tempDirectory> ]
[ <bufferSize><em>buffer</em></bufferSize> ]
[ <mixVersion><em>version</em></mixVersion> ]
[ <sigBytes><em>n</em></sigBytes> ]
<module>
<class><em>module-class-name</em></class>
[ <init><em>optional-module-init-argument</em></init> ]
[ <param><em>optional-module-parameter</em></param> ]
...
</module>
...
<outputHandler>
<class><em>output-handler-class-name</em></class>
</outputHandler>
...
[ <logLevel><em>logging-level</em></logLevel> ]
</jhoveConfig>
```

...existing content continues...

{% include footer.html %}
