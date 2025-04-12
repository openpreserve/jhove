---
title: ASCII-hul Module
layout: page
---

# ASCII-hul Module

{: #introduction .section-heading}
## 1 Introduction

The ASCII-hul module recognizes and validates ASCII content streams.

The module is invoked by the:

 > ```bash
 > jhove ... -m ASCII-hul ...
 > ```

command line option.

This module can be [configured](/getting-started/config/) with the following parameters:

- `withTextMD=true` to ask for the output of a [textMD](/references#textmd) block in the text technical properties.

{: #coverage .section-heading}
## 2 Coverage

- ASCII (ANSI X3.4-1986, ECMA-6, ISO 646:1991)
  \[[ANSI X3.4](/references#x3.4),
  [ECMA-6](/references#ecma-6),
  [ISO 646](/references#iso646)\]

{: #well-formedness .section-heading}
## 3 Well-Formedness

The following criteria must be met by an ASCII content stream for JHOVE to consider it well-formed:

- The integer value of every octet in the stream is in the inclusive range [0x00, 0x7F].

{: #validity .section-heading}
## 4 Validity

The following criteria must be met by an ASCII content stream for JHOVE to consider it valid:

- The ASCII content stream is well-formed.

{: #repinfo .section-heading}
## 5 Representation Information

The MIME type is reported as: `text/plain; charset=US-ASCII`

In addition to the standard JHOVE [representation information](/documentation#repinfo), the module defines the following properties:

- Property "ASCIIMetadata" of type PROPERTY and arity LIST
  - Property "LineEndings" of type STRING and arity LIST containing: CR, CRLF, or LF
  - Property "ControlCharacters" of type STRING and arity LIST containing the control characters in readable form, e.g., "TAB (0x09)"
  - If withTextMD, Property "TextMDMetadata" of type TextMDMetadata and arity SCALAR
