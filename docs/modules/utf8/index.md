---
title: UTF8-hul Module
nav_title: UTF-8
nav_order: 10
section: modules
parent: Modules
summary: Support for UTF-8 encoded text.
layout: page
---

# UTF8-hul Module

{: #introduction .section-heading}
## 1 Introduction

The UTF8-hul module recognizes and validates content streams encoded with the Unicode UTF-8 encoding.

The module is invoked by the:

 > ```bash
 > jhove ... -m UTF8-hul ...
 > ```

command line option.

This module can be [configured](/getting-started/config/) with the following parameters:

- withTextMD=true to ask for the output of a [textMD](/references#textmd) block in the text technical properties.

{: #coverage .section-heading}
## Coverage

- UTF-8 encoded content streams \[[Unicode](/references#unicode)\]

{: #well-formedness .section-heading}
## Well-Formedness

The following criteria must be met by an UTF8 content streams for JHOVE to consider it well-formed:

- The stream consists of an optional three-octet encoded Byte Order Mark (BOM) character, 0xEFBBBF, followed by an arbitrary number of the following one- to four-octet sequences:

  |     |     |
  | --- | --- |
  | Single octet: | 0_xxxxxxx_ |
  | Two octets: | 110_yyyyy_ 10_xxxxxx_ |
  | Three octets: | 1110_zzzz_ 10_yyyyyy_ 10_yyyyyy_ |
  | Four octets: | 11110_uuu_ 10_uuzzzz_ 10_yyyyyy_ 10_xxxxxx_ |

- The presence of an initial Byte Order Mark (BOM) character in the form of any of the following two- or four-octet sequences automatically taints the content stream as non-well-formed UTF-8:
<!-- markdownlint-disable -->
  <table>
  <tbody><tr><td rowspan="2">Two octets:</td><td><tt>0xEF&nbsp;0xFF</tt></td>
  <td>UTF-16 big-endian encoding</td></tr>
  <tr><td><tt>0xFFFE</tt></td>
  <td>UTF-16 little-endian encoding</td></tr>
  <tr><td rowspan="2">Four octets:</td>
  <td><tt>0x0000FEFF</tt></td>
  <td>UCS-4 big-endian encoding</td></tr>
  <tr><td><tt>0xFFFE0000</tt></td>
  <td>UCS-4 little-endian encoding</td></tr>
  </tbody></table>
  <!-- markdownlint-enable -->

  <!-- |     |     |     |
  | --- | --- | --- |
  | Two octets: | 0xEF 0xFF | UTF-16 big-endian encoding |
  | 0xFFFE | UTF-16 little-endian encoding | |
  | Four octets: | 0x0000FEFF | UCS-4 big-endian encoding |
  | 0xFFFE0000 | UCS-4 little-endian encoding | | -->

{: #validity .section-heading}
## Validity

The following criteria must be met by an UTF-8 encoded file for JHOVE to consider it valid:

- The UTF-8 encoded file is well-formed

{: #repinfo .section-heading}
## Representation Information

The MIME type is reported as: text/plain; charset=UTF-8

In addition to the standard JHOVE [representation information](/documentation#repinfo), the module defines the following properties:

- Property "UTF8Metadata" of type PROPERTY and arity LIST
  - Property "Characters" of type LONG and arity SCALAR containing the number of characters
  - Property "UnicodeBlocks" of type STRING and arity LIST containing Unicode 6.0.0 code blocks \[[Unicode Code Blocks](/references#blocks)\]
  - Property "LineEndings" of type STRING and arity LIST containing: CR, CRLF, or LF
  - If withTextMD, Property "TextMDMetadata" of type TextMDMetadata and arity SCALAR
