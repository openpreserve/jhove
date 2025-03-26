---
title: JSTOR/Harvard Object Validation Environment
---

![JHOVE logo](img/jhovelogo.png)

## Open source file format identification, validation & characterisation

[Download JHOVE](https://software.openpreservation.org/rel/jhove-latest.jar){: .btn .btn-primary .btn-lg .pad download}

### Software

**Currently v1.32.1 06-02-2025**

Details of the latest release, including release notes, [can be found on GitHub](https://github.com/openpreserve/jhove/releases/latest).

JHOVE is a file format identification, validation, and characterisation tool. It is implemented as a [Java](https://www.oracle.com/java/technologies/) application and is usable on any Unix, Windows, or OS X platform with appropriate Java installation.

### Supported Formats

| Module          | Format                              | Extension                     | MIME                          |
|------------------|-------------------------------------|-------------------------------|-------------------------------|
| [AIFF-hul](/modules/aiff/) | Audio Interchange File Format | .aif                          | audio/x-aiff                  |
| [ASCII-hul](/modules/ascii/) | ASCII text                     |                               | application/octet-stream      |
| [GIF-hul](/modules/gif/) | Graphics Interchange Format    | .gif                          | image/gif                     |
| [GZIP-kb](/modules/gzip/) | Gzip (GNU zip) format         | .gz                           | application/gzip              |
| [HTML-hul](/modules/html/) | HTML format                   | .html, .htm                   | text/html                     |
| [JPEG-hul](/modules/jpeg/) | JPEG format                   | .jpg/.jls/.spf                | image/jpeg                    |
| [JPEG2000-hul](/modules/jpeg2000/) | JPEG 2000 format             | .jp2/.jpx                     | image/jp2, image/jpx          |
| [PDF-hul](/modules/pdf/) | PDF format                    | .pdf                          | application/pdf               |
| [TIFF-hul](/modules/tiff/) | TIFF format                   | .tiff                         | image/tiff                    |
| [UTF8-hul](/modules/utf8/) | UTF-8 text                   |                               | text/plain; charset=UTF-8     |
| [WARC-kb](/modules/warc/) | WARC format                   | .warc/.warc.gz                | application/warc              |
| [WAVE-hul](/modules/wave/) | Audio for Windows format      | .wav/.bwf/.rt64               | audio/vnd.wave                |
| [XML-hul](/modules/xml/) | XML format                    | .xml                          | text/xml                      |
| [EPUB-ptc](/modules/epub/) | EPUB format                   | .epub                         | application/epub+zip          |
| [MP3](/modules/mp3/) | MP3-MnH                        |                               |                               |
| [ZIP/GZIP](/modules/zip/) | ZIP-ptc/GZIP-ptc             |                               |                               |

### Getting Started

The [getting started guide is on this site](/getting-started/).

### License

JHOVE is made available by the [Open Preservation Foundation](https://openpreservation.org) under the [GNU Lesser General Public License (LGPL)](https://www.gnu.org/licenses/licenses.html#LGPL).

Note that some previous versions of JHOVE were released under the [GNU General Public License (GPL)](https://www.gnu.org/licenses/licenses.html#GPL).

### Mailing List

[Subscribe](https://lists.openpreservation.org/listinfo/jhove) to the JHOVE mailing list.
