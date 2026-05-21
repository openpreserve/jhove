---
title: Using Third-Party Modules
nav_title: Third-party
nav_order: 14
section: modules
parent: Modules
summary: Requirements and examples for modules written outside HUL.
layout: page
---

# Using third-party modules with JHOVE

Adding third-party modules to JHOVE is a simple matter if they follow certain requirements. This page describes those requirements, and provides links to modules that have been written outside HUL.

A third-party module should be packaged as a jar file containing only the code for the module (not the classes of JHOVE itself). Multiple modules, if related, may be put in the same jar. The jar should include source code as well as classes. The license terms should be indicated in the source files or an accompanying text file. Additional jars may be required and should be documented. The jar(s) then need only be added to the classpath to be used with it.

To avoid confusion with HUL-created code, please do not put third-party modules in a package which begins with `edu.harvard.hul.ois`.

Individuals or institutions wishing to donate locally-developed modules for redistribution to the JHOVE user community should submit a statement of their intention to `jhove-support at harvard.edu`.

See also [Writing a Module](/documentation/dev-module/).

## Third-party modules available for download

Please note that these modules are **unsupported** by HUL, and that you use them at your own risk.

- [MP3 module](mp3/)
- [ZIP and GZIP modules](zip/)
- [EPUB module](epub/)
