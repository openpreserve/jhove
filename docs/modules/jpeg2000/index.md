---
title: JPEG2000-hul Module
layout: page
---

# JPEG2000-hul Module

{: #introduction .section-heading}
## 1 Introduction

The JPEG2000-hul module recognizes and validates the JPEG 2000 (ISO/IEC 15444) format, a digital representation format for lossy or losslessly-compressed bi-tonal, grayscale, palette color, and continuous color still image data.

The module is invoked by the:

  > `jhove ... -m JPEG2000-hul ...`

command line option.

{: #coverage .section-heading}
## 2 Coverage

The JPEG2000-hul module recognizes and validates the following public profiles:

- JP2 profile (ISO/IEC 15444-1:2000/ITU-T Rec. T.800 (2002)) \[[JP2](/references#jpeg2000-1), [ITU-T T.800](/references#itu-t.800)\]
- JPX profile (ISO/IEC 15444-2:2004) \[[JPX](/references#jpeg2000-2)\]

{: #well-formedness .section-heading}
## 3 Well-Formedness

The following criteria must be met by a JPEG 2000 object for JHOVE to consider it well-formed:

- The required Signature and File Type box structures are the first two boxes in the file
- All boxes required by a given profile exist in the file
- All box structures are well-formed (a four byte unsigned integer Box Length, followed by a four byte unsigned integer Box type, followed by a eight byte unsigned integer Box Length, followed by the Box Contents)
- No data exist before the first byte of the first box or after the last byte of the last box

{: #validity .section-heading}
## 4 Validity

The following criteria must be met by a JPEG 2000 file for JHOVE to consider it valid:

- The file is well-formed

{: #repinfo .section-heading}
## 5 Representation Information

The MIME type for the JP2 profile is reported as: image/jp2; for the JPX profile: image/jpx.

In addition to the standard JHOVE [representation information](index.html#repinfo), the following JPEG 2000-specific properties are reported:

- Property "JPEG2000Metadata" of type PROPERTY and arity ARRAY
  - Properties capturing the technical attributes of the JPEG 2000 image from all boxes

Image technical properties are reported in terms of the NISO [Z39.87](/references#z39.87) data dictionary.

### 5.1 Profiles

- **JP2**

    JP2 is the file format defined by ISO/IEC 15444-1:2000/ITU-T Rec. T.800 (2002) \[[JP2](/references#jpeg2000-1), [ITU-T T.800](/references#itu-t.800)\]. The JP2 profile provides a baseline format for the exchange of still image data using a limited range of colorspaces.

    Profile requirements include:

      - The File Type box BR field is "jp2 " (note the trailing SPACE character)
      - JP2 Header superbox precedes any code stream or Label boxes and includes Image Header and Colour Specification boxes
      - The Colour Specification box APPROX field is 0 and the METH field is either 1 or 2

- **JPX**

    JPX is the file format defined by ISO/IEC 15444-2:2004 \[[JPX](/references#jpeg2000-2)\]. The JPX profile provides extensions to JP2 that allow additional colorspaces, the specification of opacity, standardized metadata, multiple image data streams, and more non-contiguous internal organization of the image data.

    Profile requirements include:

      - The File Type box BR field is "jpx " (note the trailing SPACE character)
      - Reader Requirements box immediately follows the File Type box
      - JP2 Header superbox includes Image Header and Colour Specification boxes
      - The Colour Specification box APPROX field is non-zero

{: #additional-module-properties .section-heading}
## 6 Additional Module Properties

- Nominal file extension: .jp2 (for JP2 profile) \[[RFC 3745](/references#rfc3745)\]
- Nominal file extension: .jpf (for JPX profile)
- Alternative file extension: .jpx (for JPX profile)
