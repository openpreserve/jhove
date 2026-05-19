---
title: TIFF-hul Module
nav_title: TIFF
nav_order: 9
section: modules
parent: modules
summary: Support for TIFF image files.
layout: page
---

# JHOVE TIFF-hul Module

{: #introduction .section-heading}
## 1 Introduction

The TIFF-hul module recognizes and validates the TIFF (Tagged Image File Format) format \[[TIFF 6.0](/references#tiff6)\]. Several TIFF related links are available from the JHOVE [Resources page](/resources#tiff).

The module is invoked by the:

 > ```bash
 > jhove ... -m TIFF-hul ...
 > ```

command line option.

This module can be [configured](/getting-started/config/) with the following parameters:

- byteoffset=true to consider TIFFs without byte-aligned offsets as well-formed. This means that [TIFF-HUL-4](https://github.com/openpreserve/jhove/wiki/TIFF-hul-Messages#tiff-hul-4) and [TIFF-HUL-59](https://github.com/openpreserve/jhove/wiki/TIFF-hul-Messages#tiff-hul-59) won't be reported as errors, but will become InfoMessages and the TIFF can be reported as Well-Formed and Valid.

{: #coverage .section-heading}
## 2 Coverage

The TIFF-hul module recognizes and validates the following public profiles:

- TIFF version 4.0, 5.0, and 6.0 \[[TIFF 4.0](/references#tiff4), [TIFF 5.0](/references#tiff5), [TIFF 6.0](/references#tiff6)\]
- Baseline 6.0 bilevel (previously known as 5.0 Class B), grayscale (Class G), palette-color (Class P), and RGB (Class R) \[[TIFF 6.0](/references#tiff6)\]
- 6.0 extension YCbCr (Class Y) \[[TIFF 6.0](/references#tiff6)\]
- TIFF/IT (ISO 12639:2003), including file types CT, LW, HC, MP, BP, BL, and FP, and conformance levels P1 and P2 \[[TIFF/IT](/references#tiffit)\]
- TIFF/EP (ISO 12234-2:2001) \[[TIFF/EP](/references#tiffep)\]
- Exif 2.0, 2.1 (JEIDA-49-1998), and 2.2 (JEITA CP-3451) \[[Exif 2.1](/references#exif2.1), [Exif 2.2](/references#exif)\]
- GeoTIFF 1.0 \[[GeoTIFF](/references#geotiff)\]
- DLF Benchmark for Faithful Digital Reproductions of Monographs and Serials \[[DLF](/references#dlfbenchmark)\]
- TIFF-FX (RFC 2301), including Profiles C, F, J, L, M, and S \[[TIFF-FX](/references#tiff-fx)\]
- Class F (RFC 2306) \[[Class F](/references#classf), [RFC 2306](/references#rfc2306)\]
- RFC 1314 \[[RFC 1314](/references#rfc1314)\]
- DNG (Adobe Digital Negative) \[[DNG](/references#dng)\]

{: #well-formedness .section-heading}
## 3 Well-Formedness

The following criteria must be met by a TIFF object for JHOVE to consider it well-formed:

- Header with 0x4D4D002A (if big-endian) or 0x49492A00 (if little-endian) at offset 0
- At least one IFD
- All IFD offsets are 16-bit word aligned
- All IFDs have at least one entry
- All IFD entries are sorted in ascending order by tag number
- All IFD entries specify the correct type and count
- All value offsets are 16-bit word aligned
- All value offsets reference locations within the file
- The final IFD is followed by an offset of 0

Note that the 16-bit word aligned criteria can be by-passed by setting the byteoffset parameter

{: #validity .section-heading}
## 4 Validity

The following criteria must be met by a TIFF file for JHOVE to consider it valid:

- The file is well-formed
- The ImageLength (tag 257), ImageWidth (256), and PhotometricInterpretation (262) tags are defined
- If version 4.0 or 5.0 then StripByteCounts (279) and StripOffsets (273) are defined; if version 6.0 then either all of StripByteCounts and StripOffsets or TileByteCounts (325), TileLength (323), TileOffsets (324), and TileWidth (322) are defined
- If PhotometricInterpretation = 4, then bit 2 of NewSubfileType (254) = 1, and vice versa
- If PhotometricInterpretation = 4, then SamplesPerPixel = 1 and BitsPerSample = 1
- If PhotometricInterpretation = 0,1,3, or 4, then SamplesPerPixel = 1
- If PhotometricInterpretation = 2,6, or 8, then SamplesPerPixel = 3
- If PhotometricInterpretation = 3, then ColorMap is defined with 2BitsPerSample\[0\] + 2BitsPerSample\[1\] + 2BitsPerSample\[2\] values
- The values for DotRange (336) are in the range \[0, (2BitsPerSample\[_i_\])-1\]
- CellLength (265) defined only if Threshholding (263) = 2
- If PhotometricInterpretation = 6, then JPEGProc is defined
- If PhotometricInterpretation = 8 or 9, then BitsPerSample = 8 or 16 and SamplesPerPixel-ExtraSamples = 1 or 3
- If ClipPath (343) is defined, then XClipPathUnits (344) is defined
- TileWidth (322) and TileLength (323) values are integral multiples of 16
- DateTime (306) tag is properly formatted: "_YYYY_:_MM_:_DD_ _HH_:_MM_:_SS_"

{: #repinfo .section-heading}
## 5 Representation Information

The MIME type is reported as: image/tiff, except for the TIFF-FX profile, which is reported as: image/tiff-fx, and the Class F profile, which is reported as: image/ief.

In addition to the standard JHOVE [representation information](/documentation#repinfo), all [TIFF tags](tags) and their values are displayed.

By default numeric flag values are displayed using descriptive text labels and rational values are displayed as real decimals. To see the actual "raw" data values use the \-r command line option.

| Tag value | Default display | \-r display |
| --- | --- | --- |
| Compression = 4 | "CCITT Group 4" | "4" |
| YCbCrCoefficient = 587/1000 | "0.587" | "587/1000" |

Image technical properties are reported in terms of the NISO [Z39.87](/references#z39.87) data dictionary.

{: #profiles .section-heading}
### 5.1 Profiles

- **TIFF 4.0**

    A TIFF file is assumed to be version 4.0 until otherwise indicated.

- **TIFF 5.0**

    The presence of the following tags indicates version 5.0:

    > Artist (315), ColorMap (320), DateTime (306), HostComputer (316), NewSubfileType (254), Predictor (317), PrimaryChromaticities (319), Software (305), WhitePoint (318)

    The presence of a PhotometricInterpretation (262) value of 3 (Palette color) or 4 (Transparency mask) indicates version 5.0. The presence of a Compression (259) value of 5 (LZW) indicates version 5.0.

- **TIFF 6.0**

    The presence of the following tags indicates version 6.0:

    > Copyright (33432), DotRange (336), ExtraSamples (338), HalftoneHints (321), InkNames (333), InkSet (332), JPEGACTables (521), JPEGDCTables (520), JPEGInterchangeFormat (513), JPEGInterchangeFormatLength (514), JPEGLosslessPredictors (517), JPEGPointTransforms (518), JPEGProc (512), JPEGRestartInterval (515), JPEGQTables (519), NumberOfInks (334), ReferenceBlackWhite (532), SampleFormat (339), SMinSampleValue (340), SMaxSampleValue (341), TargetPrinter (337), TileLength (323), TileOffsets (324), TileWidth (322), TileByteCounts (325), TransferRange (342), YCbCrCoefficients (529), YCbCrPositioning (531), YCbCrSubSampling (530)

    The presence of a Compression (259) value of 6 (JPEG), or the presence of a PhotometricInterpretation (262) value of 5 (CMYK), 6 (YCbCr), or 8 (CIE L\*a\*b\*) indicates version 6.0.

    The presence of a data type 6 (SBYTE), 7 (UNDEFINED), 8 (SSHORT), 9 (SLONG), 10 (SRATIONAL), 11 (FLOAT), or 12 (DOUBLE) indicates version 6.0.

    The following tag is mandatory:

    > | Tag Name and Number |     | Value |
    | --- | --- | --- |
    | PhotometricInterpretation | 262 |     |

- **Baseline 6.0 profiles**

  All of the Baseline 6.0 profiles are defined in \[[TIFF 6.0](/references#tiff6)\].

  - **Baseline bilevel (Class B)**

    The bilevel profile is for black and white images. The following tags are mandatory:

    > | Tag Name and Number |     | Value |
    | --- | --- | --- |
    | ImageWidth | 256 |     |
    | ImageLength | 257 |     |
    | Compression | 259 | 1, 2, or 32773 |
    | PhotometricInterpretation | 262 | 0 or 1 |
    | StripOffsets | 273 |     |
    | RowsPerStrip | 278 |     |
    | StripByteCounts | 279 |     |
    | XResolution | 282 |     |
    | YResolution | 283 |     |
    | ResolutionUnit | 296 | 1, 2, or 3 |

  - **Baseline grayscale (Class G)**

    The grayscale profile is for grayscale images. The following tags are mandatory:

    > | Tag Name and Number |     | Value |
    | --- | --- | --- |
    | ImageWidth | 256 |     |
    | ImageLength | 257 |     |
    | BitsPerSample | 258 | 4 or 8 |
    | Compression | 259 | 1 or 32773 |
    | PhotometricInterpretation | 262 | 0 or 1 |
    | StripOffsets | 273 |     |
    | RowsPerStrip | 278 |     |
    | StripByteCounts | 279 |     |
    | XResolution | 282 |     |
    | YResolution | 283 |     |
    | ResolutionUnit | 296 | 1, 2, or 3 |

  - **Baseline palette-color (Class P)**

    The palette-color profile is for images using a lookup-table (or color map). The following tags are mandatory:

    > | Tag Name and Number |     | Value |
    | --- | --- | --- |
    | ImageWidth | 256 |     |
    | ImageLength | 257 |     |
    | BitsPerSample | 258 | 4 or 8 |
    | Compression | 259 | 1 or 32773 |
    | PhotometricInterpretation | 262 | 3   |
    | StripOffsets | 273 |     |
    | RowsPerStrip | 278 |     |
    | StripByteCounts | 279 |     |
    | XResolution | 282 |     |
    | YResolution | 283 |     |
    | ResolutionUnit | 296 | 1, 2, or 3 |
    | ColorMap | 320 |     |

  - **Baseline RGB (Class R)**

    The RGB profile is for full-color RGB images. The following tags are mandatory:

    > | Tag Name and Number |     | Value |
    | --- | --- | --- |
    | ImageWidth | 256 |     |
    | ImageLength | 257 |     |
    | BitsPerSample | 258 | 8,8,8 |
    | Compression | 259 | 1 or 32773 |
    | PhotometricInterpretation | 262 | 2   |
    | StripOffsets | 273 |     |
    | SamplesPerPixel | 277 | \>= 3 |
    | RowsPerStrip | 278 |     |
    | StripByteCounts | 279 |     |
    | XResolution | 282 |     |
    | YResolution | 283 |     |
    | ResolutionUnit | 296 | 1, 2, or 3 |

- **6.0 Extension profiles**

  All of the 6.0 extension profiles are defined in \[[TIFF 6.0](/references#tiff6)\].

  - **Extension YCbCr (Class Y)**

    The YCbCr profile is for images using the YCbCr colorspace. The following tags are mandatory:

    > | Tag Name and Number |     | Value |
    | --- | --- | --- |
    | ImageWidth | 256 |     |
    | ImageLength | 257 |     |
    | BitsPerSample | 258 | 8,8,8 |
    | Compression | 259 | 1, 5, or 6 |
    | PhotometricInterpretation | 262 | 6   |
    | StripOffsets | 273 |     |
    | SamplesPerPixel | 277 | 3   |
    | RowsPerStrip | 278 |     |
    | StripByteCounts | 279 |     |
    | XResolution | 282 |     |
    | YResolution | 283 |     |
    | ResolutionUnit | 296 | 1, 2, or 3 |
    | ReferenceBlackWhite | 532 |     |

- **TIFF/IT (ISO 12639:2003)**

  The TIFF/IT profile is for images used in pre-press data exchange \[[TIFF/IT](/references#tiffit)\].

  Profiles are defined for file types BL (binary line art), BP (binary picture), CT (color continuous tone), FP (final page) HC (high resolution continuous tone), LW (color line art), and MP (monochrome continuous tone), each at conformance levels full, P1 or P2. For the specific values required for each profile see the TIFF/IT specification \[[TIFF/IT](/references#tiffit)\].

- **TIFF/EP (ISO 12234-2:2001)**

  The TIFF/EP profile is for images created by digital cameras \[[TIFF/EP](/references#tiffep)\]. The following tags are mandatory:

  > | Tag Name and Number |     | Value |
  | --- | --- | --- |
  | NewSubfileType | 254 | 0 or 1 |
  | ImageWidth | 256 |     |
  | ImageLength | 257 |     |
  | BitsPerSample | 258 | 8,8,8 |
  | Compression | 259 | 1, 7, or > 32767 |
  | PhotometricInterpretation | 262 | 1, 2, 6, 32803, or > 32767 |
  | Make | 271 |     |
  | Model | 272 |     |
  | Orientation | 274 | if defined must be 1, 3, 6, 8, or 9 |
  | XResolution | 282 |     |
  | YResolution | 283 |     |
  | PlanarConfiguration | 284 | 1 or 2 |
  | ResolutionUnit | 296 | 1, 2 or 3 |
  | Software | 305 |     |
  | DateTime | 306 |     |
  | YCbCrCoefficients | 529 | defined if PhotometricInterpretation = 6 |
  | YCbCrSubSampling | 530 | defined if PhotometricInterpretation = 6 |
  | YCbCrPositioning | 531 | defined if PhotometricInterpretation = 6 |
  | ReferenceBlackWhite | 532 | defined if PhotometricInterpretation = 6 |
  | CFARepeatPatternDim | 33421 | defined if PhotometricInterpretation = 32803 |
  | CFAPattern | 33422 | defined if PhotometricInterpretation = 32803 |
  | Copyright | 33432 |     |
  | DateTimeOriginal | 36867 |     |
  | TIFF/EPStandardID | 37398 | 1,0,0,0 |
  | SensingMethod | 37399 | 0, 1, 2, 3, 4, 5, 6, 7, or 8 |

  Additionally, either all of:

  - StripOffsets (273), RowsPerStrip (278), and StripByteCounts (279); or
  - TileWidth (322), TileLength (323), TileOffsets (324), and TileByteCounts (325)

  must be defined, depending upon whether stripped or tiled organization is used.

- **Exif**

  Exif 2.0, 2.1 (JEIDA-49-1998), and 2.2 (JEITA CP-3451) define camera-specific metadata \[[Exif 2.1](/references#exif2.1), [Exif 2.2](/references#exif)\]. The following tags are mandatory in the primary TIFF IFD:

  > | Tag Name and Number |     | Value | Note |
  | --- | --- | --- | --- |
  | ImageWidth | 256 |     | If Compression (259) = 1 |
  | ImageLength | 257 |     | If Compression (259) = 1 |
  | BitsPerSample | 258 | 8,8,8 | If Compression (259) = 1 |
  | Compression | 259 | 1   | If primary TIFF IFD _and_ JPEGInterchangeFormat (513) not defined |
  | 1 or 6 | If thumbnail TIFF IFD | | |
  | PhotometricInterpretation | 262 | 2 or 6 | If Compression (259) = 1 |
  | StripOffsets | 273 |     | If Compression (259) = 1 |
  | SamplesPerPixel | 277 | 3   | If Compression (259) = 1 |
  | RowsPerStrip | 278 |     | If Compression (259) = 1 |
  | StripByteCounts | 279 |     | If Compression (259) = 1 |
  | XResolution | 282 |     | |
  | YResolution | 283 |     | |
  | ResolutionUnit | 296 | 2 or 3 | |
  | ExifIFD | 34665 |     | If primary TIFF IFD |

  The primary TIFF IFD is the first IFD in the file, whose offset is defined in the TIFF header. The optional thumbnail TIFF IFD is a subsequent IFD whose offset is defined following the primary TIFF IFD.

  The following tags are mandatory in the Exif IFD:
  
  <!-- markdownlint-disable -->
    <blockquote>
      <table>
        <tbody><tr><th colspan="2">Tag Name and Number</th><th>Value</th></tr>
        <tr><td>ExifVersion</td><td>36864</td><td>"0220" (Version 2.2)<br>"0210" (Version 2.1)<br>"0200" (Version 2.0)</td></tr>
        <tr><td>FlashpixVersion</td><td>40960</td><td>"0100"</td></tr>
        <tr><td>ColorSpace</td><td>40961</td><td>1 or 65535</td></tr>
      </tbody></table>
    </blockquote>
  <!-- markdownlint-enable -->

- **Baseline GeoTIFF 1.0**

  GeoTIFF defines an industry-standard tagset for the management of geo-referenced or geo-coded raster imagery \[[GeoTIFF](/references#geotiff)\]. The following tags are mandatory:

  > | Tag Name and Number |     | Value |
  | --- | --- | --- |
  | PhotometricInterpretation | 262 |     |
  | GeoKeyDirectoryTag | 34735 |     |

  Additionally, either ModelTiepointTag (33922) or ModelTransformationTag (34264) must be defined, but not both.

- **DLF Benchmark for Faithful Digital Reproductions of Monographs and Serials**

  The DLF benchmarks \[[DLF](/references#dlfbenchmark)\] define the minimum characteristics for effective digital reproduction of monograph and serial pages.

  - **Black and White**

    The DLF black and white benchmark requires lossless compression, one 1-bit sample value per pixel, and a minimum resolution.

    > | Tag Name and Number |     | Value |
    | --- | --- | --- |
    | BitsPerSample | 258 | 1   |
    | Compression | 259 | 1 (none) or 4 (T.6/Group 4) |
    | PhotometricInterpretation | 262 | 0 or 1 |
    | SamplesPerPixel | 277 | 1   |
    | XResolution | 282 | \>= 600 (in) or 1520 (cm) |
    | YResolution | 283 | \>= 600 (in) or 1520 (cm) |

  - **Grayscale**

    The DLF grayscale benchmark requires lossless compression, one 8-bit sample value per pixel, and a minimum resolution.

    > | Tag Name and Number |     | Value |
    | --- | --- | --- |
    | BitsPerSample | 258 | 8   |
    | Compression | 259 | 1 (none), 5 (LZW), 32773 (PackBits RLE) |
    | PhotometricInterpretation | 262 | 0 or 1 |
    | SamplesPerPixel | 277 | 1   |
    | XResolution | 282 | \>= 300 (in) or 760 (cm) |
    | YResolution | 283 | \>= 300 (in) or 760 (cm) |

  - **Color**

    The DLF color benchmark requires lossless compression, three 8-bit sample values per pixel, and a minimum resolution.

    > | Tag Name and Number |     | Value |
    | --- | --- | --- |
    | BitsPerSample | 258 | 8, 8, 8 |
    | Compression | 259 | 1 (none), 5 (LZW), or 32773 (PackBits RLE) |
    | PhotometricInterpretation | 262 | 2 (RGB) or 6 (YCbCr) |
    | SamplesPerPixel | 277 | 3   |
    | XResolution | 282 | \>= 300 (in) or 760 (cm) |
    | YResolution | 283 | \>= 300 (in) or 760 (cm) |

- **TIFF-FX (RFC 2301)**

  TIFF-FX \[[TIFF-FX](/references#tiff-fx)\] is a representation of image data for black and white and color facsimile. The MIME type for this profile is reported as: image/tiff-fx.

  Profiles are defined for minimal black-and-white using binary MH compression (S), extended black-and-white using binary MH, MR, and MMR compression (F), lossless JBIG black-and-white with JBIG compression (J), lossy color and grayscale mode using JPEG compression (C), lossless color and grayscale using JBIG compression (L), and mixed raster content (M).

- **Class F (RFC 2306)**

  TIFF Class F \[[Class F](/references#classf), [RFC 2306](/references#rfc2306)\] is a sub-class of Class B defined for representing CCITT Group 3 (G3) facsimile images. The MIME type for this profile is reported as: image/ief.

  > | Tag Name and Number |     | Value |
  | --- | --- | --- |
  | NewSubfileType | 254 | 2   |
  | ImageWidth | 256 | 1728, 2048 2432, 2592, 3072, 3648, 3456, 4096, 4864 |
  | ImageLength | 257 |     |
  | BitsPerSample | 258 | 1   |
  | Compression | 259 | 3 or 4 |
  | PhotometricInterpretation | 262 | 0 or 1 |
  | FillOrder | 266 | 1 or 2 |
  | StripOffsets | 273 |     |
  | SamplesPerPixel | 277 | 1   |
  | RowsPerStrip | 278 |     |
  | StripByteCounts | 279 |     |
  | XResolution | 282 | 204, 200, 300, 400, 408 (inches) |
  | YResolution | 283 | 98, 196, 100, 200, 300, 391, 400 (inches) |
  | Group3Options | 292 | 0, 1, 4 or 5 (if compression is 3) |
  | Group4Options | 293 | 2 (if compression is 4) |
  | ResolutionUnit | 296 | 2 or 3 |
  | PageNumber | 297 |     |

  The Group3Options tag must be specified if and only if Compression = 3. The Group4Options tag must be specified if and only if Compression = 4.

  ImageWidths of 1728, 2048, and 2432 are permitted only if the X and YResolution is 204x98, 204x196, 204x391, 200x100, or 200x200.  
  ImageWidths of 2592, 3072, and 3648 are permitted only if the X and YResolution is 300x300.  
  ImageWidths of 3456, 4096, and 4864 are permitted only if the X and YResolution is 408x391 or 400x400.

- **RFC 1314**

  RFC 1314 \[[RFC 1314](/references#rfc1314)\] is a sub-type of Class B proposed as a standard for representing FAX-like black and white images within the Internet.

  > | Tag Name and Number |     | Value |
  | --- | --- | --- |
  | NewSubfileType | 254 |     |
  | BitsPerSample | 258 | 1   |
  | Compression | 259 | 1, 3, or 4 |
  | ImageWidth | 256 |     |
  | ImageLength | 257 |     |
  | PhotometricInterpretation | 262 | 0 or 1 |
  | StripOffsets | 273 |     |
  | SamplesPerPixel | 277 | 1   |
  | RowsPerStrip | 278 |     |
  | StripByteCounts | 279 |     |
  | XResolution | 282 |     |
  | YResolution | 283 |     |
  | ResolutionUnit | 296 | 2 or 3 |

{: #extras .section-heading}
## 6 Additional Module Properties

- Nominal file extension: .tif
- Mac OS file type: TIFF
