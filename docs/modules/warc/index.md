---
title: WARC-kb Module
nav_title: WARC
nav_order: 11
section: modules
parent: Modules
summary: Support for WARC web archive files.
layout: page
---

# WARC-kb Module

{: #introduction .section-heading}
## 1 Introduction

The WARC-kb module recognizes and validates the WARC (Web ARChive) format. \[[WARC](/references#warciso)\]. It only validates the WARC file format and WARC headers, not the actual payload of the WARC records. This module uses the [JWAT](/references#jwat) library for WARC parsing. For Compressed WARC files the JWAT library is also used to parse compressed WARCs (.warc.gz)

The module is invoked by the:

 > ```bash
 > jhove ... -m WARC-kb ...
 > ```

command line option.

The WARC-kb module recognizes [ISO28500:2009](/references#warciso).

This module doesn't have configurable parameters.

{: #coverage .section-heading}
## 2 Coverage

The WARC-kb module recognizes and validates the following profiles:

- [ISO28500:2009](/references#warciso)

{: #well-formedness .section-heading}
## 3 Well-Formedness

The WARC module doesn't check the well-formedness

{: #validity .section-heading}
## 4 Validity

The WARC module only validates the WARC file format, WARC headers. It doesn't check the payload of the WARC records.

{: #repinfo .section-heading}
## 5 Representation Information

The MIME type is reported as: application/warc \[[application/warc, application/warc-fields](https://iipc.github.io/warc-specifications/specifications/warc-format/warc-1.1/#warc-file-name-size-and-compression)\].

In addition to the standard JHOVE [representation information](/documentation#repinfo), the following WARC-specific properties are reported:

- Property "WarcRecordProperties"
  - Property "Record offset" of type STRING
  - Property "Warc-Date" of type STRING
  - Property "Warc-Record-ID" of type STRING
  - Property "Record-ID-Scheme" of type STRING
  - Property "Content-Type" of type STRING
  - Property "Content-Length" of type STRING
  - Property "Warc-Type" of type STRING
  - Property "Warc-Block-Digest" of type STRING
  - Property "Block-Digest-Algorithm" of type STRING
  - Property "Block-Digest-Encoding" of type STRING
  - Property "isValidBlockDigest" of type STRING
  - Property "Warc-Payload-Digest" of type STRING
  - Property "Payload-Digest-Algorithm" of type STRING
  - Property "Payload-Digest-Encoding" of type STRING
  - Property "isValidPayloadDigest" of type STRING
  - Property "Warc-Truncated" of type STRING
  - Property "hasPayload" of type STRING
  - Property "PayloadLength" of type STRING
  - Property "Warc-Identified-Payload-Type" of type STRING
  - Property "Warc-Segment-Number" of type STRING
  - Property "isNonCompliant value" of type STRING
  - Property "Computed Block-Digest" of type STRING

{: #additional-module-properties .section-heading}
## 6 Additional Module Properties

- Nominal file extension: .warc, [.warc.gz](https://iipc.github.io/warc-specifications/specifications/warc-format/warc-1.1/#annex-c-informative-warc-file-size-and-name-recommendations)
