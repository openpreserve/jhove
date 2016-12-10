JHOVE
=====
*JSTOR/Harvard Object Validation Environment*

Licensing
---------
Copyright 2003-2012 by JSTOR and the President and Fellows of Harvard College,
2015-2016 by the [Open Preservation Foundation](http://openpreservation.org).
JHOVE is made available under the
[GNU Lesser General Public License (LGPL)](http://www.gnu.org/licenses/lgpl.html).

Rev. 1.14.6, 2016-05-12

JHOVE Homepage
--------------
<http://jhove.openpreservation.org/>

Overview
--------
JHOVE (the JSTOR/Harvard Object Validation Environment, pronounced "jove")
is an extensible software framework for performing format identification,
validation, and characterization of digital objects.

 * Format identification is the process of determining the format to which a
   digital object conforms: "I have a digital object; what format is it?"
 * Format validation is the process of determining the level of compliance of a
   digital object to the specification for its purported format: "I have an
   object purportedly of format F; is it?"
 * Format characterization is the process of determining the format-specific
   significant properties of an object of a given format: "I have an object of
   format F; what are its salient properties?"

These actions are frequently necessary during routine operation of digital
repositories and for digital preservation activities.

The output from JHOVE is controlled by output handlers. JHOVE uses an
extensible plug-in architecture; it can be configured at the time of its
invocation to include whatever specific format modules and output handlers
that are desired. The initial release of JHOVE includes modules for
arbitrary byte streams, ASCII and UTF-8 encoded text, AIFF and WAVE audio,
GIF, JPEG, JPEG 2000, TIFF, and PDF; and text and XML output handlers.

The JHOVE project is a collaboration of JSTOR and the Harvard University
Library.  Development of JHOVE was funded in part by the Andrew W. Mellon
Foundation.  JHOVE is made available under the GNU Lesser General Public
License (LGPL; see the file LICENSE for details).

JHOVE is currently being maintained by the
[Open Preservation Foundation](http://openpreservation.org).

Build Status
------------
 * [![Build Status](https://travis-ci.org/openpreserve/jhove.svg?branch=integration)](https://travis-ci.org/openpreserve/jhove "JHOVE Travis-CI integration build") Travis-CI: `mvn install integration`
 * [![Build Status](http://jenkins.openpreservation.org/buildStatus/icon?job=jhove-integration)](http://jenkins.openpreservation.org/job/jhove-integration/ "OPF Jenkins integration build") OPF Jenkins: `mvn verify integration`
 * [![Build Status](http://jenkins.openpreservation.org/buildStatus/icon?job=jhove-v1.14)](http://jenkins.openpreservation.org/job/jhove-v1.14/ "OPF Jenkins Maven deploy build") OPF Jenkins: `mvn deploy integration`

Pre-requisites
--------------
 1. Java JRE 1.6  
    (JHOVE was originally implemented using the Sun J2SE SDK 1.4.1 and has
    been tested to work with 1.5). Version 1.14 of JHOVE is built and
    tested against Oracle JDK 7, and OpenJDK 6 & 7 on Travis. Releases are
    built using Oracle JDK 7 from the [OPF's Jenkins server](http://jenkins.openpreservation.org/).

 2. If you would like to build JHOVE from source, then life will be easiest if
    you use [Apache Maven](https://maven.apache.org/).

Getting JHOVE
-------------

### For Users: JHOVE Cross Platform Installer
You can download the [latest version of JHOVE here](http://software.openpreservation.org/rel/jhove-latest.jar).

### For Developers: JHOVE JARs via Maven
JHOVE is available through Maven, currently we're distributing through the
[OPF's Maven repository](http://artifactory.openpreservation.org/artifactory/simple/opf-dev-local/org/openpreservation/jhove/jhove/) during development. The 1.14 release artifacts
will be published to Maven Central. If you'd like to try the latest Maven modules then
add:

```xml
<repositories>
  <repository>
    <snapshots>
      <enabled>false</enabled>
    </snapshots>
    <id>opf-dev-mvn</id>
    <name>opf-dev</name>
    <url>http://artifactory.openpreservation.org/artifactory/opf-dev/</url>
  </repository>
</repositories>
```

to your project's POM file, then use this dependency for the core classes (e.g.
`JhoveBase`, `Module`, `ModuleBase`, etc.):

```xml
<dependency>
  <groupId>org.openpreservation.jhove</groupId>
  <artifactId>jhove-core</artifactId>
  <version>1.14.6</version>
</dependency>
```

and this for the JHOVE core module implementations:

```xml
<dependency>
  <groupId>org.openpreservation.jhove</groupId>
  <artifactId>jhove-modules</artifactId>
  <version>1.14.6</version>
</dependency>
```

### For Developers: Building JHOVE from Source
Clone this project, checkout the integration branch, and use Maven, e.g.:

    git clone git@github.com:openpreserve/jhove.git
    cd jhove
    git checkout integration
    mvn clean install

See the [Project Structure](#project-structure) section for a guide to
the Maven artifacts produced by the build.

Installation
------------

### Application Installation
Download the JHOVE installer, this requires Java 1.6 or later to be pre-installed.
We'll assume that you've downloaded `<userHome>/Downloads/jhove-xplt-installer-1.14.x.jar`,
where `x` is the current revision/build number. Installation is OS dependant.

#### Windows
*Currently only tested on Windows 7.*

Simply double-click the downloaded installer JAR. If Java is installed then the
windowed installer will guide you through selection. It's best to stay with
the default choices if installing the beta.

Once the installation has finished you'll be able to double-click
`C:\Users\yourName\jhove\jhove-gui` to start the JHOVE GUI. Alternatively,
open a Command window, e.g. press the `Windows` key and type `cmd`, then issue
these commands:

    C:\Users\yourName>cd jhove
    C:\Users\yourName\jhove>jhove

to display the command-line usage message.

### Mac OS
*Currently only tested on OS X Mavericks.*

Simply double-click the downloaded installer JAR. If Java is installed then the
windowed installer will guide you through selection. It's best to stay with
the default choices if installing the beta.

Once the installation has finished you'll be able to double-click
`/Users/yourName/jhove/jhove-gui` to start the JHOVE GUI. Alternatively,
open a Terminal command window and then issue these commands:

    cd ~/jhove
    ./jhove

to display the command-line usage message.

### Linux
*Currently tested on Ubuntu 16.10 and Debian Jessie.*

Once the installer has downloaded, start a terminal, e.g. `Ctrl+Alt+T`,
and type the following, assuming the download is in `~/Downloads`:

    java -jar ~/Downloads/java-xplt-installer-1.14.x.jar

Once the installation is finished you'll be able to:

    cd ~/jhove
    ./jhove

to run the command-line application and show the usage message. Alternatively:

    cd ~/jhove
    ./jhove-gui

will run the GUI application.

Distribution
------------
We've moved to Maven and have taken the opportunity to update the distribution.
For now we're producing:
 * a Maven package, for developers wishing to incorporate JHOVE into their
   own software;
 * a "fat" (1MB) JAR that contains the old CLI and desktop GUI, for anyone
   who doesn't want to use the new installer; and
 * a simple cross-platform installer that installs the application JAR, support
   scripts, etc.

Currently all options, including the installer, require Java 1.6 or above to be
pre-installed. Supporting 1.5 is no longer realistic, Oracle ceased support for
its own 1.6 distribution in 2012. We've kept to 1.6 as a transition towards moving
to 1.7. If you really need a 1.5-compatible build then you can override the target
version for the Maven build, e.g.:

    mvn clean install -Djava.target.version=1.5

This also produces an installer that will work on 1.5 JREs.

Usage
-----

    jhove [-c config] [-m module] [-h handler] [-e encoding] [-H handler]
               [-o output] [-x saxclass] [-t tempdir] [-b bufsize]
               [-l loglevel] [[-krs] dir-file-or-uri [...]]

    -c config   Configuration file pathname
    -m module   Module name
    -h handler  Output handler name (defaults to TEXT)
    -e encoding Character encoding used by output handler (defaults to UTF-8)
    -H handler  About handler name
    -o output   Output file pathname (defaults to standard output)
    -x saxclass SAX parser class (defaults to J2SE default)
    -t tempdir  Temporary directory in which to create temporary files
    -b bufsize  Buffer size for buffered I/O (defaults to J2SE 1.4 default)
    -l loglevel Logging level
    -k          Calculate CRC32, MD5, and SHA-1 checksums
    -r          Display raw data flags, not textual equivalents
    -s          Format identification based on internal signatures only
    dir-file-or-uri Directory or file pathname or URI of formated content
                    stream

All named modules and output handlers must be found on the Java CLASSPATH at
the time of invocation.  The JHOVE driver script, jhove/jhove, automatically
sets the CLASSPATH and invokes the Jhove main class:

    jhove [-c config] [-m module] [-h handler] [-e encoding] [-H handler]
          [-o output] [-x saxclass] [-t tempdir] [-b bufsize] [-l loglevel]
          [[-krs] dir-file-or-uri [...]]

The following additional programs are available, primarily for testing
and debugging purposes.  They display a minimally processed, human-readable
version of the contents of AIFF, GIF, JPEG, JPEG 2000, PDF, TIFF, and WAVE
files:

    java ADump  aiff-file
    java GDump  gif-file
    java JDump  jpeg-file
    java J2Dump jpeg2000-file
    java PDump  pdf-file
    java TDump  tiff-file
    java WDump  wave-file

For convenience, the following driver scripts are also available:

    adump  aiff-file
    gdump  gif-file
    jdump  jpeg-file
    j2dump jpeg2000-file
    pdump  pdf-file
    tdump  tiff-file
    wdump  wave-file

The JHOVE Swing-based GUI interface can be invoked from a command shell from
the jhove/bin sub-directory:

    jhove-gui -c <configFile>

where `<configFile>` is the pathname of the JHOVE configuration file.

Project Structure
-----------------
A quick introduction to the restructured Maven project. The project's been broken
into three Maven modules with an additional installer module added.

    jhove/
      |-jhove-apps/
      |-jhove-core/
      |-jhove-installer/
      |-jhove-modules/

All Maven artifacts are produced in versioned form,
i.e. `${artifactId}-${project.version}.jar`, where `${project.version}` defaults
to `1.14.0-SNAPSHOT` unless you explicitly set the version number.

### jhove
The `jhove` project root acts as a Maven parent and reactor for the sub-modules.
This simply builds sub-modules and doesn't produce any artifacts, but decides
which sub-modules are built.

The `jhove-core` and `jhove-modules` are most likely all that are required for
developers wishing to call and run JHOVE from their own code.

### jhove-core
The `jhove-core` module contains all of the main data type definitions and the
output handlers. This module produces a single JAR:

    ./jhove/jhove-core/target/jhove-core-${project.version}.jar

The `jhove-core` JAR contains a single module implementation, the default
`BytestreamModule`. For the format-specific modules you'll need 
the `jhove-modules` JAR.

### jhove-modules
The `jhove-modules` contain all of JHOVE's core format specific module 
implementations, specifically:

 * AIFF
 * GIF
 * HTML
 * IFF
 * JPEG
 * JPEG 2000
 * PDF
 * TIFF
 * WAVE
 * XML

These are all packaged in a single modules JAR:

    ./jhove/jhove-modules/target/jhove-modules-${project.version}.jar

### jhove-apps
The `jhove-apps` module contains the command-line and GUI application code and 
builds a fat JAR containing the entire Java application. This JAR can be used
to execute the command-line app:

    ./jhove/jhove-apps/target/jhove-apps-${project.version}.jar

### jhove-installer
Finally, the `jhove-installer` module takes the fat JAR and creates a Java-based
installer for JHOVE. The installer bundles up invocation scripts and the like,
installs them under `<userHome>/jhove/` (default, can be changed) while also
looking after:

 * variable substitution to ensure that JHOVE_HOME and the like are set to
   reflect a user's install location;
 * making sure that Windows users get batch scripts, while Mac and Linux users
   get bash scripts; and
 * optionally generating unattended install and uninstall files.

The module produces two JARs, one called `jhove-installer-${project.version}`, which
contains the JARs for the installer, and an executable JAR to install JHOVE:

    ./jhove/jhove-installer/target/jhove-xplt-installer-${project.version}.jar

The `xplt` stands for cross-platform.
