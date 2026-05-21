---
title: Distribution
nav_title: Distribution
nav_order: 6
section: distribution
parent: null
summary: Download the latest JHOVE release and related release history.
layout: page
---

# JHOVE Distribution

The latest version of JHOVE can be [downloaded from GitHub here](https://github.com/openpreserve/jhove/releases/latest/). A full history of JHOVE releases is also available from the [GitHub downloads page](https://github.com/openpreserve/jhove/releases/).

## 1 Requirements

1. [Java J2SE 1.4](http://java.sun.com/j2se/1.4)  
   (JHOVE was originally implemented using the SUN J2SE SDK 1.4.1, but has also been tested to run properly under [Sun J2SE SDK 5](http://java.sun.com/javase/downloads/index_jdk5.jsp).)

2. To recompile the JHOVE source code, [Apache Ant](http://ant.apache.org/) is required.  
   Note that the `JAVA_HOME` environment variable must be appropriately assigned for Ant to function properly.  
   (JHOVE was implemented and tested using ANT 1.5.1.)

JHOVE should be usable on any Unix, Windows, or OS X platform with the appropriate J2SE installation.

## 2 Distribution

JHOVE is distributed as GZIP'ed TAR and ZIP files under the terms of the [GNU Lesser General Public License (LGPL)](http://www.gnu.org/licenses/licenses.html#LGPL).

## 3 Third-party modules

Modules for additional file formats have been written by authors outside of the Harvard University Library (HUL). These are [available for downloading](/modules/third-party/). Please note that these modules are *not* supported by HUL.

## 4 Installation

The files of the standard HUL-supported distribution packages can be unpacked by:

```bash
gunzip jhove-1_1.tar.gz
tar xvf jhove-1_1.tar

gunzip jhove-examples.tar.gz
tar xvf jhove-examples.tar
```

or

```bash
unzip jhove-1_1.zip
unzip jhove-example.zip
```

This will produce the following directory structure:

```plaintext
jhove/
  COPYING                          # GNU Lesser General Public License
  LICENSE                          # JHOVE license information
  README
  RELEASENOTES                     # JHOVE release notes
  ...
```

### 4.1 Directory Structure

Refer to the directory structure above.

### 4.2 Installation

After unpacking the distribution, edit the configuration file `jhove/conf/jhove.conf` and set the `<jhoveHome>` element to the absolute pathname of the JHOVE installation directory and the temporary directory:

```xml
<jhoveHome>jhove-installation-directory</jhoveHome>
<tempDirectory>temporary-directory</tempDirectory>
```

For example, on a Unix system:

```xml
<jhoveHome>/users/sampleuser/projects/jhove</jhoveHome>
<tempDirectory>/var/tmp</tempDirectory>
```

Edit the JHOVE Bourne shell driver script `jhove` to set the JHOVE home directory, Java home directory, and Java interpreter:

```bash
JHOVE_HOME=/<jhove-home-directory>
JAVA_HOME=/<java-home-directory>
JAVA=$JAVA_HOME/bin/java
```

For Windows systems, edit the DOS shell driver script `jhove.bat`:

```cmd
SET JHOVE_HOME=<jhove-home-directory>
SET JAVA_HOME=<java-home-directory>
SET JAVA=%JAVA_HOME%\bin\java
```

### 4.3 Configuring JHOVE

JHOVE uses an XML-formatted configuration file. Refer to the [documentation](http://hul.harvard.edu/ois/xml/xsd/jhove/jhoveConfig.xsd) for details.

### 4.4 Testing

Invoke JHOVE on the example file `control.txt`:

```bash
jhove -c conf/jhove.conf -k examples/ascii/control.txt
```

This should generate output similar to:

```plaintext
Jhove (Rel. 1.0, 2005-05-26)
 Date: 2005-05-12 10:20:43 EDT
 RepresentationInformation: examples/ascii/control.txt
  ReportingModule: ASCII-hul, Rel. 1.1 (2005-01-11)
  ...
```
