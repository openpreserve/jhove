---
title: GZIP-kb Module
nav_title: GZIP
nav_order: 5
section: modules
parent: Modules
summary: Support for GZIP compressed files.
layout: page
---

# GZip-kb Module

{: #introduction .section-heading}
## 1 Introduction

The GZIP-kb module recognizes and validates the Gzip (GNU zip) format. \[[GZip](/references#gzip)\].

The module is invoked by the:

 > ```bash
 > jhove ... -m GZIP-kb ...
 > ```

command line option.

The GZIP-kb module recognizes and validates [GZip version 4.3](/references#gzip). It also supports multiple member GZip files. This module uses the [JWAT](/references#jwat) library for GZip parsing.

This module doesn't have configurable parameters.

{: #coverage .section-heading}
## 2 Coverage

The GZIP-kb module recognizes and validates the following public profiles:

- [RFC 1952](/references#gzip)

{: #well-formedness .section-heading}
## 3 Well-Formedness

The GZip module checks well-formedness.

{: #validity .section-heading}
## 4 Validity

The following criteria must be met by a GZip file for JHOVE to consider it valid:

- The file is well-formed. In the GZIP-kb module, well-formedness and validity are equivalent.

{: #repinfo .section-heading}
## 5 Representation Information

The MIME type is reported as: application/gzip \[[RFC 6713](/references#rfc6713)\]. Application/x-gzip is also supported.

In addition to the standard JHOVE [representation information](/documentation#repinfo), the following GZip-specific properties are reported:

- **Property "GzipEntryProperties"**
  - **Property "Is non compliant"** of type `STRING`
  - **Property "Offset value"** of type `STRING`
  - **Property "GZip entry name"** of type `STRING`
  - **Property "GZip entry comment"** of type `STRING`
  - **Property "GZip entry date"** of type `STRING`
  - **Property "GZip entry compression method"** of type `STRING`
  - **Property "GZip entry operating system"** of type `STRING`
  - **Property "GZip entry header crc16"** of type `STRING`
  - **Property "GZip entry crc32"** of type `STRING`
  - **Property "GZip entry extracted size (ISIZE)"** of type `STRING`
  - **Property "GZip entry (computed) uncompressed size, in bytes"** of type `STRING`
  - **Property "GZip entry (computed) compressed size, in bytes"** of type `STRING`
  - **Property "GZip entry (computed) compression ratio"** of type `STRING`

{: #additional-module-properties .section-heading}
## 6 Additional Module Properties

- **Nominal file extension**: `.gz`
