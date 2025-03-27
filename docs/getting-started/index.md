---
title: Getting started
layout: page
---

# Getting Started with JHOVE

{: #introduction .section-heading}
## Introduction

JHOVE (pronounced "jove"), the JSTOR/Harvard Object Validation Environment, is an extensible software framework for performing format identification, validation, and characterization of digital objects.

- **Format identification** is the process of determining the format to which a digital object conforms; in other words, it answers the question: "I have a digital object; what format is it?"
- **Format validation** is the process of determining the level of compliance of a digital object to the specification for its purported format, e.g.: "I have an object purportedly of format *F*; is it?"
  - Format validation conformance is determined at three levels: *well-formedness*, *validity*, and *consistency*.
    1. A digital object is *well-formed* if it meets the purely syntactic requirements for its format.
    2. An object is *valid* if it is well-formed and it meets the higher-level semantic requirements for format validity.
    3. An object is *consistent* if it is valid and its internally extracted representation information is consistent with externally supplied representation information.
  - For example, a [TIFF](/references#tiff6) object is well-formed if it starts with an 8-byte header followed by a sequence of Image File Directories (IFDs), each composed of a 2-byte entry count and a series of 8-byte tagged entries. The object is valid if it meets certain additional semantic-level rules, such as that an RGB file must have at least three sample values per pixel. The object is consistent with external [NISO Z39.87](/references#z39.87) metadata if that metadata is consistent with the representation information of the object that is extracted by JHOVE.
  - The concept of distinguishing between well-formedness (syntactic correctness) and validity (semantic correctness) was taken from [XML](/references#xml).
- **Format characterization** is the process of determining the format-specific significant properties of an object of a given format, e.g.: "I have an object of format *F*; what are its salient properties?"
  - The set of characteristics reported by JHOVE about a digital object is known as the object's *representation information*, a concept introduced by the Open Archival Information System (OAIS) reference model ([ISO/IEC 14721](/references#oais)). The standard representation information reported by JHOVE includes: file pathname or URI, last modification date, byte size, format, format version, MIME type, format profiles, and optionally, CRC32, MD5, and SHA-1 digests ([CRC32](/references#crc32), [MD5](/references#md5), [SHA-1](/references#sha1)).

Identification, validation, and characterization actions are frequently necessary during routine operation of digital repositories and for digital preservation activities.

The output from JHOVE is controlled by *output handlers*. JHOVE uses an extensible plug-in architecture; it can be configured at the time of its invocation to include whatever specific format modules and output handlers that are desired. The initial release of JHOVE includes modules for [arbitrary byte streams](/modules/bytestream/), [ASCII](/modules/ascii/) and [UTF-8](/modules/utf8/) encoded text, [TIFF](/modules/tiff/), [HTML](/modules/html/), [XML](/modules/xml/), [JPEG](/modules/jpeg/), [JPEG2000](/modules/jpeg2000/), [PDF](/modules/pdf/), [AIFF](/modules/aiff/), [WAVE](/modules/wave/) audio; and [text](#text) and [XML](#xml) output handlers.

{: #getting-jhove .section-heading}
## Getting JHOVE

JHOVE is written in [Java](http://java.sun.com/j2se/). A J2SE 1.5-compliant Java Runtime Environment (JRE) is required for proper operation of JHOVE. JHOVE should be usable on any Unix, Windows, or OS X platform with the appropriate Java installation.

### Downloading JHOVE

There's now a beta of the new JHOVE installer, the latest version can be [downloaded](http://software.openpreservation.org/rel/jhove-latest.jar) from the OPF's Jenkins server.

### JHOVE for developers

If you'd like to get JHOVE via Maven or build the project from source, please read our [build guide](/documentation/build/).

{: #installing-jhove .section-heading}
## Installing JHOVE

Download the [latest JHOVE installer](http://software.openpreservation.org/rel/jhove-latest.jar), this requires Java 1.6 or later to be pre-installed. We'll assume that you've downloaded:

```
<userHome>/Downloads/jhove-latest.jar
```

Installation is OS dependent.

### Windows

*Currently only tested on Windows 7*

Simply double-click the downloaded installer JAR in Explorer. If Java is installed, then the windowed installer will guide you through selection. It's best to stay with the default choices if installing the beta.

Once the installation is finished, you'll be able to double-click:

```
C:\Users\yourName\jhove\jhove-gui
```

...existing content for Windows, Mac OS, and Linux installation...

{: #running-jhove .section-heading}
## Running JHOVE

There should be no issues configuring the current JHOVE beta, but just in case we've retained the [old configuration guide](/getting-started/config/).

### Usage

```
java Jhove [-c config] [-m module] [-h handler] [-e encoding] [-H handler]
            [-o output] [-x saxclass] [-t tempdir] [-b bufsize]
            [-l loglevel] [[-krs] dir-file-or-uri [...]]
```

...existing content for usage, format identification, validation, characterization, and GUI interface...
