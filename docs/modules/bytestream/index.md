---
title: Bytestream Module
layout: page
---

# BYTESTREAM Module

{: #introduction .section-heading}
## 1 Introduction

The BYTESTREAM module recognizes and validates arbitrary bytestreams. This module exists merely to provide a default format during identification operations.

The module is invoked by the:

{: .blockquote}
```
jhove ... -m bytestream ...
```

command line option.

{: #coverage .section-heading}
## 2 Coverage

There are no BYTESTREAM profiles.

{: #well-formedness .section-heading}
## 3 Well-Formedness

The following criteria must be met by a bytestream for JHOVE to consider it well-formed:

- All bytestreams are well-formed

{: #validity .section-heading}
## 4 Validity

The following criteria must be met by a bytestream for JHOVE to consider it valid:

- The bytestream is well-formed

{: #repinfo .section-heading}
## 5 Representation Information

The MIME type is reported as: `application/octet-stream`

The module does not define any additional representation information beyond the standard properties.
