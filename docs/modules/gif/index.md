---
title: GIF-hul Module
layout: page
---

# GIF-hul Module

{: #introduction .section-heading}
## 1 Introduction

The GIF-hul module recognizes and validates the GIF (Tagged Interchange Format) format \[[GIF89a](/references#gif89a)\].

The module is invoked by the:

  > jhove ... -m gif-hul ...

command line option.

{: #coverage .section-heading}
## 2 Coverage

The GIF-hul module recognizes and validates the following public profiles:

- GIF version 87a and 89a \[[GIF87a](/references#gif87a), [GIF89a](/references#gif89a)\]

{: #well-formedness .section-heading}
## 3 Well-Formedness

The following criteria must be met by a GIF object for JHOVE to consider it well-formed:

- Properly formed signature ("GIF" at byte offset 0) and version identifier ("87a" or "89a" at byte offset 3)
- A sequence of properly formed (as defined by the relevant specifications) control, graphic-rendering, and special purpose blocks
- GIF terminator (0x00) at end of the content stream

{: #validity .section-heading}
## 4 Validity

The following criteria must be met by a GIF file for JHOVE to consider it valid:

- The file is well-formed
- At most one global color map
- At most one graphic control extension preceding an image descriptor or a plain text extension

{: #repinfo .section-heading}
## 5 Representation Information

The MIME type is reported as: image/gif

In addition to the standard JHOVE [representation information](index.html#repinfo), the following GIF-specific properties are reported:

- Property "GIFMetadata" of type PROPERTY and arity ARRAY
  - Property "GraphicRenderingBlocks" of type INTEGER and arity SCALAR  
    indicating the total number of Image Descriptor and Plain Text Extension blocks
  - Property "Blocks" of type PROPERTY and arity LIST  
    The block types are "LogicalScreenDescriptor", "GlobalColorTable", "GraphicControlExtension", "ImageDescriptor", "PlainTextExtension", "ApplicationExtension", "CommentExtension"

    - Property "LogicalScreenDescriptor" of type PROPERTY and arity ARRAY
      - Property "LogicalScreenWidth" of type INTEGER and arity SCALAR
      - Property "LogicalScreenHeight" of type INTEGER and arity SCALAR
      - Property "ColorResolution" of type SHORT and arity SCALAR
      - Property "BackgroundColorIndex" of type SHORT and arity SCALAR
      - Property "PixelAspectRatio" of type SHORT and arity SCALAR
      - Property "GlobalColorTableFlag" of type BYTE and arity SCALAR  
        =0 "No global color table; background color index meaningless";  
        =1 "Global color table follows; background color index meaningful"
      - Property "GlobalColorTableSortFlag" of type BYTE and arity SCALAR  
        =0 "Not ordered";  
        =1 "Ordered by decreasing importance"
      - Property "GlobalColorTableSize" of type SHORT and arity SCALAR

    - (Optional) property "GlobalColorTable" of type SHORT and arity ARRAY

    - (Optional) property "GraphicControlExtension" of type PROPERTY and arity ARRAY
      - Property "DisposalMethod" of type BYTE and arity SCALAR  
        =0 "No disposal specified"; =1 "Do not dispose"; =2 "Restore to background color"; =3 "Restore to previous"
      - Property "UserInputFlag" of type BYTE and arity SCALAR  
        =0 "User input not expected"; =1 "User input expected"
      - Property "TransparencyFlag" of type BYTE and arity SCALAR  
        =0 "Transparent index is not given"; =1 "Transparent index given"
      - Property "DelayTime" of type INTEGER and arity SCALAR
      - Property "TransparentColorIndex" of type SHORT and arity SCALAR

    - (Optional) property "ImageDescriptor" of type PROPERTY and arity ARRAY
      - Property "ImageLeftPosition" of type INTEGER and arity SCALAR
      - Property "ImageRightPosition" of type INTEGER and arity SCALAR
      - Property "InterlaceFlag" of type BYTE and arity SCALAR  
        =0 "Image is not interlaced"; =1 "Image is interlaced"
      - Property "LocalColorTableFlag" of type BYTE and arity SCALAR  
        =0 "No local color table; use global table if available";  
        =1 "Local color table follows"
      - Property "LocalColorTableSortFlag" of type BYTE and arity SCALAR  
        =0 "Not ordered"; =1 "Order by decreasing importance"
      - Property "LocalColorTableSize" of type SHORT and arity SCALAR

      - Property "NisoImageMetadata" of type NISOIMAGEMETADATA and arity SCALAR  
        MIMEType = "image/gif";  
        ByteOrder = "little_endian";  
        CompressionScheme = 5 "LZW";  
        ColorSpace = 3 "Palette color";  
        Orientation = 1 "Normal";  
        ImageWidth = ImageWidth; ImageLength = ImageHeight;  
        SamplesPerPixel = 1;  
        ColorMap = local color table (if supplied)

    - (Optional) property "PlainTextExtension" of type PROPERTY and arity ARRAY
      - Property "TextGridLeftPosition" of type INTEGER and type SCALAR
      - Property "TextGridTopPosition" of type INTEGER and type SCALAR
      - Property "TextGridWidth" of type INTEGER and type SCALAR
      - Property "TextGridHeight" of type INTEGER and type SCALAR
      - Property "CharacterCellWidth" of type SHORT and type SCALAR
      - Property "CharacterCellHeight" of type SHORT and type SCALAR
      - Property "TextForegroundColorIndex" of type SHORT and type SCALAR
      - Property "TextBackgroundColorIndex" of type SHORT and type SCALAR
      - Property "PlainTextData" of type STRING and type SCALAR

    - (Optional) property "ApplicationExtension" of type PROPERTY and arity ARRAY
      - Property "ApplicationIdentifier" of type STRING and arity SCALAR
      - Property "ApplicationAuthenticationCode" of type SHORT and type ARRAY
      - Property "ApplicationDataSize" of type INTEGER and type SCALAR  
        use the block size to determine the length of the application data

    - (Optional) property "CommentExtension" of type STRING and arity SCALAR

Image technical properties are reported in terms of the NISO [Z39.87](https://www.niso.org/standards-committees/z3987) data dictionary.

{: #profiles .section-heading}
### 5.1 Profiles

- **GIF 87a**  
  The presence of the version identifier "87a" indicates version 87a.
- **GIF 89a**  
  The presence of the version identifier "89a" indicates version 89a.

{: #additional-module-properties .section-heading}
## 6 Additional Module Properties

- Nominal file extension: .gif

**Note** "GIF" and "Graphics Interchange Format" are trademarks of [CompuServe Interactive Services, Inc.](http://www.compuserve.com)
