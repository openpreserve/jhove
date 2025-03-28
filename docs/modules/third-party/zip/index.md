---
title: Zip & Gzip Modules
layout: page
---

# ZIP and GZIP modules for JHOVE (draft, 4-April-2007)

**Module:** ZIP-ptc  
**Version:** 1.1h  
**Author:** Michael C. Maggio  
**License:** GNU Lesser General Public License  
**Class:** org.portico.tool.jhove\_1\_1.PTC\_ZipModule\_1\_1  
**Requires:** Command-line unzip from PKWARE for Unix. **Runs only on Unix**.  
**File types:** ZIP-compressed files.  
**Parameters:** The module parameter, if provided, is used as the path to the command line `unzip` command. The default is `/usr/local/bin/unzip` .  
   
   
**Module:** GZIP-ptc  
**Version:** 1.1h  
**Author:** Michael C. Maggio  
**License:** GNU Lesser General Public License  
**Class:** org.portico.tool.jhove\_1\_1.PTC\_GzipModule\_1\_1  
**Requires:** Command-line gzip. **Runs only on Unix**.  
**File types:** GZIP-compressed files.  
**Parameters:** The module parameter, if provided, is used as the path to the command line `gzip` command. The default is `/usr/bin/gzip` .  
**Notes:**The JAR includes both the ZIP and GZIP modules from Portico.

Please note that these modules are **unsupported** by HUL, and that you use them at your own risk.

{: .btn.btn-primary.btn-lg.pad}
[Download Zip Module](ZipModule.jar)
