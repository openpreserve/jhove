---
title: Examples
layout: page
---

# JHOVE Examples

{: #application .section-heading}
## 1 Application

```bash
unix% jhove -c conf/jhove.conf
Jhove (Rel. 1.34.0, 2025-07-02)
 Date: 2025-07-16 14:31:31 BST
 App:
  API: 1.34.0, 2025-07-02
  Configuration: /home/cfw/apps/jhove/1.34.0/conf/jhove.conf
  JhoveHome: /home/cfw/apps/jhove/1.34.0
  Encoding: utf-8
  TempDirectory: /tmp
  BufferSize: 131072
  Module: AIFF-hul 1.6.2
  Module: ASCII-hul 1.4.2
  Module: BYTESTREAM 1.4
  Module: EPUB-ptc 1.4
  Module: GIF-hul 1.4.3
  Module: GZIP-kb 0.3
  Module: HTML-hul 1.4.5
  Module: JPEG-hul 1.5.4
  Module: JPEG2000-hul 1.4.5
  Module: PDF-hul 1.12.8
  Module: PNG-gdm 1.3
  Module: TIFF-hul 1.9.5
  Module: UTF8-hul 1.7.5
  Module: WARC-kb 1.2
  Module: WAVE-hul 1.8.3
  Module: XML-hul 1.5.5
  OutputHandler: Audit 1.1
  OutputHandler: JSON 1.4
  OutputHandler: TEXT 1.8
  OutputHandler: XML 1.14
  Usage: java JHOVE [-c config] [-m module] [-h handler] [-e encoding] [-H handler] [-o output] [-x saxclass] [-t tempdir] [-b bufsize] [-l loglevel] [[-krs] dir-file-or-uri [...]]
  Rights: Derived from software Copyright 2004-2011 by the President and Fellows of Harvard College. Version 1.7 to 1.11 independently released. Version 1.12 onwards released by Open Preservation Foundation. Released under the GNU Lesser General Public License.
```

{: #moule .section-heading}
## 2 Module

```bash
unix% jhove -c conf/jhove.conf -m bytestream
Jhove (Rel. 1.34.0, 2025-07-02)
 Date: 2025-07-16 14:32:26 BST
 Module: BYTESTREAM
  Release: 1.4
  Date: 2018-10-01
  Format: bytestream
  MIMEtype: application/octet-stream
   Feature: edu.harvard.hul.ois.jhove.canValidate
   Feature: edu.harvard.hul.ois.jhove.canCharacterize
  Methodology:
   Well-formed: All bytestreams are well-formed
  Vendor: Harvard University Library
   Type: Educational
   Address: Office for Information Systems, 90 Mt. Auburn St., Cambridge, MA 02138
   Telephone: +1 (617) 495-3724
   Email: jhove-support@hulmail.harvard.edu
  Note: This is the default format
  Rights: Copyright 2003-2007 by JSTOR and the President and Fellows of Harvard College. Released under the GNU Lesser General Public License.
```

{: #output-handler .section-heading}
## 3 Output Handler

```bash
unix% jhove -c conf/jhove.conf -H xml
Jhove (Rel. 1.34.0, 2025-07-02)
 Date: 2025-07-16 14:33:04 BST
 Handler: XML
  Release: 1.14
  Date: 2025-03-12
  Vendor: Harvard University Library
   Type: Educational
   Address: Office for Information Systems, 90 Mt. Auburn St., Cambridge, MA 02138
   Telephone: +1 (617) 495-3724
   Email: jhove-support@hulmail.harvard.edu
  Note: This output handler is defined by the XML Schema https://schema.openpreservation.org/ois/xml/xsd/jhove/jhove.xsd
  Rights: Derived from software Copyright 2004-2011 by the President and Fellows of Harvard College. Version 1.8 release by Open Preservation Foundation. Released under the GNU Lesser General Public License.
```

{: #identification .section-heading}
## 4 Identification

### 4.1 ASCII

```bash
unix% jhove -c conf/jhove.conf -k examples/ascii/control.txt
Jhove (Rel. 1.34.0, 2025-07-02)
 Date: 2025-07-16 14:34:49 BST
 RepresentationInformation: examples/ascii/control.txt
  ReportingModule: ASCII-hul, Rel. 1.4.2 (2022-04-22)
  LastModified: 2025-01-23 12:00:56 GMT
  Size: 51
  Format: ASCII
  Status: Well-Formed and valid
  MIMEtype: text/plain; charset=US-ASCII
  ASCIIMetadata: 
   LineEndings: LF
   ControlCharacters: HT (0x09), VT (0x0B), FF (0x0C), SUB (0x1A)
  Checksum: bae406b6
   Type: CRC32
  Checksum: 2774395cac046bf2fd1898ebed8a5f9a
   Type: MD5
  Checksum: 6753be3059ba36fd970ff620a7ebe3fff95c13fe
   Type: SHA-1
  Checksum: 098601a643b2147fc65bf6b7359aad65b3815cd3eed620db8943f49c94be4ed9
   Type: SHA-256
```

### 4.2 UTF-8

```bash
unix% jhove -c conf/jhove.conf -k examples/utf-8/sample.txt
Jhove (Rel. 1.34.0, 2025-07-02)
 Date: 2025-07-16 14:36:17 BST
 RepresentationInformation: examples/utf-8/sample.txt
  ReportingModule: UTF8-hul, Rel. 1.7.5 (2025-06-25)
  LastModified: 2025-01-23 12:00:57 GMT
  Size: 1297
  Format: UTF-8
  Status: Well-Formed and valid
  MIMEtype: text/plain; charset=UTF-8
  UTF8Metadata: 
   Characters: 937
   UnicodeBlocks: Basic Latin, Latin-1 Supplement, Latin Extended-A, IPA Extensions, Greek and Coptic, Cyrillic, Hebrew, Thai, Georgian, Latin Extended Additional, Greek Extended, General Punctuation, Hiragana, Katakana, CJK Unified Ideographs, Hangul Syllables, Arabic Presentation Forms-A, Arabic Presentation Forms-B
   LineEndings: LF
  Checksum: e7ed40a0
   Type: CRC32
  Checksum: d89aac0e1c61d0aa2cf5051ce21f5838
   Type: MD5
  Checksum: 23ac4b4bd4abd597b98cf03906408eddcf3822b7
   Type: SHA-1
  Checksum: 01777740066802da8e6939638098b7d71064c1adc68d4fc565fde8d35fab870f
   Type: SHA-256
```

### 4.3 PDF

```bash
unix%  jhove -c conf/jhove.conf -k examples/pdf/ddap/DDAP_Singlev3.pdf
Jhove (Rel. 1.34.0, 2025-07-02)
 Date: 2025-07-16 14:37:58 BST
 RepresentationInformation: examples/pdf/ddap/DDAP_Singlev3.pdf
  ReportingModule: PDF-hul, Rel. 1.12.8 (2025-03-12)
  LastModified: 2025-01-23 12:00:57 GMT
  Size: 21423
  Format: PDF
  Version: 1.3
  Status: Well-Formed and valid
  SignatureMatches:
   PDF-hul
  MIMEtype: application/pdf
  Profile: Linearized PDF, ISO PDF/X-1, ISO PDF/X-1a
  PDFMetadata: 
   Objects: 45
   FreeObjects: 1
   IncrementalUpdates: 1
   DocumentCatalog: 
    PageLayout: SinglePage
    PageMode: UseNone
   Info: 
    Title: DDAP_Single3
    Author: Frank Scott
    Creator: QuarkXPress Passportª 4.11 [k]: AdobePS 8.7.2 (104)
    Producer: Acrobat Distiller 4.05 for Macintosh
    CreationDate: Thu Jan 03 13:36:33 GMT 2002
    ModDate: Tue Mar 26 13:27:45 GMT 2002
   ID: 0x29a07bc8f2b880fced92f193e0e57c81, 0xd9f569fa0fe3a1db44b0cf6fc2f3854c
   Filters: 
    FilterPipeline: FlateDecode
   Fonts: 
    Type1: 
     Font: 
      BaseFont: PPJJFN+Futura-ExtraBold
      FontSubset: true
      FirstChar: 32
      LastChar: 181
      FontDescriptor: 
       FontName: PPJJFN+Futura-ExtraBold
       Flags: Nonsymbolic, ForceBold
       FontBBox: -249, -260, 1662, 1009
       FontFile3: true
      Encoding: WinAnsiEncoding
     Font: 
      BaseFont: PPJIPF+Helvetica
      FontSubset: true
      FirstChar: 32
      LastChar: 240
      FontDescriptor: 
       FontName: PPJIPF+Helvetica
       Flags: Nonsymbolic
       FontBBox: -166, -225, 1000, 931
       FontFile3: true
      Encoding: MacRomanEncoding
     Font: 
      BaseFont: PPJJCK+Helvetica-Bold
      FontSubset: true
      FirstChar: 32
      LastChar: 240
      FontDescriptor: 
       FontName: PPJJCK+Helvetica-Bold
       Flags: Nonsymbolic, ForceBold
       FontBBox: -170, -228, 1003, 962
       FontFile3: true
      Encoding: MacRomanEncoding
     Font: 
      BaseFont: PPJIPF+Helvetica
      FontSubset: true
      FirstChar: 32
      LastChar: 181
      FontDescriptor: 
       FontName: PPJIPF+Helvetica
       Flags: Nonsymbolic
       FontBBox: -166, -225, 1000, 931
       FontFile3: true
      Encoding: WinAnsiEncoding
   XMP: <rdf:RDF xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'
 xmlns:iX='http://ns.adobe.com/iX/1.0/'>

 <rdf:Description about=''
  xmlns='http://ns.adobe.com/pdf/1.3/'
  xmlns:pdf='http://ns.adobe.com/pdf/1.3/'>
  <pdf:CreationDate>2002-01-03T13:36:33Z</pdf:CreationDate>
  <pdf:Producer>Acrobat Distiller 4.05 for Macintosh</pdf:Producer>
  <pdf:Author>Frank Scott</pdf:Author>
  <pdf:Creator>QuarkXPress Passportª 4.11 [k]: AdobePS 8.7.2 (104)</pdf:Creator>
  <pdf:Title>DDAP_Single3</pdf:Title>
  <pdf:ModDate>2002-03-26T13:27:45-05:00</pdf:ModDate>
 </rdf:Description>

 <rdf:Description about=''
  xmlns='http://ns.adobe.com/pdfx/1.3/'
  xmlns:pdfx='http://ns.adobe.com/pdfx/1.3/'>
  <pdfx:GTS_PDFXConformance>PDF/X-1a:2001</pdfx:GTS_PDFXConformance>
  <pdfx:Apag_PDFX_Checkup>1.3</pdfx:Apag_PDFX_Checkup>
  <pdfx:GTS_PDFXVersion>PDF/X-1:2001</pdfx:GTS_PDFXVersion>
 </rdf:Description>

 <rdf:Description about=''
  xmlns='http://ns.adobe.com/xap/1.0/'
  xmlns:xap='http://ns.adobe.com/xap/1.0/'>
  <xap:CreateDate>2002-01-03T13:36:33Z</xap:CreateDate>
  <xap:Author>Frank Scott</xap:Author>
  <xap:Title>
   <rdf:Alt>
    <rdf:li xml:lang='x-default'>DDAP_Single3</rdf:li>
   </rdf:Alt>
  </xap:Title>
  <xap:ModifyDate>2002-03-26T13:27:45-05:00</xap:ModifyDate>
  <xap:MetadataDate>2002-03-26T13:27:45-05:00</xap:MetadataDate>
 </rdf:Description>

 <rdf:Description about=''
  xmlns='http://purl.org/dc/elements/1.1/'
  xmlns:dc='http://purl.org/dc/elements/1.1/'>
  <dc:creator>Frank Scott</dc:creator>
  <dc:title>DDAP_Single3</dc:title>
 </rdf:Description>

</rdf:RDF>
   Pages: 
    Page: 
     Label: 1
  Checksum: 5b07663e
   Type: CRC32
  Checksum: 2f23f6c3b04644cda146d2c72069ad55
   Type: MD5
  Checksum: 3294a96298b59e14660b022ecc1f4b7569fa781c
   Type: SHA-1
  Checksum: 07b83f0165a1392997329b5d65fe169b64698d43a9eb7a3be975cc42b2ec31fe
   Type: SHA-256
```

### 4.4 TIFF

```bash
unix% jhove -c conf/jhove.conf -k examples/tiff/little-endian.tif
Jhove (Rel. 1.34.0, 2025-07-02)
 Date: 2025-07-16 14:39:05 BST
 RepresentationInformation: examples/tiff/little-endian.tif
  ReportingModule: TIFF-hul, Rel. 1.9.5 (2024-08-22)
  LastModified: 2025-01-23 12:00:57 GMT
  Size: 26292
  Format: TIFF
  Version: 5.0
  Status: Well-Formed and valid
  SignatureMatches:
   TIFF-hul
  MIMEtype: image/tiff
  Profile: TIFF/IT-BP/P2 (ISO 12639:1998)
  TIFFMetadata: 
   ByteOrder: little-endian
   IFDs: 
    Number: 1
    IFD: 
     Offset: 8
     Type: TIFF
     Entries: 
      NisoImageMetadata: 
       FormatName: image/tiff
       ByteOrder: little_endian
       CompressionScheme: CCITT Group 4
       ImageWidth: 2948
       ImageHeight: 4620
       ColorSpace: white is zero
       ScanningSoftware: Pixel Translations Inc., PIXTIFF Version 54.2.210
       Orientation: normal
       SamplingFrequencyUnit: inch
       XSamplingFrequency: 600
       YSamplingFrequency: 600
       BitsPerSample: 1
       BitsPerSampleUnit: integer
       SamplesPerPixel: 1
      NewSubfileType: 0
      SampleFormat: 1
      MinSampleValue: 0
      MaxSampleValue: 1
      Threshholding: 1
      T6Options: 0
      StripOffsets: 520
      RowsPerStrip: 4620
      StripByteCounts: 25772
      PlanarConfiguration: 1
      TIFFITProperties: 
       BackgroundColorIndicator: background not defined
       ImageColorIndicator: image not defined
       TransparencyIndicator: no transparency
       PixelIntensityRange: 0, 1
       RasterPadding: 1 byte
       BitsPerRunLength: 8
       BitsPerExtendedRunLength: 16
  Checksum: d26149d3
   Type: CRC32
  Checksum: ce0ffcb1c1662240edbab4903d6307c5
   Type: MD5
  Checksum: 304ac95579c21f3498dfdff71107d3845220d34f
   Type: SHA-1
  Checksum: 4f5c8da2f18e726b9a76b2a4f56f73891787b5576e532b30fe076c63f1c17b22
   Type: SHA-256
```
