---
title: JSTOR/Harvard Object Validation Environment
nav_title: Home
nav_order: 1
section: home
parent: null
summary: JHOVE overview and download entry point.
layout: page
---

<!-- markdownlint-disable -->
<header class="jumbotron vertical-center">
    <img src="jhovelogo.png" alt="JHOVE logo">
    <h2>Open source file format identification, validation &amp; characterisation</h2>
    <a href="https://software.openpreservation.org/rel/jhove-latest.jar" class="btn btn-primary btn-lg pad" download="">Download JHOVE</a>
</header>
<!-- markdownlint-enable -->

{: #software .section-heading}
### Software

<!-- markdownlint-disable -->
**Currently v{{ site.data.vars.version }} {{ site.data.vars.release_date }}**
<!-- markdownlint-enable -->

Details of the latest release, including release notes, [can be found on GitHub](https://github.com/openpreserve/jhove/releases/latest).

JHOVE is a file format identification, validation, and characterisation tool. It is implemented as a [Java](https://www.oracle.com/java/technologies/) application and is usable on any Unix, Windows, or OS X platform with appropriate Java installation.

{: #introduction .section-heading}
### Supported Formats

| Module | Format | Extension | MIME |
| --- | --- | --- | --- |
| [AIFF-hul](/modules/aiff/) | Audio Interchange File Format | .aif | audio/x-aiff |
| [ASCII-hul](/modules/ascii/) | ASCII text |     | application/octet-stream |
| [GIF-hul](/modules/gif/) | Graphics Interchange Format | .gif | image/gif |
| [GZIP-kb](/modules/gzip/) | Gzip (GNU zip) format | .gz | application/gzip |
| [HTML-hul](/modules/html/) | HTML (Hypertext Markup Language) format | .html, .htm | text/html |
| [JPEG-hul](/modules/jpeg/) | JPEG format | .jpg/.jls(for JPEG-LA)/.spf(for SPIFF) | image/jpeg |
| [JPEG2000-hul](/modules/jpeg2000/) | JPEG 2000 (ISO/IEC 15444) format | .jpg/.jls(for JPEG-LA)/.spf(for SPIFF) | image/jp2 (for JP2)/ image/jpx (for JPX) |
| [PDF-hul](/modules/pdf/) | PDF (Portable Document Format) format | .pdf | application/pdf |
| [JHOVE TIFF-hul](/modules/tiff/) | TIFF (Tagged Image File Format) format | .tiff (nominal)/ TIFF (Mac OS) | image/tiff/ image/tiff-fx (for TIFF-FX)/ image.ief (for Class F) |
| [UTF8-hul](/modules/utf8/) |     |     | text/plain; charset=UTF-8 |
| [WARC-kb](/modules/warc/) | WARC (Web ARChive) format | .warc/ .warc.gz | application/warc |
| [WAVE-hul](/modules/wave/) | Audio for Windows format (WAVE) | .wav/ .bwf/ .rt64 | audio/vnd.wave |
| [XML-hul](/modules/xml/) | XML (Extensible Markup Language) format | .xml | text/xml |
| [EPUB-ptc](/modules/epub/) | EPUB format | .epub | application/epub+zip |
| [MP3](/modules/mp3/) | MP3-MnH |     |     |
| [ZIP/ GZIP](/modules/zip/) | ZIP-ptc/ GZIP-ptc |     |     |

{: #getting-started .section-heading}
## Getting Started

The [getting started guide is on this site](/getting-started/).

{: #license .section-heading}
## License

JHOVE is made available by the [Open Preservation Foundation](https://openpreservation.org) under the [GNU Lesser General Public License (LGPL)](https://www.gnu.org/licenses/licenses.html#LGPL).

Note that some previous versions of JHOVE were released under the [GNU General Public License (GPL)](https://www.gnu.org/licenses/licenses.html#GPL).

### Mailing List

[Subscribe](https://lists.openpreservation.org/listinfo/jhove) to the JHOVE mailing list.
