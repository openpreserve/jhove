---
title: WAVE-hul Module
layout: page
---

# WAVE-hul Module

{: #introduction .section-heading}
## 1 Introduction

The WAVE-hul module recognizes and validates the Audio for Windows format (WAVE) \[[WAVE](/references#wave), [WAVEFORMAT](/references#waveformat)\]. WAVE is a variant of the Microsoft RIFF format \[[RIFF](/references#riff)\], which is itself an implementation of the Electronic Arts IFF 85 format \[[IFF](/references#iff85)\].

The module can be invoked with the following command-line option:

  > `jhove ... -m WAVE-hul ...`

{: #coverage .section-heading}
## 2 Coverage

The WAVE-hul module recognizes and validates the following public profiles:

- PCMWAVEFORMAT \[[PCMWAVEFORMAT](/references#pcmwaveformat)\]
- WAVEFORMATEX \[[WAVEFORMATEX](/references#waveformatex)\]
- WAVEFORMATEXTENSIBLE \[[WAVEFORMATEXTENSIBLE](/references#waveformatextensible)\]
- EBU Technical Specification 3285, Broadcast Wave Format (BWF) version 0, 1 and 2 \[[BWF](/references#bwf), [BWF Supp 1](/references#bwf-1), [BWF Supp 3](/references#bwf-3), [BWF Supp 4](/references#bwf-4)\]
- EBU Technical Specification 3306, RF64 \[[RF64](/references#rf64)\]

{: #well-formedness .section-heading}
## 3 Well-Formedness

The following criteria must be met by a WAVE object for JHOVE to consider it well-formed:

- "RIFF" or "RF64" at byte offset 0; "WAVE" at offset 8
- All chunk structures are well-formed: a four-character printable ASCII ID, followed by a 32-bit unsigned integer size, followed by a size\-length data block (if size is odd, then the data block includes a final padding byte of 0x00)
- If RF64, the first chunk is a Data Size 64 chunk
- A Format chunk exists
- A Data chunk exists

{: #validity .section-heading}
## 4 Validity

The following criteria must be met by a WAVE file for JHOVE to consider it valid:

- The file is well-formed
- All chunk IDs begin with printable ASCII
- The Format chunk appears before the Data chunk
- The following chunks appear no more than once:
  - Broadcast Audio Extension
  - Cart
  - Cue Points
  - Data
  - Format
  - Instrument
  - Link
  - MPEG Audio Extension
  - Peak Envelope

{: #repinfo .section-heading}
## 5 Representation Information

The base MIME type is reported as `audio/vnd.wave`, but may be extended with a `codec` parameter as described in RFC 2361 \[[RFC 2361](/references#rfc2361)\].

In addition to the standard JHOVE [representation information](/documentation#repinfo), the following WAVE-specific properties are reported:

- Property "WAVEMetadata" of type PROPERTY and arity LIST
  - Properties capturing the technical attributes of the WAVE image from all chunks
  - ...

The module reports audio properties using the draft standard AES-X098B, Core audio metadata XML definition, developed by the [Audio Engineering Society](http://www.aes.org/) (AES) [SC-03-06](http://www.aes.org/standards/b_policies/aessc-structure.cfm#SC-03) Working Group on Digital Library and Archive Systems.

The module can recognize and process the following chunks:

Top-level chunks

| ID  | List Type | Name | Property | References |
| --- | --- | --- | --- | --- |
| `bext` |     | Broadcast Audio Extension | BroadcastAudioExtension | \[[BWF](/references#bwf)\] |
| `cue` |     | Cue Points | CuePoints | \[[WAVE](/references#wave)\] |
| `data` |     | Data | Data | \[[WAVE](/references#wave)\] |
| `ds64` |     | Data Size 64 |     | \[[RF64](/references#rf64)\] |
| `fact` |     | Fact | Fact | \[[WAVE](/references#wave)\] |
| `fmt` |     | Format |     | \[[WAVE](/references#wave), [WAVEFORMAT](/references#waveformat),  <br>[PCMWAVEFORMAT](/references#pcmwaveformat),  <br>[WAVEFORMATEX](/references#waveformatex),  <br>[WAVEFORMATEXTENSIBLE](/references#waveformatextensible)\] |
| `inst` |     | Instrument | Instrument | \[[MDSU](/references#mdsu3)\] |
| `labl` |     | Label | Label |     |
| `levl` |     | Peak Envelope | PeakEnvelope | \[[BWF Supp 3](/references#bwf-3)\] |
| `link` |     | Link | Link | \[[BWF Supp 4](/references#bwf-4)\] |
| `list` | `adtl` | Associated Data List |     |     |
| `LIST` | `adtl` | Associated Data List |     | \[[WAVE](/references#wave)\] |
| `LIST` | `exif` | Exif List | Exif | \[[Exif](/references#exif)\] |
| `LIST` | `INFO` | Info List | ListInfo | \[[WAVE](/references#wave)\] |
| `mext` |     | MPEG Audio Extension | MPEG | \[[BWF Supp 1](/references#bwf-1)\] |
| `note` |     | Note | Note |     |
| `smpl` |     | Sample | Sample | \[[MDSU](/references#mdsu3)\] |

Associated Data List chunks

| ID  | Name | Property | References |
| --- | --- | --- | --- |
| `labl` | Label | Label | \[[WAVE](/references#wave)\] |
| `ltxt` | Text with Data Length | LabeledTextItem | \[[WAVE](/references#wave)\] |
| `note` | Note | Note | \[[WAVE](/references#wave)\] |

Exif List chunks

| ID  | Name | Property | References |
| --- | --- | --- | --- |
| `ecor` | Make | Manufacturer | \[[Exif](/references#exif)\] |
| `emdl` | Model | Model | \[[Exif](/references#exif)\] |
| `erel` | Related Information | RelatedImageFile | \[[Exif](/references#exif)\] |
| `etim` | Time | TimeCreated | \[[Exif](/references#exif)\] |
| `ever` | Version | ExifVersion | \[[Exif](/references#exif)\] |

Info List chunks

| ID  | Name | Property | References |
| --- | --- | --- | --- |
| `IARL` | Archival Location | ArchivalLocation | \[[WAVE](/references#wave)\] |
| `IART` | Artist | Artist | \[[WAVE](/references#wave)\] |
| `ICMS` | Commissioned | Commissioned | \[[WAVE](/references#wave)\] |
| `ICMT` | Comments | Comments | \[[WAVE](/references#wave)\] |
| `ICOP` | Copyright | Copyright | \[[WAVE](/references#wave)\] |
| `ICRD` | Creation Date | CreationDate | \[[WAVE](/references#wave)\] |
| `ICRP` | Cropped | Cropped | \[[WAVE](/references#wave)\] |
| `IDIM` | Dimensions | Dimensions | \[[WAVE](/references#wave)\] |
| `IDPI` | Dots Per Inch | DotsPerInch | \[[WAVE](/references#wave)\] |
| `IENG` | Engineer | Engineer | \[[WAVE](/references#wave)\] |
| `IGNR` | Genre | Genre | \[[WAVE](/references#wave)\] |
| `IKEY` | Keywords | Keywords | \[[WAVE](/references#wave)\] |
| `ILGT` | Lightness | Lightness | \[[WAVE](/references#wave)\] |
| `IMED` | Medium | Medium | \[[WAVE](/references#wave)\] |
| `INAM` | Name | Name | \[[WAVE](/references#wave)\] |
| `IPLT` | Palette Setting | PaletteSetting | \[[WAVE](/references#wave)\] |
| `IPRD` | Product | Product | \[[WAVE](/references#wave)\] |
| `ISBJ` | Subject | Subject | \[[WAVE](/references#wave)\] |
| `ISFT` | Software | Software | \[[WAVE](/references#wave)\] |
| `ISHP` | Sharpness | Sharpness | \[[WAVE](/references#wave)\] |
| `ISRC` | Source | Source | \[[WAVE](/references#wave)\] |
| `ISRF` | Source Form | SourceForm | \[[WAVE](/references#wave)\] |
| `ITCH` | Technician | Technician | \[[WAVE](/references#wave)\] |

### 5.1 Profiles

WAVE is a format for uncompressed or compressed sampled audio. The format is defined informally by references to various Microsoft API data structures:

- [WAVEFORMAT](/references#waveformat)
- [PCMWAVEFORMAT](/references#pcmwaveformat)
- [WAVEFORMATEX](/references#waveformatex)
- [WAVEFORMATEXTENSIBLE](/references#waveformatextensible)

The baseline `fmt` chunk is defined by the WAVEFORMAT structure with a length of 14 bytes:

> ```text
>  WORD  wFormatTag
>  WORD  nChannels
>  DWORD nSamplesPerSec
>  DWORD nAvgBytesPerSec
>  WORD  nBlockAlign
> ```

Where `WORD` indicates a 16-bit unsigned integer and `DWORD` indicates a 32-bit unsigned integer.

The specific form of the sampled data is specified by the `fmt` chunk's wFormatTag field. For a list of registered wFormatTag values, see RFC 2361 \[[RFC 2361](/references#rfc2361)\].

#### PCMWAVEFORMAT

This is an extension to the WAVEFORMAT profile in which the `fmt` chunk is defined by the PCMWAVEFORMAT structure with a length of 16 bytes \[[PCMWAVEFORMAT](/references#pcmwaveformat)\]:

> ```text
>    WAVEFORMAT
>    WORD  wBitsPerSample
> ```

Profile requirements include:

- wFormatTag = 0x0001

#### WAVEFORMATEX

This is an extension to the PCMWAVEFORMAT profile supporting both PCM and non-PCM audio formats \[[WAVEFORMATEX](/references#waveformatex)\]. The `fmt` chunk is defined by the WAVEFORMATEX structure with a length ≥ 18 bytes:

> ```text
>    PCMWAVEFORMAT
>    WORD  cbSize
> ```

Profile requirements include:

- wFormatTag ≠ 0xFFFE
- If wFormatTag = 0x0001 then
  - nAvgBytesPerSec = nSamplesPerSec × nBlockAlign (recommended)
  - nBlockAlign = nChannels × **ceiling(**wBitsPerSample / 8**)**
  - wBitsPerSample = 8 or 16

#### WAVEFORMATEXTENSIBLE

This is the most recent version of the Microsoft WAVE format for audio sample data with greater than two channels or 16-bit sampling \[[WAVEFORMATEXTENSIBLE](/references#waveformatextensible)\]. The `fmt` chunk is defined by the WAVEFORMATEXTENSIBLE structure with a length ≥ 40 bytes:

> ```text
>    WAVEFORMATEX
>    UNION samples   {
>      WORD  wValidBitsPerSample
>      WORD  wSamplesPerBlock
>      WORD  wReserved
>    }
>    DWORD dwChannelMask
>    GUID  subFormat {
>      DWORD f1
>      WORD  f2
>      WORD  f3
>      CHAR  f4\[8\]
>    }
> ```

Where `UNION` is a C-style union structure and `CHAR` is an 8-bit unsigned integer.

Profile requirements include:

- wFormatTag = 0xFFFE
- nBlockAlign = nChannels × **ceiling(**wBitsPerSample / 8**)**
- wBitsPerSample is a multiple of 8
- cbSize > 21
- wValidBitsPerSample ≤ wBitsPerSample

#### BWF

Broadcast Wave Format is an extension of the WAVEFORMATEX profile, defined by the European Broadcast Union (EBU) as EBU Technical Specification 3285 and its supplements \[[BWF](/references#bwf), [BWF Supp 1](/references#bwf-1), [BWF Supp 2](/references#bwf-2), [BWF Supp 3](/references#bwf-3), [BWF Supp 4](/references#bwf-4), [BWF Supp 5](/references#bwf-5), [BWF Supp 6](/references#bwf-6)\].

Profile requirements include:

- `bext` chunk exists
- wFormatTag = 0x0001 or 0x0050
- If wFormatTag = 0x0050 then
  - `fact` chunk exists

#### RF64

The RF64 format was defined by the European Broadcast Union (EBU) in EBU Technical Specification 3306 to allow WAVE format files and chunks to exceed 4 gigabytes in size \[[RF64](/references#rf64)\].

Profile requirements include:

- "RF64" at byte offset 0

{: #additional-module-properties .section-heading}
## 6 Additional Module Properties

- Nominal file extension: .wav
- Alternative file extensions: .bwf, .rf64
