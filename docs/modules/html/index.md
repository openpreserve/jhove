---
title: HTML-hul Module
nav_title: HTML
nav_order: 5
section: modules
parent: modules
summary: Support for HTML documents.
layout: page
---

# HTML-hul Module

{: #introduction .section-heading}
## 1 Introduction

The HTML-hul module recognizes and validates the HTML (Hypertext Markup Language) format. \[[HTML](/references#xhtml-1.0)\].

The module is invoked by the:

 > ```bash
 > jhove ... -m HTML-hul ...
 > ```

command line option.

The HTML-hul module recognizes XHTML 1.0 (including transitional, frameset and strict) and 1.1, making use of the XML-hul module. If the XML-hul module is not available, only limited information will be provided on XHTML documents.

This module can be [configured](/getting-started/config/) with the following parameters:

- `withTextMD=true` to ask for the output of a [textMD](/references#textmd) block in the text technical properties.

{: #coverage .section-heading}
## 2 Coverage

The HTML-hul module recognizes and validates the following public profiles:

- HTML 3.2, and 4.0 and 4.01 (Strict, Transitional, and Frameset) \[[HTML 3.2](/references#html-3.2), [HTML 4.0](/references#html-4.0), [HTML 4.01](/references#html-4.01)\]
- XHTML Basic and 1.0 and 1.1 (Strict, Transitional, and Frameset) \[[XHTML Basic](/references#xhtml-basic), [XHTML 1.0](/references#xhtml-1.0), [XHTML 1.1](/references#xhtml-1.1)\]

{: #well-formedness .section-heading}
## 3 Well-Formedness

For the HTML profiles JHOVE uses the criteria for HTML well-formedness defined by \[[HTML 3.2](/references#html-3.2), [HTML 4.0](/references#html-4.0), [HTML 4.01](/references#html-4.01)\]; for the XHTML profiles, JHOVE uses the criteria defined by \[[XML](/references#xml)\]. Specifically, a well-formed HTML document must have no syntactic errors, and must contain at least one of the tags HTML, HEAD, BODY or TITLE.

{: #validity .section-heading}
## 4 Validity

For the HTML profiles JHOVE uses the criteria for HTML validity defined by \[[HTML 3.2](/references#html-3.2), [HTML 4.0](/references#html-4.0), [HTML 4.01](/references#html-4.01)\]; for the XHTML profiles JHOVE uses the criteria defined by \[[XHTML 1.0](/references#xhtml-1.0), [XHTML 1.1](/references#xhtml-1.1)\].

{: #representation-information .section-heading}
## 5 Representation Information

The MIME type is reported as: text/html \[[RFC 2854](/references#rfc2854)\]

In addition to the standard JHOVE [representation information](/documentation#repinfo), the following HTML-specific properties are reported:

- Property "XMLMetadata" of type PROPERTY and arity LIST (for XHTML only; see the documentation of the [XML-hul](/modules/xml/) module for the contents of this property).
- Property "HTMLMetadata" of type PROPERTY and arity LIST
  - Property "PrimaryLanguage" of type STRING
  - Property "OtherLanguages" of type STRING and arity SET
  - Property "Title" of type STRING
  - Property "MetaTags" of type PROPERTY and arity LIST
    - Property "Name" of type STRING
    - Property "Httpequiv" of type STRING
    - Property "Content" of type STRING
  - Property "Frames" of type PROPERTY and arity LIST
    - Property "Name" of type STRING
    - Property "Title" of type STRING
    - Property "Longdesc" of type STRING
    - Property "Src" of type STRING
  - Property "Links" of type STRING and arity LIST
  - Property "Scripts" of type STRING and arity LIST
  - Property "Images" of type PROPERTY and arity LIST
    - Property "Alt" of type STRING
    - Property "Longdesc" of type STRING
    - Property "Src" of type STRING
    - Property "Height" of type STRING
    - Property "Width" of type STRING
  - Property "Citations" of type STRING and arity LIST
  - Property "DefinedTerms" of type STRING and arity LIST
  - Property "Abbreviations" of type PROPERTY and arity LIST
    - Property "Text" of type STRING
    - Property "Title" of type STRING
  - Property "Entities" of type STRING and arity LIST
  - Property "UnicodeEntityBlocks" of type STRING and arity LIST
  - If withTextMD, Property "TextMDMetadata" of type TextMDMetadata and arity SCALAR

{: #additional-module-properties .section-heading}
## 6 Additional Module Properties

- Nominal file extension: .html, .htm
- Macintosh OS file type: TEXT
