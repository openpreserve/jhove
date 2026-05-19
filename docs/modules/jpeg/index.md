---
title: JPEG-hul Module
nav_title: JPEG
nav_order: 6
section: modules
parent: modules
summary: Support for JPEG image files.
layout: page
---

# JPEG-hul Module

{: #introduction .section-heading}
## 1 Introduction

The JPEG-hul module recognizes and validates the JPEG format.

The module is invoked by the:

 > ```bash
 > jhove ... -m JPEG-hul ...
 > ```

command line option.

Formally, JPEG refers to a format for compressed images created by the Joint Photographic Experts Group and codified in ISO/IEC 10918-1 \[[JPEG](/references#jpeg-1)\]. In 1992 C-Cube Microsystems published a specification for an interchange file format called JFIF (JPEG File Interchange Format) that encapsulates the JPEG data stream \[[JFIF](/references#jfif)\]. What most people refer to as "JPEG" files are more properly JFIF files. However, many existing "JPEG" files do in fact contain only the "raw" JPEG data stream.

{: #coverage .section-heading}
## 2 Coverage

The JPEG-hul module recognizes and validates the following public profiles:

- JPEG (ISO/IEC 10918-1:1994) \[[JPEG](/references#jpeg-1)\]
- JFIF 1.02 (JPEG File Interchange Format) \[[JFIF](/references#jfif)\]
- Exif 2.0, 2.1 (JEIDA-49-1998) 2.1, and 2.2 (JEITA CP-3451) \[[Exif 2.1](/references#exif2.1), [Exif 2.2](/references#exif),\]
- SPIFF (ISO/IEC 10918-3:1997) \[[SPIFF](/references#jpeg-3)\]
- JTIP (ISO/IEC 10918-3:1997) \[[JTIP](/references#jpeg-3)\]
- JPEG-LS (ISO/IEC 14495) \[[JPEG](/references#jpeg-ls-1)\]

{: #well-formedness .section-heading}
## 3 Well-Formedness

The following criteria must be met by a JPEG object for JHOVE to consider it well-formed:

- The first three bytes of the file are 0xFF, 0xD8, 0xFF
- The file consists of one or more correctly formatted segments. Segment markers 0xC0 through 0xFE are recognized.
- Data streams following RSTn and SOS markers are correctly terminated.

JHOVE uses the JPEG-LS algorithm for recognizing the end of a data stream, looking for a byte of 0xFF followed by a byte in the range 0X80 through 0XFE. The standard JPEG algorithm looks for a byte of 0XFF followed by any byte which is not 0 or 0xFF. Since all valid markers in existing versions of JPEG are in the range 0X80 through 0XFE, this algorithm works correctly for both JPEG and JPEG-LS.

{: #validity .section-heading}
## 4 Validity

The following criteria must be met by a JPEG file for JHOVE to consider it valid:

- The file is well-formed
- The file contains one of the following segments as the first segment of the file, not counting comments:
  - APP0 (0xE0) with identifier 0x4A, 0x46, 0x49, 0x46, 0x00, indicating a JFIF or JTIP file.
  - APP1 (0xE1) with identifier 0x45, 0x78, 0x69, 0x66, 0x00, 0x00, indicating an Exif file.
  - APP8 (0xE8) with identifier 0x53, 0x50, 0x49, 0x46, 0x46, 0x00, indicating a SPIFF file.
  - JPG7 (0xF7), also known as SOF55, indicating a JPEG-LS file.
- The file does not contain a D8 segment marker except at the beginning of the file.
- Any DTT (0xF2) segment is preceded by a DTI (0xF1) segment.
- All tiling types from DTI segments have a value of 0, 1, or 2.

A file which consists of a JPEG data stream, but does not contain the required segments for a JFIF, SPIFF, Exif, JTIP, or JPEG-LS file is considered well-formed but not valid.

{: #repinfo .section-heading}
## 5 Representation Information

The MIME type is reported as: image/jpeg

In addition to the standard JHOVE [representation information](index.html#repinfo), the following JPEG-specific properties are reported:

- Property "JPEGMetadata" of type PROPERTY and arity ARRAY
  - Property "CompressionType" of type STRING and arity SCALAR
  - Property "Images" of type PROPERTY and arity LIST
    - Property "Image" of type PROPERTY and arity LIST
      - Property "Tiling" of type PROPERTY and arity ARRAY
        - Property "TilingType" of type STRING (if raw output off) or INTEGER (if raw output on) and arity SCALAR
        - Property "VerticalScale" of type INTEGER and arity SCALAR
        - Property "HorizontalScale" of type INTEGER and arity SCALAR
        - Property "RefGridHeight" of type LONG and arity SCALAR
        - Property "RefGridWidth" of type LONG and arity SCALAR
        - Property "Tiles" of type PROPERTY and arity LIST
          - Property "Tile" of type PROPERTY and arity ARRAY
            - Property "VerticalScale" of type LONG and arity SCALAR
            - Property "HorizontalScale" of type LONG and arity SCALAR
            - Property "VerticalOffset" of type LONG and arity SCALAR
            - Property "HorizontalOffset" of type LONG and arity SCALAR
      - Property "RestartInterval" of type INTEGER and arity SCALAR
      - Property "Scans" of type INTEGER and arity SCALAR
      - Property "QuantizationTables" of type PROPERTY and arity LIST
        - Property "QuantizationTable" of type PROPERTY and arity ARRAY
          - Property "Precision" of type INTEGER (if raw output) or STRING (if not raw output) and arity SCALAR
          - Property "DestinationIdentifier" of type INTEGER and arity SCALAR
      - Property "ArithmeticConditioning" of type PROPERTY and arity LIST
        - Property "TableClass" of type INTEGER (if raw output) or STRING (if not raw output) and arity SCALAR
        - Property "DestinationIdentifier" of type INTEGER and arity SCALAR
      - Property "SelectivelyRefinedScan" of type PROPERTY and arity ARRAY
        - Property "VerticalOffset" of type INTEGER and arity SCALAR
        - Property "HorizontalOffset" of type INTEGER and arity SCALAR
        - Property "VerticalSize" of type INTEGER and arity SCALAR
        - Property "HorizontalSize" of type INTEGER and arity SCALAR
      - Property "Exif" of type PROPERTY and arity LIST
        - Property "IFD" of type PROPERTY and arity ARRAY
          - Property "Offset" of type LONG and arity SCALAR
          - Property "Type" of type STRING and arity SCALAR
          - Property "Entries" of type PROPERTY and arity LIST

            The contents of the "Entries" list are TIFF IFD properties. The [TIFF-HUL](/modules/tiff) module must be present for Exif information to be extracted from a JPEG file.

      - Property "XMP" or type STRING and arity SCALAR
      - Property "SelectivelyRefinedScans" of type PROPERTY and arity LIST
    - Property "ThumbImage" of type PROPERTY and arity LIST
      - Property "NisoImageMetadata" of type NISOIMAGEMETADATA and arity SCALAR
  - Property "Comments" of type STRING and arity LIST
  - Property "Extensions" of type STRING and arity LIST
  - Property "ApplicationSegments" of type STRING and arity LIST
  - Property "ExpansionSegments" of type PROPERTY and arity LIST
    - Property "Expansion" of type PROPERTY and arity ARRAY
      - Property "Horizontal" of type BOOLEAN and arity SCALAR
      - Property "Vertical" of type BOOLEAN and arity SCALAR

Image technical properties are reported in terms of the NISO [Z39.87](/references#z39.87) data dictionary.

{: #profiles .section-heading}
### 5.1 Profiles

- **JPEG**

    ISO/IEC 10918-1 is a lossy image compression format \[[JPEG](/references#jpeg-1)\].

- **JFIF 1.2**

    JFIF is the JPEG File Interchange Format \[[JFIF](/references#jfif)\]. The JFIF profile is indicated by the presence of an APP0 marker segment with the identifier "JFXX" immediately following the SOI marker.

- **Exif**

    Exif 2.0, 2.1 (JEIDA-49-1998), and 2.2 (JEITA CP-3451) define camera-specific metadata \[[Exif 2.1](/references#exif2.1), [Exif 2.2](/references#exif)\]. The following tags are mandatory in the primary TIFF IFD:

    > | Tag Name and Number |     | Value | Note |
    | --- | --- | --- | --- |
    | ImageWidth | 256 |     | If Compression (259) = 1 |
    | ImageLength | 257 |     | If Compression (259) = 1 |
    | BitsPerSample | 258 | 8,8,8 | If Compression (259) = 1 |
    | Compression | 259 | 1   | If primary TIFF IFD _and_ JPEGInterchangeFormat (513) not defined |
    | 1 or 6 | If thumbnail TIFF IFD |
    | PhotometricInterpretation | 262 | 2 or 6 | If Compression (259) = 1 |
    | StripOffsets | 273 |     | If Compression (259) = 1 |
    | SamplesPerPixel | 277 | 3   | If Compression (259) = 1 |
    | RowsPerStrip | 278 |     | If Compression (259) = 1 |
    | StripByteCounts | 279 |     | If Compression (259) = 1 |
    | XResolution | 282 |     |
    | YResolution | 283 |     |
    | ResolutionUnit | 296 | 2 or 3 |
    | ExifIFD | 34665 |     | If primary TIFF IFD |

    The primary TIFF IFD is the first IFD in the file, whose offset is defined in the TIFF header. The optional thumbnail TIFF IFD is a subsequent IFD whose offset is defined following the primary TIFF IFD.

    The following tags are mandatory in the Exif IFD:

    <!-- markdownlint-disable -->

    > | Tag Name and Number | | Value |
    | --- | --- | --- |
    | ExifVersion | 36864 | "0220" (Version 2.2)  <br>"0210" (Version 2.1)  <br>"0200" (Version 2.0) |
    | FlashpixVersion | 40960 | "0100" |
    | ColorSpace | 40961 | 1 or 65535 |

    <!-- markdownlint-enable -->

- **SPIFF**

    SPIFF (ISO/IEC 10918-3) is the Still Picture Interchange File Format \[[SPIFF](/references#jpeg-3)\].

- **JTIP**

    JTIP (ISO/IEC 10918-3) is the JPEG Tiled Image Pyramid format \[[JTIP](/references#jpeg-3)\].

- **JPEG-LS**

    JPEG-LS (ISO/IEC 14495) is a standard for lossless and near-lossless compression of images \[[JPEG-LS](/references#jpeg-ls-1)\].

{: #additional-module-properties .section-heading}
## 6 Additional Module Properties

- Nominal file extension: .jpg
- Nominal file extension: .jls (for JPEG-LS)
- Nominal file extension: .spf (for SPIFF)
