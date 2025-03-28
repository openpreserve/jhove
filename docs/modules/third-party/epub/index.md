---
title: EPUB-ptc Module
layout: page
---

# EPUB-ptc Module

{: #introduction .section-heading}
## 1 Introduction

The EPUB-ptc module recognizes and validates the EPUB format.

The module is invoked by the:

>     jhove ... -m EPUB-ptc ...

command line option.

The EPUB-ptc JHOVE module is a wrapper around the [official EPUBCheck tool](https://github.com/w3c/epubcheck). Visit the [EPUB Specifications and Projects page](http://www.idpf.org/epub/dir/) for more information on the EPUB format.

{: #coverage .section-heading}
## 2 Coverage

The EPUB-ptc module recognizes and validates the following public profiles:

-   EPUB 2 (up to 2.0.1) \[[EPUB 2.0.1](/references#epub2)\]
-   EPUB 3, (up to 3.2) \[[EPUB 3.2](/references#epub3)\]

Important note: The internal EPUB metadata only reveals its _major_ version - which is currently either EPUB2 or EPUB3. This is based on the "version" property on the "package" tag within the OPF XML. The EPUBCheck tool will validate the file against the specification of the most recent point release associated with that major version - and it is this number that will appear as the "version" in the JHOVE report. To clarify, all EPUB2s will validate as 2.0.1 and all EPUB3s as 3.2 until a new EPUB version is released along with an updated EPUBCheck tool. Additional metadata provided as properties in the report (detailed below) will indicate the presence of features that may be dependent on the e-book software's support for them.

{: #well-formedness .section-heading}
## 3 Well-Formedness

EPUBs are a form of ZIP archive file. Well formed status is based on evaluation of both the archive file as a whole and on the presence of specific files within it. The JHOVE report outputs the messages as provided by EPUBCheck - a full list of these can be seen [in the EPUBCheck code base](https://github.com/w3c/epubcheck/blob/v4.2.2/src/test/resources/com/adobe/epubcheck/test/command_line/listSeverities_expected_results.txt). In addition to this list, an undefined failure within the EPUBCheck module will report as a FATAL error - this happens if a non-EPUB file is passed into the module, for example. A message will cause a status of "Not Well Formed" if either:

1.  it has a severity level of FATAL
2.  it is a package-related message (starts with "PKG-") with a severity level of ERROR.

The following are some of the key criteria that must be met for an EPUB object to be considered Well Formed in the JHOVE report. For the full list, please refer to the EPUBCheck messages link provided above:

-   The EPUB must have a valid header. There must be "PK" at byte 0, "mimetype" at byte 30, and "application/epub+zip" at byte 38.
-   The EPUB and each of the files within it must not be corrupted.
-   The package must contain an OPF XML file.
-   The package must contain a META-INF/container.xml file.
-   All files referenced in the OPF must be present in the package and have valid file names

{: #validity .section-heading}
## 4 Validity

As with the "Well Formed" status, the criteria that determine "Validity" are based on the [messages output by the EPUBCheck module](https://github.com/w3c/epubcheck/blob/v4.2.2/src/test/resources/com/adobe/epubcheck/test/command_line/listSeverities_expected_results.txt). If the EPUB has a status of "Well Formed", but contains one or more message with a severity level of "ERROR" the EPUB is labelled as "Not Valid". As mentioned in the previous section, the exception is package ERRORs (PKG-\*), which will always result in a "Not Well Formed" assignment.

{: #repinfo .section-heading}
## 5 Representation Information

The MIME type is reported as: application/epub+zip

In addition to the standard JHOVE [representation information](index.html#repinfo), the following EPUB-specific properties are reported:

-   Property "EPUBMetadata" of type PROPERTY and arity LIST
    -   Property "PageCount" of type LONG and arity SCALAR
    -   Property "CharacterCount" of type LONG and arity SCALAR
    -   Property "Language" of type STRING and arity SCALAR
    -   Property "Info" of type PROPERTY and arity SET
        -   Property "Identifier" of type STRING and arity SCALAR
        -   Property "Title" of type STRING and arity ARRAY
        -   Property "Creator" of type STRING and arity ARRAY
        -   Property "Contributor" of type STRING and arity ARRAY
        -   Property "Date" of type LONG and arity SCALAR
        -   Property "Publisher" of type PROPERTY and arity SCALAR
        -   Property "Subject" of type STRING and arity ARRAY
        -   Property "Rights" of type PROPERTY and arity SCALAR
    -   Property "Fonts" of type PROPERTY and arity SET
        -   Property "Font" of type PROPERTY and arity SET
            -   Property "FontName" of type STRING and arity SCALAR
            -   Property "FontFile" of type STRING and arity SCALAR
    -   Property "MediaTypes" of type STRING and arity ARRAY
    -   Property "References" of type STRING and arity ARRAY
    -   Property "Resources" of type STRING and arity ARRAY
    -   Property "hasEncryption" of type BOOLEAN and arity SCALAR
    -   Property "hasSignatures" of type BOOLEAN and arity SCALAR
    -   Property "hasAudio" of type BOOLEAN and arity SCALAR
    -   Property "hasVideo" of type BOOLEAN and arity SCALAR
    -   Property "hasFixedLayout" of type BOOLEAN and arity SCALAR
    -   Property "hasScripts" of type BOOLEAN and arity SCALAR

### 5.1 Profiles

-   **EPUB 2.0.1**
    
    The presence of "version=2.0" in the package tag of the OPF XML will result in validation using the 2.0.1 specification. \[[EPUB2](/references#epub2)\].
    
-   **EPUB 3.2**
    
    The presence of "version=3.0" in package tag of the OPF XML will result in validation using the 3.2 specification. \[[EPUB3](/references#epub3)\].
    

{: #additional-module-properties .section-heading}
## 6 Additional Module Properties

-   Nominal file extension: .epub

{: #troubleshooting .section-heading}
## 7 Troubleshooting

The EPUB JHOVE module uses the EPUBCheck tool. This means it inherits a [problem caused by the thread stack size being too small](https://github.com/w3c/epubcheck/wiki/Running#javalangstackoverflowerror) to process the EPUB in certain situations. This will likely manifest as a "StackOverflowError" in the console. Using a 32-bit JVM instead of a 64-bit one can cause this error. The work around is to increase the thread stack size by adding "-Xss1024k" as a parameter on the java command. To do this, open the jhove\[.bat\] file and manually modify the java command as follows:

> `java -Xss1024k -classpath "%CP%" Jhove -c "%CONFIG%" %\*`

If using the JHOVE GUI, open the jhove-gui\[.bat\] file and modify the following line to fix this issue:

> `java -Xss1024k -classpath "%CP%" JhoveView -c "%CONFIG%" %\*`
