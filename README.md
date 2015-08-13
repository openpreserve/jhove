JHOVE
=====
*JSTOR/Harvard Object Validation Environment*

Licensing
---------
Copyright 2003-2012 by JSTOR and the President and Fellows of Harvard College,
20015 - by the [Open Preservation Foundation](http://openpreservation.org).
JHOVE is made available under the
[GNU Lesser General Public License (LGPL)](http://www.gnu.org/licenses/lgpl.html).

Rev. 1.12.1, 2015-08-13

Overview
--------
JHOVE (the JSTOR/Harvard Object Validation Environment, pronounced "jhove")
is an extensible software framework for performing format identification,
validation, and characterization of digital objects.

 * Format identification is the process of determining the format to which a
   digital object conforms: "I have a digital object; what format is it?"
 * Format validation is the process of determining the level of compliance of a
   digital object to the specification for its purported format: "I have an
   object purportedly of format F; is it?"
 * Format characterization is the process of determing the format-specific
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

CD Status
---------

- [![Build Status](https://travis-ci.org/openpreserve/jhove.svg?branch=integration)](https://travis-ci.org/openpreserve/jhove "JHOVE Travis-CI integration build") Travis-CI: `mvn install integration`

- [![Build Status](http://jenkins.opf-labs.org/buildStatus/icon?job=jhove-integration)](http://jenkins.opf-labs.org/job/jhove-integration/ "OPF Jenkins integration build") OPF Jenkins: `mvn verify integration`

- [![Build Status](http://jenkins.opf-labs.org/buildStatus/icon?job=jhove-mvn)](http://jenkins.opf-labs.org/job/jhove-mvn/ "OPF Jenkins maven deploy build") OPF Jenkins: `mvn deploy integration`

Version 1.12
--------------
JHOVE 1.12.x will be released by September 30th.
### What's changed?
 - Maven build and packaging.
 - Simple cross platform installer.
 - Automated test harness.
 - Automated build, test, QA and deployment.

### What's not changed?
The code, well as little as possible. A small change was required to enable the
Maven build to update the version and date details automatically. I've (@carlwilson) made a
few "non-destructive" changes to the code, mainly using compiler/IDE guidance:
removing unnecessary else's; and eliminating some obvious repetition.

Pre-requisites
--------------

 1. Java J2SE 1.5
    (JHOVE was originally implemented using the Sun J2SE SDK 1.4.1 and has
    been tested to work with 1.5). Version 1.12 of JHOVE is built and
    tested against Oracle JDK 7, and OpenJDK 6 & 7 on Travis. Relesases are
    built using Oracle JDK 7 from the [OPF's Jenkins server](http://jenkins.opf-labs.org/view/D-JHOVE/).

 2. If you would like to build JHOVE from source, then life will be easiest if
    you use [Apache Maven](https://maven.apache.org/).

Distribution
------------
We'ved moved to Maven and we've taken the opportunity to update the distribution.
For now we're producing:
 - a Maven package for developers wishing to incorporate JHOVE in their
   own software;
 - a "fat" (1MB) jar that contains the old CLI and desktop GUI, for anyone who doesn't
   want to use the new installer;
 - a simple cross platform installer that installs the application jar, support
   scripts, etc.

Currently all options, including the installer require Java 1.5 or more recent
to be pre-installed.

Installation
------------

New installer instructions coming soon....

Usage
-----

    java Jhove [-c config] [-m module] [-h handler] [-e encoding] [-H handler]
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

    java -jar JhoveView.jar -c <configFile>

where <configFile> is the pathname of the JHOVE configuration file.

Project Structure
-----------------
A quick introduction to the restructured Maven project. The project's been broken
into 3 Maven modules with an additional installer module added.

    jhove/
      |-jhove-apps/
      |-jhove-core/
      |-jhove-installer/
      |-jhove-modules/

The ```jhove``` project root, acts as a Maven parent and reactor for the submodules.
The ```jhove-core``` module contains all of the main data types and definitions and the
output handlers. ```jhove-modules``` contains all of JHOVE's core module implementations.
The two modules are all that are required for developers wishing to call and run
JHOVE from their own code. ```jhove-apps``` contains the command line and GUI
application code and builds a fat jar that contains the entire Java application.
Finally the ```jhove-installer``` module takes the fat jar and creates a Java based
installer for JHOVE. The installer bundles up invocation scripts and the like,
installs them under ```<userHome>/jhove/``` (default, can be changed) while looking after:

 - variable substitution (ensure that JHOVE_HOME and the like are set to
   reflect a users install, location;
 - making sure that Windows users get batch scripts, while Mac and linux users
   get bash scripts; and
 - optionally generate unattended install and uninstall files.
