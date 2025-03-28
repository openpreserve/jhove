---
title: Configuration
layout: page
---

# Configuring JHOVE

For proper operation, the `<jhoveHome>` element in the configuration file, `jhove/conf/jhove.conf`, must be edited to point to the absolute pathname of the JHOVE installation, or home, directory and the temporary directory (in which temporary files are created):

      <jhoveHome>jhove-home-directory</jhoveHome>
      <tempDirectory>temporary-directory</tempDirectory>
  

The JHOVE home directory is the top-most directory in the distribution TAR or ZIP file. On Unix systems, /var/tmp is an appropriate temporary directory; on Windows, C:\\Temp. For example, if the distribution TAR file is disaggregated on a Unix system in the directory "/users/stephen/projects", then the configuration file should read:

    <jhoveHome>/users/stephen/projects/jhove</jhoveHome>
    <tempDirectory>/var/tmp</jhoveHome>
  

In the JHOVE home directory, copy the JHOVE Bourne shell driver script template, "jhove.tmpl", to "jhove" (or the equivalent Windows shell script, "jhove\_bat.tmpl" to "jhove.bat"), and set the JHOVE home directory, Java home directory, and Java interpreter:

    JHOVE_HOME=jhove-home-directory
    JAVA_HOME=java-home-directory
    JAVA=java-interpreter
  

where JHOVE\_HOME is set to specify the absolute pathname of the JHOVE home directory; JAVA\_HOME is set to specify the absolute pathname of the Java home directory; and JAVA is set to specify the absolute pathname of the Java interpreter. For example:

    JHOVE_HOME=/users/[username]/projects/jhove
    JAVA_HOME=/usr/local/java/jdk1.6.0_20-32
    JAVA=$JAVA_HOME/bin/java
  

In the Windows shell driver script, "jhove.bat", the equivalent three variables are:

    SET JHOVE_HOME=jhove-home-directory
    SET JAVA_HOME=java-home-directory
    SET JAVA=%JAVA_HOME%\bin\java


For example:

    SET JHOVE_HOME="C:\Program Files\jhove"
    SET JAVA_HOME="C:\Program Files\java\jdk1.6.0_20-32"
    SET JAVA=%JAVA_HOME%\bin\java
  

The quotation marks are necessary because of the embedded space characters. On Windows platforms it may also be necessary to add the Java bin subdirectory to the System PATH environment variable:

    PATH=C:\Program Files\java\jdk1.6.0_20-32\bin;...
  

Specific instructions on installing JHOVE in a Windows XP environment are [available](/getting-started/windows/). For additional information on setting a Windows environment variable, consult your local documentation or system administrator.

Starting with version 1.8, it is no longer necessary to specify JAVA\_HOME or JAVA in the Linux/Unix shell script, and starting with 1.9, it is no longer necessary to specify it in the Windows batch file.

At the time of its invocation, JHOVE performs dynamic configuration of its modules and output handlers based on a XML-formatted configuration file. The configuration file is specified by the first valid value defined as:

1.  The \-c _config_ [command line](/getting-started#invocation) argument (only for the command-line interface);
2.  The file ${_user.home_}/jhove/conf/jhove.conf, where ${_user.home_} is the standard Java user.home property; or
3.  The edu.harvard.hul.ois.jhove.config property in the properties file ${_user.home_}/jhove/jhove.properties.

Here are some typical "user.home" locations for various operating systems with the default Java configuration:

**Windows XP:** C:\\Documents and Settings\\{username}  
**Windows Vista and 7:** C:\\Users\\{username} or perhaps C:\\{username}  
**Macintosh OS X:** /Users/{username}  
**Unix:** ~/  

Note that the GUI interface only searches for the configuration file at the second and third locations listed above; it does not make use of the \-c _config_ option.

All format modules and output handlers must be specified in the XML-formatted configuration file, validatable against the XML Schema <[http://hul.harvard.edu/ois/xml/xsd/jhove/jhoveConfig.xsd](http://hul.harvard.edu/ois/xml/xsd/jhove/jhoveConfig.xsd)\>. (In the following display, brackets \[ and \] enclose optional configuration file elements.)

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

The optional <defaultEncoding> element specifies the default character encoding used by output handlers. This option can also be specified by the \-e _encoding_ [command line](/getting-started#invocation) argument. The default output encoding is UTF-8.

The optional <tempDirectory> element specifies the pathname of the directory in which temporary files are created. This option can also be specified by the \-t _directory_ [command line](/getting-started#invocation) argument. On most Unix systems, a reasonable temporary directory is "/var/tmp"; on Windows, "C:\\temp".

The optional <bufferSize> element specifies the buffer size use for buffered I/O. This option can also be specified by the \-b _buffer_ [command line](/getting-started#invocation) argument.

The optional <mixVersion> element specifies the MIX schema version conformance for the output produced by the XML output handler. By default the handler output conforms to version 2.0 of the schema. For version 1.0 conformance, specify:

    <mixVersion>1.0<mixVersion>
  

The optional <sigBytes> element specifies the maximum number of byte that JHOVE modules will examine looking for an internal signature (or magic number). The default value is 1024.

The optional <logLevel> element specifies the logging level, used by calls to the logging API. This option can also be specified by the \-l _log-level_ [command line](/getting-started#invocation) argument. The default is SEVERE.

All class names must be fully qualified with their package name, for example:

    edu.harvard.hul.ois.jhove.module.AsciiModule
    edu.harvard.hul.ois.jhove.module.PdfModule
    edu.harvard.hul.ois.jhove.module.TiffModule
    edu.harvard.hul.ois.jhove.module.Utf8Module
  

The order in which format modules are defined is important; when performing a format identification operation, JHOVE will search for a matching module in the order in which the modules are defined in the configuration file. In general, the modules for more generic formats should come later in the list. For example, the standard module [ASCII](/modules/ascii/) should be defined before the [UTF-8](/modules/utf8/) module, since all ASCII objects are, by definition, UTF-8 objects, but not vice versa.

The optional <init> element is used to pass a module-specific argument to a module at the time it is first instantiated within JHOVE. See the details for the individual modules to see if such an argument is defined. The use of the <init> argument is currently not defined for any of the standard JHOVE modules.

The optional and repeatable <param> element is used to pass a module-specific parameter to a module immediately prior to each invocation of the module's parse() method. See the details for the individual modules to see if such a parameter is defined.

In addition to the modules and output handlers specified in the configuration file, JHOVE is also always statically linked with the standard Bytestream module and Text and XML output handlers.