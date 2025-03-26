---
title: Documentation
layout: page
---

# Documentation

## An introduction to JHOVE {#introduction}

JHOVE provides functions to perform format-specific identification, validation, and characterization of digital objects.

- **Format identification** is the process of determining the format to which a digital object conforms; in other words, it answers the question: "I have a digital object; what format is it?"
- **Format validation** is the process of determining the level of compliance of a digital object to the specification for its purported format, e.g.:  
  "I have an object purportedly of format *F*; is it?"  
  Format validation conformance is determined at two levels: *well-formedness* and *validity*.
  1. A digital object is well-formed if it meets the purely syntactic requirements for its format.
  2. An object is valid if it is well-formed and it meets additional semantic-level requirements.  
     For example, a [TIFF](/references#tiff6) object is well-formed if it starts with an 8-byte header followed by a sequence of Image File Directories (IFDs), each composed of a 2-byte entry count and a series of 8-byte tagged entries. The object is valid if it meets certain additional semantic-level rules, such as that an RGB file must have at least three sample values per pixel.
- **Format characterization** is the process of determining the format-specific significant properties of an object of a given format, e.g.:  
  "I have an object of format *F*; what are its salient properties?"  
  The set of characteristics reported by JHOVE about a digital object is known as the object's *representation information*, a concept introduced by the Open Archival Information System (OAIS) reference model ([ISO/IEC 14721](/references#oais)).  
  The standard representation information reported by JHOVE includes: file pathname or URI, last modification date, byte size, format, format version, MIME type, format profiles, and optionally, CRC32, MD5, and SHA-1 checksums ([CRC32](/references#crc32), [MD5](/references#md5), [SHA-1](/references#sha1)). Additional media type-specific representation information is consistent with the [NISO Z39.87](/references#z39.87) Data Dictionary for digital still images and the draft AES metadata standard for digital audio.

Identification, validation, and characterization actions are frequently necessary during routine operation of digital repositories and for digital preservation activities. These actions are performed by *modules*. The output from JHOVE is controlled by *output handlers*. JHOVE uses an extensible plug-in architecture; it can be configured at the time of its invocation to include whatever specific format modules and output handlers that are desired.  
The initial release of JHOVE includes [modules](/modules/) for [arbitrary byte streams](/modules/bytestream), [ASCII](/modules/ascii) and [UTF-8](/modules/utf8) encoded text, [GIF](/modules/gif), [JPEG2000](/modules/jpeg2000), [JPEG](/modules/jpeg), and [TIFF](/modules/tiff) images, [AIFF](/modules/aiff) and [WAVE](/modules/wave) audio, [PDF](/modules/pdf), [HTML](/modules/html), and [XML](/modules/xml); and text and XML output handlers.

## Tutorial {#tutorial}

- [Getting started with JHOVE](/getting-started/) (2015-10-20)
- [Selecting an XML parser](/documentation/parser/) (2007-04-04)

## For JHOVE Developers {#for-developers}

- A guide to [building JHOVE from source](/documentation/build/).
- JHOVE [JavaDoc for all packages and classes](/javadoc/)
- A UML class [diagram](/img/api.gif)
- A guide to [writing a JHOVE Module](/documentation/dev-module/) (2005-02-07)
- A guide to [logging in JHOVE](/documentation/logging/)

## Schemas {#schemas}

- JHOVE output schema [jhove.xsd](http://hul.harvard.edu/ois/xml/xsd/jhove/jhove.xsd)
- JHOVE configuration file schema [jhoveConfig.xsd](http://hul.harvard.edu/ois/xml/xsd/jhove/jhoveConfig.xsd)

## Modules and Format Specifications {#specifications}

Standard JHOVE modules:

- The [AIFF-hul](/modules/aiff/) module (2005-05-09)
- The [ASCII-hul](/modules/ascii/) module (2004-03-03)
- The [BYTESTREAM](/modules/bytestream/) module (2004-03-03)
- The [GIF-hul](/modules/gif/) module (2005-05-09)
- The [HTML-hul](/modules/html/) module (2005-05-09)
- The [JPEG-hul](/modules/jpeg/) module (2005-05-26)
- The [JPEG2000-hul](/modules/jpeg2000/) module (2005-05-26)
- The [PDF-hul](/modules/pdf/) module (2008-02-25)
- The [TIFF-hul](/modules/tiff/) module (2005-05-09)
- The [UTF8-hul](/modules/utf8/) module (2005-05-09)
- The [WAVE-hul](/modules/wave/) module (2004-12-17)
- The [XML-hul](/modules/xml/) module (2005-05-09)

- [References](/references/) (2005-05-09)

{% include footer.html %}
