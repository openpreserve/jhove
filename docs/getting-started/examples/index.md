---
title: Examples
layout: page
---

# JHOVE Examples

## 1 Application

```
unix% jhove -c conf/jhove.conf
Jhove (Rel. 1.0, 2005-05-26)
Date: 2005-05-26 15:30:42 EST
App:
  API: 1.0, 2005-05-26
  Configuration: /users/stephen/jhove/conf/jhove.conf
  JhoveHome: /users/stephen/projects/jhove/
  Encoding: utf-8
  TempDirectory: /var/tmp
  BufferSize: 131072
  Module: AIFF-hul 1.2
  Module: ASCII-hul 1.1
  Module: BYTESTREAM 1.2
  Module: GIF-hul 1.2
  Module: HTML-hul 1.0
  Module: JPEG-hul 1.1
  Module: JPEG2000-hul 1.2
  Module: PDF-hul 1.4
  Module: TIFF-hul 1.3
  Module: UTF8-hul 1.1
  Module: WAVE-hul 1.1
  Module: XML-hul 1.2
  OutputHandler: Audit 1.1
  OutputHandler: TEXT 1.3
  OutputHandler: XML 1.3
  Usage: java Jhove [-c config] [-m module] [-h handler] [-e encoding] [-H handler] [-o output] [-x saxclass] [-t tempdir] [-b bufsize] [-l loglevel] [[-krs] dir-file-or-uri [...]]
  Rights: Copyright 2004-2005 by the President and Fellows of Harvard College. Released under the GNU Lesser General Public License.
```

## 2 Module

```
unix% jhove -c conf/jhove.conf -m bytestream
Jhove (Rel. 1.0, 2005-05-26)
Date: 2005-05-26 15:32:34 EST
Module: BYTESTREAM
  Release: 1.2
  Date: 2005-03-09
  Format: bytestream
  MIMEtype: application/octet-stream
  ...
```

## 3 Output Handler

```
unix% jhove -c conf/jhove.conf -H xml
Jhove (Rel. 1.0, 2005-05-26)
Date: 2005-05-26 15:35:12 EST
Handler: XML
  Release: 1.3
  Date: 2005-05-05
  Vendor: Harvard University Library
  ...
```

## 4 Identification

### 4.1 ASCII

```
unix% jhove -c conf/jhove.conf -k examples/ascii/control.txt
Jhove (Rel. 1.0, 2005-05-26)
Date: 2005-05-26 16:06:49 EST
RepresentationInformation: examples/ascii/control.txt
  ReportingModule: ASCII-hul, Rel. 1.1 (2005-01-11)
  ...
```

### 4.2 UTF-8

```
unix% jhove -c conf/jhove.conf -k examples/utf-8/sample.txt
Jhove (Rel. 1.0, 2005-05-26)
Date: 2005-05-26 16:14:58 EST
RepresentationInformation: examples/utf-8/sample.txt
  ReportingModule: UTF8-hul, Rel. 1.1 (2005-01-11)
  ...
```

### 4.3 PDF

```
unix% jhove -c conf/jhove.conf -k examples/pdf/ddap/DDAP_Singlev3.pdf
Jhove (Rel. 1.0, 2005-05-26)
Date: 2005-05-26 14:21:51 EDT
RepresentationInformation: examples/pdf/ddap/DDAP_Singlev3.pdf
  ReportingModule: PDF-hul, Rel. 1.4 (2005-03-09)
  ...
```

### 4.4 TIFF

```
unix% jhove -c conf/jhove.conf -k examples/tiff/little-endian.tif
Jhove (Rel. 1.0, 2005-05-26)
Date: 2005-05-26 14:23:21 EDT
RepresentationInformation: examples/tiff/little-endian.tif
  ReportingModule: TIFF-hul, Rel. 1.3 (2005-05-05)
  ...
```
