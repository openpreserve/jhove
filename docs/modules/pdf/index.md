---
title: PDF-hul Module
layout: page
---

# PDF-hul Module

{: #introduction .section-heading}
## 1 Introduction

The PDF-hul module recognizes and validates the PDF (Portable Document Format) format \[[PDF 1.4](/references#pdf1.4), [PDF 1.5](/references#pdf1.5), [PDF 1.6](/references#pdf1.6)\]. Documents created as PDF 1.7 will be identified as such, but PDF 1.7 is not supported, and documents using features specific to PDF 1.7 or later may be reported as not well-formed or not valid.

The module is invoked by the:

  > `jhove ... -m PDF-hul ...`

command line option.

Parameters may be set in the configuration file to control the amount of information supplied by the module. (In earlier versions of JHOVE, these were set by the -p option of the command line, and added information rather than reducing it.) These parameters are set in the `<param>` element under the `<module>` element. The parameters may be specified as a string of letters, or as separate one-letter parameters, e.g.:

>     <param>apn500</param>

or

>     <param>a</param>
>     <param>p</param>
>     <param>n500</param>

The parameters function as flags with the following significance:

> |     |     |     |
> | --- | --- | --- |
> | a   |     | Suppress document annotations |
> | f   |     | Suppress document font information |
> | o   |     | Suppress document outline |
> | p   |     | Suppress document page structure |
> | n   |     | (JHOVE 1.2) Specify maximum number of fonts to report. Must be followed by a number, e.g., n500 |

By default, document annotations, font information, and outlines are all displayed; they may be suppressed to reduce the size of the JHOVE output. In earlier versions of JHOVE, they were suppressed by default.

Some PDF files have thousands of fonts, and attempting to report them all can make JHOVE run out of memory. By default, a maximum of 1000 fonts will be reported. If there are more fonts, an informational message will report the total number and state that some have been omitted. **This parameter will be available with the release of Jhove 1.2.**

{: #coverage .section-heading}
## 2 Coverage

The PDF-hul module recognizes and validates the following public profiles:

- PDF version 1.0-1.6 \[[PDF 1.4](/references#pdf1.4), [PDF 1.5](/references#pdf1.5), [PDF 1.6\]](/references#pdf1.6)
- PDF/X-1 (ISO 15930-1:2001) \[[PDF/X-1](/references#pdfx1)\], PDF/X-1a (ISO 15930-4:2003) \[[PDF/X-1a](/references#pdfx1a)\], PDF/X-2 (ISO 15930-5:2003) \[[PDF/X-2](/references#pdfx2)\], and PDF/X-3 (ISO 15930-6:2003) \[[PDF/X-3](/references#pdfx3)\]
- Linearized PDF \[[PDF 1.4](/references#pdf1.4)\]
- Tagged PDF \[[PDF 1.4](/references#pdf1.4)\]

{: #well-formedness .section-heading}
## 3 Well-Formedness

The following criteria must be met by a PDF object for JHOVE to consider it well-formed:

- JHOVE uses the criteria for well-formedness defined in \[[PDF](/references#pdf1.4), **Chapter 3, Syntax**\].

    In general, a file is well-formed if it has a header:

    >         %PDF-_m_._n_   

    a body consisting of well-formed objects; a cross-reference table; and a trailer defining the cross-reference table size, and an indirect reference to the document catalog dictionary, and ending with:

    >         %%EOF  

{: #validity .section-heading}
## 4 Validity

### 4.1 Validity criteria

The following criteria must be met by a PDF file for JHOVE to consider it valid:

- The file is well-formed.
- The document structure conforms to the specification. This includes (when present) outlines, pages, the page label tree, attributes, resources, role maps, name trees.
- Version information in the document catalog dictionary, if present, is properly formed.
- Dates are properly formed.
- File specifications are properly formed.
- Any annotations are properly formed.
- Any ArtBox, BleedBox, MediaBox and TrimBox items are PDF Rectangles.
- XMP data, if present, are well-formed.

### 4.2 Limitations

The PDF-HUL module does not check certain aspects of a PDF file, primarily because thoroughly checking these would require access to proprietary compression and encryption algorithms. The following are not checked:

- The data within content streams, and therefore the use of operators and the glyph descriptions of embedded fonts.
- Encrypted data. Some PDF files are effectively null-encrypted; i.e., they are nominally encrypted but the encryption is an identity mapping. JHOVE treats these as encrypted files.

{: #representation-information .section-heading}
## 5 Representation Information

The MIME type is reported as: application/pdf

### 5.1 Profiles

- **PDF 1.0 - 1.6**

    The PDF version is determined by the data specified in the PDF header and the **Version** key of the document catalog dictionary. In the event that these two values do not match, the **Version** key is taken as the authoritative value.

- **PDF/X-1**

    The PDF/X-1 profile is for pre-press data exchange using CMYK data \[[PDF/X-1](/references#pdfx1)\].

      - PDF 1.3
      - GTS\_PDFXVersion key value of (PDF/X-1:2001) or (CGATS.12/1-1999)
      - Font subsets for all characters used in the file are embedded within the file
      - Objects may be compressed using Flate and RunLength compression; for **Image XObjects** JPEG and CCITT (for monochrome images) compression are also allowed
      - Information dictionary **Trapped** key is defined
      - No Actions or JavaScripts

- **PDF/X-1a**

    The PDF/X-1 profile is for pre-press data exchange using CMYK and spot color data \[[PDF/X-1a](/references#pdfx1a)\].

      - All requirements of PDF/X-1 are met
      - GTS\_PDFXVersion key value of (PDF/X-1a:2001)
      - No encryption dictionary
      - If a BleedBox is present and if the ViewerPreferences dictionary contains the ViewClip, PrintArea or PrintClip keys, each of those keys present has the value MediaBox or BleedBox
      - SMask keys are absent or have a value of "None"
      - No JBIG2 or LZW filters
      - No Ref key in Form dictionary
- **PDF/X-2**

    The PDF/X-2 profile is for partial pre-press data exchange \[[PDF/X-2](/references#pdfx2)\].

      - OutputIntents array in Document Catalog Dictionary, with subtype of "GTS\_PDFX", with keys OutputConditionIdentifier and AtoB1Tag, and with either RegistryName or DestOutputProfile
      - Each page includes or inherits a MediaBox
      - Each page has a TrimBox or an ArtBox, but not both.
      - The document information dictionary has a Trapped key with a value of either True or False
      - If an ExtGState object is present, it does not have TR, TR2, or HTP entries
      - SMask keys are absent or have a value of "None"
      - No OPI key in Form or Image objects
- **PDF/X-3**

    The PDF/X-3 profile is for pre-press data exchange using color-managed workflows \[[PDF/X-3](/references#pdfx3)\].

      - OutputIntents array in Document Catalog Dictionary with subtype of "GTS\_PDFX"
      - Any Separation and DeviceN resources have an AlternateSpace of DeviceGray or DeviceCMYK
      - No PostScript or OPI objects
      - No OPI key in Form or Image objects
      - Image Alternates do not have a DefaultForPrinting key
- **Linearized PDF**

    The Linearized PDF profile is for optimized viewing over a network \[[PDF 1.4](/references#pdf1.4)\]

      - First object from beginning of file is valid linearization dictionary
- **Tagged PDF**

    The Tagged PDF profile provides access to higher-level structural and semantic information contained in PDF files \[[PDF 1.4](/references#pdf1.4)\]

      - Document catalog dictionary has MarkInfo dictionary
      - A valid structure tree is present

{: #additional-module-properties .section-heading}
## 6 Additional Module Properties

-Nominal file extension: .pdf
