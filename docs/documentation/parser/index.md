---
title: Selecting an XML Parser
nav_title: Parser
nav_order: 1
section: documentation
parent: Documentation
summary: Guidance for choosing and configuring an XML parser for JHOVE.
layout: page
---

# Selecting an XML parser (_draft_, 2007-03-30)

The default XML parser provided with Java 1.4 does not support schema validation. For this or other reasons, you may want to use a different XML parser with JHOVE. [Xerces2](http://xerces.apache.org/xerces2-j/) is a widely used parser which provides schema validation; any parser which conforms to Java's XML API may also be used. To use an XML parser with JHOVE, take the following steps:

**1.** Obtain the necessary `jar` files for the parser. In the case of Xerces, this will be `xercesImpl.jar`.

**2.** Add the `jar` file or files to the classpath that will be used when you invoke JHOVE. If you are invoking `jhove` (Unix/Linux) or `jhove.bat` (Windows) from the command line, then add the path to the parser jar file to `EXTRA_JARS`.

If you have been invoking JhoveViewer.jar by double-clicking it, you will need to create a command line file to run it with a third-party XML parser. The simplest way to do this is to make a modified copy of `jhove` or `jhove.bat` and change the line which runs JHOVE (the last line of the file) to:  

`${JAVA} -classpath $CP JhoveView`   (Unix/Linux)  
or  
`%JAVA% -classpath %CP% JhoveView`   (Windows)  

Then make the changes as with command line JHOVE.

If you are currently invoking JHOVE with a custom command-line script that uses the `java -jar` option, your classpath will not be recognized. Instead, put JhoveApp.jar or JhoveViewer.jar into your classpath and invoke the main class, `Jhove` or `JhoveView`. For example:  

`java -classpath bin/xerces-impl.jar:bin/JhoveApp.jar Jhove [parameters]`

**3.** Specify the parser class in your command line or configuration file. The class to specify must be a subclass of `org.xml.sax.XMLReader`. If you're using Xerces and not creating your own subclass, this would usually be `org.apache.xerces.parsers.SAXParser`.

In the JHOVE command line, you would specify this as  

  `-x _[sax-class]_`

You can do this permanently by setting the value of `$ARGS` in the `jhove` or `jhove.bat` script file. Ignore the warning against not editing below the line.

Alternatively, you can specify the name of the parser in the Java properties used by the application. The name of the property is `edu.harvard.hul.ois.jhove.saxClass`. This can be specified in the command line, e.g.,  

  `java -Dedu.harvard.hul.ois.jhove.saxClass=org.apache.xerces.parsers.SAXParser ...`  

Since the viewer does not recognize JHOVE command line parameters, you must use a property to specify the parser when running the viewer.
