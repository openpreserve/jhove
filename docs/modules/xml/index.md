---
title: XML-hul Module
nav_title: XML
nav_order: 13
section: modules
parent: Modules
summary: Support for XML documents.
layout: page
---

# XML-hul Module

{: #introduction .section-heading}
## 1 Introduction

The XML-hul module recognizes and validates the XML (Extensible Markup Language) format \[[XML](/references#xml)\].

The module can be invoked with the following command-line options:

 > ```bash
 > jhove ... -m XML-hul [-x sax-class] ...
 > ```

### XML Parser Options

The XML-hul module can use any XML parser that conforms to the [SAX2](http://www.saxproject.org) interfaces. Note that if the optional SAX2 LexicalHandler interface isn't supported by the parser, JHOVE will only be able to report a restricted set of representation information.

The actual parser used is either:

1. The parser specified by the `-x sax-class` command-line option (whose class file _must_ be found on the CLASSPATH at the time of execution);
2. The value of the `edu.harvard.hul.ois.jhove.saxClass` property in the `${user.home}/jhove/jhove.properties` Java properties file, where `${user.home}` is the standard Java `user.home` property; or
3. The default parser of your Java Runtime Environment (JRE).

For example, if you would like to use the latest [Apache Xerces](http://xerces.apache.org/), you would need to specify sax-class as `org.apache.xerces.parsers.SAXParser`.

### Module Configuration Options

This module can be [configured](/getting-started/config/) with the following parameters:

**schema=_schema-URL;local-schema-path_**

Specifies the local schema file to use for validation in place of any occurrences of the external schema in an XML document. Using a local file is typically faster and more reliable than retrieving files over a network such as the internet, and is recommended when processing large volumes of XML. This parameter can be declared as many times as necessary.

Example: `schema=http://example.com/schema.xsd;C:\schemas\example.com\schema.xsd`

**withtextmd=_true_**

Indicates that [textMD](/references#textmd) metadata should be included as part of a document's representation information.

{: #coverage .section-heading}
## 2 Coverage

The XML-hul module recognizes and validates the following public profiles:

- XML 1.0 \[[XML](/references#xml)\]

{: #well-formedness .section-heading}
## 3 Well-Formedness

JHOVE uses the criteria for XML well-formedness defined by \[[XML](/references#xml)\].

{: #validity .section-heading}
## 4 Validity

JHOVE uses the criteria for XML validity defined by \[[XML](/references#xml)\].

Note that the concept of validity applies only to XML files that explicitly reference a DTD or XML Schema. JHOVE can determine if either of these conditions are met and if so, it will automatically invoke the SAX2 parser in a validating mode. Otherwise, the parser is invoked in a manner that only checks for well-formedness.

{: #repinfo .section-heading}
## 5 Representation Information

The MIME type is reported as: text/xml

In addition to the standard JHOVE [representation information](/documentation#repinfo), the following XML-specific properties are reported:

- Property "XMLMetadata" of type PROPERTY and arity LIST
  - Property "Version" of type STRING and arity SCALAR
  - Property "Encoding" of type STRING and arity SCALAR
  - Property "Standalone" of type BOOLEAN and arity SCALAR
  - Property "DTD" of type PROPERTY and arity LIST (if a DTD is specified)
    - Property "PublicID" of type STRING and arity SCALAR
    - Property "SystemID" of type STRING and arity SCALAR
    - Property "InternalSubset" of type BOOLEAN and arity SCALAR
  - Property "Schemas" of type PROPERTY and arity LIST (if schemas are specified)
    - Property "Schema" of type PROPERTY and arity ARRAY
      - Property "NamespaceURI" of type STRING and arity SCALAR
      - Property "SchemaLocation" of type STRING and arity SCALAR
  - Property "Root" of type STRING and arity SCALAR
  - Property "Namespaces" of type PROPERTY and arity LIST
    - Property "Namespace" of type PROPERTY and arity ARRAY
      - Property "Prefix" of type STRING and arity SCALAR
      - Property "URI" of type STRING and arity SCALAR
  - Property "Notations" of type PROPERTY and arity LIST (if there are any)
    - Property "Notation" of type PROPERTY and arity SCALAR
      - Property "Name" of type STRING and arity SCALAR
      - Property "PublicID" of type STRING and arity SCALAR (if non-null)
      - Property "SystemID" of type STRING and arity SCALAR (if non-null)
  - Property "CharacterReferences" of type SCALAR and arity LIST
  - Property "Entities" of type PROPERTY and arity LIST (if there are any)
    - Property "Entity" of type PROPERTY and arity SCALAR
      - Property "Name" of type STRING and arity SCALAR
      - Property "Type" of type STRING and arity SCALAR  
        must be: "Internal", "External parsed", or "External unparsed"
      - Property "Value" of type STRING and arity SCALAR (if internal)
      - Property "PublicID" of type STRING and arity SCALAR (if external and non-null)
      - Property "SystemID" of type STRING and arity SCALAR (if external and non-null)
      - Property "Notation" of type STRING and arity SCALAR (if unparsed)
  - Property "ProcessingInstructions" of type PROPERTY and arity LIST (if there are any)
    - Property "ProcessingInstruction" of type PROPERTY and arity SCALAR
      - Property "Target" of type STRING and arity SCALAR
      - Property "Data" of type STRING and arity SCALAR
  - Property "Comments" of type PROPERTY and arity LIST (if there are any)
    - Property "Comment" of type STRING and arity SCALAR
  - Property "TextMDMetadata" of type TextMDMetadata and arity SCALAR (if configured)

Note that the notations and entities reported above are only those that appear in the XML file, not all of those that are defined in the DTD or XML Schema associated with the file.

{: #additional-module-properties .section-heading}
## 6 Additional Module Properties

- Nominal file extension: .xml
