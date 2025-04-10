---
title: AIFF-hul Module
layout: page
---

# AIFF-hul Module

{: #introduction .section-heading}
## Introduction

The AIFF-hul module recognizes and validates the Audio Interchange File Format (AIFF)  
[AIFF](/references#aiff).  
The AIFF format is itself a profile of the Electronic Arts IFF 85 format  
[IFF](/references#iff85).

The module is invoked by the:

> ```bash
> jhove ... -m AIFF-hul ...
> ```

command line option.

{: #coverage .section-heading}
## 2 Coverage

The AIFF-hul module recognizes and validates the following public profiles:

- AIFF 1.3 [AIFF](/references#aiff)
- AIFF-C [AIFF-C](/references#aiff-c)

{: #well-formedness .section-heading}
## 3 Well-Formedness

The following criteria must be met by an AIFF object for JHOVE to consider it well-formed:

- Magic number: "FORM" at byte offset 0; "AIFF" (for AIFF) or "AIFC" (for AIFF-C) at offset 8
- One Form chunk containing one Common chunk and at most one Sound Data chunk (if `numSampleFrames` > 0)
- At most one instance of each of the following optional chunks: Marker, Instrument, Audio Recording, Comments, Name, Author, Copyright
- All chunks required by a given profile exist in the file
- All chunk structures are well-formed: a four ASCII character `ID`, followed by a 32 signed integer `size`, followed by a `size` byte data block (if `size` is odd, then the data block includes a final padding byte of value 0x00)
- No data exist before the first byte of the chunk or after the last byte of the last chunk

{: #validity .section-heading}
## 4 Validity

The following criteria must be met by an AIFF file for JHOVE to consider it valid:

- The file is well-formed

{: #repinfo .section-heading}
## 5 Representation Information

The MIME type is reported as: `audio/x-aiff`.

In addition to the standard JHOVE [representation information](index.html#repinfo), the following AIFF-specific properties are reported:

- Property "AIFFMetadata" of type PROPERTY and arity ARRAY
  - Properties capturing the technical attributes of the AIFF image from all chunks

The AIFF module reports audio technical properties using the draft AES-X098B, *Core audio metadata XML definition*, currently under development by the [Audio Engineering Society](http://www.aes.org/) (AES) [SC-03-06](http://www.aes.org/standards/b_policies/aessc-structure.cfm#SC-03) Working Group on Digital Library and Archive Systems.

### 5.1 Profiles

#### AIFF 1.3

AIFF is an interchange format for sampled monaural or multichannel sampled sounds using a variety of sample rates and widths.

Profile requirements include:

- The Form chunk identifier is "AIFF"

#### AIFF-C

AIFF-C is an extended AIFF format that can contain compressed audio data.

Profile requirements include:

- The Form chunk identifier is "AIFC"
- The fields `compressionType` and `compressionName` in the Common Chunk

    Common compression types and names include, but are not limited to:

    > | Type   | Name                     | Note                                                                 |
    |--------|--------------------------|----------------------------------------------------------------------|
    | "NONE" | "Not compressed"         | PCM data                                                            |
    | "ACE2" | "ACE 2-to-1"             | [Apple](http://www.apple.com/) IIGS ACE (Audio Compression/Expansion) |
    | "ACE8" | "ACE 8-to-3"             |                                                                      |
    | "APD4" | "4:1 Intel/DVI ADPCM"    | [SoundHack](http://www.soundhack.com/)                              |
    | "ALAW" | "CCITT G.711 A-law"      | [SGI](http://www.sgi.com/) 8-bit [ITU-T](http://www.itu.int/ITU-T/) G.711 A-law (64 kb/s) |
    | "alaw" | "ALaw 2:1"               | [Apple](http://www.apple.com) 8-bit [ITU-T](http://www.itu.int/ITU-T) G.711 A-law |
    | "DWVW" | "Delta With Variable Word Width" | Yamaha TX16W Typhoon sampler                                      |
    | "FL32" | "Float 32"               | [SoundHack](http://www.soundhack.com/) / [CSound](http://www.csound.com/) [IEEE](http://www.ieee.org/) 32-bit floating point |
    | "fl32" | "32-bit floating point"  | [Apple](http://www.apple.com/) [IEEE](http://www.ieee.com/) 32-bit floating point |
    | "fl64" | "64-bit floating point"  |                                                                      |
    | "ima4" | "IMA 4:1"                |                                                                      |
    | "MAC3" | "MACE 3-to-1"            | Macintosh Audio Compression/Expansion                               |
    | "MACE6" | "MACE 6-to-1"           |                                                                      |
    | "QDMC" | "QDesign Music"          | [QDesign](http://www.qdesign.com/) Music codec                      |
    | "Qclp" | "QualComm PureVoice"     | [QualComm](http://www.qualcomm.com/) PureVoice codec                |
    | "rt24" | "RT24 50:1"              | [Voxware](http://www.voxware.com/) codec                            |
    | "rt29" | "RT29 50:1"              |                                                                      |
    | "ULAW" | "CCITT G.711 µ-law"      | [SGI](http://www.sgi.com/) 8-bit [ITU-T](http://www.itu.int/ITU-T/) G.711 µ-law (64 kb/s) |
    | "ulaw" | "µLaw 2:1"               | [Apple](http://www.apple.com/) 8-bit [ITU-T](http://www.itu.int/ITU-T/) G.711 µ-law |

- One Format Version chunk

{: #additional-module-properties .section-heading}
## 6 Additional Module Properties

- Nominal file extension: `.aif`
- Nominal file extension: `.afc` (for the AIFF-C profile)
