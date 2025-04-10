---
title: Getting started
layout: page
---

# Getting Started with JHOVE

{: #introduction .section-heading}
## Introduction

JHOVE (pronounced "jove"), the JSTOR/Harvard Object Validation Environment, is an extensible software framework for performing format identification, validation, and characterization of digital objects.

- Format _identification_ is the process of determining the format to which a digital object conforms; in other words, it answers the question: "I have a digital object; what format is it?"
- Format _validation_ is the process of determining the level of compliance of a digital object to the specification for its purported format, e.g.: "I have an object purportedly of format _F_; is it?"

    Format validation conformance is determined at three levels: _well-formedness_, _validity_, and _consistency_.

    1. A digital object is _well-formed_ if it meets the purely syntactic requirements for its format
    2. An object is _valid_ if it is well-formed and it meets the higher-level semantic requirements for format validity
    3. An object is _consistent_ if it is valid and its internally extracted representation information is consistent with externally supplied representation information

    For example, a [TIFF](/references#tiff6) object is well-formed if it starts with an 8 byte header followed by a sequence of Image File Directories (IFDs), each composed of a 2 byte entry count and a series of 8 byte tagged entries. The object is valid if it meets certain additional semantic-level rules, such as that an RGB file must have at least three sample values per pixel. The object is consistent with external [NISO Z39.87](/references#z39.87) metadata if that metadata is consistent with the representation information of the object that is extracted by JHOVE.

    The concept of distinguishing between well-formedness (syntactic correctness) and validity (semantic correctness) was taken from [XML](/references#xml).

- Format _characterization_ is the process of determining the format-specific significant properties of an object of a given format, e.g.: "I have an object of format _F_; what are its salient properties?"

    The set of characteristics reported by JHOVE about a digital object is known as the object's _representation information_, a concept introduced by the Open Archival Information System (OAIS) reference model \[[ISO/IEC 14721](/references#oais)\]. The standard representation information reported by JHOVE includes: file pathname or URI, last modification date, byte size, format, format version, MIME type, format profiles, and optionally, CRC32, MD5, and SHA-1 digests \[[CRC32](/references#crc32), [MD5](/references#md5), [SHA-1](/references#sha1)\].

Identification, validation, and characterization actions are frequently necessary during routine operation of digital repositories and for digital preservation activities.

The output from JHOVE is controlled by _output handlers_. JHOVE uses an extensible plug-in architecture; it can be configured at the time of its invocation to include whatever specific format modules and output handlers that are desired. The initial release of JHOVE includes modules for [arbitrary byte streams](/modules/bytestream/), [ASCII](/modules/ascii/) and [UTF-8](/modules/utf8/) encoded text, [TIFF](/modules/tiff/), [HTML](/modules/html/), [XML](/modules/xml/), [JPEG](/modules/jpeg/), [JPEG2000](/modules/jpeg2000/), [PDF](/modules/pdf/), [AIFF](/modules/aiff/), [WAVE](/modules/wave/) audio; and [text](#text) and [XML](#xml) output handlers.

{: #getting-jhove .section-heading}
## Getting JHOVE

JHOVE is written in [Java](http://java.sun.com/j2se/). A J2SE 1.5-compliant Java Runtime Environment (JRE) is required for proper operation of JHOVE. JHOVE should be usable on any Unix, Windows, or OS X platform with the appropriate Java installation.

### Downloading JHOVE

There's now a beta of the new JHOVE installer, the latest version can be [downloaded](http://software.openpreservation.org/rel/jhove-latest.jar) from the OPF's Jenkins server.

### JHOVE for developers

If you'd like to get JHOVE via Maven or build the project from source please read our [build guide](/documentation/build/).

{: #installing-jhove .section-heading}
## Installing JHOVE

Download the [latest JHOVE installer](http://software.openpreservation.org/rel/jhove-latest.jar) , this requires Java 1.6 or later to be pre-installed. We'll assume that you've downloaded

      /Downloads/jhove-latest.jar
    

Installation is OS dependant.

### Windows

Simply double click the downloaded installer JAR in Explorer. If Java is installed then the windowed installer will guide you through selection. It's best to stay with the default choices if installing the beta.

Once the installation is finished you'll be able to double click

      C:\Users\yourName\jhove\jhove-gui
    

to start the JHOVE GUI. Alternatively start a command window, e.g.

    winkey

then type

    cmd

then issue these commands:

      C:\Users\yourName>cd jhove
      C:\Users\yourName\jhove>jhove
    

to display the command line usage message.

### Mac OS

Simply double click the downloaded installer JAR in Explorer. If Java is installed then the windowed installer will guide you through selection. It's best to stay with the default choices if installing the beta.

Once the installation is finished you'll be able to double click

      /Users/yourName/jhove/jhove-gui
    
to start the JHOVE GUI. Alternatively start a command window

    Terminal

and then issue these commands:

    cd ~/jhove
    ./jhove
    
to display the command line usage message.

### Linux

_Currently only tested on Ubuntu 14.10_ Once the installer is downloaded start a terminal, e.g.

    ctrl+alt+T

and type the following, assuming the download is in:

    ~/Downloads
    java -jar ~/Downloads/jhove-latest.jar
    

Once the installation is finished you'll be able to:

    cd ~/jhove
    ./jhove
    

to run the command line application and show the usage message. Alternatively:

    cd ~/jhove
    ./jhove-gui
    

will run the GUI application.

{: #running-jhove .section-heading}
## Running JHOVE

There should be no issues configuring the current JHOVE beta but just in case we've retained the [old configuration guide](/getting-started/config/).

{: #invocation .section-heading}
### Usage

    java Jhove \[-c config\] \[-m module\] \[-h handler\] \[-e encoding\] \[-H handler\]
                \[-o output\] \[-x saxclass\] \[-t tempdir\] \[-b bufsize\]
                \[-l loglevel\] \[\[-krs\] dir-file-or-uri \[...\]\]

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

All named modules and output handlers must be found on the Java CLASSPATH at the time of invocation. The JHOVE driver script, jhove/jhove, automatically sets the CLASSPATH and invokes the Jhove main class:

    jhove \[-c config\] \[-m module\] \[-h handler\] \[-e encoding\] \[-H handler\]
          \[-o output\] \[-x saxclass\] \[-t tempdir\] \[-b bufsize\] \[-l loglevel\]
          \[\[-krs\] dir-file-or-uri \[...\]\]

The following additional programs are available, primarily for testing and debugging purposes. They display a minimally processed, human-readable version of the contents of AIFF, GIF, JPEG, JPEG 2000, PDF, TIFF, and WAVE files:

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

The JHOVE Swing-based GUI interface can be invoked from a command shell from the jhove/bin sub-directory:

    java -jar JhoveView.jar -c 

where is the pathname of the JHOVE configuration file.

There are some [usage examples](/getting-started/examples/) to help you get started.

### Format Identification

The following syntax is used to discover, or identify, the format of a digital object.

    jhove ... \[-ks\] _file-or-uri1 .. file-or-uriN_

where the first ellipsis ... is a placeholder for any of the optional standard options defined [above](#invocation).

The digital object(s) can be specified as a file or directory pathname or as a URI. If a directory is specified, JHOVE will recursively walk through the directory. The optional \-s flag specified that the identification should be performed solely on the basis of the internal signatures (e.g., magic numbers) associated with the formats, rather than by a complete parsing of the object. After the object's format has been identified, its representation information is [displayed](examples#identification). The optional \-k flag specifies that object checksum values should be calculated and displayed as part of the representation information.

If the file or URI contains spaces, then it must be enclosed in quotation marks, e.g.,

    jhove ... "name with spaces"

If running in a Unix/Linux shell, the quotation marks must be escaped with backslashes, e.g.,

    jhove ... \"name with spaces\"

This is clunky but unavoidable, because of the way Java processes command line input. Backslash-quoting the spaces doesn't work. The backslashes should be omitted with the Windows command line.

### Format Validation/Characterization

The following syntax is used to determine the validity of a digital object with respect to a particular format, and to display format-specific representation information.

  jhove ... -m _module_ \[-kr\] _file-or-uri_

where the ellipsis ... is a placeholder for any of the optional standard options defined [above](#invocation).

Many formats use numeric flags to specify format properties. By default, JHOVE will translate these numeric values into descriptive strings. For example, the TIFF compression value 2 corresponds to "CCITT Group 3 RLE". The optional \-r flag specifies that the "raw" data values should be displayed, not the text labels. The optional \-k flag specifies that object checksum values should be calculated and displayed as part of the representation information.

The class file implementing the named module must be found on the Java CLASSPATH at the time of invocation. Note that JHOVE recognizes module names in a case-insensitive manner: "ASCII-hul" and "ascii-hul" both specify the standard [ASCII](/modules/ascii/) module.

### JHOVE Descriptive Information

The following syntax options display descriptive information about various components of JHOVE.

  jhove ...
  jhove ... -m _module_
  jhove ... -H _output-handler_

where the ellipsis ... is a placeholder for any of the optional standard options defined [above](#invocation).

The first invocation option will display descriptive information about [JHOVE](examples#app) itself, including a list of all loaded modules and output handlers. The second option will display descriptive information about the named [module](examples#module). The third option will display descriptive information about the named [output handler](examples#handler).

The class file implementing the named module or output handler must be found on the Java CLASSPATH at the time of invocation. Note that JHOVE recognizes modules and output handler names in a case-insensitive manner: "ASCII-hul" and "ascii-hul" both specify the standard [ASCII](/modules/ascii/) module.

{: #jhove-gui .section-heading}
## JHOVE GUI Interface

The JHOVE [Swing](http://java.sun.com/products/jfc/#swing)\-based GUI interface is invoked from a command shell:

  java -jar bin/JhoveView.jar

or by the appropriate mouse click behavior defined by the windowing system.

![JHOVE GUI screenshot](/img/jhovegui.png)

The menu options are:

|     |     |     |     |
| --- | --- | --- | --- |
| **File** |     |     |     |
|     | **Open file...** |     | Select file (equivalent to command line option: jhove ... _file-or-uri_) |
|     | **Open URL...** |     | Select URI (jhove ... _file-or-uri_) |
|     | **Close all document windows** |     | Close all open document windows |
|     | **Exit** |     | Terminate JHOVE |
| **Edit** |     |     |     |
|     | **Select** | **module** | Select JHOVE module |
|     |     | **(Any)** |     |
|     |     | **AIFF-hul** | Select AIFF module (jhove ... -m aiff-hul ...) |
|     |     | **ASCII-hul** | Select ASCII module (jhove ... -m ascii-hul ...) |
|     |     | **BYTESTREAM** | Select BYTESTREAM module (jhove ... -m bytestream ...) |
|     |     | **GIF-hul** | Select GIF module (jhove ... -m gif-hul ...) |
|     |     | **HTML-hul** | Select HTML module (jhove ... -m html-hul ...) |
|     |     | **JPEG-hul** | Select JPEG module (jhove ... -m jpeg-hul ...) |
|     |     | **JPEG2000-hul** | Select JPEG 2000 module (jhove ... -m jpeg2000-hul ...) |
|     |     | **PDF-hul** | Select PDF module (jhove ... -m pdf-hul ...) |
|     |     | **TIFF-hul** | Select TIFF module (jhove ... -m tiff-hul ...) |
|     |     | **UTF8-hul** | Select UTF-8 module (jhove ... -m utf8-hul ...) |
|     |     | **WAVE-hul** | Select WAVE module (jhove ... -m wave-hul ...) |
|     |     | **XML-hul** | Select XML module (jhove ... -m xml-hul ...) |
|     | **Edit configuration...** |     | Edit configuration file |
|     | **Preferences...** |     | Set preferences (jhove ... \[-kr\] ...) |
| **Help** |     |     |     |
|     | **About module...** |     | Display module descriptive information (jhove ... -m _module_) |
|     | **About Jhove...** |     | Display JHOVE descriptive information (jhove) |

{: #modules .section-heading}
## Standard Modules

The initial JHOVE distribution includes the following standard modules.

- [AIFF](/modules/aiff/)
- [ASCII](/modules/ascii/)
- [BYTESTREAM](/modules/bytestream/)
- [GIF](/modules/gif/)
- [HTML](/modules/html/)
- [JPEG](/modules/jpeg/)
- [JPEG 2000](/modules/jpeg2000/)
- [PDF](/modules/pdf/)
- [TIFF](/modules/tiff/)
- [UTF-8](/modules/utf8/)
- [WAVE](/modules/wave/)
- [XML](/modules/xml/)

{: #standard-output-handlers .section-heading}
## Standard Output Handlers

The initial JHOVE distribution includes the following standard output handlers.

{: #text .section-heading}
### TEXT Output Handler

The Text handler is the default output handler; if no other handler is explicitly specified, the Text handler is used.

```bash
jhove ...
jhove ... -h text ...
```

(Recall that JHOVE output handlers can be specified in a case-insensitive manner.)

Regardless of the [configuration](config/) options, JHOVE always statically loads the Text output handler.

{: #xml .section-heading}
### XML Output Handler

The XML handler output is defined by the JHOVE schema <[http://hul.harvard.edu/ois/xml/xsd/jhove/jhove.xsd](http://hul.harvard.edu/ois/xml/xsd/jhove/jhove.xsd)\>.

jhove ... -h xml ...

(Recall that JHOVE output handlers can be specified in a case-insensitive manner.)

The XML handler formats raster still image representation information according to the MIX schema \[[MIX](/references#mix)\] for the NISO image metadata \[[NISO Z39.87](/references#z39.87)\]. **Note**: Contrary to the NISO image metadata data dictionary, JHOVE defines XSamplingFrequency and YSamplingFrequency as rational values, not positive integers. This is necessary for images whose image length or width is not an integral ratio of the image source X or Y dimension.

Audio representation information is formatted according to the proposed AES-X098B, _Core audio metadata XML definition_, currently under development by the [Audio Engineering Society](http://www.aes.org/) (AES) [SC-03-06](http://www.aes.org/standards/b_policies/aessc-structure.cfm#SC-03) Working Group on Digital Library and Archive Systems.

Regardless of the [configuration](/getting-started/config/) options, JHOVE always statically loads the XML output handler.

{: #audit .section-heading}
### Audit Output Handler

The Audit handler should be invoked against a directory (or directories) without specifying a module. The handler produces an XML-formatted summary of all of the files in the directory, e.g.:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jhove xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns="http://hul.harvard.edu/ois/xml/ns/jhove"
          xsi:schemaLocation="http://hul.harvard.edu/ois/xml/ns/jhove
                              http://hul.harvard.edu/ois/xml/xsd/jhove/1.6/jhove.xsd"
          name="Jhove" release="1.18.1" date="2017-11-30">
 <date>2018-03-02T09:37:11-05:00</date>
 <audit home="/home/user">
  <file mime="application/pdf" status="valid">jhove/examples/pdf/AA\_Banner-single.pdf</file>
  <file mime="text/plain; charset=US-ASCII" status="valid">jhove/examples/pdf/AA\_Banner.pdf</file>
  <file mime="text/plain; charset=US-ASCII" status="valid">jhove/examples/pdf/README</file>
  <file mime="application/pdf" status="valid">jhove/examples/pdf/bedfordcompressed.pdf</file>
  <file mime="application/pdf" status="valid">jhove/examples/pdf/fallforum03.pdf</file>
  <file mime="application/pdf" status="valid">jhove/examples/pdf/imd.pdf</file>
  <file mime="application/pdf" status="well-formed">jhove/examples/pdf/ddap/DDAP\_Singlev3.pdf</file>
  <file mime="application/pdf" status="well-formed">jhove/examples/pdf/ddap/DDAP\_Spreadv3.pdf</file>
  <file mime="text/plain; charset=US-ASCII" status="valid">jhove/examples/pdf/ddap/README</file>
 </audit>
</jhove>
<!-- Summary by MIME type:
application/pdf: 6 (4,2)
text/plain; charset=US-ASCII: 3 (3,0)
Total: 9 (7,2)
-->
<!-- Summary by directory:
jhove/examples/pdf: 6 (6,0) + 0,0
jhove/examples/pdf/ddap: 3 (1,2) + 0,0
Total: 9 (7,2) + 0,0
-->
<!-- Elapsed time: 0:00:05 -->
```

The numbers in the MIME type summary are to be read as follows:

```text
number of files (number of valid files, number of well-formed files)
```

So in the example there are 6 PDF files, 4 of them valid and 2 well-formed, and 3 plain text files, all of them valid. This sums up to a total of 9 files, 7 valid and 2 well-formed. The numbers in the directory summary are to be read as follows:

```text
number of files (number of valid files, number of well-formed files) + number of
files that were not processed, number of files that were not found
```

It is intended that the Audit handler will form the basis for other, more interesting handlers but it can also be used for a quick overview of MIME types and validity status.

```text
jhove ... -h audit ...
```

(Recall that JHOVE output handlers can be specified in a case-insensitive manner.)

Regardless of the [configuration](config/) options, JHOVE always statically loads the Audit output handler.

{: #logging-support .section-heading}
## Logging support

As an aid to debugging third-party modifications, JHOVE supports the Java logging (java.util.logging) API. As delivered, each instance of `JhoveBase` creates a logger named "edu.harvard.hul.ois.jhove", and any module which invokes the ModuleBase constructor creates a logger named "edu.harvard.hul.ois.jhove.module". The logging level can be set either with the logLevel element of the configuration file or with the -l parameter in the command line. Permissible logging levels are OFF, SEVERE, WARNING, INFO, CONFIG, FINE, FINER FINEST, and ALL. The default logging level is SEVERE. See the [Sun logging overview](https://docs.oracle.com/javase/6/docs/technotes/guides/logging/overview.html) for more information on logging.

{: #license .section-heading}
## License

JHOVE is made available under the [GNU](http://www.gnu.org/) [Lesser General Public License](http://www.gnu.org/licenses/licenses.html#LGPL) (LGPL).

{: #acknowledgements .section-heading}
## Acknowledgements

Development of JHOVE was funded in part by the [Andrew W. Mellon Foundation](http://www.mellon.org) through a grant to [JSTOR](http://www.jstor.org).
